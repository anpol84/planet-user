package ru.planet.gpt.dto;

import ru.tinkoff.kora.json.common.annotation.Json;

import java.util.List;

@Json
public record GigaChatDto(String model, List<Message> messages) {
    @Json
    public record Message(String role, String content) {}
}
