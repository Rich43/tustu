package java.security.interfaces;

import java.math.BigInteger;
import java.security.PrivateKey;

/* loaded from: rt.jar:java/security/interfaces/RSAPrivateKey.class */
public interface RSAPrivateKey extends PrivateKey, RSAKey {
    public static final long serialVersionUID = 5187144804936595022L;

    BigInteger getPrivateExponent();
}
