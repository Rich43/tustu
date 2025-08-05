package java.net;

/* loaded from: rt.jar:java/net/PasswordAuthentication.class */
public final class PasswordAuthentication {
    private String userName;
    private char[] password;

    public PasswordAuthentication(String str, char[] cArr) {
        this.userName = str;
        this.password = (char[]) cArr.clone();
    }

    public String getUserName() {
        return this.userName;
    }

    public char[] getPassword() {
        return this.password;
    }
}
