package java.security.spec;

import java.math.BigInteger;

/* loaded from: rt.jar:java/security/spec/DSAPrivateKeySpec.class */
public class DSAPrivateKeySpec implements KeySpec {

    /* renamed from: x, reason: collision with root package name */
    private BigInteger f12473x;

    /* renamed from: p, reason: collision with root package name */
    private BigInteger f12474p;

    /* renamed from: q, reason: collision with root package name */
    private BigInteger f12475q;

    /* renamed from: g, reason: collision with root package name */
    private BigInteger f12476g;

    public DSAPrivateKeySpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
        this.f12473x = bigInteger;
        this.f12474p = bigInteger2;
        this.f12475q = bigInteger3;
        this.f12476g = bigInteger4;
    }

    public BigInteger getX() {
        return this.f12473x;
    }

    public BigInteger getP() {
        return this.f12474p;
    }

    public BigInteger getQ() {
        return this.f12475q;
    }

    public BigInteger getG() {
        return this.f12476g;
    }
}
