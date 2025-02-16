package ru.planet.hotel.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.common.exception.BusinessException;
import ru.planet.hotel.helper.HotelHelper;
import ru.planet.hotel.helper.mapper.HotelMapper;
import ru.planet.hotel.model.*;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.common.Component;
import ru.tinkoff.kora.database.jdbc.RuntimeSqlException;

import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Stream;

import static ru.planet.common.DuplicateExceptionValidator.validateDuplicatePositionException;

@Component
@RequiredArgsConstructor
public class CreateHotelOperation {

    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final HotelHelper hotelHelper;

    public void activate(CreateHotelRequest request) {
        var hotel = hotelMapper.buildHotel(request);
        var roomViews = hotelHelper.extractRoomViews(request.roomViews());
        var roomTypes = hotelHelper.extractRoomTypes(request.roomTypes());
        var roomPeople = hotelHelper.extractRoomPeople(request.roomPeople());

        double minPrice = hotelHelper.calculateMin(request.roomViews(), request.roomTypes(), request.roomPeople());
        double maxPrice = hotelHelper.calculateMax(request.roomViews(), request.roomTypes(), request.roomPeople());

        hotelRepository.getJdbcConnectionFactory().inTx(() -> {
            try {
                Long hotelId = hotelRepository.saveHotel(hotel, hotel.additions().toString(),
                        hotel.positions().toString(), minPrice, maxPrice);
                roomViews.forEach((room) -> hotelRepository.saveHotelWithRoomView(hotelId, room));
                roomTypes.forEach((room) -> hotelRepository.saveHotelWithRoomType(hotelId, room));
                roomPeople.forEach((room) -> hotelRepository.saveHotelWithRoomPeople(hotelId, room));
            } catch (RuntimeSqlException e) {
                if (validateDuplicatePositionException(e)) {
                    throw new BusinessException("Такой отель уже существует");
                }
                throw e;
            }
        });

    }


}
