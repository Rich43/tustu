package java.security.spec;

import java.math.BigInteger;

/* loaded from: rt.jar:java/security/spec/RSAOtherPrimeInfo.class */
public class RSAOtherPrimeInfo {
    private BigInteger prime;
    private BigInteger primeExponent;
    private BigInteger crtCoefficient;

    public RSAOtherPrimeInfo(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        if (bigInteger == null) {
            throw new NullPointerException("the prime parameter must be non-null");
        }
        if (bigInteger2 == null) {
            throw new NullPointerException("the primeExponent parameter must be non-null");
        }
        if (bigInteger3 == null) {
            throw new NullPointerException("the crtCoefficient parameter must be non-null");
        }
        this.prime = bigInteger;
        this.primeExponent = bigInteger2;
        this.crtCoefficient = bigInteger3;
    }

    public final BigInteger getPrime() {
        return this.prime;
    }

    public final BigInteger getExponent() {
        return this.primeExponent;
    }

    public final BigInteger getCrtCoefficient() {
        return this.crtCoefficient;
    }
}
