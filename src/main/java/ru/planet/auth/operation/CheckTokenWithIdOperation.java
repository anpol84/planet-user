package ru.planet.auth.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.auth.helper.JwtService;
import ru.tinkoff.kora.common.Component;

@Component
@RequiredArgsConstructor
public class CheckTokenWithIdOperation {
    private final JwtService jwtService;

    public boolean activate(String token, long id) {
        return jwtService.isValidJwtWithId(token, id);
    }
}
