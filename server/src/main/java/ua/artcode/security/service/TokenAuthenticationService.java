package ua.artcode.security.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import ua.artcode.exceptions.InvalidUserLoginException;
import ua.artcode.model.User;
import ua.artcode.service.UserService;
import ua.artcode.utils.AppPropertyHolder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;

/**
 * Created by v21k on 07.06.17.
 */
@Component
public class TokenAuthenticationService {

    private final AppPropertyHolder.Security security;
    private final UserService userService;

    @Autowired
    public TokenAuthenticationService(UserService userService, AppPropertyHolder appPropertyHolder) {
        this.userService = userService;
        this.security = appPropertyHolder.getSecurity();
    }

    public void addAuthentication(HttpServletResponse res, String username) {
        String JWT = Jwts.builder()
                .setSubject(username)
                .setExpiration(new Date(System.currentTimeMillis() + security.getExpirationTime()))
                .signWith(SignatureAlgorithm.HS512, security.getSecret())
                .compact();
        res.addHeader(security.getHeaderString(), security.getTokenPrefix() + " " + JWT);
    }

    public Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(security.getHeaderString());
        if (token != null) {
            // parse the token.
            String user = Jwts.parser()
                    .setSigningKey(security.getSecret())
                    .parseClaimsJws(token.replace(security.getTokenPrefix(), ""))
                    .getBody()
                    .getSubject();

            User appUser;

            try {
                appUser = userService.findByUserName(user);
                request.setAttribute("User", appUser.getId());
            } catch (InvalidUserLoginException e) {
                return null;
            }

            return user != null ?
                    new UsernamePasswordAuthenticationToken(user,
                            null,
                            new HashSet<>(Collections.singletonList(
                                    new SimpleGrantedAuthority("ROLE_" + appUser.getUserType())))) :
                    null;
        }
        return null;
    }

}
