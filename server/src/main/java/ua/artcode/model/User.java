package ua.artcode.model;

import ua.artcode.enums.UserType;

import javax.persistence.*;

/**
 * Created by zhenia on 23.04.17.
 */
@Entity
@Inheritance
public class User {
    @Id
    @GeneratedValue
    @Column(name = "USER_ID")
    protected int id;
    @Column(unique = true)
    protected String login;
    protected String pass;
    @Column(unique = true)
    protected String email;
    protected boolean isActivated;
    protected UserType userType;

    public User() {
    }

    public User(String login, String pass, String email, UserType userType) {
        this.login = login;
        this.pass = pass;
        this.email = email;
        this.userType = userType;
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

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public void setActivated(boolean activated) {
        isActivated = activated;
    }

    public void activate() {
        isActivated = true;
    }

    public int getId() {
        return id;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public boolean isAccessable(String reqUrl) {
        return false;
    }
}
