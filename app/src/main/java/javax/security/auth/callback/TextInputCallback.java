package javax.security.auth.callback;

import java.io.Serializable;

/* loaded from: rt.jar:javax/security/auth/callback/TextInputCallback.class */
public class TextInputCallback implements Callback, Serializable {
    private static final long serialVersionUID = -8064222478852811804L;
    private String prompt;
    private String defaultText;
    private String inputText;

    public TextInputCallback(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.prompt = str;
    }

    public TextInputCallback(String str, String str2) {
        if (str == null || str.length() == 0 || str2 == null || str2.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.prompt = str;
        this.defaultText = str2;
    }

    public String getPrompt() {
        return this.prompt;
    }

    public String getDefaultText() {
        return this.defaultText;
    }

    public void setText(String str) {
        this.inputText = str;
    }

    public String getText() {
        return this.inputText;
    }
}
