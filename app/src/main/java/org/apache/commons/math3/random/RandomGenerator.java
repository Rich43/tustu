package org.apache.commons.math3.random;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/random/RandomGenerator.class */
public interface RandomGenerator {
    void setSeed(int i2);

    void setSeed(int[] iArr);

    void setSeed(long j2);

    void nextBytes(byte[] bArr);

    int nextInt();

    int nextInt(int i2);

    long nextLong();

    boolean nextBoolean();

    float nextFloat();

    double nextDouble();

    double nextGaussian();
}
