package ua.artcode.model.dto;

import ua.artcode.enums.UserType;

/**
 * Created by v21k on 30.05.17.
 */
public class ChangeUserInfoDTO {
    private String oldPass;
    private String newPass;
    private String email;
    int userId;
    private UserType userType;

    public ChangeUserInfoDTO() {
    }

    public String getOldPass() {
        return oldPass;
    }

    public void setOldPass(String oldPass) {
        this.oldPass = oldPass;
    }

    public String getNewPass() {
        return newPass;
    }

    public void setNewPass(String newPass) {
        this.newPass = newPass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(UserType userType) {
        this.userType = userType;
    }


    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
