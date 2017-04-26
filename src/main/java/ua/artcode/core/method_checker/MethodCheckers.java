package ua.artcode.core.method_checker;

import org.junit.Test;

import java.util.Arrays;

/**
 * Created by v21k on 24.04.17.
 */
public class MethodCheckers {
    public static MethodChecker testChecker = (classes -> Arrays.stream(classes)
            .allMatch(cls -> Arrays.stream(cls.getDeclaredMethods())
                    .anyMatch(method -> method.isAnnotationPresent(Test.class))));

    public static MethodChecker main = (classes -> Arrays.stream(classes)
            .allMatch(cls -> Arrays.stream(cls.getDeclaredMethods())
                    .anyMatch(method -> method.getName().equals("main"))));
}
