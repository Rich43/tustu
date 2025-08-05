package org.apache.commons.math3.stat.descriptive;

import org.apache.commons.math3.exception.MathIllegalArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/stat/descriptive/WeightedEvaluation.class */
public interface WeightedEvaluation {
    double evaluate(double[] dArr, double[] dArr2) throws MathIllegalArgumentException;

    double evaluate(double[] dArr, double[] dArr2, int i2, int i3) throws MathIllegalArgumentException;
}
