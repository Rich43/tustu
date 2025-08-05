package org.apache.commons.math3.distribution;

import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/ZipfDistribution.class */
public class ZipfDistribution extends AbstractIntegerDistribution {
    private static final long serialVersionUID = -140627372283420404L;
    private final int numberOfElements;
    private final double exponent;
    private double numericalMean;
    private boolean numericalMeanIsCalculated;
    private double numericalVariance;
    private boolean numericalVarianceIsCalculated;
    private transient ZipfRejectionInversionSampler sampler;

    public ZipfDistribution(int numberOfElements, double exponent) {
        this(new Well19937c(), numberOfElements, exponent);
    }

    public ZipfDistribution(RandomGenerator rng, int numberOfElements, double exponent) throws NotStrictlyPositiveException {
        super(rng);
        this.numericalMean = Double.NaN;
        this.numericalMeanIsCalculated = false;
        this.numericalVariance = Double.NaN;
        this.numericalVarianceIsCalculated = false;
        if (numberOfElements <= 0) {
            throw new NotStrictlyPositiveException(LocalizedFormats.DIMENSION, Integer.valueOf(numberOfElements));
        }
        if (exponent <= 0.0d) {
            throw new NotStrictlyPositiveException(LocalizedFormats.EXPONENT, Double.valueOf(exponent));
        }
        this.numberOfElements = numberOfElements;
        this.exponent = exponent;
    }

    public int getNumberOfElements() {
        return this.numberOfElements;
    }

    public double getExponent() {
        return this.exponent;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double probability(int x2) {
        if (x2 <= 0 || x2 > this.numberOfElements) {
            return 0.0d;
        }
        return (1.0d / FastMath.pow(x2, this.exponent)) / generalizedHarmonic(this.numberOfElements, this.exponent);
    }

    @Override // org.apache.commons.math3.distribution.AbstractIntegerDistribution
    public double logProbability(int x2) {
        if (x2 <= 0 || x2 > this.numberOfElements) {
            return Double.NEGATIVE_INFINITY;
        }
        return ((-FastMath.log(x2)) * this.exponent) - FastMath.log(generalizedHarmonic(this.numberOfElements, this.exponent));
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double cumulativeProbability(int x2) {
        if (x2 <= 0) {
            return 0.0d;
        }
        if (x2 >= this.numberOfElements) {
            return 1.0d;
        }
        return generalizedHarmonic(x2, this.exponent) / generalizedHarmonic(this.numberOfElements, this.exponent);
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double getNumericalMean() {
        if (!this.numericalMeanIsCalculated) {
            this.numericalMean = calculateNumericalMean();
            this.numericalMeanIsCalculated = true;
        }
        return this.numericalMean;
    }

    protected double calculateNumericalMean() {
        int N2 = getNumberOfElements();
        double s2 = getExponent();
        double Hs1 = generalizedHarmonic(N2, s2 - 1.0d);
        double Hs = generalizedHarmonic(N2, s2);
        return Hs1 / Hs;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public double getNumericalVariance() {
        if (!this.numericalVarianceIsCalculated) {
            this.numericalVariance = calculateNumericalVariance();
            this.numericalVarianceIsCalculated = true;
        }
        return this.numericalVariance;
    }

    protected double calculateNumericalVariance() {
        int N2 = getNumberOfElements();
        double s2 = getExponent();
        double Hs2 = generalizedHarmonic(N2, s2 - 2.0d);
        double Hs1 = generalizedHarmonic(N2, s2 - 1.0d);
        double Hs = generalizedHarmonic(N2, s2);
        return (Hs2 / Hs) - ((Hs1 * Hs1) / (Hs * Hs));
    }

    private double generalizedHarmonic(int n2, double m2) {
        double value = 0.0d;
        for (int k2 = n2; k2 > 0; k2--) {
            value += 1.0d / FastMath.pow(k2, m2);
        }
        return value;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public int getSupportLowerBound() {
        return 1;
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public int getSupportUpperBound() {
        return getNumberOfElements();
    }

    @Override // org.apache.commons.math3.distribution.IntegerDistribution
    public boolean isSupportConnected() {
        return true;
    }

    @Override // org.apache.commons.math3.distribution.AbstractIntegerDistribution, org.apache.commons.math3.distribution.IntegerDistribution
    public int sample() {
        if (this.sampler == null) {
            this.sampler = new ZipfRejectionInversionSampler(this.numberOfElements, this.exponent);
        }
        return this.sampler.sample(this.random);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/distribution/ZipfDistribution$ZipfRejectionInversionSampler.class */
    static final class ZipfRejectionInversionSampler {
        private final double exponent;
        private final int numberOfElements;
        private final double hIntegralNumberOfElements;
        private final double hIntegralX1 = hIntegral(1.5d) - 1.0d;

        /* renamed from: s, reason: collision with root package name */
        private final double f12993s = 2.0d - hIntegralInverse(hIntegral(2.5d) - h(2.0d));

        ZipfRejectionInversionSampler(int numberOfElements, double exponent) {
            this.exponent = exponent;
            this.numberOfElements = numberOfElements;
            this.hIntegralNumberOfElements = hIntegral(numberOfElements + 0.5d);
        }

        int sample(RandomGenerator random) {
            double u2;
            int k2;
            do {
                u2 = this.hIntegralNumberOfElements + (random.nextDouble() * (this.hIntegralX1 - this.hIntegralNumberOfElements));
                double x2 = hIntegralInverse(u2);
                k2 = (int) (x2 + 0.5d);
                if (k2 < 1) {
                    k2 = 1;
                } else if (k2 > this.numberOfElements) {
                    k2 = this.numberOfElements;
                }
                if (k2 - x2 <= this.f12993s) {
                    break;
                }
            } while (u2 < hIntegral(k2 + 0.5d) - h(k2));
            return k2;
        }

        private double hIntegral(double x2) {
            double logX = FastMath.log(x2);
            return helper2((1.0d - this.exponent) * logX) * logX;
        }

        private double h(double x2) {
            return FastMath.exp((-this.exponent) * FastMath.log(x2));
        }

        private double hIntegralInverse(double x2) {
            double t2 = x2 * (1.0d - this.exponent);
            if (t2 < -1.0d) {
                t2 = -1.0d;
            }
            return FastMath.exp(helper1(t2) * x2);
        }

        static double helper1(double x2) {
            if (FastMath.abs(x2) > 1.0E-8d) {
                return FastMath.log1p(x2) / x2;
            }
            return 1.0d - (x2 * (0.5d - (x2 * (0.3333333333333333d - (x2 * 0.25d)))));
        }

        static double helper2(double x2) {
            if (FastMath.abs(x2) > 1.0E-8d) {
                return FastMath.expm1(x2) / x2;
            }
            return 1.0d + (x2 * 0.5d * (1.0d + (x2 * 0.3333333333333333d * (1.0d + (x2 * 0.25d)))));
        }
    }
}
