package org.apache.commons.math3.random;

import java.io.Serializable;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/BitsStreamGenerator.class */
public abstract class BitsStreamGenerator implements RandomGenerator, Serializable {
    private static final long serialVersionUID = 20130104;
    private double nextGaussian = Double.NaN;

    @Override // org.apache.commons.math3.random.RandomGenerator
    public abstract void setSeed(int i2);

    @Override // org.apache.commons.math3.random.RandomGenerator
    public abstract void setSeed(int[] iArr);

    @Override // org.apache.commons.math3.random.RandomGenerator
    public abstract void setSeed(long j2);

    protected abstract int next(int i2);

    @Override // org.apache.commons.math3.random.RandomGenerator
    public boolean nextBoolean() {
        return next(1) != 0;
    }

    @Override // org.apache.commons.math3.random.RandomGenerator
    public double nextDouble() {
        long high = next(26) << 26;
        int low = next(26);
        return (high | low) * 2.220446049250313E-16d;
    }

    @Override // org.apache.commons.math3.random.RandomGenerator
    public float nextFloat() {
        return next(23) * 1.1920929E-7f;
    }

    @Override // org.apache.commons.math3.random.RandomGenerator
    public double nextGaussian() {
        double random;
        if (Double.isNaN(this.nextGaussian)) {
            double x2 = nextDouble();
            double y2 = nextDouble();
            double alpha = 6.283185307179586d * x2;
            double r2 = FastMath.sqrt((-2.0d) * FastMath.log(y2));
            random = r2 * FastMath.cos(alpha);
            this.nextGaussian = r2 * FastMath.sin(alpha);
        } else {
            random = this.nextGaussian;
            this.nextGaussian = Double.NaN;
        }
        return random;
    }

    @Override // org.apache.commons.math3.random.RandomGenerator
    public int nextInt() {
        return next(32);
    }

    @Override // org.apache.commons.math3.random.RandomGenerator
    public int nextInt(int n2) throws IllegalArgumentException {
        int bits;
        int val;
        if (n2 > 0) {
            if ((n2 & (-n2)) == n2) {
                return (int) ((n2 * next(31)) >> 31);
            }
            do {
                bits = next(31);
                val = bits % n2;
            } while ((bits - val) + (n2 - 1) < 0);
            return val;
        }
        throw new NotStrictlyPositiveException(Integer.valueOf(n2));
    }

    @Override // org.apache.commons.math3.random.RandomGenerator
    public long nextLong() {
        long high = next(32) << 32;
        long low = next(32) & 4294967295L;
        return high | low;
    }

    public long nextLong(long n2) throws IllegalArgumentException {
        long bits;
        long val;
        if (n2 > 0) {
            do {
                bits = (next(31) << 32) | (next(32) & 4294967295L);
                val = bits % n2;
            } while ((bits - val) + (n2 - 1) < 0);
            return val;
        }
        throw new NotStrictlyPositiveException(Long.valueOf(n2));
    }

    public void clear() {
        this.nextGaussian = Double.NaN;
    }

    @Override // org.apache.commons.math3.random.RandomGenerator
    public void nextBytes(byte[] bytes) {
        nextBytesFill(bytes, 0, bytes.length);
    }

    public void nextBytes(byte[] bytes, int start, int len) {
        if (start < 0 || start >= bytes.length) {
            throw new OutOfRangeException(Integer.valueOf(start), 0, Integer.valueOf(bytes.length));
        }
        if (len < 0 || len > bytes.length - start) {
            throw new OutOfRangeException(Integer.valueOf(len), 0, Integer.valueOf(bytes.length - start));
        }
        nextBytesFill(bytes, start, len);
    }

    private void nextBytesFill(byte[] bytes, int start, int len) {
        int index = start;
        int indexLoopLimit = index + (len & 2147483644);
        while (index < indexLoopLimit) {
            int random = next(32);
            int i2 = index;
            int index2 = index + 1;
            bytes[i2] = (byte) random;
            int index3 = index2 + 1;
            bytes[index2] = (byte) (random >>> 8);
            int index4 = index3 + 1;
            bytes[index3] = (byte) (random >>> 16);
            index = index4 + 1;
            bytes[index4] = (byte) (random >>> 24);
        }
        int indexLimit = start + len;
        if (index < indexLimit) {
            int next = next(32);
            while (true) {
                int random2 = next;
                int i3 = index;
                index++;
                bytes[i3] = (byte) random2;
                if (index < indexLimit) {
                    next = random2 >>> 8;
                } else {
                    return;
                }
            }
        }
    }
}
