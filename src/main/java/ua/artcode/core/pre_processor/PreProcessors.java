package ua.artcode.core.pre_processor;

import ua.artcode.utils.RunUtils;
import ua.artcode.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by v21k on 17.04.17.
 */
public class PreProcessors {
    // todo mvn go offline
    public static MethodRunnerPreProcessor singleClass = ((classPaths, methodName) -> {
        String singleClassPath = classPaths[0];

        String classPath = StringUtils.getClassNameFromClassPath(singleClassPath, "/");
        String root = StringUtils.getClassRootFromClassPath(singleClassPath, "/");

        return new Class[]{RunUtils.getClass(classPath, root)};
    });

    public static MethodRunnerPreProcessor lessons = ((classPaths, methodName) -> {
        String root = StringUtils.getClassRootFromClassPath(classPaths[0], "src/");

        int count = (int) Arrays.stream(classPaths)
                .filter(path -> path.toLowerCase().contains(methodName))
                .count();

        Class<?>[] classes = new Class[count];

        String[] classNames = Arrays.stream(classPaths)
                .filter(path -> path.toLowerCase().contains(methodName))
                .map(path -> StringUtils.getClassNameFromClassPath(path, "src/"))
                .toArray(String[]::new);

        for (int i = 0; i < classes.length; i++) {
            classes[i] = RunUtils.getClass(classNames[i], root);
        }

        return classes;
    });


}
