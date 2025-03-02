package ru.planet.common;

import lombok.experimental.UtilityClass;
import ru.tinkoff.kora.database.jdbc.RuntimeSqlException;

import java.sql.SQLException;

import static org.postgresql.util.PSQLState.FOREIGN_KEY_VIOLATION;
import static org.postgresql.util.PSQLState.UNIQUE_VIOLATION;

@UtilityClass
public class DuplicateExceptionValidator {
    public static boolean validateDuplicatePositionException(Throwable exception) {
        if (exception instanceof RuntimeSqlException) {
            return ((SQLException) exception.getCause()).getSQLState().equals(UNIQUE_VIOLATION.getState());
        }
        if (!(exception instanceof SQLException)) {
            return false;
        }
        return ((SQLException) exception).getSQLState().equals(UNIQUE_VIOLATION.getState());
    }

    public static boolean validateNotFoundKeyException(Throwable exception) {
        if (!(exception instanceof RuntimeSqlException)) {
            return false;
        }
        return ((SQLException) exception.getCause()).getSQLState().equals(FOREIGN_KEY_VIOLATION.getState());
    }
}
