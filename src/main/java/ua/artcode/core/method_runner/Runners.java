package ua.artcode.core.method_runner;

import java.lang.reflect.Method;

/**
 * Created by v21k on 16.04.17.
 */
public class Runners {
    public static MethodRunner main = (cls -> {
        Method method = cls.getMethod("main", String[].class);
        String[] argumentsForMain = null;
        // todo test this place with a negative test
        method.invoke(null, (Object) argumentsForMain);
        return "";
    });
}
