package ua.artcode.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.artcode.core.method_checkers.MethodChecker;
import ua.artcode.core.method_runner.MethodRunner;
import ua.artcode.core.post_processor.MethodResultsProcessor;
import ua.artcode.core.pre_processor.MethodRunnerPreProcessor;
import ua.artcode.model.response.GeneralResponse;
import ua.artcode.model.response.RunResults;
import ua.artcode.utils.IO_utils.CommonIOUtils;
import ua.artcode.utils.RunUtils;
import ua.artcode.utils.StringUtils;

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

    // todo finish logging
    private static final Logger LOGGER = LoggerFactory.getLogger(RunCore.class);

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
            IllegalAccessException {

        // compile, save results and return it if there are any errors
        String compilationErrors = RunUtils.compile(classPaths);
        if (compilationErrors.length() > 0) {
            LOGGER.error(String.format("Compilation FAILED, errors: %s", compilationErrors));
            return new RunResults(new GeneralResponse(compilationErrors));
        }
        LOGGER.info("Compilation - OK");

        // prepare array with main class path and it's root package
        String[] paths = preProcessor.getPaths(classPaths);

        // getting a class
        Class<?> cls = RunUtils.getClass(paths[MAIN_CLASS_PATH], paths[MAIN_CLASS_ROOT_PACKAGE]);

        // checking method(s) needed
        if (!checker.checkMethods(cls)) {
            LOGGER.error("Method check - FAILED. Can't fine required method in class " + cls.getName());
            return new RunResults(new GeneralResponse("Can't find required method(s)"));
        }
        LOGGER.info("Method check - OK");

        // call runner
        String[] methodOutput = callRunner(runner, cls);

        // return RunResult
        return postProcessor.process(methodOutput);
    }

    /**
     * @return String array with next info:
     * 1. index 0 - runtime exceptions
     * 2. index 1 - system.out
     * 3. index 2 - methodOutput
     */
    private String[] callRunner(MethodRunner runner, Class<?> cls)
            throws IOException, NoSuchMethodException, IllegalAccessException {

        String runtimeException = null;
        String systemOut;
        String methodOutput = null;

        try (ByteArrayOutputStream redirectedSystemOut = new ByteArrayOutputStream()) {
            PrintStream systemOutOld = ioUtils.redirectSystemOut(redirectedSystemOut);

            // call method
            try {
                methodOutput = runner.runMethod(cls);
            } catch (InvocationTargetException e) {
                // save exception message
                runtimeException = StringUtils.getInvocationTargetExceptionInfo(e);
                //redirecting s.out back so we can use logger (and logger's message will not be processed in post-proc)
                ioUtils.resetSystemOut(redirectedSystemOut, systemOutOld);
                LOGGER.error("Method call - FAILED. " + runtimeException, e);
            } finally {
                systemOut = ioUtils.resetSystemOut(redirectedSystemOut, systemOutOld);
            }
        }
        LOGGER.info("Method call - OK");

        return new String[]{runtimeException, systemOut, methodOutput};
    }


}
