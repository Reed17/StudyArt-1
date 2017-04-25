package ua.artcode.core.pre_processor;

import ua.artcode.utils.StringUtils;

import java.io.File;

/**
 * Created by v21k on 17.04.17.
 */
public class PreProcessors {
    public static MethodRunnerPreProcessor singleClass = (classPaths -> {
        String classPath = classPaths[0];
        return StringUtils.getClassNameAndRootFolder(classPath, File.separator);
    });

    public static MethodRunnerPreProcessor lessons = (classPaths -> {
        String classPath = StringUtils.getClassPathByClassName(classPaths, "main");
        return StringUtils.getClassNameAndRootFolder(classPath, "src"+ File.separator);
    });


}
