package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;

/* loaded from: jsse.jar:sun/security/ssl/SSLConsumer.class */
interface SSLConsumer {
    void consume(ConnectionContext connectionContext, ByteBuffer byteBuffer) throws IOException;
}
