package ru.planet.user.operation;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import ru.planet.Application;
import ru.planet.hotel.model.GetUserResponse;
import ru.planet.hotel.model.GetUsersResponse;
import ru.planet.user.dto.GetUser;
import ru.planet.user.helper.mapper.UserMapper;
import ru.planet.user.repository.UserRepository;
import ru.tinkoff.kora.test.extension.junit5.KoraAppTest;
import ru.tinkoff.kora.test.extension.junit5.TestComponent;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.wildfly.common.Assert.assertNotNull;

@KoraAppTest(Application.class)
public class GetUsersOperationTest {

    @Mock
    @TestComponent
    private UserRepository userRepository;

    @Mock
    @TestComponent
    private UserMapper userMapper;

    @TestComponent
    private GetUsersOperation getUsersOperation;

    private GetUser user1;
    private GetUser user2;
    private GetUserResponse userResponse1;
    private GetUserResponse userResponse2;

    @BeforeEach
    void setUp() {

        user1 = new GetUser(1L, "user1_login");
        user2 = new GetUser(2L, "user2_login");
        userResponse1 = new GetUserResponse(1L, "user1_login");
        userResponse2 = new GetUserResponse(2L, "user2_login");
    }

    @Test
    void testActivate_ReturnsCorrectResponse() {
        when(userRepository.getUsers()).thenReturn(Arrays.asList(user1, user2));
        when(userMapper.toGetUserResponse(user1)).thenReturn(userResponse1);
        when(userMapper.toGetUserResponse(user2)).thenReturn(userResponse2);

        GetUsersResponse result = getUsersOperation.activate();

        assertNotNull(result);
        List<GetUserResponse> users = result.users();
        assertNotNull(users);
        assertEquals(2, users.size());
        assertEquals(userResponse1, users.get(0));
        assertEquals(userResponse2, users.get(1));

        verify(userRepository).getUsers();
        verify(userMapper).toGetUserResponse(user1);
        verify(userMapper).toGetUserResponse(user2);
    }

    @Test
    void testActivate_EmptyUserList() {
        when(userRepository.getUsers()).thenReturn(List.of());

        GetUsersResponse result = getUsersOperation.activate();

        assertNotNull(result);
        assertNotNull(result.users());
        assertTrue(result.users().isEmpty());

        verify(userRepository).getUsers();
    }
}
