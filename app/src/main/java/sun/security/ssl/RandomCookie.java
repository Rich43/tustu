package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.SecureRandom;
import sun.security.util.ByteArrays;

/* loaded from: jsse.jar:sun/security/ssl/RandomCookie.class */
final class RandomCookie {
    final byte[] randomBytes;
    private static final byte[] hrrRandomBytes = {-49, 33, -83, 116, -27, -102, 97, 17, -66, 29, -116, 2, 30, 101, -72, -111, -62, -94, 17, 22, 122, -69, -116, 94, 7, -98, 9, -30, -56, -88, 51, -100};
    private static final byte[] t12Protection = {68, 79, 87, 78, 71, 82, 68, 1};
    private static final byte[] t11Protection = {68, 79, 87, 78, 71, 82, 68, 0};
    static final RandomCookie hrrRandom = new RandomCookie(hrrRandomBytes);

    RandomCookie(SecureRandom secureRandom) {
        this.randomBytes = new byte[32];
        secureRandom.nextBytes(this.randomBytes);
    }

    RandomCookie(HandshakeContext handshakeContext) {
        this.randomBytes = new byte[32];
        handshakeContext.sslContext.getSecureRandom().nextBytes(this.randomBytes);
        byte[] bArr = null;
        if (handshakeContext.maximumActiveProtocol.useTLS13PlusSpec()) {
            if (!handshakeContext.negotiatedProtocol.useTLS13PlusSpec()) {
                bArr = handshakeContext.negotiatedProtocol.useTLS12PlusSpec() ? t12Protection : t11Protection;
            }
        } else if (handshakeContext.maximumActiveProtocol.useTLS12PlusSpec() && !handshakeContext.negotiatedProtocol.useTLS12PlusSpec()) {
            bArr = t11Protection;
        }
        if (bArr != null) {
            System.arraycopy(bArr, 0, this.randomBytes, this.randomBytes.length - bArr.length, bArr.length);
        }
    }

    RandomCookie(ByteBuffer byteBuffer) throws IOException {
        this.randomBytes = new byte[32];
        byteBuffer.get(this.randomBytes);
    }

    private RandomCookie(byte[] bArr) {
        this.randomBytes = new byte[32];
        System.arraycopy(bArr, 0, this.randomBytes, 0, 32);
    }

    public String toString() {
        return "random_bytes = {" + Utilities.toHexString(this.randomBytes) + "}";
    }

    boolean isHelloRetryRequest() {
        return MessageDigest.isEqual(hrrRandomBytes, this.randomBytes);
    }

    boolean isVersionDowngrade(HandshakeContext handshakeContext) {
        if (handshakeContext.maximumActiveProtocol.useTLS13PlusSpec()) {
            if (handshakeContext.negotiatedProtocol.useTLS13PlusSpec()) {
                return false;
            }
            return isT12Downgrade() || isT11Downgrade();
        }
        if (handshakeContext.maximumActiveProtocol.useTLS12PlusSpec() && !handshakeContext.negotiatedProtocol.useTLS12PlusSpec()) {
            return isT11Downgrade();
        }
        return false;
    }

    private boolean isT12Downgrade() {
        return ByteArrays.isEqual(this.randomBytes, 24, 32, t12Protection, 0, 8);
    }

    private boolean isT11Downgrade() {
        return ByteArrays.isEqual(this.randomBytes, 24, 32, t11Protection, 0, 8);
    }
}
