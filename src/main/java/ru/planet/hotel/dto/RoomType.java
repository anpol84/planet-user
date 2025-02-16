package ru.planet.hotel.dto;


import lombok.Builder;

@Builder
public record RoomType(Long id, Type type, double price) {
    public enum Type {
        STANDARD,
        SUPERIOR,
        STUDIO,
        FAMILY_ROOM,
        FAMILY_STUDIO,
        DELUX,
        SUITE,
        PRESIDENTIAL_SUITE,
        HONEYMOON_SUITE
    }
}
