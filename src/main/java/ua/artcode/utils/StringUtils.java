package ua.artcode.utils;

import ua.artcode.model.ExternalCode;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Created by v21k on 15.04.17.
 */
public class StringUtils {
    public static String getClassRootFromClassPath(String classPath, String delimiter) {
        return classPath.substring(0, classPath.lastIndexOf(delimiter)) + delimiter;
    }

    public static String getClassNameFromClassPath(String classPath, String delimiter) {
        return classPath.substring(classPath.lastIndexOf(delimiter) + delimiter.length(),
                classPath.lastIndexOf(".")).replace("/", ".");
    }

    public static String[] getClassNameAndRootFolder(String classPath, String delimiter) {
        return new String[]{getClassNameFromClassPath(classPath, delimiter),
                getClassRootFromClassPath(classPath, delimiter)};
    }

    public static String getClassPathByClassName(String[] classPaths, String className) throws ClassNotFoundException {
        return Arrays.stream(classPaths)
                .filter(classPath -> classPath.toLowerCase().contains(className.toLowerCase())
                        && classPath.contains(".java"))
                .findFirst()
                .orElseThrow(() -> new ClassNotFoundException("No Main class found"));
    }

    public static String getInvocationTargetExceptionInfo(InvocationTargetException e) {
        return String.format("Runtime exception: name: %s, message: %s",
                e.getTargetException().getClass().getName(),
                e.getTargetException().getMessage());
    }

    // todo replace by some template engine
    public static String appendSolution(ExternalCode code, String originalContent) {
        return originalContent.substring(0, originalContent.lastIndexOf("}")) + code.getSourceCode() + "}";
    }

}
