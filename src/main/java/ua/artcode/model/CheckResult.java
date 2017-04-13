package ua.artcode.model;

/**
 * Created by v21k on 13.04.17.
 */
public class CheckResult {
    private int overallTests;
    private int passedTests;
    private int failedTests;
    private String testsInfo;
    private String testsStats;

    public CheckResult() {
    }

    public CheckResult(int overallTests, int passedTests, int failedTests, String testsInfo, String testsStats) {
        this.overallTests = overallTests;
        this.passedTests = passedTests;
        this.failedTests = failedTests;
        this.testsInfo = testsInfo;
        this.testsStats = testsStats;
    }

    public CheckResult(GeneralResponse testsInfo) {
        this.testsInfo = String.valueOf(testsInfo);
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

    public String getTestsInfo() {
        return testsInfo;
    }

    public void setTestsInfo(String testsInfo) {
        this.testsInfo = testsInfo;
    }

    public String getTestsStats() {
        return testsStats;
    }

    public void setTestsStats(String testsStats) {
        this.testsStats = testsStats;
    }

    @Override
    public String toString() {
        return "CheckResult{" +
                "overallTests=" + overallTests +
                ", passedTests=" + passedTests +
                ", failedTests=" + failedTests +
                ", testsInfo='" + testsInfo + '\'' +
                ", testsStats='" + testsStats + '\'' +
                '}';
    }
}
