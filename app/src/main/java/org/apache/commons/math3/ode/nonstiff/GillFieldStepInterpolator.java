package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/GillFieldStepInterpolator.class */
class GillFieldStepInterpolator<T extends RealFieldElement<T>> extends RungeKuttaFieldStepInterpolator<T> {
    private final T one_minus_inv_sqrt_2;
    private final T one_plus_inv_sqrt_2;

    GillFieldStepInterpolator(Field<T> field, boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldODEStateAndDerivative<T> softPreviousState, FieldODEStateAndDerivative<T> softCurrentState, FieldEquationsMapper<T> mapper) {
        super(field, forward, yDotK, globalPreviousState, globalCurrentState, softPreviousState, softCurrentState, mapper);
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) field.getZero().add(0.5d)).sqrt();
        this.one_minus_inv_sqrt_2 = (T) field.getOne().subtract(realFieldElement);
        this.one_plus_inv_sqrt_2 = (T) field.getOne().add(realFieldElement);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.ode.nonstiff.RungeKuttaFieldStepInterpolator
    public GillFieldStepInterpolator<T> create(Field<T> newField, boolean newForward, T[][] newYDotK, FieldODEStateAndDerivative<T> newGlobalPreviousState, FieldODEStateAndDerivative<T> newGlobalCurrentState, FieldODEStateAndDerivative<T> newSoftPreviousState, FieldODEStateAndDerivative<T> newSoftCurrentState, FieldEquationsMapper<T> newMapper) {
        return new GillFieldStepInterpolator<>(newField, newForward, newYDotK, newGlobalPreviousState, newGlobalCurrentState, newSoftPreviousState, newSoftCurrentState, newMapper);
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractFieldStepInterpolator
    protected FieldODEStateAndDerivative<T> computeInterpolatedStateAndDerivatives(FieldEquationsMapper<T> mapper, T time, T theta, T thetaH, T oneMinusThetaH) {
        T[] interpolatedState;
        T[] interpolatedDerivatives;
        RealFieldElement realFieldElement = (RealFieldElement) time.getField().getOne();
        RealFieldElement realFieldElement2 = (RealFieldElement) theta.multiply(2);
        RealFieldElement realFieldElement3 = (RealFieldElement) realFieldElement2.multiply(realFieldElement2);
        RealFieldElement realFieldElement4 = (RealFieldElement) ((RealFieldElement) theta.multiply(realFieldElement2.subtract(3.0d))).add(1.0d);
        RealFieldElement realFieldElement5 = (RealFieldElement) realFieldElement2.multiply((RealFieldElement) realFieldElement.subtract(theta));
        RealFieldElement realFieldElement6 = (RealFieldElement) realFieldElement5.multiply(this.one_minus_inv_sqrt_2);
        RealFieldElement realFieldElement7 = (RealFieldElement) realFieldElement5.multiply(this.one_plus_inv_sqrt_2);
        RealFieldElement realFieldElement8 = (RealFieldElement) theta.multiply(realFieldElement2.subtract(1.0d));
        if (getGlobalPreviousState() != null && theta.getReal() <= 0.5d) {
            RealFieldElement realFieldElement9 = (RealFieldElement) thetaH.divide(6.0d);
            RealFieldElement realFieldElement10 = (RealFieldElement) realFieldElement9.multiply((RealFieldElement) ((RealFieldElement) theta.multiply(6)).subtract(realFieldElement3));
            interpolatedState = previousStateLinearCombination((RealFieldElement) realFieldElement9.multiply((RealFieldElement) ((RealFieldElement) realFieldElement3.subtract((RealFieldElement) theta.multiply(9))).add(6.0d)), (RealFieldElement) realFieldElement10.multiply(this.one_minus_inv_sqrt_2), (RealFieldElement) realFieldElement10.multiply(this.one_plus_inv_sqrt_2), (RealFieldElement) realFieldElement9.multiply((RealFieldElement) realFieldElement3.subtract((RealFieldElement) theta.multiply(3))));
            interpolatedDerivatives = derivativeLinearCombination(realFieldElement4, realFieldElement6, realFieldElement7, realFieldElement8);
        } else {
            RealFieldElement realFieldElement11 = (RealFieldElement) oneMinusThetaH.divide(-6.0d);
            RealFieldElement realFieldElement12 = (RealFieldElement) realFieldElement11.multiply((RealFieldElement) ((RealFieldElement) realFieldElement2.add(2.0d)).subtract(realFieldElement3));
            interpolatedState = currentStateLinearCombination((RealFieldElement) realFieldElement11.multiply((RealFieldElement) ((RealFieldElement) realFieldElement3.subtract((RealFieldElement) theta.multiply(5))).add(1.0d)), (RealFieldElement) realFieldElement12.multiply(this.one_minus_inv_sqrt_2), (RealFieldElement) realFieldElement12.multiply(this.one_plus_inv_sqrt_2), (RealFieldElement) realFieldElement11.multiply((RealFieldElement) ((RealFieldElement) realFieldElement3.add(theta)).add(1.0d)));
            interpolatedDerivatives = derivativeLinearCombination(realFieldElement4, realFieldElement6, realFieldElement7, realFieldElement8);
        }
        return new FieldODEStateAndDerivative<>(time, interpolatedState, interpolatedDerivatives);
    }
}
