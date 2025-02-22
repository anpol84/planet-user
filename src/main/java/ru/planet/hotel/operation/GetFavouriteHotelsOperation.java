package ru.planet.hotel.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.hotel.helper.mapper.HotelMapper;
import ru.planet.hotel.model.GetFavouriteHotelsResponse;
import ru.planet.hotel.model.HotelWithoutExtensions;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.common.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetFavouriteHotelsOperation {
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    public GetFavouriteHotelsResponse activate(Long userId) {
        var hotels = hotelRepository.getFavouriteHotels(userId);
        List<HotelWithoutExtensions> hotelsWithoutExtensions = new ArrayList<>();
        for (var hotel : hotels) {
            hotelsWithoutExtensions.add(hotelMapper.buildHotel(hotel));
        }
        return new GetFavouriteHotelsResponse(hotelsWithoutExtensions);
    }
}
