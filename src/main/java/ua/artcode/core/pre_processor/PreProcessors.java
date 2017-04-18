package ua.artcode.core.pre_processor;

import ua.artcode.utils.StringUtils;

/**
 * Created by v21k on 17.04.17.
 */
public class PreProcessors {
    public static MethodRunnerPreProcessor singleClass = (classPaths -> {
        String classPath = classPaths[0];
        return StringUtils.getClassNameAndRootFolder(classPath);
    });

    public static MethodRunnerPreProcessor lessons = (classPaths -> {
        String classPath = StringUtils.getClassPath(classPaths, "main");

        // todo extract to StringUtils
        String className = classPath.substring(classPath.lastIndexOf("src/")+4, classPath.lastIndexOf(".")).replace("/", ".");
        String rootPath = classPath.substring(0, classPath.lastIndexOf("src/")) + "src/";
        return new String[] {className, rootPath};
    });
}
