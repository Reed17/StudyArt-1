package ua.artcode.core.post_processor;

import ua.artcode.model.response.MethodResult;
import ua.artcode.model.response.MethodStats;
import ua.artcode.model.response.RunResults;

public class ResultsProcessors {
    private static final int RUNTIME_EXCEPTIONS = 0;
    private static final int SYSTEM_OUT = 1;
    private static final int METHOD_OUTPUT = 2;
    public static MethodResultsProcessor main = ((methodOutput) ->
            new RunResults(
                    new MethodResult(methodOutput[SYSTEM_OUT],
                            methodOutput[RUNTIME_EXCEPTIONS],
                            methodOutput[METHOD_OUTPUT]),
                    new MethodStats()));
}
