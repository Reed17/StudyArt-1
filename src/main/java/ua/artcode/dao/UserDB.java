package ua.artcode.dao;

import ua.artcode.exceptions.InvalidUserEmailException;
import ua.artcode.exceptions.InvalidUserLoginException;
import ua.artcode.model.User;

/**
 * Created by zhenia on 23.04.17.
 */
public interface UserDB <T extends User> {

    /**
     *
     * @param user - new user for adding to DB
     * @return added user
     */
    T add(T user);

    /**
     * Basic method for searching user in DB by original id
     * @param id - original id for searching in DB
     * @return founded user
     */
    T getUserById(int id);

    /**
     * Basic method for searching user in DB by original login
     * @param login - original login for searching
     * @return founded user
     */
    T getUserByLogin(String login) throws InvalidUserLoginException;

    /**
     * Basic method for searching user in DB by original email
     * @param email - original email for searching
     * @return founded user
     */
    T getUserByEmail(String email) throws InvalidUserEmailException;

    /**
     * Method for checking does user exist
     * @param id - original id
     * @return is user with that id in DB already
     */
    boolean contains(int id);

    /**
     * Method for checking does user exist
     * @param login - original login
     * @return is user with that login in DB already
     */
    boolean containsLogin(String login);

    /**
     * Method for checking does user exist
     * @param email - original email
     * @return is user with that email in DB already
     */
    boolean containsEmail(String email);
}
