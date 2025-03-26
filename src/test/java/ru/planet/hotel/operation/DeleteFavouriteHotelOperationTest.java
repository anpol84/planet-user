package ru.planet.hotel.operation;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.common.exception.BusinessException;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.database.common.UpdateCount;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@KoraAppTest(Application.class)
public class DeleteFavouriteHotelOperationTest {

    @Mock
    @TestComponent
    private HotelRepository hotelRepository;

    @TestComponent
    private DeleteFavouriteHotelOperation deleteFavouriteHotelOperation;

    @Test
    public void testActivate_SuccessfulDeletion() {
        Long userId = 1L;
        Long hotelId = 2L;

        when(hotelRepository.deleteFavouriteHotel(userId, hotelId)).thenReturn(new UpdateCount(1));

        deleteFavouriteHotelOperation.activate(userId, hotelId);

        verify(hotelRepository).deleteFavouriteHotel(userId, hotelId);
    }

    @Test
    public void testActivate_FailureDeletion() {
        Long userId = 1L;
        Long hotelId = 2L;

        when(hotelRepository.deleteFavouriteHotel(userId, hotelId)).thenReturn(new UpdateCount(0));

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            deleteFavouriteHotelOperation.activate(userId, hotelId);
        });

        assertEquals("Отеля или пользователя не существует", thrown.getMessage());
    }
}
