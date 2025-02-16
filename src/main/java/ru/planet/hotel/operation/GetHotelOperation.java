package ru.planet.hotel.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.common.exception.BusinessException;
import ru.planet.hotel.helper.mapper.HotelMapper;
import ru.planet.hotel.model.GetHotelResponse;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.common.Component;

@Component
@RequiredArgsConstructor
public class GetHotelOperation {
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;

    public GetHotelResponse activate(Long id) {
        var hotel = hotelRepository.getHotel(id);
        if (hotel == null) {
            throw new BusinessException("Такого отеля не существует");
        }
        var roomViews = hotelRepository.getHotelsView(id);
        var roomTypes = hotelRepository.getHotelType(id);
        var roomPeople = hotelRepository.getHotelPeople(id);
        return hotelMapper.buildGetHotelResponse(hotel, roomViews, roomTypes, roomPeople);
    }
}
