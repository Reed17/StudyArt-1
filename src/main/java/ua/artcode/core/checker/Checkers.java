package ua.artcode.core.checker;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by v21k on 24.04.17.
 */
public class Checkers {
    public static ClassChecker testChecker = (classes -> Arrays.stream(classes)
            .allMatch(cls -> Arrays.stream(cls.getDeclaredMethods())
                    .anyMatch(method -> method.isAnnotationPresent(Test.class))));

    public static ClassChecker main = (classes -> Arrays.stream(classes)
            .allMatch(cls -> Arrays.stream(cls.getDeclaredMethods())
                    .anyMatch(method -> method.getName().equals("main"))));
}
