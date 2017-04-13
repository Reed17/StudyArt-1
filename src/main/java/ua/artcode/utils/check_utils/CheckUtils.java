package ua.artcode.utils.check_utils;

import ua.artcode.model.CheckResult;
import ua.artcode.utils.stats_utils.StatsUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URLClassLoader;

/**
 * Created by v21k on 13.04.17.
 */
public class CheckUtils {
    public static CheckResult runCheckMethod(String className, URLClassLoader classLoader) {
        String methodName = "check";
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
