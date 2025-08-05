package java.security.spec;

import java.math.BigInteger;

/* loaded from: rt.jar:java/security/spec/DSAPublicKeySpec.class */
public class DSAPublicKeySpec implements KeySpec {

    /* renamed from: y, reason: collision with root package name */
    private BigInteger f12477y;

    /* renamed from: p, reason: collision with root package name */
    private BigInteger f12478p;

    /* renamed from: q, reason: collision with root package name */
    private BigInteger f12479q;

    /* renamed from: g, reason: collision with root package name */
    private BigInteger f12480g;

    public DSAPublicKeySpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3, BigInteger bigInteger4) {
        this.f12477y = bigInteger;
        this.f12478p = bigInteger2;
        this.f12479q = bigInteger3;
        this.f12480g = bigInteger4;
    }

    public BigInteger getY() {
        return this.f12477y;
    }

    public BigInteger getP() {
        return this.f12478p;
    }

    public BigInteger getQ() {
        return this.f12479q;
    }

    public BigInteger getG() {
        return this.f12480g;
    }
}
