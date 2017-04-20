package ua.artcode.model;

/**
 * Created by v21k on 15.04.17.
 */
public class RunResults {
    private int overallTests;
    private int passedTests;
    private int failedTests;
    private String outputInfo;
    private String systemOut;
    private String stats;

    public RunResults() {
    }

    public RunResults(String outputInfo) {
        this.outputInfo = outputInfo;
    }

    public RunResults(String outputInfo, String systemOut) {
        this.outputInfo = outputInfo;
        this.systemOut = systemOut;
    }

    public RunResults(int overallTests, int passedTests, int failedTests, String outputInfo, String systemOut, String stats) {
        this.overallTests = overallTests;
        this.passedTests = passedTests;
        this.failedTests = failedTests;
        this.outputInfo = outputInfo;
        this.systemOut = systemOut;
        this.stats = stats;
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

    public String getOutputInfo() {
        return outputInfo;
    }

    public void setOutputInfo(String outputInfo) {
        this.outputInfo = outputInfo;
    }

    public String getStats() {
        return stats;
    }

    public void setStats(String stats) {
        this.stats = stats;
    }

    public String getSystemOut() {
        return systemOut;
    }

    public void setSystemOut(String systemOut) {
        this.systemOut = systemOut;
    }
}
