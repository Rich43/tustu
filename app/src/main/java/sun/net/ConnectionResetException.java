package sun.net;

import java.net.SocketException;

/* loaded from: rt.jar:sun/net/ConnectionResetException.class */
public class ConnectionResetException extends SocketException {
    private static final long serialVersionUID = -7633185991801851556L;

    public ConnectionResetException(String str) {
        super(str);
    }

    public ConnectionResetException() {
    }
}
