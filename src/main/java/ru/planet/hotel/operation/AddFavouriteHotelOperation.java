package ru.planet.hotel.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.common.Component;

@Component
@RequiredArgsConstructor
public class AddFavouriteHotelOperation {

    private final HotelRepository hotelRepository;

    public void activate(Long userId, Long hotelId) {
        hotelRepository.addFavouriteHotel(userId, hotelId);
    }
}
