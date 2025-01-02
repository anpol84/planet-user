package ru.planet.user.operation;

import lombok.RequiredArgsConstructor;
import ru.planet.user.helper.mapper.UserMapper;
import ru.planet.user.model.GetUserResponse;
import ru.planet.user.repository.UserRepository;
import ru.tinkoff.kora.common.Component;

@Component
@RequiredArgsConstructor
public class GetUserOperation {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public GetUserResponse activate(long userId) {
        var user = userRepository.getUser(userId);
        var roles = userRepository.findRoles(userId);
        return userMapper.toGetUserResponse(user, roles);
    }
}
