package sun.security.pkcs11;

import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECParameterSpec;
import java.security.spec.ECPoint;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import sun.security.ec.ECPrivateKeyImpl;
import sun.security.ec.ECPublicKeyImpl;
import sun.security.x509.X509Key;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11ECUtil.class */
final class P11ECUtil {
    static ECPublicKey decodeX509ECPublicKey(byte[] bArr) throws InvalidKeySpecException {
        return (ECPublicKey) ECGeneratePublic(new X509EncodedKeySpec(bArr));
    }

    static byte[] x509EncodeECPublicKey(ECPoint eCPoint, ECParameterSpec eCParameterSpec) throws InvalidKeySpecException {
        return ((X509Key) ECGeneratePublic(new ECPublicKeySpec(eCPoint, eCParameterSpec))).getEncoded();
    }

    static ECPrivateKey decodePKCS8ECPrivateKey(byte[] bArr) throws InvalidKeySpecException {
        return (ECPrivateKey) ECGeneratePrivate(new PKCS8EncodedKeySpec(bArr));
    }

    static ECPrivateKey generateECPrivateKey(BigInteger bigInteger, ECParameterSpec eCParameterSpec) throws InvalidKeySpecException {
        return (ECPrivateKey) ECGeneratePrivate(new ECPrivateKeySpec(bigInteger, eCParameterSpec));
    }

    private static PublicKey ECGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
        try {
            if (keySpec instanceof X509EncodedKeySpec) {
                return new ECPublicKeyImpl(((X509EncodedKeySpec) keySpec).getEncoded());
            }
            if (keySpec instanceof ECPublicKeySpec) {
                ECPublicKeySpec eCPublicKeySpec = (ECPublicKeySpec) keySpec;
                return new ECPublicKeyImpl(eCPublicKeySpec.getW(), eCPublicKeySpec.getParams());
            }
            throw new InvalidKeySpecException("Only ECPublicKeySpec and X509EncodedKeySpec supported for EC public keys");
        } catch (InvalidKeySpecException e2) {
            throw e2;
        } catch (GeneralSecurityException e3) {
            throw new InvalidKeySpecException(e3);
        }
    }

    private static PrivateKey ECGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
        try {
            if (keySpec instanceof PKCS8EncodedKeySpec) {
                return new ECPrivateKeyImpl(((PKCS8EncodedKeySpec) keySpec).getEncoded());
            }
            if (keySpec instanceof ECPrivateKeySpec) {
                ECPrivateKeySpec eCPrivateKeySpec = (ECPrivateKeySpec) keySpec;
                return new ECPrivateKeyImpl(eCPrivateKeySpec.getS(), eCPrivateKeySpec.getParams());
            }
            throw new InvalidKeySpecException("Only ECPrivateKeySpec and PKCS8EncodedKeySpec supported for EC private keys");
        } catch (InvalidKeySpecException e2) {
            throw e2;
        } catch (GeneralSecurityException e3) {
            throw new InvalidKeySpecException(e3);
        }
    }

    private P11ECUtil() {
    }
}
