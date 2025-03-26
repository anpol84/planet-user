package ru.planet.hotel.helper;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.hotel.model.RoomPeople;
import ru.planet.hotel.model.RoomType;
import ru.planet.hotel.model.RoomView;
import ru.planet.hotel.repository.HotelRepository;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static ru.planet.hotel.model.RoomView.TypeEnum.CITY_VIEW;
import static ru.planet.hotel.model.RoomView.TypeEnum.SEA_VIEW;

@KoraAppTest(Application.class)
public class HotelHelperTest {
    @Mock
    @TestComponent
    private HotelRepository hotelRepository;

    @TestComponent
    private HotelHelper hotelService;

    @Test
    public void testCalculateMin() {
        RoomView roomView1 = new RoomView(SEA_VIEW, 100.0);
        RoomView roomView2 = new RoomView(CITY_VIEW, 200.0);
        RoomType roomType1 = new RoomType(RoomType.TypeEnum.STANDARD, 150.0);
        RoomPeople roomPeople1 = new RoomPeople(RoomPeople.TypeEnum.SINGLE,80.0);

        List<RoomView> roomViews = Arrays.asList(roomView1, roomView2);
        List<RoomType> roomTypes = Arrays.asList(roomType1);
        List<RoomPeople> roomPeople = Arrays.asList(roomPeople1);

        double minPrice = hotelService.calculateMin(roomViews, roomTypes, roomPeople);

        assertEquals(80.0, minPrice);
    }

    @Test
    public void testExtractRoomViews_existingRoomView() {
        RoomView roomView = new RoomView(SEA_VIEW, 100.0);

        when(hotelRepository.getRoomView(roomView.type().getValue(), roomView.price()))
                .thenReturn(Optional.of(1L));

        List<Long> result = hotelService.extractRoomViews(Arrays.asList(roomView));

        assertEquals(Arrays.asList(1L), result);
        verify(hotelRepository).getRoomView(roomView.type().getValue(), roomView.price());
        verify(hotelRepository, never()).saveRoomView(anyString(), anyDouble());
    }


    @Test
    public void testExtractRoomViews_newRoomView() {
        RoomView roomView = new RoomView(SEA_VIEW, 100.0);

        when(hotelRepository.getRoomView(roomView.type().getValue(), roomView.price()))
                .thenReturn(Optional.empty());
        when(hotelRepository.saveRoomView(roomView.type().getValue(), roomView.price()))
                .thenReturn(2L);

        List<Long> result = hotelService.extractRoomViews(Arrays.asList(roomView));

        assertEquals(Arrays.asList(2L), result);
        verify(hotelRepository).getRoomView(roomView.type().getValue(), roomView.price());
        verify(hotelRepository).saveRoomView(roomView.type().getValue(), roomView.price());
    }

    @Test
    public void testExtractRoomPeople_existingRoomPeople() {
        RoomPeople roomPeople = new RoomPeople(RoomPeople.TypeEnum.SINGLE, 80.0);

        when(hotelRepository.getRoomPeople(roomPeople.type().getValue(), roomPeople.price()))
                .thenReturn(Optional.of(1L));

        List<Long> result = hotelService.extractRoomPeople(Arrays.asList(roomPeople));

        assertEquals(Arrays.asList(1L), result);
        verify(hotelRepository).getRoomPeople(roomPeople.type().getValue(), roomPeople.price());
        verify(hotelRepository, never()).saveRoomPeople(anyString(), anyDouble());
    }


    @Test
    public void testExtractRoomPeople_newRoomPeople() {
        RoomPeople roomPeople = new RoomPeople(RoomPeople.TypeEnum.SINGLE, 80.0);

        when(hotelRepository.getRoomPeople(roomPeople.type().getValue(), roomPeople.price()))
                .thenReturn(Optional.empty());
        when(hotelRepository.saveRoomPeople(roomPeople.type().getValue(), roomPeople.price()))
                .thenReturn(2L);
        List<Long> result = hotelService.extractRoomPeople(Arrays.asList(roomPeople));

        assertEquals(Arrays.asList(2L), result);
        verify(hotelRepository).getRoomPeople(roomPeople.type().getValue(), roomPeople.price());
        verify(hotelRepository).saveRoomPeople(roomPeople.type().getValue(), roomPeople.price());
    }


    @Test
    public void testExtractRoomTypes_existingRoomType() {
        RoomType roomType = new RoomType(RoomType.TypeEnum.STANDARD, 150.0);

        when(hotelRepository.getRoomType(roomType.type().getValue(), roomType.price()))
                .thenReturn(Optional.of(1L));

        List<Long> result = hotelService.extractRoomTypes(Arrays.asList(roomType));

        assertEquals(Arrays.asList(1L), result);
        verify(hotelRepository).getRoomType(roomType.type().getValue(), roomType.price());
        verify(hotelRepository, never()).saveRoomType(anyString(), anyDouble());
    }

    @Test
    public void testExtractRoomTypes_newRoomType() {
        RoomType roomType = new RoomType(RoomType.TypeEnum.STANDARD, 150.0);

        when(hotelRepository.getRoomType(roomType.type().getValue(), roomType.price()))
                .thenReturn(Optional.empty());
        when(hotelRepository.saveRoomType(roomType.type().getValue(), roomType.price()))
                .thenReturn(2L);

        List<Long> result = hotelService.extractRoomTypes(Arrays.asList(roomType));

        assertEquals(Arrays.asList(2L), result);
        verify(hotelRepository).getRoomType(roomType.type().getValue(), roomType.price());
        verify(hotelRepository).saveRoomType(roomType.type().getValue(), roomType.price());
    }

}
