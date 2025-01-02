package ru.planet.user.dto;

import jakarta.annotation.Nullable;
import java.lang.Long;
import java.lang.NullPointerException;
import java.lang.String;
import java.sql.ResultSet;
import java.sql.SQLException;
import ru.tinkoff.kora.common.annotation.Generated;
import ru.tinkoff.kora.database.jdbc.mapper.result.JdbcResultSetMapper;

@Generated("ru.tinkoff.kora.database.annotation.processor.jdbc.extension.JdbcTypesExtension")
public final class $User_JdbcResultSetMapper implements JdbcResultSetMapper<User> {
  public $User_JdbcResultSetMapper() {
  }

  @Nullable
  public final User apply(ResultSet _rs) throws SQLException {
    if (!_rs.next()) {
      return null;
    }
    var _idColumn = _rs.findColumn("id");
    var _loginColumn = _rs.findColumn("login");
    var _passwordColumn = _rs.findColumn("password");
    var _cityColumn = _rs.findColumn("city");
    Long id = _rs.getLong(_idColumn);
    if (_rs.wasNull()) {
      throw new NullPointerException("Result field id is not nullable but row id has null");
    }
    String login = _rs.getString(_loginColumn);
    if (_rs.wasNull()) {
      throw new NullPointerException("Result field login is not nullable but row login has null");
    }
    String password = _rs.getString(_passwordColumn);
    if (_rs.wasNull()) {
      password = null;
    }
    String city = _rs.getString(_cityColumn);
    if (_rs.wasNull()) {
      city = null;
    }
    var _result = new User(
          id,
          login,
          password,
          city
        );
    if (_rs.next()) {
      throw new IllegalStateException("ResultSet was expected to return zero or one row but got two or more");
    }
    return _result;
  }
}
