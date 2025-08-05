package org.apache.commons.math3.ode.sampling;

import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/sampling/FieldFixedStepHandler.class */
public interface FieldFixedStepHandler<T extends RealFieldElement<T>> {
    void init(FieldODEStateAndDerivative<T> fieldODEStateAndDerivative, T t2);

    void handleStep(FieldODEStateAndDerivative<T> fieldODEStateAndDerivative, boolean z2);
}
