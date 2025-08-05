package java.security.spec;

import java.math.BigInteger;
import java.util.Objects;

/* loaded from: rt.jar:java/security/spec/RSAMultiPrimePrivateCrtKeySpec.class */
public class RSAMultiPrimePrivateCrtKeySpec extends RSAPrivateKeySpec {
    private final BigInteger publicExponent;
    private final BigInteger primeP;
    private final BigInteger primeQ;
    private final BigInteger primeExponentP;
    private final BigInteger primeExponentQ;
    private final BigInteger crtCoefficient;
    private final RSAOtherPrimeInfo[] otherPrimeInfo;

    public RSAMultiPrimePrivateCrtKeySpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5, BigInteger bigInteger6, BigInteger bigInteger7, BigInteger bigInteger8, RSAOtherPrimeInfo[] rSAOtherPrimeInfoArr) {
        this(bigInteger, bigInteger2, bigInteger3, bigInteger4, bigInteger5, bigInteger6, bigInteger7, bigInteger8, rSAOtherPrimeInfoArr, null);
    }

    public RSAMultiPrimePrivateCrtKeySpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4, BigInteger bigInteger5, BigInteger bigInteger6, BigInteger bigInteger7, BigInteger bigInteger8, RSAOtherPrimeInfo[] rSAOtherPrimeInfoArr, AlgorithmParameterSpec algorithmParameterSpec) {
        super(bigInteger, bigInteger3, algorithmParameterSpec);
        Objects.requireNonNull(bigInteger, "the modulus parameter must be non-null");
        Objects.requireNonNull(bigInteger3, "the privateExponent parameter must be non-null");
        this.publicExponent = (BigInteger) Objects.requireNonNull(bigInteger2, "the publicExponent parameter must be non-null");
        this.primeP = (BigInteger) Objects.requireNonNull(bigInteger4, "the primeP parameter must be non-null");
        this.primeQ = (BigInteger) Objects.requireNonNull(bigInteger5, "the primeQ parameter must be non-null");
        this.primeExponentP = (BigInteger) Objects.requireNonNull(bigInteger6, "the primeExponentP parameter must be non-null");
        this.primeExponentQ = (BigInteger) Objects.requireNonNull(bigInteger7, "the primeExponentQ parameter must be non-null");
        this.crtCoefficient = (BigInteger) Objects.requireNonNull(bigInteger8, "the crtCoefficient parameter must be non-null");
        if (rSAOtherPrimeInfoArr == null) {
            this.otherPrimeInfo = null;
        } else {
            if (rSAOtherPrimeInfoArr.length == 0) {
                throw new IllegalArgumentException("the otherPrimeInfo parameter must not be empty");
            }
            this.otherPrimeInfo = (RSAOtherPrimeInfo[]) rSAOtherPrimeInfoArr.clone();
        }
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

    public RSAOtherPrimeInfo[] getOtherPrimeInfo() {
        if (this.otherPrimeInfo == null) {
            return null;
        }
        return (RSAOtherPrimeInfo[]) this.otherPrimeInfo.clone();
    }
}
