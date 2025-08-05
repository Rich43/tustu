package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.fitting.PolynomialFitter;
import org.apache.commons.math3.optim.SimpleVectorValueChecker;
import org.apache.commons.math3.optim.nonlinear.vector.jacobian.GaussNewtonOptimizer;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.Precision;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/SmoothingPolynomialBicubicSplineInterpolator.class */
public class SmoothingPolynomialBicubicSplineInterpolator extends BicubicSplineInterpolator {
    private final PolynomialFitter xFitter;
    private final int xDegree;
    private final PolynomialFitter yFitter;
    private final int yDegree;

    public SmoothingPolynomialBicubicSplineInterpolator() {
        this(3);
    }

    public SmoothingPolynomialBicubicSplineInterpolator(int degree) throws NotPositiveException {
        this(degree, degree);
    }

    public SmoothingPolynomialBicubicSplineInterpolator(int xDegree, int yDegree) throws NotPositiveException {
        if (xDegree < 0) {
            throw new NotPositiveException(Integer.valueOf(xDegree));
        }
        if (yDegree < 0) {
            throw new NotPositiveException(Integer.valueOf(yDegree));
        }
        this.xDegree = xDegree;
        this.yDegree = yDegree;
        SimpleVectorValueChecker checker = new SimpleVectorValueChecker(100.0d * Precision.EPSILON, 100.0d * Precision.SAFE_MIN);
        this.xFitter = new PolynomialFitter(new GaussNewtonOptimizer(false, checker));
        this.yFitter = new PolynomialFitter(new GaussNewtonOptimizer(false, checker));
    }

    @Override // org.apache.commons.math3.analysis.interpolation.BicubicSplineInterpolator, org.apache.commons.math3.analysis.interpolation.BivariateGridInterpolator
    public BicubicSplineInterpolatingFunction interpolate(double[] xval, double[] yval, double[][] fval) throws NullArgumentException, NoDataException, NonMonotonicSequenceException, DimensionMismatchException {
        if (xval.length == 0 || yval.length == 0 || fval.length == 0) {
            throw new NoDataException();
        }
        if (xval.length != fval.length) {
            throw new DimensionMismatchException(xval.length, fval.length);
        }
        int xLen = xval.length;
        int yLen = yval.length;
        for (int i2 = 0; i2 < xLen; i2++) {
            if (fval[i2].length != yLen) {
                throw new DimensionMismatchException(fval[i2].length, yLen);
            }
        }
        MathArrays.checkOrder(xval);
        MathArrays.checkOrder(yval);
        PolynomialFunction[] yPolyX = new PolynomialFunction[yLen];
        for (int j2 = 0; j2 < yLen; j2++) {
            this.xFitter.clearObservations();
            for (int i3 = 0; i3 < xLen; i3++) {
                this.xFitter.addObservedPoint(1.0d, xval[i3], fval[i3][j2]);
            }
            yPolyX[j2] = new PolynomialFunction(this.xFitter.fit(new double[this.xDegree + 1]));
        }
        double[][] fval_1 = new double[xLen][yLen];
        for (int j3 = 0; j3 < yLen; j3++) {
            PolynomialFunction f2 = yPolyX[j3];
            for (int i4 = 0; i4 < xLen; i4++) {
                fval_1[i4][j3] = f2.value(xval[i4]);
            }
        }
        PolynomialFunction[] xPolyY = new PolynomialFunction[xLen];
        for (int i5 = 0; i5 < xLen; i5++) {
            this.yFitter.clearObservations();
            for (int j4 = 0; j4 < yLen; j4++) {
                this.yFitter.addObservedPoint(1.0d, yval[j4], fval_1[i5][j4]);
            }
            xPolyY[i5] = new PolynomialFunction(this.yFitter.fit(new double[this.yDegree + 1]));
        }
        double[][] fval_2 = new double[xLen][yLen];
        for (int i6 = 0; i6 < xLen; i6++) {
            PolynomialFunction f3 = xPolyY[i6];
            for (int j5 = 0; j5 < yLen; j5++) {
                fval_2[i6][j5] = f3.value(yval[j5]);
            }
        }
        return super.interpolate(xval, yval, fval_2);
    }
}
