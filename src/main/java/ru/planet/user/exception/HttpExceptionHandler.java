package ru.planet.user.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.planet.user.model.UserErrorResponse;
import ru.planet.user.service.gRPC.AuthGrpcService;
import ru.tinkoff.kora.common.Component;
import ru.tinkoff.kora.common.Context;
import ru.tinkoff.kora.common.Tag;
import ru.tinkoff.kora.generated.grpc.PlanetAuth.CheckAdminRequest;
import ru.tinkoff.kora.generated.grpc.PlanetAuth.CheckTokenWithIdRequest;
import ru.tinkoff.kora.http.common.body.HttpBody;
import ru.tinkoff.kora.http.common.header.HttpHeaders;
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
    private static final String GET_USERS_METHOD = "GET";
    private static final String USER_ID_PATH_VARIABLE = "userId";
    private static final String HEADER_TOKEN = "token";
    private static final HttpHeaders CORS_HEADERS = HttpHeaders.of(
            "Access-Control-Allow-Origin", "*",
            "Access-Control-Allow-Headers", "Content-Type, token",
            "Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");

    private final JsonWriter<UserErrorResponse> errorJsonWriter;
    private final AuthGrpcService authGrpcService;

    @Override
    public CompletionStage<HttpServerResponse> intercept(Context context, HttpServerRequest request, InterceptChain chain)
            throws Exception {
        if (isGetUsersRequest(request)) {
            return validateAdminToken(request)
                    .thenCompose(isValid -> {
                        if (!isValid) {
                            return createErrorResponse("token is not valid", 403);
                        }
                        try {
                            return processRequest(context, request, chain);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }
        if (isUserIdPathRequest(request)) {
            return validateTokenWithId(request)
                    .thenCompose(isValid -> {
                        if (!isValid) {
                            return createErrorResponse("token is not valid", 403);
                        }
                        try {
                            return processRequest(context, request, chain);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    });
        }

        return processRequest(context, request, chain);
    }

    private boolean isGetUsersRequest(HttpServerRequest request) {
        return request.method().equals(GET_USERS_METHOD) && request.path().equals(CREATE_USER_PATH);
    }

    private boolean isUserIdPathRequest(HttpServerRequest request) {
        return !request.method().equals(CREATE_USER_METHOD) && !request.path().equals(CREATE_USER_PATH)
                && request.pathParams().containsKey(USER_ID_PATH_VARIABLE);
    }

    private CompletionStage<Boolean> validateAdminToken(HttpServerRequest request) {
        return CompletableFuture.supplyAsync(() -> authGrpcService.checkAdminResponse(CheckAdminRequest.newBuilder()
                .setToken(request.headers().getFirst(HEADER_TOKEN))
                .build()).getIsValid());
    }

    private CompletionStage<Boolean> validateTokenWithId(HttpServerRequest request) {
        return CompletableFuture.supplyAsync(() -> authGrpcService.checkTokenWithId(CheckTokenWithIdRequest.newBuilder()
                .setToken(request.headers().getFirst(HEADER_TOKEN))
                .setId(Long.parseLong(request.pathParams().get(USER_ID_PATH_VARIABLE)))
                .build()).getIsValid());
    }

    private CompletionStage<HttpServerResponse> createErrorResponse(String message, int statusCode) {
        var body = HttpBody.json(errorJsonWriter.toByteArrayUnchecked(new UserErrorResponse(message)));
        return CompletableFuture.completedFuture(HttpServerResponse.of(statusCode, CORS_HEADERS, body));
    }

    private CompletionStage<HttpServerResponse> processRequest(Context context, HttpServerRequest request, InterceptChain chain) throws Exception {
        return chain.process(context, request).thenApply(response -> HttpServerResponse.of(response.code(),
                CORS_HEADERS, response.body())).exceptionally(e -> handleError(e, request));
    }

    private HttpServerResponse handleError(Throwable e, HttpServerRequest request) {
        if (e instanceof HttpServerResponseException ex) {
            return ex;
        }

        var body = HttpBody.json(errorJsonWriter.toByteArrayUnchecked(new UserErrorResponse(e.getCause().getMessage())));
        if (e.getCause() instanceof BusinessException) {
            log.warn("Request '{} {}' failed due to {}", request.method(), request.path(), e.getMessage());
            return HttpServerResponse.of(422, CORS_HEADERS, body);
        } else {
            log.error("Request '{} {}' failed", request.method(), request.path(), e);
            return HttpServerResponse.of(500, CORS_HEADERS, body);
        }
    }
}
