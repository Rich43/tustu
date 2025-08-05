package javax.obex;

import java.io.IOException;
import javax.microedition.io.Connection;

/* loaded from: bluecove-2.1.1.jar:javax/obex/ClientSession.class */
public interface ClientSession extends Connection {
    void setAuthenticator(Authenticator authenticator);

    HeaderSet createHeaderSet();

    void setConnectionID(long j2);

    long getConnectionID();

    HeaderSet connect(HeaderSet headerSet) throws IOException;

    HeaderSet disconnect(HeaderSet headerSet) throws IOException;

    HeaderSet setPath(HeaderSet headerSet, boolean z2, boolean z3) throws IOException;

    HeaderSet delete(HeaderSet headerSet) throws IOException;

    Operation get(HeaderSet headerSet) throws IOException;

    Operation put(HeaderSet headerSet) throws IOException;
}
