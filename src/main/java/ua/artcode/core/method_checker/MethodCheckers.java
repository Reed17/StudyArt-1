package ua.artcode.core.method_checker;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

/**
 * Created by v21k on 24.04.17.
 */
public class MethodCheckers {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodChecker.class);

    public static MethodChecker testChecker = (classes ->
    {
        if (!Arrays.stream(classes)
                .allMatch(cls -> Arrays.stream(cls.getDeclaredMethods())
                        .anyMatch(method -> method.isAnnotationPresent(Test.class)))) {
            LOGGER.error("Check classes for methods/annotations - FAILED. @Test methods not found");
            throw new NoSuchMethodException("@Test methods not found");
        }
    });

    public static MethodChecker main = (classes -> {
        if (!Arrays.stream(classes)
                .allMatch(cls -> Arrays.stream(cls.getDeclaredMethods())
                        .anyMatch(method -> method.getName().equals("main")))) {
            LOGGER.error("Check classes for methods/annotations - FAILED. Main method not found");
            throw new NoSuchMethodException("Main method not found");
        }
    });

}
