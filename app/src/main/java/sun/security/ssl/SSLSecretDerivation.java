package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.SecretKey;
import javax.net.ssl.SSLHandshakeException;
import sun.security.ssl.CipherSuite;
import sun.util.locale.LanguageTag;

/* loaded from: jsse.jar:sun/security/ssl/SSLSecretDerivation.class */
final class SSLSecretDerivation implements SSLKeyDerivation {
    private static final byte[] sha256EmptyDigest = {-29, -80, -60, 66, -104, -4, 28, 20, -102, -5, -12, -56, -103, 111, -71, 36, 39, -82, 65, -28, 100, -101, -109, 76, -92, -107, -103, 27, 120, 82, -72, 85};
    private static final byte[] sha384EmptyDigest = {56, -80, 96, -89, 81, -84, -106, 56, 76, -39, 50, 126, -79, -79, -29, 106, 33, -3, -73, 17, 20, -66, 7, 67, 76, 12, -57, -65, 99, -10, -31, -38, 39, 78, -34, -65, -25, 111, 101, -5, -43, 26, -46, -15, 72, -104, -71, 91};
    private final HandshakeContext context;
    private final String hkdfAlg;
    private final CipherSuite.HashAlg hashAlg;
    private final SecretKey secret;
    private final byte[] transcriptHash;

    SSLSecretDerivation(HandshakeContext handshakeContext, SecretKey secretKey) {
        this.context = handshakeContext;
        this.secret = secretKey;
        this.hashAlg = handshakeContext.negotiatedCipherSuite.hashAlg;
        this.hkdfAlg = "HKDF-Expand/Hmac" + this.hashAlg.name.replace(LanguageTag.SEP, "");
        handshakeContext.handshakeHash.update();
        this.transcriptHash = handshakeContext.handshakeHash.digest();
    }

    SSLSecretDerivation forContext(HandshakeContext handshakeContext) {
        return new SSLSecretDerivation(handshakeContext, this.secret);
    }

    @Override // sun.security.ssl.SSLKeyDerivation
    public SecretKey deriveKey(String str, AlgorithmParameterSpec algorithmParameterSpec) throws IOException {
        byte[] bArr;
        SecretSchedule secretScheduleValueOf = SecretSchedule.valueOf(str);
        try {
            if (secretScheduleValueOf == SecretSchedule.TlsSaltSecret) {
                if (this.hashAlg == CipherSuite.HashAlg.H_SHA256) {
                    bArr = sha256EmptyDigest;
                } else if (this.hashAlg == CipherSuite.HashAlg.H_SHA384) {
                    bArr = sha384EmptyDigest;
                } else {
                    throw new SSLHandshakeException("Unexpected unsupported hash algorithm: " + str);
                }
            } else {
                bArr = this.transcriptHash;
            }
            return new HKDF(this.hashAlg.name).expand(this.secret, createHkdfInfo(secretScheduleValueOf.label, bArr, this.hashAlg.hashLength), this.hashAlg.hashLength, str);
        } catch (GeneralSecurityException e2) {
            throw ((SSLHandshakeException) new SSLHandshakeException("Could not generate secret").initCause(e2));
        }
    }

    public static byte[] createHkdfInfo(byte[] bArr, byte[] bArr2, int i2) {
        byte[] bArr3 = new byte[4 + bArr.length + bArr2.length];
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr3);
        try {
            Record.putInt16(byteBufferWrap, i2);
            Record.putBytes8(byteBufferWrap, bArr);
            Record.putBytes8(byteBufferWrap, bArr2);
            return bArr3;
        } catch (IOException e2) {
            throw new RuntimeException("Unexpected exception", e2);
        }
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLSecretDerivation$SecretSchedule.class */
    private enum SecretSchedule {
        TlsSaltSecret("derived"),
        TlsExtBinderKey("ext binder"),
        TlsResBinderKey("res binder"),
        TlsClientEarlyTrafficSecret("c e traffic"),
        TlsEarlyExporterMasterSecret("e exp master"),
        TlsClientHandshakeTrafficSecret("c hs traffic"),
        TlsServerHandshakeTrafficSecret("s hs traffic"),
        TlsClientAppTrafficSecret("c ap traffic"),
        TlsServerAppTrafficSecret("s ap traffic"),
        TlsExporterMasterSecret("exp master"),
        TlsResumptionMasterSecret("res master");

        private final byte[] label;

        SecretSchedule(String str) {
            this.label = ("tls13 " + str).getBytes();
        }
    }
}
