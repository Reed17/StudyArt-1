package ua.artcode.core.method_checkers;

import java.lang.reflect.Method;

/**
 * Created by v21k on 16.04.17.
 */
public class Checkers {
    public static MethodChecker main = ((cls) -> {
        Method[] methods = cls.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getName().equals("main")) {
                return true;
            }
        }
        return false;
    });
}
