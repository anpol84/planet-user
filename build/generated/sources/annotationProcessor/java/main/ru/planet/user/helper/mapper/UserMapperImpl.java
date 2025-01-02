package ru.planet.user.helper.mapper;

import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import ru.planet.user.dto.User;
import ru.planet.user.model.ChangeUserRequest;
import ru.planet.user.model.CreateUserRequest;
import ru.planet.user.model.GetUserResponse;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-01-02T20:59:36+0300",
    comments = "version: 1.5.5.Final, compiler: IncrementalProcessingEnvironment from gradle-language-java-8.8.jar, environment: Java 21.0.2 (Eclipse Adoptium)"
)
public class UserMapperImpl implements UserMapper {

    @Override
    public User toUserFromCreate(CreateUserRequest createUserRequest, String password) {
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

        Long id = null;

        User user = new User( id, login, password1, city );

        return user;
    }

    @Override
    public User toUserFromUpdate(ChangeUserRequest changeUserRequest) {
        if ( changeUserRequest == null ) {
            return null;
        }

        String login = null;
        String city = null;

        login = changeUserRequest.login();
        city = changeUserRequest.city();

        Long id = null;
        String password = null;

        User user = new User( id, login, password, city );

        return user;
    }

    @Override
    public GetUserResponse toGetUserResponse(User user, List<String> roles) {
        if ( user == null && roles == null ) {
            return null;
        }

        Long id = null;
        String login = null;
        String city = null;
        if ( user != null ) {
            id = user.id();
            login = user.login();
            city = user.city();
        }
        List<GetUserResponse.RolesEnum> roles1 = null;
        roles1 = stringListToRolesEnumList( roles );

        GetUserResponse getUserResponse = new GetUserResponse( id, login, city, roles1 );

        return getUserResponse;
    }

    protected List<GetUserResponse.RolesEnum> stringListToRolesEnumList(List<String> list) {
        if ( list == null ) {
            return null;
        }

        List<GetUserResponse.RolesEnum> list1 = new ArrayList<GetUserResponse.RolesEnum>( list.size() );
        for ( String string : list ) {
            list1.add( Enum.valueOf( GetUserResponse.RolesEnum.class, string ) );
        }

        return list1;
    }
}
