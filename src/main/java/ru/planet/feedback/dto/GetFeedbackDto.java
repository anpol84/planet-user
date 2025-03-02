package ru.planet.feedback.dto;

import ru.planet.feedback.repository.FeedbackRepository;
import ru.tinkoff.kora.common.Mapping;
import ru.tinkoff.kora.database.jdbc.EntityJdbc;

import java.time.LocalDateTime;

@EntityJdbc
public record GetFeedbackDto(Long id, Long userId, int mark, String body,
                             @Mapping(FeedbackRepository.DateMapper.class) LocalDateTime createdAt) {
}
