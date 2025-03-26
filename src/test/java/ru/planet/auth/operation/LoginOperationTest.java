package ru.planet.auth.operation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.auth.dto.User;
import ru.planet.auth.helper.JwtService;
import ru.planet.common.exception.BusinessException;
import ru.planet.common.exception.ValidationException;
import ru.planet.user.configuration.properties.BcryptProperties;
import ru.tinkoff.kora.cache.LoadableCache;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@KoraAppTest(Application.class)
public class LoginOperationTest {

    @Mock
    @TestComponent
    private JwtService jwtService;

    @Mock
    @TestComponent
    private BcryptProperties bcryptProperties;

    @Mock
    @TestComponent
    private LoadableCache<String, User> userCache;

    @Mock
    @TestComponent
    private LoadableCache<Long, List<String>> roleCache;

    @TestComponent
    private LoginOperation loginOperation;

    private String login = "testUser";
    private String password = "password123";
    private String hashedPassword;
    private User user;
    private List<String> roles;

    @BeforeEach
    void setUp() {
        hashedPassword = BCrypt.hashpw(password, "$2a$10$abcaefghjjnkcnopqrstuv");
        user = new User(1L, login, hashedPassword);
        roles = List.of("ROLE_USER");

        when(bcryptProperties.salt()).thenReturn("$2a$10$abcaefghjjnkcnopqrstuv");
        when(userCache.get(login)).thenReturn(user);
        when(roleCache.get(user.id())).thenReturn(roles);
    }

    @Test
    void testActivate_Success() {
        String jwt = "generatedJwtToken";
        when(jwtService.generateJwt(user, roles)).thenReturn(jwt);

        String result = loginOperation.activate(login, password);

        assertEquals(jwt, result);
        verify(userCache).get(login);
        verify(roleCache).get(user.id());
        verify(jwtService).generateJwt(user, roles);
    }

    @Test
    void testActivate_EmptyLogin() {
        Exception exception = assertThrows(ValidationException.class, () -> {
            loginOperation.activate("", password);
        });
        assertEquals("login or password is not presented or empty", exception.getMessage());
    }

    @Test
    void testActivate_EmptyPassword() {
        Exception exception = assertThrows(ValidationException.class, () -> {
            loginOperation.activate(login, "");
        });
        assertEquals("login or password is not presented or empty", exception.getMessage());
    }

    @Test
    void testActivate_UserNotFound() {
        when(userCache.get(login)).thenReturn(null);

        Exception exception = assertThrows(BusinessException.class, () -> {
            loginOperation.activate(login, password);
        });
        assertEquals("login or password incorrect", exception.getMessage());
    }

    @Test
    void testActivate_IncorrectPassword() {
        User incorrectUser = new User(1L, login, BCrypt.hashpw("wrongPassword", bcryptProperties.salt()));
        when(userCache.get(login)).thenReturn(incorrectUser);

        Exception exception = assertThrows(BusinessException.class, () -> {
            loginOperation.activate(login, password);
        });
        assertEquals("login or password incorrect", exception.getMessage());
    }
}
