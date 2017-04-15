package ua.artcode.utils;

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
}
