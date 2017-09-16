package ua.artcode.model;


import org.apache.commons.validator.routines.*;

import java.io.Serializable;

/**
 * Created by zhenia on 13.09.17.
 */
public class UserCourseCopy implements Serializable {
    private static final UrlValidator validator = new UrlValidator();

    private String git;
    private String path;

    public UserCourseCopy(String source) {
        if (validator.isValid(source)) {
            this.git = source;
        } else {
            this.path = source;
        }
    }

    public String getGit() {
        return git;
    }

    public void setGit(String git) {
        this.git = git;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getSource() {
        return ((git != null) && (!git.isEmpty())) ? git : path;
    }

    public void setSource(String source) {
        if (validator.isValid(source)) {
            this.git = source;
        } else {
            this.path = source;
        }
    }

    @Override
    public String toString() {
        return "UserCourseCopy{" +
                "git='" + git + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
