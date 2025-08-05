package org.apache.commons.math3.analysis.function;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/function/Abs.class */
public class Abs implements UnivariateFunction {
    @Override // org.apache.commons.math3.analysis.UnivariateFunction
    public double value(double x2) {
        return FastMath.abs(x2);
    }
}
