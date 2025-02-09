package ru.planet.user.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.auth.cache.UserCache;
import ru.planet.common.exception.BusinessException;
import ru.planet.user.repository.UserRepository;
import ru.tinkoff.kora.common.Component;

@Component
@RequiredArgsConstructor
public class DeleteUserOperation {
    private final UserRepository userRepository;
    private final UserCache userCache;

    public void activate(long id) {
        userRepository.deleteUserRoles(id);
        String login = userRepository.getLoginById(id);
        if (userRepository.deleteUser(id).value() == 0) {
            throw new BusinessException("Такого пользователя не существует");
        }
        userCache.invalidate(login);
    }
}
