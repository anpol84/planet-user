package ru.planet.gpt.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.planet.hotel.dto.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GptResponse(List<Choice> choices
) {
    @JsonIgnoreProperties(ignoreUnknown = true)
    public record Choice(
            Message message
    ) {
        @JsonIgnoreProperties(ignoreUnknown = true)
        public record Message(
                String content
        ) {
        }
    }
}

