package ru.planet.user.repository;

import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.sql.Connection;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import ru.planet.user.dto.User;
import ru.tinkoff.kora.common.annotation.Generated;
import ru.tinkoff.kora.database.common.QueryContext;
import ru.tinkoff.kora.database.jdbc.JdbcConnectionFactory;
import ru.tinkoff.kora.database.jdbc.JdbcRepository;
import ru.tinkoff.kora.database.jdbc.mapper.result.JdbcResultSetMapper;
import ru.tinkoff.kora.logging.common.annotation.Log;

@Generated("ru.tinkoff.kora.database.annotation.processor.RepositoryAnnotationProcessor")
public class $UserRepository_Impl implements UserRepository, JdbcRepository {
  private static final QueryContext QUERY_CONTEXT_1 = new QueryContext(
        "SELECT id, username as login, password FROM users WHERE username = :login",
        "SELECT id, username as login, password FROM users WHERE username = ?",
        "UserRepository.findByLogin"
      );

  private static final QueryContext QUERY_CONTEXT_2 = new QueryContext(
        "SELECT r.name FROM user_roles ur JOIN roles r on ur.role_id = r.id WHERE ur.user_id = :id",
        "SELECT r.name FROM user_roles ur JOIN roles r on ur.role_id = r.id WHERE ur.user_id = ?",
        "UserRepository.findRoles"
      );

  private static final QueryContext QUERY_CONTEXT_3 = new QueryContext(
        "INSERT INTO users (username, password, city) VALUES (:user.login, :user.password, :user.city) RETURNING id",
        "INSERT INTO users (username, password, city) VALUES (?, ?, ?) RETURNING id",
        "UserRepository.insertUser"
      );

  private static final QueryContext QUERY_CONTEXT_4 = new QueryContext(
        "INSERT INTO user_roles (user_id, role_id) VALUES (:id, 2)",
        "INSERT INTO user_roles (user_id, role_id) VALUES (?, 2)",
        "UserRepository.insertUserRole"
      );

  private static final QueryContext QUERY_CONTEXT_5 = new QueryContext(
        "SELECT id, username as login, password, city FROM users WHERE id = :id",
        "SELECT id, username as login, password, city FROM users WHERE id = ?",
        "UserRepository.getUser"
      );

  private static final QueryContext QUERY_CONTEXT_6 = new QueryContext(
        "UPDATE users SET username = :user.login, city = :user.password WHERE id = :id",
        "UPDATE users SET username = ?, city = ? WHERE id = ?",
        "UserRepository.updateUser"
      );

  private static final QueryContext QUERY_CONTEXT_7 = new QueryContext(
        "DELETE FROM users WHERE id = :id",
        "DELETE FROM users WHERE id = ?",
        "UserRepository.deleteUser"
      );

  private static final QueryContext QUERY_CONTEXT_8 = new QueryContext(
        "DELETE FROM user_roles WHERE user_id = :id",
        "DELETE FROM user_roles WHERE user_id = ?",
        "UserRepository.deleteUserRoles"
      );

  private final JdbcConnectionFactory _connectionFactory;

  private final JdbcResultSetMapper<User> _result_mapper_1;

  private final JdbcResultSetMapper<List<String>> _result_mapper_2;

  private final JdbcResultSetMapper<Long> _result_mapper_3;

  public $UserRepository_Impl(JdbcConnectionFactory _connectionFactory,
      JdbcResultSetMapper<User> _result_mapper_1,
      JdbcResultSetMapper<List<String>> _result_mapper_2,
      JdbcResultSetMapper<Long> _result_mapper_3) {
    this._connectionFactory = _connectionFactory;
    this._result_mapper_1 = _result_mapper_1;
    this._result_mapper_2 = _result_mapper_2;
    this._result_mapper_3 = _result_mapper_3;
  }

  @Override
  public final JdbcConnectionFactory getJdbcConnectionFactory() {
    return this._connectionFactory;
  }

