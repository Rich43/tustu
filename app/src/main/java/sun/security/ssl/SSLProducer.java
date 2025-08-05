package sun.security.ssl;

import java.io.IOException;

/* loaded from: jsse.jar:sun/security/ssl/SSLProducer.class */
interface SSLProducer {
    byte[] produce(ConnectionContext connectionContext) throws IOException;
}
