package ua.artcode.core.method_runner;


import java.lang.reflect.InvocationTargetException;

/**
 * Created by v21k on 16.04.17.
 */
public interface MethodRunner {
    /**
     * Functional interface to make various logic for running methods
     *
     * @param cls
     * @return String - results
     */
    String runMethod(Class<?> cls) throws InvocationTargetException, IllegalAccessException, NoSuchMethodException;
}
