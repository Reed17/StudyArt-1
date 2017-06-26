package ua.artcode.security.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import ua.artcode.model.dto.LoginRequestDTO;
import ua.artcode.security.service.TokenAuthenticationService;
import ua.artcode.utils.AppPropertyHolder;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * Created by v21k on 07.06.17.
 */
public class JWTLoginFilter extends AbstractAuthenticationProcessingFilter {
    private final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private final String ACCESS_CONTROL_EXPOSE_HEADERS = "Access-Control-Expose-Headers";
    private final String ACCESS_CONTROL_ALLOW_CREDENTIALS = "Access-Control-Allow-Credentials";
    private final TokenAuthenticationService tokenAuthenticationService;

    private final AppPropertyHolder.Security security;

    public JWTLoginFilter(String url, AuthenticationManager authManager, TokenAuthenticationService tokenAuthenticationService, AppPropertyHolder appPropertyHolder) {
        super(new AntPathRequestMatcher(url));
        this.tokenAuthenticationService = tokenAuthenticationService;
        setAuthenticationManager(authManager);
        this.security = appPropertyHolder.getSecurity();
    }

    @Override
    public Authentication attemptAuthentication(
            HttpServletRequest req, HttpServletResponse res)
            throws AuthenticationException, IOException, ServletException {


        LoginRequestDTO creds = new ObjectMapper()
                .readValue(req.getInputStream(), LoginRequestDTO.class);

        res.setHeader(ACCESS_CONTROL_ALLOW_ORIGIN, security.getResponseHeaders().getAccessControlAllowOrigin());
        res.setHeader(ACCESS_CONTROL_ALLOW_CREDENTIALS, security.getResponseHeaders().getAccessControlAllowCredentials());
        res.setHeader(ACCESS_CONTROL_EXPOSE_HEADERS, security.getResponseHeaders().getAccessControlExposeHeaders());

        return getAuthenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(
                        creds.getUsername(),
                        creds.getPassword(),
                        Collections.emptyList())
        );
    }

    @Override
    protected void successfulAuthentication(
            HttpServletRequest req,
            HttpServletResponse res, FilterChain chain,
            Authentication auth) throws IOException, ServletException {
        tokenAuthenticationService
                .addAuthentication(res, auth.getName());
    }
}