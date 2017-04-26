package ua.artcode.core.method_checker;

import org.junit.Test;

import javax.el.MethodNotFoundException;
import java.util.Arrays;

/**
 * Created by v21k on 24.04.17.
 */
public class MethodCheckers {
    public static MethodChecker testChecker = (classes ->
    {
        if (!Arrays.stream(classes)
                .allMatch(cls -> Arrays.stream(cls.getDeclaredMethods())
                        .anyMatch(method -> method.isAnnotationPresent(Test.class)))){
            throw new NoSuchMethodException("@Test methods not found");
        }
    });

    public static MethodChecker main = (classes -> {
        if(!Arrays.stream(classes)
                .allMatch(cls -> Arrays.stream(cls.getDeclaredMethods())
                        .anyMatch(method -> method.getName().equals("main")))){
            throw new NoSuchMethodException("Main method not found");
        }
    });

}
