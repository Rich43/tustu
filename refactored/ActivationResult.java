package az;

/**
 * Result of validating an activation code.
 */
public class ActivationResult {
    public static final String NO_ACTIVATION = "A valid Activation has not been entered.";

    private int code = 2; // default invalid
    private String message = "Invalid activation data.";
    private ActivationData data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ActivationData getData() {
        return data;
    }

    public void setData(ActivationData data) {
        this.data = data;
    }
}
