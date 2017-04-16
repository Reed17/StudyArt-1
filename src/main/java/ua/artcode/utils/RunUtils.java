package ua.artcode.utils;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by v21k on 16.04.17.
 */
public class RunUtils {

    private static final JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();

    public static String compile(String classPath){
        ByteArrayOutputStream baosErr = new ByteArrayOutputStream();
        COMPILER.run(null, null, baosErr, classPath);
        return baosErr.toString();
    }

    public static Class<?> getClass(String className, String classRoot) throws MalformedURLException, ClassNotFoundException {
        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(classRoot).toURI().toURL()});
        return Class.forName(className, true, classLoader);
    }
}
