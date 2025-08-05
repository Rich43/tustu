package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/ThreeEighthesFieldStepInterpolator.class */
class ThreeEighthesFieldStepInterpolator<T extends RealFieldElement<T>> extends RungeKuttaFieldStepInterpolator<T> {
    ThreeEighthesFieldStepInterpolator(Field<T> field, boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldODEStateAndDerivative<T> softPreviousState, FieldODEStateAndDerivative<T> softCurrentState, FieldEquationsMapper<T> mapper) {
        super(field, forward, yDotK, globalPreviousState, globalCurrentState, softPreviousState, softCurrentState, mapper);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.ode.nonstiff.RungeKuttaFieldStepInterpolator
    public ThreeEighthesFieldStepInterpolator<T> create(Field<T> newField, boolean newForward, T[][] newYDotK, FieldODEStateAndDerivative<T> newGlobalPreviousState, FieldODEStateAndDerivative<T> newGlobalCurrentState, FieldODEStateAndDerivative<T> newSoftPreviousState, FieldODEStateAndDerivative<T> newSoftCurrentState, FieldEquationsMapper<T> newMapper) {
        return new ThreeEighthesFieldStepInterpolator<>(newField, newForward, newYDotK, newGlobalPreviousState, newGlobalCurrentState, newSoftPreviousState, newSoftCurrentState, newMapper);
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractFieldStepInterpolator
    protected FieldODEStateAndDerivative<T> computeInterpolatedStateAndDerivatives(FieldEquationsMapper<T> mapper, T time, T theta, T thetaH, T oneMinusThetaH) {
        T[] interpolatedState;
        T[] interpolatedDerivatives;
        RealFieldElement realFieldElement = (RealFieldElement) theta.multiply(0.75d);
        RealFieldElement realFieldElement2 = (RealFieldElement) ((RealFieldElement) realFieldElement.multiply((RealFieldElement) ((RealFieldElement) theta.multiply(4)).subtract(5.0d))).add(1.0d);
        RealFieldElement realFieldElement3 = (RealFieldElement) realFieldElement.multiply((RealFieldElement) ((RealFieldElement) theta.multiply(-6)).add(5.0d));
        RealFieldElement realFieldElement4 = (RealFieldElement) realFieldElement.multiply((RealFieldElement) ((RealFieldElement) theta.multiply(2)).subtract(1.0d));
        if (getGlobalPreviousState() != null && theta.getReal() <= 0.5d) {
            RealFieldElement realFieldElement5 = (RealFieldElement) thetaH.divide(8.0d);
            RealFieldElement realFieldElement6 = (RealFieldElement) ((RealFieldElement) theta.multiply(theta)).multiply(4);
            interpolatedState = previousStateLinearCombination((RealFieldElement) realFieldElement5.multiply((RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement6.multiply(2)).subtract((RealFieldElement) theta.multiply(15))).add(8.0d)), (RealFieldElement) ((RealFieldElement) realFieldElement5.multiply((RealFieldElement) ((RealFieldElement) theta.multiply(5)).subtract(realFieldElement6))).multiply(3), (RealFieldElement) ((RealFieldElement) realFieldElement5.multiply(theta)).multiply(3), (RealFieldElement) realFieldElement5.multiply((RealFieldElement) realFieldElement6.subtract((RealFieldElement) theta.multiply(3))));
            interpolatedDerivatives = derivativeLinearCombination(realFieldElement2, realFieldElement3, realFieldElement, realFieldElement4);
        } else {
            RealFieldElement realFieldElement7 = (RealFieldElement) oneMinusThetaH.divide(-8.0d);
            RealFieldElement realFieldElement8 = (RealFieldElement) ((RealFieldElement) theta.multiply(theta)).multiply(4);
            RealFieldElement realFieldElement9 = (RealFieldElement) theta.add(1.0d);
            interpolatedState = currentStateLinearCombination((RealFieldElement) realFieldElement7.multiply((RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement8.multiply(2)).subtract((RealFieldElement) theta.multiply(7))).add(1.0d)), (RealFieldElement) ((RealFieldElement) realFieldElement7.multiply((RealFieldElement) realFieldElement9.subtract(realFieldElement8))).multiply(3), (RealFieldElement) ((RealFieldElement) realFieldElement7.multiply(realFieldElement9)).multiply(3), (RealFieldElement) realFieldElement7.multiply((RealFieldElement) realFieldElement9.add(realFieldElement8)));
            interpolatedDerivatives = derivativeLinearCombination(realFieldElement2, realFieldElement3, realFieldElement, realFieldElement4);
        }
        return new FieldODEStateAndDerivative<>(time, interpolatedState, interpolatedDerivatives);
    }
}
