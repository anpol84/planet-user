package ru.planet.gpt.properties;

import ru.tinkoff.kora.config.common.annotation.ConfigSource;

@ConfigSource("sberGpt")
public interface SberProperties {
    String token();
    String tokenUrl();
    String askUrl();
    String certPath();
    String systemQuery();
}
