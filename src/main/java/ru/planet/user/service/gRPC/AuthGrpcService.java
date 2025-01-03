package ru.planet.user.service.gRPC;

import lombok.RequiredArgsConstructor;
import ru.tinkoff.kora.common.Component;
import ru.tinkoff.kora.generated.grpc.AuthServiceGrpc.AuthServiceBlockingStub;
import ru.tinkoff.kora.generated.grpc.PlanetAuth.CheckTokenWithIdResponse;
import ru.tinkoff.kora.generated.grpc.PlanetAuth.CheckTokenWithIdRequest;
import ru.tinkoff.kora.generated.grpc.PlanetAuth.LoginResponse;
import ru.tinkoff.kora.generated.grpc.PlanetAuth.LoginRequest;

@Component
@RequiredArgsConstructor
public class AuthGrpcService {
    private final AuthServiceBlockingStub authServiceBlockingStub;

    public CheckTokenWithIdResponse checkTokenWithId(CheckTokenWithIdRequest request) {
        return authServiceBlockingStub.checkTokenWithId(request);
    }

    public LoginResponse login(LoginRequest request) {
        return authServiceBlockingStub.login(request);
    }
}
