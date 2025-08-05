package java.security.spec;

import java.math.BigInteger;

/* loaded from: rt.jar:java/security/spec/ECFieldFp.class */
public class ECFieldFp implements ECField {

    /* renamed from: p, reason: collision with root package name */
    private BigInteger f12482p;

    public ECFieldFp(BigInteger bigInteger) {
        if (bigInteger.signum() != 1) {
            throw new IllegalArgumentException("p is not positive");
        }
        this.f12482p = bigInteger;
    }

    @Override // java.security.spec.ECField
    public int getFieldSize() {
        return this.f12482p.bitLength();
    }

    public BigInteger getP() {
        return this.f12482p;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof ECFieldFp) {
            return this.f12482p.equals(((ECFieldFp) obj).f12482p);
        }
        return false;
    }

    public int hashCode() {
        return this.f12482p.hashCode();
    }
}
