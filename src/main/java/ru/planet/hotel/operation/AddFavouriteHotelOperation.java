package ru.planet.hotel.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.common.exception.BusinessException;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.common.Component;
import ru.tinkoff.kora.database.jdbc.RuntimeSqlException;

import static ru.planet.common.DuplicateExceptionValidator.validateDuplicatePositionException;
import static ru.planet.common.DuplicateExceptionValidator.validateNotFoundKeyException;

@Component
@RequiredArgsConstructor
public class AddFavouriteHotelOperation {

    private final HotelRepository hotelRepository;

    public void activate(Long userId, Long hotelId) {
        try {
            hotelRepository.addFavouriteHotel(userId, hotelId);
        } catch (RuntimeSqlException e) {
            if (validateDuplicatePositionException(e)) {
                throw new BusinessException("Этот отель уже добавлен в избранное");
            }
            if (validateNotFoundKeyException(e)) {
                throw new BusinessException("Отеля или пользователя не существует");
            }
            throw e;
        }

    }
}
