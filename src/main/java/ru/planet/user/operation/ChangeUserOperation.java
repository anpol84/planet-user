package ru.planet.user.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.common.exception.BusinessException;
import ru.planet.user.helper.mapper.UserMapper;
import ru.planet.hotel.model.ChangeUserRequest;
import ru.planet.user.repository.UserRepository;
import ru.tinkoff.kora.common.Component;
import ru.tinkoff.kora.database.jdbc.RuntimeSqlException;

import static ru.planet.user.helper.DuplicateExceptionValidator.validateDuplicatePositionException;

@Component
@RequiredArgsConstructor
public class ChangeUserOperation {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public void activate(ChangeUserRequest request, long id) {
        try {
            long updatedCount = updateUser(request, id);
            validateUpdateCount(updatedCount);
        } catch (RuntimeSqlException e) {
            handleSqlException(e);
        }
    }

    private long updateUser(ChangeUserRequest request, long id) {
        return userRepository.updateUser(userMapper.toUserFromUpdate(request), id).value();
    }

    private void validateUpdateCount(long updatedCount) {
        if (updatedCount == 0) {
            throw new BusinessException("Такого пользователя не существует");
        }
    }

    private void handleSqlException(RuntimeSqlException e) {
        if (validateDuplicatePositionException(e.getCause())) {
            throw new BusinessException("Пользователь с таким логином уже существует");
        }
        throw e;
    }
}
