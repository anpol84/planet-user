package ru.planet.auth.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.common.exception.ValidationException;
import ru.tinkoff.kora.common.Component;
import ru.planet.auth.helper.JwtService;

@Component
@RequiredArgsConstructor
public class CheckTokenOperation {
    private final JwtService jwtService;

    public boolean activate(String jwt) {
        return jwtService.isValidJwt(jwt);
    }
}