  @Override
  @Log
  @Nullable
  public User findByLogin(String login) {
    var _ctxCurrent = ru.tinkoff.kora.common.Context.current();
    var _query = QUERY_CONTEXT_1;
    var _telemetry = this._connectionFactory.telemetry().createContext(_ctxCurrent, _query);
    var _conToUse = this._connectionFactory.currentConnection();
    Connection _conToClose;
    if (_conToUse == null) {
        _conToUse = this._connectionFactory.newConnection();
        _conToClose = _conToUse;
    } else {
        _conToClose = null;
    }
    try (_conToClose; var _stmt = _conToUse.prepareStatement(_query.sql())) {
      _stmt.setString(1, login);
      try (var _rs = _stmt.executeQuery()) {
        var _result = _result_mapper_1.apply(_rs);
        _telemetry.close(null);
        return _result;
      }

    } catch (java.sql.SQLException e) {
      _telemetry.close(e);
      throw new ru.tinkoff.kora.database.jdbc.RuntimeSqlException(e);
    } catch (Exception e) {
      _telemetry.close(e);
      throw e;
    }
  }

  @Override
  @Log
  public List<String> findRoles(Long id) {
    var _ctxCurrent = ru.tinkoff.kora.common.Context.current();
    var _query = QUERY_CONTEXT_2;
    var _telemetry = this._connectionFactory.telemetry().createContext(_ctxCurrent, _query);
    var _conToUse = this._connectionFactory.currentConnection();
    Connection _conToClose;
    if (_conToUse == null) {
        _conToUse = this._connectionFactory.newConnection();
        _conToClose = _conToUse;
    } else {
        _conToClose = null;
    }
    try (_conToClose; var _stmt = _conToUse.prepareStatement(_query.sql())) {
      _stmt.setLong(1, id);
      try (var _rs = _stmt.executeQuery()) {
        var _result = _result_mapper_2.apply(_rs);
        _telemetry.close(null);
        return Objects.requireNonNull(_result, "Result mapping is expected non-null, but was null");
      }

    } catch (java.sql.SQLException e) {
      _telemetry.close(e);
      throw new ru.tinkoff.kora.database.jdbc.RuntimeSqlException(e);
    } catch (Exception e) {
      _telemetry.close(e);
      throw e;
    }
  }

  @Override
  @Log
  public long insertUser(User user) {
    var _ctxCurrent = ru.tinkoff.kora.common.Context.current();
    var _query = QUERY_CONTEXT_3;
    var _telemetry = this._connectionFactory.telemetry().createContext(_ctxCurrent, _query);
    var _conToUse = this._connectionFactory.currentConnection();
    Connection _conToClose;
    if (_conToUse == null) {
        _conToUse = this._connectionFactory.newConnection();
        _conToClose = _conToUse;
    } else {
        _conToClose = null;
    }
    try (_conToClose; var _stmt = _conToUse.prepareStatement(_query.sql())) {
      _stmt.setString(1, user.login());
      if (user.password() != null) {
        _stmt.setString(2, user.password());
      } else {
        _stmt.setNull(2, Types.VARCHAR);
      }
      if (user.city() != null) {
        _stmt.setString(3, user.city());
      } else {
        _stmt.setNull(3, Types.VARCHAR);
      }
      try (var _rs = _stmt.executeQuery()) {
        var _result = _result_mapper_3.apply(_rs);
        _telemetry.close(null);
        return _result;
      }

    } catch (java.sql.SQLException e) {
      _telemetry.close(e);
      throw new ru.tinkoff.kora.database.jdbc.RuntimeSqlException(e);
    } catch (Exception e) {
      _telemetry.close(e);
      throw e;
    }
  }

  @Override
  @Log
  public void insertUserRole(Long id) {
    var _ctxCurrent = ru.tinkoff.kora.common.Context.current();
    var _query = QUERY_CONTEXT_4;
    var _telemetry = this._connectionFactory.telemetry().createContext(_ctxCurrent, _query);
    var _conToUse = this._connectionFactory.currentConnection();
    Connection _conToClose;
    if (_conToUse == null) {
        _conToUse = this._connectionFactory.newConnection();
        _conToClose = _conToUse;
    } else {
        _conToClose = null;
    }
    try (_conToClose; var _stmt = _conToUse.prepareStatement(_query.sql())) {
      _stmt.setLong(1, id);
      _stmt.execute();
      _telemetry.close(null);

    } catch (java.sql.SQLException e) {
      _telemetry.close(e);
      throw new ru.tinkoff.kora.database.jdbc.RuntimeSqlException(e);
    } catch (Exception e) {
      _telemetry.close(e);
      throw e;
    }
  }

