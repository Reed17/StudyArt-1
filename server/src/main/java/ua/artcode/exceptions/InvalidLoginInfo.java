package ua.artcode.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by zhenia on 29.04.17.
 */

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidLoginInfo extends AppException {
    public InvalidLoginInfo(String msg) {
        super(msg);
    }
}
