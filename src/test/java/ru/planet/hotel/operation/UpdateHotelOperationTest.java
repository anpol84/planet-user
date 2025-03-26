package ru.planet.hotel.operation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.common.exception.BusinessException;
import ru.planet.hotel.helper.HotelHelper;
import ru.planet.hotel.model.RoomPeople;
import ru.planet.hotel.model.RoomType;
import ru.planet.hotel.model.RoomView;
import ru.planet.hotel.model.UpdateHotelRequest;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.database.common.UpdateCount;
import ru.tinkoff.kora.database.jdbc.JdbcConnectionFactory;
import ru.tinkoff.kora.database.jdbc.JdbcHelper;
import ru.tinkoff.kora.database.jdbc.RuntimeSqlException;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.postgresql.util.PSQLState.UNIQUE_VIOLATION;

@KoraAppTest(Application.class)
public class UpdateHotelOperationTest {

    @Mock
    @TestComponent
    private HotelRepository hotelRepository;

    @Mock
    @TestComponent
    private HotelHelper hotelHelper;

    @TestComponent
    private UpdateHotelOperation updateHotelOperation;

    private UpdateHotelRequest request;

    @BeforeEach
    public void setUp() {
        request = new UpdateHotelRequest(
                "Hotel A",
                "City A",
                5,
                "http://example.com/image.jpg",
                Arrays.asList(UpdateHotelRequest.PositionsEnum.CENTER),
                Arrays.asList(UpdateHotelRequest.AdditionsEnum.WIFI),
                Arrays.asList(new RoomPeople(RoomPeople.TypeEnum.DOUBLE, 200)),
                Arrays.asList(new RoomView(RoomView.TypeEnum.SEA_VIEW, 5000)),
                Arrays.asList(new RoomType(RoomType.TypeEnum.DELUX, 555))
        );
    }

    @Test
    public void testActivate_Success() throws SQLException {
        // Arrange
        Long hotelId = 1L;
        double minPrice = 100.0;
        when(hotelHelper.calculateMin(request.roomViews(), request.roomTypes(), request.roomPeople())).thenReturn(minPrice);
        when(hotelRepository.updateHotel(any(), anyString(), anyString())).thenReturn(new UpdateCount(1));

        doNothing().when(hotelRepository).deleteHotelViews(hotelId);
        doNothing().when(hotelRepository).deleteHotelTypes(hotelId);
        doNothing().when(hotelRepository).deleteHotelPeople(hotelId);
        when(hotelHelper.extractRoomViews(request.roomViews())).thenReturn(Collections.singletonList(1L));
        when(hotelHelper.extractRoomTypes(request.roomTypes())).thenReturn(Collections.singletonList(1L));
        when(hotelHelper.extractRoomPeople(request.roomPeople())).thenReturn(Collections.singletonList(1L));
        doNothing().when(hotelRepository).saveHotelWithRoomView(hotelId, 1L);
        doNothing().when(hotelRepository).saveHotelWithRoomType(hotelId, 1L);
        doNothing().when(hotelRepository).saveHotelWithRoomPeople(hotelId, 1L);

        JdbcConnectionFactory jdbcConnectionFactory = mock(JdbcConnectionFactory.class);
        when(hotelRepository.getJdbcConnectionFactory()).thenReturn(jdbcConnectionFactory);
        ArgumentCaptor<JdbcHelper.SqlRunnable> functionCaptor = ArgumentCaptor.forClass(JdbcHelper.SqlRunnable.class);

        updateHotelOperation.activate(request, hotelId);
        verify(jdbcConnectionFactory).inTx(functionCaptor.capture());

        functionCaptor.getValue().run();


        verify(hotelRepository).updateHotel(any(), anyString(), anyString());
        verify(hotelRepository).deleteHotelViews(hotelId);
        verify(hotelRepository).deleteHotelTypes(hotelId);
        verify(hotelRepository).deleteHotelPeople(hotelId);
        verify(hotelRepository).saveHotelWithRoomView(hotelId, 1L);
        verify(hotelRepository).saveHotelWithRoomType(hotelId, 1L);
        verify(hotelRepository).saveHotelWithRoomPeople(hotelId, 1L);
    }


    @Test
    public void testActivate_HotelNotFound() {
        Long hotelId = 1L;
        when(hotelHelper.calculateMin(request.roomViews(), request.roomTypes(), request.roomPeople())).thenReturn(100.0);
        when(hotelRepository.updateHotel(any(), anyString(), anyString())).thenReturn(new UpdateCount(0));

        JdbcConnectionFactory jdbcConnectionFactory = mock(JdbcConnectionFactory.class);
        when(hotelRepository.getJdbcConnectionFactory()).thenReturn(jdbcConnectionFactory);
        ArgumentCaptor<JdbcHelper.SqlRunnable> functionCaptor = ArgumentCaptor.forClass(JdbcHelper.SqlRunnable.class);

        updateHotelOperation.activate(request, hotelId);
        verify(jdbcConnectionFactory).inTx(functionCaptor.capture());


        BusinessException exception = assertThrows(BusinessException.class, () -> {
            functionCaptor.getValue().run();
        });
        assertEquals("Такого отеля не существует", exception.getMessage());
    }


    @Test
    public void testActivate_DuplicatePositionException() {

        Long hotelId = 1L;
        when(hotelHelper.calculateMin(request.roomViews(), request.roomTypes(), request.roomPeople())).thenReturn(100.0);
        when(hotelRepository.updateHotel(any(), anyString(), anyString())).thenReturn(new UpdateCount(1));

        when(hotelRepository.updateHotel(any(), anyString(), anyString())).thenThrow(new RuntimeSqlException(new SQLException("SQL error",UNIQUE_VIOLATION.getState(), new Throwable("Duplicate entry"))));

        JdbcConnectionFactory jdbcConnectionFactory = mock(JdbcConnectionFactory.class);
        when(hotelRepository.getJdbcConnectionFactory()).thenReturn(jdbcConnectionFactory);
        ArgumentCaptor<JdbcHelper.SqlRunnable> functionCaptor = ArgumentCaptor.forClass(JdbcHelper.SqlRunnable.class);

        updateHotelOperation.activate(request, hotelId);
        verify(jdbcConnectionFactory).inTx(functionCaptor.capture());


        BusinessException exception = assertThrows(BusinessException.class, () -> {
            functionCaptor.getValue().run();
        });
        assertEquals("Такой отель уже существует", exception.getMessage());
    }

}
