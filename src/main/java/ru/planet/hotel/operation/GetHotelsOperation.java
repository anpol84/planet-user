package ru.planet.hotel.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.hotel.helper.mapper.HotelMapper;
import ru.planet.hotel.model.GetHotelsResponse;
import ru.planet.hotel.model.HotelWithoutExtensions;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.common.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetHotelsOperation {
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    public GetHotelsResponse activate() {

       var hotels = hotelRepository.getHotels();
       List<HotelWithoutExtensions> hotelsWithoutExtensions = new ArrayList<>();
       for (var hotel : hotels) {
           hotelsWithoutExtensions.add(hotelMapper.buildHotel(hotel));
       }
       return new GetHotelsResponse(hotelsWithoutExtensions);
    }
}
