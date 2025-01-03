package ru.planet.user.repository;

import ru.planet.user.dto.CreateUser;
import ru.planet.user.dto.GetUser;
import ru.tinkoff.kora.database.common.UpdateCount;
import ru.tinkoff.kora.database.common.annotation.Query;
import ru.tinkoff.kora.database.common.annotation.Repository;
import ru.tinkoff.kora.database.jdbc.JdbcRepository;
import ru.tinkoff.kora.logging.common.annotation.Log;

import javax.annotation.Nullable;
import java.util.List;

@Repository
public interface UserRepository extends JdbcRepository {

    @Log
    @Query("SELECT r.name FROM user_roles ur JOIN roles r on ur.role_id = r.id WHERE ur.user_id = :id")
    List<String> findRoles(Long id);

    @Log
    @Query("INSERT INTO users (username, password, city) VALUES (:user.login, :user.password, :user.city) RETURNING id")
    long insertUser(CreateUser user);

    @Log
    @Query("INSERT INTO user_roles (user_id, role_id) VALUES (:id, 2)")
    void insertUserRole(Long id);

    @Log
    @Query("SELECT id, username as login, city FROM users WHERE id = :id")
    @Nullable
    GetUser getUser(Long id);

    @Log
    @Query("UPDATE users SET username = :user.login, city = :user.city WHERE id = :id")
    UpdateCount updateUser(CreateUser user, Long id);

    @Log
    @Query("DELETE FROM users WHERE id = :id")
    UpdateCount deleteUser(Long id);

    @Log
    @Query("DELETE FROM user_roles WHERE user_id = :id")
    void deleteUserRoles(Long id);

}
