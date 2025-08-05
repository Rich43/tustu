package org.apache.commons.math3.optimization.fitting;

import java.util.Arrays;
import java.util.Comparator;
import org.apache.commons.math3.analysis.function.Gaussian;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.ZeroException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.optimization.DifferentiableMultivariateVectorOptimizer;
import org.apache.commons.math3.util.FastMath;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/fitting/GaussianFitter.class */
public class GaussianFitter extends CurveFitter<Gaussian.Parametric> {
    public GaussianFitter(DifferentiableMultivariateVectorOptimizer optimizer) {
        super(optimizer);
    }

    public double[] fit(double[] initialGuess) {
        Gaussian.Parametric f2 = new Gaussian.Parametric() { // from class: org.apache.commons.math3.optimization.fitting.GaussianFitter.1
            @Override // org.apache.commons.math3.analysis.function.Gaussian.Parametric, org.apache.commons.math3.analysis.ParametricUnivariateFunction
            public double value(double x2, double... p2) throws NullArgumentException, DimensionMismatchException {
                double v2 = Double.POSITIVE_INFINITY;
                try {
                    v2 = super.value(x2, p2);
                } catch (NotStrictlyPositiveException e2) {
                }
                return v2;
            }

            @Override // org.apache.commons.math3.analysis.function.Gaussian.Parametric, org.apache.commons.math3.analysis.ParametricUnivariateFunction
            public double[] gradient(double x2, double... p2) throws NullArgumentException, DimensionMismatchException {
                double[] v2 = {Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY};
                try {
                    v2 = super.gradient(x2, p2);
                } catch (NotStrictlyPositiveException e2) {
                }
                return v2;
            }
        };
        return fit(f2, initialGuess);
    }

