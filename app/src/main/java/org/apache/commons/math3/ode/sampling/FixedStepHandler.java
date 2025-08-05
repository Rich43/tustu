package org.apache.commons.math3.ode.sampling;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/sampling/FixedStepHandler.class */
public interface FixedStepHandler {
    void init(double d2, double[] dArr, double d3);

    void handleStep(double d2, double[] dArr, double[] dArr2, boolean z2);
}
