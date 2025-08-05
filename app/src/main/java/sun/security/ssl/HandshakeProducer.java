package sun.security.ssl;

import java.io.IOException;
import sun.security.ssl.SSLHandshake;

/* loaded from: jsse.jar:sun/security/ssl/HandshakeProducer.class */
interface HandshakeProducer {
    byte[] produce(ConnectionContext connectionContext, SSLHandshake.HandshakeMessage handshakeMessage) throws IOException;
}
