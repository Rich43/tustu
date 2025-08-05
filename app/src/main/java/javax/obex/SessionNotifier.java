package javax.obex;

import java.io.IOException;
import javax.microedition.io.Connection;

/* loaded from: bluecove-2.1.1.jar:javax/obex/SessionNotifier.class */
public interface SessionNotifier extends Connection {
    Connection acceptAndOpen(ServerRequestHandler serverRequestHandler) throws IOException;

    Connection acceptAndOpen(ServerRequestHandler serverRequestHandler, Authenticator authenticator) throws IOException;
}
