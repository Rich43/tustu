package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.TrivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/TrivariateGridInterpolator.class */
public interface TrivariateGridInterpolator {
    TrivariateFunction interpolate(double[] dArr, double[] dArr2, double[] dArr3, double[][][] dArr4) throws NumberIsTooSmallException, NoDataException, DimensionMismatchException, NonMonotonicSequenceException;
}
