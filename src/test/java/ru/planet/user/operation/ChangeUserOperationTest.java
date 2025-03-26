package ru.planet.user.operation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.auth.dto.User;
import ru.planet.common.exception.BusinessException;
import ru.planet.hotel.model.ChangeUserRequest;
import ru.planet.user.dto.CreateUser;
import ru.planet.user.helper.mapper.UserMapper;
import ru.planet.user.repository.UserRepository;
import ru.tinkoff.kora.database.common.UpdateCount;
import ru.tinkoff.kora.database.jdbc.RuntimeSqlException;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.postgresql.util.PSQLState.UNIQUE_VIOLATION;

@KoraAppTest(Application.class)
class ChangeUserOperationTest {

    @Mock
    @TestComponent
    private UserRepository userRepository;

    @Mock
    @TestComponent
    private UserMapper userMapper;

    @TestComponent
    private ChangeUserOperation changeUserOperation;

    private ChangeUserRequest request;
    private long userId = 1L;

    @BeforeEach
    void setUp() {
        request = new ChangeUserRequest("new_login");
    }

    @Test
    void testActivate_Success() {
        CreateUser user = new CreateUser( "new_login", "password123");

        when(userMapper.toUserFromUpdate(request)).thenReturn(user);
        when(userRepository.updateUser(user, userId)).thenReturn(new UpdateCount(1));

        changeUserOperation.activate(request, userId);

        verify(userMapper).toUserFromUpdate(request);
        verify(userRepository).updateUser(user, userId);
    }

    @Test
    void testActivate_UserNotFound() {
        when(userMapper.toUserFromUpdate(request)).thenReturn(new CreateUser("new_login", "password123"));
        when(userRepository.updateUser(any(), eq(userId))).thenReturn(new UpdateCount(0));

        Exception exception = assertThrows(BusinessException.class, () -> {
            changeUserOperation.activate(request, userId);
        });
        assertEquals("Такого пользователя не существует", exception.getMessage());
    }

    @Test
    void testActivate_SqlException() {
        when(userMapper.toUserFromUpdate(request)).thenReturn(new CreateUser("new_login", "password123"));

        RuntimeSqlException sqlException = new RuntimeSqlException(new SQLException("123", UNIQUE_VIOLATION.getState(), new Exception()));
        when(userRepository.updateUser(any(), eq(userId))).thenThrow(sqlException);

        Exception exception = assertThrows(BusinessException.class, () -> {
            changeUserOperation.activate(request, userId);
        });
        assertEquals("Пользователь с таким логином уже существует", exception.getMessage());
    }

    @Test
    void testActivate_OtherSqlException() {
        when(userMapper.toUserFromUpdate(request)).thenReturn(new CreateUser("new_login", "password123"));

        RuntimeSqlException sqlException = new RuntimeSqlException(new SQLException("SQL error", "some other", new Exception()));
        when(userRepository.updateUser(any(), eq(userId))).thenThrow(sqlException);

        Exception exception = assertThrows(RuntimeSqlException.class, () -> {
            changeUserOperation.activate(request, userId);
        });
        assertEquals("SQL error", exception.getMessage());
    }
}
