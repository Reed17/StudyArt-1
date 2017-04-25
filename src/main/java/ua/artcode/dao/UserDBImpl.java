package ua.artcode.dao;

import ua.artcode.exceptions.InvalidUserEmailException;
import ua.artcode.exceptions.InvalidUserLoginException;
import ua.artcode.model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhenia on 23.04.17.
 */
public class UserDBImpl <T extends User> implements UserDB <T> {
    private Map<Integer, T> users;

    public UserDBImpl() {
        users = new ConcurrentHashMap<Integer, T>();
    }

    @Override
    public T add(T user) {
        return users.put(user.getId(), user);
    }

    @Override
    public T getUserById(int id) {
        return users.get(id);
    }

    @Override
    public T getUserByLogin(String login) throws InvalidUserLoginException {
        T searchRes = users.values()
                .stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst().orElse(null);

        if(searchRes == null) throw new InvalidUserLoginException("No users with that login");

        return searchRes;
    }

    @Override
    public T getUserByEmail(String email) throws InvalidUserEmailException {
        T searchRes = users.values()
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst().orElse(null);

        if(searchRes == null) throw new InvalidUserEmailException("No users with that email");

        return searchRes;
    }

    @Override
    public boolean contains(int id) {
        return users.containsKey(id);
    }

    @Override
    public boolean containsLogin(String login) {
        return users.values()
                .stream()
                .filter(u -> u.getLogin().equals(login))
                .findFirst().orElse(null) != null;
    }

    @Override
    public boolean containsEmail(String email) {
        return users.values()
                .stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst().orElse(null) != null;
    }
}
