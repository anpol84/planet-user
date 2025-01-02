package ru.planet.user.dto;

import java.lang.Long;
import java.lang.NullPointerException;
import java.lang.String;
import java.sql.ResultSet;
import java.sql.SQLException;
import ru.tinkoff.kora.common.annotation.Generated;
import ru.tinkoff.kora.database.jdbc.mapper.result.JdbcRowMapper;

@Generated("ru.tinkoff.kora.database.annotation.processor.jdbc.extension.JdbcTypesExtension")
public final class $User_JdbcRowMapper implements JdbcRowMapper<User> {
  public $User_JdbcRowMapper() {
  }

  public final User apply(ResultSet _rs) throws SQLException {
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
    return _result;
  }
}
