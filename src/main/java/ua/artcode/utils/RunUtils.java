package ua.artcode.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by v21k on 16.04.17.
 */
public class RunUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(RunUtils.class);

    private static final JavaCompiler COMPILER = getSystemJavaCompiler();

    // todo use logger
    private static JavaCompiler getSystemJavaCompiler() {
        return ToolProvider.getSystemJavaCompiler();
    }

    // todo get s.out too
    public static String compile(String[] classPaths) throws IOException {
        try (ByteArrayOutputStream baosErr = new ByteArrayOutputStream()) {
            COMPILER.run(null, null, baosErr, classPaths);
            return baosErr.toString();
        }
    }

    public static Class<?> getClass(String className, String classRoot) throws MalformedURLException,
            ClassNotFoundException {
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(classRoot).toURI().toURL()});
        return Class.forName(className, true, classLoader);
    }
}
