package ua.artcode.core;

import ua.artcode.core.method_checkers.MethodChecker;
import ua.artcode.core.method_runner.MethodRunner;
import ua.artcode.core.post_processor.MethodResultsProcessor;
import ua.artcode.core.pre_processor.MethodRunnerPreProcessor;
import ua.artcode.model.RunResults;
import ua.artcode.utils.IOUtils;
import ua.artcode.utils.RunUtils;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;

/**
 * Created by v21k on 15.04.17.
 */
public class RunCore {
    private static final int MAIN_CLASS_PATH = 0;
    private static final int MAIN_CLASS_ROOT_PACKAGE = 1;

    public static RunResults runMethod(String[] classPaths,
                                       MethodRunnerPreProcessor preProcessor,
                                       MethodChecker checker,
                                       MethodRunner runner,
                                       MethodResultsProcessor processor)
            throws MalformedURLException,
            ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException {

        // compile and save results (errors)
        String compilationErrors = RunUtils.compile(classPaths);

        // prepare array with main class path and it's root package
        String[] paths = preProcessor.getPaths(classPaths);

        // getting a class
        Class<?> cls = RunUtils.getClass(paths[MAIN_CLASS_PATH], paths[MAIN_CLASS_ROOT_PACKAGE]);

        // checking method(s) needed
        if (!checker.checkMethods(cls)) {
            return new RunResults("Can't find required method(s)");
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
