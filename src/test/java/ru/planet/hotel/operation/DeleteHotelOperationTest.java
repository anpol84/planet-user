package ru.planet.hotel.operation;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.common.exception.BusinessException;
import ru.planet.feedback.repository.FeedbackRepository;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.database.common.UpdateCount;
import ru.tinkoff.kora.database.jdbc.JdbcConnectionFactory;
import ru.tinkoff.kora.database.jdbc.JdbcHelper;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@KoraAppTest(Application.class)
public class DeleteHotelOperationTest {

    @Mock
    @TestComponent
    private HotelRepository hotelRepository;

    @Mock
    @TestComponent
    private FeedbackRepository feedbackRepository;

    @TestComponent
    private DeleteHotelOperation deleteHotelOperation;


    @Test
    public void testActivate_SuccessfulDeletion() throws SQLException {
        Long hotelId = 1L;

        when(hotelRepository.deleteHotel(hotelId)).thenReturn(new UpdateCount(1));


        JdbcConnectionFactory jdbcConnectionFactory = mock(JdbcConnectionFactory.class);
        when(hotelRepository.getJdbcConnectionFactory()).thenReturn(jdbcConnectionFactory);
        ArgumentCaptor<JdbcHelper.SqlRunnable> functionCaptor = ArgumentCaptor.forClass(JdbcHelper.SqlRunnable.class);

        deleteHotelOperation.activate(hotelId);
        verify(jdbcConnectionFactory).inTx(functionCaptor.capture());

        functionCaptor.getValue().run();


        verify(hotelRepository).deleteHotelViews(hotelId);
        verify(hotelRepository).deleteHotelTypes(hotelId);
        verify(hotelRepository).deleteHotelPeople(hotelId);
        verify(hotelRepository).deleteHotelFavourites(hotelId);
        verify(feedbackRepository).deleteFeedbacksByHotelId(hotelId);
        verify(hotelRepository).deleteHotel(hotelId);

    }

    @Test
    public void testActivate_HotelDoesNotExist() {
        Long hotelId = 1L;

        when(hotelRepository.deleteHotel(hotelId)).thenReturn(new UpdateCount(0));

        JdbcConnectionFactory jdbcConnectionFactory = mock(JdbcConnectionFactory.class);
        when(hotelRepository.getJdbcConnectionFactory()).thenReturn(jdbcConnectionFactory);
        ArgumentCaptor<JdbcHelper.SqlRunnable> functionCaptor = ArgumentCaptor.forClass(JdbcHelper.SqlRunnable.class);

        deleteHotelOperation.activate(hotelId);
        verify(jdbcConnectionFactory).inTx(functionCaptor.capture());

        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            functionCaptor.getValue().run();
        });

        assertEquals("Такого отеля не существует", thrown.getMessage());
    }
}
