package java.security.spec;

import java.math.BigInteger;

/* loaded from: rt.jar:java/security/spec/ECPoint.class */
public class ECPoint {

    /* renamed from: x, reason: collision with root package name */
    private final BigInteger f12486x;

    /* renamed from: y, reason: collision with root package name */
    private final BigInteger f12487y;
    public static final ECPoint POINT_INFINITY = new ECPoint();

    private ECPoint() {
        this.f12486x = null;
        this.f12487y = null;
    }

    public ECPoint(BigInteger bigInteger, BigInteger bigInteger2) {
        if (bigInteger == null || bigInteger2 == null) {
            throw new NullPointerException("affine coordinate x or y is null");
        }
        this.f12486x = bigInteger;
        this.f12487y = bigInteger2;
    }

    public BigInteger getAffineX() {
        return this.f12486x;
    }

    public BigInteger getAffineY() {
        return this.f12487y;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return this != POINT_INFINITY && (obj instanceof ECPoint) && this.f12486x.equals(((ECPoint) obj).f12486x) && this.f12487y.equals(((ECPoint) obj).f12487y);
    }

    public int hashCode() {
        if (this == POINT_INFINITY) {
            return 0;
        }
        return this.f12486x.hashCode() << (5 + this.f12487y.hashCode());
    }
}
