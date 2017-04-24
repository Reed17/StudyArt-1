package ua.artcode.core.post_processor;

import ua.artcode.core.method_runner.MethodRunner;
import ua.artcode.model.response.MethodStats;
import ua.artcode.model.response.RunResults;

/**
 * Created by v21k on 16.04.17.
 */

public interface MethodResultsProcessor {
    /**
     * Processing results from MethodRunner
     *
     * @return RunResult model with all info
     * @see MethodRunner
     */
    RunResults process(MethodStats stats, String systemErr, String systemOut);
}
