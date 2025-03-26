package ru.planet.hotel.operation;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.common.exception.BusinessException;
import ru.planet.hotel.dto.GetHotel;
import ru.planet.hotel.helper.mapper.HotelMapper;
import ru.planet.hotel.model.GetFavouriteHotelsResponse;
import ru.planet.hotel.model.HotelWithoutExtensions;
import ru.planet.hotel.repository.HotelRepository;
import ru.planet.user.dto.GetUser;
import ru.planet.user.repository.UserRepository;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@KoraAppTest(Application.class)
public class GetFavouriteHotelsOperationTest {

    @Mock
    @TestComponent
    private HotelRepository hotelRepository;

    @Mock
    @TestComponent
    private HotelMapper hotelMapper;

    @Mock
    @TestComponent
    private UserRepository userRepository;

    @TestComponent
    private GetFavouriteHotelsOperation getFavouriteHotelsOperation;


    @Test
    public void testActivate_UserDoesNotExist() {
        Long userId = 1L;

        when(userRepository.getUser(userId)).thenReturn(null);

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            getFavouriteHotelsOperation.activate(userId);
        });

        assertEquals("Такого пользователя не существует", thrown.getMessage());
    }

    @Test
    public void testActivate_UserExists_NoFavouriteHotels() {

        Long userId = 1L;
        when(userRepository.getUser(userId)).thenReturn(new GetUser(1L, "123"));

        when(hotelRepository.getFavouriteHotels(userId)).thenReturn(Collections.emptyList());

        GetFavouriteHotelsResponse response = getFavouriteHotelsOperation.activate(userId);

        assertNotNull(response);
        assertNotNull(response.hotels());
        assertTrue(response.hotels().isEmpty());
    }

    @Test
    public void testActivate_UserExists_WithFavouriteHotels() {
        Long userId = 1L;
        when(userRepository.getUser(userId)).thenReturn(new GetUser(1L, "123"));

        GetHotel hotel1 = new GetHotel(1L, "Hotel One", "City A", "123", 4.5, 100.0, 5, Collections.emptyList(), Collections.emptyList());
        GetHotel hotel2 = new GetHotel(2L, "Hotel Two", "City B", "456", 4.0, 80.0, 5, Collections.emptyList(), Collections.emptyList());

        when(hotelRepository.getFavouriteHotels(userId)).thenReturn(Arrays.asList(hotel1, hotel2));

        when(hotelMapper.buildHotel(hotel1, true)).thenReturn(new HotelWithoutExtensions(1L, "Hotel One", "City A", 5, 4.5, 100.0, "imageUrl1", Collections.emptyList(), Collections.emptyList(), true));
        when(hotelMapper.buildHotel(hotel2, true)).thenReturn(new HotelWithoutExtensions(2L, "Hotel Two", "City B", 4, 4.0, 80.0, "imageUrl2", Collections.emptyList(), Collections.emptyList(), true));

        GetFavouriteHotelsResponse response = getFavouriteHotelsOperation.activate(userId);

        assertNotNull(response);
        assertNotNull(response.hotels());
        assertEquals(2, response.hotels().size());

        assertEquals(1L, response.hotels().get(0).id());
        assertEquals("Hotel One", response.hotels().get(0).name());

        assertEquals(2L, response.hotels().get(1).id());
        assertEquals("Hotel Two", response.hotels().get(1).name());
    }
}
