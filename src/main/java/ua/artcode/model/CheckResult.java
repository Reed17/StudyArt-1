package ua.artcode.model;

import java.util.Arrays;
import java.util.List;

/**
 * Created by v21k on 13.04.17.
 */
public class CheckResult {
    private int overallTests;
    private int passedTests;
    private int failedTests;
    private List<String> testsInfo;
    private String testsStats;
    private GeneralResponse result;

    public CheckResult() {
    }

    public CheckResult(GeneralResponse result) {
        this.result = result;
    }

    public CheckResult(int overallTests, int passedTests, int failedTests, List<String> testsInfo, String testsStats, GeneralResponse result) {
        this.overallTests = overallTests;
        this.passedTests = passedTests;
        this.failedTests = failedTests;
        this.testsInfo = testsInfo;
        this.testsStats = testsStats;
        this.result = result;
    }

    public int getOverallTests() {
        return overallTests;
    }

    public void setOverallTests(int overallTests) {
        this.overallTests = overallTests;
    }

    public int getPassedTests() {
        return passedTests;
    }

    public void setPassedTests(int passedTests) {
        this.passedTests = passedTests;
    }

    public int getFailedTests() {
        return failedTests;
    }

    public void setFailedTests(int failedTests) {
        this.failedTests = failedTests;
    }

    public List<String>  getTestsInfo() {
        return testsInfo;
    }

    public void setTestsInfo(List<String>  testsInfo) {
        this.testsInfo = testsInfo;
    }

    public String getTestsStats() {
        return testsStats;
    }

    public void setTestsStats(String testsStats) {
        this.testsStats = testsStats;
    }

    public GeneralResponse getResult() {
        return result;
    }

    public void setResult(GeneralResponse result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "CheckResult{" +
                "overallTests=" + overallTests +
                ", passedTests=" + passedTests +
                ", failedTests=" + failedTests +
                ", testsInfo=" + testsInfo +
                ", testsStats='" + testsStats + '\'' +
                ", result=" + result +
                '}';
    }
}
