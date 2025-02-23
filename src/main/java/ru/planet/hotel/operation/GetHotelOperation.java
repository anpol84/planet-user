package ru.planet.hotel.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.auth.helper.JwtService;
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
    private final JwtService jwtService;

    public GetHotelResponse activate(Long id, String token) {
        var hotel = hotelRepository.getHotel(id);
        if (hotel == null) {
            throw new BusinessException("Такого отеля не существует");
        }
        var claims = token == null ? null : jwtService.getClaims(token);
        var isFavourite = claims != null && hotelRepository.validateFavouriteHotel(hotel.id(),
                Long.valueOf(String.valueOf(claims.get("user_id"))));
        var roomViews = hotelRepository.getHotelsView(id);
        var roomTypes = hotelRepository.getHotelType(id);
        var roomPeople = hotelRepository.getHotelPeople(id);
        return hotelMapper.buildGetHotelResponse(hotel, roomViews, roomTypes, roomPeople, isFavourite);
    }
}
