package ua.artcode.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.artcode.core.method_checker.MethodChecker;
import ua.artcode.core.method_runner.MethodRunner;
import ua.artcode.core.post_processor.MethodResultsProcessor;
import ua.artcode.core.pre_processor.MethodRunnerPreProcessor;
import ua.artcode.model.response.GeneralResponse;
import ua.artcode.model.response.MethodStats;
import ua.artcode.model.response.ResponseType;
import ua.artcode.model.response.RunResults;
import ua.artcode.utils.IO_utils.CommonIOUtils;
import ua.artcode.utils.IO_utils.CourseIOUtils;
import ua.artcode.utils.RunUtils;
import ua.artcode.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLClassLoader;

import static ua.artcode.utils.RunUtils.getUrlClassLoader;

/**
 * Created by v21k on 15.04.17.
 */
@Component
public class RunCore {
    private static final Logger LOGGER = LoggerFactory.getLogger(RunCore.class);

    private final CommonIOUtils ioUtils;

    private final CourseIOUtils courseIOUtils;

    @Autowired
    public RunCore(CommonIOUtils ioUtils, CourseIOUtils courseIOUtils) {
        this.ioUtils = ioUtils;
        this.courseIOUtils = courseIOUtils;
    }

    public RunResults run(String projectRoot,
                          String[] sourcesRoot,
                          String[] classPaths,
                          MethodRunnerPreProcessor preProcessor,
                          MethodChecker checker,
                          MethodRunner runner,
                          MethodResultsProcessor postProcessor)
            throws IOException,
            ClassNotFoundException,
            NoSuchMethodException,
            IllegalAccessException {

        // todo this step must be extracted somewhere else - it reduces performance dramatically
        // download and save maven dependencies
        if (!courseIOUtils.saveMavenDependenciesLocally(projectRoot)) {
            LOGGER.error("Maven dependencies download - FAILED");
            return new RunResults(
                    new GeneralResponse(ResponseType.ERROR, "Maven dependencies download - FAILED. Please, check(or add) pom.xml"));
        }
        LOGGER.info("Maven dependencies download - OK.");


        // compile, save results and return it if there are any errors
        String compilationErrors = RunUtils.compile(projectRoot, classPaths);

        // checking for compilation errors
        if (compilationErrors.length() > 0) {
            LOGGER.error(String.format("Compilation FAILED, errors: %s", compilationErrors));
            return new RunResults(new GeneralResponse(ResponseType.ERROR, compilationErrors));
        }
        LOGGER.info("Compilation - OK");

        try (
                // getting classLoader instance
                URLClassLoader classLoader = getUrlClassLoader(projectRoot, sourcesRoot)
        ) {
            // prepare array with classes
            Class<?>[] classes = preProcessor.getClasses(classPaths, classLoader);

            // checking methods/annotations needed
            checker.checkClasses(classes);

            LOGGER.info("Check classes for methods/annotations - OK");

            // call runner
            String systemError = null;
            String systemOut = null;
            MethodStats stats = null;

            try (ByteArrayOutputStream redirectedSystemOut = new ByteArrayOutputStream()) {
                PrintStream systemOutOld = ioUtils.redirectSystemOut(redirectedSystemOut);

                // call method
                try {
                    stats = runner.runMethod(classes);
                    systemOut = ioUtils.resetSystemOut(redirectedSystemOut, systemOutOld);
                    LOGGER.info("Method call - OK");
                } catch (InvocationTargetException e) {
                    // save exception message
                    systemError = StringUtils.getInvocationTargetExceptionInfo(e);
                    //redirecting s.out back so we can use logger (and logger's message will not be processed in post-proc)
                    ioUtils.resetSystemOut(redirectedSystemOut, systemOutOld);
                    LOGGER.error("Method call - FAILED. Caused by: {}", systemError);
                }
            }

            // return RunResult
            return postProcessor.process(stats, systemError, systemOut);
        }
    }
}
