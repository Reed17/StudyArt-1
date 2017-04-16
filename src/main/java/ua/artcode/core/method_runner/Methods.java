package ua.artcode.core.method_runner;

import java.lang.reflect.Method;

/**
 * Created by v21k on 16.04.17.
 */
public class Methods {
    public static MethodRunner main = (cls -> {
        Method method = cls.getMethod("main", String[].class);
        String[] argumentsForMain = null;
        method.invoke(null, (Object) argumentsForMain);
        return "";
    });
}
