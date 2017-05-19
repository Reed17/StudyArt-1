package ua.artcode.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by v21k on 15.04.17.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidIDException extends AppException {
    public InvalidIDException(String message) {
        super(message);
    }
}
