package java.security.interfaces;

import java.math.BigInteger;
import java.security.spec.RSAOtherPrimeInfo;

/* loaded from: rt.jar:java/security/interfaces/RSAMultiPrimePrivateCrtKey.class */
public interface RSAMultiPrimePrivateCrtKey extends RSAPrivateKey {
    public static final long serialVersionUID = 618058533534628008L;

    BigInteger getPublicExponent();

    BigInteger getPrimeP();

    BigInteger getPrimeQ();

    BigInteger getPrimeExponentP();

    BigInteger getPrimeExponentQ();

    BigInteger getCrtCoefficient();

    RSAOtherPrimeInfo[] getOtherPrimeInfo();
}
