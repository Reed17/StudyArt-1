package ua.artcode.utils;

import org.springframework.stereotype.Component;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by v21k on 16.04.17.
 */
@Component
public class RunUtils {


    private static final JavaCompiler COMPILER = getSystemJavaCompiler();

    private static JavaCompiler getSystemJavaCompiler() {
        return ToolProvider.getSystemJavaCompiler();
    }

    // todo check concurrency
    public static String compile(String projectRoot, String[] classPaths) throws IOException {
        // diagnostics - to collect compilation errors
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
        StandardJavaFileManager fileManager = COMPILER.getStandardFileManager(diagnostics, null, null);

        // .jar files (will be added to classpath)
        List<String> jarPaths = getJarPaths(projectRoot);

        // option list ---> "javac [options...]"
        List<String> optionList = new ArrayList<>();

        // -cp command cant be executed without args, so we make sure that there is at least 1
        if (jarPaths.size() > 0) {
            optionList.add("-cp");
            optionList.addAll(jarPaths);
        }

        // task for compiler
        JavaCompiler.CompilationTask task = COMPILER.getTask(null,
                fileManager,
                diagnostics,
                optionList,
                null,
                fileManager.getJavaFileObjectsFromStrings(Arrays.asList(classPaths)));

        // task call
        task.call();

        // collect and return compilation errors
        return diagnostics.getDiagnostics().stream().map(Diagnostic::toString).collect(Collectors.joining());
    }

    public static Class<?>[] getClass(String projectRoot, String sourcesRoot, String[] classNames)
            throws IOException,
            ClassNotFoundException {

        // getting classLoader instance
        URLClassLoader classLoader = getUrlClassLoader(projectRoot, sourcesRoot);

        Class<?>[] classes = new Class[classNames.length];
        // no streams because of exception handling
        for (int i = 0; i < classNames.length; i++) {
            classes[i] = Class.forName(classNames[i], true, classLoader);
        }
        return classes;
    }

    /**
     * Convert all necessary paths to URL and return URLClassLoader instance
     */
    private static URLClassLoader getUrlClassLoader(String projectRoot, String sourcesRoot) throws IOException {
        // get all necessary paths as URLs
        URL[] classPaths = getClassPathsAsURLs(projectRoot, sourcesRoot);
        // pass them to ClassLoader
        return URLClassLoader.newInstance(classPaths);
    }

    /**
     * Convert all classPath values (.jar and sourceRoot path) to URLs
     */
    private static URL[] getClassPathsAsURLs(String projectRoot, String sourcesRoot) throws IOException {
        List<String> jarPaths = getJarPaths(projectRoot);

        // size()+1 - because we will add 1 more URL - sourceRoot
        URL[] classPathsAsURLs = new URL[jarPaths.size() + 1];
        for (int i = 0; i < jarPaths.size(); i++) {
            classPathsAsURLs[i] = new File(jarPaths.get(i)).toURI().toURL();
        }
        classPathsAsURLs[classPathsAsURLs.length - 1] = new File(sourcesRoot).toURI().toURL();

        return classPathsAsURLs;
    }

    /**
     * Walks through project root dir and collects all .jar files
     */
    private static List<String> getJarPaths(String projectRoot) throws IOException {
        return Files.walk(Paths.get(projectRoot))
                .map(Path::toString)
                .filter(path -> path.endsWith(".jar"))
                .map(path -> new File(path).getAbsolutePath())
                .collect(Collectors.toList());
    }
}
