package ua.artcode.core.post_processor;

import ua.artcode.model.RunResults;

public class ResultsProcessors {
    private static final int RUNTIME_EXCEPTIONS = 0;
    private static final int SYSTEM_OUT = 1;
    private static final int METHOD_OUTPUT = 2;
    public static MethodResultsProcessor main = ((methodOutput) ->
            methodOutput[RUNTIME_EXCEPTIONS] != null && methodOutput[RUNTIME_EXCEPTIONS].length() > 0 ?
                    new RunResults(methodOutput[RUNTIME_EXCEPTIONS]) :
                    new RunResults(methodOutput[METHOD_OUTPUT], methodOutput[SYSTEM_OUT]));
}
