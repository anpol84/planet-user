package ru.planet.feedback.dto;

public record CreateFeedbackDto(long hotelId, long userId, String body, int mark) {
}
