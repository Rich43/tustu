package java.security;

import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/* loaded from: rt.jar:java/security/KeyFactorySpi.class */
public abstract class KeyFactorySpi {
    protected abstract PublicKey engineGeneratePublic(KeySpec keySpec) throws InvalidKeySpecException;

    protected abstract PrivateKey engineGeneratePrivate(KeySpec keySpec) throws InvalidKeySpecException;

    protected abstract <T extends KeySpec> T engineGetKeySpec(Key key, Class<T> cls) throws InvalidKeySpecException;

    protected abstract Key engineTranslateKey(Key key) throws InvalidKeyException;
}
