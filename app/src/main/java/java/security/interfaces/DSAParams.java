package java.security.interfaces;

import java.math.BigInteger;

/* loaded from: rt.jar:java/security/interfaces/DSAParams.class */
public interface DSAParams {
    BigInteger getP();

    BigInteger getQ();

    BigInteger getG();
}
