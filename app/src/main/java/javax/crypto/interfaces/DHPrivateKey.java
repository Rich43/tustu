package javax.crypto.interfaces;

import java.math.BigInteger;
import java.security.PrivateKey;

/* loaded from: jce.jar:javax/crypto/interfaces/DHPrivateKey.class */
public interface DHPrivateKey extends DHKey, PrivateKey {
    public static final long serialVersionUID = 2211791113380396553L;

    BigInteger getX();
}
