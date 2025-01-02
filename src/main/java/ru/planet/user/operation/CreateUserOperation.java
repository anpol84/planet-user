package ru.planet.user.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.user.configuration.properties.BcryptProperties;
import ru.planet.user.helper.mapper.UserMapper;
import ru.planet.user.model.CreateUserRequest;
import ru.planet.user.repository.UserRepository;
import ru.planet.user.service.gRPC.AuthGrpcService;
import ru.tinkoff.kora.common.Component;
import ru.tinkoff.kora.generated.grpc.PlanetAuth;

@Component
@RequiredArgsConstructor
public class CreateUserOperation {

    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final AuthGrpcService authGrpcService;
    private final BcryptProperties bcryptProperties;

    public String activate(CreateUserRequest request) {

        long id = userRepository.insertUser(userMapper.toUserFromCreate(request, bcryptProperties));
        userRepository.insertUserRole(id);
        return authGrpcService.login(PlanetAuth.LoginRequest.newBuilder()
                        .setLogin(request.login())
                        .setPassword(request.password())
                .build()).getToken();
    }
}
