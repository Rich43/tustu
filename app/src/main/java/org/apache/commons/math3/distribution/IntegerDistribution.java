package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/IntegerDistribution.class */
public interface IntegerDistribution {
    double probability(int i2);

    double cumulativeProbability(int i2);

    double cumulativeProbability(int i2, int i3) throws NumberIsTooLargeException;

    int inverseCumulativeProbability(double d2) throws OutOfRangeException;

    double getNumericalMean();

    double getNumericalVariance();

    int getSupportLowerBound();

    int getSupportUpperBound();

    boolean isSupportConnected();

    void reseedRandomGenerator(long j2);

    int sample();

    int[] sample(int i2);
}
