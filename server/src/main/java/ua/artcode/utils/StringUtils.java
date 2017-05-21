package ua.artcode.utils;

import ua.artcode.model.ExternalCode;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

/**
 * Created by v21k on 15.04.17.
 */
public class StringUtils {
    public static String getClassRootFromClassPath(String classPath, String delimiter) {
        return classPath.substring(0, classPath.lastIndexOf(delimiter)) + delimiter;
    }

    public static String substringAfterDelimiter(String classPath, String delimiter) {
        return classPath.substring(classPath.lastIndexOf(delimiter) + delimiter.length(),
                classPath.lastIndexOf(".")).replace(File.separator, ".");
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

    public static String parseString(String source, String before, String after) {
        return source.substring(source.indexOf(before) + before.length(),
                source.indexOf(after) - after.length()).trim();
    }

    public static String normalizePath(String path) {
        return path.replace(File.separator + File.separator, File.separator);
    }

    public static String checkStartsWithAndAppend(String source, String startsWith){
        return source.startsWith(startsWith) ? source : startsWith + source;
    }
}
