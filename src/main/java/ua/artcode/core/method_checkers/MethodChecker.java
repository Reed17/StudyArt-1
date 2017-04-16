package ua.artcode.core.method_checkers;

/**
 * Created by v21k on 16.04.17.
 */
@FunctionalInterface
public interface MethodChecker {
    boolean checkMethods(Class<?> cls);
}
