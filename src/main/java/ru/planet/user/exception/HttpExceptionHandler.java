package ru.planet.user.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.planet.user.model.UserErrorResponse;
import ru.planet.user.service.gRPC.AuthGrpcService;
import ru.tinkoff.kora.common.Component;
import ru.tinkoff.kora.common.Context;
import ru.tinkoff.kora.common.Tag;
import ru.tinkoff.kora.generated.grpc.PlanetAuth;
import ru.tinkoff.kora.http.common.body.HttpBody;
import ru.tinkoff.kora.http.server.common.*;
import ru.tinkoff.kora.json.common.JsonWriter;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

@Tag(HttpServerModule.class)
@Component
@Slf4j
@RequiredArgsConstructor
public final class HttpExceptionHandler implements HttpServerInterceptor {

    private static final String CREATE_USER_PATH = "/api/users";
    private static final String CREATE_USER_METHOD = "POST";

    private final JsonWriter<UserErrorResponse> errorJsonWriter;
    private final AuthGrpcService authGrpcService;

    @Override
    public CompletionStage<HttpServerResponse> intercept(Context context, HttpServerRequest request, InterceptChain chain)
            throws Exception {
        System.out.println(request.headers().getFirst("token"));
        if (!request.method().equals(CREATE_USER_METHOD) && !request.path().equals(CREATE_USER_PATH)) {
            if (!authGrpcService.checkToken(PlanetAuth.CheckTokenRequest.newBuilder()
                            .setToken(request.headers().getFirst("token"))
                    .build()).getIsValid()) {
                var body = HttpBody.json(errorJsonWriter.toByteArrayUnchecked(new UserErrorResponse("token is not valid")));
                return CompletableFuture.completedFuture(HttpServerResponse.of(403, body));
            }
        }
        return chain.process(context, request).exceptionally(e -> {
            if (e instanceof HttpServerResponseException ex) {
                return ex;
            }

            var body = HttpBody.json(errorJsonWriter.toByteArrayUnchecked(new UserErrorResponse(e.getMessage())));
            if (e instanceof BusinessException) {
                log.warn("Request '{} {}' failed due to {}", request.method(), request.path(), e.getMessage());
                return HttpServerResponse.of(422, body);
            } else {
                log.error("Request '{} {}' failed", request.method(), request.path(), e);
                return HttpServerResponse.of(500, body);
            }
        });
    }
}
