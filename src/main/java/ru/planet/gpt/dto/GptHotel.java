package ru.planet.gpt.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import ru.planet.hotel.dto.*;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record GptHotel(String name, String city, @JsonProperty("avg_rate") double avgRate,
                       @JsonProperty("min_price") int minPrice, int stars, List<Position> positions,
                       List<Addition> additions, List<RoomPeople> roomPeople, List<RoomView> roomView,
                       List<RoomType> roomType) {
}
