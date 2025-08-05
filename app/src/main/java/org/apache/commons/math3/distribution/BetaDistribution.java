package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.special.Beta;
import org.apache.commons.math3.special.Gamma;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/BetaDistribution.class */
public class BetaDistribution extends AbstractRealDistribution {
    public static final double DEFAULT_INVERSE_ABSOLUTE_ACCURACY = 1.0E-9d;
    private static final long serialVersionUID = -1221965979403477668L;
    private final double alpha;
    private final double beta;

    /* renamed from: z, reason: collision with root package name */
    private double f12986z;
    private final double solverAbsoluteAccuracy;

    public BetaDistribution(double alpha, double beta) {
        this(alpha, beta, 1.0E-9d);
    }

    public BetaDistribution(double alpha, double beta, double inverseCumAccuracy) {
        this(new Well19937c(), alpha, beta, inverseCumAccuracy);
    }

    public BetaDistribution(RandomGenerator rng, double alpha, double beta) {
        this(rng, alpha, beta, 1.0E-9d);
    }

    public BetaDistribution(RandomGenerator rng, double alpha, double beta, double inverseCumAccuracy) {
        super(rng);
        this.alpha = alpha;
        this.beta = beta;
        this.f12986z = Double.NaN;
        this.solverAbsoluteAccuracy = inverseCumAccuracy;
    }

    public double getAlpha() {
        return this.alpha;
    }

    public double getBeta() {
        return this.beta;
    }

    private void recomputeZ() {
        if (Double.isNaN(this.f12986z)) {
            this.f12986z = (Gamma.logGamma(this.alpha) + Gamma.logGamma(this.beta)) - Gamma.logGamma(this.alpha + this.beta);
        }
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double density(double x2) {
        double logDensity = logDensity(x2);
        if (logDensity == Double.NEGATIVE_INFINITY) {
            return 0.0d;
        }
        return FastMath.exp(logDensity);
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution
    public double logDensity(double x2) {
        recomputeZ();
        if (x2 < 0.0d || x2 > 1.0d) {
            return Double.NEGATIVE_INFINITY;
        }
        if (x2 == 0.0d) {
            if (this.alpha < 1.0d) {
                throw new NumberIsTooSmallException(LocalizedFormats.CANNOT_COMPUTE_BETA_DENSITY_AT_0_FOR_SOME_ALPHA, Double.valueOf(this.alpha), 1, false);
            }
            return Double.NEGATIVE_INFINITY;
        }
        if (x2 == 1.0d) {
            if (this.beta < 1.0d) {
                throw new NumberIsTooSmallException(LocalizedFormats.CANNOT_COMPUTE_BETA_DENSITY_AT_1_FOR_SOME_BETA, Double.valueOf(this.beta), 1, false);
            }
            return Double.NEGATIVE_INFINITY;
        }
        double logX = FastMath.log(x2);
        double log1mX = FastMath.log1p(-x2);
        return (((this.alpha - 1.0d) * logX) + ((this.beta - 1.0d) * log1mX)) - this.f12986z;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double cumulativeProbability(double x2) {
        if (x2 <= 0.0d) {
            return 0.0d;
        }
        if (x2 >= 1.0d) {
            return 1.0d;
        }
        return Beta.regularizedBeta(x2, this.alpha, this.beta);
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution
    protected double getSolverAbsoluteAccuracy() {
        return this.solverAbsoluteAccuracy;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalMean() {
        double a2 = getAlpha();
        return a2 / (a2 + getBeta());
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getNumericalVariance() {
        double a2 = getAlpha();
        double b2 = getBeta();
        double alphabetasum = a2 + b2;
        return (a2 * b2) / ((alphabetasum * alphabetasum) * (alphabetasum + 1.0d));
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getSupportLowerBound() {
        return 0.0d;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public double getSupportUpperBound() {
        return 1.0d;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public boolean isSupportLowerBoundInclusive() {
        return false;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public boolean isSupportUpperBoundInclusive() {
        return false;
    }

    @Override // org.apache.commons.math3.distribution.RealDistribution
    public boolean isSupportConnected() {
        return true;
    }

    @Override // org.apache.commons.math3.distribution.AbstractRealDistribution, org.apache.commons.math3.distribution.RealDistribution
    public double sample() {
        return ChengBetaSampler.sample(this.random, this.alpha, this.beta);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/BetaDistribution$ChengBetaSampler.class */
    private static final class ChengBetaSampler {
        private ChengBetaSampler() {
        }

        static double sample(RandomGenerator random, double alpha, double beta) {
            double a2 = FastMath.min(alpha, beta);
            double b2 = FastMath.max(alpha, beta);
            if (a2 > 1.0d) {
                return algorithmBB(random, alpha, a2, b2);
            }
            return algorithmBC(random, alpha, b2, a2);
        }

        private static double algorithmBB(RandomGenerator random, double a0, double a2, double b2) {
            double w2;
            double r2;
            double t2;
            double alpha = a2 + b2;
            double beta = FastMath.sqrt((alpha - 2.0d) / (((2.0d * a2) * b2) - alpha));
            double gamma = a2 + (1.0d / beta);
            do {
                double u1 = random.nextDouble();
                double u2 = random.nextDouble();
                double v2 = beta * (FastMath.log(u1) - FastMath.log1p(-u1));
                w2 = a2 * FastMath.exp(v2);
                double z2 = u1 * u1 * u2;
                r2 = (gamma * v2) - 1.3862944d;
                double s2 = (a2 + r2) - w2;
                if (s2 + 2.609438d >= 5.0d * z2) {
                    break;
                }
                t2 = FastMath.log(z2);
                if (s2 >= t2) {
                    break;
                }
            } while (r2 + (alpha * (FastMath.log(alpha) - FastMath.log(b2 + w2))) < t2);
            double w3 = FastMath.min(w2, Double.MAX_VALUE);
            return Precision.equals(a2, a0) ? w3 / (b2 + w3) : b2 / (b2 + w3);
        }

        /* JADX WARN: Removed duplicated region for block: B:26:0x00ed A[EDGE_INSN: B:26:0x00ed->B:18:0x00ed BREAK  A[LOOP:0: B:3:0x003f->B:29:0x003f], SYNTHETIC] */
        /* JADX WARN: Removed duplicated region for block: B:31:0x003f A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private static double algorithmBC(org.apache.commons.math3.random.RandomGenerator r9, double r10, double r12, double r14) {
            /*
                Method dump skipped, instructions count: 275
                To view this dump change 'Code comments level' option to 'DEBUG'
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.distribution.BetaDistribution.ChengBetaSampler.algorithmBC(org.apache.commons.math3.random.RandomGenerator, double, double, double):double");
        }
    }
}
