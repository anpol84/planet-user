package ru.planet.gpt.operation;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ru.planet.auth.helper.JwtService;
import ru.planet.gpt.cache.QueryCache;
import ru.planet.gpt.client.GigaChatDialog;
import ru.planet.gpt.dto.GptHotel;
import ru.planet.gpt.dto.GptResponse;
import ru.planet.hotel.dto.GetHotel;
import ru.planet.hotel.dto.RoomPeople;
import ru.planet.hotel.dto.RoomType;
import ru.planet.hotel.dto.RoomView;
import ru.planet.hotel.helper.mapper.HotelMapper;
import ru.planet.hotel.model.AskGptRequest;
import ru.planet.hotel.model.AskGptResponse;
import ru.planet.hotel.model.HotelWithoutExtensions;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.cache.LoadableCache;
import ru.tinkoff.kora.common.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AskGptOperation {
    private final LoadableCache<String, String> queryCache;
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final JwtService jwtService;

    public AskGptResponse activate(String token, AskGptRequest request) {
        ObjectMapper objectMapper = new ObjectMapper();
        var claims = token == null ? null : jwtService.getClaims(token);
        List<GetHotel> hotels;
        GptHotel gptHotelTemp = null;
        List<GetHotel> filteredHotels;
        boolean isError = false;
        try {
            String response = queryCache.get(request.query());
            GptResponse gptResponse = objectMapper.readValue(response, GptResponse.class);
            gptHotelTemp = objectMapper.readValue(gptResponse.choices().get(0).message().content(), GptHotel.class);
            hotels = hotelRepository.getHotelsForGpt(gptHotelTemp);
        } catch (Exception e) {
            hotels = new ArrayList<>();
            isError = true;
        }
        var gptHotel = gptHotelTemp;
        if (!hotels.isEmpty()) {

            if (!gptHotel.name().isEmpty()) {
                hotels = hotels.stream()
                        .filter(hotel -> hotel.name().equals(gptHotel.name()))
                        .toList();
            }
            if (!gptHotel.city().isEmpty()) {
                hotels = hotels.stream()
                        .filter(hotel -> hotel.city().equals(gptHotel.city()))
                        .toList();
            }


            hotels = hotels.stream()
                    .filter(hotel -> (new HashSet<>(hotel.additions()).containsAll(gptHotel.additions()) || gptHotel.additions().isEmpty())
                            && (new HashSet<>(hotel.positions()).containsAll(gptHotel.positions()) || gptHotel.positions().isEmpty()))
                    .toList();

            filteredHotels = hotels.stream()
                    .filter(hotel -> {
                        var roomViews = hotelRepository.getHotelsView(hotel.id());
                        boolean containsRoomView = gptHotel.roomView().isEmpty();
                        for (var roomView : roomViews) {
                            Optional<RoomView> view = gptHotel.roomView().stream()
                                    .filter(item -> item.type().equals(roomView.type()))
                                    .findAny();
                            if (view.isPresent() && view.get().price() >= roomView.price()) {
                                containsRoomView = true;
                                break;
                            }
                        }
                        ;
                        if (!containsRoomView) {
                            return false;
                        }
                        var roomTypes = hotelRepository.getHotelType(hotel.id());
                        boolean containsRoomTypes = gptHotel.roomType().isEmpty();
                        for (var roomType : roomTypes) {
                            Optional<RoomType> type = gptHotel.roomType().stream()
                                    .filter(item -> item.type().equals(roomType.type()))
                                    .findAny();
                            if (type.isPresent() && type.get().price() >= roomType.price()) {
                                containsRoomTypes = true;
                                break;
                            }
                        }
                        ;
                        if (!containsRoomTypes) {
                            return false;
                        }
                        var roomPeople = hotelRepository.getHotelPeople(hotel.id());
                        boolean containsRoomPeople = gptHotel.roomPeople().isEmpty();
                        for (var roomHuman : roomPeople) {
                            Optional<RoomPeople> people = gptHotel.roomPeople().stream()
                                    .filter(item -> item.type().equals(roomHuman.type()))
                                    .findAny();
                            if (people.isPresent() && people.get().price() >= roomHuman.price()) {
                                containsRoomPeople = true;
                                break;
                            }
                        }
                        ;
                        return containsRoomPeople;
                    })
                    .toList();
        } else {
            filteredHotels = new ArrayList<>();
            isError = true;
        }
        if (filteredHotels.isEmpty()) {
            filteredHotels = hotelRepository.getBestHotels();
            isError = true;
        }
        List<HotelWithoutExtensions> hotelsWithoutExtensions = new ArrayList<>();
        for (var hotel : filteredHotels) {
            var isFavourite = claims != null && hotelRepository.validateFavouriteHotel(hotel.id(), Long.valueOf(String.valueOf(claims.get("user_id"))));
            hotelsWithoutExtensions.add(hotelMapper.buildHotel(hotel, isFavourite));
        }
        return new AskGptResponse(isError, hotelsWithoutExtensions);
    }

}
