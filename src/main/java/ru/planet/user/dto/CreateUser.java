package ru.planet.user.dto;

import ru.tinkoff.kora.database.jdbc.EntityJdbc;

@EntityJdbc
public record CreateUser(String login, String password) {
}

