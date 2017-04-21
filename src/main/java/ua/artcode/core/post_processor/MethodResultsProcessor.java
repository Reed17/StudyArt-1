package ua.artcode.core.post_processor;

import ua.artcode.core.method_runner.MethodRunner;
import ua.artcode.model.RunResults;

/**
 * Created by v21k on 16.04.17.
 */

public interface MethodResultsProcessor {
    /**
     * Processing results from MethodRunner(string)
     *@param methodOutput String array with next info:
     *                    1. index 0 - runtime exceptions
     *                    2. index 1 - system.out
     *                    3. index 2 - methodOutput
     * @return RunResult model with all info
     * @see MethodRunner
     */
    RunResults process(String[] methodOutput);
}
