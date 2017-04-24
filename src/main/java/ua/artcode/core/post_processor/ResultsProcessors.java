package ua.artcode.core.post_processor;

import ua.artcode.model.response.GeneralResponse;
import ua.artcode.model.response.MethodResult;
import ua.artcode.model.response.RunResults;

public class ResultsProcessors {

    public static MethodResultsProcessor main = (((stats, systemErr, systemOut) -> new RunResults(
            new MethodResult(systemOut, systemErr),
            stats,
            new GeneralResponse(stats != null && stats.getFailedTests() > 0 ? "FAILED" : "OK"))));
}
