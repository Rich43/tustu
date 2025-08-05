package sun.security.ssl;

import javax.net.ssl.SSLEngineResult;

/* loaded from: jsse.jar:sun/security/ssl/Ciphertext.class */
final class Ciphertext {
    final byte contentType;
    final byte handshakeType;
    final long recordSN;
    SSLEngineResult.HandshakeStatus handshakeStatus;

    private Ciphertext() {
        this.contentType = (byte) 0;
        this.handshakeType = (byte) -1;
        this.recordSN = -1L;
        this.handshakeStatus = null;
    }

    Ciphertext(byte b2, byte b3, long j2) {
        this.contentType = b2;
        this.handshakeType = b3;
        this.recordSN = j2;
        this.handshakeStatus = null;
    }
}
