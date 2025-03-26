package ru.planet.hotel.operation;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.auth.helper.JwtService;
import ru.planet.hotel.dto.GetHotel;
import ru.planet.hotel.helper.mapper.HotelMapper;
import ru.planet.hotel.model.GetFilteredHotelsResponse;
import ru.planet.hotel.model.GetHotelResponse;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@KoraAppTest(Application.class)
public class GetFilteredHotelsOperationTest {
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
    private GetFilteredHotelsOperation getFilteredHotelsOperation;

    @Test
    public void testActivate_WithFilteredHotels() {
        String city = "City A";
        double minPrice = 100.0;
        String token = null;

        GetHotel hotel1 = new GetHotel(1L, "Hotel One", "City A", "imageUrl1", 100.0, 4.5, 5, Collections.emptyList(), Collections.emptyList());
        GetHotel hotel2 = new GetHotel(2L, "Hotel Two", "City A", "imageUrl2", 150.0, 4.0, 4, Collections.emptyList(), Collections.emptyList());

        when(hotelRepository.getHotelsWithFilter(city, minPrice)).thenReturn(Arrays.asList(hotel1, hotel2));

        when(hotelMapper.buildGetHotelResponse(hotel1, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), false))
                .thenReturn(new GetHotelResponse(1L, "Hotel One", "City A", 5, 4.5, 100.0, "imageUrl1", false, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()));
        when(hotelMapper.buildGetHotelResponse(hotel2, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), false))
                .thenReturn(new GetHotelResponse(2L, "Hotel Two", "City A", 4, 4.0, 150.0, "imageUrl2", false, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()));

        GetFilteredHotelsResponse response = getFilteredHotelsOperation.activate(city, minPrice, token);

        assertFalse(response.isError());
        assertNotNull(response.hotels());
        assertEquals(2, response.hotels().size());
        assertEquals("Hotel One", response.hotels().get(0).name());
        assertEquals("Hotel Two", response.hotels().get(1).name());
    }


    @Test
    public void testActivate_NoFilteredHotels_FallsBackToBestHotels() {
        String city = "City A";
        double minPrice = 100.0;
        String token = null;

        when(hotelRepository.getHotelsWithFilter(city, minPrice)).thenReturn(Collections.emptyList());

        GetHotel bestHotel = new GetHotel(3L, "Best Hotel", "City A", "bestImageUrl", 90.0, 4.8, 5, Collections.emptyList(), Collections.emptyList());
        when(hotelRepository.getBestHotels()).thenReturn(Collections.singletonList(bestHotel));

        when(hotelMapper.buildGetHotelResponse(bestHotel, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), false))
                .thenReturn(new GetHotelResponse(3L, "Best Hotel", "City A", 5, 4.8, 90.0, "bestImageUrl", false, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()));

        GetFilteredHotelsResponse response = getFilteredHotelsOperation.activate(city, minPrice, token);

        assertTrue(response.isError());
        assertNotNull(response.hotels());
        assertEquals(1, response.hotels().size());
        assertEquals("Best Hotel", response.hotels().get(0).name());
    }

    @Test
    public void testActivate_WithTokenAndFavouriteHotel() {
        String city = "City A";
        double minPrice = 100.0;
        String token = "valid.token";

        Claims claims = mock(Claims.class);

        when(jwtService.getClaims(token)).thenReturn(claims);

        when(claims.get("user_id")).thenReturn(1L);

        GetHotel hotel1 = new GetHotel(1L, "Hotel One", "City A", "imageUrl1", 100.0, 4.5, 5, Collections.emptyList(), Collections.emptyList());

        when(hotelRepository.getHotelsWithFilter(city, minPrice)).thenReturn(Collections.singletonList(hotel1));

        when(hotelRepository.validateFavouriteHotel(hotel1.id(), 1L)).thenReturn(true);

        when(hotelMapper.buildGetHotelResponse(hotel1, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), true))
                .thenReturn(new GetHotelResponse(1L, "Hotel One", "City A", 5, 4.5, 100.0, "imageUrl1", true, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()));

        GetFilteredHotelsResponse response = getFilteredHotelsOperation.activate(city, minPrice, token);

        assertFalse(response.isError());
        assertNotNull(response.hotels());
        assertEquals(1, response.hotels().size());
        assertTrue(response.hotels().get(0).isFavourite());
    }

    @Test
    public void testActivate_WithNullToken() {

        String city = "City A";
        double minPrice = 100.0;
        String token = null;

        GetHotel hotel1 = new GetHotel(1L, "Hotel One", "City A", "imageUrl1", 100.0, 4.5, 5, Collections.emptyList(), Collections.emptyList());

        when(hotelRepository.getHotelsWithFilter(city, minPrice)).thenReturn(Collections.singletonList(hotel1));

        when(hotelMapper.buildGetHotelResponse(hotel1, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), false))
                .thenReturn(new GetHotelResponse(1L, "Hotel One", "City A", 5, 4.5, 100.0, "imageUrl1", false, Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList(), Collections.emptyList()));

        GetFilteredHotelsResponse response = getFilteredHotelsOperation.activate(city, minPrice, token);

        assertFalse(response.isError());
        assertNotNull(response.hotels());
        assertEquals(1, response.hotels().size());
        assertFalse(response.hotels().get(0).isFavourite());
    }

}
