package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.OutOfRangeException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/ConstantRealDistribution.class */
public class ConstantRealDistribution extends AbstractRealDistribution {
    private static final long serialVersionUID = -4157745166772046273L;
    private final double value;

    public ConstantRealDistribution(double value) {
        super(null);
        this.value = value;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double density(double x2) {
        return x2 == this.value ? 1.0d : 0.0d;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double cumulativeProbability(double x2) {
        return x2 < this.value ? 0.0d : 1.0d;
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution, org.apache.commons.math3.distribution.RealDistribution
    public double inverseCumulativeProbability(double p2) throws OutOfRangeException {
        if (p2 < 0.0d || p2 > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p2), 0, 1);
        }
        return this.value;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalMean() {
        return this.value;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalVariance() {
        return 0.0d;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getSupportLowerBound() {
        return this.value;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getSupportUpperBound() {
        return this.value;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public boolean isSupportLowerBoundInclusive() {
        return true;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public boolean isSupportUpperBoundInclusive() {
        return true;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public boolean isSupportConnected() {
        return true;
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution, org.apache.commons.math3.distribution.RealDistribution
    public double sample() {
        return this.value;
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution, org.apache.commons.math3.distribution.RealDistribution
    public void reseedRandomGenerator(long seed) {
    }
}
