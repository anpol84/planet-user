package ru.planet.auth.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.auth.helper.JwtService;
import ru.tinkoff.kora.common.Component;

@Component
@RequiredArgsConstructor
public class CheckAdminOperation {
    private final JwtService jwtService;

    public boolean activate(String jwt) {
        return jwtService.isValidAdmin(jwt);
    }
}
