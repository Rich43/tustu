package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/BicubicInterpolator.class */
public class BicubicInterpolator implements BivariateGridInterpolator {
    @Override // org.apache.commons.math3.analysis.interpolation.BivariateGridInterpolator
    public BicubicInterpolatingFunction interpolate(final double[] xval, final double[] yval, double[][] fval) throws NumberIsTooSmallException, NoDataException, NonMonotonicSequenceException, DimensionMismatchException {
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
        double[][] dFdX = new double[xLen][yLen];
        double[][] dFdY = new double[xLen][yLen];
        double[][] d2FdXdY = new double[xLen][yLen];
        for (int i2 = 1; i2 < xLen - 1; i2++) {
            int nI = i2 + 1;
            int pI = i2 - 1;
            double nX = xval[nI];
            double pX = xval[pI];
            double deltaX = nX - pX;
            for (int j2 = 1; j2 < yLen - 1; j2++) {
                int nJ = j2 + 1;
                int pJ = j2 - 1;
                double nY = yval[nJ];
                double pY = yval[pJ];
                double deltaY = nY - pY;
                dFdX[i2][j2] = (fval[nI][j2] - fval[pI][j2]) / deltaX;
                dFdY[i2][j2] = (fval[i2][nJ] - fval[i2][pJ]) / deltaY;
                double deltaXY = deltaX * deltaY;
                d2FdXdY[i2][j2] = (((fval[nI][nJ] - fval[nI][pJ]) - fval[pI][nJ]) + fval[pI][pJ]) / deltaXY;
            }
        }
        return new BicubicInterpolatingFunction(xval, yval, fval, dFdX, dFdY, d2FdXdY) { // from class: org.apache.commons.math3.analysis.interpolation.BicubicInterpolator.1
            @Override // org.apache.commons.math3.analysis.interpolation.BicubicInterpolatingFunction
            public boolean isValidPoint(double x2, double y2) {
                if (x2 < xval[1] || x2 > xval[xval.length - 2] || y2 < yval[1] || y2 > yval[yval.length - 2]) {
                    return false;
                }
                return true;
            }
        };
    }
}
