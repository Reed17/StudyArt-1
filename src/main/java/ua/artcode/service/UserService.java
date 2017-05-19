package ua.artcode.service;

import ua.artcode.exceptions.InvalidLoginInfo;
import ua.artcode.exceptions.InvalidUserEmailException;
import ua.artcode.exceptions.InvalidUserLoginException;
import ua.artcode.exceptions.InvalidUserPassException;
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
    T register(String login, String pass, String email, String type)
            throws InvalidUserLoginException, InvalidUserEmailException, InvalidUserPassException;


    /**
     * method for accepting email
     *
     * @param userId - id of user for activation
     * @return activated user
     */
    T activate(int userId);

    String login(String login, String pass) throws InvalidLoginInfo;
}
