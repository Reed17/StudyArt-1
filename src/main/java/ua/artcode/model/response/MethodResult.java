package ua.artcode.model.response;

/**
 * Created by v21k on 21.04.17.
 */
public class MethodResult {
    String systemOut;
    String systemErr;
    String returnedValue;

    public MethodResult() {
    }

    public MethodResult(String systemOut, String systemErr, String returnedValue) {
        this.systemOut = systemOut;
        this.systemErr = systemErr;
        this.returnedValue = returnedValue;
    }

    public String getSystemOut() {
        return systemOut;
    }

    public void setSystemOut(String systemOut) {
        this.systemOut = systemOut;
    }

    public String getSystemErr() {
        return systemErr;
    }

    public void setSystemErr(String systemErr) {
        this.systemErr = systemErr;
    }

    public String getReturnedValue() {
        return returnedValue;
    }

    public void setReturnedValue(String returnedValue) {
        this.returnedValue = returnedValue;
    }

    @Override
    public String toString() {
        return "MethodResult{" +
                "systemOut='" + systemOut + '\'' +
                ", systemErr='" + systemErr + '\'' +
                ", returnedValue='" + returnedValue + '\'' +
                '}';
    }
}