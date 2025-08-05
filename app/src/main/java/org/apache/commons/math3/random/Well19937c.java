package org.apache.commons.math3.random;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/Well19937c.class */
public class Well19937c extends AbstractWell {
    private static final long serialVersionUID = -7203498180754925124L;

    /* renamed from: K, reason: collision with root package name */
    private static final int f13089K = 19937;
    private static final int M1 = 70;
    private static final int M2 = 179;
    private static final int M3 = 449;

    public Well19937c() {
        super(f13089K, 70, 179, M3);
    }

    public Well19937c(int seed) {
        super(f13089K, 70, 179, M3, seed);
    }

    public Well19937c(int[] seed) {
        super(f13089K, 70, 179, M3, seed);
    }

    public Well19937c(long seed) {
        super(f13089K, 70, 179, M3, seed);
    }

    @Override // org.apache.commons.math3.random.AbstractWell, org.apache.commons.math3.random.BitsStreamGenerator
    protected int next(int bits) {
        int indexRm1 = this.iRm1[this.index];
        int indexRm2 = this.iRm2[this.index];
        int v0 = this.f13083v[this.index];
        int vM1 = this.f13083v[this.i1[this.index]];
        int vM2 = this.f13083v[this.i2[this.index]];
        int vM3 = this.f13083v[this.i3[this.index]];
        int z0 = (Integer.MIN_VALUE & this.f13083v[indexRm1]) ^ (Integer.MAX_VALUE & this.f13083v[indexRm2]);
        int z1 = (v0 ^ (v0 << 25)) ^ (vM1 ^ (vM1 >>> 27));
        int z2 = (vM2 >>> 9) ^ (vM3 ^ (vM3 >>> 1));
        int z3 = z1 ^ z2;
        int z4 = ((z0 ^ (z1 ^ (z1 << 9))) ^ (z2 ^ (z2 << 21))) ^ (z3 ^ (z3 >>> 21));
        this.f13083v[this.index] = z3;
        this.f13083v[indexRm1] = z4;
        int[] iArr = this.f13083v;
        iArr[indexRm2] = iArr[indexRm2] & Integer.MIN_VALUE;
        this.index = indexRm1;
        int z42 = z4 ^ ((z4 << 7) & (-462547200));
        return (z42 ^ ((z42 << 15) & (-1685684224))) >>> (32 - bits);
    }
}
