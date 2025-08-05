package java.net;

import java.io.IOException;

/* loaded from: rt.jar:java/net/HttpRetryException.class */
public class HttpRetryException extends IOException {
    private static final long serialVersionUID = -9186022286469111381L;
    private int responseCode;
    private String location;

    public HttpRetryException(String str, int i2) {
        super(str);
        this.responseCode = i2;
    }

    public HttpRetryException(String str, int i2, String str2) {
        super(str);
        this.responseCode = i2;
        this.location = str2;
    }

    public int responseCode() {
        return this.responseCode;
    }

    public String getReason() {
        return super.getMessage();
    }

    public String getLocation() {
        return this.location;
    }
}
