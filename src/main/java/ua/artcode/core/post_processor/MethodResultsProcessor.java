package ua.artcode.core.results_processor;

import ua.artcode.core.method_runner.MethodRunner;
import ua.artcode.model.RunResults;

/**
 * Created by v21k on 16.04.17.
 */

@FunctionalInterface
public interface MethodResultsProcessor {
    /**
     * Processing results from MethodRunner(string)
     *
     * @param runner MethodRunner with logic for running certain method
     * @param args   - String args in next order: compilation errors, systemOut, methodResult
     * @return RunResult model with all info
     * @see MethodRunner
     */
    RunResults process(MethodRunner runner, String... args);
}
