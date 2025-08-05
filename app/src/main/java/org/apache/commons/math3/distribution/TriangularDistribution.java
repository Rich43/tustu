package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/TriangularDistribution.class */
public class TriangularDistribution extends AbstractRealDistribution {
    private static final long serialVersionUID = 20120112;

    /* renamed from: a, reason: collision with root package name */
    private final double f12990a;

    /* renamed from: b, reason: collision with root package name */
    private final double f12991b;

    /* renamed from: c, reason: collision with root package name */
    private final double f12992c;
    private final double solverAbsoluteAccuracy;

    public TriangularDistribution(double a2, double c2, double b2) throws NumberIsTooSmallException, NumberIsTooLargeException {
        this(new Well19937c(), a2, c2, b2);
    }

    public TriangularDistribution(RandomGenerator rng, double a2, double c2, double b2) throws NumberIsTooSmallException, NumberIsTooLargeException {
        super(rng);
        if (a2 >= b2) {
            throw new NumberIsTooLargeException(LocalizedFormats.LOWER_BOUND_NOT_BELOW_UPPER_BOUND, Double.valueOf(a2), Double.valueOf(b2), false);
        }
        if (c2 < a2) {
            throw new NumberIsTooSmallException(LocalizedFormats.NUMBER_TOO_SMALL, Double.valueOf(c2), Double.valueOf(a2), true);
        }
        if (c2 > b2) {
            throw new NumberIsTooLargeException(LocalizedFormats.NUMBER_TOO_LARGE, Double.valueOf(c2), Double.valueOf(b2), true);
        }
        this.f12990a = a2;
        this.f12992c = c2;
        this.f12991b = b2;
        this.solverAbsoluteAccuracy = FastMath.max(FastMath.ulp(a2), FastMath.ulp(b2));
    }

    public double getMode() {
        return this.f12992c;
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution
    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double density(double x2) {
        if (x2 < this.f12990a) {
            return 0.0d;
        }
        if (this.f12990a <= x2 && x2 < this.f12992c) {
            double divident = 2.0d * (x2 - this.f12990a);
            double divisor = (this.f12991b - this.f12990a) * (this.f12992c - this.f12990a);
            return divident / divisor;
        }
        if (x2 == this.f12992c) {
            return 2.0d / (this.f12991b - this.f12990a);
        }
        if (this.f12992c < x2 && x2 <= this.f12991b) {
            double divident2 = 2.0d * (this.f12991b - x2);
            double divisor2 = (this.f12991b - this.f12990a) * (this.f12991b - this.f12992c);
            return divident2 / divisor2;
        }
        return 0.0d;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double cumulativeProbability(double x2) {
        if (x2 < this.f12990a) {
            return 0.0d;
        }
        if (this.f12990a <= x2 && x2 < this.f12992c) {
            double divident = (x2 - this.f12990a) * (x2 - this.f12990a);
            double divisor = (this.f12991b - this.f12990a) * (this.f12992c - this.f12990a);
            return divident / divisor;
        }
        if (x2 == this.f12992c) {
            return (this.f12992c - this.f12990a) / (this.f12991b - this.f12990a);
        }
        if (this.f12992c < x2 && x2 <= this.f12991b) {
            double divident2 = (this.f12991b - x2) * (this.f12991b - x2);
            double divisor2 = (this.f12991b - this.f12990a) * (this.f12991b - this.f12992c);
            return 1.0d - (divident2 / divisor2);
        }
        return 1.0d;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalMean() {
        return ((this.f12990a + this.f12991b) + this.f12992c) / 3.0d;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalVariance() {
        return ((((((this.f12990a * this.f12990a) + (this.f12991b * this.f12991b)) + (this.f12992c * this.f12992c)) - (this.f12990a * this.f12991b)) - (this.f12990a * this.f12992c)) - (this.f12991b * this.f12992c)) / 18.0d;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getSupportLowerBound() {
        return this.f12990a;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getSupportUpperBound() {
        return this.f12991b;
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
    public double inverseCumulativeProbability(double p2) throws OutOfRangeException {
        if (p2 < 0.0d || p2 > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(p2), 0, 1);
        }
        if (p2 == 0.0d) {
            return this.f12990a;
        }
        if (p2 == 1.0d) {
            return this.f12991b;
        }
        if (p2 < (this.f12992c - this.f12990a) / (this.f12991b - this.f12990a)) {
            return this.f12990a + FastMath.sqrt(p2 * (this.f12991b - this.f12990a) * (this.f12992c - this.f12990a));
        }
        return this.f12991b - FastMath.sqrt(((1.0d - p2) * (this.f12991b - this.f12990a)) * (this.f12991b - this.f12992c));
    }
}
