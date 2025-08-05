package com.sun.crypto.provider;

import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactorySpi;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.SecretKeySpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/DESedeKeyFactory.class */
public final class DESedeKeyFactory extends SecretKeyFactorySpi {
    @Override // javax.crypto.SecretKeyFactorySpi
    protected SecretKey engineGenerateSecret(KeySpec keySpec) throws InvalidKeySpecException {
        try {
            if (keySpec instanceof DESedeKeySpec) {
                return new DESedeKey(((DESedeKeySpec) keySpec).getKey());
            }
            if (keySpec instanceof SecretKeySpec) {
                return new DESedeKey(((SecretKeySpec) keySpec).getEncoded());
            }
            throw new InvalidKeySpecException("Inappropriate key specification");
        } catch (InvalidKeyException e2) {
            throw new InvalidKeySpecException(e2.getMessage());
        }
    }

    @Override // javax.crypto.SecretKeyFactorySpi
    protected KeySpec engineGetKeySpec(SecretKey secretKey, Class<?> cls) throws InvalidKeySpecException {
        try {
            if ((secretKey instanceof SecretKey) && secretKey.getAlgorithm().equalsIgnoreCase("DESede") && secretKey.getFormat().equalsIgnoreCase("RAW")) {
                if (cls.isAssignableFrom(DESedeKeySpec.class)) {
                    return new DESedeKeySpec(secretKey.getEncoded());
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
                if (secretKey.getAlgorithm().equalsIgnoreCase("DESede") && secretKey.getFormat().equalsIgnoreCase("RAW")) {
                    if (secretKey instanceof DESedeKey) {
                        return secretKey;
                    }
                    return engineGenerateSecret((DESedeKeySpec) engineGetKeySpec(secretKey, DESedeKeySpec.class));
                }
            } catch (InvalidKeySpecException e2) {
                throw new InvalidKeyException("Cannot translate key");
            }
        }
        throw new InvalidKeyException("Inappropriate key format/algorithm");
    }
}
