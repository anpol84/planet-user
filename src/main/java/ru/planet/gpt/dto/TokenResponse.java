package ru.planet.gpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import ru.tinkoff.kora.json.common.annotation.Json;


public record TokenResponse(@JsonProperty("access_token") String accessToken, @JsonProperty("expires_at") Long expiredAt) {
}
