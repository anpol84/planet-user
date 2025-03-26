package ru.planet.auth.operation;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.auth.helper.JwtService;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@KoraAppTest(Application.class)
public class CheckAdminOperationTest {
    @Mock
    @TestComponent
    private JwtService jwtService;

    @TestComponent
    private CheckAdminOperation checkAdminOperation;

    private String validAdminJwt = "validAdminJwt";
    private String invalidAdminJwt = "invalidAdminJwt";


    @Test
    void testActivate_ValidAdminJwt() {
        when(jwtService.isValidAdmin(validAdminJwt)).thenReturn(true);
        assertTrue(checkAdminOperation.activate(validAdminJwt));
    }

    @Test
    void testActivate_InvalidAdminJwt() {
        when(jwtService.isValidAdmin(invalidAdminJwt)).thenReturn(false);
        assertFalse(checkAdminOperation.activate(invalidAdminJwt));
    }
}
