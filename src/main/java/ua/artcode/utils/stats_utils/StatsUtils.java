package ua.artcode.utils.stats_utils;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by v21k on 13.04.17.
 */
public class StatsUtils {
    public static String stats(String result){
        int overallTests = StringUtils.countMatches(result, "Result");
        int failedTests = StringUtils.countMatches(result, "false");
        int passedTests = overallTests - failedTests;

        String testsStats = String.format("Tests overall: %d, passed: %d, failed: %d", overallTests, passedTests, failedTests);

        return String.format("Tests results:\n%s\nTests stats:\n%s\n%s",
                result,
                testsStats,
                testsStats.trim().endsWith("0") ? "---OK---" : "---FAIL---");
    }

}
