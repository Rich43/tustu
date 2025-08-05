package sun.security.ssl;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.spec.AlgorithmParameterSpec;
import javax.crypto.SecretKey;
import javax.net.ssl.SSLHandshakeException;
import sun.util.locale.LanguageTag;

/* loaded from: jsse.jar:sun/security/ssl/SSLBasicKeyDerivation.class */
final class SSLBasicKeyDerivation implements SSLKeyDerivation {
    private final String hashAlg;
    private final SecretKey secret;
    private final byte[] hkdfInfo;

    SSLBasicKeyDerivation(SecretKey secretKey, String str, byte[] bArr, byte[] bArr2, int i2) {
        this.hashAlg = str.replace(LanguageTag.SEP, "");
        this.secret = secretKey;
        this.hkdfInfo = createHkdfInfo(bArr, bArr2, i2);
    }

    @Override // sun.security.ssl.SSLKeyDerivation
    public SecretKey deriveKey(String str, AlgorithmParameterSpec algorithmParameterSpec) throws IOException {
        try {
            return new HKDF(this.hashAlg).expand(this.secret, this.hkdfInfo, ((SecretSizeSpec) algorithmParameterSpec).length, str);
        } catch (GeneralSecurityException e2) {
            throw ((SSLHandshakeException) new SSLHandshakeException("Could not generate secret").initCause(e2));
        }
    }

    private static byte[] createHkdfInfo(byte[] bArr, byte[] bArr2, int i2) {
        byte[] bArr3 = new byte[4 + bArr.length + bArr2.length];
        ByteBuffer byteBufferWrap = ByteBuffer.wrap(bArr3);
        try {
            Record.putInt16(byteBufferWrap, i2);
            Record.putBytes8(byteBufferWrap, bArr);
            Record.putBytes8(byteBufferWrap, bArr2);
        } catch (IOException e2) {
        }
        return bArr3;
    }

    /* loaded from: jsse.jar:sun/security/ssl/SSLBasicKeyDerivation$SecretSizeSpec.class */
    static class SecretSizeSpec implements AlgorithmParameterSpec {
        final int length;

        SecretSizeSpec(int i2) {
            this.length = i2;
        }
    }
}
