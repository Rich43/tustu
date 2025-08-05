package org.apache.commons.math3.analysis.integration;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/integration/UnivariateIntegrator.class */
public interface UnivariateIntegrator {
    double getRelativeAccuracy();

    double getAbsoluteAccuracy();

    int getMinimalIterationCount();

    int getMaximalIterationCount();

    double integrate(int i2, UnivariateFunction univariateFunction, double d2, double d3) throws MaxCountExceededException, MathIllegalArgumentException;

    int getEvaluations();

    int getIterations();
}
