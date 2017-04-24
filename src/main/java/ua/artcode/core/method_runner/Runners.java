package ua.artcode.core.method_runner;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Created by v21k on 16.04.17.
 */
public class Runners {
    public static MethodRunner main = (classes -> {
        for (Class<?> aClass : classes) {
            Method method = aClass.getMethod("main", String[].class);
            String[] argumentsForMain = null;
            // todo test this place with a negative test
            method.invoke(null, (Object) argumentsForMain);
        }
        return "";
    });

    // todo get JUnitCore.class and method run() and pass classes there

}
