package org.apache.commons.math3.analysis.interpolation;

import java.io.Serializable;
import java.util.Arrays;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NotFiniteNumberException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/LoessInterpolator.class */
public class LoessInterpolator implements UnivariateInterpolator, Serializable {
    public static final double DEFAULT_BANDWIDTH = 0.3d;
    public static final int DEFAULT_ROBUSTNESS_ITERS = 2;
    public static final double DEFAULT_ACCURACY = 1.0E-12d;
    private static final long serialVersionUID = 5204927143605193821L;
    private final double bandwidth;
    private final int robustnessIters;
    private final double accuracy;

    public LoessInterpolator() {
        this.bandwidth = 0.3d;
        this.robustnessIters = 2;
        this.accuracy = 1.0E-12d;
    }

    public LoessInterpolator(double bandwidth, int robustnessIters) {
        this(bandwidth, robustnessIters, 1.0E-12d);
    }

    public LoessInterpolator(double bandwidth, int robustnessIters, double accuracy) throws OutOfRangeException, NotPositiveException {
        if (bandwidth < 0.0d || bandwidth > 1.0d) {
            throw new OutOfRangeException(LocalizedFormats.BANDWIDTH, Double.valueOf(bandwidth), 0, 1);
        }
        this.bandwidth = bandwidth;
        if (robustnessIters < 0) {
            throw new NotPositiveException(LocalizedFormats.ROBUSTNESS_ITERATIONS, Integer.valueOf(robustnessIters));
        }
        this.robustnessIters = robustnessIters;
        this.accuracy = accuracy;
    }

    @Override // org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator
    public final PolynomialSplineFunction interpolate(double[] xval, double[] yval) throws NumberIsTooSmallException, NoDataException, NotFiniteNumberException, NonMonotonicSequenceException, DimensionMismatchException {
        return new SplineInterpolator().interpolate(xval, smooth(xval, yval));
    }

    public final double[] smooth(double[] xval, double[] yval, double[] weights) throws NumberIsTooSmallException, NoDataException, NotFiniteNumberException, NonMonotonicSequenceException, DimensionMismatchException {
        int edge;
        double d2;
        double d3;
        double d4;
        if (xval.length != yval.length) {
            throw new DimensionMismatchException(xval.length, yval.length);
        }
        int n2 = xval.length;
        if (n2 == 0) {
            throw new NoDataException();
        }
        checkAllFiniteReal(xval);
        checkAllFiniteReal(yval);
        checkAllFiniteReal(weights);
        MathArrays.checkOrder(xval);
        if (n2 == 1) {
            return new double[]{yval[0]};
        }
        if (n2 == 2) {
            return new double[]{yval[0], yval[1]};
        }
        int bandwidthInPoints = (int) (this.bandwidth * n2);
        if (bandwidthInPoints < 2) {
            throw new NumberIsTooSmallException(LocalizedFormats.BANDWIDTH, Integer.valueOf(bandwidthInPoints), 2, true);
        }
        double[] res = new double[n2];
        double[] residuals = new double[n2];
        double[] sortedResiduals = new double[n2];
        double[] robustnessWeights = new double[n2];
        Arrays.fill(robustnessWeights, 1.0d);
        for (int iter = 0; iter <= this.robustnessIters; iter++) {
            int[] bandwidthInterval = {0, bandwidthInPoints - 1};
            for (int i2 = 0; i2 < n2; i2++) {
                double x2 = xval[i2];
                if (i2 > 0) {
                    updateBandwidthInterval(xval, weights, i2, bandwidthInterval);
                }
                int ileft = bandwidthInterval[0];
                int iright = bandwidthInterval[1];
                if (xval[i2] - xval[ileft] > xval[iright] - xval[i2]) {
                    edge = ileft;
                } else {
                    edge = iright;
                }
                double sumWeights = 0.0d;
                double sumX = 0.0d;
                double sumXSquared = 0.0d;
                double sumY = 0.0d;
                double sumXY = 0.0d;
                double denom = FastMath.abs(1.0d / (xval[edge] - x2));
                for (int k2 = ileft; k2 <= iright; k2++) {
                    double xk = xval[k2];
                    double yk = yval[k2];
                    if (k2 < i2) {
                        d3 = x2;
                        d4 = xk;
                    } else {
                        d3 = xk;
                        d4 = x2;
                    }
                    double dist = d3 - d4;
                    double w2 = tricube(dist * denom) * robustnessWeights[k2] * weights[k2];
                    double xkw = xk * w2;
                    sumWeights += w2;
                    sumX += xkw;
                    sumXSquared += xk * xkw;
                    sumY += yk * w2;
                    sumXY += yk * xkw;
                }
                double meanX = sumX / sumWeights;
                double meanY = sumY / sumWeights;
                double meanXY = sumXY / sumWeights;
                double meanXSquared = sumXSquared / sumWeights;
                if (FastMath.sqrt(FastMath.abs(meanXSquared - (meanX * meanX))) < this.accuracy) {
                    d2 = 0.0d;
                } else {
                    d2 = (meanXY - (meanX * meanY)) / (meanXSquared - (meanX * meanX));
                }
                double beta = d2;
                double alpha = meanY - (beta * meanX);
                res[i2] = (beta * x2) + alpha;
                residuals[i2] = FastMath.abs(yval[i2] - res[i2]);
            }
            if (iter == this.robustnessIters) {
                break;
            }
            System.arraycopy(residuals, 0, sortedResiduals, 0, n2);
            Arrays.sort(sortedResiduals);
            double medianResidual = sortedResiduals[n2 / 2];
            if (FastMath.abs(medianResidual) < this.accuracy) {
                break;
            }
            for (int i3 = 0; i3 < n2; i3++) {
                double arg = residuals[i3] / (6.0d * medianResidual);
                if (arg >= 1.0d) {
                    robustnessWeights[i3] = 0.0d;
                } else {
                    double w3 = 1.0d - (arg * arg);
                    robustnessWeights[i3] = w3 * w3;
                }
            }
        }
        return res;
    }

    public final double[] smooth(double[] xval, double[] yval) throws NumberIsTooSmallException, NoDataException, NotFiniteNumberException, NonMonotonicSequenceException, DimensionMismatchException {
        if (xval.length != yval.length) {
            throw new DimensionMismatchException(xval.length, yval.length);
        }
        double[] unitWeights = new double[xval.length];
        Arrays.fill(unitWeights, 1.0d);
        return smooth(xval, yval, unitWeights);
    }

    private static void updateBandwidthInterval(double[] xval, double[] weights, int i2, int[] bandwidthInterval) {
        int left = bandwidthInterval[0];
        int right = bandwidthInterval[1];
        int nextRight = nextNonzero(weights, right);
        if (nextRight < xval.length && xval[nextRight] - xval[i2] < xval[i2] - xval[left]) {
            int nextLeft = nextNonzero(weights, bandwidthInterval[0]);
            bandwidthInterval[0] = nextLeft;
            bandwidthInterval[1] = nextRight;
        }
    }

    private static int nextNonzero(double[] weights, int i2) {
        int j2 = i2 + 1;
        while (j2 < weights.length && weights[j2] == 0.0d) {
            j2++;
        }
        return j2;
    }

    private static double tricube(double x2) {
        double absX = FastMath.abs(x2);
        if (absX >= 1.0d) {
            return 0.0d;
        }
        double tmp = 1.0d - ((absX * absX) * absX);
        return tmp * tmp * tmp;
    }

    private static void checkAllFiniteReal(double[] values) throws NotFiniteNumberException {
        for (double d2 : values) {
            MathUtils.checkFinite(d2);
        }
    }
}
