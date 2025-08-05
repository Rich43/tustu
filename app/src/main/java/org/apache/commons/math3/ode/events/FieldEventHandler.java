package org.apache.commons.math3.ode.events;

import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldODEState;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/events/FieldEventHandler.class */
public interface FieldEventHandler<T extends RealFieldElement<T>> {
    void init(FieldODEStateAndDerivative<T> fieldODEStateAndDerivative, T t2);

    T g(FieldODEStateAndDerivative<T> fieldODEStateAndDerivative);

    Action eventOccurred(FieldODEStateAndDerivative<T> fieldODEStateAndDerivative, boolean z2);

    FieldODEState<T> resetState(FieldODEStateAndDerivative<T> fieldODEStateAndDerivative);
}
