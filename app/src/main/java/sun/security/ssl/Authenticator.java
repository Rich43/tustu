package sun.security.ssl;

import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import sun.security.ssl.CipherSuite;

/* loaded from: jsse.jar:sun/security/ssl/Authenticator.class */
abstract class Authenticator {
    protected final byte[] block;

    abstract boolean seqNumOverflow();

    abstract boolean seqNumIsHuge();

    private Authenticator(byte[] bArr) {
        this.block = bArr;
    }

    static Authenticator valueOf(ProtocolVersion protocolVersion) {
        if (protocolVersion.useTLS13PlusSpec()) {
            return new TLS13Authenticator(protocolVersion);
        }
        if (protocolVersion.useTLS10PlusSpec()) {
            return new TLS10Authenticator(protocolVersion);
        }
        return new SSL30Authenticator();
    }

    static <T extends Authenticator & MAC> T valueOf(ProtocolVersion protocolVersion, CipherSuite.MacAlg macAlg, SecretKey secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
        if (protocolVersion.useTLS13PlusSpec()) {
            throw new RuntimeException("No MacAlg used in TLS 1.3");
        }
        if (protocolVersion.useTLS10PlusSpec()) {
            return new TLS10Mac(protocolVersion, macAlg, secretKey);
        }
        return new SSL30Mac(protocolVersion, macAlg, secretKey);
    }

    static Authenticator nullTlsMac() {
        return new SSLNullMac();
    }

    final byte[] sequenceNumber() {
        return Arrays.copyOf(this.block, 8);
    }

    final void increaseSequenceNumber() {
        for (int i2 = 7; i2 >= 0; i2--) {
            byte[] bArr = this.block;
            int i3 = i2;
            byte b2 = (byte) (bArr[i3] + 1);
            bArr[i3] = b2;
            if (b2 != 0) {
                return;
            }
        }
    }

    byte[] acquireAuthenticationBytes(byte b2, int i2, byte[] bArr) {
        throw new UnsupportedOperationException("Used by AEAD algorithms only");
    }

    /* loaded from: jsse.jar:sun/security/ssl/Authenticator$SSLAuthenticator.class */
    private static class SSLAuthenticator extends Authenticator {
        private SSLAuthenticator(byte[] bArr) {
            super(bArr);
        }

        @Override // sun.security.ssl.Authenticator
        boolean seqNumOverflow() {
            return this.block.length != 0 && this.block[0] == -1 && this.block[1] == -1 && this.block[2] == -1 && this.block[3] == -1 && this.block[4] == -1 && this.block[5] == -1 && this.block[6] == -1;
        }

