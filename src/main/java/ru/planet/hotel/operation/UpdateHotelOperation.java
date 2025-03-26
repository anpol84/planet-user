package ru.planet.hotel.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.common.exception.BusinessException;
import ru.planet.hotel.dto.GetHotel;
import ru.planet.hotel.helper.HotelHelper;
import ru.planet.hotel.model.UpdateHotelRequest;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.common.Component;
import ru.tinkoff.kora.database.jdbc.RuntimeSqlException;

import static java.util.concurrent.CompletableFuture.runAsync;
import static ru.planet.common.DuplicateExceptionValidator.validateDuplicatePositionException;

@Component
@RequiredArgsConstructor
public class UpdateHotelOperation {
    private final HotelRepository hotelRepository;
    private final HotelHelper hotelHelper;

    public void activate(UpdateHotelRequest request, Long hotelId) {
        double minPrice = hotelHelper.calculateMin(request.roomViews(), request.roomTypes(), request.roomPeople());
        var newHotel = GetHotel.builder()
                .id(hotelId)
                .stars(request.stars())
                .city(request.city())
                .name(request.name())
                .imageUrl(request.imageUrl())
                .minPrice(minPrice)
                .build();
        hotelRepository.getJdbcConnectionFactory().inTx(() -> {
            try {
                if (hotelRepository.updateHotel(newHotel, request.additions().toString(),
                        request.positions().toString()).value() == 0) {
                    throw new BusinessException("Такого отеля не существует");
                }
                hotelRepository.deleteHotelViews(newHotel.id());
                hotelRepository.deleteHotelTypes(newHotel.id());
                hotelRepository.deleteHotelPeople(newHotel.id());
                var viewIds = hotelHelper.extractRoomViews(request.roomViews());
                viewIds.forEach(id -> hotelRepository.saveHotelWithRoomView(newHotel.id(), id));

                var typeIds = hotelHelper.extractRoomTypes(request.roomTypes());
                typeIds.forEach(id -> hotelRepository.saveHotelWithRoomType(newHotel.id(), id));

                var peopleIds = hotelHelper.extractRoomPeople(request.roomPeople());
                peopleIds.forEach(id -> hotelRepository.saveHotelWithRoomPeople(newHotel.id(), id));
            } catch (RuntimeSqlException e) {
                if (validateDuplicatePositionException(e)) {
                    throw new BusinessException("Такой отель уже существует");
                }
                throw e;
            }
        });
        runAsync(hotelRepository::deleteUnusedRoomPeople);
        runAsync(hotelRepository::deleteUnusedRoomType);
        runAsync(hotelRepository::deleteUnusedRoomView);
    }
}
