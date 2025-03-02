package ru.planet.hotel.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.common.exception.BusinessException;
import ru.planet.feedback.repository.FeedbackRepository;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.common.Component;

import static java.util.concurrent.CompletableFuture.runAsync;

@Component
@RequiredArgsConstructor
public class DeleteHotelOperation {

    private final HotelRepository hotelRepository;
    private final FeedbackRepository feedbackRepository;

    public void activate(Long id) {
        hotelRepository.getJdbcConnectionFactory().inTx(() -> {
            hotelRepository.deleteHotelViews(id);
            hotelRepository.deleteHotelTypes(id);
            hotelRepository.deleteHotelPeople(id);
            hotelRepository.deleteHotelFavourites(id);
            feedbackRepository.deleteFeedbacksByHotelId(id);
            if (hotelRepository.deleteHotel(id).value() == 0) {
                throw new BusinessException("Такого отеля не существует");
            }
        });

        runAsync(hotelRepository::deleteUnusedRoomPeople);
        runAsync(hotelRepository::deleteUnusedRoomType);
        runAsync(hotelRepository::deleteUnusedRoomView);
    }
}
