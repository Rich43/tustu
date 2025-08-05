package javax.security.auth.callback;

import java.io.Serializable;

/* loaded from: rt.jar:javax/security/auth/callback/TextOutputCallback.class */
public class TextOutputCallback implements Callback, Serializable {
    private static final long serialVersionUID = 1689502495511663102L;
    public static final int INFORMATION = 0;
    public static final int WARNING = 1;
    public static final int ERROR = 2;
    private int messageType;
    private String message;

    public TextOutputCallback(int i2, String str) {
        if ((i2 != 0 && i2 != 1 && i2 != 2) || str == null || str.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.messageType = i2;
        this.message = str;
    }

    public int getMessageType() {
        return this.messageType;
    }

    public String getMessage() {
        return this.message;
    }
}
