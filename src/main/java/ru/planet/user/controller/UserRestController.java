package ru.planet.user.controller;

import lombok.RequiredArgsConstructor;
import ru.planet.user.api.UserApiDelegate;
import ru.planet.user.api.UserApiResponses;
import ru.planet.user.api.UserApiResponses.ChangeUserApiResponse;
import ru.planet.user.api.UserApiResponses.CreateUserApiResponse;
import ru.planet.user.api.UserApiResponses.DeleteUserApiResponse;
import ru.planet.user.api.UserApiResponses.GetUserApiResponse;
import ru.planet.user.model.ChangeUserRequest;
import ru.planet.user.model.CreateUserRequest;
import ru.planet.user.model.CreateUserResponse;
import ru.planet.user.operation.*;
import ru.tinkoff.kora.common.Component;

@Component
@RequiredArgsConstructor
public class UserRestController implements UserApiDelegate {
    private final CreateUserOperation createUserOperation;
    private final GetUserOperation getUserOperation;
    private final ChangeUserOperation changeUserOperation;
    private final DeleteUserOperation deleteUserOperation;
    private final GetUsersOperation getUsersOperation;

    @Override
    public ChangeUserApiResponse changeUser(long userId, String token, ChangeUserRequest changeUserRequest) throws Exception {
        changeUserOperation.activate(changeUserRequest, userId);
        return new ChangeUserApiResponse.ChangeUser200ApiResponse();
    }

    @Override
    public CreateUserApiResponse createUser(CreateUserRequest createUserRequest) throws Exception {
        return new CreateUserApiResponse.CreateUser200ApiResponse(
                new CreateUserResponse(createUserOperation.activate(createUserRequest)));
    }

    @Override
    public DeleteUserApiResponse deleteUser(long userId, String token) throws Exception {
        deleteUserOperation.activate(userId);
        return new DeleteUserApiResponse.DeleteUser200ApiResponse();
    }

    @Override
    public GetUserApiResponse getUser(long userId, String token) throws Exception {
        return new GetUserApiResponse.GetUser200ApiResponse(getUserOperation.activate(userId));
    }

    @Override
    public UserApiResponses.GetUsersApiResponse getUsers(String token) throws Exception {
        return new UserApiResponses.GetUsersApiResponse.GetUsers200ApiResponse(getUsersOperation.activate());
    }
}

