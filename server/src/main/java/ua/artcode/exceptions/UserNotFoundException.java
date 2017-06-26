package ua.artcode.exceptions;

/**
 * Created by v21k on 26.06.17.
 */
public class UserNotFoundException extends AppException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
