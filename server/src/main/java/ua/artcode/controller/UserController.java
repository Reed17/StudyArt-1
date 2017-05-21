package ua.artcode.controller;

import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ua.artcode.dao.repositories.SessionRepository;
import ua.artcode.exceptions.UnexpectedNullException;
import ua.artcode.model.User;
import ua.artcode.service.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhenia on 27.04.17.
 */

@RestController
public class UserController {
    private static final Logger LOGGER = Logger.getLogger(UserController.class);

//    private final StudentService studentService;
//
//    private final TeacherService teacherService;

    private final UserServiceImpl userService;

    private final SessionRepository sessionDB;

    @Autowired
    public UserController(UserServiceImpl userService, SessionRepository sessionDB) {
        this.userService = userService;
        this.sessionDB = sessionDB;
//        this.teacherService = teacherService;
    }

    @ApiOperation(httpMethod = "POST",
            value = "Resource to register new user",
            response = User.class,
            produces = "application/json")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    // todo usecure passing of params
    // return general response
    // see how to throw an exception to client, ExceptionHandler
    public User registerUser(@RequestParam String login, @RequestParam String email, @RequestParam String pass,
                             @RequestParam String type,
                             HttpServletRequest request) throws Throwable {
        User newUser = userService.register(login, pass, email, type);

        LOGGER.info("Registration - OK, id = " + newUser.getId());

//        try {
//            if (type.toLowerCase().equals("teacher")) {
//                newUser = teacherService.register(login, pass, email);
//            } else {
//                newUser = studentService.register(login, pass, email);
//            }

//            LOGGER.info("Registration - OK, id = " + newUser.getId());
//        } catch (Throwable e) {
//            LOGGER.warn("Registration - FAILED, msg = " + e.getMessage());
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//
//            try {
//                response.getWriter().write(e.getMessage());
//                response.getWriter().flush();
//                response.getWriter().close();
//            } catch (IOException e1) {
//                // todo logger
//                e1.printStackTrace();
//            }
//        }

        return newUser;
    }

    @ApiOperation(httpMethod = "POST",
            value = "Resource to register new user",
            response = User.class,
            produces = "application/json")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    // todo usecure passing of params
    // return general response
    // see how to throw an exception to client, ExceptionHandler
    public String loginUser(@RequestParam String login, @RequestParam String pass,
                             HttpServletRequest request) throws Throwable {
        String newUserKey = userService.login(login, pass);

        LOGGER.info("Login - OK, id = " + sessionDB.findOne(newUserKey).getUser().getId());


        return newUserKey;
    }


    @ApiOperation(httpMethod = "GET",
            value = "Resource to activate user account",
            response = User.class,
            produces = "application/json")
    @RequestMapping(value = "/activate", method = RequestMethod.GET)
    // todo use the id as String
    public User activateUser(@RequestParam int id) throws UnexpectedNullException {
        User activatedUser = userService.activate(id);

        LOGGER.info("Activation - OK, id = " + activatedUser.getId());

        return activatedUser;
    }
}
