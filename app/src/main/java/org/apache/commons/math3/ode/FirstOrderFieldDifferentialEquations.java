package org.apache.commons.math3.ode;

import org.apache.commons.math3.RealFieldElement;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/FirstOrderFieldDifferentialEquations.class */
public interface FirstOrderFieldDifferentialEquations<T extends RealFieldElement<T>> {
    int getDimension();

    void init(T t2, T[] tArr, T t3);

    T[] computeDerivatives(T t2, T[] tArr);
}
