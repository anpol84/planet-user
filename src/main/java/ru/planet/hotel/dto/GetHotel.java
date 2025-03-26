package ru.planet.hotel.dto;

import lombok.Builder;
import ru.tinkoff.kora.database.jdbc.EntityJdbc;

import java.util.List;

@EntityJdbc
@Builder
public record GetHotel(Long id, String name, String city, String imageUrl, double minPrice, double avgRate,
                       int stars, List<Addition> additions, List<Position> positions) {
}
