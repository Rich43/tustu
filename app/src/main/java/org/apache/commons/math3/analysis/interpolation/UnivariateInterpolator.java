package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/UnivariateInterpolator.class */
public interface UnivariateInterpolator {
    UnivariateFunction interpolate(double[] dArr, double[] dArr2) throws MathIllegalArgumentException;
}
