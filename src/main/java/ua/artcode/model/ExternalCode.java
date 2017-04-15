package ua.artcode.model;

/**
 * Created by v21k on 15.04.17.
 */
public class ExternalCode {
    private String sourceCode;

    public ExternalCode() {
    }

    public ExternalCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }


    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
}
