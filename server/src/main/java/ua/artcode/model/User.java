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
    protected String username;
    protected String password;
    @Column(unique = true)
    protected String email;
    protected boolean isActivated;
    protected UserType userType;

    public User() {
    }

    public User(String login, String password, String email, UserType userType) {
        this.username = login;
        this.password = password;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public boolean isAccessible(String reqUrl) {
        return false;
    }
}
