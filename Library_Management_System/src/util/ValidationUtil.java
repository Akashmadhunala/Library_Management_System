package util;

import exceptions.DatabaseException;

public class ValidationUtil {

    public static void validateBookTitle(String title) throws DatabaseException {
        if (title == null || title.trim().isEmpty()) {
            throw new DatabaseException("Book title cannot be empty.");
        }
        if (title.length() > 35) {
            throw new DatabaseException("Book title cannot exceed 35 characters.");
        }
    }

    public static void validateNotEmpty(String field, String value) throws DatabaseException {
        if (value == null || value.trim().isEmpty()) {
            throw new DatabaseException(field + " cannot be empty.");
        }
    }
}
