package ru.planet.user.operation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.common.exception.BusinessException;
import ru.planet.hotel.model.GetUserResponse;
import ru.planet.user.dto.GetUser;
import ru.planet.user.helper.mapper.UserMapper;
import ru.planet.user.repository.UserRepository;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@KoraAppTest(Application.class)
public class GetUserOperationTest {

    @Mock
    @TestComponent
    private UserRepository userRepository;

    @Mock
    @TestComponent
    private UserMapper userMapper;

    @TestComponent
    private GetUserOperation getUserOperation;

    private long userId;
    private GetUser user;
    private GetUserResponse userResponse;

    @BeforeEach
    void setUp() {
        userId = 1L;
        user = new GetUser(userId, "user_login");
        userResponse = new GetUserResponse(userId, "user_login");
    }

    @Test
    void testActivate_UserFound() {
        when(userRepository.getUser(userId)).thenReturn(user);
        when(userMapper.toGetUserResponse(user)).thenReturn(userResponse);

        GetUserResponse result = getUserOperation.activate(userId);

        assertNotNull(result);
        assertEquals(userId, result.id());
        assertEquals("user_login", result.login());

        verify(userRepository).getUser(userId);
        verify(userMapper).toGetUserResponse(user);
    }

    @Test
    void testActivate_UserNotFound() {
        when(userRepository.getUser(userId)).thenReturn(null);

        BusinessException exception = assertThrows(BusinessException.class, () -> {
            getUserOperation.activate(userId);
        });

        assertEquals("Пользователя с таким id не существует", exception.getMessage());

        verify(userRepository).getUser(userId);
        verify(userMapper, never()).toGetUserResponse(any());
    }
}
