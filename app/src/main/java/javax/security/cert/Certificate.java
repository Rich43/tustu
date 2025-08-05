package javax.security.cert;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SignatureException;

/* loaded from: rt.jar:javax/security/cert/Certificate.class */
public abstract class Certificate {
    public abstract byte[] getEncoded() throws CertificateEncodingException;

    public abstract void verify(PublicKey publicKey) throws CertificateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException;

    public abstract void verify(PublicKey publicKey, String str) throws CertificateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException, NoSuchProviderException;

    public abstract String toString();

    public abstract PublicKey getPublicKey();

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Certificate)) {
            return false;
        }
        try {
            byte[] encoded = getEncoded();
            byte[] encoded2 = ((Certificate) obj).getEncoded();
            if (encoded.length != encoded2.length) {
                return false;
            }
            for (int i2 = 0; i2 < encoded.length; i2++) {
                if (encoded[i2] != encoded2[i2]) {
                    return false;
                }
            }
            return true;
        } catch (CertificateException e2) {
            return false;
        }
    }

    public int hashCode() {
        int i2 = 0;
        try {
            byte[] encoded = getEncoded();
            for (int i3 = 1; i3 < encoded.length; i3++) {
                i2 += encoded[i3] * i3;
            }
            return i2;
        } catch (CertificateException e2) {
            return i2;
        }
    }
}
