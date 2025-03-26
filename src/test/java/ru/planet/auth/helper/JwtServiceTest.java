package ru.planet.auth.helper;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.auth.configuration.properties.JwtProperties;
import ru.planet.auth.dto.User;
import ru.planet.auth.repository.AuthRepository;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@KoraAppTest(Application.class)
public class JwtServiceTest {

    @Mock
    @TestComponent
    private JwtProperties jwtProperties;

    @Mock
    @TestComponent
    private AuthRepository authRepository;

    @TestComponent
    private JwtService jwtService;

    private User user;
    private String token;

    @BeforeEach
    void setUp() {
        user = new User(1L, "testUser", "password");

        when(jwtProperties.jwtExpiration()).thenReturn(3600000L);
        when(jwtProperties.signingKey()).thenReturn("iuLTU5aq83ssfe4gfege5ge14hryjtyumjt7mt7mh6iu");

        token = jwtService.generateJwt(user, List.of("ROLE_USER"));
    }

    @Test
    void testGenerateJwt() {
        assertNotNull(token);
        Claims claims = jwtService.getClaims(token);
        assertEquals(user.login(), claims.get(ClaimField.USERNAME));
        assertEquals(user.id(), Long.valueOf(String.valueOf(claims.get(ClaimField.USER_ID))));
        assertTrue(claims.getExpiration().after(new Date()));
    }

    @Test
    void testIsValidJwt_ValidToken() {
        when(authRepository.findByLogin(user.login())).thenReturn(user);
        assertTrue(jwtService.isValidJwt(token));
    }

    @Test
    void testIsValidJwt_InvalidToken() {
        assertFalse(jwtService.isValidJwt("invalidToken"));
    }

    @Test
    void testIsValidJwtWithId_ValidTokenAndId() {
        when(authRepository.findByLogin(user.login())).thenReturn(user);
        assertTrue(jwtService.isValidJwtWithId(token, user.id()));
    }

    @Test
    void testIsValidJwtWithId_InvalidToken() {
        assertFalse(jwtService.isValidJwtWithId("invalidToken", user.id()));
    }

    @Test
    void testIsValidAdmin_ValidAdminToken() {
        when(authRepository.findByLogin(user.login())).thenReturn(user);
        token = jwtService.generateJwt(user, List.of("ROLE_ADMIN"));
        assertTrue(jwtService.isValidAdmin(token));
    }

    @Test
    void testIsValidAdmin_NonAdminToken() {
        when(authRepository.findByLogin(user.login())).thenReturn(user);
        assertFalse(jwtService.isValidAdmin(token));
    }

    @Test
    void testGetClaims_ValidToken() {
        Claims claims = jwtService.getClaims(token);
        assertNotNull(claims);
        assertEquals(user.login(), claims.get(ClaimField.USERNAME));
    }

    @Test
    void testGetClaims_InvalidToken() {
        Claims claims = jwtService.getClaims("invalidToken");
        assertNull(claims);
    }
}
