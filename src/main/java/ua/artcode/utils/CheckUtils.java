package ua.artcode.utils;

import ua.artcode.model.CheckResult;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

/**
 * Created by v21k on 13.04.17.
 */
public class CheckUtils {
    /**
     * @param className name of class which we will run
     * @param classLoader classloader to load our class
     *
     * @return CheckResult model
     * Check result model form:
     * overall tests (int)
     * passed tests (int)
     * failed tests (int)
     * tests info (String)
     * tests stats (String)
     *
     * @see StatsUtils#stats(String)
     * @see CheckResult
     * */
    public static CheckResult runCheckMethod(String className, URLClassLoader classLoader) {
        // todo should be passed as arg also
        String methodName = "check";
        // todo show how to results will be presented
        String results = null;
        try {
            Class<?> cls = Class.forName(className, true, classLoader);
            Method mainMethod = cls.getMethod(methodName);
            results = (String) mainMethod.invoke(null);
        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return StatsUtils.stats(results);
    }
}
