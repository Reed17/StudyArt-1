package ua.artcode.utils;

import ua.artcode.model.CheckResult;
import ua.artcode.model.GeneralResponse;

import java.util.List;

/**
 * Created by v21k on 13.04.17.
 */
public class StatsUtils {
    /**
     * Parsing info from input string with results
     *
     * @param testsInfo - information abouts tests in form List(String), where each String like:
     *                  Result: true/false, expected: ..., actual: ...
     * @return CheckResult model with parsed information
     * @see CheckResult
     */
    public static CheckResult stats(List<String> testsInfo) {
        int overallTests = testsInfo.size();
        int failedTests = 0;

        for (String test : testsInfo) {
            failedTests = test.contains("false") ? failedTests + 1 : failedTests;
        }

        int passedTests = overallTests - failedTests;

        String testsStats = String.format("Tests overall: %d, passed: %d, failed: %d\n",
                overallTests,
                passedTests,
                failedTests);

        GeneralResponse result = failedTests == 0 ? new GeneralResponse("PASSED") : new GeneralResponse("FAILED");


        return new CheckResult(overallTests, passedTests, failedTests, testsInfo, testsStats, result);
    }

}