  @Override
  @Log
  public User getUser(Long id) {
    var _ctxCurrent = ru.tinkoff.kora.common.Context.current();
    var _query = QUERY_CONTEXT_5;
    var _telemetry = this._connectionFactory.telemetry().createContext(_ctxCurrent, _query);
    var _conToUse = this._connectionFactory.currentConnection();
    Connection _conToClose;
    if (_conToUse == null) {
        _conToUse = this._connectionFactory.newConnection();
        _conToClose = _conToUse;
    } else {
        _conToClose = null;
    }
    try (_conToClose; var _stmt = _conToUse.prepareStatement(_query.sql())) {
      _stmt.setLong(1, id);
      try (var _rs = _stmt.executeQuery()) {
        var _result = _result_mapper_1.apply(_rs);
        _telemetry.close(null);
        return Objects.requireNonNull(_result, "Result mapping is expected non-null, but was null");
      }

    } catch (java.sql.SQLException e) {
      _telemetry.close(e);
      throw new ru.tinkoff.kora.database.jdbc.RuntimeSqlException(e);
    } catch (Exception e) {
      _telemetry.close(e);
      throw e;
    }
  }

  @Override
  @Log
  public void updateUser(User user, Long id) {
    var _ctxCurrent = ru.tinkoff.kora.common.Context.current();
    var _query = QUERY_CONTEXT_6;
    var _telemetry = this._connectionFactory.telemetry().createContext(_ctxCurrent, _query);
    var _conToUse = this._connectionFactory.currentConnection();
    Connection _conToClose;
    if (_conToUse == null) {
        _conToUse = this._connectionFactory.newConnection();
        _conToClose = _conToUse;
    } else {
        _conToClose = null;
    }
    try (_conToClose; var _stmt = _conToUse.prepareStatement(_query.sql())) {
      _stmt.setString(1, user.login());
      if (user.password() != null) {
        _stmt.setString(2, user.password());
      } else {
        _stmt.setNull(2, Types.VARCHAR);
      }
      _stmt.setLong(3, id);
      _stmt.execute();
      _telemetry.close(null);

    } catch (java.sql.SQLException e) {
      _telemetry.close(e);
      throw new ru.tinkoff.kora.database.jdbc.RuntimeSqlException(e);
    } catch (Exception e) {
      _telemetry.close(e);
      throw e;
    }
  }

  @Override
  @Log
  public void deleteUser(Long id) {
    var _ctxCurrent = ru.tinkoff.kora.common.Context.current();
    var _query = QUERY_CONTEXT_7;
    var _telemetry = this._connectionFactory.telemetry().createContext(_ctxCurrent, _query);
    var _conToUse = this._connectionFactory.currentConnection();
    Connection _conToClose;
    if (_conToUse == null) {
        _conToUse = this._connectionFactory.newConnection();
        _conToClose = _conToUse;
    } else {
        _conToClose = null;
    }
    try (_conToClose; var _stmt = _conToUse.prepareStatement(_query.sql())) {
      _stmt.setLong(1, id);
      _stmt.execute();
      _telemetry.close(null);

    } catch (java.sql.SQLException e) {
      _telemetry.close(e);
      throw new ru.tinkoff.kora.database.jdbc.RuntimeSqlException(e);
    } catch (Exception e) {
      _telemetry.close(e);
      throw e;
    }
  }

  @Override
  @Log
  public void deleteUserRoles(Long id) {
    var _ctxCurrent = ru.tinkoff.kora.common.Context.current();
    var _query = QUERY_CONTEXT_8;
    var _telemetry = this._connectionFactory.telemetry().createContext(_ctxCurrent, _query);
    var _conToUse = this._connectionFactory.currentConnection();
    Connection _conToClose;
    if (_conToUse == null) {
        _conToUse = this._connectionFactory.newConnection();
        _conToClose = _conToUse;
    } else {
        _conToClose = null;
    }
    try (_conToClose; var _stmt = _conToUse.prepareStatement(_query.sql())) {
      _stmt.setLong(1, id);
      _stmt.execute();
      _telemetry.close(null);

    } catch (java.sql.SQLException e) {
      _telemetry.close(e);
      throw new ru.tinkoff.kora.database.jdbc.RuntimeSqlException(e);
    } catch (Exception e) {
      _telemetry.close(e);
      throw e;
    }
  }
}
