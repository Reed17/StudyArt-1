package ua.artcode.utils;

import com.google.common.collect.ObjectArrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.tools.*;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
    @Value("${application.courses.paths.dependencies}")
    private String dependenciesPath;

    private static JavaCompiler getSystemJavaCompiler() {
        return ToolProvider.getSystemJavaCompiler();
    }

    public static Class<?>[] getClass(String[] classNames, URLClassLoader urlClassLoader)
            throws IOException,
            ClassNotFoundException {

        Class<?>[] classes = new Class[classNames.length];

        for (int i = 0; i < classNames.length; i++) {
            classes[i] = Class.forName(classNames[i], true, urlClassLoader);
        }

        return classes;
    }

    /**
     * Convert all necessary paths to URL and return URLClassLoader instance
     */
    public static URLClassLoader getUrlClassLoader(String projectRoot, String[] sourcesRoot) throws IOException {
        // get all necessary paths as URLs
        URL[] classPaths = getClassPathsAsURLs(projectRoot, sourcesRoot);
        // password them to ClassLoader
        return new URLClassLoader(classPaths);
    }

    /**
     * Convert all classPath values (.jar and sourceRoot path) to URLs
     */
    private static URL[] getClassPathsAsURLs(String projectRoot, String[] sourcesRoot) throws IOException {

        URL[] jarPathsAsURL = convertToURL(getJarPaths(projectRoot));
        URL[] classPathsAsURL = convertToURL(sourcesRoot);
        return ObjectArrays.concat(jarPathsAsURL, classPathsAsURL, URL.class);
    }

    private static URL[] convertToURL(String[] paths) {
        return Arrays.stream(paths)
                .map(File::new)
                .map(File::toURI)
                .map(uri -> {
                    try {
                        return uri.toURL();
                    } catch (MalformedURLException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toArray(URL[]::new);
    }

    /**
     * Walks through project root dir and collects all .jar files
     */
    private static String[] getJarPaths(String jarPath) throws IOException {
        return Files.walk(Paths.get(jarPath))
                .map(Path::toString)
                .filter(path -> path.endsWith(".jar"))
                .map(path -> new File(path).getAbsolutePath())
                .toArray(String[]::new);
    }

    public String compile(String[] classPaths, String[] dependencies) throws IOException {
        DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

        String jarPathsAsString = Arrays.stream(getJarPaths(dependenciesPath))
                .filter(path -> Arrays.stream(dependencies)
                        .anyMatch(path::contains))
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
        JavaCompiler.CompilationTask task;

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

        task.call();

        // collect and return compilation errors
        return diagnostics.getDiagnostics().stream().map(Diagnostic::toString).collect(Collectors.joining());
    }
}