        @Override // sun.security.ssl.Authenticator
        boolean seqNumIsHuge() {
            return this.block.length != 0 && this.block[0] == -1 && this.block[1] == -1 && this.block[2] == -1 && this.block[3] == -1;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Authenticator$SSLNullAuthenticator.class */
    private static class SSLNullAuthenticator extends SSLAuthenticator {
        private SSLNullAuthenticator() {
            super(new byte[8]);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Authenticator$SSL30Authenticator.class */
    private static class SSL30Authenticator extends SSLAuthenticator {
        private static final int BLOCK_SIZE = 11;

        private SSL30Authenticator() {
            super(new byte[11]);
        }

        @Override // sun.security.ssl.Authenticator
        byte[] acquireAuthenticationBytes(byte b2, int i2, byte[] bArr) {
            byte[] bArr2 = (byte[]) this.block.clone();
            increaseSequenceNumber();
            bArr2[8] = b2;
            bArr2[9] = (byte) (i2 >> 8);
            bArr2[10] = (byte) i2;
            return bArr2;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Authenticator$TLS10Authenticator.class */
    private static class TLS10Authenticator extends SSLAuthenticator {
        private static final int BLOCK_SIZE = 13;

        private TLS10Authenticator(ProtocolVersion protocolVersion) {
            super(new byte[13]);
            this.block[9] = protocolVersion.major;
            this.block[10] = protocolVersion.minor;
        }

        @Override // sun.security.ssl.Authenticator
        byte[] acquireAuthenticationBytes(byte b2, int i2, byte[] bArr) {
            byte[] bArr2 = (byte[]) this.block.clone();
            if (bArr != null) {
                if (bArr.length != 8) {
                    throw new RuntimeException("Insufficient explicit sequence number bytes");
                }
                System.arraycopy(bArr, 0, bArr2, 0, bArr.length);
            } else {
                increaseSequenceNumber();
            }
            bArr2[8] = b2;
            bArr2[11] = (byte) (i2 >> 8);
            bArr2[12] = (byte) i2;
            return bArr2;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Authenticator$TLS13Authenticator.class */
    private static final class TLS13Authenticator extends SSLAuthenticator {
        private static final int BLOCK_SIZE = 13;

        private TLS13Authenticator(ProtocolVersion protocolVersion) {
            super(new byte[13]);
            this.block[9] = ProtocolVersion.TLS12.major;
            this.block[10] = ProtocolVersion.TLS12.minor;
        }

        @Override // sun.security.ssl.Authenticator
        byte[] acquireAuthenticationBytes(byte b2, int i2, byte[] bArr) {
            byte[] bArrCopyOfRange = Arrays.copyOfRange(this.block, 8, 13);
            increaseSequenceNumber();
            bArrCopyOfRange[0] = b2;
            bArrCopyOfRange[3] = (byte) (i2 >> 8);
            bArrCopyOfRange[4] = (byte) (i2 & 255);
            return bArrCopyOfRange;
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Authenticator$MAC.class */
    interface MAC {
        CipherSuite.MacAlg macAlg();

        byte[] compute(byte b2, ByteBuffer byteBuffer, byte[] bArr, boolean z2);

        default byte[] compute(byte b2, ByteBuffer byteBuffer, boolean z2) {
            return compute(b2, byteBuffer, null, z2);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Authenticator$MacImpl.class */
    private class MacImpl implements MAC {
        private final CipherSuite.MacAlg macAlg;
        private final Mac mac;

        private MacImpl() {
            this.macAlg = CipherSuite.MacAlg.M_NULL;
            this.mac = null;
        }

        private MacImpl(ProtocolVersion protocolVersion, CipherSuite.MacAlg macAlg, SecretKey secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
            String str;
            if (macAlg == null) {
                throw new RuntimeException("Null MacAlg");
            }
            boolean z2 = protocolVersion.id < ProtocolVersion.TLS10.id;
            switch (macAlg) {
                case M_MD5:
                    str = z2 ? "SslMacMD5" : "HmacMD5";
                    break;
                case M_SHA:
                    str = z2 ? "SslMacSHA1" : "HmacSHA1";
                    break;
                case M_SHA256:
                    str = "HmacSHA256";
                    break;
                case M_SHA384:
                    str = "HmacSHA384";
                    break;
                default:
                    throw new RuntimeException("Unknown MacAlg " + ((Object) macAlg));
            }
            Mac mac = JsseJce.getMac(str);
            mac.init(secretKey);
            this.macAlg = macAlg;
            this.mac = mac;
        }

        @Override // sun.security.ssl.Authenticator.MAC
        public CipherSuite.MacAlg macAlg() {
            return this.macAlg;
        }

        @Override // sun.security.ssl.Authenticator.MAC
        public byte[] compute(byte b2, ByteBuffer byteBuffer, byte[] bArr, boolean z2) throws IllegalStateException {
            if (this.macAlg.size == 0) {
                return new byte[0];
            }
            if (!z2) {
                this.mac.update(Authenticator.this.acquireAuthenticationBytes(b2, byteBuffer.remaining(), bArr));
            }
            this.mac.update(byteBuffer);
            return this.mac.doFinal();
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Authenticator$SSLNullMac.class */
    private static final class SSLNullMac extends SSLNullAuthenticator implements MAC {
        private final MacImpl macImpl;

        public SSLNullMac() {
            super();
            this.macImpl = new MacImpl();
        }

        @Override // sun.security.ssl.Authenticator.MAC
        public CipherSuite.MacAlg macAlg() {
            return this.macImpl.macAlg;
        }

        @Override // sun.security.ssl.Authenticator.MAC
        public byte[] compute(byte b2, ByteBuffer byteBuffer, byte[] bArr, boolean z2) {
            return this.macImpl.compute(b2, byteBuffer, bArr, z2);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Authenticator$SSL30Mac.class */
    private static final class SSL30Mac extends SSL30Authenticator implements MAC {
        private final MacImpl macImpl;

        public SSL30Mac(ProtocolVersion protocolVersion, CipherSuite.MacAlg macAlg, SecretKey secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
            super();
            this.macImpl = new MacImpl(protocolVersion, macAlg, secretKey);
        }

        @Override // sun.security.ssl.Authenticator.MAC
        public CipherSuite.MacAlg macAlg() {
            return this.macImpl.macAlg;
        }

        @Override // sun.security.ssl.Authenticator.MAC
        public byte[] compute(byte b2, ByteBuffer byteBuffer, byte[] bArr, boolean z2) {
            return this.macImpl.compute(b2, byteBuffer, bArr, z2);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/Authenticator$TLS10Mac.class */
    private static final class TLS10Mac extends TLS10Authenticator implements MAC {
        private final MacImpl macImpl;

        public TLS10Mac(ProtocolVersion protocolVersion, CipherSuite.MacAlg macAlg, SecretKey secretKey) throws NoSuchAlgorithmException, InvalidKeyException {
            super(protocolVersion);
            this.macImpl = new MacImpl(protocolVersion, macAlg, secretKey);
        }

        @Override // sun.security.ssl.Authenticator.MAC
        public CipherSuite.MacAlg macAlg() {
            return this.macImpl.macAlg;
        }

        @Override // sun.security.ssl.Authenticator.MAC
        public byte[] compute(byte b2, ByteBuffer byteBuffer, byte[] bArr, boolean z2) {
            return this.macImpl.compute(b2, byteBuffer, bArr, z2);
        }
    }

    static final long toLong(byte[] bArr) {
        if (bArr != null && bArr.length == 8) {
            return ((bArr[0] & 255) << 56) | ((bArr[1] & 255) << 48) | ((bArr[2] & 255) << 40) | ((bArr[3] & 255) << 32) | ((bArr[4] & 255) << 24) | ((bArr[5] & 255) << 16) | ((bArr[6] & 255) << 8) | (bArr[7] & 255);
        }
        return -1L;
    }
}
