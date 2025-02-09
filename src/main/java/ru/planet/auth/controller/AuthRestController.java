package ru.planet.auth.controller;

import lombok.RequiredArgsConstructor;
import ru.planet.auth.operation.CheckTokenOperation;
import ru.planet.hotel.api.AuthApiDelegate;
import ru.planet.hotel.api.AuthApiResponses;
import ru.planet.hotel.model.AuthRequest;
import ru.planet.hotel.model.AuthResponse;
import ru.planet.hotel.model.ValidateRequest;
import ru.planet.hotel.model.ValidateResponse;
import ru.tinkoff.kora.common.Component;
import ru.planet.auth.operation.LoginOperation;
import ru.tinkoff.kora.logging.common.annotation.Log;


@Component
@RequiredArgsConstructor
public class AuthRestController implements AuthApiDelegate {
    private final LoginOperation loginOperation;
    private final CheckTokenOperation checkTokenOperation;

    @Override
    @Log
    public AuthApiResponses.CheckTokenApiResponse checkToken(ValidateRequest validateRequest) throws Exception {
        return new AuthApiResponses.CheckTokenApiResponse.CheckToken200ApiResponse(
                new ValidateResponse(
                        checkTokenOperation.activate(validateRequest.token())));
    }

    @Override
    @Log
    public AuthApiResponses.LoginApiResponse login(AuthRequest authRequest) throws Exception {
        return new AuthApiResponses.LoginApiResponse.Login200ApiResponse(
                new AuthResponse(
                        loginOperation.activate(authRequest.login(), authRequest.password())));
    }

}

