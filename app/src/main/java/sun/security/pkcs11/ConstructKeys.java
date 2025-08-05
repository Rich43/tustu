package sun.security.pkcs11;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/* compiled from: P11RSACipher.java */
/* loaded from: sunpkcs11.jar:sun/security/pkcs11/ConstructKeys.class */
final class ConstructKeys {
    ConstructKeys() {
    }

    private static final PublicKey constructPublicKey(byte[] bArr, String str) throws NoSuchAlgorithmException, InvalidKeyException {
        try {
            return KeyFactory.getInstance(str).generatePublic(new X509EncodedKeySpec(bArr));
        } catch (NoSuchAlgorithmException e2) {
            throw new NoSuchAlgorithmException("No installed providers can create keys for the " + str + "algorithm", e2);
        } catch (InvalidKeySpecException e3) {
            throw new InvalidKeyException("Cannot construct public key", e3);
        }
    }

    private static final PrivateKey constructPrivateKey(byte[] bArr, String str) throws NoSuchAlgorithmException, InvalidKeyException {
        try {
            return KeyFactory.getInstance(str).generatePrivate(new PKCS8EncodedKeySpec(bArr));
        } catch (NoSuchAlgorithmException e2) {
            throw new NoSuchAlgorithmException("No installed providers can create keys for the " + str + "algorithm", e2);
        } catch (InvalidKeySpecException e3) {
            throw new InvalidKeyException("Cannot construct private key", e3);
        }
    }

    private static final SecretKey constructSecretKey(byte[] bArr, String str) {
        return new SecretKeySpec(bArr, str);
    }

    static final Key constructKey(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
        switch (i2) {
            case 1:
                return constructPublicKey(bArr, str);
            case 2:
                return constructPrivateKey(bArr, str);
            case 3:
                return constructSecretKey(bArr, str);
            default:
                throw new InvalidKeyException("Unknown keytype " + i2);
        }
    }
}
