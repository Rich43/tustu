package javax.security.auth.callback;

import java.io.Serializable;

/* loaded from: rt.jar:javax/security/auth/callback/NameCallback.class */
public class NameCallback implements Callback, Serializable {
    private static final long serialVersionUID = 3770938795909392253L;
    private String prompt;
    private String defaultName;
    private String inputName;

    public NameCallback(String str) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.prompt = str;
    }

    public NameCallback(String str, String str2) {
        if (str == null || str.length() == 0 || str2 == null || str2.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.prompt = str;
        this.defaultName = str2;
    }

    public String getPrompt() {
        return this.prompt;
    }

    public String getDefaultName() {
        return this.defaultName;
    }

    public void setName(String str) {
        this.inputName = str;
    }

    public String getName() {
        return this.inputName;
    }
}
