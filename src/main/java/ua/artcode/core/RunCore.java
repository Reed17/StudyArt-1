package ua.artcode.core;

import ua.artcode.core.method_checkers.MethodChecker;
import ua.artcode.core.method_runner.MethodRunner;
import ua.artcode.core.results_processor.MethodRusultsProcessor;
import ua.artcode.model.RunResults;
import ua.artcode.utils.IOUtils;
import ua.artcode.utils.RunUtils;
import ua.artcode.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

/**
 * Created by v21k on 15.04.17.
 */
public class RunCore {

    public static RunResults runMethod(String classPath, MethodChecker checker, MethodRunner runner, MethodRusultsProcessor processor) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        // compile and save results (errors)
        String compilationErrors = RunUtils.compile(classPath);

        // parse class name and root package for this class
        String className = StringUtils.getClassNameFromClassPath(classPath);
        String classRoot = StringUtils.getClassRootFromClassPath(classPath);

        // getting class
        Class<?> cls = RunUtils.getClass(className, classRoot);

        // checking method(s) needed
        if(!checker.checkMethods(cls)){
            return new RunResults("Can't find required method");
        }

        // redirecting s.out
        ByteArrayOutputStream redirectedSystemOut = new ByteArrayOutputStream();
        PrintStream systemOutOld = IOUtils.redirectSystemOut(redirectedSystemOut);

        // run method
        String methodOutput = runner.runMethod(cls);

        // reset s.out to old and save results from previous
        String systemOut = IOUtils.resetSystemOut(redirectedSystemOut, systemOutOld);

        // return RunResult
        return processor.process(runner, compilationErrors, systemOut, methodOutput);
    }



}
