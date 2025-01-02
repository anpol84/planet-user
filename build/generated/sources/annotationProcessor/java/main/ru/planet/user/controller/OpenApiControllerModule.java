package ru.planet.user.controller;

import ru.tinkoff.kora.common.Module;
import ru.tinkoff.kora.common.annotation.Generated;
import ru.tinkoff.kora.http.server.common.handler.BlockingRequestExecutor;
import ru.tinkoff.kora.http.server.common.handler.HttpServerRequestHandler;
import ru.tinkoff.kora.http.server.common.handler.HttpServerRequestHandlerImpl;

@Generated("ru.tinkoff.kora.http.server.annotation.processor.HttpControllerProcessor")
@Module
public interface OpenApiControllerModule {
  default HttpServerRequestHandler get_docs_openapi_yaml(OpenApiController _controller,
      BlockingRequestExecutor _executor) {
    return HttpServerRequestHandlerImpl.of("GET", "/docs/openapi.yaml", (_ctx, _request) -> {
      return _executor.execute(_ctx, () -> {
        return _controller.getOpenApiContract();
      });
    });
  }

  default HttpServerRequestHandler options_(OpenApiController _controller,
      BlockingRequestExecutor _executor) {
    return HttpServerRequestHandlerImpl.of("OPTIONS", "/*", (_ctx, _request) -> {
      return _executor.execute(_ctx, () -> {
        return _controller.preflight();
      });
    });
  }
}
