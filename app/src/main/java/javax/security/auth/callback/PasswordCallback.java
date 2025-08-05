package javax.security.auth.callback;

import java.io.Serializable;

/* loaded from: rt.jar:javax/security/auth/callback/PasswordCallback.class */
public class PasswordCallback implements Callback, Serializable {
    private static final long serialVersionUID = 2267422647454909926L;
    private String prompt;
    private boolean echoOn;
    private char[] inputPassword;

    public PasswordCallback(String str, boolean z2) {
        if (str == null || str.length() == 0) {
            throw new IllegalArgumentException();
        }
        this.prompt = str;
        this.echoOn = z2;
    }

    public String getPrompt() {
        return this.prompt;
    }

    public boolean isEchoOn() {
        return this.echoOn;
    }

    public void setPassword(char[] cArr) {
        this.inputPassword = cArr == null ? null : (char[]) cArr.clone();
    }

    public char[] getPassword() {
        if (this.inputPassword == null) {
            return null;
        }
        return (char[]) this.inputPassword.clone();
    }

    public void clearPassword() {
        if (this.inputPassword != null) {
            for (int i2 = 0; i2 < this.inputPassword.length; i2++) {
                this.inputPassword[i2] = ' ';
            }
        }
    }
}
