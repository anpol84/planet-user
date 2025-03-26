package ru.planet.user.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.auth.cache.UserCache;
import ru.planet.common.exception.BusinessException;
import ru.planet.feedback.repository.FeedbackRepository;
import ru.planet.user.repository.UserRepository;
import ru.tinkoff.kora.common.Component;

import static java.util.concurrent.CompletableFuture.runAsync;

@Component
@RequiredArgsConstructor
public class DeleteUserOperation {
    private final UserRepository userRepository;
    private final UserCache userCache;
    private final FeedbackRepository feedbackRepository;

    public void activate(long id) {
        userRepository.deleteUserRoles(id);
        String login = userRepository.getLoginById(id);
        userRepository.getJdbcConnectionFactory().inTx(() -> processDeleting(id));
        userCache.invalidate(login);
    }

    private void processDeleting(Long id) {
        userRepository.deleteUserFavourites(id);
        var hotels = feedbackRepository.getHotelsByUserId(id);
        hotels.forEach((hotel) -> runAsync(() -> feedbackRepository.updateRate(hotel)));
        feedbackRepository.deleteFeedbacksByUserId(id);
        if (userRepository.deleteUser(id).value() == 0) {
            throw new BusinessException("Такого пользователя не существует");
        }
    }
}
