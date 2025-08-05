package sun.security.ssl;

import java.io.IOException;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/HandshakeConsumer.class */
interface HandshakeConsumer {
    void consume(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException;
}
