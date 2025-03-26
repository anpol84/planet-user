package ru.planet.feedback.operation;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.auth.helper.JwtService;
import ru.planet.common.exception.BusinessException;
import ru.planet.feedback.dto.UpdateFeedbackDto;
import ru.planet.feedback.helper.FeedbackMapper;
import ru.planet.feedback.repository.FeedbackRepository;
import ru.planet.hotel.model.UpdateFeedbackRequest;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@KoraAppTest(Application.class)
public class UpdateFeedbackOperationTest {

    @Mock
    @TestComponent
    private FeedbackRepository feedbackRepository;

    @Mock
    @TestComponent
    private FeedbackMapper feedbackMapper;

    @Mock
    @TestComponent
    private JwtService jwtService;

    @TestComponent
    private UpdateFeedbackOperation updateFeedbackOperation;

    private UpdateFeedbackRequest updateRequest;
    private Long feedbackId;
    private String token;
    private Long userId;
    private Long userByFeedbackId;
    private Long hotelId;

    @BeforeEach
    public void setUp() {
        updateRequest = new UpdateFeedbackRequest(5, "Отличный сервис");
        feedbackId = 1L;
        token = "token";
        userId = 2L;
        userByFeedbackId = 2L;
        hotelId = 3L;
    }

    @Test
    public void testActivate_Success() {
        Claims claims = mock(Claims.class);
        when(jwtService.getClaims("token")).thenReturn(claims);
        when(claims.get("user_id")).thenReturn(2L);
        when(feedbackRepository.getUserByFeedback(feedbackId)).thenReturn(userByFeedbackId);
        when(feedbackMapper.toUpdateFeedbackDto(updateRequest)).thenReturn(new UpdateFeedbackDto( "Отличный сервис", 5));
        when(feedbackRepository.updateFeedback(any(UpdateFeedbackDto.class), eq(feedbackId))).thenReturn(hotelId);

        updateFeedbackOperation.activate(updateRequest, feedbackId, token);

        verify(feedbackRepository).updateFeedback(any(UpdateFeedbackDto.class), eq(feedbackId));
        verify(feedbackRepository).updateRate(hotelId);
    }

    @Test
    public void testActivate_UserNotOwner() {
        Claims claims = mock(Claims.class);
        when(jwtService.getClaims("token")).thenReturn(claims);
        when(claims.get("user_id")).thenReturn(1L);
        when(feedbackRepository.getUserByFeedback(feedbackId)).thenReturn(3L);

        BusinessException exception = assertThrows(BusinessException.class, () ->
                updateFeedbackOperation.activate(updateRequest, feedbackId, token)
        );
        assertEquals("Вы не можете менять не свой отзыв", exception.getMessage());
    }

    @Test
    public void testActivate_FeedbackNotFound() {
        Claims claims = mock(Claims.class);
        when(jwtService.getClaims("token")).thenReturn(claims);
        when(claims.get("user_id")).thenReturn(2L);
        when(feedbackRepository.getUserByFeedback(feedbackId)).thenReturn(userByFeedbackId);
        when(feedbackMapper.toUpdateFeedbackDto(updateRequest)).thenReturn(new UpdateFeedbackDto("Отличный сервис", 5));
        when(feedbackRepository.updateFeedback(any(UpdateFeedbackDto.class), eq(feedbackId))).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () ->
                updateFeedbackOperation.activate(updateRequest, feedbackId, token)
        );
        assertEquals("Такого отзыва не существует", exception.getMessage());
    }

    @Test
    public void testActivate_AdminRole() {
        Claims claims = mock(Claims.class);
        when(jwtService.getClaims("token")).thenReturn(claims);
        when(claims.get("role")).thenReturn("ROLE_ADMIN");
        when(claims.get("user_id")).thenReturn(2L);
        when(feedbackMapper.toUpdateFeedbackDto(updateRequest)).thenReturn(new UpdateFeedbackDto("Отличный сервис", 5));
        when(feedbackRepository.updateFeedback(any(UpdateFeedbackDto.class), eq(feedbackId))).thenReturn(hotelId);
        updateFeedbackOperation.activate(updateRequest, feedbackId, token);

        verify(feedbackRepository).updateFeedback(any(UpdateFeedbackDto.class), eq(feedbackId));
        verify(feedbackRepository).updateRate(hotelId);
    }
}