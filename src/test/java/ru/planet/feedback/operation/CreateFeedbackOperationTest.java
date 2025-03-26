package ru.planet.feedback.operation;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.auth.helper.JwtService;
import ru.planet.common.exception.BusinessException;
import ru.planet.feedback.dto.CreateFeedbackDto;
import ru.planet.feedback.helper.FeedbackMapper;
import ru.planet.feedback.repository.FeedbackRepository;
import ru.planet.hotel.model.CreateFeedbackRequest;
import ru.tinkoff.kora.database.jdbc.RuntimeSqlException;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.postgresql.util.PSQLState.FOREIGN_KEY_VIOLATION;
import static org.postgresql.util.PSQLState.UNIQUE_VIOLATION;

@KoraAppTest(Application.class)
public class CreateFeedbackOperationTest {
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
    private CreateFeedbackOperation createFeedbackOperation;

    private CreateFeedbackRequest validRequest;
    private String validToken;

    @BeforeEach
    public void setUp() {
        validRequest = new CreateFeedbackRequest(1L, 1L, "Great hotel!", 5);
        validToken = "token";
    }

    @Test
    public void testActivate_Success() {
        Claims claims = mock(Claims.class);
        when(jwtService.getClaims("token")).thenReturn(claims);
        when(claims.get("user_id")).thenReturn(1L);
        when(feedbackMapper.toCreateFeedbackDto(validRequest)).thenReturn(new CreateFeedbackDto(1L, 1L, "Great hotel!", 5));

        createFeedbackOperation.activate(validRequest, validToken);

        verify(feedbackRepository).createFeedback(any(CreateFeedbackDto.class));
        verify(feedbackRepository).updateRate(1L);
    }

    @Test
    public void testActivate_UserIdMismatch() {
        Claims claims = mock(Claims.class);
        when(jwtService.getClaims("token")).thenReturn(claims);
        when(claims.get("user_id")).thenReturn(2L);

        BusinessException exception = assertThrows(BusinessException.class, () ->
                createFeedbackOperation.activate(validRequest, validToken)
        );
        assertEquals("Вы не можете писать не свой отзыв", exception.getMessage());
    }

    @Test
    public void testActivate_DuplicateFeedbackException() {
        Claims claims = mock(Claims.class);
        when(jwtService.getClaims("token")).thenReturn(claims);
        when(claims.get("user_id")).thenReturn(1L);
        when(feedbackMapper.toCreateFeedbackDto(validRequest)).thenReturn(new CreateFeedbackDto(1L, 1L, "Great hotel!", 5));
        doThrow(new RuntimeSqlException(new SQLException("SQL error", UNIQUE_VIOLATION.getState(), new Throwable("Duplicate entry"))))
                .when(feedbackRepository).createFeedback(any(CreateFeedbackDto.class));

        BusinessException exception = assertThrows(BusinessException.class, () ->
                createFeedbackOperation.activate(validRequest, validToken)
        );
        assertEquals("Отзыв на данный отель данный пользователем уже написан", exception.getMessage());
    }

    @Test
    public void testActivate_NotFoundKeyException() {
        Claims claims = mock(Claims.class);
        when(jwtService.getClaims("token")).thenReturn(claims);
        when(claims.get("user_id")).thenReturn(1L);
        when(feedbackMapper.toCreateFeedbackDto(validRequest)).thenReturn(new CreateFeedbackDto(1L, 1L, "Great hotel!", 5));
        doThrow(new RuntimeSqlException(new SQLException("SQL error", FOREIGN_KEY_VIOLATION.getState(), new Throwable("Duplicate entry"))))
                .when(feedbackRepository).createFeedback(any(CreateFeedbackDto.class));

        BusinessException exception = assertThrows(BusinessException.class, () ->
                createFeedbackOperation.activate(validRequest, validToken)
        );
        assertEquals("Отеля или пользователя не существует", exception.getMessage());
    }
}