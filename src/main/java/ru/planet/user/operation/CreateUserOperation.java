package ru.planet.user.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.user.configuration.properties.BcryptProperties;
import ru.planet.common.exception.BusinessException;
import ru.planet.user.helper.mapper.UserMapper;
import ru.planet.hotel.model.CreateUserRequest;
import ru.planet.user.repository.UserRepository;
import ru.planet.user.service.AuthService;
import ru.tinkoff.kora.common.Component;
import ru.tinkoff.kora.database.jdbc.RuntimeSqlException;

import static ru.planet.common.DuplicateExceptionValidator.validateDuplicatePositionException;

@Component
@RequiredArgsConstructor
public class CreateUserOperation {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final BcryptProperties bcryptProperties;

    public String activate(CreateUserRequest request) {
        long id = insertUser(request);
        userRepository.insertUserRole(id);
        return loginUser(request);
    }

    private long insertUser(CreateUserRequest request) {
        try {
            return userRepository.insertUser(userMapper.toUserFromCreate(request, bcryptProperties));
        } catch (RuntimeSqlException e) {
            handleSqlException(e);
            throw e;
        }
    }

    private void handleSqlException(RuntimeSqlException e) {
        if (validateDuplicatePositionException(e.getCause())) {
            throw new BusinessException("Пользователь с таким логином уже существует");
        }
    }

    private String loginUser(CreateUserRequest request) {
        return authService.login(request.login(), request.password());
    }
}
