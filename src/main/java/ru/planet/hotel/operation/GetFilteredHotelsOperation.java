package ru.planet.hotel.operation;

import lombok.RequiredArgsConstructor;
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

    public GetFilteredHotelsResponse activate(String city, double minPrice) {
        var hotels = hotelRepository.getHotelsWithFilter(city, minPrice);
        if (hotels.isEmpty()) {
            return new GetFilteredHotelsResponse();
        }
        List<GetHotelResponse> finalHotels = new ArrayList<>();
        for (var hotel : hotels) {
            var hotelViews = hotelRepository.getHotelsView(hotel.id());
            var hotelTypes = hotelRepository.getHotelType(hotel.id());
            var hotelPeople = hotelRepository.getHotelPeople(hotel.id());
            var hotelWithExtensions = hotelMapper.buildGetHotelResponse(hotel, hotelViews,
                    hotelTypes, hotelPeople);
            finalHotels.add(hotelWithExtensions);
        }
        return new GetFilteredHotelsResponse(finalHotels);
    }
}
