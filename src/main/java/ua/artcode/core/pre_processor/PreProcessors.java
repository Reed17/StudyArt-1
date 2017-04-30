package ua.artcode.core.pre_processor;

import ua.artcode.utils.RunUtils;

import java.io.File;
import java.util.Arrays;

import static ua.artcode.utils.StringUtils.getClassNameFromClassPath;

/**
 * Created by v21k on 17.04.17.
 */
public class PreProcessors {

    public static MethodRunnerPreProcessor singleClass = ((classPaths, classLoader) -> {


        String[] classNames = new String[]{getClassNameFromClassPath(classPaths[0], File.separator)};

        checkIfEmpty(classNames);

        return RunUtils.getClass(classNames, classLoader);
    });

    public static MethodRunnerPreProcessor lessonsMain = ((classPaths, classLoader) -> {
        String classNames[] = Arrays.stream(classPaths)
                .map(path -> getClassNameFromClassPath(path, "java"+File.separator))
                .filter(path -> path.toLowerCase().contains("main"))
                .toArray(String[]::new);

        checkIfEmpty(classNames);

        return RunUtils.getClass(classNames, classLoader);
    });

    public static MethodRunnerPreProcessor lessonsTests = ((classPaths, classLoader) -> {

        String[] classNames = Arrays.stream(classPaths)
                .map(path -> getClassNameFromClassPath(path, "java"+File.separator))
                .filter(path -> path.toLowerCase().contains("test"))
                .toArray(String[]::new);

        checkIfEmpty(classNames);

        return RunUtils.getClass(classNames, classLoader);
    });

    private static void checkIfEmpty(String[] classNames) throws ClassNotFoundException {
        if (classNames.length == 0) {
            throw new ClassNotFoundException("Main class not found!");
        }
    }
}