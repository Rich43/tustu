package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.MathArrays;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/BicubicSplineInterpolator.class */
public class BicubicSplineInterpolator implements BivariateGridInterpolator {
    private final boolean initializeDerivatives;

    public BicubicSplineInterpolator() {
        this(false);
    }

    public BicubicSplineInterpolator(boolean initializeDerivatives) {
        this.initializeDerivatives = initializeDerivatives;
    }

    @Override // org.apache.commons.math3.analysis.interpolation.BivariateGridInterpolator
    public BicubicSplineInterpolatingFunction interpolate(double[] xval, double[] yval, double[][] fval) throws NumberIsTooSmallException, NoDataException, NonMonotonicSequenceException, DimensionMismatchException {
        if (xval.length == 0 || yval.length == 0 || fval.length == 0) {
            throw new NoDataException();
        }
        if (xval.length != fval.length) {
            throw new DimensionMismatchException(xval.length, fval.length);
        }
        MathArrays.checkOrder(xval);
        MathArrays.checkOrder(yval);
        int xLen = xval.length;
        int yLen = yval.length;
        double[][] fX = new double[yLen][xLen];
        for (int i2 = 0; i2 < xLen; i2++) {
            if (fval[i2].length != yLen) {
                throw new DimensionMismatchException(fval[i2].length, yLen);
            }
            for (int j2 = 0; j2 < yLen; j2++) {
                fX[j2][i2] = fval[i2][j2];
            }
        }
        SplineInterpolator spInterpolator = new SplineInterpolator();
        PolynomialSplineFunction[] ySplineX = new PolynomialSplineFunction[yLen];
        for (int j3 = 0; j3 < yLen; j3++) {
            ySplineX[j3] = spInterpolator.interpolate(xval, fX[j3]);
        }
        PolynomialSplineFunction[] xSplineY = new PolynomialSplineFunction[xLen];
        for (int i3 = 0; i3 < xLen; i3++) {
            xSplineY[i3] = spInterpolator.interpolate(yval, fval[i3]);
        }
        double[][] dFdX = new double[xLen][yLen];
        for (int j4 = 0; j4 < yLen; j4++) {
            UnivariateFunction f2 = ySplineX[j4].derivative();
            for (int i4 = 0; i4 < xLen; i4++) {
                dFdX[i4][j4] = f2.value(xval[i4]);
            }
        }
        double[][] dFdY = new double[xLen][yLen];
        for (int i5 = 0; i5 < xLen; i5++) {
            UnivariateFunction f3 = xSplineY[i5].derivative();
            for (int j5 = 0; j5 < yLen; j5++) {
                dFdY[i5][j5] = f3.value(yval[j5]);
            }
        }
        double[][] d2FdXdY = new double[xLen][yLen];
        for (int i6 = 0; i6 < xLen; i6++) {
            int nI = nextIndex(i6, xLen);
            int pI = previousIndex(i6);
            for (int j6 = 0; j6 < yLen; j6++) {
                int nJ = nextIndex(j6, yLen);
                int pJ = previousIndex(j6);
                d2FdXdY[i6][j6] = (((fval[nI][nJ] - fval[nI][pJ]) - fval[pI][nJ]) + fval[pI][pJ]) / ((xval[nI] - xval[pI]) * (yval[nJ] - yval[pJ]));
            }
        }
        return new BicubicSplineInterpolatingFunction(xval, yval, fval, dFdX, dFdY, d2FdXdY, this.initializeDerivatives);
    }

    private int nextIndex(int i2, int max) {
        int index = i2 + 1;
        return index < max ? index : index - 1;
    }

    private int previousIndex(int i2) {
        int index = i2 - 1;
        if (index >= 0) {
            return index;
        }
        return 0;
    }
}
