package sun.security.ssl;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.ShortBufferException;
import javax.crypto.spec.SecretKeySpec;
import sun.util.locale.LanguageTag;

/* loaded from: jsse.jar:sun/security/ssl/HKDF.class */
final class HKDF {
    private final String hmacAlg;
    private final Mac hmacObj;
    private final int hmacLen;

    HKDF(String str) throws NoSuchAlgorithmException {
        Objects.requireNonNull(str, "Must provide underlying HKDF Digest algorithm.");
        this.hmacAlg = "Hmac" + str.replace(LanguageTag.SEP, "");
        this.hmacObj = JsseJce.getMac(this.hmacAlg);
        this.hmacLen = this.hmacObj.getMacLength();
    }

    SecretKey extract(SecretKey secretKey, SecretKey secretKey2, String str) throws InvalidKeyException {
        if (secretKey == null) {
            secretKey = new SecretKeySpec(new byte[this.hmacLen], "HKDF-Salt");
        }
        this.hmacObj.init(secretKey);
        return new SecretKeySpec(this.hmacObj.doFinal(secretKey2.getEncoded()), str);
    }

    SecretKey extract(byte[] bArr, SecretKey secretKey, String str) throws InvalidKeyException {
        if (bArr == null) {
            bArr = new byte[this.hmacLen];
        }
        return extract(new SecretKeySpec(bArr, "HKDF-Salt"), secretKey, str);
    }

    SecretKey expand(SecretKey secretKey, byte[] bArr, int i2, String str) throws IllegalStateException, InvalidKeyException {
        Objects.requireNonNull(secretKey, "A null PRK is not allowed.");
        if (i2 > 255 * this.hmacLen) {
            throw new IllegalArgumentException("Requested output length exceeds maximum length allowed for HKDF expansion");
        }
        this.hmacObj.init(secretKey);
        if (bArr == null) {
            bArr = new byte[0];
        }
        int i3 = ((i2 + this.hmacLen) - 1) / this.hmacLen;
        byte[] bArr2 = new byte[i3 * this.hmacLen];
        int i4 = 0;
        int i5 = 0;
        for (int i6 = 0; i6 < i3; i6++) {
            try {
                this.hmacObj.update(bArr2, Math.max(0, i4 - this.hmacLen), i5);
                this.hmacObj.update(bArr);
                this.hmacObj.update((byte) (i6 + 1));
                this.hmacObj.doFinal(bArr2, i4);
                i5 = this.hmacLen;
                i4 += this.hmacLen;
            } catch (ShortBufferException e2) {
                throw new RuntimeException(e2);
            }
        }
        return new SecretKeySpec(bArr2, 0, i2, str);
    }
}
