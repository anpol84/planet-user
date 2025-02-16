package ru.planet.hotel.helper.mapper;

import org.mapstruct.Mapper;
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
                                           List<RoomPeople> roomPeople);

    HotelWithoutExtensions buildHotel(GetHotel hotel);
}
