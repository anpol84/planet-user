package ru.planet.user.api;

import java.lang.RuntimeException;
import java.lang.String;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import ru.planet.user.model.ChangeUserRequest;
import ru.planet.user.model.CreateUserRequest;
import ru.tinkoff.kora.common.Module;
import ru.tinkoff.kora.common.Tag;
import ru.tinkoff.kora.common.annotation.Generated;
import ru.tinkoff.kora.http.server.common.HttpServerResponse;
import ru.tinkoff.kora.http.server.common.HttpServerResponseException;
import ru.tinkoff.kora.http.server.common.handler.BlockingRequestExecutor;
import ru.tinkoff.kora.http.server.common.handler.HttpServerRequestHandler;
import ru.tinkoff.kora.http.server.common.handler.HttpServerRequestHandlerImpl;
import ru.tinkoff.kora.http.server.common.handler.HttpServerRequestMapper;
import ru.tinkoff.kora.http.server.common.handler.RequestHandlerUtils;

@Generated("ru.tinkoff.kora.http.server.annotation.processor.HttpControllerProcessor")
@Module
public interface UserApiControllerModule {
  default HttpServerRequestHandler post_api_users(UserApiController _controller,
      @Tag({ru.tinkoff.kora.json.common.annotation.Json.class}) HttpServerRequestMapper<CreateUserRequest> createUserRequestHttpRequestMapper,
      UserApiServerResponseMappers.CreateUserApiResponseMapper _responseMapper,
      BlockingRequestExecutor _executor) {
    return HttpServerRequestHandlerImpl.of("POST", "/api/users", (_ctx, _request) -> {

      return _executor.execute(_ctx, () -> {
        final CreateUserRequest createUserRequest;
        try {
          createUserRequest = createUserRequestHttpRequestMapper.apply(_request);
        } catch (CompletionException _e) {
          if (_e.getCause() instanceof HttpServerResponse && _e.getCause() instanceof RuntimeException) throw (RuntimeException) _e.getCause();
          throw HttpServerResponseException.of(400, _e.getCause());
        } catch (Exception _e) {
          if (_e instanceof HttpServerResponse) throw _e;
          throw HttpServerResponseException.of(400, _e);
        }
        var _result = _controller.createUser(createUserRequest);
        return _responseMapper.apply(_ctx, _request, _result);
      });
    });
  }

  default HttpServerRequestHandler put_api_users_userId(UserApiController _controller,
      @Tag({ru.tinkoff.kora.json.common.annotation.Json.class}) HttpServerRequestMapper<ChangeUserRequest> changeUserRequestHttpRequestMapper,
      UserApiServerResponseMappers.ChangeUserApiResponseMapper _responseMapper,
      BlockingRequestExecutor _executor) {
    return HttpServerRequestHandlerImpl.of("PUT", "/api/users/{userId}", (_ctx, _request) -> {
      final long userId;
      final String token;
      try {
        userId = RequestHandlerUtils.parseLongPathParameter(_request, "userId");
        token = RequestHandlerUtils.parseStringHeaderParameter(_request, "token");

      } catch (Exception _e) {
        if (_e instanceof HttpServerResponse) {
          return CompletableFuture.failedFuture(_e);
        } else {
          return CompletableFuture.failedFuture(HttpServerResponseException.of(400, _e));
        }
      }

      return _executor.execute(_ctx, () -> {
        final ChangeUserRequest changeUserRequest;
        try {
          changeUserRequest = changeUserRequestHttpRequestMapper.apply(_request);
        } catch (CompletionException _e) {
          if (_e.getCause() instanceof HttpServerResponse && _e.getCause() instanceof RuntimeException) throw (RuntimeException) _e.getCause();
          throw HttpServerResponseException.of(400, _e.getCause());
        } catch (Exception _e) {
          if (_e instanceof HttpServerResponse) throw _e;
          throw HttpServerResponseException.of(400, _e);
        }
        var _result = _controller.changeUser(userId, token, changeUserRequest);
        return _responseMapper.apply(_ctx, _request, _result);
      });
    });
  }

  default HttpServerRequestHandler delete_api_users_userId(UserApiController _controller,
      UserApiServerResponseMappers.DeleteUserApiResponseMapper _responseMapper,
      BlockingRequestExecutor _executor) {
    return HttpServerRequestHandlerImpl.of("DELETE", "/api/users/{userId}", (_ctx, _request) -> {
      final long userId;
      final String token;
      try {
        userId = RequestHandlerUtils.parseLongPathParameter(_request, "userId");
        token = RequestHandlerUtils.parseStringHeaderParameter(_request, "token");
      } catch (Exception _e) {
        if (_e instanceof HttpServerResponse) {
          return CompletableFuture.failedFuture(_e);
        } else {
          return CompletableFuture.failedFuture(HttpServerResponseException.of(400, _e));
        }
      }

      return _executor.execute(_ctx, () -> {
        var _result = _controller.deleteUser(userId, token);
        return _responseMapper.apply(_ctx, _request, _result);
      });
    });
  }

  default HttpServerRequestHandler get_api_users_userId(UserApiController _controller,
      UserApiServerResponseMappers.GetUserApiResponseMapper _responseMapper,
      BlockingRequestExecutor _executor) {
    return HttpServerRequestHandlerImpl.of("GET", "/api/users/{userId}", (_ctx, _request) -> {
      final long userId;
      final String token;
      try {
        userId = RequestHandlerUtils.parseLongPathParameter(_request, "userId");
        token = RequestHandlerUtils.parseStringHeaderParameter(_request, "token");
      } catch (Exception _e) {
        if (_e instanceof HttpServerResponse) {
          return CompletableFuture.failedFuture(_e);
        } else {
          return CompletableFuture.failedFuture(HttpServerResponseException.of(400, _e));
        }
      }

      return _executor.execute(_ctx, () -> {
        var _result = _controller.getUser(userId, token);
        return _responseMapper.apply(_ctx, _request, _result);
      });
    });
  }
}
