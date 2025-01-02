package ru.planet.user;

import io.grpc.ChannelCredentials;
import io.netty.channel.EventLoopGroup;
import io.undertow.connector.ByteBufferPool;
import java.lang.Double;
import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ThreadFactory;
import java.util.function.Supplier;
import org.slf4j.ILoggerFactory;
import org.xnio.XnioWorker;
import ru.planet.user.api.UserApiController;
import ru.planet.user.api.UserApiServerResponseMappers;
import ru.planet.user.configuration.properties.$BcryptProperties_ConfigValueExtractor;
import ru.planet.user.configuration.properties.BcryptProperties;
import ru.planet.user.controller.OpenApiController;
import ru.planet.user.controller.UserRestController;
import ru.planet.user.dto.$User_JdbcResultSetMapper;
import ru.planet.user.dto.User;
import ru.planet.user.exception.HttpExceptionHandler;
import ru.planet.user.helper.OpenApiProvider;
import ru.planet.user.helper.interceptor.AuthGrpcServiceInterceptor;
import ru.planet.user.helper.mapper.UserMapperImpl;
import ru.planet.user.model.$ChangeUserRequest_JsonReader;
import ru.planet.user.model.$CreateUser400Response_JsonWriter;
import ru.planet.user.model.$CreateUserRequest_JsonReader;
import ru.planet.user.model.$CreateUserResponse_JsonWriter;
import ru.planet.user.model.$GetUser400Response_JsonWriter;
import ru.planet.user.model.$GetUserResponse_JsonWriter;
import ru.planet.user.model.$UserErrorResponse_JsonWriter;
import ru.planet.user.model.ChangeUserRequest;
import ru.planet.user.model.CreateUser400Response;
import ru.planet.user.model.CreateUserRequest;
import ru.planet.user.model.CreateUserResponse;
import ru.planet.user.model.GetUser400Response;
import ru.planet.user.model.GetUserResponse;
import ru.planet.user.model.UserErrorResponse;
import ru.planet.user.operation.CreateUserOperation;
import ru.planet.user.operation.GetUserOperation;
import ru.planet.user.repository.$$UserRepository_Impl__AopProxy;
import ru.planet.user.repository.$UserRepository_Impl;
import ru.planet.user.service.gRPC.AuthGrpcService;
import ru.tinkoff.grpc.client.GrpcNettyClientChannelFactory;
import ru.tinkoff.grpc.client.ManagedChannelLifecycle;
import ru.tinkoff.grpc.client.config.$GrpcClientConfig_ConfigValueExtractor;
import ru.tinkoff.grpc.client.config.GrpcClientConfig;
import ru.tinkoff.grpc.client.telemetry.DefaultGrpcClientTelemetryFactory;
import ru.tinkoff.grpc.client.telemetry.GrpcClientLoggerFactory;
import ru.tinkoff.grpc.client.telemetry.GrpcClientMetricsFactory;
import ru.tinkoff.grpc.client.telemetry.GrpcClientTracerFactory;
import ru.tinkoff.kora.application.graph.All;
import ru.tinkoff.kora.application.graph.ApplicationGraphDraw;
import ru.tinkoff.kora.application.graph.LifecycleWrapper;
import ru.tinkoff.kora.application.graph.Node;
import ru.tinkoff.kora.application.graph.TypeRef;
import ru.tinkoff.kora.application.graph.ValueOf;
import ru.tinkoff.kora.application.graph.Wrapped;
import ru.tinkoff.kora.common.annotation.Generated;
import ru.tinkoff.kora.common.readiness.ReadinessProbe;
import ru.tinkoff.kora.config.common.Config;
import ru.tinkoff.kora.config.common.ConfigWatcher;
import ru.tinkoff.kora.config.common.extractor.ConfigValueExtractor;
import ru.tinkoff.kora.config.common.extractor.EnumConfigValueExtractor;
import ru.tinkoff.kora.config.common.origin.ConfigOrigin;
import ru.tinkoff.kora.database.common.telemetry.DataBaseLoggerFactory;
import ru.tinkoff.kora.database.common.telemetry.DataBaseMetricWriterFactory;
import ru.tinkoff.kora.database.common.telemetry.DataBaseTracerFactory;
import ru.tinkoff.kora.database.common.telemetry.DefaultDataBaseTelemetryFactory;
import ru.tinkoff.kora.database.jdbc.$JdbcDatabaseConfig_ConfigValueExtractor;
import ru.tinkoff.kora.database.jdbc.JdbcDatabase;
import ru.tinkoff.kora.database.jdbc.JdbcDatabaseConfig;
import ru.tinkoff.kora.database.jdbc.mapper.result.JdbcResultSetMapper;
import ru.tinkoff.kora.database.jdbc.mapper.result.JdbcRowMapper;
import ru.tinkoff.kora.generated.grpc.AuthServiceGrpc;
import ru.tinkoff.kora.http.server.common.$HttpServerConfig_ConfigValueExtractor;
import ru.tinkoff.kora.http.server.common.HttpServerConfig;
import ru.tinkoff.kora.http.server.common.PrivateApiHandler;
import ru.tinkoff.kora.http.server.common.handler.BlockingRequestExecutor;
import ru.tinkoff.kora.http.server.common.handler.HttpServerRequestHandler;
import ru.tinkoff.kora.http.server.common.router.PublicApiHandler;
import ru.tinkoff.kora.http.server.common.telemetry.$HttpServerLoggerConfig_ConfigValueExtractor;
import ru.tinkoff.kora.http.server.common.telemetry.$HttpServerTelemetryConfig_ConfigValueExtractor;
import ru.tinkoff.kora.http.server.common.telemetry.DefaultHttpServerTelemetryFactory;
import ru.tinkoff.kora.http.server.common.telemetry.HttpServerMetricsFactory;
import ru.tinkoff.kora.http.server.common.telemetry.HttpServerTracerFactory;
import ru.tinkoff.kora.http.server.common.telemetry.PrivateApiMetrics;
import ru.tinkoff.kora.http.server.common.telemetry.Slf4jHttpServerLoggerFactory;
import ru.tinkoff.kora.http.server.undertow.UndertowHttpServer;
import ru.tinkoff.kora.http.server.undertow.UndertowPrivateApiHandler;
import ru.tinkoff.kora.http.server.undertow.UndertowPrivateHttpServer;
import ru.tinkoff.kora.http.server.undertow.UndertowPublicApiHandler;
import ru.tinkoff.kora.json.common.JsonWriter;
import ru.tinkoff.kora.json.module.http.server.JsonReaderHttpServerRequestMapper;
import ru.tinkoff.kora.json.module.http.server.JsonWriterHttpServerResponseMapper;
import ru.tinkoff.kora.logging.common.LoggingConfig;
import ru.tinkoff.kora.logging.common.LoggingLevelApplier;
import ru.tinkoff.kora.logging.common.LoggingLevelRefresher;
import ru.tinkoff.kora.logging.common.arg.StructuredArgumentMapper;
import ru.tinkoff.kora.netty.common.$NettyTransportConfig_ConfigValueExtractor;
import ru.tinkoff.kora.netty.common.NettyChannelFactory;
import ru.tinkoff.kora.netty.common.NettyTransportConfig;
import ru.tinkoff.kora.openapi.management.$OpenApiManagementConfig_ConfigValueExtractor;
import ru.tinkoff.kora.openapi.management.$OpenApiManagementConfig_RapidocConfig_ConfigValueExtractor;
import ru.tinkoff.kora.openapi.management.$OpenApiManagementConfig_SwaggerUIConfig_ConfigValueExtractor;
import ru.tinkoff.kora.openapi.management.OpenApiManagementConfig;
import ru.tinkoff.kora.telemetry.common.$TelemetryConfig_ConfigValueExtractor;
import ru.tinkoff.kora.telemetry.common.$TelemetryConfig_LogConfig_ConfigValueExtractor;
import ru.tinkoff.kora.telemetry.common.$TelemetryConfig_MetricsConfig_ConfigValueExtractor;
import ru.tinkoff.kora.telemetry.common.$TelemetryConfig_TracingConfig_ConfigValueExtractor;

