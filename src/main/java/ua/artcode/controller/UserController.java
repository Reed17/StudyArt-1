package ua.artcode.controller;

import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.artcode.exceptions.*;
import ua.artcode.model.Course;
import ua.artcode.model.User;
import ua.artcode.service.StudentService;
import ua.artcode.service.TeacherService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by zhenia on 27.04.17.
 */

@RestController
public class UserController {
    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;

    @ApiOperation(httpMethod = "POST",
            value = "Resource to register new user",
            response = User.class,
            produces = "application/json")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public User registerUser(@RequestParam String login, @RequestParam String email, @RequestParam String pass, @RequestParam String type,
                             HttpServletRequest request, HttpServletResponse response) {
        User newUser = null;

        try {
            if(type.toLowerCase().equals("teacher")) {
                newUser = teacherService.register(login, pass, email);
            } else {
                newUser = studentService.register(login, pass, email);
            }

            LOGGER.info("Registration - OK, id = " + newUser.getId());
        } catch (Throwable e) {
            LOGGER.warn("Registration - FAILED, msg = " + e.getMessage());
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

            try {
                response.getWriter().write(e.getMessage());
                response.getWriter().flush();
                response.getWriter().close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }

        return newUser;
    }


    @ApiOperation(httpMethod = "GET",
            value = "Resource to activate user account",
            response = User.class,
            produces = "application/json")
    @RequestMapping(value = "/activate", method = RequestMethod.GET)
    public User activateUser(@RequestParam int id) {
        User activatedUser = teacherService.activate(id);

        if(activatedUser == null) activatedUser = studentService.activate(id);

        LOGGER.info("Activation - OK, id = " + activatedUser.getId());

        return activatedUser;
    }
}
