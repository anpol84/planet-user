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
public class CheckTokenOperationTest {
    @Mock
    @TestComponent
    private JwtService jwtService;

    @TestComponent
    private CheckTokenOperation checkTokenOperation;

    private String validJwt = "validJwt";
    private String invalidJwt = "invalidJwt";

    @Test
    void testActivate_ValidJwt() {
        when(jwtService.isValidJwt(validJwt)).thenReturn(true);
        assertTrue(checkTokenOperation.activate(validJwt));
    }

    @Test
    void testActivate_InvalidJwt() {
        when(jwtService.isValidJwt(invalidJwt)).thenReturn(false);
        assertFalse(checkTokenOperation.activate(invalidJwt));
    }
}
