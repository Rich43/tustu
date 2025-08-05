package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/AbstractRandomGenerator.class */
public abstract class AbstractRandomGenerator implements RandomGenerator {
    private double cachedNormalDeviate = Double.NaN;

    @Override // org.apache.commons.math3.random.RandomGenerator
    public abstract void setSeed(long j2);

    @Override // org.apache.commons.math3.random.RandomGenerator
    public abstract double nextDouble();

    public void clear() {
        this.cachedNormalDeviate = Double.NaN;
    }

    @Override // org.apache.commons.math3.random.RandomGenerator
    public void setSeed(int seed) {
        setSeed(seed);
    }

    @Override // org.apache.commons.math3.random.RandomGenerator
    public void setSeed(int[] seed) {
        long combined = 0;
        for (int s2 : seed) {
            combined = (combined * 4294967291L) + s2;
        }
        setSeed(combined);
    }

    @Override // org.apache.commons.math3.random.RandomGenerator
    public void nextBytes(byte[] bytes) {
        int bytesOut = 0;
        while (bytesOut < bytes.length) {
            int randInt = nextInt();
            for (int i2 = 0; i2 < 3; i2++) {
                if (i2 > 0) {
                    randInt >>= 8;
                }
                int i3 = bytesOut;
                bytesOut++;
                bytes[i3] = (byte) randInt;
                if (bytesOut == bytes.length) {
                    return;
                }
            }
        }
    }

    @Override // org.apache.commons.math3.random.RandomGenerator
    public int nextInt() {
        return (int) (((2.0d * nextDouble()) - 1.0d) * 2.147483647E9d);
    }

    @Override // org.apache.commons.math3.random.RandomGenerator
    public int nextInt(int n2) {
        if (n2 <= 0) {
            throw new NotStrictlyPositiveException(Integer.valueOf(n2));
        }
        int result = (int) (nextDouble() * n2);
        return result < n2 ? result : n2 - 1;
    }

    @Override // org.apache.commons.math3.random.RandomGenerator
    public long nextLong() {
        return (long) (((2.0d * nextDouble()) - 1.0d) * 9.223372036854776E18d);
    }

    @Override // org.apache.commons.math3.random.RandomGenerator
    public boolean nextBoolean() {
        return nextDouble() <= 0.5d;
    }

    @Override // org.apache.commons.math3.random.RandomGenerator
    public float nextFloat() {
        return (float) nextDouble();
    }

    @Override // org.apache.commons.math3.random.RandomGenerator
    public double nextGaussian() {
        double s2;
        if (!Double.isNaN(this.cachedNormalDeviate)) {
            double dev = this.cachedNormalDeviate;
            this.cachedNormalDeviate = Double.NaN;
            return dev;
        }
        double v1 = 0.0d;
        double v2 = 0.0d;
        double d2 = 1.0d;
        while (true) {
            s2 = d2;
            if (s2 < 1.0d) {
                break;
            }
            v1 = (2.0d * nextDouble()) - 1.0d;
            v2 = (2.0d * nextDouble()) - 1.0d;
            d2 = (v1 * v1) + (v2 * v2);
        }
        if (s2 != 0.0d) {
            s2 = FastMath.sqrt(((-2.0d) * FastMath.log(s2)) / s2);
        }
        this.cachedNormalDeviate = v2 * s2;
        return v1 * s2;
    }
}
