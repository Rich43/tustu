package javax.crypto.spec;

import java.math.BigInteger;
import java.security.spec.KeySpec;

/* loaded from: jce.jar:javax/crypto/spec/DHPublicKeySpec.class */
public class DHPublicKeySpec implements KeySpec {

    /* renamed from: y, reason: collision with root package name */
    private BigInteger f12775y;

    /* renamed from: p, reason: collision with root package name */
    private BigInteger f12776p;

    /* renamed from: g, reason: collision with root package name */
    private BigInteger f12777g;

    public DHPublicKeySpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        this.f12775y = bigInteger;
        this.f12776p = bigInteger2;
        this.f12777g = bigInteger3;
    }

    public BigInteger getY() {
        return this.f12775y;
    }

    public BigInteger getP() {
        return this.f12776p;
    }

    public BigInteger getG() {
        return this.f12777g;
    }
}
