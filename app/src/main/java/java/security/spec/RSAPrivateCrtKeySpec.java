package java.security.spec;

import java.math.BigInteger;

/* loaded from: rt.jar:java/security/spec/RSAPrivateCrtKeySpec.class */
public class RSAPrivateCrtKeySpec extends RSAPrivateKeySpec {
    private final BigInteger publicExponent;
    private final BigInteger primeP;
    private final BigInteger primeQ;
    private final BigInteger primeExponentP;
    private final BigInteger primeExponentQ;
    private final BigInteger crtCoefficient;

    public RSAPrivateCrtKeySpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5, BigInteger bigInteger6, BigInteger bigInteger7, BigInteger bigInteger8) {
        this(bigInteger, bigInteger2, bigInteger3, bigInteger4, bigInteger5, bigInteger6, bigInteger7, bigInteger8, null);
    }

    public RSAPrivateCrtKeySpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5, BigInteger bigInteger6, BigInteger bigInteger7, BigInteger bigInteger8, AlgorithmParameterSpec algorithmParameterSpec) {
        super(bigInteger, bigInteger3, algorithmParameterSpec);
        this.publicExponent = bigInteger2;
        this.primeP = bigInteger4;
        this.primeQ = bigInteger5;
        this.primeExponentP = bigInteger6;
        this.primeExponentQ = bigInteger7;
        this.crtCoefficient = bigInteger8;
    }

    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }

    public BigInteger getPrimeP() {
        return this.primeP;
    }

    public BigInteger getPrimeQ() {
        return this.primeQ;
    }

    public BigInteger getPrimeExponentP() {
        return this.primeExponentP;
    }

    public BigInteger getPrimeExponentQ() {
        return this.primeExponentQ;
    }

    public BigInteger getCrtCoefficient() {
        return this.crtCoefficient;
    }
}
