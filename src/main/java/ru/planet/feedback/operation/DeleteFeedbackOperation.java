package ru.planet.feedback.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.auth.helper.ClaimField;
import ru.planet.auth.helper.JwtService;
import ru.planet.common.exception.BusinessException;
import ru.planet.feedback.repository.FeedbackRepository;
import ru.tinkoff.kora.common.Component;

import static java.util.concurrent.CompletableFuture.runAsync;

@Component
@RequiredArgsConstructor
public class DeleteFeedbackOperation {
    private final FeedbackRepository feedbackRepository;
    private final JwtService jwtService;

    public void activate(long feedbackId, String token) {
        var claims = jwtService.getClaims(token);
        if (!(String.valueOf(claims.get(ClaimField.ROLE)).contains("ROLE_ADMIN"))) {
            var userId = Long.valueOf(String.valueOf(claims.get(ClaimField.USER_ID)));
            var userByFeedback = feedbackRepository.getUserByFeedback(feedbackId);
            if (!userId.equals(userByFeedback)) {
                throw new BusinessException("Вы не можете удалять не свой отзыв");
            }
        }
        var hotelId = feedbackRepository.deleteFeedback(feedbackId);
        if (hotelId == null){
            throw new BusinessException("Такого отзыва не существует");
        }
        runAsync(() -> feedbackRepository.updateRate(hotelId));
    }

}
