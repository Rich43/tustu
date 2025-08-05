package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.CombinatoricsUtils;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/PoissonDistribution.class */
public class PoissonDistribution extends AbstractIntegerDistribution {
    public static final int DEFAULT_MAX_ITERATIONS = 10000000;
    public static final double DEFAULT_EPSILON = 1.0E-12d;
    private static final long serialVersionUID = -3349935121172596109L;
    private final NormalDistribution normal;
    private final ExponentialDistribution exponential;
    private final double mean;
    private final int maxIterations;
    private final double epsilon;

    public PoissonDistribution(double p2) throws NotStrictlyPositiveException {
        this(p2, 1.0E-12d, DEFAULT_MAX_ITERATIONS);
    }

    public PoissonDistribution(double p2, double epsilon, int maxIterations) throws NotStrictlyPositiveException {
        this(new Well19937c(), p2, epsilon, maxIterations);
    }

    public PoissonDistribution(RandomGenerator rng, double p2, double epsilon, int maxIterations) throws NotStrictlyPositiveException {
        super(rng);
        if (p2 <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.MEAN, Double.valueOf(p2));
        }
        this.mean = p2;
        this.epsilon = epsilon;
        this.maxIterations = maxIterations;
        this.normal = new NormalDistribution(rng, p2, FastMath.sqrt(p2), 1.0E-9d);
        this.exponential = new ExponentialDistribution(rng, 1.0d, 1.0E-9d);
    }

    public PoissonDistribution(double p2, double epsilon) throws NotStrictlyPositiveException {
        this(p2, epsilon, DEFAULT_MAX_ITERATIONS);
    }

    public PoissonDistribution(double p2, int maxIterations) {
        this(p2, 1.0E-12d, maxIterations);
    }

    public double getMean() {
        return this.mean;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double probability(int x2) {
        double logProbability = logProbability(x2);
        if (logProbability == Double.NEGATIVE_INFINITY) {
            return 0.0d;
        }
        return FastMath.exp(logProbability);
    }

    @Override // org.apache.commons.math3.distribution.AbstractIntegerDistribution
    public double logProbability(int x2) {
        double ret;
        if (x2 < 0 || x2 == Integer.MAX_VALUE) {
            ret = Double.NEGATIVE_INFINITY;
        } else if (x2 == 0) {
            ret = -this.mean;
        } else {
            ret = (((-SaddlePointExpansion.getStirlingError(x2)) - SaddlePointExpansion.getDeviancePart(x2, this.mean)) - (0.5d * FastMath.log(6.283185307179586d))) - (0.5d * FastMath.log(x2));
        }
        return ret;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double cumulativeProbability(int x2) {
        if (x2 < 0) {
            return 0.0d;
        }
        if (x2 == Integer.MAX_VALUE) {
            return 1.0d;
        }
        return Gamma.regularizedGammaQ(x2 + 1.0d, this.mean, this.epsilon, this.maxIterations);
    }

    public double normalApproximateProbability(int x2) {
        return this.normal.cumulativeProbability(x2 + 0.5d);
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double getNumericalMean() {
        return getMean();
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double getNumericalVariance() {
        return getMean();
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public int getSupportLowerBound() {
        return 0;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public int getSupportUpperBound() {
        return Integer.MAX_VALUE;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public boolean isSupportConnected() {
        return true;
    }

    @Override // org.apache.commons.math3.distribution.AbstractIntegerDistribution, org.apache.commons.math3.distribution.IntegerDistribution
    public int sample() {
        return (int) FastMath.min(nextPoisson(this.mean), 2147483647L);
    }

    private long nextPoisson(double meanPoisson) throws NotPositiveException {
        double y2;
        double x2;
        double y3;
        double v2;
        if (meanPoisson < 40.0d) {
            double p2 = FastMath.exp(-meanPoisson);
            long n2 = 0;
            double r2 = 1.0d;
            while (n2 < 1000.0d * meanPoisson) {
                double rnd = this.random.nextDouble();
                r2 *= rnd;
                if (r2 >= p2) {
                    n2++;
                } else {
                    return n2;
                }
            }
            return n2;
        }
        double lambda = FastMath.floor(meanPoisson);
        double lambdaFractional = meanPoisson - lambda;
        double logLambda = FastMath.log(lambda);
        double logLambdaFactorial = CombinatoricsUtils.factorialLog((int) lambda);
        long y22 = lambdaFractional < Double.MIN_VALUE ? 0L : nextPoisson(lambdaFractional);
        double delta = FastMath.sqrt(lambda * FastMath.log(((32.0d * lambda) / 3.141592653589793d) + 1.0d));
        double halfDelta = delta / 2.0d;
        double twolpd = (2.0d * lambda) + delta;
        double a1 = FastMath.sqrt(3.141592653589793d * twolpd) * FastMath.exp(1.0d / (8.0d * lambda));
        double a2 = (twolpd / delta) * FastMath.exp(((-delta) * (1.0d + delta)) / twolpd);
        double aSum = a1 + a2 + 1.0d;
        double p1 = a1 / aSum;
        double p22 = a2 / aSum;
        double c1 = 1.0d / (8.0d * lambda);
        while (true) {
            double u2 = this.random.nextDouble();
            if (u2 <= p1) {
                double n3 = this.random.nextGaussian();
                x2 = (n3 * FastMath.sqrt(lambda + halfDelta)) - 0.5d;
                if (x2 <= delta && x2 >= (-lambda)) {
                    y3 = x2 < 0.0d ? FastMath.floor(x2) : FastMath.ceil(x2);
                    double e2 = this.exponential.sample();
                    v2 = ((-e2) - ((n3 * n3) / 2.0d)) + c1;
                }
            } else {
                if (u2 > p1 + p22) {
                    y2 = lambda;
                    break;
                }
                x2 = delta + ((twolpd / delta) * this.exponential.sample());
                y3 = FastMath.ceil(x2);
                v2 = (-this.exponential.sample()) - ((delta * (x2 + 1.0d)) / twolpd);
            }
            int a3 = x2 < 0.0d ? 1 : 0;
            double t2 = (y3 * (y3 + 1.0d)) / (2.0d * lambda);
            if (v2 < (-t2) && a3 == 0) {
                y2 = lambda + y3;
                break;
            }
            double qr = t2 * ((((2.0d * y3) + 1.0d) / (6.0d * lambda)) - 1.0d);
            double qa = qr - ((t2 * t2) / (3.0d * (lambda + (a3 * (y3 + 1.0d)))));
            if (v2 < qa) {
                y2 = lambda + y3;
                break;
            }
            if (v2 <= qr && v2 < ((y3 * logLambda) - CombinatoricsUtils.factorialLog((int) (y3 + lambda))) + logLambdaFactorial) {
                y2 = lambda + y3;
                break;
            }
        }
        return y22 + ((long) y2);
    }
}
