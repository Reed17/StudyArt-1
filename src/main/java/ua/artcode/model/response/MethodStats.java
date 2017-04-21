package ua.artcode.model.response;

/**
 * Created by v21k on 21.04.17.
 */
public class MethodStats {
    private int overallTests;
    private int passedTests;
    private int failedTests;

    public MethodStats() {
    }

    public MethodStats(int overallTests, int passedTests, int failedTests) {
        this.overallTests = overallTests;
        this.passedTests = passedTests;
        this.failedTests = failedTests;
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

    @Override
    public String toString() {
        return "MethodStats{" +
                "overallTests=" + overallTests +
                ", passedTests=" + passedTests +
                ", failedTests=" + failedTests +
                '}';
    }
}
