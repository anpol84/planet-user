package ru.planet.feedback.operation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.common.exception.BusinessException;
import ru.planet.feedback.dto.GetFeedbackDto;
import ru.planet.feedback.helper.FeedbackMapper;
import ru.planet.feedback.repository.FeedbackRepository;
import ru.planet.hotel.model.Feedback;
import ru.planet.hotel.model.GetHotelFeedbacksResponse;
import ru.planet.hotel.repository.HotelRepository;
import ru.planet.user.repository.UserRepository;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@KoraAppTest(Application.class)
public class GetHotelFeedbacksOperationTest {

    @Mock
    @TestComponent
    private FeedbackRepository feedbackRepository;

    @Mock
    @TestComponent
    private HotelRepository hotelRepository;

    @Mock
    @TestComponent
    private UserRepository userRepository;

    @Mock
    @TestComponent
    private FeedbackMapper feedbackMapper;

    @TestComponent
    private GetHotelFeedbacksOperation getHotelFeedbacksOperation;

    private Long hotelId;
    private List<GetFeedbackDto> feedbackDtos;
    private String userLogin;
    private Feedback feedback;

    @BeforeEach
    public void setUp() {
        hotelId = 1L;
        userLogin = "user123";

        feedbackDtos = new ArrayList<>();
        feedbackDtos.add(new GetFeedbackDto(1L, 2L, 5, "Отличный отель!", LocalDateTime.now()));

        feedback = new Feedback(1L, 5, "Отличный отель!", LocalDate.now(), userLogin);
    }

    @Test
    public void testActivate_Success() {
        when(hotelRepository.validateHotel(hotelId)).thenReturn(true);
        when(feedbackRepository.getHotelFeedbacks(hotelId)).thenReturn(feedbackDtos);
        when(userRepository.getLoginById(2L)).thenReturn(userLogin);
        when(feedbackMapper.toFeedback(any(GetFeedbackDto.class), anyString())).thenReturn(feedback);

        GetHotelFeedbacksResponse result = getHotelFeedbacksOperation.activate(hotelId);

        assertNotNull(result);
        assertEquals(1, result.hotels().size());
        assertEquals(feedback, result.hotels().get(0));
        verify(hotelRepository).validateHotel(hotelId);
        verify(feedbackRepository).getHotelFeedbacks(hotelId);
        verify(userRepository).getLoginById(2L);
        verify(feedbackMapper).toFeedback(any(GetFeedbackDto.class), eq(userLogin));
    }

    @Test
    public void testActivate_HotelNotFound() {
        when(hotelRepository.validateHotel(hotelId)).thenReturn(false);

        BusinessException exception = assertThrows(BusinessException.class, () ->
                getHotelFeedbacksOperation.activate(hotelId)
        );
        assertEquals("Такого отеля не существует", exception.getMessage());
        verify(hotelRepository).validateHotel(hotelId);
        verify(feedbackRepository, never()).getHotelFeedbacks(anyLong());
    }
}
