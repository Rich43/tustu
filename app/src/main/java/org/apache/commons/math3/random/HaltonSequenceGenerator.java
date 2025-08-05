package org.apache.commons.math3.random;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/HaltonSequenceGenerator.class */
public class HaltonSequenceGenerator implements RandomVectorGenerator {
    private static final int[] PRIMES = {2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41, 43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97, 101, 103, 107, 109, 113, 127, 131, 137, 139, 149, 151, 157, 163, 167, 173};
    private static final int[] WEIGHTS = {1, 2, 3, 3, 8, 11, 12, 14, 7, 18, 12, 13, 17, 18, 29, 14, 18, 43, 41, 44, 40, 30, 47, 65, 71, 28, 40, 60, 79, 89, 56, 50, 52, 61, 108, 56, 66, 63, 60, 66};
    private final int dimension;
    private int count;
    private final int[] base;
    private final int[] weight;

    public HaltonSequenceGenerator(int dimension) throws OutOfRangeException {
        this(dimension, PRIMES, WEIGHTS);
    }

    public HaltonSequenceGenerator(int dimension, int[] bases, int[] weights) throws OutOfRangeException, NullArgumentException, DimensionMismatchException {
        this.count = 0;
        MathUtils.checkNotNull(bases);
        if (dimension < 1 || dimension > bases.length) {
            throw new OutOfRangeException(Integer.valueOf(dimension), 1, Integer.valueOf(PRIMES.length));
        }
        if (weights != null && weights.length != bases.length) {
            throw new DimensionMismatchException(weights.length, bases.length);
        }
        this.dimension = dimension;
        this.base = (int[]) bases.clone();
        this.weight = weights == null ? null : (int[]) weights.clone();
        this.count = 0;
    }

    @Override // org.apache.commons.math3.random.RandomVectorGenerator
    public double[] nextVector() {
        double[] v2 = new double[this.dimension];
        for (int i2 = 0; i2 < this.dimension; i2++) {
            int index = this.count;
            double f2 = 1.0d / this.base[i2];
            while (index > 0) {
                int digit = scramble(i2, 0, this.base[i2], index % this.base[i2]);
                int i3 = i2;
                v2[i3] = v2[i3] + (f2 * digit);
                index /= this.base[i2];
                f2 /= this.base[i2];
            }
        }
        this.count++;
        return v2;
    }

    protected int scramble(int i2, int j2, int b2, int digit) {
        return this.weight != null ? (this.weight[i2] * digit) % b2 : digit;
    }

    public double[] skipTo(int index) throws NotPositiveException {
        this.count = index;
        return nextVector();
    }

    public int getNextIndex() {
        return this.count;
    }
}
