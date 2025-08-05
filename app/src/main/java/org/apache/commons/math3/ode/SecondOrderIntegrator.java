package org.apache.commons.math3.ode;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/SecondOrderIntegrator.class */
public interface SecondOrderIntegrator extends ODEIntegrator {
    void integrate(SecondOrderDifferentialEquations secondOrderDifferentialEquations, double d2, double[] dArr, double[] dArr2, double d3, double[] dArr3, double[] dArr4) throws MathIllegalStateException, MathIllegalArgumentException;
}
