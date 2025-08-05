package org.apache.commons.math3.random;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/GaussianRandomGenerator.class */
public class GaussianRandomGenerator implements NormalizedRandomGenerator {
    private final RandomGenerator generator;

    public GaussianRandomGenerator(RandomGenerator generator) {
        this.generator = generator;
    }

    @Override // org.apache.commons.math3.random.NormalizedRandomGenerator
    public double nextNormalizedDouble() {
        return this.generator.nextGaussian();
    }
}
