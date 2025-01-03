package ru.planet.user.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.user.exception.BusinessException;
import ru.planet.user.repository.UserRepository;
import ru.tinkoff.kora.common.Component;

@Component
@RequiredArgsConstructor
public class DeleteUserOperation {
    private final UserRepository userRepository;

    public void activate(long id) {
        userRepository.deleteUserRoles(id);
        if (userRepository.deleteUser(id).value() == 0) {
            throw new BusinessException("Такого пользователя не существует");
        }
    }
}
