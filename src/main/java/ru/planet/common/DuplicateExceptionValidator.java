package ru.planet.common;

import lombok.experimental.UtilityClass;

import java.sql.SQLException;

import static org.postgresql.util.PSQLState.UNIQUE_VIOLATION;

@UtilityClass
public class DuplicateExceptionValidator {
    public static boolean validateDuplicatePositionException(Throwable exception) {
        if (!(exception instanceof SQLException)) {
            return false;
        }
        return ((SQLException) exception).getSQLState().equals(UNIQUE_VIOLATION.getState());
    }
}
