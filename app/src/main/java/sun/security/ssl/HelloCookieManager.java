package sun.security.ssl;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import sun.security.ssl.ClientHello;

/* loaded from: jsse.jar:sun/security/ssl/HelloCookieManager.class */
abstract class HelloCookieManager {
    abstract byte[] createCookie(ServerHandshakeContext serverHandshakeContext, ClientHello.ClientHelloMessage clientHelloMessage) throws IOException;

    abstract boolean isCookieValid(ServerHandshakeContext serverHandshakeContext, ClientHello.ClientHelloMessage clientHelloMessage, byte[] bArr) throws IOException;

    HelloCookieManager() {
    }

    /* loaded from: jsse.jar:sun/security/ssl/HelloCookieManager$Builder.class */
    static class Builder {
        final SecureRandom secureRandom;
        private volatile T13HelloCookieManager t13HelloCookieManager;

        Builder(SecureRandom secureRandom) {
            this.secureRandom = secureRandom;
        }

        HelloCookieManager valueOf(ProtocolVersion protocolVersion) {
            if (protocolVersion.useTLS13PlusSpec()) {
                if (this.t13HelloCookieManager != null) {
                    return this.t13HelloCookieManager;
                }
                synchronized (this) {
                    if (this.t13HelloCookieManager == null) {
                        this.t13HelloCookieManager = new T13HelloCookieManager(this.secureRandom);
                    }
                }
                return this.t13HelloCookieManager;
            }
            return null;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/HelloCookieManager$T13HelloCookieManager.class */
    private static final class T13HelloCookieManager extends HelloCookieManager {
        final SecureRandom secureRandom;
        private int cookieVersion;
        private final byte[] cookieSecret = new byte[64];
        private final byte[] legacySecret = new byte[64];

        T13HelloCookieManager(SecureRandom secureRandom) {
            this.secureRandom = secureRandom;
            this.cookieVersion = secureRandom.nextInt();
            secureRandom.nextBytes(this.cookieSecret);
            System.arraycopy(this.cookieSecret, 0, this.legacySecret, 0, 64);
        }

        @Override // sun.security.ssl.HelloCookieManager
        byte[] createCookie(ServerHandshakeContext serverHandshakeContext, ClientHello.ClientHelloMessage clientHelloMessage) throws IOException {
            int i2;
            byte[] bArr;
            synchronized (this) {
                i2 = this.cookieVersion;
                bArr = this.cookieSecret;
                if ((this.cookieVersion & 16777215) == 0) {
                    System.arraycopy(this.cookieSecret, 0, this.legacySecret, 0, 64);
                    this.secureRandom.nextBytes(this.cookieSecret);
                }
                this.cookieVersion++;
            }
            MessageDigest messageDigest = JsseJce.getMessageDigest(serverHandshakeContext.negotiatedCipherSuite.hashAlg.name);
            messageDigest.update(clientHelloMessage.getHeaderBytes());
            byte[] bArrDigest = messageDigest.digest(bArr);
            serverHandshakeContext.handshakeHash.update();
            byte[] bArrDigest2 = serverHandshakeContext.handshakeHash.digest();
            byte[] bArr2 = {(byte) ((serverHandshakeContext.negotiatedCipherSuite.id >> 8) & 255), (byte) (serverHandshakeContext.negotiatedCipherSuite.id & 255), (byte) ((i2 >> 24) & 255)};
            byte[] bArrCopyOf = Arrays.copyOf(bArr2, bArr2.length + bArrDigest.length + bArrDigest2.length);
            System.arraycopy(bArrDigest, 0, bArrCopyOf, bArr2.length, bArrDigest.length);
            System.arraycopy(bArrDigest2, 0, bArrCopyOf, bArr2.length + bArrDigest.length, bArrDigest2.length);
            return bArrCopyOf;
        }

        @Override // sun.security.ssl.HelloCookieManager
        boolean isCookieValid(ServerHandshakeContext serverHandshakeContext, ClientHello.ClientHelloMessage clientHelloMessage, byte[] bArr) throws IOException {
            CipherSuite cipherSuiteValueOf;
            byte[] bArr2;
            if (bArr == null || bArr.length <= 32 || (cipherSuiteValueOf = CipherSuite.valueOf(((bArr[0] & 255) << 8) | (bArr[1] & 255))) == null || cipherSuiteValueOf.hashAlg == null || cipherSuiteValueOf.hashAlg.hashLength == 0) {
                return false;
            }
            int i2 = cipherSuiteValueOf.hashAlg.hashLength;
            if (bArr.length != 3 + (i2 * 2)) {
                return false;
            }
            byte[] bArrCopyOfRange = Arrays.copyOfRange(bArr, 3, 3 + i2);
            byte[] bArrCopyOfRange2 = Arrays.copyOfRange(bArr, 3 + i2, bArr.length);
            synchronized (this) {
                if (((byte) ((this.cookieVersion >> 24) & 255)) == bArr[2]) {
                    bArr2 = this.cookieSecret;
                } else {
                    bArr2 = this.legacySecret;
                }
            }
            MessageDigest messageDigest = JsseJce.getMessageDigest(cipherSuiteValueOf.hashAlg.name);
            messageDigest.update(clientHelloMessage.getHeaderBytes());
            if (!MessageDigest.isEqual(messageDigest.digest(bArr2), bArrCopyOfRange)) {
                return false;
            }
            serverHandshakeContext.handshakeHash.push(ServerHello.hrrReproducer.produce(serverHandshakeContext, clientHelloMessage));
            byte[] bArr3 = new byte[4 + i2];
            bArr3[0] = SSLHandshake.MESSAGE_HASH.id;
            bArr3[1] = 0;
            bArr3[2] = 0;
            bArr3[3] = (byte) (i2 & 255);
            System.arraycopy(bArrCopyOfRange2, 0, bArr3, 4, i2);
            serverHandshakeContext.handshakeHash.push(bArr3);
            return true;
        }
    }
}
