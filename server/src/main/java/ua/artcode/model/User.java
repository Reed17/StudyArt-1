package ua.artcode.model;

import javax.persistence.*;

/**
 * Created by zhenia on 23.04.17.
 */
@Entity
@Inheritance
public class User {
    @Id
    @GeneratedValue
    protected int id;
    @Column (unique = true)
    protected String login;
    protected String pass;
    @Column (unique = true)
    protected String email;
    protected boolean isActivated;

    public User() {
    }

    public User(String login, String pass, String email) {
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

    public boolean isAccessable(String reqUrl) { return false; }
}
