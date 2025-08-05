package org.apache.commons.math3.random;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/Well1024a.class */
public class Well1024a extends AbstractWell {
    private static final long serialVersionUID = 5680173464174485492L;

    /* renamed from: K, reason: collision with root package name */
    private static final int f13087K = 1024;
    private static final int M1 = 3;
    private static final int M2 = 24;
    private static final int M3 = 10;

    public Well1024a() {
        super(1024, 3, 24, 10);
    }

    public Well1024a(int seed) {
        super(1024, 3, 24, 10, seed);
    }

    public Well1024a(int[] seed) {
        super(1024, 3, 24, 10, seed);
    }

    public Well1024a(long seed) {
        super(1024, 3, 24, 10, seed);
    }

    @Override // org.apache.commons.math3.random.AbstractWell, org.apache.commons.math3.random.BitsStreamGenerator
    protected int next(int bits) {
        int indexRm1 = this.iRm1[this.index];
        int v0 = this.f13083v[this.index];
        int vM1 = this.f13083v[this.i1[this.index]];
        int vM2 = this.f13083v[this.i2[this.index]];
        int vM3 = this.f13083v[this.i3[this.index]];
        int z0 = this.f13083v[indexRm1];
        int z1 = v0 ^ (vM1 ^ (vM1 >>> 8));
        int z2 = (vM2 ^ (vM2 << 19)) ^ (vM3 ^ (vM3 << 14));
        int z3 = z1 ^ z2;
        int z4 = ((z0 ^ (z0 << 11)) ^ (z1 ^ (z1 << 7))) ^ (z2 ^ (z2 << 13));
        this.f13083v[this.index] = z3;
        this.f13083v[indexRm1] = z4;
        this.index = indexRm1;
        return z4 >>> (32 - bits);
    }
}
