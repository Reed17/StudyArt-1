package ua.artcode.dao;

import org.springframework.stereotype.Component;
import ua.artcode.exceptions.InvalidUserEmailException;
import ua.artcode.exceptions.InvalidUserLoginException;
import ua.artcode.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhenia on 23.04.17.
 */
@Component
public class UserDBImpl <T extends User> implements UserDB <T> {
    private Map<Integer, T> users;

    public UserDBImpl() {
        users = new ConcurrentHashMap<Integer, T>();
    }

    @Override
    public T add(T user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public T getUserById(int id) {
        return users.get(id);
    }

    @Override
    public T getUserByLogin(String login) throws InvalidUserLoginException {

        return users.values()
                .stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst().orElseThrow(() -> new InvalidUserLoginException("No users with that login"));
    }

    @Override
    public T getUserByEmail(String email) throws InvalidUserEmailException {

        return users.values()
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst().orElseThrow(() -> new InvalidUserEmailException("No users with that email"));
    }

    @Override
    public boolean contains(int id) {
        return users.containsKey(id);
    }

    @Override
    public boolean containsLogin(String login) {
        return users.values()
                .stream()
                .anyMatch(u -> u.getLogin().equals(login));
    }

    @Override
    public boolean containsEmail(String email) {
        return users.values()
                .stream()
                .anyMatch(u -> u.getEmail().equals(email));
    }
}
