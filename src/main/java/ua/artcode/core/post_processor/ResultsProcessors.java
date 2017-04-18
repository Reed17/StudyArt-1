package ua.artcode.core.results_processor;

import ua.artcode.model.RunResults;

public class ResultsProcessors {
    public static int ERORR = 0;
    public static int SYSTEM_OUT = 1;
    public static int METHOD_RESULTS = 2;

    public static MethodResultsProcessor main = ((runner, args) -> {
        return args[ERORR].length() > 0 ? new RunResults(args[ERORR]) : new RunResults(args[SYSTEM_OUT]);
    });
}
