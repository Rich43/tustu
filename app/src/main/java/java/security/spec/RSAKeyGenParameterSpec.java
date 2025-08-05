package java.security.spec;

import java.math.BigInteger;

/* loaded from: rt.jar:java/security/spec/RSAKeyGenParameterSpec.class */
public class RSAKeyGenParameterSpec implements AlgorithmParameterSpec {
    private int keysize;
    private BigInteger publicExponent;
    private AlgorithmParameterSpec keyParams;
    public static final BigInteger F0 = BigInteger.valueOf(3);
    public static final BigInteger F4 = BigInteger.valueOf(65537);

    public RSAKeyGenParameterSpec(int i2, BigInteger bigInteger) {
        this(i2, bigInteger, null);
    }

    public RSAKeyGenParameterSpec(int i2, BigInteger bigInteger, AlgorithmParameterSpec algorithmParameterSpec) {
        this.keysize = i2;
        this.publicExponent = bigInteger;
        this.keyParams = algorithmParameterSpec;
    }

    public int getKeysize() {
        return this.keysize;
    }

    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }

    public AlgorithmParameterSpec getKeyParams() {
        return this.keyParams;
    }
}