    public double[] fit() {
        double[] guess = new ParameterGuesser(getObservations()).guess();
        return fit(guess);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/fitting/GaussianFitter$ParameterGuesser.class */
    public static class ParameterGuesser {
        private final double norm;
        private final double mean;
        private final double sigma;

        public ParameterGuesser(WeightedObservedPoint[] observations) {
            if (observations == null) {
                throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
            }
            if (observations.length < 3) {
                throw new NumberIsTooSmallException(Integer.valueOf(observations.length), 3, true);
            }
            WeightedObservedPoint[] sorted = sortObservations(observations);
            double[] params = basicGuess(sorted);
            this.norm = params[0];
            this.mean = params[1];
            this.sigma = params[2];
        }

        public double[] guess() {
            return new double[]{this.norm, this.mean, this.sigma};
        }

        private WeightedObservedPoint[] sortObservations(WeightedObservedPoint[] unsorted) {
            WeightedObservedPoint[] observations = (WeightedObservedPoint[]) unsorted.clone();
            Comparator<WeightedObservedPoint> cmp = new Comparator<WeightedObservedPoint>() { // from class: org.apache.commons.math3.optimization.fitting.GaussianFitter.ParameterGuesser.1
                @Override // java.util.Comparator
                public int compare(WeightedObservedPoint p1, WeightedObservedPoint p2) {
                    if (p1 == null && p2 == null) {
                        return 0;
                    }
                    if (p1 == null) {
                        return -1;
                    }
                    if (p2 == null) {
                        return 1;
                    }
                    int cmpX = Double.compare(p1.getX(), p2.getX());
                    if (cmpX < 0) {
                        return -1;
                    }
                    if (cmpX > 0) {
                        return 1;
                    }
                    int cmpY = Double.compare(p1.getY(), p2.getY());
                    if (cmpY < 0) {
                        return -1;
                    }
                    if (cmpY > 0) {
                        return 1;
                    }
                    int cmpW = Double.compare(p1.getWeight(), p2.getWeight());
                    if (cmpW < 0) {
                        return -1;
                    }
                    if (cmpW > 0) {
                        return 1;
                    }
                    return 0;
                }
            };
            Arrays.sort(observations, cmp);
            return observations;
        }

        private double[] basicGuess(WeightedObservedPoint[] points) {
            double fwhmApprox;
            int maxYIdx = findMaxY(points);
            double n2 = points[maxYIdx].getY();
            double m2 = points[maxYIdx].getX();
            try {
                double halfY = n2 + ((m2 - n2) / 2.0d);
                double fwhmX1 = interpolateXAtY(points, maxYIdx, -1, halfY);
                double fwhmX2 = interpolateXAtY(points, maxYIdx, 1, halfY);
                fwhmApprox = fwhmX2 - fwhmX1;
            } catch (OutOfRangeException e2) {
                fwhmApprox = points[points.length - 1].getX() - points[0].getX();
            }
            double s2 = fwhmApprox / (2.0d * FastMath.sqrt(2.0d * FastMath.log(2.0d)));
            return new double[]{n2, m2, s2};
        }

        private int findMaxY(WeightedObservedPoint[] points) {
            int maxYIdx = 0;
            for (int i2 = 1; i2 < points.length; i2++) {
                if (points[i2].getY() > points[maxYIdx].getY()) {
                    maxYIdx = i2;
                }
            }
            return maxYIdx;
        }

        private double interpolateXAtY(WeightedObservedPoint[] points, int startIdx, int idxStep, double y2) throws OutOfRangeException {
            if (idxStep == 0) {
                throw new ZeroException();
            }
            WeightedObservedPoint[] twoPoints = getInterpolationPointsForY(points, startIdx, idxStep, y2);
            WeightedObservedPoint p1 = twoPoints[0];
            WeightedObservedPoint p2 = twoPoints[1];
            if (p1.getY() == y2) {
                return p1.getX();
            }
            if (p2.getY() == y2) {
                return p2.getX();
            }
            return p1.getX() + (((y2 - p1.getY()) * (p2.getX() - p1.getX())) / (p2.getY() - p1.getY()));
        }

        /* JADX WARN: Code restructure failed: missing block: B:24:0x008a, code lost:
        
            throw new org.apache.commons.math3.exception.OutOfRangeException(java.lang.Double.valueOf(r12), java.lang.Double.valueOf(Double.NEGATIVE_INFINITY), java.lang.Double.valueOf(Double.POSITIVE_INFINITY));
         */
        /* JADX WARN: Removed duplicated region for block: B:22:0x0069 A[LOOP:0: B:7:0x000f->B:22:0x0069, LOOP_END] */
        /* JADX WARN: Removed duplicated region for block: B:27:0x0047 A[SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct code enable 'Show inconsistent code' option in preferences
        */
        private org.apache.commons.math3.optimization.fitting.WeightedObservedPoint[] getInterpolationPointsForY(org.apache.commons.math3.optimization.fitting.WeightedObservedPoint[] r9, int r10, int r11, double r12) throws org.apache.commons.math3.exception.OutOfRangeException {
            /*
                r8 = this;
                r0 = r11
                if (r0 != 0) goto Lc
                org.apache.commons.math3.exception.ZeroException r0 = new org.apache.commons.math3.exception.ZeroException
                r1 = r0
                r1.<init>()
                throw r0
            Lc:
                r0 = r10
                r14 = r0
            Lf:
                r0 = r11
                if (r0 >= 0) goto L1d
                r0 = r14
                r1 = r11
                int r0 = r0 + r1
                if (r0 < 0) goto L72
                goto L26
            L1d:
                r0 = r14
                r1 = r11
                int r0 = r0 + r1
                r1 = r9
                int r1 = r1.length
                if (r0 >= r1) goto L72
            L26:
                r0 = r9
                r1 = r14
                r0 = r0[r1]
                r15 = r0
                r0 = r9
                r1 = r14
                r2 = r11
                int r1 = r1 + r2
                r0 = r0[r1]
                r16 = r0
                r0 = r8
                r1 = r12
                r2 = r15
                double r2 = r2.getY()
                r3 = r16
                double r3 = r3.getY()
                boolean r0 = r0.isBetween(r1, r2, r3)
                if (r0 == 0) goto L69
                r0 = r11
                if (r0 >= 0) goto L5a
                r0 = 2
                org.apache.commons.math3.optimization.fitting.WeightedObservedPoint[] r0 = new org.apache.commons.math3.optimization.fitting.WeightedObservedPoint[r0]
                r1 = r0
                r2 = 0
                r3 = r16
                r1[r2] = r3
                r1 = r0
                r2 = 1
                r3 = r15
                r1[r2] = r3
                return r0
            L5a:
                r0 = 2
                org.apache.commons.math3.optimization.fitting.WeightedObservedPoint[] r0 = new org.apache.commons.math3.optimization.fitting.WeightedObservedPoint[r0]
                r1 = r0
                r2 = 0
                r3 = r15
                r1[r2] = r3
                r1 = r0
                r2 = 1
                r3 = r16
                r1[r2] = r3
                return r0
            L69:
                r0 = r14
                r1 = r11
                int r0 = r0 + r1
                r14 = r0
                goto Lf
            L72:
                org.apache.commons.math3.exception.OutOfRangeException r0 = new org.apache.commons.math3.exception.OutOfRangeException
                r1 = r0
                r2 = r12
                java.lang.Double r2 = java.lang.Double.valueOf(r2)
                r3 = -4503599627370496(0xfff0000000000000, double:-Infinity)
                java.lang.Double r3 = java.lang.Double.valueOf(r3)
                r4 = 9218868437227405312(0x7ff0000000000000, double:Infinity)
                java.lang.Double r4 = java.lang.Double.valueOf(r4)
                r1.<init>(r2, r3, r4)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.optimization.fitting.GaussianFitter.ParameterGuesser.getInterpolationPointsForY(org.apache.commons.math3.optimization.fitting.WeightedObservedPoint[], int, int, double):org.apache.commons.math3.optimization.fitting.WeightedObservedPoint[]");
        }

        private boolean isBetween(double value, double boundary1, double boundary2) {
            return (value >= boundary1 && value <= boundary2) || (value >= boundary2 && value <= boundary1);
        }
    }
}
