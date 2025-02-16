package ru.planet.user.service;

import lombok.RequiredArgsConstructor;
import ru.planet.auth.operation.CheckAdminOperation;
import ru.planet.auth.operation.CheckTokenOperation;
import ru.planet.auth.operation.CheckTokenWithIdOperation;
import ru.planet.auth.operation.LoginOperation;
import ru.tinkoff.kora.common.Component;

@Component
@RequiredArgsConstructor
public class AuthService {

    private final CheckTokenWithIdOperation checkTokenWithIdOperation;
    private final LoginOperation loginOperation;
    private final CheckAdminOperation checkAdminOperation;
    private final CheckTokenOperation checkTokenOperation;

    public boolean checkTokenWithId(String jwt, long id) {
        return checkTokenWithIdOperation.activate(jwt, id);
    }

    public String login(String login, String password) {
        return loginOperation.activate(login, password);
    }

    public boolean checkAdminResponse(String jwt) {
        return checkAdminOperation.activate(jwt);
    }

    public boolean checkToken(String jwt) {
        return checkTokenOperation.activate(jwt);
    }
}
