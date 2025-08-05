package com.sun.crypto.provider;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactorySpi;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.DHPrivateKeySpec;
import javax.crypto.spec.DHPublicKeySpec;

/* loaded from: sunjce_provider.jar:com/sun/crypto/provider/DHKeyFactory.class */
public final class DHKeyFactory extends KeyFactorySpi {
    @Override // java.security.KeyFactorySpi
    protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
        try {
            if (keySpec instanceof DHPublicKeySpec) {
                DHPublicKeySpec dHPublicKeySpec = (DHPublicKeySpec) keySpec;
                return new DHPublicKey(dHPublicKeySpec.getY(), dHPublicKeySpec.getP(), dHPublicKeySpec.getG());
            }
            if (keySpec instanceof X509EncodedKeySpec) {
                return new DHPublicKey(((X509EncodedKeySpec) keySpec).getEncoded());
            }
            throw new InvalidKeySpecException("Inappropriate key specification");
        } catch (InvalidKeyException e2) {
            throw new InvalidKeySpecException("Inappropriate key specification", e2);
        }
    }

    @Override // java.security.KeyFactorySpi
    protected PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
        try {
            if (keySpec instanceof DHPrivateKeySpec) {
                DHPrivateKeySpec dHPrivateKeySpec = (DHPrivateKeySpec) keySpec;
                return new DHPrivateKey(dHPrivateKeySpec.getX(), dHPrivateKeySpec.getP(), dHPrivateKeySpec.getG());
            }
            if (keySpec instanceof PKCS8EncodedKeySpec) {
                return new DHPrivateKey(((PKCS8EncodedKeySpec) keySpec).getEncoded());
            }
            throw new InvalidKeySpecException("Inappropriate key specification");
        } catch (InvalidKeyException e2) {
            throw new InvalidKeySpecException("Inappropriate key specification", e2);
        }
    }

    @Override // java.security.KeyFactorySpi
    protected <T extends KeySpec> T engineGetKeySpec(Key key, Class<T> cls) throws InvalidKeySpecException {
        if (key instanceof javax.crypto.interfaces.DHPublicKey) {
            if (cls.isAssignableFrom(DHPublicKeySpec.class)) {
                javax.crypto.interfaces.DHPublicKey dHPublicKey = (javax.crypto.interfaces.DHPublicKey) key;
                DHParameterSpec params = dHPublicKey.getParams();
                return cls.cast(new DHPublicKeySpec(dHPublicKey.getY(), params.getP(), params.getG()));
            }
            if (cls.isAssignableFrom(X509EncodedKeySpec.class)) {
                return cls.cast(new X509EncodedKeySpec(key.getEncoded()));
            }
            throw new InvalidKeySpecException("Inappropriate key specification");
        }
        if (key instanceof javax.crypto.interfaces.DHPrivateKey) {
            if (cls.isAssignableFrom(DHPrivateKeySpec.class)) {
                javax.crypto.interfaces.DHPrivateKey dHPrivateKey = (javax.crypto.interfaces.DHPrivateKey) key;
                DHParameterSpec params2 = dHPrivateKey.getParams();
                return cls.cast(new DHPrivateKeySpec(dHPrivateKey.getX(), params2.getP(), params2.getG()));
            }
            if (cls.isAssignableFrom(PKCS8EncodedKeySpec.class)) {
                return cls.cast(new PKCS8EncodedKeySpec(key.getEncoded()));
            }
            throw new InvalidKeySpecException("Inappropriate key specification");
        }
        throw new InvalidKeySpecException("Inappropriate key type");
    }

    @Override // java.security.KeyFactorySpi
    protected Key engineTranslateKey(Key key) throws InvalidKeyException {
        try {
            if (key instanceof javax.crypto.interfaces.DHPublicKey) {
                if (key instanceof DHPublicKey) {
                    return key;
                }
                return engineGeneratePublic((DHPublicKeySpec) engineGetKeySpec(key, DHPublicKeySpec.class));
            }
            if (key instanceof javax.crypto.interfaces.DHPrivateKey) {
                if (key instanceof DHPrivateKey) {
                    return key;
                }
                return engineGeneratePrivate((DHPrivateKeySpec) engineGetKeySpec(key, DHPrivateKeySpec.class));
            }
            throw new InvalidKeyException("Wrong algorithm type");
        } catch (InvalidKeySpecException e2) {
            throw new InvalidKeyException("Cannot translate key", e2);
        }
    }
}
