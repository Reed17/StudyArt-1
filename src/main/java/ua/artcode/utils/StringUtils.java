package ua.artcode.utils;

import ua.artcode.model.ExternalCode;

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

    public static String getClassPathByClassName(String[] classPaths, String className) throws ClassNotFoundException {
        return Arrays.stream(classPaths)
                .filter(classPath -> classPath.toLowerCase().contains(className.toLowerCase())
                        && classPath.contains(".java"))
                .findFirst()
                .orElseThrow(() -> new ClassNotFoundException("No Main class found"));
    }

    // todo replace by some template engine
    public static String appendSolution(ExternalCode code, String originalContent) {
        return originalContent.substring(0, originalContent.lastIndexOf("}")) + code.getSourceCode() + "}";
    }

}
