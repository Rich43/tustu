package javax.microedition.io;

import java.io.IOException;

/* loaded from: bluecove-2.1.1.jar:javax/microedition/io/ConnectionNotFoundException.class */
public class ConnectionNotFoundException extends IOException {
    private static final long serialVersionUID = 1;

    public ConnectionNotFoundException() {
    }

    public ConnectionNotFoundException(String msg) {
        super(msg);
    }
}
