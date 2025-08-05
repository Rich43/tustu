package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.BivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/BivariateGridInterpolator.class */
public interface BivariateGridInterpolator {
    BivariateFunction interpolate(double[] dArr, double[] dArr2, double[][] dArr3) throws NumberIsTooSmallException, NoDataException, DimensionMismatchException, NonMonotonicSequenceException;
}
