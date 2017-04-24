package ua.artcode.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
    // constants with indexes for paths array (which contains Course className and root package).
    private static final int MAIN_CLASS_PATH = 0;
    private static final int MAIN_CLASS_ROOT_PACKAGE = 1;

    private static final Logger LOGGER = LoggerFactory.getLogger(RunCore.class);

    @Autowired
    private CommonIOUtils ioUtils;

    public RunResults runMethod(String[] classPaths,
                                MethodRunnerPreProcessor preProcessor,
                                String methodName,
                                MethodRunner runner,
                                MethodResultsProcessor postProcessor)
            throws IOException,
            ClassNotFoundException,
            NoSuchMethodException,
            IllegalAccessException {

        // compile, save results and return it if there are any errors
        String compilationErrors = RunUtils.compile(classPaths);

        // checking for compilation errors
        if (compilationErrors.length() > 0) {
            LOGGER.error(String.format("Compilation FAILED, errors: %s", compilationErrors));
            return new RunResults(new GeneralResponse(compilationErrors));
        }
        LOGGER.info("Compilation - OK");

        // prepare array with classes
        Class<?>[] classes = preProcessor.getClasses(classPaths, methodName);

        // checking method(s) needed
        if (!RunUtils.checkMethods(classes, methodName)) {
            LOGGER.error("Method check - FAILED. Can't fine method {}", methodName);
            return new RunResults(new GeneralResponse("Can't find required method(s)"));
        }
        LOGGER.info("Method check - OK");

        // call runner
        String[] methodOutput = callRunner(runner, classes);

        // return RunResult
        return postProcessor.process(methodOutput);
    }

    /**
     * @return String array with next info:
     * 1. index 0 - runtime exceptions
     * 2. index 1 - system.out
     * 3. index 2 - methodOutput
     */
    private String[] callRunner(MethodRunner runner, Class<?>[] cls)
            throws IOException, NoSuchMethodException, IllegalAccessException {

        String systemError = null;
        String systemOut = null;
        String methodOutput = null;

        try (ByteArrayOutputStream redirectedSystemOut = new ByteArrayOutputStream()) {
            PrintStream systemOutOld = ioUtils.redirectSystemOut(redirectedSystemOut);

            // call method
            try {
                methodOutput = runner.runMethod(cls);
                systemOut = ioUtils.resetSystemOut(redirectedSystemOut, systemOutOld);
                LOGGER.info("Method call - OK");
            } catch (InvocationTargetException e) {
                // save exception message
                systemError = StringUtils.getInvocationTargetExceptionInfo(e);
                //redirecting s.out back so we can use logger (and logger's message will not be processed in post-proc)
                ioUtils.resetSystemOut(redirectedSystemOut, systemOutOld);
                LOGGER.error("Method call - FAILED. Caused by: {}", systemError);
            }


            return new String[]{systemError, systemOut, methodOutput};
        }
    }
}
