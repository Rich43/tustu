package java.security.spec;

import java.math.BigInteger;

/* loaded from: rt.jar:java/security/spec/EllipticCurve.class */
public class EllipticCurve {
    private final ECField field;

    /* renamed from: a, reason: collision with root package name */
    private final BigInteger f12490a;

    /* renamed from: b, reason: collision with root package name */
    private final BigInteger f12491b;
    private final byte[] seed;

    private static void checkValidity(ECField eCField, BigInteger bigInteger, String str) {
        if (eCField instanceof ECFieldFp) {
            if (((ECFieldFp) eCField).getP().compareTo(bigInteger) != 1) {
                throw new IllegalArgumentException(str + " is too large");
            }
            if (bigInteger.signum() < 0) {
                throw new IllegalArgumentException(str + " is negative");
            }
            return;
        }
        if (eCField instanceof ECFieldF2m) {
            if (bigInteger.bitLength() > ((ECFieldF2m) eCField).getM()) {
                throw new IllegalArgumentException(str + " is too large");
            }
        }
    }

    public EllipticCurve(ECField eCField, BigInteger bigInteger, BigInteger bigInteger2) {
        this(eCField, bigInteger, bigInteger2, null);
    }

    public EllipticCurve(ECField eCField, BigInteger bigInteger, BigInteger bigInteger2, byte[] bArr) {
        if (eCField == null) {
            throw new NullPointerException("field is null");
        }
        if (bigInteger == null) {
            throw new NullPointerException("first coefficient is null");
        }
        if (bigInteger2 == null) {
            throw new NullPointerException("second coefficient is null");
        }
        checkValidity(eCField, bigInteger, "first coefficient");
        checkValidity(eCField, bigInteger2, "second coefficient");
        this.field = eCField;
        this.f12490a = bigInteger;
        this.f12491b = bigInteger2;
        if (bArr != null) {
            this.seed = (byte[]) bArr.clone();
        } else {
            this.seed = null;
        }
    }

    public ECField getField() {
        return this.field;
    }

    public BigInteger getA() {
        return this.f12490a;
    }

    public BigInteger getB() {
        return this.f12491b;
    }

    public byte[] getSeed() {
        if (this.seed == null) {
            return null;
        }
        return (byte[]) this.seed.clone();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof EllipticCurve) {
            EllipticCurve ellipticCurve = (EllipticCurve) obj;
            if (this.field.equals(ellipticCurve.field) && this.f12490a.equals(ellipticCurve.f12490a) && this.f12491b.equals(ellipticCurve.f12491b)) {
                return true;
            }
            return false;
        }
        return false;
    }

    public int hashCode() {
        return this.field.hashCode() << ((6 + (this.f12490a.hashCode() << 4)) + (this.f12491b.hashCode() << 2));
    }
}
