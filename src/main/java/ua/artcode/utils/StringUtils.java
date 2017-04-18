package ua.artcode.utils;

import java.util.Arrays;

/**
 * Created by v21k on 15.04.17.
 */
public class StringUtils {
    public static String getClassRootFromClassPath(String classPath) {
        return classPath.substring(0, classPath.lastIndexOf("/"));
    }

    public static String getClassNameFromClassPath(String classPath) {
        return classPath.substring(classPath.lastIndexOf("/") + 1, classPath.lastIndexOf("."));
    }

    public static String[] getClassNameAndRootFolder(String classPath) {
        return new String[]{getClassNameFromClassPath(classPath), getClassRootFromClassPath(classPath)};
    }

    public static String getClassPath(String[] classPaths, String className) throws ClassNotFoundException {
        return Arrays.stream(classPaths)
                .filter(classPath -> classPath.toLowerCase().contains(className.toLowerCase()))
                .findFirst()
                .orElseThrow(() -> new ClassNotFoundException("No Main class found"));
    }
}
