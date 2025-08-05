package org.apache.commons.math3.analysis.function;

import org.apache.commons.math3.analysis.BivariateFunction;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/function/Add.class */
public class Add implements BivariateFunction {
    @Override // org.apache.commons.math3.analysis.BivariateFunction
    public double value(double x2, double y2) {
        return x2 + y2;
    }
}
