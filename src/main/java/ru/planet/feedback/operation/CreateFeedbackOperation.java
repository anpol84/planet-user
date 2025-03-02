package ru.planet.feedback.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.auth.helper.ClaimField;
import ru.planet.auth.helper.JwtService;
import ru.planet.common.exception.BusinessException;
import ru.planet.feedback.helper.FeedbackMapper;
import ru.planet.feedback.repository.FeedbackRepository;
import ru.planet.hotel.model.CreateFeedbackRequest;
import ru.tinkoff.kora.common.Component;
import ru.tinkoff.kora.database.jdbc.RuntimeSqlException;

import static java.util.concurrent.CompletableFuture.runAsync;
import static ru.planet.common.DuplicateExceptionValidator.validateDuplicatePositionException;
import static ru.planet.common.DuplicateExceptionValidator.validateNotFoundKeyException;

@Component
@RequiredArgsConstructor
public class CreateFeedbackOperation {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackMapper feedbackMapper;
    private final JwtService jwtService;

    public void activate(CreateFeedbackRequest request, String token) {
        var claims = jwtService.getClaims(token);
        var userId = Long.valueOf(String.valueOf(claims.get(ClaimField.USER_ID)));
        if (!userId.equals(request.userId())) {
            throw new BusinessException("Вы не можете писать не свой отзыв");
        }
        try {
            feedbackRepository.createFeedback(feedbackMapper.toCreateFeedbackDto(request));
            runAsync(() -> feedbackRepository.updateRate(request.hotelId()));
        } catch (RuntimeSqlException e) {
            if (validateDuplicatePositionException(e)) {
                throw new BusinessException("Отзыв на данный отель данный пользователем уже написан");
            }
            if (validateNotFoundKeyException(e)) {
                throw new BusinessException("Отеля или пользователя не существует");
            }
            throw e;
        }

    }
}
