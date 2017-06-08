package ua.artcode.security.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Enumeration;

import static java.util.Collections.emptyList;

/**
 * Created by v21k on 07.06.17.
 */
@Component
public class TokenAuthenticationService {

    @Value("${application.security.expirationTime}")
    private long expirationTime;
    @Value("${application.security.secret}")
    private String secret;
    @Value("${application.security.tokenPrefix}")
    private String tokenPrefix;
    @Value("${application.security.headerString}")
    private String headerString;

    public void addAuthentication(HttpServletResponse res, String username) {
        String JWT = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
        res.addHeader(headerString, tokenPrefix + " " + JWT);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(headerString);
        Enumeration<String> headerNames = request.getHeaderNames(); // test
        if (token != null) {
            // parse the token.
            String user = Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token.replace(tokenPrefix, ""))
                    .getBody()
                    .getSubject();

            return user != null ?
                    new UsernamePasswordAuthenticationToken(user, null, emptyList()) : // todo authority here
                    null;
        }
        return null;
    }

}
