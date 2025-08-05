package org.apache.commons.math3.analysis.function;

import org.apache.commons.math3.analysis.BivariateFunction;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/function/Min.class */
public class Min implements BivariateFunction {
    @Override // org.apache.commons.math3.analysis.BivariateFunction
    public double value(double x2, double y2) {
        return FastMath.min(x2, y2);
    }
}
