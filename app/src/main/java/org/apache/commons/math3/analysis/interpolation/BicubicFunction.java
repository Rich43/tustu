package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.BivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;

/* compiled from: BicubicInterpolatingFunction.java */
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/BicubicFunction.class */
class BicubicFunction implements BivariateFunction {

    /* renamed from: N, reason: collision with root package name */
    private static final short f12966N = 4;

    /* renamed from: a, reason: collision with root package name */
    private final double[][] f12967a = new double[4][4];

    BicubicFunction(double[] coeff) {
        for (int j2 = 0; j2 < 4; j2++) {
            double[] aJ2 = this.f12967a[j2];
            for (int i2 = 0; i2 < 4; i2++) {
                aJ2[i2] = coeff[(i2 * 4) + j2];
            }
        }
    }

    @Override // org.apache.commons.math3.analysis.BivariateFunction
    public double value(double x2, double y2) {
        if (x2 < 0.0d || x2 > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(x2), 0, 1);
        }
        if (y2 < 0.0d || y2 > 1.0d) {
            throw new OutOfRangeException(Double.valueOf(y2), 0, 1);
        }
        double x22 = x2 * x2;
        double x3 = x22 * x2;
        double[] pX = {1.0d, x2, x22, x3};
        double y22 = y2 * y2;
        double y3 = y22 * y2;
        double[] pY = {1.0d, y2, y22, y3};
        return apply(pX, pY, this.f12967a);
    }

    private double apply(double[] pX, double[] pY, double[][] coeff) throws DimensionMismatchException {
        double result = 0.0d;
        for (int i2 = 0; i2 < 4; i2++) {
            double r2 = MathArrays.linearCombination(coeff[i2], pY);
            result += r2 * pX[i2];
        }
        return result;
    }
}
