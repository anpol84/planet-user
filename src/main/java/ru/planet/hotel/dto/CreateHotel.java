package ru.planet.hotel.dto;

import ru.tinkoff.kora.database.jdbc.EntityJdbc;

import java.util.List;

@EntityJdbc
public record CreateHotel(Long id,
                          String name,
                          String city,
                          String imageUrl,
                          int stars,
                          List<Addition> additions,
                          List<Position> positions) {
}
