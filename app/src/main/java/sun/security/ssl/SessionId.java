package sun.security.ssl;

import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import javax.net.ssl.SSLProtocolException;

/* loaded from: jsse.jar:sun/security/ssl/SessionId.class */
final class SessionId {
    private static final int MAX_LENGTH = 32;
    private final byte[] sessionId;

    SessionId(boolean z2, SecureRandom secureRandom) {
        if (z2 && secureRandom != null) {
            this.sessionId = new RandomCookie(secureRandom).randomBytes;
        } else {
            this.sessionId = new byte[0];
        }
    }

    SessionId(byte[] bArr) {
        this.sessionId = (byte[]) bArr.clone();
    }

    int length() {
        return this.sessionId.length;
    }

    byte[] getId() {
        return (byte[]) this.sessionId.clone();
    }

    public String toString() {
        if (this.sessionId.length == 0) {
            return "";
        }
        return Utilities.toHexString(this.sessionId);
    }

    public int hashCode() {
        return Arrays.hashCode(this.sessionId);
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof SessionId) {
            return MessageDigest.isEqual(this.sessionId, ((SessionId) obj).sessionId);
        }
        return false;
    }

    void checkLength(int i2) throws SSLProtocolException {
        if (this.sessionId.length > 32) {
            throw new SSLProtocolException("Invalid session ID length (" + this.sessionId.length + " bytes)");
        }
    }
}
