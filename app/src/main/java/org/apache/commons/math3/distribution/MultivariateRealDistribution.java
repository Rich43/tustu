package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/MultivariateRealDistribution.class */
public interface MultivariateRealDistribution {
    double density(double[] dArr);

    void reseedRandomGenerator(long j2);

    int getDimension();

    double[] sample();

    double[][] sample(int i2) throws NotStrictlyPositiveException;
}
