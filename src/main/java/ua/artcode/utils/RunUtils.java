package ua.artcode.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(RunUtils.class);

    private static JavaCompiler getSystemJavaCompiler() {
        return ToolProvider.getSystemJavaCompiler();
    }

    // todo check concurrency
    public static String compile(String projectRoot, String[] classPaths) throws IOException {
        // diagnostics - to collect compilation errors
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
//        StandardJavaFileManager fileManager = COMPILER.getStandardFileManager(diagnostics, null, null);

        // .jar files (will be added to classpath) - must be concatenated with ":" delimiter
        String jarPathsAsString = getJarPaths(projectRoot).stream()
                .map(path -> path + File.pathSeparator)
                .collect(Collectors.joining());

        // option list ---> "javac [options...]"
        List<String> optionList = new ArrayList<>();

        // -cp command cant be executed without args, so we make sure that jarPaths not empty
        if (!jarPathsAsString.isEmpty()) {
            optionList.add("-cp");
            optionList.add(jarPathsAsString + ".");
        }

        // task for compiler
        JavaCompiler.CompilationTask task = null;
        try (StandardJavaFileManager fileManager = COMPILER.getStandardFileManager(diagnostics, null, null);) {
            task = COMPILER.getTask(null,
                    fileManager,
                    diagnostics,
                    optionList,
                    null,
                    fileManager.getJavaFileObjectsFromStrings(Arrays.asList(classPaths)));
        } catch (Exception e) {
            LOGGER.error("Compilation task building - FAILED.", e);
            return e.getMessage();
        }

        // task call
        task.call();

        // collect and return compilation errors
        String errors = diagnostics.getDiagnostics().stream().map(Diagnostic::toString).collect(Collectors.joining());
        return errors;
    }

    public static Class<?>[] getClass(String[] classNames, URLClassLoader urlClassLoader)
            throws IOException,
            ClassNotFoundException {


        Class<?>[] classes = new Class[classNames.length];
        // no streams because of exception handling

//        // getting classLoader instance
//        URLClassLoader classLoader = getUrlClassLoader(projectRoot, sourcesRoot);


//        try (
//                // getting classLoader instance
//                URLClassLoader classLoader = getUrlClassLoader(projectRoot, sourcesRoot);
//        ) {
            for (int i = 0; i < classNames.length; i++) {
                classes[i] = Class.forName(classNames[i], true, urlClassLoader);
            }
//        }

//        classLoader.close();

        return classes;
    }

    /**
     * Convert all necessary paths to URL and return URLClassLoader instance
     */
    public static URLClassLoader getUrlClassLoader(String projectRoot, String sourcesRoot) throws IOException {
        // get all necessary paths as URLs
        URL[] classPaths = getClassPathsAsURLs(projectRoot, sourcesRoot);
        // pass them to ClassLoader
        return new URLClassLoader(classPaths);
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
