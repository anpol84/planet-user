package ru.planet.user.dto;

import ru.tinkoff.kora.database.jdbc.EntityJdbc;

import javax.annotation.Nullable;

@EntityJdbc
public record User (Long id, String login, @Nullable String password, @Nullable String city) {
}

