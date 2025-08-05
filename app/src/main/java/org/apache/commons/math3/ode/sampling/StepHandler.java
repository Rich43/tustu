package org.apache.commons.math3.ode.sampling;

import org.apache.commons.math3.exception.MaxCountExceededException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/sampling/StepHandler.class */
public interface StepHandler {
    void init(double d2, double[] dArr, double d3);

    void handleStep(StepInterpolator stepInterpolator, boolean z2) throws MaxCountExceededException;
}
