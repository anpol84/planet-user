package ru.planet.hotel.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.common.exception.BusinessException;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.common.Component;

@Component
@RequiredArgsConstructor
public class DeleteFavouriteHotelOperation {
    private final HotelRepository hotelRepository;

    public void activate(Long userId, Long hotelId) {
       if (hotelRepository.deleteFavouriteHotel(userId, hotelId).value() == 0) {
           throw new BusinessException("Отеля или пользователя не существует");
       }
    }
}
