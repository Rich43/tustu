package org.apache.commons.math3.random;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/Well44497a.class */
public class Well44497a extends AbstractWell {
    private static final long serialVersionUID = -3859207588353972099L;

    /* renamed from: K, reason: collision with root package name */
    private static final int f13090K = 44497;
    private static final int M1 = 23;
    private static final int M2 = 481;
    private static final int M3 = 229;

    public Well44497a() {
        super(f13090K, 23, M2, 229);
    }

    public Well44497a(int seed) {
        super(f13090K, 23, M2, 229, seed);
    }

    public Well44497a(int[] seed) {
        super(f13090K, 23, M2, 229, seed);
    }

    public Well44497a(long seed) {
        super(f13090K, 23, M2, 229, seed);
    }

    @Override // org.apache.commons.math3.random.AbstractWell, org.apache.commons.math3.random.BitsStreamGenerator
    protected int next(int bits) {
        int indexRm1 = this.iRm1[this.index];
        int indexRm2 = this.iRm2[this.index];
        int v0 = this.f13083v[this.index];
        int vM1 = this.f13083v[this.i1[this.index]];
        int vM2 = this.f13083v[this.i2[this.index]];
        int vM3 = this.f13083v[this.i3[this.index]];
        int z0 = ((-32768) & this.f13083v[indexRm1]) ^ (32767 & this.f13083v[indexRm2]);
        int z1 = (v0 ^ (v0 << 24)) ^ (vM1 ^ (vM1 >>> 30));
        int z2 = (vM2 ^ (vM2 << 10)) ^ (vM3 << 26);
        int z3 = z1 ^ z2;
        int z2Prime = ((z2 << 9) ^ (z2 >>> 23)) & (-67108865);
        int z2Second = (z2 & 131072) != 0 ? z2Prime ^ (-1221985044) : z2Prime;
        int z4 = ((z0 ^ (z1 ^ (z1 >>> 20))) ^ z2Second) ^ z3;
        this.f13083v[this.index] = z3;
        this.f13083v[indexRm1] = z4;
        int[] iArr = this.f13083v;
        iArr[indexRm2] = iArr[indexRm2] & Short.MIN_VALUE;
        this.index = indexRm1;
        return z4 >>> (32 - bits);
    }
}
