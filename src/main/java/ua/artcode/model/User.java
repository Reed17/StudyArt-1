package ua.artcode.model;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by zhenia on 23.04.17.
 */
public class User {
    private static AtomicInteger NEXT_USER_ID = new AtomicInteger();
    protected int id;
    protected String login;
    protected String pass;
    protected String email;
    protected boolean isActivated;

    public User() {
    }

    public User(String login, String pass, String email) {
        this.id = NEXT_USER_ID.incrementAndGet();
        this.login = login;
        this.pass = pass;
        this.email = email;
        this.isActivated = false;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLogin() {
        return login;
    }

    public String getPass() {
        return pass;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void activate() {
        isActivated = true;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        User user = (User) o;

        if (id != user.id) return false;
        if (login != null ? !login.equals(user.login) : user.login != null) return false;
        if (pass != null ? !pass.equals(user.pass) : user.pass != null) return false;
        return email != null ? email.equals(user.email) : user.email == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (pass != null ? pass.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        return result;
    }
}
