package ru.planet.hotel.operation;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.auth.helper.JwtService;
import ru.planet.hotel.dto.GetHotel;
import ru.planet.hotel.helper.mapper.HotelMapper;
import ru.planet.hotel.model.GetHotelsResponse;
import ru.planet.hotel.model.HotelWithoutExtensions;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@KoraAppTest(Application.class)
public class GetHotelsOperationTest {

    @Mock
    @TestComponent
    private HotelRepository hotelRepository;

    @Mock
    @TestComponent
    private HotelMapper hotelMapper;

    @Mock
    @TestComponent
    private JwtService jwtService;

    @TestComponent
    private GetHotelsOperation getHotelsOperation;

    private List<GetHotel> hotels;

    @BeforeEach
    public void setUp() {
        hotels = new ArrayList<>();
        var hotel = GetHotel.builder()
                .id(1L)
                .name("Hotel Test")
                .city("Test City")
                .imageUrl("http://example.com/image.jpg")
                .minPrice(100.0)
                .avgRate(4.5)
                .stars(5)
                .additions(Collections.emptyList())
                .positions(Collections.emptyList())
                .build();
        var hotel2 = GetHotel.builder()
                .id(1L)
                .name("Hotel Test")
                .city("Test City")
                .imageUrl("http://example.com/image.jpg")
                .minPrice(100.0)
                .avgRate(4.5)
                .stars(5)
                .additions(Collections.emptyList())
                .positions(Collections.emptyList())
                .build();
        hotels.add(hotel);
        hotels.add(hotel2);
    }

    @Test
    public void testActivate_WithToken() {
        String token = "token";
        Claims claims = mock(Claims.class);
        when(jwtService.getClaims("token")).thenReturn(claims);
        when(claims.get("user_id")).thenReturn(1L);
        when(hotelRepository.getHotels()).thenReturn(hotels);

        for (var hotel : hotels) {
            when(hotelRepository.validateFavouriteHotel(hotel.id(), 1L)).thenReturn(true);
            when(hotelMapper.buildHotel(hotel, true)).thenReturn(new HotelWithoutExtensions(1L, "Hotel One", "City A", 5, 4.5, 100.0, "imageUrl1", Collections.emptyList(), Collections.emptyList(), true));
        }

        GetHotelsResponse response = getHotelsOperation.activate(token);

        assertNotNull(response);
        assertEquals(2, response.hotels().size());
        assertTrue(response.hotels().get(0).isFavourite());
        assertTrue(response.hotels().get(1).isFavourite());
    }

    @Test
    public void testActivate_WithoutToken() {
        when(hotelRepository.getHotels()).thenReturn(hotels);

        for (var hotel : hotels) {
            when(hotelRepository.validateFavouriteHotel(hotel.id(), null)).thenReturn(false);
            when(hotelMapper.buildHotel(hotel, false)).thenReturn(new HotelWithoutExtensions(1L, "Hotel One", "City A", 5, 4.5, 100.0, "imageUrl1", Collections.emptyList(), Collections.emptyList(), false));
        }

        GetHotelsResponse response = getHotelsOperation.activate(null);

        assertNotNull(response);
        assertEquals(2, response.hotels().size());
        assertFalse(response.hotels().get(0).isFavourite());
        assertFalse(response.hotels().get(1).isFavourite());
    }

    @Test
    public void testActivate_NoHotels() {
        when(hotelRepository.getHotels()).thenReturn(Collections.emptyList());

        GetHotelsResponse response = getHotelsOperation.activate(null);
        assertNotNull(response);
        assertTrue(response.hotels().isEmpty());
    }
}
