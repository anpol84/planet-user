package ru.planet.user.operation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.auth.dto.User;
import ru.planet.common.exception.BusinessException;
import ru.planet.hotel.model.CreateUserRequest;
import ru.planet.user.configuration.properties.BcryptProperties;
import ru.planet.user.dto.CreateUser;
import ru.planet.user.helper.mapper.UserMapper;
import ru.planet.user.repository.UserRepository;
import ru.planet.user.service.AuthService;
import ru.tinkoff.kora.database.jdbc.RuntimeSqlException;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.postgresql.util.PSQLState.UNIQUE_VIOLATION;

@KoraAppTest(Application.class)
public class CreateUserOperationTest {

    @Mock
    @TestComponent
    private UserMapper userMapper;

    @Mock
    @TestComponent
    private UserRepository userRepository;

    @Mock
    @TestComponent
    private AuthService authService;

    @Mock
    @TestComponent
    private BcryptProperties bcryptProperties;

    @TestComponent
    private CreateUserOperation createUserOperation;

    private CreateUserRequest request;

    @BeforeEach
    void setUp() {
        request = new CreateUserRequest("new_login", "password123");
    }

    @Test
    void testActivate_Success() {
        long userId = 1L;
        CreateUser user = new CreateUser("new_login", "hashed_password");

        when(userMapper.toUserFromCreate(request, bcryptProperties)).thenReturn(user);
        when(userRepository.insertUser(user)).thenReturn(userId);
        when(authService.login(request.login(), request.password())).thenReturn("token");

        String token = createUserOperation.activate(request);

        verify(userMapper).toUserFromCreate(request, bcryptProperties);
        verify(userRepository).insertUser(user);
        verify(userRepository).insertUserRole(userId);
        verify(authService).login(request.login(), request.password());

        assertEquals("token", token);
    }

    @Test
    void testActivate_DuplicateLogin() {
        CreateUser user = new CreateUser("new_login", "hashed_password");

        when(userMapper.toUserFromCreate(request, bcryptProperties)).thenReturn(user);
        when(userRepository.insertUser(user)).thenThrow(new RuntimeSqlException(new SQLException("SQL error",UNIQUE_VIOLATION.getState(), new Throwable("Duplicate entry"))));

        Exception exception = assertThrows(BusinessException.class, () -> {
            createUserOperation.activate(request);
        });

        assertEquals("Пользователь с таким логином уже существует", exception.getMessage());
    }

    @Test
    void testActivate_OtherSqlException() {
        CreateUser user = new CreateUser("new_login", "hashed_password");

        when(userMapper.toUserFromCreate(request, bcryptProperties)).thenReturn(user);
        when(userRepository.insertUser(user)).thenThrow(new RuntimeSqlException(new SQLException("SQL error","other error", new Throwable())));

        Exception exception = assertThrows(RuntimeSqlException.class, () -> {
            createUserOperation.activate(request);
        });

        assertEquals("SQL error", exception.getMessage());
    }
}
