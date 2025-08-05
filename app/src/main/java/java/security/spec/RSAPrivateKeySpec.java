package java.security.spec;

import java.math.BigInteger;

/* loaded from: rt.jar:java/security/spec/RSAPrivateKeySpec.class */
public class RSAPrivateKeySpec implements KeySpec {
    private final BigInteger modulus;
    private final BigInteger privateExponent;
    private final AlgorithmParameterSpec params;

    public RSAPrivateKeySpec(BigInteger bigInteger, BigInteger bigInteger2) {
        this(bigInteger, bigInteger2, null);
    }

    public RSAPrivateKeySpec(BigInteger bigInteger, BigInteger bigInteger2, AlgorithmParameterSpec algorithmParameterSpec) {
        this.modulus = bigInteger;
        this.privateExponent = bigInteger2;
        this.params = algorithmParameterSpec;
    }

    public BigInteger getModulus() {
        return this.modulus;
    }

    public BigInteger getPrivateExponent() {
        return this.privateExponent;
    }

    public AlgorithmParameterSpec getParams() {
        return this.params;
    }
}
