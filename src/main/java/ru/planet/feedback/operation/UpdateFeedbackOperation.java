package ru.planet.feedback.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.auth.helper.ClaimField;
import ru.planet.auth.helper.JwtService;
import ru.planet.common.exception.BusinessException;
import ru.planet.feedback.helper.FeedbackMapper;
import ru.planet.feedback.repository.FeedbackRepository;
import ru.planet.hotel.model.UpdateFeedbackRequest;
import ru.tinkoff.kora.common.Component;

import static java.util.concurrent.CompletableFuture.runAsync;

@Component
@RequiredArgsConstructor
public class UpdateFeedbackOperation {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final JwtService jwtService;

    public void activate(UpdateFeedbackRequest request, Long feedbackId, String token) {
        var claims = jwtService.getClaims(token);
        if (!(String.valueOf(claims.get(ClaimField.ROLE)).contains("ROLE_ADMIN"))) {
            var userId = Long.valueOf(String.valueOf(claims.get(ClaimField.USER_ID)));
            var userByFeedback = feedbackRepository.getUserByFeedback(feedbackId);
            if (!userId.equals(userByFeedback)) {
                throw new BusinessException("Вы не можете менять не свой отзыв");
            }
        }
        var hotelId = feedbackRepository.updateFeedback(feedbackMapper.toUpdateFeedbackDto(request), feedbackId);
        if (hotelId == null){
            throw new BusinessException("Такого отзыва не существует");
        }
        runAsync(() -> feedbackRepository.updateRate(hotelId));
    }
}
