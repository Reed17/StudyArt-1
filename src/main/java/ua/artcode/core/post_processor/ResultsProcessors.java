package ua.artcode.core.post_processor;

import ua.artcode.model.RunResults;

public class ResultsProcessors {
    public static MethodResultsProcessor main = ((exceptions, sysOut, methodOutput) ->
            exceptions != null && exceptions.length() > 0 ?
            new RunResults(exceptions) : new RunResults(methodOutput, sysOut));
}
