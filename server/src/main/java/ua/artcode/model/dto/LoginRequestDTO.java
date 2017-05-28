package ua.artcode.model.dto;

/**
 * Created by Алексей on 28.05.2017.
 */
public class LoginRequestDTO {
    public String login;
    public String password;

    public LoginRequestDTO(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public LoginRequestDTO() {
    }


}