@Generated("ru.tinkoff.kora.kora.app.annotation.processor.KoraAppProcessor")
public class ApplicationGraph implements Supplier<ApplicationGraphDraw> {
  private static final ApplicationGraphDraw graphDraw;

  private static final ComponentHolder0 holder0;

  static {
    var impl = new $ApplicationImpl();
    graphDraw = new ApplicationGraphDraw(Application.class);
    holder0 = new ComponentHolder0(graphDraw, impl);
  }

  @Override
  public ApplicationGraphDraw get() {
    return graphDraw;
  }

  public static ApplicationGraphDraw graph() {
    return graphDraw;
  }

  @Generated("ru.tinkoff.kora.kora.app.annotation.processor.KoraAppProcessor")
  public static final class ComponentHolder0 {
    private final Node<ConfigOrigin> component0;

    private final Node<Optional<ValueOf<ConfigOrigin>>> component1;

    private final Node<ConfigWatcher> component2;

    private final Node<Config> component3;

    private final Node<Config> component4;

    private final Node<com.typesafe.config.Config> component5;

    private final Node<com.typesafe.config.Config> component6;

    private final Node<Config> component7;

    private final Node<Config> component8;

    private final Node<ConfigValueExtractor<Duration>> component9;

    private final Node<ConfigValueExtractor<String>> component10;

    private final Node<ConfigValueExtractor<Set<String>>> component11;

    private final Node<$HttpServerLoggerConfig_ConfigValueExtractor> component12;

    private final Node<$TelemetryConfig_TracingConfig_ConfigValueExtractor> component13;

    private final Node<ConfigValueExtractor<Double>> component14;

    private final Node<ConfigValueExtractor<double[]>> component15;

    private final Node<$TelemetryConfig_MetricsConfig_ConfigValueExtractor> component16;

    private final Node<$HttpServerTelemetryConfig_ConfigValueExtractor> component17;

    private final Node<$HttpServerConfig_ConfigValueExtractor> component18;

    private final Node<HttpServerConfig> component19;

    private final Node<UserMapperImpl> component20;

    private final Node<ConfigValueExtractor<Properties>> component21;

    private final Node<$TelemetryConfig_LogConfig_ConfigValueExtractor> component22;

    private final Node<$TelemetryConfig_ConfigValueExtractor> component23;

    private final Node<$JdbcDatabaseConfig_ConfigValueExtractor> component24;

    private final Node<JdbcDatabaseConfig> component25;

    private final Node<DataBaseLoggerFactory.DefaultDataBaseLoggerFactory> component26;

    private final Node<DefaultDataBaseTelemetryFactory> component27;

    private final Node<JdbcDatabase> component28;

    private final Node<$User_JdbcResultSetMapper> component29;

    private final Node<JdbcRowMapper<String>> component30;

    private final Node<JdbcResultSetMapper<List<String>>> component31;

    private final Node<JdbcRowMapper<Long>> component32;

    private final Node<JdbcResultSetMapper<Long>> component33;

    private final Node<ILoggerFactory> component34;

    private final Node<$UserRepository_Impl> component35;

    private final Node<AuthGrpcServiceInterceptor> component36;

    private final Node<$GrpcClientConfig_ConfigValueExtractor> component37;

    private final Node<GrpcClientConfig> component38;

    private final Node<DefaultGrpcClientTelemetryFactory> component39;

    private final Node<EnumConfigValueExtractor<NettyTransportConfig.EventLoop>> component40;

    private final Node<$NettyTransportConfig_ConfigValueExtractor> component41;

    private final Node<NettyTransportConfig> component42;

    private final Node<LifecycleWrapper<EventLoopGroup>> component43;

    private final Node<NettyChannelFactory> component44;

    private final Node<GrpcNettyClientChannelFactory> component45;

    private final Node<ManagedChannelLifecycle> component46;

    private final Node<AuthServiceGrpc.AuthServiceBlockingStub> component47;

    private final Node<AuthGrpcService> component48;

    private final Node<$BcryptProperties_ConfigValueExtractor> component49;

    private final Node<BcryptProperties> component50;

    private final Node<CreateUserOperation> component51;

    private final Node<GetUserOperation> component52;

    private final Node<UserRestController> component53;

    private final Node<UserApiController> component54;

    private final Node<$GetUser400Response_JsonWriter> component55;

    private final Node<JsonWriterHttpServerResponseMapper<GetUser400Response>> component56;

    private final Node<$UserErrorResponse_JsonWriter> component57;

    private final Node<JsonWriterHttpServerResponseMapper<UserErrorResponse>> component58;

    private final Node<UserApiServerResponseMappers.DeleteUserApiResponseMapper> component59;

    private final Node<Wrapped<XnioWorker>> component60;

    private final Node<BlockingRequestExecutor> component61;

    private final Node<HttpServerRequestHandler> component62;

