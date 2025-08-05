package org.apache.commons.math3.analysis.interpolation;

import java.util.Arrays;
import org.apache.commons.math3.analysis.BivariateFunction;
import org.apache.commons.math3.analysis.polynomials.PolynomialSplineFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.InsufficientDataException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/PiecewiseBicubicSplineInterpolatingFunction.class */
public class PiecewiseBicubicSplineInterpolatingFunction implements BivariateFunction {
    private static final int MIN_NUM_POINTS = 5;
    private final double[] xval;
    private final double[] yval;
    private final double[][] fval;

    public PiecewiseBicubicSplineInterpolatingFunction(double[] x2, double[] y2, double[][] f2) throws NullArgumentException, NoDataException, NonMonotonicSequenceException, DimensionMismatchException {
        if (x2 == null || y2 == null || f2 == null || f2[0] == null) {
            throw new NullArgumentException();
        }
        int xLen = x2.length;
        int yLen = y2.length;
        if (xLen == 0 || yLen == 0 || f2.length == 0 || f2[0].length == 0) {
            throw new NoDataException();
        }
        if (xLen < 5 || yLen < 5 || f2.length < 5 || f2[0].length < 5) {
            throw new InsufficientDataException();
        }
        if (xLen != f2.length) {
            throw new DimensionMismatchException(xLen, f2.length);
        }
        if (yLen != f2[0].length) {
            throw new DimensionMismatchException(yLen, f2[0].length);
        }
        MathArrays.checkOrder(x2);
        MathArrays.checkOrder(y2);
        this.xval = (double[]) x2.clone();
        this.yval = (double[]) y2.clone();
        this.fval = (double[][]) f2.clone();
    }

    @Override // org.apache.commons.math3.analysis.BivariateFunction
    public double value(double x2, double y2) throws NumberIsTooSmallException, OutOfRangeException, NonMonotonicSequenceException, DimensionMismatchException {
        AkimaSplineInterpolator interpolator = new AkimaSplineInterpolator();
        int i2 = searchIndex(x2, this.xval, 2, 5);
        int j2 = searchIndex(y2, this.yval, 2, 5);
        double[] xArray = new double[5];
        double[] yArray = new double[5];
        double[] zArray = new double[5];
        double[] interpArray = new double[5];
        for (int index = 0; index < 5; index++) {
            xArray[index] = this.xval[i2 + index];
            yArray[index] = this.yval[j2 + index];
        }
        for (int zIndex = 0; zIndex < 5; zIndex++) {
            for (int index2 = 0; index2 < 5; index2++) {
                zArray[index2] = this.fval[i2 + index2][j2 + zIndex];
            }
            PolynomialSplineFunction spline = interpolator.interpolate(xArray, zArray);
            interpArray[zIndex] = spline.value(x2);
        }
        PolynomialSplineFunction spline2 = interpolator.interpolate(yArray, interpArray);
        double returnValue = spline2.value(y2);
        return returnValue;
    }

    public boolean isValidPoint(double x2, double y2) {
        if (x2 < this.xval[0] || x2 > this.xval[this.xval.length - 1] || y2 < this.yval[0] || y2 > this.yval[this.yval.length - 1]) {
            return false;
        }
        return true;
    }

    private int searchIndex(double c2, double[] val, int offset, int count) {
        int r2;
        int r3 = Arrays.binarySearch(val, c2);
        if (r3 == -1 || r3 == (-val.length) - 1) {
            throw new OutOfRangeException(Double.valueOf(c2), Double.valueOf(val[0]), Double.valueOf(val[val.length - 1]));
        }
        if (r3 < 0) {
            r2 = ((-r3) - offset) - 1;
        } else {
            r2 = r3 - offset;
        }
        if (r2 < 0) {
            r2 = 0;
        }
        if (r2 + count >= val.length) {
            r2 = val.length - count;
        }
        return r2;
    }
}
