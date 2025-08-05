package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/TricubicInterpolator.class */
public class TricubicInterpolator implements TrivariateGridInterpolator {
    @Override // org.apache.commons.math3.analysis.interpolation.TrivariateGridInterpolator
    public TricubicInterpolatingFunction interpolate(final double[] xval, final double[] yval, final double[] zval, double[][][] fval) throws NumberIsTooSmallException, NoDataException, NonMonotonicSequenceException, DimensionMismatchException {
        if (xval.length == 0 || yval.length == 0 || zval.length == 0 || fval.length == 0) {
            throw new NoDataException();
        }
        if (xval.length != fval.length) {
            throw new DimensionMismatchException(xval.length, fval.length);
        }
        MathArrays.checkOrder(xval);
        MathArrays.checkOrder(yval);
        MathArrays.checkOrder(zval);
        int xLen = xval.length;
        int yLen = yval.length;
        int zLen = zval.length;
        double[][][] dFdX = new double[xLen][yLen][zLen];
        double[][][] dFdY = new double[xLen][yLen][zLen];
        double[][][] dFdZ = new double[xLen][yLen][zLen];
        double[][][] d2FdXdY = new double[xLen][yLen][zLen];
        double[][][] d2FdXdZ = new double[xLen][yLen][zLen];
        double[][][] d2FdYdZ = new double[xLen][yLen][zLen];
        double[][][] d3FdXdYdZ = new double[xLen][yLen][zLen];
        for (int i2 = 1; i2 < xLen - 1; i2++) {
            if (yval.length != fval[i2].length) {
                throw new DimensionMismatchException(yval.length, fval[i2].length);
            }
            int nI = i2 + 1;
            int pI = i2 - 1;
            double nX = xval[nI];
            double pX = xval[pI];
            double deltaX = nX - pX;
            for (int j2 = 1; j2 < yLen - 1; j2++) {
                if (zval.length != fval[i2][j2].length) {
                    throw new DimensionMismatchException(zval.length, fval[i2][j2].length);
                }
                int nJ = j2 + 1;
                int pJ = j2 - 1;
                double nY = yval[nJ];
                double pY = yval[pJ];
                double deltaY = nY - pY;
                double deltaXY = deltaX * deltaY;
                for (int k2 = 1; k2 < zLen - 1; k2++) {
                    int nK = k2 + 1;
                    int pK = k2 - 1;
                    double nZ = zval[nK];
                    double pZ = zval[pK];
                    double deltaZ = nZ - pZ;
                    dFdX[i2][j2][k2] = (fval[nI][j2][k2] - fval[pI][j2][k2]) / deltaX;
                    dFdY[i2][j2][k2] = (fval[i2][nJ][k2] - fval[i2][pJ][k2]) / deltaY;
                    dFdZ[i2][j2][k2] = (fval[i2][j2][nK] - fval[i2][j2][pK]) / deltaZ;
                    double deltaXZ = deltaX * deltaZ;
                    double deltaYZ = deltaY * deltaZ;
                    d2FdXdY[i2][j2][k2] = (((fval[nI][nJ][k2] - fval[nI][pJ][k2]) - fval[pI][nJ][k2]) + fval[pI][pJ][k2]) / deltaXY;
                    d2FdXdZ[i2][j2][k2] = (((fval[nI][j2][nK] - fval[nI][j2][pK]) - fval[pI][j2][nK]) + fval[pI][j2][pK]) / deltaXZ;
                    d2FdYdZ[i2][j2][k2] = (((fval[i2][nJ][nK] - fval[i2][nJ][pK]) - fval[i2][pJ][nK]) + fval[i2][pJ][pK]) / deltaYZ;
                    double deltaXYZ = deltaXY * deltaZ;
                    d3FdXdYdZ[i2][j2][k2] = (((((((fval[nI][nJ][nK] - fval[nI][pJ][nK]) - fval[pI][nJ][nK]) + fval[pI][pJ][nK]) - fval[nI][nJ][pK]) + fval[nI][pJ][pK]) + fval[pI][nJ][pK]) - fval[pI][pJ][pK]) / deltaXYZ;
                }
            }
        }
        return new TricubicInterpolatingFunction(xval, yval, zval, fval, dFdX, dFdY, dFdZ, d2FdXdY, d2FdXdZ, d2FdYdZ, d3FdXdYdZ) { // from class: org.apache.commons.math3.analysis.interpolation.TricubicInterpolator.1
            @Override // org.apache.commons.math3.analysis.interpolation.TricubicInterpolatingFunction
            public boolean isValidPoint(double x2, double y2, double z2) {
                if (x2 < xval[1] || x2 > xval[xval.length - 2] || y2 < yval[1] || y2 > yval[yval.length - 2] || z2 < zval[1] || z2 > zval[zval.length - 2]) {
                    return false;
                }
                return true;
            }
        };
    }
}
