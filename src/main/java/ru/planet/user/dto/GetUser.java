package ru.planet.user.dto;

import ru.tinkoff.kora.database.jdbc.EntityJdbc;

@EntityJdbc
public record GetUser(Long id, String login) {
}
