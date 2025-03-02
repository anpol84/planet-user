package ru.planet.feedback.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.common.exception.BusinessException;
import ru.planet.feedback.helper.FeedbackMapper;
import ru.planet.feedback.repository.FeedbackRepository;
import ru.planet.hotel.model.GetFeedbackResponse;
import ru.tinkoff.kora.common.Component;

@Component
@RequiredArgsConstructor
public class GetFeedbackOperation {
    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;

    public GetFeedbackResponse activate(Long feedbackId) {
        var feedback = feedbackRepository.getFeedbackById(feedbackId);
        if (feedback == null) {
            throw new BusinessException("Такого отзыва не существует");
        }
        return feedbackMapper.toGetFeedbackResponse(feedback);
    }
}
