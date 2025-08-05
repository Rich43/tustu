package org.apache.commons.math3.ode.sampling;

import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/sampling/FieldStepHandler.class */
public interface FieldStepHandler<T extends RealFieldElement<T>> {
    void init(FieldODEStateAndDerivative<T> fieldODEStateAndDerivative, T t2);

    void handleStep(FieldStepInterpolator<T> fieldStepInterpolator, boolean z2) throws MaxCountExceededException;
}
