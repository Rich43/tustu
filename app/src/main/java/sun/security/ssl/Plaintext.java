package sun.security.ssl;

import java.nio.ByteBuffer;
import javax.net.ssl.SSLEngineResult;

/* loaded from: jsse.jar:sun/security/ssl/Plaintext.class */
final class Plaintext {
    static final Plaintext PLAINTEXT_NULL = new Plaintext();
    final byte contentType;
    final byte majorVersion;
    final byte minorVersion;
    final int recordEpoch;
    final long recordSN;
    final ByteBuffer fragment;
    SSLEngineResult.HandshakeStatus handshakeStatus;

    private Plaintext() {
        this.contentType = (byte) 0;
        this.majorVersion = (byte) 0;
        this.minorVersion = (byte) 0;
        this.recordEpoch = -1;
        this.recordSN = -1L;
        this.fragment = null;
        this.handshakeStatus = null;
    }

    Plaintext(byte b2, byte b3, byte b4, int i2, long j2, ByteBuffer byteBuffer) {
        this.contentType = b2;
        this.majorVersion = b3;
        this.minorVersion = b4;
        this.recordEpoch = i2;
        this.recordSN = j2;
        this.fragment = byteBuffer;
        this.handshakeStatus = null;
    }

    public String toString() {
        return "contentType: " + ((int) this.contentType) + "/majorVersion: " + ((int) this.majorVersion) + "/minorVersion: " + ((int) this.minorVersion) + "/recordEpoch: " + this.recordEpoch + "/recordSN: 0x" + Long.toHexString(this.recordSN) + "/fragment: " + ((Object) this.fragment);
    }
}
