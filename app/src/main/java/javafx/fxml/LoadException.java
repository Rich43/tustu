package javafx.fxml;

import java.io.IOException;

/* loaded from: jfxrt.jar:javafx/fxml/LoadException.class */
public class LoadException extends IOException {
    private static final long serialVersionUID = 0;

    public LoadException() {
    }

    public LoadException(String message) {
        super(message);
    }

    public LoadException(Throwable cause) {
        super(cause);
    }

    public LoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