    private final Node<JsonWriter<String>> component63;

    private final Node<GetUserResponse.RolesEnum.RolesEnumJsonWriter> component64;

    private final Node<JsonWriter<List<GetUserResponse.RolesEnum>>> component65;

    private final Node<$GetUserResponse_JsonWriter> component66;

    private final Node<JsonWriterHttpServerResponseMapper<GetUserResponse>> component67;

    private final Node<UserApiServerResponseMappers.GetUserApiResponseMapper> component68;

    private final Node<HttpServerRequestHandler> component69;

    private final Node<$CreateUserRequest_JsonReader> component70;

    private final Node<JsonReaderHttpServerRequestMapper<CreateUserRequest>> component71;

    private final Node<$CreateUserResponse_JsonWriter> component72;

    private final Node<JsonWriterHttpServerResponseMapper<CreateUserResponse>> component73;

    private final Node<$CreateUser400Response_JsonWriter> component74;

    private final Node<JsonWriterHttpServerResponseMapper<CreateUser400Response>> component75;

    private final Node<UserApiServerResponseMappers.CreateUserApiResponseMapper> component76;

    private final Node<HttpServerRequestHandler> component77;

    private final Node<$ChangeUserRequest_JsonReader> component78;

    private final Node<JsonReaderHttpServerRequestMapper<ChangeUserRequest>> component79;

    private final Node<UserApiServerResponseMappers.ChangeUserApiResponseMapper> component80;

    private final Node<HttpServerRequestHandler> component81;

    private final Node<OpenApiProvider> component82;

    private final Node<OpenApiController> component83;

    private final Node<HttpServerRequestHandler> component84;

    private final Node<HttpServerRequestHandler> component85;

    private final Node<ConfigValueExtractor<List<String>>> component86;

    private final Node<$OpenApiManagementConfig_SwaggerUIConfig_ConfigValueExtractor> component87;

    private final Node<$OpenApiManagementConfig_RapidocConfig_ConfigValueExtractor> component88;

    private final Node<$OpenApiManagementConfig_ConfigValueExtractor> component89;

    private final Node<OpenApiManagementConfig> component90;

    private final Node<HttpServerRequestHandler> component91;

    private final Node<HttpServerRequestHandler> component92;

    private final Node<HttpServerRequestHandler> component93;

    private final Node<HttpExceptionHandler> component94;

    private final Node<Slf4jHttpServerLoggerFactory> component95;

    private final Node<DefaultHttpServerTelemetryFactory> component96;

    private final Node<PublicApiHandler> component97;

    private final Node<UndertowPublicApiHandler> component98;

    private final Node<ByteBufferPool> component99;

    private final Node<UndertowHttpServer> component100;

    private final Node<Optional<PrivateApiMetrics>> component101;

    private final Node<PrivateApiHandler> component102;

    private final Node<UndertowPrivateApiHandler> component103;

    private final Node<ByteBufferPool> component104;

    private final Node<UndertowPrivateHttpServer> component105;

    private final Node<ConfigValueExtractor<LoggingConfig>> component106;

    private final Node<LoggingConfig> component107;

    private final Node<LoggingLevelApplier> component108;

    private final Node<LoggingLevelRefresher> component109;

