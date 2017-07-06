package ua.artcode.service;

import ua.artcode.enums.UserType;
import ua.artcode.exceptions.InvalidUserEmailException;
import ua.artcode.exceptions.InvalidUserLoginException;
import ua.artcode.exceptions.InvalidUserPassException;
import ua.artcode.exceptions.UserNotFoundException;
import ua.artcode.model.User;

/**
 * Created by zhenia on 23.04.17.
 */
public interface UserService {

    /**
     * method for creation new user with data validation
     * and message to email for account activation
     *
     * @param login - user username
     * @param pass  - user password
     * @param email - user email
     * @return new User with inputed fields
     */
    User register(String login, String pass, String email, UserType type)
            throws InvalidUserLoginException, InvalidUserEmailException, InvalidUserPassException;


    /**
     * method for accepting email
     *
     * @param userId - id of user for activation
     * @return activated user
     */
    User activate(int userId) throws UserNotFoundException;

    User findByUserName(String userName) throws InvalidUserLoginException;

    boolean subscribe(int courseId, int userId);

    boolean changePersonalInfo(String oldPass, String newPass, String email, int id, UserType userType) throws InvalidUserPassException;

    void deleteAccount(int userId);
}
