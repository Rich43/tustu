package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/PiecewiseBicubicSplineInterpolator.class */
public class PiecewiseBicubicSplineInterpolator implements BivariateGridInterpolator {
    @Override // org.apache.commons.math3.analysis.interpolation.BivariateGridInterpolator
    public PiecewiseBicubicSplineInterpolatingFunction interpolate(double[] xval, double[] yval, double[][] fval) throws NullArgumentException, NoDataException, NonMonotonicSequenceException, DimensionMismatchException {
        if (xval == null || yval == null || fval == null || fval[0] == null) {
            throw new NullArgumentException();
        }
        if (xval.length == 0 || yval.length == 0 || fval.length == 0) {
            throw new NoDataException();
        }
        MathArrays.checkOrder(xval);
        MathArrays.checkOrder(yval);
        return new PiecewiseBicubicSplineInterpolatingFunction(xval, yval, fval);
    }
}
