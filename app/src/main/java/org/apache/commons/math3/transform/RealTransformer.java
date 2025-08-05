package org.apache.commons.math3.transform;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/transform/RealTransformer.class */
public interface RealTransformer {
    double[] transform(double[] dArr, TransformType transformType) throws MathIllegalArgumentException;

    double[] transform(UnivariateFunction univariateFunction, double d2, double d3, int i2, TransformType transformType) throws MathIllegalArgumentException;
}
