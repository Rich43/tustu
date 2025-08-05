package javax.crypto.spec;

import java.math.BigInteger;
import java.security.spec.KeySpec;

/* loaded from: jce.jar:javax/crypto/spec/DHPrivateKeySpec.class */
public class DHPrivateKeySpec implements KeySpec {

    /* renamed from: x, reason: collision with root package name */
    private BigInteger f12772x;

    /* renamed from: p, reason: collision with root package name */
    private BigInteger f12773p;

    /* renamed from: g, reason: collision with root package name */
    private BigInteger f12774g;

    public DHPrivateKeySpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        this.f12772x = bigInteger;
        this.f12773p = bigInteger2;
        this.f12774g = bigInteger3;
    }

    public BigInteger getX() {
        return this.f12772x;
    }

    public BigInteger getP() {
        return this.f12773p;
    }

    public BigInteger getG() {
        return this.f12774g;
    }
}
