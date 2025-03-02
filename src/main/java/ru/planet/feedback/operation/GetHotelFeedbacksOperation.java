package ru.planet.feedback.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.common.exception.BusinessException;
import ru.planet.feedback.helper.FeedbackMapper;
import ru.planet.feedback.repository.FeedbackRepository;
import ru.planet.hotel.model.Feedback;
import ru.planet.hotel.model.GetHotelFeedbacksResponse;
import ru.planet.hotel.repository.HotelRepository;
import ru.planet.user.repository.UserRepository;
import ru.tinkoff.kora.common.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetHotelFeedbacksOperation {

    private final FeedbackRepository feedbackRepository;
    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;
    private final FeedbackMapper feedbackMapper;

    public GetHotelFeedbacksResponse activate(Long hotelId){
        if (!hotelRepository.validateHotel(hotelId)) {
            throw new BusinessException("Такого отеля не существует");
        }
        var feedbacksDto = feedbackRepository.getHotelFeedbacks(hotelId);
        List<Feedback> feedbacks = new ArrayList<>();
        feedbacksDto.forEach((item) -> {
            String userLogin = userRepository.getLoginById(item.userId());
            feedbacks.add(feedbackMapper.toFeedback(item, userLogin));
        });
        return new GetHotelFeedbacksResponse(feedbacks);
    }
}
