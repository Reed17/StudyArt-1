package ua.artcode.model.response;

/**
 * Created by v21k on 15.04.17.
 */
public class GeneralResponse {
    private String message;

    public GeneralResponse() {
    }

    public GeneralResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "GeneralResponse{" +
                "message='" + message + '\'' +
                '}';
    }
}
