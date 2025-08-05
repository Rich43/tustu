package javax.crypto.spec;

import java.math.BigInteger;
import java.security.spec.AlgorithmParameterSpec;

/* loaded from: jce.jar:javax/crypto/spec/DHParameterSpec.class */
public class DHParameterSpec implements AlgorithmParameterSpec {

    /* renamed from: p, reason: collision with root package name */
    private BigInteger f12769p;

    /* renamed from: g, reason: collision with root package name */
    private BigInteger f12770g;

    /* renamed from: l, reason: collision with root package name */
    private int f12771l;

    public DHParameterSpec(BigInteger bigInteger, BigInteger bigInteger2) {
        this.f12769p = bigInteger;
        this.f12770g = bigInteger2;
        this.f12771l = 0;
    }

    public DHParameterSpec(BigInteger bigInteger, BigInteger bigInteger2, int i2) {
        this.f12769p = bigInteger;
        this.f12770g = bigInteger2;
        this.f12771l = i2;
    }

    public BigInteger getP() {
        return this.f12769p;
    }

    public BigInteger getG() {
        return this.f12770g;
    }

    public int getL() {
        return this.f12771l;
    }
}
