package ru.planet.user.configuration.properties;

import ru.tinkoff.kora.config.common.annotation.ConfigSource;

@ConfigSource("bcrypt")
public record BcryptProperties(String salt){
}

