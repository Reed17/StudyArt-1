package ua.artcode.model.response;

import ua.artcode.enums.UserType;
import ua.artcode.model.User;

/**
 * Created by zhenia on 23.09.17.
 */
public class UserResponse {
    private String username;
    private Integer id;
    private UserType userType;
    private Boolean isActivated;
    private String email;

    public UserResponse(String username, Integer id, UserType userType, Boolean isActivated, String email) {
        this.username = username;
        this.id = id;
        this.userType = userType;
        this.isActivated = isActivated;
        this.email = email;
    }

    public UserResponse(User user) {
        this.username = user.getUsername();
        this.id = user.getId();
        this.userType = user.getUserType();
        this.isActivated = user.isActivated();
        this.email = user.getEmail();
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

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }

    public Boolean getActivated() {
        return isActivated;
    }

    public void setActivated(Boolean activated) {
        isActivated = activated;
    }
}
