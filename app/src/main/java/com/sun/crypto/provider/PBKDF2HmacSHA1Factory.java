package com.sun.crypto.provider;

import java.security.InvalidKeyException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactorySpi;
import javax.crypto.spec.PBEKeySpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/PBKDF2HmacSHA1Factory.class */
public final class PBKDF2HmacSHA1Factory extends SecretKeyFactorySpi {
    @Override // javax.crypto.SecretKeyFactorySpi
    protected SecretKey engineGenerateSecret(KeySpec keySpec) throws InvalidKeySpecException {
        if (!(keySpec instanceof PBEKeySpec)) {
            throw new InvalidKeySpecException("Invalid key spec");
        }
        return new PBKDF2KeyImpl((PBEKeySpec) keySpec, "HmacSHA1");
    }

    @Override // javax.crypto.SecretKeyFactorySpi
    protected KeySpec engineGetKeySpec(SecretKey secretKey, Class<?> cls) throws InvalidKeySpecException {
        if (secretKey instanceof javax.crypto.interfaces.PBEKey) {
            if (cls != null && PBEKeySpec.class.isAssignableFrom(cls)) {
                javax.crypto.interfaces.PBEKey pBEKey = (javax.crypto.interfaces.PBEKey) secretKey;
                return new PBEKeySpec(pBEKey.getPassword(), pBEKey.getSalt(), pBEKey.getIterationCount(), pBEKey.getEncoded().length * 8);
            }
            throw new InvalidKeySpecException("Invalid key spec");
        }
        throw new InvalidKeySpecException("Invalid key format/algorithm");
    }

    @Override // javax.crypto.SecretKeyFactorySpi
    protected SecretKey engineTranslateKey(SecretKey secretKey) throws InvalidKeyException {
        if (secretKey != null && secretKey.getAlgorithm().equalsIgnoreCase("PBKDF2WithHmacSHA1") && secretKey.getFormat().equalsIgnoreCase("RAW")) {
            if (secretKey instanceof PBKDF2KeyImpl) {
                return secretKey;
            }
            if (secretKey instanceof javax.crypto.interfaces.PBEKey) {
                javax.crypto.interfaces.PBEKey pBEKey = (javax.crypto.interfaces.PBEKey) secretKey;
                try {
                    return new PBKDF2KeyImpl(new PBEKeySpec(pBEKey.getPassword(), pBEKey.getSalt(), pBEKey.getIterationCount(), pBEKey.getEncoded().length * 8), "HmacSHA1");
                } catch (InvalidKeySpecException e2) {
                    InvalidKeyException invalidKeyException = new InvalidKeyException("Invalid key component(s)");
                    invalidKeyException.initCause(e2);
                    throw invalidKeyException;
                }
            }
        }
        throw new InvalidKeyException("Invalid key format/algorithm");
    }
}
