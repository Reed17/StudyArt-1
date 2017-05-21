package ua.artcode.utils;


import org.springframework.stereotype.Component;
import ua.artcode.exceptions.UnexpectedNullException;

@Component
public class ResultChecker {
    public void checkNull(Object o, String s) throws UnexpectedNullException {
        if(o == null) throw new UnexpectedNullException("Result of action - null pointer!\t" + s);
    }
}
