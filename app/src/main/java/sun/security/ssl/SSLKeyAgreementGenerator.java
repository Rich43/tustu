package sun.security.ssl;

import java.io.IOException;

/* loaded from: jsse.jar:sun/security/ssl/SSLKeyAgreementGenerator.class */
interface SSLKeyAgreementGenerator {
    SSLKeyDerivation createKeyDerivation(HandshakeContext handshakeContext) throws IOException;
}
