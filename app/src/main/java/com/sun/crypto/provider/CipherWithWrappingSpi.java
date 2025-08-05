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
import javax.crypto.BadPaddingException;
import javax.crypto.CipherSpi;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/CipherWithWrappingSpi.class */
public abstract class CipherWithWrappingSpi extends CipherSpi {
    @Override // javax.crypto.CipherSpi
    protected final byte[] engineWrap(Key key) throws IllegalBlockSizeException, InvalidKeyException {
        byte[] encoded;
        byte[] bArrEngineDoFinal = null;
        try {
            encoded = key.getEncoded();
        } catch (BadPaddingException e2) {
        }
        if (encoded == null || encoded.length == 0) {
            throw new InvalidKeyException("Cannot get an encoding of the key to be wrapped");
        }
        bArrEngineDoFinal = engineDoFinal(encoded, 0, encoded.length);
        return bArrEngineDoFinal;
    }

    @Override // javax.crypto.CipherSpi
    protected final Key engineUnwrap(byte[] bArr, String str, int i2) throws NoSuchAlgorithmException, InvalidKeyException {
        SecretKey secretKeyConstructPublicKey = null;
        try {
            byte[] bArrEngineDoFinal = engineDoFinal(bArr, 0, bArr.length);
            switch (i2) {
                case 1:
                    secretKeyConstructPublicKey = constructPublicKey(bArrEngineDoFinal, str);
                    break;
                case 2:
                    secretKeyConstructPublicKey = constructPrivateKey(bArrEngineDoFinal, str);
                    break;
                case 3:
                    secretKeyConstructPublicKey = constructSecretKey(bArrEngineDoFinal, str);
                    break;
            }
            return secretKeyConstructPublicKey;
        } catch (BadPaddingException e2) {
            throw new InvalidKeyException();
        } catch (IllegalBlockSizeException e3) {
            throw new InvalidKeyException();
        }
    }

    private final PublicKey constructPublicKey(byte[] bArr, String str) throws NoSuchAlgorithmException, InvalidKeyException {
        PublicKey publicKeyGeneratePublic = null;
        try {
            publicKeyGeneratePublic = KeyFactory.getInstance(str, SunJCE.getInstance()).generatePublic(new X509EncodedKeySpec(bArr));
        } catch (NoSuchAlgorithmException e2) {
            try {
                publicKeyGeneratePublic = KeyFactory.getInstance(str).generatePublic(new X509EncodedKeySpec(bArr));
            } catch (NoSuchAlgorithmException e3) {
                throw new NoSuchAlgorithmException("No installed providers can create keys for the " + str + "algorithm");
            } catch (InvalidKeySpecException e4) {
            }
        } catch (InvalidKeySpecException e5) {
        }
        return publicKeyGeneratePublic;
    }

    private final PrivateKey constructPrivateKey(byte[] bArr, String str) throws NoSuchAlgorithmException, InvalidKeyException {
        PrivateKey privateKeyGeneratePrivate = null;
        try {
            return KeyFactory.getInstance(str, SunJCE.getInstance()).generatePrivate(new PKCS8EncodedKeySpec(bArr));
        } catch (NoSuchAlgorithmException e2) {
            try {
                privateKeyGeneratePrivate = KeyFactory.getInstance(str).generatePrivate(new PKCS8EncodedKeySpec(bArr));
            } catch (NoSuchAlgorithmException e3) {
                throw new NoSuchAlgorithmException("No installed providers can create keys for the " + str + "algorithm");
            } catch (InvalidKeySpecException e4) {
            }
            return privateKeyGeneratePrivate;
        } catch (InvalidKeySpecException e5) {
            return privateKeyGeneratePrivate;
        }
    }

    private final SecretKey constructSecretKey(byte[] bArr, String str) {
        return new SecretKeySpec(bArr, str);
    }
}
