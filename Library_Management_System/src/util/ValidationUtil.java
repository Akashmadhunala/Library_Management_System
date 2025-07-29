package util;

import exceptions.ManagementException;

public class ValidationUtil {

    public static void validateBookTitle(String title) throws ManagementException {
        if(title == null || title.trim().isEmpty()){
            throw new ManagementException("Book title cannot be empty.");
        }
        if(title.length() > 35){
            throw new ManagementException("Book title cannot exceed 35 characters.");
        }
    }

    public static void validateNotEmpty(String field, String value) throws ManagementException{
        if (value == null || value.trim().isEmpty()) {
            throw new ManagementException(field + " cannot be empty.");
        }
    }
}
