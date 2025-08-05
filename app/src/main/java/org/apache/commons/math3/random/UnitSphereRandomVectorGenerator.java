package org.apache.commons.math3.random;

import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/UnitSphereRandomVectorGenerator.class */
public class UnitSphereRandomVectorGenerator implements RandomVectorGenerator {
    private final RandomGenerator rand;
    private final int dimension;

    public UnitSphereRandomVectorGenerator(int dimension, RandomGenerator rand) {
        this.dimension = dimension;
        this.rand = rand;
    }

    public UnitSphereRandomVectorGenerator(int dimension) {
        this(dimension, new MersenneTwister());
    }

    @Override // org.apache.commons.math3.random.RandomVectorGenerator
    public double[] nextVector() {
        double[] v2 = new double[this.dimension];
        double normSq = 0.0d;
        for (int i2 = 0; i2 < this.dimension; i2++) {
            double comp = this.rand.nextGaussian();
            v2[i2] = comp;
            normSq += comp * comp;
        }
        double f2 = 1.0d / FastMath.sqrt(normSq);
        for (int i3 = 0; i3 < this.dimension; i3++) {
            int i4 = i3;
            v2[i4] = v2[i4] * f2;
        }
        return v2;
    }
}
