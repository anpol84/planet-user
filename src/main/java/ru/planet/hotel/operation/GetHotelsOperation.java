package ru.planet.hotel.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.auth.helper.JwtService;
import ru.planet.hotel.helper.mapper.HotelMapper;
import ru.planet.hotel.model.GetHotelsResponse;
import ru.planet.hotel.model.HotelWithoutExtensions;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.common.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class GetHotelsOperation {
    private final HotelRepository hotelRepository;
    private final HotelMapper hotelMapper;
    private final JwtService jwtService;

    public GetHotelsResponse activate(String token) {
        var hotels = hotelRepository.getHotels();
        List<HotelWithoutExtensions> hotelsWithoutExtensions = new ArrayList<>();
        var claims = token == null ? null : jwtService.getClaims(token);
        for (var hotel : hotels) {
            var isFavourite = claims != null && hotelRepository.validateFavouriteHotel(hotel.id(), Long.valueOf(String.valueOf(claims.get("user_id"))));
            hotelsWithoutExtensions.add(hotelMapper.buildHotel(hotel, isFavourite));
        }
        return new GetHotelsResponse(hotelsWithoutExtensions);
    }
}
