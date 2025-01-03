package ru.planet.user.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.user.configuration.properties.BcryptProperties;
import ru.planet.user.exception.BusinessException;
import ru.planet.user.helper.mapper.UserMapper;
import ru.planet.user.model.CreateUserRequest;
import ru.planet.user.repository.UserRepository;
import ru.planet.user.service.gRPC.AuthGrpcService;
import ru.tinkoff.kora.common.Component;
import ru.tinkoff.kora.database.jdbc.RuntimeSqlException;
import ru.tinkoff.kora.generated.grpc.PlanetAuth;

import static ru.planet.user.helper.DuplicateExceptionValidator.validateDuplicatePositionException;

@Component
@RequiredArgsConstructor
public class CreateUserOperation {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AuthGrpcService authGrpcService;
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
        return authGrpcService.login(PlanetAuth.LoginRequest.newBuilder()
                .setLogin(request.login())
                .setPassword(request.password())
                .build()).getToken();
    }
}
