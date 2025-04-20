package ru.planet.hotel.helper.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.planet.gpt.dto.GptHotel;
import ru.planet.hotel.dto.*;
import ru.planet.hotel.model.CreateHotelRequest;
import ru.planet.hotel.model.GetHotelResponse;
import ru.planet.hotel.model.HotelWithoutExtensions;

import java.util.List;

@Mapper
public interface HotelMapper {

    CreateHotel buildHotel(CreateHotelRequest request);

    GetHotelResponse buildGetHotelResponse(GetHotel getHotel,
                                           List<RoomView> roomViews,
                                           List<RoomType> roomTypes,
                                           List<RoomPeople> roomPeople,
                                           boolean isFavourite);

    HotelWithoutExtensions buildHotel(GetHotel hotel, boolean isFavourite);

    @Mapping(target = "minPrice", source = "minPrice")
    GptHotel buildPrice(GptHotel hotel, int minPrice);
}
