/*
package ua.artcode.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ua.artcode.dao.repositories.SessionRepository;
import ua.artcode.exceptions.InvalidUserSessionException;
import ua.artcode.model.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Enumeration;

*/
/**
 * Created by zhenia on 19.05.17.
 *//*

@Aspect
@Component
public class SecurityAspect {

    @Autowired
    private SessionRepository sessionDB;

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityAspect.class);

    @Before("execution(* ua.artcode.controller.CourseController.*(..))")
    public void securityCheck(JoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();

        HttpServletRequest req = (HttpServletRequest) args[args.length - 1];

        LOGGER.info(req.getRequestURI());

        Cookie[] cookies = req.getCookies();

        String sessionKey = Arrays.stream(cookies).filter(c -> c.getName().equals("Access-Key")).findFirst().get().getValue();

        if(sessionKey == null || sessionKey.equals("")) {
            throw new InvalidUserSessionException("No user with this session id");
        }

        User user = sessionDB.findOne(sessionKey).getUser();

        if(user == null) throw new InvalidUserSessionException("No user with this session id");

        if(!user.isAccessable(req.getRequestURI())) throw new SecurityException("No rights for that action");
    }


}
*/
