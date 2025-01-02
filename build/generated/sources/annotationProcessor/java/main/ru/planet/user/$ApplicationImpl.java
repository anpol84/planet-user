package ru.planet.user;

import ru.planet.user.api.UserApiControllerModule;
import ru.planet.user.configuration.properties.BcryptPropertiesModule;
import ru.planet.user.controller.OpenApiControllerModule;
import ru.tinkoff.kora.common.annotation.Generated;

@Generated("ru.tinkoff.kora.kora.app.annotation.processor.KoraAppProcessor")
public final class $ApplicationImpl implements Application {
  public final UserApiControllerModule module0 = new UserApiControllerModule(){};

  public final BcryptPropertiesModule module1 = new BcryptPropertiesModule(){};

  public final OpenApiControllerModule module2 = new OpenApiControllerModule(){};
}
