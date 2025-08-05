package sun.security.ssl;

import java.io.IOException;
import javax.crypto.SecretKey;

/* loaded from: jsse.jar:sun/security/ssl/SSLKeyDerivationGenerator.class */
interface SSLKeyDerivationGenerator {
    SSLKeyDerivation createKeyDerivation(HandshakeContext handshakeContext, SecretKey secretKey) throws IOException;
}
