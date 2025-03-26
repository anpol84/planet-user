package ru.planet.user.operation;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import ru.planet.auth.cache.UserCache;
import ru.planet.common.exception.BusinessException;
import ru.planet.feedback.repository.FeedbackRepository;
import ru.planet.user.repository.UserRepository;
import ru.tinkoff.kora.database.common.UpdateCount;
import ru.tinkoff.kora.database.jdbc.JdbcConnectionFactory;
import ru.tinkoff.kora.database.jdbc.JdbcHelper;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
public class DeleteUserOperationTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private JdbcConnectionFactory jdbcConnectionFactory;

    @Mock
    private UserCache userCache;

    @Mock
    private FeedbackRepository feedbackRepository;

    private DeleteUserOperation deleteUserOperation;

    private long userId;
    private String userLogin;

    @BeforeEach
    void setUp() {
        userId = 1L;
        userLogin = "user_login";

        deleteUserOperation = new DeleteUserOperation(userRepository, userCache, feedbackRepository);

    }

    @Test
    void testActivate_Success() throws SQLException {

        when(userRepository.getLoginById(userId)).thenReturn(userLogin);
        when(userRepository.deleteUser(userId)).thenReturn(new UpdateCount(1));
        when(userRepository.getJdbcConnectionFactory()).thenReturn(jdbcConnectionFactory);

        ArgumentCaptor<JdbcHelper.SqlRunnable> functionCaptor = ArgumentCaptor.forClass(JdbcHelper.SqlRunnable.class);
        deleteUserOperation.activate(userId);

        verify(jdbcConnectionFactory).inTx(functionCaptor.capture());
        functionCaptor.getValue().run();

        verify(userRepository).deleteUserRoles(userId);
        verify(userRepository).getLoginById(userId);
        verify(feedbackRepository).getHotelsByUserId(userId);
        verify(feedbackRepository).deleteFeedbacksByUserId(userId);
        verify(userRepository).deleteUser(userId);
        verify(userCache).invalidate(userLogin);
    }

    @Test
    void testActivate_UserDoesNotExist() {
        when(userRepository.getLoginById(userId)).thenReturn(userLogin);
        when(userRepository.deleteUser(userId)).thenReturn(new UpdateCount(0));
        when(userRepository.getJdbcConnectionFactory()).thenReturn(jdbcConnectionFactory);

        ArgumentCaptor<JdbcHelper.SqlRunnable> functionCaptor = ArgumentCaptor.forClass(JdbcHelper.SqlRunnable.class);

        deleteUserOperation.activate(userId);

        verify(jdbcConnectionFactory).inTx(functionCaptor.capture());
        Exception exception = assertThrows(BusinessException.class, () -> {
            functionCaptor.getValue().run();
        });

        assertEquals("Такого пользователя не существует", exception.getMessage());
    }

}