    public ComponentHolder0(ApplicationGraphDraw graphDraw, $ApplicationImpl impl) {
      var map = new HashMap<String, Type>();
      for (var field : ComponentHolder0.class.getDeclaredFields()) {
        if (!field.getName().startsWith("component")) continue;
        map.put(field.getName(), ((ParameterizedType) field.getGenericType()).getActualTypeArguments()[0]);
      }
      var _type_of_component0 = map.get("component0");
      component0 = graphDraw.addNode0(_type_of_component0, new Class<?>[]{ru.tinkoff.kora.config.common.annotation.ApplicationConfig.class, }, g -> impl.applicationConfigOrigin(), List.of());
      var _type_of_component1 = map.get("component1");
      component1 = graphDraw.addNode0(_type_of_component1, new Class<?>[]{ru.tinkoff.kora.config.common.annotation.ApplicationConfig.class, }, g -> Optional.<ValueOf<ConfigOrigin>>ofNullable(
            g.valueOf(ApplicationGraph.holder0.component0).map(v -> (ConfigOrigin) v)
          ), List.of(), component0.valueOf());
      var _type_of_component2 = map.get("component2");
      component2 = graphDraw.addNode0(_type_of_component2, new Class<?>[]{}, g -> impl.configRefresher(
            g.get(ApplicationGraph.holder0.component1)
          ), List.of(), component1);
      var _type_of_component3 = map.get("component3");
      component3 = graphDraw.addNode0(_type_of_component3, new Class<?>[]{ru.tinkoff.kora.config.common.annotation.Environment.class, }, g -> impl.environmentConfig(), List.of());
      var _type_of_component4 = map.get("component4");
      component4 = graphDraw.addNode0(_type_of_component4, new Class<?>[]{ru.tinkoff.kora.config.common.annotation.SystemProperties.class, }, g -> impl.systemProperties(), List.of());
      var _type_of_component5 = map.get("component5");
      component5 = graphDraw.addNode0(_type_of_component5, new Class<?>[]{ru.tinkoff.kora.config.common.annotation.ApplicationConfig.class, }, g -> impl.applicationUnresolved(
            g.get(ApplicationGraph.holder0.component0)
          ), List.of(), component0);
      var _type_of_component6 = map.get("component6");
      component6 = graphDraw.addNode0(_type_of_component6, new Class<?>[]{}, g -> impl.hoconConfig(
            g.get(ApplicationGraph.holder0.component5)
          ), List.of(), component5);
      var _type_of_component7 = map.get("component7");
      component7 = graphDraw.addNode0(_type_of_component7, new Class<?>[]{ru.tinkoff.kora.config.common.annotation.ApplicationConfig.class, }, g -> impl.config(
            g.get(ApplicationGraph.holder0.component0),
            g.get(ApplicationGraph.holder0.component6)
          ), List.of(), component0, component6);
      var _type_of_component8 = map.get("component8");
      component8 = graphDraw.addNode0(_type_of_component8, new Class<?>[]{}, g -> impl.config(
            g.get(ApplicationGraph.holder0.component3),
            g.get(ApplicationGraph.holder0.component4),
            g.get(ApplicationGraph.holder0.component7)
          ), List.of(), component3, component4, component7);
      var _type_of_component9 = map.get("component9");
      component9 = graphDraw.addNode0(_type_of_component9, new Class<?>[]{}, g -> impl.durationConfigValueExtractor(), List.of());
      var _type_of_component10 = map.get("component10");
      component10 = graphDraw.addNode0(_type_of_component10, new Class<?>[]{}, g -> impl.stringConfigValueExtractor(), List.of());
      var _type_of_component11 = map.get("component11");
      component11 = graphDraw.addNode0(_type_of_component11, new Class<?>[]{}, g -> impl.<String>setConfigValueExtractor(
            g.get(ApplicationGraph.holder0.component10)
          ), List.of(), component10);
      var _type_of_component12 = map.get("component12");
      component12 = graphDraw.addNode0(_type_of_component12, new Class<?>[]{}, g -> new $HttpServerLoggerConfig_ConfigValueExtractor(
            g.get(ApplicationGraph.holder0.component11)
          ), List.of(), component11);
      var _type_of_component13 = map.get("component13");
      component13 = graphDraw.addNode0(_type_of_component13, new Class<?>[]{}, g -> new $TelemetryConfig_TracingConfig_ConfigValueExtractor(), List.of());
      var _type_of_component14 = map.get("component14");
      component14 = graphDraw.addNode0(_type_of_component14, new Class<?>[]{}, g -> impl.doubleConfigValueExtractor(), List.of());
      var _type_of_component15 = map.get("component15");
      component15 = graphDraw.addNode0(_type_of_component15, new Class<?>[]{}, g -> impl.doubleArrayConfigValueExtractor(
            g.get(ApplicationGraph.holder0.component14)
          ), List.of(), component14);
      var _type_of_component16 = map.get("component16");
      component16 = graphDraw.addNode0(_type_of_component16, new Class<?>[]{}, g -> new $TelemetryConfig_MetricsConfig_ConfigValueExtractor(
            g.get(ApplicationGraph.holder0.component15)
          ), List.of(), component15);
      var _type_of_component17 = map.get("component17");
      component17 = graphDraw.addNode0(_type_of_component17, new Class<?>[]{}, g -> new $HttpServerTelemetryConfig_ConfigValueExtractor(
            g.get(ApplicationGraph.holder0.component12),
            g.get(ApplicationGraph.holder0.component13),
            g.get(ApplicationGraph.holder0.component16)
          ), List.of(), component12, component13, component16);
      var _type_of_component18 = map.get("component18");
      component18 = graphDraw.addNode0(_type_of_component18, new Class<?>[]{}, g -> new $HttpServerConfig_ConfigValueExtractor(
            g.get(ApplicationGraph.holder0.component9),
            g.get(ApplicationGraph.holder0.component17)
          ), List.of(), component9, component17);
      var _type_of_component19 = map.get("component19");
      component19 = graphDraw.addNode0(_type_of_component19, new Class<?>[]{}, g -> impl.httpServerConfig(
            g.get(ApplicationGraph.holder0.component8),
            g.get(ApplicationGraph.holder0.component18)
          ), List.of(), component8, component18);
      var _type_of_component20 = map.get("component20");
      component20 = graphDraw.addNode0(_type_of_component20, new Class<?>[]{}, g -> new UserMapperImpl(), List.of());
      var _type_of_component21 = map.get("component21");
      component21 = graphDraw.addNode0(_type_of_component21, new Class<?>[]{}, g -> impl.propertiesConfigValueExtractor(), List.of());
      var _type_of_component22 = map.get("component22");
      component22 = graphDraw.addNode0(_type_of_component22, new Class<?>[]{}, g -> new $TelemetryConfig_LogConfig_ConfigValueExtractor(), List.of());
      var _type_of_component23 = map.get("component23");
      component23 = graphDraw.addNode0(_type_of_component23, new Class<?>[]{}, g -> new $TelemetryConfig_ConfigValueExtractor(
            g.get(ApplicationGraph.holder0.component22),
            g.get(ApplicationGraph.holder0.component13),
            g.get(ApplicationGraph.holder0.component16)
          ), List.of(), component22, component13, component16);
      var _type_of_component24 = map.get("component24");
      component24 = graphDraw.addNode0(_type_of_component24, new Class<?>[]{}, g -> new $JdbcDatabaseConfig_ConfigValueExtractor(
            g.get(ApplicationGraph.holder0.component9),
            g.get(ApplicationGraph.holder0.component21),
            g.get(ApplicationGraph.holder0.component23)
          ), List.of(), component9, component21, component23);
      var _type_of_component25 = map.get("component25");
      component25 = graphDraw.addNode0(_type_of_component25, new Class<?>[]{}, g -> impl.jdbcDataBaseConfig(
            g.get(ApplicationGraph.holder0.component8),
            g.get(ApplicationGraph.holder0.component24)
          ), List.of(), component8, component24);
      var _type_of_component26 = map.get("component26");
      component26 = graphDraw.addNode0(_type_of_component26, new Class<?>[]{}, g -> impl.defaultDataBaseLoggerFactory(), List.of());
      var _type_of_component27 = map.get("component27");
      component27 = graphDraw.addNode0(_type_of_component27, new Class<?>[]{}, g -> impl.defaultDataBaseTelemetry(
            g.get(ApplicationGraph.holder0.component26),
            (DataBaseMetricWriterFactory) null,
            (DataBaseTracerFactory) null
          ), List.of(), component26);
      var _type_of_component28 = map.get("component28");
      component28 = graphDraw.addNode0(_type_of_component28, new Class<?>[]{}, g -> impl.jdbcDataBase(
            g.get(ApplicationGraph.holder0.component25),
            g.get(ApplicationGraph.holder0.component27)
          ), List.of(), component25, component27);
      var _type_of_component29 = map.get("component29");
      component29 = graphDraw.addNode0(_type_of_component29, new Class<?>[]{}, g -> new $User_JdbcResultSetMapper(), List.of());
      var _type_of_component30 = map.get("component30");
      component30 = graphDraw.addNode0(_type_of_component30, new Class<?>[]{}, g -> impl.stringJdbcRowMapper(), List.of());
      var _type_of_component31 = map.get("component31");
      component31 = graphDraw.addNode0(_type_of_component31, new Class<?>[]{}, g -> JdbcResultSetMapper.listResultSetMapper(
            g.get(ApplicationGraph.holder0.component30)
          ), List.of(), component30);
      var _type_of_component32 = map.get("component32");
      component32 = graphDraw.addNode0(_type_of_component32, new Class<?>[]{}, g -> impl.longJdbcRowMapper(), List.of());
      var _type_of_component33 = map.get("component33");
      component33 = graphDraw.addNode0(_type_of_component33, new Class<?>[]{}, g -> JdbcResultSetMapper.singleResultSetMapper(
            g.get(ApplicationGraph.holder0.component32)
          ), List.of(), component32);
      var _type_of_component34 = map.get("component34");
      component34 = graphDraw.addNode0(_type_of_component34, new Class<?>[]{}, g -> impl.loggerFactory(), List.of());
      var _type_of_component35 = map.get("component35");
      component35 = graphDraw.addNode0(_type_of_component35, new Class<?>[]{}, g -> new $$UserRepository_Impl__AopProxy(
            g.get(ApplicationGraph.holder0.component28),
            g.get(ApplicationGraph.holder0.component29),
            g.get(ApplicationGraph.holder0.component31),
            g.get(ApplicationGraph.holder0.component33),
            g.get(ApplicationGraph.holder0.component34),
            (StructuredArgumentMapper<String>) null,
            (StructuredArgumentMapper<User>) null,
            (StructuredArgumentMapper<Long>) null,
            (StructuredArgumentMapper<List<String>>) null
          ), List.of(), component28, component29, component31, component33, component34);
      var _type_of_component36 = map.get("component36");
      component36 = graphDraw.addNode0(_type_of_component36, new Class<?>[]{}, g -> new AuthGrpcServiceInterceptor(), List.of());
      var _type_of_component37 = map.get("component37");
      component37 = graphDraw.addNode0(_type_of_component37, new Class<?>[]{}, g -> new $GrpcClientConfig_ConfigValueExtractor(
            g.get(ApplicationGraph.holder0.component9),
            g.get(ApplicationGraph.holder0.component23)
          ), List.of(), component9, component23);
      var _type_of_component38 = map.get("component38");
      component38 = graphDraw.addNode0(_type_of_component38, new Class<?>[]{ru.tinkoff.kora.generated.grpc.AuthServiceGrpc.class, }, g -> GrpcClientConfig.defaultConfig(
            g.get(ApplicationGraph.holder0.component8),
            g.get(ApplicationGraph.holder0.component37)
          , AuthServiceGrpc.SERVICE_NAME), List.of(), component8, component37);
      var _type_of_component39 = map.get("component39");
      component39 = graphDraw.addNode0(_type_of_component39, new Class<?>[]{}, g -> impl.defaultGrpcClientTelemetryFactory(
            (GrpcClientMetricsFactory) null,
            (GrpcClientTracerFactory) null,
            (GrpcClientLoggerFactory) null
          ), List.of());
      var _type_of_component40 = map.get("component40");
      component40 = graphDraw.addNode0(_type_of_component40, new Class<?>[]{}, g -> impl.<NettyTransportConfig.EventLoop>enumConfigValueExtractor(
            TypeRef.of(NettyTransportConfig.EventLoop.class)
          ), List.of());
      var _type_of_component41 = map.get("component41");
      component41 = graphDraw.addNode0(_type_of_component41, new Class<?>[]{}, g -> new $NettyTransportConfig_ConfigValueExtractor(
            g.get(ApplicationGraph.holder0.component40)
          ), List.of(), component40);
      var _type_of_component42 = map.get("component42");
      component42 = graphDraw.addNode0(_type_of_component42, new Class<?>[]{}, g -> impl.nettyTransportConfig(
            g.get(ApplicationGraph.holder0.component8),
            g.get(ApplicationGraph.holder0.component41)
          ), List.of(), component8, component41);
      var _type_of_component43 = map.get("component43");
      component43 = graphDraw.addNode0(_type_of_component43, new Class<?>[]{ru.tinkoff.kora.netty.common.NettyCommonModule.WorkerLoopGroup.class, }, g -> impl.nettyEventLoopGroupLifecycle(
            (ThreadFactory) null,
            g.get(ApplicationGraph.holder0.component42)
          ), List.of(), component42);
      var _type_of_component44 = map.get("component44");
      component44 = graphDraw.addNode0(_type_of_component44, new Class<?>[]{}, g -> impl.nettyChannelFactory(
            g.get(ApplicationGraph.holder0.component42)
          ), List.of(), component42);
      var _type_of_component45 = map.get("component45");
      component45 = graphDraw.addNode0(_type_of_component45, new Class<?>[]{}, g -> impl.grpcNettyClientChannelFactory(
            g.get(ApplicationGraph.holder0.component43).value(),
            g.get(ApplicationGraph.holder0.component44)
          ), List.of(), component43, component44);
      var _type_of_component46 = map.get("component46");
      component46 = graphDraw.addNode0(_type_of_component46, new Class<?>[]{ru.tinkoff.kora.generated.grpc.AuthServiceGrpc.class, }, g -> new ManagedChannelLifecycle(
            g.get(ApplicationGraph.holder0.component38),
            (ChannelCredentials) null,
            All.of(  ),
            g.get(ApplicationGraph.holder0.component39),
            g.get(ApplicationGraph.holder0.component45)
          , AuthServiceGrpc.getServiceDescriptor()), List.of(), component38, component39, component45);
      var _type_of_component47 = map.get("component47");
      component47 = graphDraw.addNode0(_type_of_component47, new Class<?>[]{}, g -> AuthServiceGrpc.newBlockingStub(
            g.get(ApplicationGraph.holder0.component46).value()
          ), List.of(component36), component46);
      var _type_of_component48 = map.get("component48");
      component48 = graphDraw.addNode0(_type_of_component48, new Class<?>[]{}, g -> new AuthGrpcService(
            g.get(ApplicationGraph.holder0.component47)
          ), List.of(), component47);
      var _type_of_component49 = map.get("component49");
      component49 = graphDraw.addNode0(_type_of_component49, new Class<?>[]{}, g -> new $BcryptProperties_ConfigValueExtractor(), List.of());
      var _type_of_component50 = map.get("component50");
      component50 = graphDraw.addNode0(_type_of_component50, new Class<?>[]{}, g -> impl.module1.bcryptProperties(
            g.get(ApplicationGraph.holder0.component8),
            g.get(ApplicationGraph.holder0.component49)
          ), List.of(), component8, component49);
      var _type_of_component51 = map.get("component51");
      component51 = graphDraw.addNode0(_type_of_component51, new Class<?>[]{}, g -> new CreateUserOperation(
            g.get(ApplicationGraph.holder0.component20),
            g.get(ApplicationGraph.holder0.component35),
            g.get(ApplicationGraph.holder0.component48),
            g.get(ApplicationGraph.holder0.component50)
          ), List.of(), component20, component35, component48, component50);
      var _type_of_component52 = map.get("component52");
      component52 = graphDraw.addNode0(_type_of_component52, new Class<?>[]{}, g -> new GetUserOperation(
            g.get(ApplicationGraph.holder0.component35),
            g.get(ApplicationGraph.holder0.component20)
          ), List.of(), component35, component20);
      var _type_of_component53 = map.get("component53");
      component53 = graphDraw.addNode0(_type_of_component53, new Class<?>[]{}, g -> new UserRestController(
            g.get(ApplicationGraph.holder0.component51),
            g.get(ApplicationGraph.holder0.component52)
          ), List.of(), component51, component52);
      var _type_of_component54 = map.get("component54");
      component54 = graphDraw.addNode0(_type_of_component54, new Class<?>[]{}, g -> new UserApiController(
            g.get(ApplicationGraph.holder0.component53)
          ), List.of(), component53);
      var _type_of_component55 = map.get("component55");
      component55 = graphDraw.addNode0(_type_of_component55, new Class<?>[]{}, g -> new $GetUser400Response_JsonWriter(), List.of());
      var _type_of_component56 = map.get("component56");
      component56 = graphDraw.addNode0(_type_of_component56, new Class<?>[]{ru.tinkoff.kora.json.common.annotation.Json.class, }, g -> impl.<GetUser400Response>jsonResponseMapper(
            g.get(ApplicationGraph.holder0.component55)
          ), List.of(), component55);
      var _type_of_component57 = map.get("component57");
      component57 = graphDraw.addNode0(_type_of_component57, new Class<?>[]{}, g -> new $UserErrorResponse_JsonWriter(), List.of());
      var _type_of_component58 = map.get("component58");
      component58 = graphDraw.addNode0(_type_of_component58, new Class<?>[]{ru.tinkoff.kora.json.common.annotation.Json.class, }, g -> impl.<UserErrorResponse>jsonResponseMapper(
            g.get(ApplicationGraph.holder0.component57)
          ), List.of(), component57);
      var _type_of_component59 = map.get("component59");
      component59 = graphDraw.addNode0(_type_of_component59, new Class<?>[]{}, g -> new UserApiServerResponseMappers.DeleteUserApiResponseMapper(
            g.get(ApplicationGraph.holder0.component56),
            g.get(ApplicationGraph.holder0.component58)
          ), List.of(), component56, component58);
      var _type_of_component60 = map.get("component60");
      component60 = graphDraw.addNode0(_type_of_component60, new Class<?>[]{io.undertow.Undertow.class, }, g -> impl.xnioWorker(
            g.valueOf(ApplicationGraph.holder0.component19).map(v -> (HttpServerConfig) v)
          ), List.of(), component19.valueOf());
      var _type_of_component61 = map.get("component61");
      component61 = graphDraw.addNode0(_type_of_component61, new Class<?>[]{}, g -> impl.undertowBlockingRequestExecutor(
            g.get(ApplicationGraph.holder0.component60).value()
          ), List.of(), component60);
      var _type_of_component62 = map.get("component62");
      component62 = graphDraw.addNode0(_type_of_component62, new Class<?>[]{}, g -> impl.module0.delete_api_users_userId(
            g.get(ApplicationGraph.holder0.component54),
            g.get(ApplicationGraph.holder0.component59),
            g.get(ApplicationGraph.holder0.component61)
          ), List.of(), component54, component59, component61);
      var _type_of_component63 = map.get("component63");
      component63 = graphDraw.addNode0(_type_of_component63, new Class<?>[]{}, g -> impl.stringJsonWriter(), List.of());
      var _type_of_component64 = map.get("component64");
      component64 = graphDraw.addNode0(_type_of_component64, new Class<?>[]{}, g -> new GetUserResponse.RolesEnum.RolesEnumJsonWriter(
            g.get(ApplicationGraph.holder0.component63)
          ), List.of(), component63);
      var _type_of_component65 = map.get("component65");
      component65 = graphDraw.addNode0(_type_of_component65, new Class<?>[]{}, g -> impl.<GetUserResponse.RolesEnum>listJsonWriterFactory(
            g.get(ApplicationGraph.holder0.component64)
          ), List.of(), component64);
      var _type_of_component66 = map.get("component66");
      component66 = graphDraw.addNode0(_type_of_component66, new Class<?>[]{}, g -> new $GetUserResponse_JsonWriter(
            g.get(ApplicationGraph.holder0.component65)
          ), List.of(), component65);
      var _type_of_component67 = map.get("component67");
      component67 = graphDraw.addNode0(_type_of_component67, new Class<?>[]{ru.tinkoff.kora.json.common.annotation.Json.class, }, g -> impl.<GetUserResponse>jsonResponseMapper(
            g.get(ApplicationGraph.holder0.component66)
          ), List.of(), component66);
      var _type_of_component68 = map.get("component68");
      component68 = graphDraw.addNode0(_type_of_component68, new Class<?>[]{}, g -> new UserApiServerResponseMappers.GetUserApiResponseMapper(
            g.get(ApplicationGraph.holder0.component67),
            g.get(ApplicationGraph.holder0.component56),
            g.get(ApplicationGraph.holder0.component58)
          ), List.of(), component67, component56, component58);
      var _type_of_component69 = map.get("component69");
      component69 = graphDraw.addNode0(_type_of_component69, new Class<?>[]{}, g -> impl.module0.get_api_users_userId(
            g.get(ApplicationGraph.holder0.component54),
            g.get(ApplicationGraph.holder0.component68),
            g.get(ApplicationGraph.holder0.component61)
          ), List.of(), component54, component68, component61);
      var _type_of_component70 = map.get("component70");
      component70 = graphDraw.addNode0(_type_of_component70, new Class<?>[]{}, g -> new $CreateUserRequest_JsonReader(), List.of());
      var _type_of_component71 = map.get("component71");
      component71 = graphDraw.addNode0(_type_of_component71, new Class<?>[]{ru.tinkoff.kora.json.common.annotation.Json.class, }, g -> impl.<CreateUserRequest>jsonRequestMapper(
            g.get(ApplicationGraph.holder0.component70)
          ), List.of(), component70);
      var _type_of_component72 = map.get("component72");
      component72 = graphDraw.addNode0(_type_of_component72, new Class<?>[]{}, g -> new $CreateUserResponse_JsonWriter(), List.of());
      var _type_of_component73 = map.get("component73");
      component73 = graphDraw.addNode0(_type_of_component73, new Class<?>[]{ru.tinkoff.kora.json.common.annotation.Json.class, }, g -> impl.<CreateUserResponse>jsonResponseMapper(
            g.get(ApplicationGraph.holder0.component72)
          ), List.of(), component72);
      var _type_of_component74 = map.get("component74");
      component74 = graphDraw.addNode0(_type_of_component74, new Class<?>[]{}, g -> new $CreateUser400Response_JsonWriter(), List.of());
      var _type_of_component75 = map.get("component75");
      component75 = graphDraw.addNode0(_type_of_component75, new Class<?>[]{ru.tinkoff.kora.json.common.annotation.Json.class, }, g -> impl.<CreateUser400Response>jsonResponseMapper(
            g.get(ApplicationGraph.holder0.component74)
          ), List.of(), component74);
      var _type_of_component76 = map.get("component76");
      component76 = graphDraw.addNode0(_type_of_component76, new Class<?>[]{}, g -> new UserApiServerResponseMappers.CreateUserApiResponseMapper(
            g.get(ApplicationGraph.holder0.component73),
            g.get(ApplicationGraph.holder0.component75),
            g.get(ApplicationGraph.holder0.component58)
          ), List.of(), component73, component75, component58);
      var _type_of_component77 = map.get("component77");
      component77 = graphDraw.addNode0(_type_of_component77, new Class<?>[]{}, g -> impl.module0.post_api_users(
            g.get(ApplicationGraph.holder0.component54),
            g.get(ApplicationGraph.holder0.component71),
            g.get(ApplicationGraph.holder0.component76),
            g.get(ApplicationGraph.holder0.component61)
          ), List.of(), component54, component71, component76, component61);
      var _type_of_component78 = map.get("component78");
      component78 = graphDraw.addNode0(_type_of_component78, new Class<?>[]{}, g -> new $ChangeUserRequest_JsonReader(), List.of());
      var _type_of_component79 = map.get("component79");
      component79 = graphDraw.addNode0(_type_of_component79, new Class<?>[]{ru.tinkoff.kora.json.common.annotation.Json.class, }, g -> impl.<ChangeUserRequest>jsonRequestMapper(
            g.get(ApplicationGraph.holder0.component78)
          ), List.of(), component78);
      var _type_of_component80 = map.get("component80");
      component80 = graphDraw.addNode0(_type_of_component80, new Class<?>[]{}, g -> new UserApiServerResponseMappers.ChangeUserApiResponseMapper(
            g.get(ApplicationGraph.holder0.component56),
            g.get(ApplicationGraph.holder0.component58)
          ), List.of(), component56, component58);
      var _type_of_component81 = map.get("component81");
      component81 = graphDraw.addNode0(_type_of_component81, new Class<?>[]{}, g -> impl.module0.put_api_users_userId(
            g.get(ApplicationGraph.holder0.component54),
            g.get(ApplicationGraph.holder0.component79),
            g.get(ApplicationGraph.holder0.component80),
            g.get(ApplicationGraph.holder0.component61)
          ), List.of(), component54, component79, component80, component61);
      var _type_of_component82 = map.get("component82");
      component82 = graphDraw.addNode0(_type_of_component82, new Class<?>[]{}, g -> new OpenApiProvider(
            g.get(ApplicationGraph.holder0.component6)
          ), List.of(), component6);
      var _type_of_component83 = map.get("component83");
      component83 = graphDraw.addNode0(_type_of_component83, new Class<?>[]{}, g -> new OpenApiController(
            g.get(ApplicationGraph.holder0.component82)
          ), List.of(), component82);
      var _type_of_component84 = map.get("component84");
      component84 = graphDraw.addNode0(_type_of_component84, new Class<?>[]{}, g -> impl.module2.get_docs_openapi_yaml(
            g.get(ApplicationGraph.holder0.component83),
            g.get(ApplicationGraph.holder0.component61)
          ), List.of(), component83, component61);
      var _type_of_component85 = map.get("component85");
      component85 = graphDraw.addNode0(_type_of_component85, new Class<?>[]{}, g -> impl.module2.options_(
            g.get(ApplicationGraph.holder0.component83),
            g.get(ApplicationGraph.holder0.component61)
          ), List.of(), component83, component61);
      var _type_of_component86 = map.get("component86");
      component86 = graphDraw.addNode0(_type_of_component86, new Class<?>[]{}, g -> impl.<String>listConfigValueExtractor(
            g.get(ApplicationGraph.holder0.component10)
          ), List.of(), component10);
      var _type_of_component87 = map.get("component87");
      component87 = graphDraw.addNode0(_type_of_component87, new Class<?>[]{}, g -> new $OpenApiManagementConfig_SwaggerUIConfig_ConfigValueExtractor(), List.of());
      var _type_of_component88 = map.get("component88");
      component88 = graphDraw.addNode0(_type_of_component88, new Class<?>[]{}, g -> new $OpenApiManagementConfig_RapidocConfig_ConfigValueExtractor(), List.of());
      var _type_of_component89 = map.get("component89");
      component89 = graphDraw.addNode0(_type_of_component89, new Class<?>[]{}, g -> new $OpenApiManagementConfig_ConfigValueExtractor(
            g.get(ApplicationGraph.holder0.component86),
            g.get(ApplicationGraph.holder0.component87),
            g.get(ApplicationGraph.holder0.component88)
          ), List.of(), component86, component87, component88);
      var _type_of_component90 = map.get("component90");
      component90 = graphDraw.addNode0(_type_of_component90, new Class<?>[]{}, g -> impl.openApiManagementConfig(
            g.get(ApplicationGraph.holder0.component8),
            g.get(ApplicationGraph.holder0.component89)
          ), List.of(), component8, component89);
      var _type_of_component91 = map.get("component91");
      component91 = graphDraw.addNode0(_type_of_component91, new Class<?>[]{}, g -> impl.openApiManagementController(
            g.get(ApplicationGraph.holder0.component90)
          ), List.of(), component90);
      var _type_of_component92 = map.get("component92");
      component92 = graphDraw.addNode0(_type_of_component92, new Class<?>[]{}, g -> impl.rapidocManagementController(
            g.get(ApplicationGraph.holder0.component90)
          ), List.of(), component90);
      var _type_of_component93 = map.get("component93");
      component93 = graphDraw.addNode0(_type_of_component93, new Class<?>[]{}, g -> impl.swaggerUIManagementController(
            g.get(ApplicationGraph.holder0.component90)
          ), List.of(), component90);
      var _type_of_component94 = map.get("component94");
      component94 = graphDraw.addNode0(_type_of_component94, new Class<?>[]{ru.tinkoff.kora.http.server.common.HttpServerModule.class, }, g -> new HttpExceptionHandler(
            g.get(ApplicationGraph.holder0.component57),
            g.get(ApplicationGraph.holder0.component48)
          ), List.of(), component57, component48);
      var _type_of_component95 = map.get("component95");
      component95 = graphDraw.addNode0(_type_of_component95, new Class<?>[]{}, g -> impl.slf4jHttpServerLoggerFactory(), List.of());
      var _type_of_component96 = map.get("component96");
      component96 = graphDraw.addNode0(_type_of_component96, new Class<?>[]{}, g -> impl.defaultHttpServerTelemetryFactory(
            g.get(ApplicationGraph.holder0.component95),
            (HttpServerMetricsFactory) null,
            (HttpServerTracerFactory) null
          ), List.of(), component95);
      var _type_of_component97 = map.get("component97");
      component97 = graphDraw.addNode0(_type_of_component97, new Class<?>[]{}, g -> impl.publicApiHandler(
            All.of(
              g.get(ApplicationGraph.holder0.component62),
              g.get(ApplicationGraph.holder0.component69),
              g.get(ApplicationGraph.holder0.component77),
              g.get(ApplicationGraph.holder0.component81),
              g.get(ApplicationGraph.holder0.component84),
              g.get(ApplicationGraph.holder0.component85),
              g.get(ApplicationGraph.holder0.component91),
              g.get(ApplicationGraph.holder0.component92),
              g.get(ApplicationGraph.holder0.component93)
              ),
            All.of(
              g.get(ApplicationGraph.holder0.component94)
              ),
            g.get(ApplicationGraph.holder0.component96),
            g.get(ApplicationGraph.holder0.component19)
          ), List.of(), component62, component69, component77, component81, component84, component85, component91, component92, component93, component94, component96, component19);
      var _type_of_component98 = map.get("component98");
      component98 = graphDraw.addNode0(_type_of_component98, new Class<?>[]{}, g -> impl.undertowPublicApiHandler(
            g.get(ApplicationGraph.holder0.component97),
            (HttpServerTracerFactory) null,
            g.get(ApplicationGraph.holder0.component19)
          ), List.of(), component97, component19);
      var _type_of_component99 = map.get("component99");
      component99 = graphDraw.addNode0(_type_of_component99, new Class<?>[]{io.undertow.Undertow.class, }, g -> impl.undertowPublicByteBufferPool(), List.of());
      var _type_of_component100 = map.get("component100");
      component100 = graphDraw.addNode0(_type_of_component100, new Class<?>[]{}, g -> impl.undertowHttpServer(
            g.valueOf(ApplicationGraph.holder0.component19).map(v -> (HttpServerConfig) v),
            g.valueOf(ApplicationGraph.holder0.component98).map(v -> (UndertowPublicApiHandler) v),
            g.get(ApplicationGraph.holder0.component60).value(),
            g.get(ApplicationGraph.holder0.component99)
          ), List.of(), component19.valueOf(), component98.valueOf(), component60, component99);
      var _type_of_component101 = map.get("component101");
      component101 = graphDraw.addNode0(_type_of_component101, new Class<?>[]{}, g -> Optional.<PrivateApiMetrics>ofNullable(
            (PrivateApiMetrics) null
          ), List.of());
      var _type_of_component102 = map.get("component102");
      component102 = graphDraw.addNode0(_type_of_component102, new Class<?>[]{}, g -> impl.privateApiHandler(
            g.valueOf(ApplicationGraph.holder0.component19).map(v -> (HttpServerConfig) v),
            g.valueOf(ApplicationGraph.holder0.component101).map(v -> (Optional<PrivateApiMetrics>) v),
            All.of(
              g.promiseOf(ApplicationGraph.holder0.component28).map(v -> (ReadinessProbe) v),
              g.promiseOf(ApplicationGraph.holder0.component100).map(v -> (ReadinessProbe) v)
              ),
            All.of(  )
          ), List.of(), component19.valueOf(), component101.valueOf());
      var _type_of_component103 = map.get("component103");
      component103 = graphDraw.addNode0(_type_of_component103, new Class<?>[]{}, g -> impl.undertowPrivateApiHandler(
            g.get(ApplicationGraph.holder0.component102)
          ), List.of(), component102);
      var _type_of_component104 = map.get("component104");
      component104 = graphDraw.addNode0(_type_of_component104, new Class<?>[]{ru.tinkoff.kora.http.server.undertow.UndertowPrivateHttpServer.class, }, g -> impl.undertowPrivateByteBufferPool(), List.of());
      var _type_of_component105 = map.get("component105");
      component105 = graphDraw.addNode0(_type_of_component105, new Class<?>[]{}, g -> impl.undertowPrivateHttpServer(
            g.valueOf(ApplicationGraph.holder0.component19).map(v -> (HttpServerConfig) v),
            g.valueOf(ApplicationGraph.holder0.component103).map(v -> (UndertowPrivateApiHandler) v),
            g.get(ApplicationGraph.holder0.component60).value(),
            g.get(ApplicationGraph.holder0.component104)
          ), List.of(), component19.valueOf(), component103.valueOf(), component60, component104);
      var _type_of_component106 = map.get("component106");
      component106 = graphDraw.addNode0(_type_of_component106, new Class<?>[]{}, g -> impl.loggingLevelConfigValueExtractor(), List.of());
      var _type_of_component107 = map.get("component107");
      component107 = graphDraw.addNode0(_type_of_component107, new Class<?>[]{}, g -> impl.loggingConfig(
            g.get(ApplicationGraph.holder0.component8),
            g.get(ApplicationGraph.holder0.component106)
          ), List.of(), component8, component106);
      var _type_of_component108 = map.get("component108");
      component108 = graphDraw.addNode0(_type_of_component108, new Class<?>[]{}, g -> impl.loggingLevelApplier(), List.of());
      var _type_of_component109 = map.get("component109");
      component109 = graphDraw.addNode0(_type_of_component109, new Class<?>[]{}, g -> impl.loggingLevelRefresher(
            g.get(ApplicationGraph.holder0.component107),
            g.get(ApplicationGraph.holder0.component108)
          ), List.of(), component107, component108);
    }
  }
}
