package java.security.interfaces;

import java.math.BigInteger;
import java.security.PrivateKey;

/* loaded from: rt.jar:java/security/interfaces/DSAPrivateKey.class */
public interface DSAPrivateKey extends DSAKey, PrivateKey {
    public static final long serialVersionUID = 7776497482533790279L;

    BigInteger getX();
}
