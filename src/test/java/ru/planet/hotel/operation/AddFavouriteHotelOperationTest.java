package ru.planet.hotel.operation;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.common.exception.BusinessException;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.database.jdbc.RuntimeSqlException;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.postgresql.util.PSQLState.FOREIGN_KEY_VIOLATION;
import static org.postgresql.util.PSQLState.UNIQUE_VIOLATION;

@KoraAppTest(Application.class)
public class AddFavouriteHotelOperationTest {
    @Mock
    @TestComponent
    private HotelRepository hotelRepository;
    @TestComponent
    private AddFavouriteHotelOperation addFavouriteHotelOperation;

    @Test
    public void testActivate_AddsHotelToFavourites() {
        Long userId = 1L;
        Long hotelId = 1L;

        addFavouriteHotelOperation.activate(userId, hotelId);

        verify(hotelRepository).addFavouriteHotel(userId, hotelId);
    }

    @Test
    public void testActivate_ThrowsBusinessException_DuplicatePosition() {
        Long userId = 1L;
        Long hotelId = 1L;

        doThrow(new RuntimeSqlException(new SQLException("SQL error", UNIQUE_VIOLATION.getState(), new Throwable("Duplicate entry"))))
                .when(hotelRepository).addFavouriteHotel(userId, hotelId);

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            addFavouriteHotelOperation.activate(userId, hotelId);
        });

        assertEquals("Этот отель уже добавлен в избранное", thrown.getMessage());
    }

    @Test
    public void testActivate_ThrowsBusinessException_NotFoundKey() {
        Long userId = 1L;
        Long hotelId = 1L;

        RuntimeSqlException exception = mock(RuntimeSqlException.class);
        when(exception.getMessage()).thenReturn("Not found key");

        doThrow(new RuntimeSqlException(new SQLException("SQL error", FOREIGN_KEY_VIOLATION.getState(), new Throwable("Duplicate entry"))))
                .when(hotelRepository).addFavouriteHotel(userId, hotelId);

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            addFavouriteHotelOperation.activate(userId, hotelId);
        });

        assertEquals("Отеля или пользователя не существует", thrown.getMessage());
    }

    @Test
    public void testActivate_ThrowsRuntimeSqlException_Other() {
        Long userId = 1L;
        Long hotelId = 1L;

        doThrow(new RuntimeSqlException(new SQLException("SQL error", "other error", new Throwable("Duplicate entry"))))
                .when(hotelRepository).addFavouriteHotel(userId, hotelId);

        RuntimeSqlException thrown = assertThrows(RuntimeSqlException.class, () -> {
            addFavouriteHotelOperation.activate(userId, hotelId);
        });

        assertEquals("SQL error", thrown.getMessage());
    }
}
