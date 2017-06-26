package ua.artcode.utils;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * Created by v21k on 26.06.17.
 */
@Component
@ConfigurationProperties(prefix = "application")
public class AppPropertyHolder {
    private String profiling;
    @NestedConfigurationProperty
    private Courses courses;
    @NestedConfigurationProperty
    private Security security;
    @NestedConfigurationProperty
    private Maven maven;

    public String getProfiling() {
        return profiling;
    }

    public void setProfiling(String profiling) {
        this.profiling = profiling;
    }

    public Courses getCourses() {
        return courses;
    }

    public void setCourses(Courses courses) {
        this.courses = courses;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public Maven getMaven() {
        return maven;
    }

    public void setMaven(Maven maven) {
        this.maven = maven;
    }

    public static class Courses {
        @NestedConfigurationProperty
        private Paths paths;

        public Paths getPaths() {
            return paths;
        }

        public void setPaths(Paths paths) {
            this.paths = paths;
        }

        public static class Paths {
            private String git;
            private String externalCode;
            private String dependencies;

            public String getGit() {
                return git;
            }

            public void setGit(String git) {
                this.git = git;
            }

            public String getExternalCode() {
                return externalCode;
            }

            public void setExternalCode(String externalCode) {
                this.externalCode = externalCode;
            }

            public String getDependencies() {
                return dependencies;
            }

            public void setDependencies(String dependencies) {
                this.dependencies = dependencies;
            }
        }
    }

    public static class Security {
        private long expirationTime;
        private String secret;
        private String tokenPrefix;
        private String headerString;

        @NestedConfigurationProperty
        private ResponseHeaders responseHeaders;

        public long getExpirationTime() {
            return expirationTime;
        }

        public void setExpirationTime(long expirationTime) {
            this.expirationTime = expirationTime;
        }

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getTokenPrefix() {
            return tokenPrefix;
        }

        public void setTokenPrefix(String tokenPrefix) {
            this.tokenPrefix = tokenPrefix;
        }

        public String getHeaderString() {
            return headerString;
        }

        public void setHeaderString(String headerString) {
            this.headerString = headerString;
        }

        public ResponseHeaders getResponseHeaders() {
            return responseHeaders;
        }

        public void setResponseHeaders(ResponseHeaders responseHeaders) {
            this.responseHeaders = responseHeaders;
        }

        public static class ResponseHeaders {

            private String accessControlAllowOrigin;
            private String accessControlAllowCredentials;
            private String accessControlExposeHeaders;

            public String getAccessControlAllowOrigin() {
                return accessControlAllowOrigin;
            }

            public void setAccessControlAllowOrigin(String accessControlAllowOrigin) {
                this.accessControlAllowOrigin = accessControlAllowOrigin;
            }

            public String getAccessControlAllowCredentials() {
                return accessControlAllowCredentials;
            }

            public void setAccessControlAllowCredentials(String accessControlAllowCredentials) {
                this.accessControlAllowCredentials = accessControlAllowCredentials;
            }

            public String getAccessControlExposeHeaders() {
                return accessControlExposeHeaders;
            }

            public void setAccessControlExposeHeaders(String accessControlExposeHeaders) {
                this.accessControlExposeHeaders = accessControlExposeHeaders;
            }
        }
    }

    public static class Maven {
        private String dependenciesPath;
        @NestedConfigurationProperty
        private Goals goals;

        public String getDependenciesPath() {
            return dependenciesPath;
        }

        public void setDependenciesPath(String dependenciesPath) {
            this.dependenciesPath = dependenciesPath;
        }

        public Goals getGoals() {
            return goals;
        }

        public void setGoals(Goals goals) {
            this.goals = goals;
        }

        public static class Goals {
            private String copyToDirectory;

            public String getCopyToDirectory() {
                return copyToDirectory;
            }

            public void setCopyToDirectory(String copyToDirectory) {
                this.copyToDirectory = copyToDirectory;
            }
        }
    }
}
