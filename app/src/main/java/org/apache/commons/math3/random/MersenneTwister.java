package org.apache.commons.math3.random;

import java.io.Serializable;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/MersenneTwister.class */
public class MersenneTwister extends BitsStreamGenerator implements Serializable {
    private static final long serialVersionUID = 8661194735290153518L;

    /* renamed from: N, reason: collision with root package name */
    private static final int f13084N = 624;

    /* renamed from: M, reason: collision with root package name */
    private static final int f13085M = 397;
    private static final int[] MAG01 = {0, -1727483681};
    private int[] mt;
    private int mti;

    public MersenneTwister() {
        this.mt = new int[f13084N];
        setSeed(System.currentTimeMillis() + System.identityHashCode(this));
    }

    public MersenneTwister(int seed) {
        this.mt = new int[f13084N];
        setSeed(seed);
    }

    public MersenneTwister(int[] seed) {
        this.mt = new int[f13084N];
        setSeed(seed);
    }

    public MersenneTwister(long seed) {
        this.mt = new int[f13084N];
        setSeed(seed);
    }

    @Override // org.apache.commons.math3.random.BitsStreamGenerator, org.apache.commons.math3.random.RandomGenerator
    public void setSeed(int seed) {
        long longMT = seed;
        this.mt[0] = (int) longMT;
        this.mti = 1;
        while (this.mti < f13084N) {
            longMT = ((1812433253 * (longMT ^ (longMT >> 30))) + this.mti) & 4294967295L;
            this.mt[this.mti] = (int) longMT;
            this.mti++;
        }
        clear();
    }

    @Override // org.apache.commons.math3.random.BitsStreamGenerator, org.apache.commons.math3.random.RandomGenerator
    public void setSeed(int[] seed) {
        if (seed == null) {
            setSeed(System.currentTimeMillis() + System.identityHashCode(this));
            return;
        }
        setSeed(19650218);
        int i2 = 1;
        int j2 = 0;
        for (int k2 = FastMath.max(f13084N, seed.length); k2 != 0; k2--) {
            long l0 = (this.mt[i2] & 2147483647L) | (this.mt[i2] < 0 ? 2147483648L : 0L);
            long l1 = (this.mt[i2 - 1] & 2147483647L) | (this.mt[i2 - 1] < 0 ? 2147483648L : 0L);
            long l2 = (l0 ^ ((l1 ^ (l1 >> 30)) * 1664525)) + seed[j2] + j2;
            this.mt[i2] = (int) (l2 & 4294967295L);
            i2++;
            j2++;
            if (i2 >= f13084N) {
                this.mt[0] = this.mt[623];
                i2 = 1;
            }
            if (j2 >= seed.length) {
                j2 = 0;
            }
        }
        for (int k3 = 623; k3 != 0; k3--) {
            long l02 = (this.mt[i2] & 2147483647L) | (this.mt[i2] < 0 ? 2147483648L : 0L);
            long l12 = (this.mt[i2 - 1] & 2147483647L) | (this.mt[i2 - 1] < 0 ? 2147483648L : 0L);
            long l3 = (l02 ^ ((l12 ^ (l12 >> 30)) * 1566083941)) - i2;
            this.mt[i2] = (int) (l3 & 4294967295L);
            i2++;
            if (i2 >= f13084N) {
                this.mt[0] = this.mt[623];
                i2 = 1;
            }
        }
        this.mt[0] = Integer.MIN_VALUE;
        clear();
    }

    @Override // org.apache.commons.math3.random.BitsStreamGenerator, org.apache.commons.math3.random.RandomGenerator
    public void setSeed(long seed) {
        setSeed(new int[]{(int) (seed >>> 32), (int) (seed & 4294967295L)});
    }

    @Override // org.apache.commons.math3.random.BitsStreamGenerator
    protected int next(int bits) {
        if (this.mti >= f13084N) {
            int mtNext = this.mt[0];
            for (int k2 = 0; k2 < 227; k2++) {
                int mtCurr = mtNext;
                mtNext = this.mt[k2 + 1];
                int y2 = (mtCurr & Integer.MIN_VALUE) | (mtNext & Integer.MAX_VALUE);
                this.mt[k2] = (this.mt[k2 + f13085M] ^ (y2 >>> 1)) ^ MAG01[y2 & 1];
            }
            for (int k3 = 227; k3 < 623; k3++) {
                int mtCurr2 = mtNext;
                mtNext = this.mt[k3 + 1];
                int y3 = (mtCurr2 & Integer.MIN_VALUE) | (mtNext & Integer.MAX_VALUE);
                this.mt[k3] = (this.mt[k3 - 227] ^ (y3 >>> 1)) ^ MAG01[y3 & 1];
            }
            int y4 = (mtNext & Integer.MIN_VALUE) | (this.mt[0] & Integer.MAX_VALUE);
            this.mt[623] = (this.mt[396] ^ (y4 >>> 1)) ^ MAG01[y4 & 1];
            this.mti = 0;
        }
        int[] iArr = this.mt;
        int i2 = this.mti;
        this.mti = i2 + 1;
        int y5 = iArr[i2];
        int y6 = y5 ^ (y5 >>> 11);
        int y7 = y6 ^ ((y6 << 7) & (-1658038656));
        int y8 = y7 ^ ((y7 << 15) & (-272236544));
        return (y8 ^ (y8 >>> 18)) >>> (32 - bits);
    }
}
