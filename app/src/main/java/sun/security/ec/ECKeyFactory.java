package sun.security.ec;

import com.sun.org.apache.xml.internal.security.keys.content.x509.XMLX509Certificate;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyFactorySpi;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.interfaces.ECKey;
import java.security.interfaces.ECPrivateKey;
import java.security.interfaces.ECPublicKey;
import java.security.spec.ECPrivateKeySpec;
import java.security.spec.ECPublicKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/* loaded from: sunec.jar:sun/security/ec/ECKeyFactory.class */
public final class ECKeyFactory extends KeyFactorySpi {
    private static KeyFactory instance;

    private static KeyFactory getInstance() {
        if (instance == null) {
            try {
                instance = KeyFactory.getInstance("EC", "SunEC");
            } catch (NoSuchAlgorithmException e2) {
                throw new RuntimeException(e2);
            } catch (NoSuchProviderException e3) {
                throw new RuntimeException(e3);
            }
        }
        return instance;
    }

    public static ECKey toECKey(Key key) throws InvalidKeyException {
        if (key instanceof ECKey) {
            ECKey eCKey = (ECKey) key;
            checkKey(eCKey);
            return eCKey;
        }
        return (ECKey) getInstance().translateKey(key);
    }

    private static void checkKey(ECKey eCKey) throws InvalidKeyException {
        if (eCKey instanceof ECPublicKey) {
            if (eCKey instanceof ECPublicKeyImpl) {
                return;
            }
        } else if (eCKey instanceof ECPrivateKey) {
            if (eCKey instanceof ECPrivateKeyImpl) {
                return;
            }
        } else {
            throw new InvalidKeyException("Neither a public nor a private key");
        }
        String algorithm = ((Key) eCKey).getAlgorithm();
        if (!algorithm.equals("EC")) {
            throw new InvalidKeyException("Not an EC key: " + algorithm);
        }
    }

    @Override // java.security.KeyFactorySpi
    protected Key engineTranslateKey(Key key) throws InvalidKeyException {
        if (key == null) {
            throw new InvalidKeyException("Key must not be null");
        }
        String algorithm = key.getAlgorithm();
        if (!algorithm.equals("EC")) {
            throw new InvalidKeyException("Not an EC key: " + algorithm);
        }
        if (key instanceof PublicKey) {
            return implTranslatePublicKey((PublicKey) key);
        }
        if (key instanceof PrivateKey) {
            return implTranslatePrivateKey((PrivateKey) key);
        }
        throw new InvalidKeyException("Neither a public nor a private key");
    }

    @Override // java.security.KeyFactorySpi
    protected PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException {
        try {
            return implGeneratePublic(keySpec);
        } catch (InvalidKeySpecException e2) {
            throw e2;
        } catch (GeneralSecurityException e3) {
            throw new InvalidKeySpecException(e3);
        }
    }

    @Override // java.security.KeyFactorySpi
    protected PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException {
        try {
            return implGeneratePrivate(keySpec);
        } catch (InvalidKeySpecException e2) {
            throw e2;
        } catch (GeneralSecurityException e3) {
            throw new InvalidKeySpecException(e3);
        }
    }

    private PublicKey implTranslatePublicKey(PublicKey publicKey) throws InvalidKeyException {
        if (publicKey instanceof ECPublicKey) {
            if (publicKey instanceof ECPublicKeyImpl) {
                return publicKey;
            }
            ECPublicKey eCPublicKey = (ECPublicKey) publicKey;
            return new ECPublicKeyImpl(eCPublicKey.getW(), eCPublicKey.getParams());
        }
        if (XMLX509Certificate.JCA_CERT_ID.equals(publicKey.getFormat())) {
            return new ECPublicKeyImpl(publicKey.getEncoded());
        }
        throw new InvalidKeyException("Public keys must be instance of ECPublicKey or have X.509 encoding");
    }

