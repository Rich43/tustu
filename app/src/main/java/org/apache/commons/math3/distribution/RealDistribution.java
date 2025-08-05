package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/RealDistribution.class */
public interface RealDistribution {
    double probability(double d2);

    double density(double d2);

    double cumulativeProbability(double d2);

    @Deprecated
    double cumulativeProbability(double d2, double d3) throws NumberIsTooLargeException;

    double inverseCumulativeProbability(double d2) throws OutOfRangeException;

    double getNumericalMean();

    double getNumericalVariance();

    double getSupportLowerBound();

    double getSupportUpperBound();

    @Deprecated
    boolean isSupportLowerBoundInclusive();

    @Deprecated
    boolean isSupportUpperBoundInclusive();

    boolean isSupportConnected();

    void reseedRandomGenerator(long j2);

    double sample();

    double[] sample(int i2);
}
