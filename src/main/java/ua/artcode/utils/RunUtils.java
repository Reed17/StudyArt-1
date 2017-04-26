package ua.artcode.utils;

import javax.tools.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by v21k on 16.04.17.
 */
public class RunUtils {

    private static final JavaCompiler COMPILER = getSystemJavaCompiler();

    private static JavaCompiler getSystemJavaCompiler() {
        return ToolProvider.getSystemJavaCompiler();
    }

    public static String compile(String[] classPaths) throws IOException {
        // todo 1. Download maven dependencies to Course folder
        // todo 2. parse all .jar files
        // todo 3. understand how it works
        try (ByteArrayOutputStream baosErr = new ByteArrayOutputStream()) {
            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<JavaFileObject>();
            StandardJavaFileManager fileManager = COMPILER.getStandardFileManager(diagnostics, null, null);


            List<String> optionList = new ArrayList<>();
            optionList.add("-classpath");
            optionList.add("/home/v21k/.m2/repository/args4j/args4j/2.33/args4j-2.33.jar");

            JavaCompiler.CompilationTask task = COMPILER.getTask(new PrintWriter(baosErr),
                    fileManager,
                    diagnostics,
                    optionList,
                    null,
                    fileManager.getJavaFileObjectsFromStrings(Arrays.asList(classPaths)));
            task.call();
            return baosErr.toString();
        }
    }

    public static Class<?> getClass(String className, String classRoot) throws MalformedURLException,
            ClassNotFoundException {
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(classRoot).toURI().toURL()});
        return Class.forName(className, true, classLoader);
    }
}