    private PrivateKey implTranslatePrivateKey(PrivateKey privateKey) throws InvalidKeyException {
        if (privateKey instanceof ECPrivateKey) {
            if (privateKey instanceof ECPrivateKeyImpl) {
                return privateKey;
            }
            ECPrivateKey eCPrivateKey = (ECPrivateKey) privateKey;
            return new ECPrivateKeyImpl(eCPrivateKey.getS(), eCPrivateKey.getParams());
        }
        if ("PKCS#8".equals(privateKey.getFormat())) {
            return new ECPrivateKeyImpl(privateKey.getEncoded());
        }
        throw new InvalidKeyException("Private keys must be instance of ECPrivateKey or have PKCS#8 encoding");
    }

    private PublicKey implGeneratePublic(KeySpec keySpec) throws GeneralSecurityException {
        if (keySpec instanceof X509EncodedKeySpec) {
            return new ECPublicKeyImpl(((X509EncodedKeySpec) keySpec).getEncoded());
        }
        if (keySpec instanceof ECPublicKeySpec) {
            ECPublicKeySpec eCPublicKeySpec = (ECPublicKeySpec) keySpec;
            return new ECPublicKeyImpl(eCPublicKeySpec.getW(), eCPublicKeySpec.getParams());
        }
        throw new InvalidKeySpecException("Only ECPublicKeySpec and X509EncodedKeySpec supported for EC public keys");
    }

    private PrivateKey implGeneratePrivate(KeySpec keySpec) throws GeneralSecurityException {
        if (keySpec instanceof PKCS8EncodedKeySpec) {
            return new ECPrivateKeyImpl(((PKCS8EncodedKeySpec) keySpec).getEncoded());
        }
        if (keySpec instanceof ECPrivateKeySpec) {
            ECPrivateKeySpec eCPrivateKeySpec = (ECPrivateKeySpec) keySpec;
            return new ECPrivateKeyImpl(eCPrivateKeySpec.getS(), eCPrivateKeySpec.getParams());
        }
        throw new InvalidKeySpecException("Only ECPrivateKeySpec and PKCS8EncodedKeySpec supported for EC private keys");
    }

    @Override // java.security.KeyFactorySpi
    protected <T extends KeySpec> T engineGetKeySpec(Key key, Class<T> cls) throws InvalidKeySpecException {
        try {
            Key keyEngineTranslateKey = engineTranslateKey(key);
            if (keyEngineTranslateKey instanceof ECPublicKey) {
                ECPublicKey eCPublicKey = (ECPublicKey) keyEngineTranslateKey;
                if (cls.isAssignableFrom(ECPublicKeySpec.class)) {
                    return cls.cast(new ECPublicKeySpec(eCPublicKey.getW(), eCPublicKey.getParams()));
                }
                if (cls.isAssignableFrom(X509EncodedKeySpec.class)) {
                    return cls.cast(new X509EncodedKeySpec(keyEngineTranslateKey.getEncoded()));
                }
                throw new InvalidKeySpecException("KeySpec must be ECPublicKeySpec or X509EncodedKeySpec for EC public keys");
            }
            if (keyEngineTranslateKey instanceof ECPrivateKey) {
                if (cls.isAssignableFrom(PKCS8EncodedKeySpec.class)) {
                    return cls.cast(new PKCS8EncodedKeySpec(keyEngineTranslateKey.getEncoded()));
                }
                if (cls.isAssignableFrom(ECPrivateKeySpec.class)) {
                    ECPrivateKey eCPrivateKey = (ECPrivateKey) keyEngineTranslateKey;
                    return cls.cast(new ECPrivateKeySpec(eCPrivateKey.getS(), eCPrivateKey.getParams()));
                }
                throw new InvalidKeySpecException("KeySpec must be ECPrivateKeySpec or PKCS8EncodedKeySpec for EC private keys");
            }
            throw new InvalidKeySpecException("Neither public nor private key");
        } catch (InvalidKeyException e2) {
            throw new InvalidKeySpecException(e2);
        }
    }
}
