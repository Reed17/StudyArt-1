package ua.artcode.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.artcode.utils.IO_utils.CommonIOUtils;

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
import java.util.stream.Collectors;

/**
 * Created by v21k on 16.04.17.
 */
@Component
public class RunUtils {
    @Autowired
    CommonIOUtils ioUtils;

    private static final JavaCompiler COMPILER = getSystemJavaCompiler();

    private static JavaCompiler getSystemJavaCompiler() {
        return ToolProvider.getSystemJavaCompiler();
    }

    public String compile(String projectRoot, String[] classPaths) throws IOException {
        // todo 1. Download maven dependencies to Course folder
        // todo 2. parse all .jar files
        // todo 3. understand how it works
        try (ByteArrayOutputStream baosErr = new ByteArrayOutputStream()) {

            DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
            StandardJavaFileManager fileManager = COMPILER.getStandardFileManager(diagnostics, null, null);

/*            List<String> jars = Arrays.stream(ioUtils.parseFilePaths(projectRoot, ".jar"))
                    .map(path -> new File(path).getAbsolutePath())
                    .collect(Collectors.toList());*/

            List<String> optionList = new ArrayList<>();
            optionList.add("-cp");
            optionList.add("/home/v21k/IdeaProjects/StudyArtTeam/courses/1Primitives/dependencies/gson-2.8.0.jar");

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
