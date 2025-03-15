package ru.planet.gpt.controller;

import lombok.RequiredArgsConstructor;
import ru.planet.gpt.operation.AskGptOperation;
import ru.planet.hotel.api.GptApiDelegate;
import ru.planet.hotel.api.GptApiResponses;
import ru.planet.hotel.model.AskGptRequest;
import ru.tinkoff.kora.common.Component;

@Component
@RequiredArgsConstructor
public class GptController implements GptApiDelegate {
    private final AskGptOperation askGptOperation;

    @Override
    public GptApiResponses.AskGptApiResponse askGpt(String token, AskGptRequest askGptRequest) throws Exception {
        return new GptApiResponses.AskGptApiResponse.AskGpt200ApiResponse(askGptOperation.activate(token, askGptRequest));
    }
}
