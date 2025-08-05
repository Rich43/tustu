package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/MultivariateInterpolator.class */
public interface MultivariateInterpolator {
    MultivariateFunction interpolate(double[][] dArr, double[] dArr2) throws MathIllegalArgumentException;
}
