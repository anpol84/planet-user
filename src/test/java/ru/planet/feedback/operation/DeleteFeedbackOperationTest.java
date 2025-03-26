package ru.planet.feedback.operation;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.auth.helper.JwtService;
import ru.planet.common.exception.BusinessException;
import ru.planet.feedback.repository.FeedbackRepository;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@KoraAppTest(Application.class)
public class DeleteFeedbackOperationTest {

    @Mock
    @TestComponent
    private FeedbackRepository feedbackRepository;

    @Mock
    @TestComponent
    private JwtService jwtService;

    @TestComponent
    private DeleteFeedbackOperation deleteFeedbackOperation;

    private String validToken;
    private long feedbackId;
    private long userId;

    @BeforeEach
    public void setUp() {
        validToken = "token";
        feedbackId = 1L;
        userId = 1L;
    }

    @Test
    public void testActivate_Success_User() {
        Claims claims = mock(Claims.class);
        when(jwtService.getClaims("token")).thenReturn(claims);
        when(claims.get("user_id")).thenReturn(1L);
        when(feedbackRepository.getUserByFeedback(feedbackId)).thenReturn(userId);
        when(feedbackRepository.deleteFeedback(feedbackId)).thenReturn(1L);

        deleteFeedbackOperation.activate(feedbackId, validToken);

        verify(feedbackRepository).deleteFeedback(feedbackId);
    }

    @Test
    public void testActivate_Success_Admin() {
        Claims claims = mock(Claims.class);
        when(jwtService.getClaims("token")).thenReturn(claims);
        when(claims.get("user_id")).thenReturn(1L);
        when(feedbackRepository.getUserByFeedback(1L)).thenReturn(1L);
        when(feedbackRepository.deleteFeedback(feedbackId)).thenReturn(1L);

        deleteFeedbackOperation.activate(feedbackId, validToken);

        verify(feedbackRepository).deleteFeedback(feedbackId);
    }

    @Test
    public void testActivate_UserIdMismatch() {
        Claims claims = mock(Claims.class);
        when(jwtService.getClaims("token")).thenReturn(claims);
        when(claims.get("user_id")).thenReturn(1L);
        when(feedbackRepository.getUserByFeedback(feedbackId)).thenReturn(2L);

        BusinessException exception = assertThrows(BusinessException.class, () ->
                deleteFeedbackOperation.activate(feedbackId, validToken)
        );
        assertEquals("Вы не можете удалять не свой отзыв", exception.getMessage());
    }

    @Test
    public void testActivate_FeedbackNotFound() {
        Claims claims = mock(Claims.class);
        when(jwtService.getClaims("token")).thenReturn(claims);
        when(claims.get("user_id")).thenReturn(1L);
        when(feedbackRepository.getUserByFeedback(feedbackId)).thenReturn(userId);
        when(feedbackRepository.deleteFeedback(feedbackId)).thenReturn(null);
        BusinessException exception = assertThrows(BusinessException.class, () ->
                deleteFeedbackOperation.activate(feedbackId, validToken)
        );
        assertEquals("Такого отзыва не существует", exception.getMessage());
    }
}
