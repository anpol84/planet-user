package ru.planet.user.helper.mapper;

import javax.annotation.processing.Generated;
import ru.planet.user.dto.CreateUser;
import ru.planet.user.dto.GetUser;
import ru.planet.user.model.ChangeUserRequest;
import ru.planet.user.model.CreateUserRequest;
import ru.planet.user.model.GetUserResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-03T17:35:33+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 21.0.2 (Eclipse Adoptium)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public CreateUser toUserFromCreate(CreateUserRequest createUserRequest, String password) {
        if ( createUserRequest == null && password == null ) {
            return null;
        }

        String login = null;
        String city = null;
        if ( createUserRequest != null ) {
            login = createUserRequest.login();
            city = createUserRequest.city();
        }
        String password1 = null;
        password1 = password;

        CreateUser createUser = new CreateUser( login, password1, city );

        return createUser;
    }

    @Override
    public CreateUser toUserFromUpdate(ChangeUserRequest changeUserRequest) {
        if ( changeUserRequest == null ) {
            return null;
        }

        String login = null;
        String city = null;

        login = changeUserRequest.login();
        city = changeUserRequest.city();

        String password = null;

        CreateUser createUser = new CreateUser( login, password, city );

        return createUser;
    }

    @Override
    public GetUserResponse toGetUserResponse(GetUser user) {
        if ( user == null ) {
            return null;
        }

        Long id = null;
        String login = null;
        String city = null;

        id = user.id();
        login = user.login();
        city = user.city();

        GetUserResponse getUserResponse = new GetUserResponse( id, login, city );

        return getUserResponse;
    }
}
