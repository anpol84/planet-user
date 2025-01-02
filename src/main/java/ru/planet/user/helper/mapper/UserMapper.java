package ru.planet.user.helper.mapper;

import org.mapstruct.*;
import org.mindrot.jbcrypt.BCrypt;
import ru.planet.user.configuration.properties.BcryptProperties;
import ru.planet.user.dto.User;
import ru.planet.user.model.ChangeUserRequest;
import ru.planet.user.model.CreateUserRequest;
import ru.planet.user.model.GetUserResponse;

import java.util.List;

@Mapper
public interface UserMapper {

    @Mapping(target = "password", source = "password")
    User toUserFromCreate(CreateUserRequest createUserRequest, String password);
    User toUserFromUpdate(ChangeUserRequest changeUserRequest);

    GetUserResponse toGetUserResponse(User user, List<String> roles);

    default User toUserFromCreate(CreateUserRequest createUserRequest, BcryptProperties bcryptProperties) {
        var hashPassword = BCrypt.hashpw(createUserRequest.password(), bcryptProperties.salt());
        return toUserFromCreate(createUserRequest, hashPassword);
    }
}
