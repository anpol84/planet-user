package ru.planet.hotel.operation;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.common.exception.BusinessException;
import ru.planet.hotel.dto.Addition;
import ru.planet.hotel.dto.CreateHotel;
import ru.planet.hotel.dto.Position;
import ru.planet.hotel.helper.HotelHelper;
import ru.planet.hotel.helper.mapper.HotelMapper;
import ru.planet.hotel.model.CreateHotelRequest;
import ru.planet.hotel.model.RoomPeople;
import ru.planet.hotel.model.RoomType;
import ru.planet.hotel.model.RoomView;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.database.jdbc.JdbcConnectionFactory;
import ru.tinkoff.kora.database.jdbc.JdbcHelper;
import ru.tinkoff.kora.database.jdbc.RuntimeSqlException;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import java.sql.SQLException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.postgresql.util.PSQLState.UNIQUE_VIOLATION;

@KoraAppTest(Application.class)
public class CreateHotelOperationTest {
    @Mock
    @TestComponent
    private HotelRepository hotelRepository;
    @Mock
    @TestComponent
    private HotelMapper hotelMapper;
    @Mock
    @TestComponent
    private HotelHelper hotelHelper;
    @TestComponent
    private CreateHotelOperation createHotelOperation;


    @Test
    public void testActivate_CreatesHotelSuccessfully() throws SQLException {

        CreateHotelRequest request = new CreateHotelRequest(
                "Test Hotel",
                "Test City",
                5,
                "http://example.com/image.jpg",
                Collections.singletonList(CreateHotelRequest.PositionsEnum.CENTER),
                Collections.singletonList(CreateHotelRequest.AdditionsEnum.WIFI),
                Collections.singletonList(new RoomPeople(RoomPeople.TypeEnum.TRIPLE, 900)),
                Collections.singletonList(new RoomView(RoomView.TypeEnum.SEA_VIEW, 222)),
                Collections.singletonList(new RoomType(RoomType.TypeEnum.PRESIDENTIAL_SUITE, 232))
        );

        JdbcConnectionFactory jdbcConnectionFactory = mock(JdbcConnectionFactory.class);

        CreateHotel hotel = new CreateHotel(1L, "Test Hotel", "Test City", "http://example.com/image.jpg", 5,
                Collections.singletonList(Addition.WIFI),
                Collections.singletonList(Position.CENTER));

        when(hotelMapper.buildHotel(request)).thenReturn(hotel);
        when(hotelHelper.extractRoomViews(request.roomViews())).thenReturn(Collections.singletonList(1L));
        when(hotelHelper.extractRoomTypes(request.roomTypes())).thenReturn(Collections.singletonList(2L));
        when(hotelHelper.extractRoomPeople(request.roomPeople())).thenReturn(Collections.singletonList(3L));
        when(hotelHelper.calculateMin(request.roomViews(), request.roomTypes(), request.roomPeople())).thenReturn(100.0);

        when(hotelRepository.getJdbcConnectionFactory()).thenReturn(jdbcConnectionFactory);
        when(hotelRepository.saveHotel(any(CreateHotel.class), anyString(), anyString(), anyDouble())).thenReturn(1L);

        ArgumentCaptor<JdbcHelper.SqlRunnable> functionCaptor = ArgumentCaptor.forClass(JdbcHelper.SqlRunnable.class);

        createHotelOperation.activate(request);
        verify(jdbcConnectionFactory).inTx(functionCaptor.capture());
        functionCaptor.getValue().run();

        verify(hotelRepository).saveHotel(any(CreateHotel.class), anyString(), anyString(), anyDouble());
        verify(hotelRepository).saveHotelWithRoomView(1L, 1L);
        verify(hotelRepository).saveHotelWithRoomType(1L, 2L);
        verify(hotelRepository).saveHotelWithRoomPeople(1L, 3L);
    }


    @Test
    public void testActivate_ThrowsBusinessException_DuplicateHotel() {
        // Arrange
        CreateHotelRequest request = new CreateHotelRequest(
                "Test Hotel",
                "Test City",
                5,
                "http://example.com/image.jpg",
                Collections.singletonList(CreateHotelRequest.PositionsEnum.CENTER),
                Collections.singletonList(CreateHotelRequest.AdditionsEnum.WIFI),
                Collections.singletonList(new RoomPeople(RoomPeople.TypeEnum.TRIPLE, 900)),
                Collections.singletonList(new RoomView(RoomView.TypeEnum.SEA_VIEW, 222)),
                Collections.singletonList(new RoomType(RoomType.TypeEnum.PRESIDENTIAL_SUITE, 232))
        );

        CreateHotel hotel = new CreateHotel(1L, "Test Hotel", "Test City", "http://example.com/image.jpg", 5,
                Collections.singletonList(Addition.WIFI),
                Collections.singletonList(Position.CENTER));

        when(hotelMapper.buildHotel(request)).thenReturn(hotel);
        when(hotelHelper.extractRoomViews(request.roomViews())).thenReturn(Collections.singletonList(1L));
        when(hotelHelper.extractRoomTypes(request.roomTypes())).thenReturn(Collections.singletonList(2L));
        when(hotelHelper.extractRoomPeople(request.roomPeople())).thenReturn(Collections.singletonList(3L));
        when(hotelHelper.calculateMin(request.roomViews(), request.roomTypes(), request.roomPeople())).thenReturn(100.0);

        when(hotelRepository.saveHotel(any(CreateHotel.class), anyString(), anyString(), anyDouble()))
                .thenThrow(new RuntimeSqlException(new SQLException("SQL error", UNIQUE_VIOLATION.getState(), new Throwable("Duplicate entry"))));
        JdbcConnectionFactory jdbcConnectionFactory = mock(JdbcConnectionFactory.class);
        when(hotelRepository.getJdbcConnectionFactory()).thenReturn(jdbcConnectionFactory);
        ArgumentCaptor<JdbcHelper.SqlRunnable> functionCaptor = ArgumentCaptor.forClass(JdbcHelper.SqlRunnable.class);

        createHotelOperation.activate(request);
        verify(jdbcConnectionFactory).inTx(functionCaptor.capture());

        // Act & Assert
        BusinessException thrown = assertThrows(BusinessException.class, () -> {
            functionCaptor.getValue().run();
        });

        assertEquals("Такой отель уже существует", thrown.getMessage());
    }
}

