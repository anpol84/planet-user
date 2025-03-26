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
public class CheckTokenWithIdOperationTest {

    @Mock
    @TestComponent
    private JwtService jwtService;

    @TestComponent
    private CheckTokenWithIdOperation checkTokenWithIdOperation;

    private String validToken = "validToken";
    private long validId = 1L;
    private long invalidId = 2L;

    @Test
    void testActivate_ValidTokenAndId() {
        when(jwtService.isValidJwtWithId(validToken, validId)).thenReturn(true);
        assertTrue(checkTokenWithIdOperation.activate(validToken, validId));
    }

    @Test
    void testActivate_InvalidTokenOrId() {
        when(jwtService.isValidJwtWithId(validToken, invalidId)).thenReturn(false);
        assertFalse(checkTokenWithIdOperation.activate(validToken, invalidId));
    }
}
