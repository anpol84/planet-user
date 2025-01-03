package ru.planet.user.helper.mapper;

import org.mapstruct.*;
import org.mindrot.jbcrypt.BCrypt;
import ru.planet.user.configuration.properties.BcryptProperties;
import ru.planet.user.dto.CreateUser;
import ru.planet.user.dto.GetUser;
import ru.planet.user.model.ChangeUserRequest;
import ru.planet.user.model.CreateUserRequest;
import ru.planet.user.model.GetUserResponse;

@Mapper
public interface UserMapper {

    @Mapping(target = "password", source = "password")
    CreateUser toUserFromCreate(CreateUserRequest createUserRequest, String password);
    CreateUser toUserFromUpdate(ChangeUserRequest changeUserRequest);

    GetUserResponse toGetUserResponse(GetUser user);

    default CreateUser toUserFromCreate(CreateUserRequest createUserRequest, BcryptProperties bcryptProperties) {
        var hashPassword = BCrypt.hashpw(createUserRequest.password(), bcryptProperties.salt());
        return toUserFromCreate(createUserRequest, hashPassword);
    }
}
