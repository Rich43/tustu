package com.sun.crypto.provider;

import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactorySpi;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/DESKeyFactory.class */
public final class DESKeyFactory extends SecretKeyFactorySpi {
    @Override // javax.crypto.SecretKeyFactorySpi
    protected SecretKey engineGenerateSecret(KeySpec keySpec) throws InvalidKeySpecException {
        try {
            if (keySpec instanceof DESKeySpec) {
                return new DESKey(((DESKeySpec) keySpec).getKey());
            }
            if (keySpec instanceof SecretKeySpec) {
                return new DESKey(((SecretKeySpec) keySpec).getEncoded());
            }
            throw new InvalidKeySpecException("Inappropriate key specification");
        } catch (InvalidKeyException e2) {
            throw new InvalidKeySpecException(e2.getMessage());
        }
    }

    @Override // javax.crypto.SecretKeyFactorySpi
    protected KeySpec engineGetKeySpec(SecretKey secretKey, Class<?> cls) throws InvalidKeySpecException {
        try {
            if ((secretKey instanceof SecretKey) && secretKey.getAlgorithm().equalsIgnoreCase("DES") && secretKey.getFormat().equalsIgnoreCase("RAW")) {
                if (cls != null && cls.isAssignableFrom(DESKeySpec.class)) {
                    return new DESKeySpec(secretKey.getEncoded());
                }
                throw new InvalidKeySpecException("Inappropriate key specification");
            }
            throw new InvalidKeySpecException("Inappropriate key format/algorithm");
        } catch (InvalidKeyException e2) {
            throw new InvalidKeySpecException("Secret key has wrong size");
        }
    }

    @Override // javax.crypto.SecretKeyFactorySpi
    protected SecretKey engineTranslateKey(SecretKey secretKey) throws InvalidKeyException {
        if (secretKey != null) {
            try {
                if (secretKey.getAlgorithm().equalsIgnoreCase("DES") && secretKey.getFormat().equalsIgnoreCase("RAW")) {
                    if (secretKey instanceof DESKey) {
                        return secretKey;
                    }
                    return engineGenerateSecret((DESKeySpec) engineGetKeySpec(secretKey, DESKeySpec.class));
                }
            } catch (InvalidKeySpecException e2) {
                throw new InvalidKeyException("Cannot translate key");
            }
        }
        throw new InvalidKeyException("Inappropriate key format/algorithm");
    }
}
