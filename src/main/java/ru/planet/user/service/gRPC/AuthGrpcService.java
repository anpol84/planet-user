package ru.planet.user.service.gRPC;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.kora.common.Component;
import ru.tinkoff.kora.generated.grpc.AuthServiceGrpc.AuthServiceBlockingStub;
import ru.tinkoff.kora.generated.grpc.PlanetAuth.InvalidateUserCacheRequest;
import ru.tinkoff.kora.generated.grpc.PlanetAuth.CheckAdminResponse;
import ru.tinkoff.kora.generated.grpc.PlanetAuth.CheckAdminRequest;
import ru.tinkoff.kora.generated.grpc.PlanetAuth.CheckTokenWithIdResponse;
import ru.tinkoff.kora.generated.grpc.PlanetAuth.CheckTokenWithIdRequest;
import ru.tinkoff.kora.generated.grpc.PlanetAuth.LoginResponse;
import ru.tinkoff.kora.generated.grpc.PlanetAuth.LoginRequest;
import ru.tinkoff.kora.resilient.retry.annotation.Retry;

@Component
@RequiredArgsConstructor
public class AuthGrpcService {
    private final AuthServiceBlockingStub authServiceBlockingStub;

    @Retry("default")
    public CheckTokenWithIdResponse checkTokenWithId(CheckTokenWithIdRequest request) {
        return authServiceBlockingStub.checkTokenWithId(request);
    }

    @Retry("default")
    public LoginResponse login(LoginRequest request) {
        return authServiceBlockingStub.login(request);
    }

    @Retry("default")
    public CheckAdminResponse checkAdminResponse(CheckAdminRequest request) {
        return authServiceBlockingStub.checkAdmin(request);
    }

    @Retry("default")
    public void invalidateUserCache(InvalidateUserCacheRequest request) {
        authServiceBlockingStub.invalidateUserCache(request);
    }
}
