package ru.planet.user.helper.interceptor;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import ru.tinkoff.kora.application.graph.GraphInterceptor;
import ru.tinkoff.kora.common.Component;
import ru.tinkoff.kora.generated.grpc.AuthServiceGrpc;

@Component
@RequiredArgsConstructor
public class AuthGrpcServiceInterceptor implements GraphInterceptor<AuthServiceGrpc.AuthServiceBlockingStub> {
    @Override
    public AuthServiceGrpc.AuthServiceBlockingStub init(AuthServiceGrpc.AuthServiceBlockingStub value) {
        return value.withChannel(ManagedChannelBuilder.forAddress("localhost", 9090).usePlaintext().build());
    }

    @Override
    public AuthServiceGrpc.AuthServiceBlockingStub release(AuthServiceGrpc.AuthServiceBlockingStub value) {
        return value;
    }
}
