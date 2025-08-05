package java.security.spec;

import java.math.BigInteger;

/* loaded from: rt.jar:java/security/spec/ECPrivateKeySpec.class */
public class ECPrivateKeySpec implements KeySpec {

    /* renamed from: s, reason: collision with root package name */
    private BigInteger f12488s;
    private ECParameterSpec params;

    public ECPrivateKeySpec(BigInteger bigInteger, ECParameterSpec eCParameterSpec) {
        if (bigInteger == null) {
            throw new NullPointerException("s is null");
        }
        if (eCParameterSpec == null) {
            throw new NullPointerException("params is null");
        }
        this.f12488s = bigInteger;
        this.params = eCParameterSpec;
    }

    public BigInteger getS() {
        return this.f12488s;
    }

    public ECParameterSpec getParams() {
        return this.params;
    }
}
