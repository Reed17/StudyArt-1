package ua.artcode.model.response;

/**
 * Created by v21k on 15.04.17.
 */
public class GeneralResponse {
    private ResponseType type;
    private String message;

    public GeneralResponse() {
    }

    public GeneralResponse(ResponseType type, String message) {
        this.type = type;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ResponseType getType() {
        return type;
    }

    public void setType(ResponseType type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "GeneralResponse{" +
                "type=" + type +
                ", message='" + message + '\'' +
                '}';
    }
}
