package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.BivariateFunction;
import org.apache.commons.math3.exception.OutOfRangeException;

/* compiled from: BicubicSplineInterpolatingFunction.java */
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/BicubicSplineFunction.class */
class BicubicSplineFunction implements BivariateFunction {

    /* renamed from: N, reason: collision with root package name */
    private static final short f12968N = 4;

    /* renamed from: a, reason: collision with root package name */
    private final double[][] f12969a;
    private final BivariateFunction partialDerivativeX;
    private final BivariateFunction partialDerivativeY;
    private final BivariateFunction partialDerivativeXX;
    private final BivariateFunction partialDerivativeYY;
    private final BivariateFunction partialDerivativeXY;

    BicubicSplineFunction(double[] coeff) {
        this(coeff, false);
    }

    BicubicSplineFunction(double[] coeff, boolean initializeDerivatives) {
        this.f12969a = new double[4][4];
        for (int i2 = 0; i2 < 4; i2++) {
            for (int j2 = 0; j2 < 4; j2++) {
                this.f12969a[i2][j2] = coeff[(i2 * 4) + j2];
            }
        }
        if (initializeDerivatives) {
            final double[][] aX2 = new double[4][4];
            final double[][] aY2 = new double[4][4];
            final double[][] aXX = new double[4][4];
            final double[][] aYY = new double[4][4];
            final double[][] aXY = new double[4][4];
            for (int i3 = 0; i3 < 4; i3++) {
                for (int j3 = 0; j3 < 4; j3++) {
                    double c2 = this.f12969a[i3][j3];
                    aX2[i3][j3] = i3 * c2;
                    aY2[i3][j3] = j3 * c2;
                    aXX[i3][j3] = (i3 - 1) * aX2[i3][j3];
                    aYY[i3][j3] = (j3 - 1) * aY2[i3][j3];
                    aXY[i3][j3] = j3 * aX2[i3][j3];
                }
            }
            this.partialDerivativeX = new BivariateFunction() { // from class: org.apache.commons.math3.analysis.interpolation.BicubicSplineFunction.1
                @Override // org.apache.commons.math3.analysis.BivariateFunction
                public double value(double x2, double y2) {
                    double x22 = x2 * x2;
                    double[] pX = {0.0d, 1.0d, x2, x22};
                    double y22 = y2 * y2;
                    double y3 = y22 * y2;
                    double[] pY = {1.0d, y2, y22, y3};
                    return BicubicSplineFunction.this.apply(pX, pY, aX2);
                }
            };
            this.partialDerivativeY = new BivariateFunction() { // from class: org.apache.commons.math3.analysis.interpolation.BicubicSplineFunction.2
                @Override // org.apache.commons.math3.analysis.BivariateFunction
                public double value(double x2, double y2) {
                    double x22 = x2 * x2;
                    double x3 = x22 * x2;
                    double[] pX = {1.0d, x2, x22, x3};
                    double y22 = y2 * y2;
                    double[] pY = {0.0d, 1.0d, y2, y22};
                    return BicubicSplineFunction.this.apply(pX, pY, aY2);
                }
            };
            this.partialDerivativeXX = new BivariateFunction() { // from class: org.apache.commons.math3.analysis.interpolation.BicubicSplineFunction.3
                @Override // org.apache.commons.math3.analysis.BivariateFunction
                public double value(double x2, double y2) {
                    double[] pX = {0.0d, 0.0d, 1.0d, x2};
                    double y22 = y2 * y2;
                    double y3 = y22 * y2;
                    double[] pY = {1.0d, y2, y22, y3};
                    return BicubicSplineFunction.this.apply(pX, pY, aXX);
                }
            };
            this.partialDerivativeYY = new BivariateFunction() { // from class: org.apache.commons.math3.analysis.interpolation.BicubicSplineFunction.4
                @Override // org.apache.commons.math3.analysis.BivariateFunction
                public double value(double x2, double y2) {
                    double x22 = x2 * x2;
                    double x3 = x22 * x2;
                    double[] pX = {1.0d, x2, x22, x3};
                    double[] pY = {0.0d, 0.0d, 1.0d, y2};
                    return BicubicSplineFunction.this.apply(pX, pY, aYY);
                }
            };
            this.partialDerivativeXY = new BivariateFunction() { // from class: org.apache.commons.math3.analysis.interpolation.BicubicSplineFunction.5
                @Override // org.apache.commons.math3.analysis.BivariateFunction
                public double value(double x2, double y2) {
                    double x22 = x2 * x2;
                    double[] pX = {0.0d, 1.0d, x2, x22};
                    double y22 = y2 * y2;
                    double[] pY = {0.0d, 1.0d, y2, y22};
                    return BicubicSplineFunction.this.apply(pX, pY, aXY);
                }
            };
            return;
        }
        this.partialDerivativeX = null;
        this.partialDerivativeY = null;
        this.partialDerivativeXX = null;
        this.partialDerivativeYY = null;
        this.partialDerivativeXY = null;
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
        return apply(pX, pY, this.f12969a);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double apply(double[] pX, double[] pY, double[][] coeff) {
        double result = 0.0d;
        for (int i2 = 0; i2 < 4; i2++) {
            for (int j2 = 0; j2 < 4; j2++) {
                result += coeff[i2][j2] * pX[i2] * pY[j2];
            }
        }
        return result;
    }

    public BivariateFunction partialDerivativeX() {
        return this.partialDerivativeX;
    }

    public BivariateFunction partialDerivativeY() {
        return this.partialDerivativeY;
    }

    public BivariateFunction partialDerivativeXX() {
        return this.partialDerivativeXX;
    }

    public BivariateFunction partialDerivativeYY() {
        return this.partialDerivativeYY;
    }

    public BivariateFunction partialDerivativeXY() {
        return this.partialDerivativeXY;
    }
}
