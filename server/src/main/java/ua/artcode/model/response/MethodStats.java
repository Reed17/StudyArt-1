package ua.artcode.model.response;

import org.junit.runner.notification.Failure;

import java.util.List;

/**
 * Created by v21k on 21.04.17.
 */
public class MethodStats {
    private int overallTests;
    private int passedTests;
    private int failedTests;
    private List<Failure> failures;

    public MethodStats() {
    }

    public MethodStats(int overallTests, int passedTests, int failedTests, List<Failure> failures) {
        this.overallTests = overallTests;
        this.passedTests = passedTests;
        this.failedTests = failedTests;
        this.failures = failures;
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

    public List<Failure> getFailures() {
        return failures;
    }

    public void setFailures(List<Failure> failures) {
        this.failures = failures;
    }

    @Override
    public String toString() {
        return String.format("Overall tests: %d, passed: %d, failed :%d\nFailed tests info: %s\n",
                overallTests,
                passedTests,
                failedTests,
                failures.toString());
    }
}
