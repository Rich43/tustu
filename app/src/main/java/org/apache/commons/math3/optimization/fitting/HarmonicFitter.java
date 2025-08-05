package org.apache.commons.math3.optimization.fitting;

import org.apache.commons.math3.analysis.function.HarmonicOscillator;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.DifferentiableMultivariateVectorOptimizer;
import org.apache.commons.math3.util.FastMath;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/fitting/HarmonicFitter.class */
public class HarmonicFitter extends CurveFitter<HarmonicOscillator.Parametric> {
    public HarmonicFitter(DifferentiableMultivariateVectorOptimizer optimizer) {
        super(optimizer);
    }

    public double[] fit(double[] initialGuess) {
        return fit(new HarmonicOscillator.Parametric(), initialGuess);
    }

    public double[] fit() {
        return fit(new ParameterGuesser(getObservations()).guess());
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/fitting/HarmonicFitter$ParameterGuesser.class */
    public static class ParameterGuesser {

        /* renamed from: a, reason: collision with root package name */
        private final double f13079a;
        private final double omega;
        private final double phi;

        public ParameterGuesser(WeightedObservedPoint[] observations) {
            if (observations.length < 4) {
                throw new NumberIsTooSmallException(LocalizedFormats.INSUFFICIENT_OBSERVED_POINTS_IN_SAMPLE, Integer.valueOf(observations.length), 4, true);
            }
            WeightedObservedPoint[] sorted = sortObservations(observations);
            double[] aOmega = guessAOmega(sorted);
            this.f13079a = aOmega[0];
            this.omega = aOmega[1];
            this.phi = guessPhi(sorted);
        }

        public double[] guess() {
            return new double[]{this.f13079a, this.omega, this.phi};
        }

        private WeightedObservedPoint[] sortObservations(WeightedObservedPoint[] unsorted) {
            WeightedObservedPoint[] observations = (WeightedObservedPoint[]) unsorted.clone();
            WeightedObservedPoint curr = observations[0];
            for (int j2 = 1; j2 < observations.length; j2++) {
                WeightedObservedPoint prec = curr;
                curr = observations[j2];
                if (curr.getX() < prec.getX()) {
                    int i2 = j2 - 1;
                    WeightedObservedPoint mI = observations[i2];
                    while (i2 >= 0 && curr.getX() < mI.getX()) {
                        observations[i2 + 1] = mI;
                        int i3 = i2;
                        i2--;
                        if (i3 != 0) {
                            mI = observations[i2];
                        }
                    }
                    observations[i2 + 1] = curr;
                    curr = observations[j2];
                }
            }
            return observations;
        }

        private double[] guessAOmega(WeightedObservedPoint[] observations) {
            double[] aOmega = new double[2];
            double sx2 = 0.0d;
            double sy2 = 0.0d;
            double sxy = 0.0d;
            double sxz = 0.0d;
            double syz = 0.0d;
            double currentX = observations[0].getX();
            double currentY = observations[0].getY();
            double f2Integral = 0.0d;
            double fPrime2Integral = 0.0d;
            for (int i2 = 1; i2 < observations.length; i2++) {
                double previousX = currentX;
                double previousY = currentY;
                currentX = observations[i2].getX();
                currentY = observations[i2].getY();
                double dx = currentX - previousX;
                double dy = currentY - previousY;
                double f2StepIntegral = (dx * (((previousY * previousY) + (previousY * currentY)) + (currentY * currentY))) / 3.0d;
                double fPrime2StepIntegral = (dy * dy) / dx;
                double x2 = currentX - currentX;
                f2Integral += f2StepIntegral;
                fPrime2Integral += fPrime2StepIntegral;
                sx2 += x2 * x2;
                sy2 += f2Integral * f2Integral;
                sxy += x2 * f2Integral;
                sxz += x2 * fPrime2Integral;
                syz += f2Integral * fPrime2Integral;
            }
            double c1 = (sy2 * sxz) - (sxy * syz);
            double c2 = (sxy * sxz) - (sx2 * syz);
            double c3 = (sx2 * sy2) - (sxy * sxy);
            if (c1 / c2 < 0.0d || c2 / c3 < 0.0d) {
                int last = observations.length - 1;
                double xRange = observations[last].getX() - observations[0].getX();
                if (xRange == 0.0d) {
                    throw new ZeroException();
                }
                aOmega[1] = 6.283185307179586d / xRange;
                double yMin = Double.POSITIVE_INFINITY;
                double yMax = Double.NEGATIVE_INFINITY;
                for (int i3 = 1; i3 < observations.length; i3++) {
                    double y2 = observations[i3].getY();
                    if (y2 < yMin) {
                        yMin = y2;
                    }
                    if (y2 > yMax) {
                        yMax = y2;
                    }
                }
                aOmega[0] = 0.5d * (yMax - yMin);
            } else {
                if (c2 == 0.0d) {
                    throw new MathIllegalStateException(LocalizedFormats.ZERO_DENOMINATOR, new Object[0]);
                }
                aOmega[0] = FastMath.sqrt(c1 / c2);
                aOmega[1] = FastMath.sqrt(c2 / c3);
            }
            return aOmega;
        }

        private double guessPhi(WeightedObservedPoint[] observations) {
            double fcMean = 0.0d;
            double fsMean = 0.0d;
            double currentX = observations[0].getX();
            double currentY = observations[0].getY();
            for (int i2 = 1; i2 < observations.length; i2++) {
                double previousX = currentX;
                double previousY = currentY;
                currentX = observations[i2].getX();
                currentY = observations[i2].getY();
                double currentYPrime = (currentY - previousY) / (currentX - previousX);
                double omegaX = this.omega * currentX;
                double cosine = FastMath.cos(omegaX);
                double sine = FastMath.sin(omegaX);
                fcMean += ((this.omega * currentY) * cosine) - (currentYPrime * sine);
                fsMean += (this.omega * currentY * sine) + (currentYPrime * cosine);
            }
            return FastMath.atan2(-fsMean, fcMean);
        }
    }
}
