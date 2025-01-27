package ru.planet.user.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.user.exception.BusinessException;
import ru.planet.user.repository.UserRepository;
import ru.planet.user.service.gRPC.AuthGrpcService;
import ru.tinkoff.kora.common.Component;
import ru.tinkoff.kora.generated.grpc.PlanetAuth;

@Component
@RequiredArgsConstructor
public class DeleteUserOperation {
    private final UserRepository userRepository;
    private final AuthGrpcService authGrpcService;

    public void activate(long id) {
        userRepository.deleteUserRoles(id);
        String login = userRepository.getLoginById(id);
        if (userRepository.deleteUser(id).value() == 0) {
            throw new BusinessException("Такого пользователя не существует");
        }
        authGrpcService.invalidateUserCache(PlanetAuth.InvalidateUserCacheRequest
                .newBuilder()
                .setLogin(login)
                .build());
    }
}
