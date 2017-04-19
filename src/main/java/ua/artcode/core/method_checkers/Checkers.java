package ua.artcode.core.method_checkers;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by v21k on 16.04.17.
 */
public class Checkers {
    public static MethodChecker main = ((cls) -> Arrays.stream(cls.getDeclaredMethods())
            .anyMatch(method -> method.getName().equals("main")));
}
