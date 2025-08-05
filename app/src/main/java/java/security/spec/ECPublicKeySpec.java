package java.security.spec;

/* loaded from: rt.jar:java/security/spec/ECPublicKeySpec.class */
public class ECPublicKeySpec implements KeySpec {

    /* renamed from: w, reason: collision with root package name */
    private ECPoint f12489w;
    private ECParameterSpec params;

    public ECPublicKeySpec(ECPoint eCPoint, ECParameterSpec eCParameterSpec) {
        if (eCPoint == null) {
            throw new NullPointerException("w is null");
        }
        if (eCParameterSpec == null) {
            throw new NullPointerException("params is null");
        }
        if (eCPoint == ECPoint.POINT_INFINITY) {
            throw new IllegalArgumentException("w is ECPoint.POINT_INFINITY");
        }
        this.f12489w = eCPoint;
        this.params = eCParameterSpec;
    }

    public ECPoint getW() {
        return this.f12489w;
    }

    public ECParameterSpec getParams() {
        return this.params;
    }
}
