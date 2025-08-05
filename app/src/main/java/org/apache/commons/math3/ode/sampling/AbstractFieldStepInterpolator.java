package org.apache.commons.math3.ode.sampling;

import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/sampling/AbstractFieldStepInterpolator.class */
public abstract class AbstractFieldStepInterpolator<T extends RealFieldElement<T>> implements FieldStepInterpolator<T> {
    private final FieldODEStateAndDerivative<T> globalPreviousState;
    private final FieldODEStateAndDerivative<T> globalCurrentState;
    private final FieldODEStateAndDerivative<T> softPreviousState;
    private final FieldODEStateAndDerivative<T> softCurrentState;
    private final boolean forward;
    private FieldEquationsMapper<T> mapper;

    protected abstract AbstractFieldStepInterpolator<T> create(boolean z2, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative2, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative3, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative4, FieldEquationsMapper<T> fieldEquationsMapper);

    protected abstract FieldODEStateAndDerivative<T> computeInterpolatedStateAndDerivatives(FieldEquationsMapper<T> fieldEquationsMapper, T t2, T t3, T t4, T t5) throws MaxCountExceededException;

    protected AbstractFieldStepInterpolator(boolean isForward, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldODEStateAndDerivative<T> softPreviousState, FieldODEStateAndDerivative<T> softCurrentState, FieldEquationsMapper<T> equationsMapper) {
        this.forward = isForward;
        this.globalPreviousState = globalPreviousState;
        this.globalCurrentState = globalCurrentState;
        this.softPreviousState = softPreviousState;
        this.softCurrentState = softCurrentState;
        this.mapper = equationsMapper;
    }

    public AbstractFieldStepInterpolator<T> restrictStep(FieldODEStateAndDerivative<T> previousState, FieldODEStateAndDerivative<T> currentState) {
        return create(this.forward, this.globalPreviousState, this.globalCurrentState, previousState, currentState, this.mapper);
    }

    public FieldODEStateAndDerivative<T> getGlobalPreviousState() {
        return this.globalPreviousState;
    }

    public FieldODEStateAndDerivative<T> getGlobalCurrentState() {
        return this.globalCurrentState;
    }

    @Override // org.apache.commons.math3.ode.sampling.FieldStepInterpolator
    public FieldODEStateAndDerivative<T> getPreviousState() {
        return this.softPreviousState;
    }

    @Override // org.apache.commons.math3.ode.sampling.FieldStepInterpolator
    public FieldODEStateAndDerivative<T> getCurrentState() {
        return this.softCurrentState;
    }

    @Override // org.apache.commons.math3.ode.sampling.FieldStepInterpolator
    public FieldODEStateAndDerivative<T> getInterpolatedState(T time) {
        RealFieldElement realFieldElement = (RealFieldElement) time.subtract(this.globalPreviousState.getTime());
        RealFieldElement realFieldElement2 = (RealFieldElement) this.globalCurrentState.getTime().subtract(time);
        return computeInterpolatedStateAndDerivatives(this.mapper, time, (RealFieldElement) realFieldElement.divide((RealFieldElement) this.globalCurrentState.getTime().subtract(this.globalPreviousState.getTime())), realFieldElement, realFieldElement2);
    }

    @Override // org.apache.commons.math3.ode.sampling.FieldStepInterpolator
    public boolean isForward() {
        return this.forward;
    }
}
