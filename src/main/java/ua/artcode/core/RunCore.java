package ua.artcode.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.artcode.core.method_checkers.MethodChecker;
import ua.artcode.core.method_runner.MethodRunner;
import ua.artcode.core.post_processor.MethodResultsProcessor;
import ua.artcode.core.pre_processor.MethodRunnerPreProcessor;
import ua.artcode.model.RunResults;
import ua.artcode.utils.IO_utils.CommonIOUtils;
import ua.artcode.utils.RunUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by v21k on 15.04.17.
 */
@Component
public class RunCore {
    // constants with indexes for paths array (which containsCourse className and root package).
    private static final int MAIN_CLASS_PATH = 0;
    private static final int MAIN_CLASS_ROOT_PACKAGE = 1;

    @Autowired
    private CommonIOUtils ioUtils;

    public RunResults runMethod(String[] classPaths,
                                MethodRunnerPreProcessor preProcessor,
                                MethodChecker checker,
                                MethodRunner runner,
                                MethodResultsProcessor postProcessor)
            throws IOException,
            ClassNotFoundException,
            NoSuchMethodException,
            InvocationTargetException,
            IllegalAccessException {

        // compile and save results (errors)
        String compilationErrors = RunUtils.compile(classPaths);

        if (compilationErrors.length() > 0) {
            return new RunResults(compilationErrors);
        }
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
        PrintStream systemOutOld = ioUtils.redirectSystemOut(redirectedSystemOut);

        // run method
        String methodOutput = runner.runMethod(cls);
        // todo how to process runtime errors...

        // reset s.out to old and save results from previous
        String systemOut = ioUtils.resetSystemOut(redirectedSystemOut, systemOutOld);

        // return RunResult
        return postProcessor.process(runner, compilationErrors, systemOut, methodOutput);
    }


}
