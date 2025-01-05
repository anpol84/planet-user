package ru.planet.user.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.user.helper.mapper.UserMapper;
import ru.planet.user.model.GetUsersResponse;
import ru.planet.user.repository.UserRepository;
import ru.tinkoff.kora.common.Component;

@Component
@RequiredArgsConstructor
public class GetUsersOperation {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public GetUsersResponse activate() {
        return new GetUsersResponse(userRepository.getUsers().stream()
                .map(userMapper::toGetUserResponse)
                .toList());
    }
}
