package com.sun.crypto.provider;

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

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/ConstructKeys.class */
final class ConstructKeys {
    ConstructKeys() {
    }

    private static final PublicKey constructPublicKey(byte[] bArr, String str) throws NoSuchAlgorithmException, InvalidKeyException {
        PublicKey publicKeyGeneratePublic;
        try {
            publicKeyGeneratePublic = KeyFactory.getInstance(str, SunJCE.getInstance()).generatePublic(new X509EncodedKeySpec(bArr));
        } catch (NoSuchAlgorithmException e2) {
            try {
                publicKeyGeneratePublic = KeyFactory.getInstance(str).generatePublic(new X509EncodedKeySpec(bArr));
            } catch (NoSuchAlgorithmException e3) {
                throw new NoSuchAlgorithmException("No installed providers can create keys for the " + str + "algorithm");
            } catch (InvalidKeySpecException e4) {
                InvalidKeyException invalidKeyException = new InvalidKeyException("Cannot construct public key");
                invalidKeyException.initCause(e4);
                throw invalidKeyException;
            }
        } catch (InvalidKeySpecException e5) {
            InvalidKeyException invalidKeyException2 = new InvalidKeyException("Cannot construct public key");
            invalidKeyException2.initCause(e5);
            throw invalidKeyException2;
        }
        return publicKeyGeneratePublic;
    }

    private static final PrivateKey constructPrivateKey(byte[] bArr, String str) throws NoSuchAlgorithmException, InvalidKeyException {
        try {
            return KeyFactory.getInstance(str, SunJCE.getInstance()).generatePrivate(new PKCS8EncodedKeySpec(bArr));
        } catch (NoSuchAlgorithmException e2) {
            try {
                return KeyFactory.getInstance(str).generatePrivate(new PKCS8EncodedKeySpec(bArr));
            } catch (NoSuchAlgorithmException e3) {
                throw new NoSuchAlgorithmException("No installed providers can create keys for the " + str + "algorithm");
            } catch (InvalidKeySpecException e4) {
                InvalidKeyException invalidKeyException = new InvalidKeyException("Cannot construct private key");
                invalidKeyException.initCause(e4);
                throw invalidKeyException;
            }
        } catch (InvalidKeySpecException e5) {
            InvalidKeyException invalidKeyException2 = new InvalidKeyException("Cannot construct private key");
            invalidKeyException2.initCause(e5);
            throw invalidKeyException2;
        }
    }

    private static final SecretKey constructSecretKey(byte[] bArr, String str) {
        return new SecretKeySpec(bArr, str);
    }

    static final Key constructKey(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
        Key keyConstructPublicKey = null;
        switch (i2) {
            case 1:
                keyConstructPublicKey = constructPublicKey(bArr, str);
                break;
            case 2:
                keyConstructPublicKey = constructPrivateKey(bArr, str);
                break;
            case 3:
                keyConstructPublicKey = constructSecretKey(bArr, str);
                break;
        }
        return keyConstructPublicKey;
    }
}
