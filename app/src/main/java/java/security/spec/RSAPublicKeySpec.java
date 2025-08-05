package java.security.spec;

import java.math.BigInteger;

/* loaded from: rt.jar:java/security/spec/RSAPublicKeySpec.class */
public class RSAPublicKeySpec implements KeySpec {
    private final BigInteger modulus;
    private final BigInteger publicExponent;
    private final AlgorithmParameterSpec params;

    public RSAPublicKeySpec(BigInteger bigInteger, BigInteger bigInteger2) {
        this(bigInteger, bigInteger2, null);
    }

    public RSAPublicKeySpec(BigInteger bigInteger, BigInteger bigInteger2, AlgorithmParameterSpec algorithmParameterSpec) {
        this.modulus = bigInteger;
        this.publicExponent = bigInteger2;
        this.params = algorithmParameterSpec;
    }

    public BigInteger getModulus() {
        return this.modulus;
    }

    public BigInteger getPublicExponent() {
        return this.publicExponent;
    }

    public AlgorithmParameterSpec getParams() {
        return this.params;
    }
}
