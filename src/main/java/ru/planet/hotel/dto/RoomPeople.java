package ru.planet.hotel.dto;


import lombok.Builder;

@Builder
public record RoomPeople(Long id, Type type, double price) {

    public enum Type {
        SINGLE,
        DOUBLE,
        TWIN,
        DBL_EXB,
        TRIPLE,

    }
}
