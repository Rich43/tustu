package java.security.spec;

import java.math.BigInteger;
import java.security.interfaces.DSAParams;

/* loaded from: rt.jar:java/security/spec/DSAParameterSpec.class */
public class DSAParameterSpec implements AlgorithmParameterSpec, DSAParams {

    /* renamed from: p, reason: collision with root package name */
    BigInteger f12470p;

    /* renamed from: q, reason: collision with root package name */
    BigInteger f12471q;

    /* renamed from: g, reason: collision with root package name */
    BigInteger f12472g;

    public DSAParameterSpec(BigInteger bigInteger, BigInteger bigInteger2, BigInteger bigInteger3) {
        this.f12470p = bigInteger;
        this.f12471q = bigInteger2;
        this.f12472g = bigInteger3;
    }

    @Override // java.security.interfaces.DSAParams
    public BigInteger getP() {
        return this.f12470p;
    }

    @Override // java.security.interfaces.DSAParams
    public BigInteger getQ() {
        return this.f12471q;
    }

    @Override // java.security.interfaces.DSAParams
    public BigInteger getG() {
        return this.f12472g;
    }
}
