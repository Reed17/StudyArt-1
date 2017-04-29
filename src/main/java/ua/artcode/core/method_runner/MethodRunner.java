package ua.artcode.core.method_runner;


import ua.artcode.model.response.MethodStats;

import java.lang.reflect.InvocationTargetException;

/**
 * Created by v21k on 16.04.17.
 */
@FunctionalInterface
public interface MethodRunner {
    /**
     * Functional interface to make various logic for calling methods
     *
     * @param cls
     * @return String - results
     */
    MethodStats runMethod(Class<?>[] cls) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;
}
