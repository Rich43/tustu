package org.apache.commons.math3.random;

import java.util.Random;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/RandomAdaptor.class */
public class RandomAdaptor extends Random implements RandomGenerator {
    private static final long serialVersionUID = 2306581345647615033L;
    private final RandomGenerator randomGenerator;

    private RandomAdaptor() {
        this.randomGenerator = null;
    }

    public RandomAdaptor(RandomGenerator randomGenerator) {
        this.randomGenerator = randomGenerator;
    }

    public static Random createAdaptor(RandomGenerator randomGenerator) {
        return new RandomAdaptor(randomGenerator);
    }

    @Override // java.util.Random
    public boolean nextBoolean() {
        return this.randomGenerator.nextBoolean();
    }

    @Override // java.util.Random
    public void nextBytes(byte[] bytes) {
        this.randomGenerator.nextBytes(bytes);
    }

    @Override // java.util.Random
    public double nextDouble() {
        return this.randomGenerator.nextDouble();
    }

    @Override // java.util.Random
    public float nextFloat() {
        return this.randomGenerator.nextFloat();
    }

    @Override // java.util.Random
    public double nextGaussian() {
        return this.randomGenerator.nextGaussian();
    }

    @Override // java.util.Random
    public int nextInt() {
        return this.randomGenerator.nextInt();
    }

    @Override // java.util.Random
    public int nextInt(int n2) {
        return this.randomGenerator.nextInt(n2);
    }

    @Override // java.util.Random
    public long nextLong() {
        return this.randomGenerator.nextLong();
    }

    @Override // org.apache.commons.math3.random.RandomGenerator
    public void setSeed(int seed) {
        if (this.randomGenerator != null) {
            this.randomGenerator.setSeed(seed);
        }
    }

    @Override // org.apache.commons.math3.random.RandomGenerator
    public void setSeed(int[] seed) {
        if (this.randomGenerator != null) {
            this.randomGenerator.setSeed(seed);
        }
    }

    @Override // java.util.Random
    public void setSeed(long seed) {
        if (this.randomGenerator != null) {
            this.randomGenerator.setSeed(seed);
        }
    }
}
