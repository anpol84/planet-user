package ru.planet.hotel.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.common.exception.BusinessException;
import ru.planet.hotel.helper.mapper.HotelMapper;
import ru.planet.hotel.model.GetFavouriteHotelsResponse;
import ru.planet.hotel.model.HotelWithoutExtensions;
import ru.planet.hotel.repository.HotelRepository;
import ru.planet.user.repository.UserRepository;
import ru.tinkoff.kora.common.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetFavouriteHotelsOperation {
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final UserRepository userRepository;

    public GetFavouriteHotelsResponse activate(Long userId) {
        if (userRepository.getUser(userId) == null) {
            throw new BusinessException("Такого пользователя не существует");
        }
        var hotels = hotelRepository.getFavouriteHotels(userId);
        List<HotelWithoutExtensions> hotelsWithoutExtensions = new ArrayList<>();
        for (var hotel : hotels) {
            hotelsWithoutExtensions.add(hotelMapper.buildHotel(hotel, true));
        }
        return new GetFavouriteHotelsResponse(hotelsWithoutExtensions);
    }
}
