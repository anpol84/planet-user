package ru.planet.feedback.operation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.common.exception.BusinessException;
import ru.planet.feedback.dto.GetFeedbackDto;
import ru.planet.feedback.helper.FeedbackMapper;
import ru.planet.feedback.repository.FeedbackRepository;
import ru.planet.hotel.model.GetFeedbackResponse;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@KoraAppTest(Application.class)
public class GetFeedbackOperationTest {

    @Mock
    @TestComponent
    private FeedbackRepository feedbackRepository;

    @Mock
    @TestComponent
    private FeedbackMapper feedbackMapper;

    @TestComponent
    private GetFeedbackOperation getFeedbackOperation;

    private Long feedbackId;
    private GetFeedbackDto feedbackDto;
    private GetFeedbackResponse feedbackResponse;

    @BeforeEach
    public void setUp() {
        feedbackId = 1L;
        feedbackDto = new GetFeedbackDto(feedbackId, 1L, 5, "Excellent service!", LocalDateTime.now());
        feedbackResponse = new GetFeedbackResponse(5, "Excellent service!");
    }

    @Test
    public void testActivate_Success() {
        when(feedbackRepository.getFeedbackById(feedbackId)).thenReturn(feedbackDto);
        when(feedbackMapper.toGetFeedbackResponse(feedbackDto)).thenReturn(feedbackResponse);

        GetFeedbackResponse result = getFeedbackOperation.activate(feedbackId);

        assertNotNull(result);
        assertEquals(feedbackResponse.mark(), result.mark());
        assertEquals(feedbackResponse.body(), result.body());
        verify(feedbackRepository).getFeedbackById(feedbackId);
        verify(feedbackMapper).toGetFeedbackResponse(feedbackDto);
    }

    @Test
    public void testActivate_FeedbackNotFound() {
        when(feedbackRepository.getFeedbackById(feedbackId)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () ->
                getFeedbackOperation.activate(feedbackId)
        );
        assertEquals("Такого отзыва не существует", exception.getMessage());
        verify(feedbackRepository).getFeedbackById(feedbackId);
        verify(feedbackMapper, never()).toGetFeedbackResponse(any());
    }
}
