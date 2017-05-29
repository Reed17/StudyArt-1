package ua.artcode.model.dto;

import ua.artcode.enums.UserType;

/**
 * Created by zhenia on 30.04.17.
 */
public class RegisterRequestDTO {
    public String login;
    public String email;
    public String pass;
    public UserType type;

    public RegisterRequestDTO() {
    }

    public RegisterRequestDTO(String login, String email, String pass, UserType type) {
        this.login = login;
        this.email = email;
        this.pass = pass;
        this.type = type;
    }
}
