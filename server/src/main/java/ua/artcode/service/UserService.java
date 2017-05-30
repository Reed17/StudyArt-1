package ua.artcode.service;

import ua.artcode.enums.UserType;
import ua.artcode.exceptions.*;
import ua.artcode.model.User;

/**
 * Created by zhenia on 23.04.17.
 */
public interface UserService<T extends User> {

    /**
     * method for creation new user with data validation
     * and message to email for account activation
     *
     * @param login - user login
     * @param pass  - user pass
     * @param email - user email
     * @return new User with inputed fields
     */
    T register(String login, String pass, String email, UserType type)
            throws InvalidUserLoginException, InvalidUserEmailException, InvalidUserPassException;


    /**
     * method for accepting email
     *
     * @param userId - id of user for activation
     * @return activated user
     */
    T activate(int userId) throws UnexpectedNullException;

    String login(String login, String pass) throws InvalidLoginInfo;

    T find(String accessKey) throws InvalidUserSessionException;

    boolean subscribe(int courseId, int userId);
}
