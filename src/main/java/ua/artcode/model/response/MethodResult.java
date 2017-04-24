package ua.artcode.model.response;

/**
 * Created by v21k on 21.04.17.
 */
public class MethodResult {
    String systemOut;
    String systemErr;

    public MethodResult() {
    }

    public MethodResult(String systemOut, String systemErr) {
        this.systemOut = systemOut;
        this.systemErr = systemErr;
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


    @Override
    public String toString() {
        return "MethodResult{" +
                "systemOut='" + systemOut + '\'' +
                ", systemErr='" + systemErr + '\'' +
                '}';
    }
}
