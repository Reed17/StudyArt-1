package ua.artcode.model.dto;

/**
 * Created by zhenia on 30.04.17.
 */
public class RegisterRequestDTO {
    public String login;
    public String email;
    public String pass;
    public String type;

    public RegisterRequestDTO() {
    }

    public RegisterRequestDTO(String login, String email, String pass, String type) {
        this.login = login;
        this.email = email;
        this.pass = pass;
        this.type = type;
    }
}
