package org.apache.commons.math3.analysis.polynomials;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/polynomials/PolynomialFunctionLagrangeForm.class */
public class PolynomialFunctionLagrangeForm implements UnivariateFunction {
    private double[] coefficients;

    /* renamed from: x, reason: collision with root package name */
    private final double[] f12974x;

    /* renamed from: y, reason: collision with root package name */
    private final double[] f12975y;
    private boolean coefficientsComputed;

    /* JADX WARN: Type inference failed for: r1v11, types: [double[], double[][]] */
    public PolynomialFunctionLagrangeForm(double[] x2, double[] y2) throws NumberIsTooSmallException, NullArgumentException, DimensionMismatchException, NonMonotonicSequenceException {
        this.f12974x = new double[x2.length];
        this.f12975y = new double[y2.length];
        System.arraycopy(x2, 0, this.f12974x, 0, x2.length);
        System.arraycopy(y2, 0, this.f12975y, 0, y2.length);
        this.coefficientsComputed = false;
        if (!verifyInterpolationArray(x2, y2, false)) {
            MathArrays.sortInPlace(this.f12974x, new double[]{this.f12975y});
            verifyInterpolationArray(this.f12974x, this.f12975y, true);
        }
    }

    @Override // org.apache.commons.math3.analysis.UnivariateFunction
    public double value(double z2) {
        return evaluateInternal(this.f12974x, this.f12975y, z2);
    }

    public int degree() {
        return this.f12974x.length - 1;
    }

    public double[] getInterpolatingPoints() {
        double[] out = new double[this.f12974x.length];
        System.arraycopy(this.f12974x, 0, out, 0, this.f12974x.length);
        return out;
    }

    public double[] getInterpolatingValues() {
        double[] out = new double[this.f12975y.length];
        System.arraycopy(this.f12975y, 0, out, 0, this.f12975y.length);
        return out;
    }

    public double[] getCoefficients() {
        if (!this.coefficientsComputed) {
            computeCoefficients();
        }
        double[] out = new double[this.coefficients.length];
        System.arraycopy(this.coefficients, 0, out, 0, this.coefficients.length);
        return out;
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [double[], double[][]] */
    public static double evaluate(double[] x2, double[] y2, double z2) throws NumberIsTooSmallException, NullArgumentException, DimensionMismatchException, NonMonotonicSequenceException {
        if (verifyInterpolationArray(x2, y2, false)) {
            return evaluateInternal(x2, y2, z2);
        }
        double[] xNew = new double[x2.length];
        double[] yNew = new double[y2.length];
        System.arraycopy(x2, 0, xNew, 0, x2.length);
        System.arraycopy(y2, 0, yNew, 0, y2.length);
        MathArrays.sortInPlace(xNew, new double[]{yNew});
        verifyInterpolationArray(xNew, yNew, true);
        return evaluateInternal(xNew, yNew, z2);
    }

    private static double evaluateInternal(double[] x2, double[] y2, double z2) {
        double d2;
        double d3;
        int nearest = 0;
        int n2 = x2.length;
        double[] c2 = new double[n2];
        double[] d4 = new double[n2];
        double min_dist = Double.POSITIVE_INFINITY;
        for (int i2 = 0; i2 < n2; i2++) {
            c2[i2] = y2[i2];
            d4[i2] = y2[i2];
            double dist = FastMath.abs(z2 - x2[i2]);
            if (dist < min_dist) {
                nearest = i2;
                min_dist = dist;
            }
        }
        double value = y2[nearest];
        for (int i3 = 1; i3 < n2; i3++) {
            for (int j2 = 0; j2 < n2 - i3; j2++) {
                double tc = x2[j2] - z2;
                double td = x2[i3 + j2] - z2;
                double divider = x2[j2] - x2[i3 + j2];
                double w2 = (c2[j2 + 1] - d4[j2]) / divider;
                c2[j2] = tc * w2;
                d4[j2] = td * w2;
            }
            if (nearest < 0.5d * ((n2 - i3) + 1)) {
                d2 = value;
                d3 = c2[nearest];
            } else {
                nearest--;
                d2 = value;
                d3 = d4[nearest];
            }
            value = d2 + d3;
        }
        return value;
    }

    protected void computeCoefficients() {
        int n2 = degree() + 1;
        this.coefficients = new double[n2];
        for (int i2 = 0; i2 < n2; i2++) {
            this.coefficients[i2] = 0.0d;
        }
        double[] c2 = new double[n2 + 1];
        c2[0] = 1.0d;
        for (int i3 = 0; i3 < n2; i3++) {
            for (int j2 = i3; j2 > 0; j2--) {
                c2[j2] = c2[j2 - 1] - (c2[j2] * this.f12974x[i3]);
            }
            c2[0] = c2[0] * (-this.f12974x[i3]);
            c2[i3 + 1] = 1.0d;
        }
        double[] tc = new double[n2];
        for (int i4 = 0; i4 < n2; i4++) {
            double d2 = 1.0d;
            for (int j3 = 0; j3 < n2; j3++) {
                if (i4 != j3) {
                    d2 *= this.f12974x[i4] - this.f12974x[j3];
                }
            }
            double t2 = this.f12975y[i4] / d2;
            tc[n2 - 1] = c2[n2];
            double[] dArr = this.coefficients;
            int i5 = n2 - 1;
            dArr[i5] = dArr[i5] + (t2 * tc[n2 - 1]);
            for (int j4 = n2 - 2; j4 >= 0; j4--) {
                tc[j4] = c2[j4 + 1] + (tc[j4 + 1] * this.f12974x[i4]);
                double[] dArr2 = this.coefficients;
                int i6 = j4;
                dArr2[i6] = dArr2[i6] + (t2 * tc[j4]);
            }
        }
        this.coefficientsComputed = true;
    }

    public static boolean verifyInterpolationArray(double[] x2, double[] y2, boolean abort) throws NumberIsTooSmallException, DimensionMismatchException, NonMonotonicSequenceException {
        if (x2.length != y2.length) {
            throw new DimensionMismatchException(x2.length, y2.length);
        }
        if (x2.length < 2) {
            throw new NumberIsTooSmallException(LocalizedFormats.WRONG_NUMBER_OF_POINTS, 2, Integer.valueOf(x2.length), true);
        }
        return MathArrays.checkOrder(x2, MathArrays.OrderDirection.INCREASING, true, abort);
    }
}
