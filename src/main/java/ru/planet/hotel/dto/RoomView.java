package ru.planet.hotel.dto;


import lombok.Builder;
import ru.tinkoff.kora.database.jdbc.EntityJdbc;

@Builder
@EntityJdbc
public record RoomView(Long id, Type type, double price) {

    public enum Type {
        SEA_VIEW,
        CITY_VIEW,
        GARDEN_VIEW,
        POOL_VIEW
    }
}
