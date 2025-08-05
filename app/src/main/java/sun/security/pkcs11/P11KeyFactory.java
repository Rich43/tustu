package sun.security.pkcs11;

import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyFactorySpi;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import sun.security.pkcs11.wrapper.PKCS11Exception;

/* loaded from: sunpkcs11.jar:sun/security/pkcs11/P11KeyFactory.class */
abstract class P11KeyFactory extends KeyFactorySpi {
    final Token token;
    final String algorithm;

    abstract <T extends KeySpec> T implGetPublicKeySpec(P11Key p11Key, Class<T> cls, Session[] sessionArr) throws PKCS11Exception, InvalidKeySpecException;

    abstract <T extends KeySpec> T implGetPrivateKeySpec(P11Key p11Key, Class<T> cls, Session[] sessionArr) throws PKCS11Exception, InvalidKeySpecException;

    abstract PublicKey implTranslatePublicKey(PublicKey publicKey) throws InvalidKeyException;

    abstract PrivateKey implTranslatePrivateKey(PrivateKey privateKey) throws InvalidKeyException;

    abstract KeyFactory implGetSoftwareFactory() throws GeneralSecurityException;

    P11KeyFactory(Token token, String str) {
        this.token = token;
        this.algorithm = str;
    }

    static P11Key convertKey(Token token, Key key, String str) throws InvalidKeyException {
        return (P11Key) token.getKeyFactory(str).engineTranslateKey(key);
    }

    @Override // java.security.KeyFactorySpi
    protected final <T extends KeySpec> T engineGetKeySpec(Key key, Class<T> cls) throws InvalidKeySpecException {
        this.token.ensureValid();
        if (key == null || cls == null) {
            throw new InvalidKeySpecException("key and keySpec must not be null");
        }
        if (cls.isAssignableFrom(PKCS8EncodedKeySpec.class) || cls.isAssignableFrom(X509EncodedKeySpec.class)) {
            try {
                return (T) implGetSoftwareFactory().getKeySpec(key, cls);
            } catch (GeneralSecurityException e2) {
                throw new InvalidKeySpecException("Could not encode key", e2);
            }
        }
        try {
            P11Key p11Key = (P11Key) engineTranslateKey(key);
            Session[] sessionArr = new Session[1];
            try {
                try {
                    if (p11Key.isPublic()) {
                        T t2 = (T) implGetPublicKeySpec(p11Key, cls, sessionArr);
                        sessionArr[0] = this.token.releaseSession(sessionArr[0]);
                        return t2;
                    }
                    T t3 = (T) implGetPrivateKeySpec(p11Key, cls, sessionArr);
                    sessionArr[0] = this.token.releaseSession(sessionArr[0]);
                    return t3;
                } catch (PKCS11Exception e3) {
                    throw new InvalidKeySpecException("Could not generate KeySpec", e3);
                }
            } catch (Throwable th) {
                sessionArr[0] = this.token.releaseSession(sessionArr[0]);
                throw th;
            }
        } catch (InvalidKeyException e4) {
            throw new InvalidKeySpecException("Could not convert key", e4);
        }
    }

    @Override // java.security.KeyFactorySpi
    protected final Key engineTranslateKey(Key key) throws InvalidKeyException {
        this.token.ensureValid();
        if (key == null) {
            throw new InvalidKeyException("Key must not be null");
        }
        if (!key.getAlgorithm().equals(this.algorithm)) {
            throw new InvalidKeyException("Key algorithm must be " + this.algorithm);
        }
        if ((key instanceof P11Key) && ((P11Key) key).token == this.token) {
            return key;
        }
        P11Key p11Key = this.token.privateCache.get(key);
        if (p11Key != null) {
            return p11Key;
        }
        if (key instanceof PublicKey) {
            Key keyImplTranslatePublicKey = implTranslatePublicKey((PublicKey) key);
            this.token.privateCache.put(key, (P11Key) keyImplTranslatePublicKey);
            return keyImplTranslatePublicKey;
        }
        if (key instanceof PrivateKey) {
            Key keyImplTranslatePrivateKey = implTranslatePrivateKey((PrivateKey) key);
            this.token.privateCache.put(key, (P11Key) keyImplTranslatePrivateKey);
            return keyImplTranslatePrivateKey;
        }
        throw new InvalidKeyException("Key must be instance of PublicKey or PrivateKey");
    }
}
