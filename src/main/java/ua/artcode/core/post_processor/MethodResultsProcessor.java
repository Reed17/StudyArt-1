package ua.artcode.core.post_processor;

import ua.artcode.core.method_runner.MethodRunner;
import ua.artcode.model.RunResults;

/**
 * Created by v21k on 16.04.17.
 */

public interface MethodResultsProcessor {
    /**
     * Processing results from MethodRunner(string)
     *
     * @return RunResult model with all info
     * @see MethodRunner
     */
    RunResults process(String runtimeExceptions, String systemOut, String methodOutput);
}
