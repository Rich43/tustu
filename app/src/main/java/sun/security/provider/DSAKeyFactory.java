package sun.security.provider;

import java.security.AccessController;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactorySpi;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.DSAParams;
import java.security.spec.DSAPrivateKeySpec;
import java.security.spec.DSAPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/security/provider/DSAKeyFactory.class */
public class DSAKeyFactory extends KeyFactorySpi {
    private static final String SERIAL_PROP = "sun.security.key.serial.interop";
    static final boolean SERIAL_INTEROP = "true".equalsIgnoreCase((String) AccessController.doPrivileged(new GetPropertyAction(SERIAL_PROP, null)));

    @Override // java.security.KeyFactorySpi
    protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
        try {
            if (keySpec instanceof DSAPublicKeySpec) {
                DSAPublicKeySpec dSAPublicKeySpec = (DSAPublicKeySpec) keySpec;
                if (SERIAL_INTEROP) {
                    return new DSAPublicKey(dSAPublicKeySpec.getY(), dSAPublicKeySpec.getP(), dSAPublicKeySpec.getQ(), dSAPublicKeySpec.getG());
                }
                return new DSAPublicKeyImpl(dSAPublicKeySpec.getY(), dSAPublicKeySpec.getP(), dSAPublicKeySpec.getQ(), dSAPublicKeySpec.getG());
            }
            if (keySpec instanceof X509EncodedKeySpec) {
                if (SERIAL_INTEROP) {
                    return new DSAPublicKey(((X509EncodedKeySpec) keySpec).getEncoded());
                }
                return new DSAPublicKeyImpl(((X509EncodedKeySpec) keySpec).getEncoded());
            }
            throw new InvalidKeySpecException("Inappropriate key specification");
        } catch (InvalidKeyException e2) {
            throw new InvalidKeySpecException("Inappropriate key specification: " + e2.getMessage());
        }
    }

    @Override // java.security.KeyFactorySpi
    protected PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
        try {
            if (keySpec instanceof DSAPrivateKeySpec) {
                DSAPrivateKeySpec dSAPrivateKeySpec = (DSAPrivateKeySpec) keySpec;
                return new DSAPrivateKey(dSAPrivateKeySpec.getX(), dSAPrivateKeySpec.getP(), dSAPrivateKeySpec.getQ(), dSAPrivateKeySpec.getG());
            }
            if (keySpec instanceof PKCS8EncodedKeySpec) {
                return new DSAPrivateKey(((PKCS8EncodedKeySpec) keySpec).getEncoded());
            }
            throw new InvalidKeySpecException("Inappropriate key specification");
        } catch (InvalidKeyException e2) {
            throw new InvalidKeySpecException("Inappropriate key specification: " + e2.getMessage());
        }
    }

    @Override // java.security.KeyFactorySpi
    protected <T extends KeySpec> T engineGetKeySpec(Key key, Class<T> cls) throws InvalidKeySpecException {
        try {
            if (key instanceof java.security.interfaces.DSAPublicKey) {
                Class<?> cls2 = Class.forName("java.security.spec.DSAPublicKeySpec");
                Class<?> cls3 = Class.forName("java.security.spec.X509EncodedKeySpec");
                if (cls.isAssignableFrom(cls2)) {
                    java.security.interfaces.DSAPublicKey dSAPublicKey = (java.security.interfaces.DSAPublicKey) key;
                    DSAParams params = dSAPublicKey.getParams();
                    return cls.cast(new DSAPublicKeySpec(dSAPublicKey.getY(), params.getP(), params.getQ(), params.getG()));
                }
                if (cls.isAssignableFrom(cls3)) {
                    return cls.cast(new X509EncodedKeySpec(key.getEncoded()));
                }
                throw new InvalidKeySpecException("Inappropriate key specification");
            }
            if (key instanceof java.security.interfaces.DSAPrivateKey) {
                Class<?> cls4 = Class.forName("java.security.spec.DSAPrivateKeySpec");
                Class<?> cls5 = Class.forName("java.security.spec.PKCS8EncodedKeySpec");
                if (cls.isAssignableFrom(cls4)) {
                    java.security.interfaces.DSAPrivateKey dSAPrivateKey = (java.security.interfaces.DSAPrivateKey) key;
                    DSAParams params2 = dSAPrivateKey.getParams();
                    return cls.cast(new DSAPrivateKeySpec(dSAPrivateKey.getX(), params2.getP(), params2.getQ(), params2.getG()));
                }
                if (cls.isAssignableFrom(cls5)) {
                    return cls.cast(new PKCS8EncodedKeySpec(key.getEncoded()));
                }
                throw new InvalidKeySpecException("Inappropriate key specification");
            }
            throw new InvalidKeySpecException("Inappropriate key type");
        } catch (ClassNotFoundException e2) {
            throw new InvalidKeySpecException("Unsupported key specification: " + e2.getMessage());
        }
    }

    @Override // java.security.KeyFactorySpi
    protected Key engineTranslateKey(Key key) throws InvalidKeyException {
        try {
            if (key instanceof java.security.interfaces.DSAPublicKey) {
                if (key instanceof DSAPublicKey) {
                    return key;
                }
                return engineGeneratePublic((DSAPublicKeySpec) engineGetKeySpec(key, DSAPublicKeySpec.class));
            }
            if (key instanceof java.security.interfaces.DSAPrivateKey) {
                if (key instanceof DSAPrivateKey) {
                    return key;
                }
                return engineGeneratePrivate((DSAPrivateKeySpec) engineGetKeySpec(key, DSAPrivateKeySpec.class));
            }
            throw new InvalidKeyException("Wrong algorithm type");
        } catch (InvalidKeySpecException e2) {
            throw new InvalidKeyException("Cannot translate key: " + e2.getMessage());
        }
    }
}
