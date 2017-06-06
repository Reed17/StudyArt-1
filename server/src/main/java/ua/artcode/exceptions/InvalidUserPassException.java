package ua.artcode.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by zhenia on 23.04.17.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidUserPassException extends AppException {
    public InvalidUserPassException(String s) {
        super(s);
    }
}
