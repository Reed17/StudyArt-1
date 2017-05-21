package ua.artcode.model.response;

/**
 * Created by v21k on 15.04.17.
 */
public class RunResults {
    private MethodResult methodResult;
    private MethodStats methodStats;
    private GeneralResponse generalResponse;

    public RunResults() {
    }

    public RunResults(MethodResult methodResult) {
        this.methodResult = methodResult;
    }

    public RunResults(GeneralResponse generalResponse) {
        this.generalResponse = generalResponse;
    }

    public RunResults(MethodResult methodResult, MethodStats methodStats) {
        this.methodResult = methodResult;
        this.methodStats = methodStats;
    }

    public RunResults(MethodResult methodResult, MethodStats methodStats, GeneralResponse generalResponse) {
        this.methodResult = methodResult;
        this.methodStats = methodStats;
        this.generalResponse = generalResponse;
    }

    public MethodResult getMethodResult() {
        return methodResult;
    }

    public void setMethodResult(MethodResult methodResult) {
        this.methodResult = methodResult;
    }

    public MethodStats getMethodStats() {
        return methodStats;
    }

    public void setMethodStats(MethodStats methodStats) {
        this.methodStats = methodStats;
    }

    public GeneralResponse getGeneralResponse() {
        return generalResponse;
    }

    public void setGeneralResponse(GeneralResponse generalResponse) {
        this.generalResponse = generalResponse;
    }
}
