package org.apache.commons.math3.ode.sampling;

import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/sampling/FieldStepInterpolator.class */
public interface FieldStepInterpolator<T extends RealFieldElement<T>> {
    FieldODEStateAndDerivative<T> getPreviousState();

    FieldODEStateAndDerivative<T> getCurrentState();

    FieldODEStateAndDerivative<T> getInterpolatedState(T t2);

    boolean isForward();
}
