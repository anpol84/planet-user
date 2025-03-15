package ru.planet.hotel.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.auth.helper.JwtService;
import ru.planet.hotel.helper.mapper.HotelMapper;
import ru.planet.hotel.model.GetFilteredHotelsResponse;
import ru.planet.hotel.model.GetHotelResponse;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.common.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetFilteredHotelsOperation {
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final JwtService jwtService;

    public GetFilteredHotelsResponse activate(String city, double minPrice, String token) {
        var hotels = hotelRepository.getHotelsWithFilter(city, minPrice);
        boolean isError = false;
        if (hotels == null || hotels.isEmpty()) {
            hotels = hotelRepository.getBestHotels();
            isError = true;
        }
        var claims = token == null ? null : jwtService.getClaims(token);
        List<GetHotelResponse> finalHotels = new ArrayList<>();
        for (var hotel : hotels) {
            var hotelViews = hotelRepository.getHotelsView(hotel.id());
            var hotelTypes = hotelRepository.getHotelType(hotel.id());
            var hotelPeople = hotelRepository.getHotelPeople(hotel.id());
            var isFavourite = claims != null && hotelRepository.validateFavouriteHotel(hotel.id(), Long.valueOf(String.valueOf(claims.get("user_id"))));
            var hotelWithExtensions = hotelMapper.buildGetHotelResponse(hotel, hotelViews,
                    hotelTypes, hotelPeople, isFavourite);
            finalHotels.add(hotelWithExtensions);
        }
        return new GetFilteredHotelsResponse(isError, finalHotels);
    }
}
