package ua.artcode.controller;

import io.swagger.annotations.ApiOperation;
import org.apache.log4j.Logger;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ua.artcode.exceptions.*;
import ua.artcode.model.User;
import ua.artcode.model.dto.ChangeUserInfoDTO;
import ua.artcode.model.dto.RegisterRequestDTO;
import ua.artcode.model.response.GeneralResponse;
import ua.artcode.model.response.ResponseType;
import ua.artcode.model.response.UserResponse;
import ua.artcode.service.UserServiceImpl;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by zhenia on 27.04.17.
 */

@RestController
public class UserController {
    private static final Logger LOGGER = Logger.getLogger(UserController.class);


    @Autowired
    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @ApiOperation(httpMethod = "POST",
            value = "Resource to register new user",
            response = UserResponse.class,
            produces = "application/json")
    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public UserResponse registerUser(@RequestBody RegisterRequestDTO dto) throws AppException {
        User newUser = userService.register(dto.login, dto.pass, dto.email, dto.type);

        LOGGER.info("Registration - OK, id = " + newUser.getId());

        return new UserResponse(newUser);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Resource to activate user account",
            response = Boolean.class,
            produces = "application/json")
    @RequestMapping(value = "/activate", method = RequestMethod.GET)
    public Boolean activateUser(@RequestParam int id) throws UserNotFoundException {
        boolean isActivated = userService.activate(id);

        LOGGER.info("Activation - " + (isActivated ? "OK" : "FAIL") + ", id = " + id);

        return isActivated;
    }

    @ApiOperation(httpMethod = "GET",
            value = "Get user by name",
            response = UserResponse.class,
            produces = "application/json")
    @RequestMapping(value = "/findByUsername", method = RequestMethod.GET)
    public UserResponse getUserByAccessKey(@RequestParam String username) throws InvalidUserLoginException {
        User found = userService.findByUserName(username);
        found.setPassword(null);

        LOGGER.info("User find by username - OK, id = " + found.getId());

        return new UserResponse(found);
    }

    @ApiOperation(httpMethod = "GET",
            value = "Subscribe to course",
            produces = "application/json")
    @RequestMapping(value = "/subscribe", method = RequestMethod.GET)
    public GeneralResponse subscribeToCourse(@RequestParam int courseId, HttpServletRequest req)
            throws GitAPIException, DirectoryCreatingException,
                CourseNotFoundException, InvalidUserAccessTockenException {

        Integer userId = (Integer) req.getAttribute("User");
        if (userId == null) {
            throw new InvalidUserAccessTockenException("Invalid access token");
        }

        boolean result = userService.subscribe(courseId, userId);

        LOGGER.info(String.format("Subscribe to course - %s, userId %d, courseId %d",
                result ? "OK" : "FAILED",
                userId,
                courseId));

        return new GeneralResponse(result ? ResponseType.INFO : ResponseType.ERROR, null);
    }

    @ApiOperation(httpMethod = "POST",
            value = "Change password/email",
            produces = "application/json")
    @RequestMapping(value = "/user/change-personal-info", method = RequestMethod.POST)
    public boolean changePersonalInfo(@RequestBody ChangeUserInfoDTO userInfoDTO) {
        boolean result = userService.changePersonalInfo(userInfoDTO.getOldPass(),
                userInfoDTO.getNewPass(),
                userInfoDTO.getEmail(),
                userInfoDTO.getUserId(),
                userInfoDTO.getUserType());

        LOGGER.info(String.format("Change personal info - %s", result ? "OK" : "FAILED"));

        return result;
    }

    @ApiOperation(httpMethod = "GET",
            value = "Delete an account",
            produces = "application/json")
    @RequestMapping(value = "/user/delete", method = RequestMethod.GET)
    public GeneralResponse deleteAccount(@RequestParam int userId) {
        userService.deleteAccount(userId);

        LOGGER.info(String.format("Delete user with ID %d - OK", userId));

        return new GeneralResponse(ResponseType.INFO, "Account deleted.");
    }
}
