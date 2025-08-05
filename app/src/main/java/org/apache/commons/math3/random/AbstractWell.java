package org.apache.commons.math3.random;

import java.io.Serializable;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/AbstractWell.class */
public abstract class AbstractWell extends BitsStreamGenerator implements Serializable {
    private static final long serialVersionUID = -817701723016583596L;
    protected int index;

    /* renamed from: v, reason: collision with root package name */
    protected final int[] f13083v;
    protected final int[] iRm1;
    protected final int[] iRm2;
    protected final int[] i1;
    protected final int[] i2;
    protected final int[] i3;

    @Override // org.apache.commons.math3.random.BitsStreamGenerator
    protected abstract int next(int i2);

    protected AbstractWell(int k2, int m1, int m2, int m3) {
        this(k2, m1, m2, m3, (int[]) null);
    }

    protected AbstractWell(int k2, int m1, int m2, int m3, int seed) {
        this(k2, m1, m2, m3, new int[]{seed});
    }

    protected AbstractWell(int k2, int m1, int m2, int m3, int[] seed) {
        int r2 = ((k2 + 32) - 1) / 32;
        this.f13083v = new int[r2];
        this.index = 0;
        this.iRm1 = new int[r2];
        this.iRm2 = new int[r2];
        this.i1 = new int[r2];
        this.i2 = new int[r2];
        this.i3 = new int[r2];
        for (int j2 = 0; j2 < r2; j2++) {
            this.iRm1[j2] = ((j2 + r2) - 1) % r2;
            this.iRm2[j2] = ((j2 + r2) - 2) % r2;
            this.i1[j2] = (j2 + m1) % r2;
            this.i2[j2] = (j2 + m2) % r2;
            this.i3[j2] = (j2 + m3) % r2;
        }
        setSeed(seed);
    }

    protected AbstractWell(int k2, int m1, int m2, int m3, long seed) {
        this(k2, m1, m2, m3, new int[]{(int) (seed >>> 32), (int) (seed & 4294967295L)});
    }

    @Override // org.apache.commons.math3.random.BitsStreamGenerator, org.apache.commons.math3.random.RandomGenerator
    public void setSeed(int seed) {
        setSeed(new int[]{seed});
    }

    @Override // org.apache.commons.math3.random.BitsStreamGenerator, org.apache.commons.math3.random.RandomGenerator
    public void setSeed(int[] seed) {
        if (seed == null) {
            setSeed(System.currentTimeMillis() + System.identityHashCode(this));
            return;
        }
        System.arraycopy(seed, 0, this.f13083v, 0, FastMath.min(seed.length, this.f13083v.length));
        if (seed.length < this.f13083v.length) {
            for (int i2 = seed.length; i2 < this.f13083v.length; i2++) {
                long l2 = this.f13083v[i2 - seed.length];
                this.f13083v[i2] = (int) (((1812433253 * (l2 ^ (l2 >> 30))) + i2) & 4294967295L);
            }
        }
        this.index = 0;
        clear();
    }

    @Override // org.apache.commons.math3.random.BitsStreamGenerator, org.apache.commons.math3.random.RandomGenerator
    public void setSeed(long seed) {
        setSeed(new int[]{(int) (seed >>> 32), (int) (seed & 4294967295L)});
    }
}
