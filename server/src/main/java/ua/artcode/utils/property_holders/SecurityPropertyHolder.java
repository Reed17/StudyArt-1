package ua.artcode.utils.property_holders;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

/**
 * Created by v21k on 26.06.17.
 */
@Component
@ConfigurationProperties(prefix = "application.security")
public class SecurityPropertyHolder {

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
