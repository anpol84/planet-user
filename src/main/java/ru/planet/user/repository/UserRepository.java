package ru.planet.user.repository;

import jakarta.annotation.Nullable;
import ru.planet.user.dto.CreateUser;
import ru.planet.user.dto.GetUser;
import ru.tinkoff.kora.database.common.UpdateCount;
import ru.tinkoff.kora.database.common.annotation.Query;
import ru.tinkoff.kora.database.common.annotation.Repository;
import ru.tinkoff.kora.database.jdbc.JdbcRepository;
import ru.tinkoff.kora.logging.common.annotation.Log;

import java.util.List;

@Repository
public interface UserRepository extends JdbcRepository {

    @Log
    @Query("SELECT r.name FROM user_roles ur JOIN roles r on ur.role_id = r.id WHERE ur.user_id = :id")
    List<String> findRoles(Long id);

    @Log
    @Query("INSERT INTO users (username, password) VALUES (:user.login, :user.password) RETURNING id")
    long insertUser(CreateUser user);

    @Log
    @Query("INSERT INTO user_roles (user_id, role_id) VALUES (:id, 2)")
    void insertUserRole(Long id);

    @Log
    @Query("SELECT id, username as login FROM users WHERE id = :id")
    @Nullable
    GetUser getUser(Long id);

    @Log
    @Query("UPDATE users SET username = :user.login WHERE id = :id")
    UpdateCount updateUser(CreateUser user, Long id);

    @Log
    @Query("DELETE FROM users WHERE id = :id")
    UpdateCount deleteUser(Long id);

    @Log
    @Query("DELETE FROM user_roles WHERE user_id = :id")
    void deleteUserRoles(Long id);

    @Log
    @Query("SELECT id, username as login FROM users")
    List<GetUser> getUsers();

    @Log
    @Query("SELECT username FROM users WHERE id = :id")
    @Nullable
    String getLoginById(Long id);

    @Query("""
            DELETE FROM user_favourite WHERE user_id = :userId
            """)
    void deleteUserFavourites(Long userId);

}
