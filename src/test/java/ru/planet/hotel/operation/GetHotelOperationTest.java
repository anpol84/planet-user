package ru.planet.hotel.operation;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.auth.helper.JwtService;
import ru.planet.common.exception.BusinessException;
import ru.planet.hotel.dto.GetHotel;
import ru.planet.hotel.helper.mapper.HotelMapper;
import ru.planet.hotel.model.GetHotelResponse;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@KoraAppTest(Application.class)
public class GetHotelOperationTest {

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
    private GetHotelOperation getHotelOperation;

    private GetHotel hotel;

    @BeforeEach
    public void setUp() {
        hotel = GetHotel.builder()
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
    }

    @Test
    public void testActivate_Success() {

        when(hotelRepository.getHotel(1L)).thenReturn(hotel);
        when(hotelRepository.getHotelsView(1L)).thenReturn(Collections.emptyList());
        when(hotelRepository.getHotelType(1L)).thenReturn(Collections.emptyList());
        when(hotelRepository.getHotelPeople(1L)).thenReturn(Collections.emptyList());
        when(hotelMapper.buildGetHotelResponse(any(), any(), any(), any(), anyBoolean())).thenReturn(new GetHotelResponse(
                hotel.id(),
                hotel.name(),
                hotel.city(),
                hotel.stars(),
                hotel.avgRate(),
                hotel.minPrice(),
                hotel.imageUrl(),
                false,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        ));

        GetHotelResponse response = getHotelOperation.activate(1L, null);

        assertNotNull(response);
        assertEquals(hotel.id(), response.id());
        assertEquals(hotel.name(), response.name());
        assertEquals(hotel.city(), response.city());
        assertEquals(hotel.stars(), response.stars());
        assertEquals(hotel.avgRate(), response.avgRate());
        assertEquals(hotel.minPrice(), response.minPrice());
        assertEquals(hotel.imageUrl(), response.imageUrl());
        assertFalse(response.isFavourite());
    }

    @Test
    public void testActivate_HotelNotFound() {
        when(hotelRepository.getHotel(1L)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, ()
                -> {
            getHotelOperation.activate(1L, null);
        });
        assertEquals("Такого отеля не существует", exception.getMessage());
    }

    @Test
    public void testActivate_WithToken_FavouriteHotel() {
        when(hotelRepository.getHotel(1L)).thenReturn(hotel);
        Claims claims = mock(Claims.class);

        when(jwtService.getClaims("token")).thenReturn(claims);

        when(claims.get("user_id")).thenReturn(1L);
        when(hotelRepository.validateFavouriteHotel(hotel.id(), 1L)).thenReturn(true);
        when(hotelRepository.getHotelsView(1L)).thenReturn(Collections.emptyList());
        when(hotelRepository.getHotelType(1L)).thenReturn(Collections.emptyList());
        when(hotelRepository.getHotelPeople(1L)).thenReturn(Collections.emptyList());

        when(hotelMapper.buildGetHotelResponse(any(), any(), any(), any(), anyBoolean())).thenReturn(new GetHotelResponse(
                hotel.id(),
                hotel.name(),
                hotel.city(),
                hotel.stars(),
                hotel.avgRate(),
                hotel.minPrice(),
                hotel.imageUrl(),
                true,
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList(),
                Collections.emptyList()
        ));

        GetHotelResponse response = getHotelOperation.activate(1L, "token");

        assertTrue(response.isFavourite());
    }

}
