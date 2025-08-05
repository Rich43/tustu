package java.security.spec;

import java.math.BigInteger;

/* loaded from: rt.jar:java/security/spec/ECParameterSpec.class */
public class ECParameterSpec implements AlgorithmParameterSpec {
    private final EllipticCurve curve;

    /* renamed from: g, reason: collision with root package name */
    private final ECPoint f12483g;

    /* renamed from: n, reason: collision with root package name */
    private final BigInteger f12484n;

    /* renamed from: h, reason: collision with root package name */
    private final int f12485h;

    public ECParameterSpec(EllipticCurve ellipticCurve, ECPoint eCPoint, BigInteger bigInteger, int i2) {
        if (ellipticCurve == null) {
            throw new NullPointerException("curve is null");
        }
        if (eCPoint == null) {
            throw new NullPointerException("g is null");
        }
        if (bigInteger == null) {
            throw new NullPointerException("n is null");
        }
        if (bigInteger.signum() != 1) {
            throw new IllegalArgumentException("n is not positive");
        }
        if (i2 <= 0) {
            throw new IllegalArgumentException("h is not positive");
        }
        this.curve = ellipticCurve;
        this.f12483g = eCPoint;
        this.f12484n = bigInteger;
        this.f12485h = i2;
    }

    public EllipticCurve getCurve() {
        return this.curve;
    }

    public ECPoint getGenerator() {
        return this.f12483g;
    }

    public BigInteger getOrder() {
        return this.f12484n;
    }

    public int getCofactor() {
        return this.f12485h;
    }
}
