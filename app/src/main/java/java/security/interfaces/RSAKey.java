package java.security.interfaces;

import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;

/* loaded from: rt.jar:java/security/interfaces/RSAKey.class */
public interface RSAKey {
    BigInteger getModulus();

    default AlgorithmParameterSpec getParams() {
        return null;
    }
}
