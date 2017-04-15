package ua.artcode.core;

import ua.artcode.utils.StringUtils;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by v21k on 15.04.17.
 */
public class RunCore {
    private static final JavaCompiler COMPILER = ToolProvider.getSystemJavaCompiler();

    public static String runClassWithMain(String classPath) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        ByteArrayOutputStream baosOut = new ByteArrayOutputStream();
        ByteArrayOutputStream baosErr = new ByteArrayOutputStream();

        COMPILER.run(null, baosOut, baosErr, classPath);

        String errors = baosErr.toString();
        String out = baosOut.toString();

        String className = StringUtils.getClassNameFromClassPath(classPath);
        String classRoot = StringUtils.getClassRootFromClassPath(classPath);

        URLClassLoader classLoader = URLClassLoader.newInstance(new URL[]{new File(classRoot).toURI().toURL()});
        Class<?> cls = Class.forName(className, true, classLoader);

        Method method = cls.getMethod("main", String[].class);
        String[] args = null;
        method.invoke(null, (Object) args);

        return errors.length() > 0 ? errors : out;
    }


}
