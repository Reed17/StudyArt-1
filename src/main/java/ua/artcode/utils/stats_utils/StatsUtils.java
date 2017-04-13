package ua.artcode.utils.stats_utils;

import org.apache.commons.lang3.StringUtils;
import ua.artcode.model.CheckResult;
import ua.artcode.model.GeneralResponse;

/**
 * Created by v21k on 13.04.17.
 */
public class StatsUtils {
    public static CheckResult stats(String testsInfo) {
        int overallTests = StringUtils.countMatches(testsInfo, "Result");
        int failedTests = StringUtils.countMatches(testsInfo, "false");
        int passedTests = overallTests - failedTests;

        String testsStats = String.format("Tests overall: %d, passed: %d, failed: %d\n%s",
                overallTests,
                passedTests,
                failedTests,
                failedTests == 0 ? GeneralResponse.DONE : GeneralResponse.FAILED);


        return new CheckResult(overallTests, passedTests, failedTests, testsInfo, testsStats);
    }

}
