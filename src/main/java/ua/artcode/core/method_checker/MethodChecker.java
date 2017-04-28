package ua.artcode.core.method_checker;

/**
 * Created by v21k on 24.04.17.
 */
@FunctionalInterface
public interface MethodChecker {
    void checkClasses(Class<?>[] classes) throws NoSuchMethodException;
}
