package ua.artcode.controller;

import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.artcode.dao.repositories.SessionRepository;
import ua.artcode.exceptions.UnexpectedNullException;
import ua.artcode.model.User;
import ua.artcode.model.dto.LoginRequestDTO;
import ua.artcode.model.dto.RegisterRequestDTO;
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
    public User registerUser(@RequestBody RegisterRequestDTO dto) throws Throwable {
        User newUser = userService.register(dto.login, dto.pass, dto.email, dto.type);

        LOGGER.info("Registration - OK, id = " + newUser.getId());


        return newUser;
    }

    @ApiOperation(httpMethod = "POST",
            value = "Resource to register new user",
            response = User.class,
            produces = "text/plain")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    // todo usecure passing of params
    // return general response
    // see how to throw an exception to client, ExceptionHandler
    public String loginUser(@RequestBody LoginRequestDTO loginRequestDTO,
                            HttpServletRequest request) throws Throwable {
        String newUserKey = userService.login(loginRequestDTO.login,
                loginRequestDTO.password);

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
