package ua.artcode.core.pre_processor;

import ua.artcode.utils.RunUtils;
import ua.artcode.utils.StringUtils;

import java.util.Arrays;

/**
 * Created by v21k on 17.04.17.
 */
public class PreProcessors {
    // todo mvn go offline (pre-processor to match maven)

    public static MethodRunnerPreProcessor singleClass = ((classPaths) -> {
        String singleClassPath = classPaths[0];

        String classPath = StringUtils.getClassNameFromClassPath(singleClassPath, "/");
        String root = StringUtils.getClassRootFromClassPath(singleClassPath, "/");

        return new Class[]{RunUtils.getClass(classPath, root)};
    });

    public static MethodRunnerPreProcessor lessonsMain = (classPaths) -> {
        String mainClassPath = Arrays.stream(classPaths)
                .filter(path -> path.toLowerCase().contains("main"))
                .findFirst()
                .orElseThrow(() -> new ClassNotFoundException("Main class not found!"));

        String className = StringUtils.getClassNameFromClassPath(mainClassPath, "src/main/java/");
        String root = StringUtils.getClassRootFromClassPath(mainClassPath, "src/main/java/");

        return new Class<?>[]{RunUtils.getClass(className, root)};
    };

    public static MethodRunnerPreProcessor lessonsTests = ((classPaths) -> {
        String root = StringUtils.getClassRootFromClassPath(classPaths[0], "src/");

        String[] classNames = Arrays.stream(classPaths)
                .filter(path -> path.toLowerCase().contains("test"))
                .map(path -> StringUtils.getClassNameFromClassPath(path, "src/"))
                .toArray(String[]::new);

        Class<?>[] classes = new Class[classNames.length];

        for (int i = 0; i < classes.length; i++) {
            classes[i] = RunUtils.getClass(classNames[i], root);
        }

        return classes;
    });


}
