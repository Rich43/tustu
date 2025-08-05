package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/GillFieldIntegrator.class */
public class GillFieldIntegrator<T extends RealFieldElement<T>> extends RungeKuttaFieldIntegrator<T> {
    public GillFieldIntegrator(Field<T> field, T step) {
        super(field, "Gill", step);
    }

    @Override // org.apache.commons.math3.ode.nonstiff.FieldButcherArrayProvider
    public T[] getC() {
        T[] tArr = (T[]) ((RealFieldElement[]) MathArrays.buildArray(getField(), 3));
        tArr[0] = fraction(1, 2);
        tArr[1] = tArr[0];
        tArr[2] = getField().getOne();
        return tArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.ode.nonstiff.FieldButcherArrayProvider
    public T[][] getA() {
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) getField().getZero().add(2.0d)).sqrt();
        T[][] tArr = (T[][]) ((RealFieldElement[][]) MathArrays.buildArray(getField(), 3, -1));
        for (int i2 = 0; i2 < tArr.length; i2++) {
            tArr[i2] = (RealFieldElement[]) MathArrays.buildArray(getField(), i2 + 1);
        }
        tArr[0][0] = fraction(1, 2);
        tArr[1][0] = (RealFieldElement) ((RealFieldElement) realFieldElement.subtract(1.0d)).multiply(0.5d);
        tArr[1][1] = (RealFieldElement) ((RealFieldElement) realFieldElement.subtract(2.0d)).multiply(-0.5d);
        tArr[2][0] = getField().getZero();
        tArr[2][1] = (RealFieldElement) realFieldElement.multiply(-0.5d);
        tArr[2][2] = (RealFieldElement) ((RealFieldElement) realFieldElement.add(2.0d)).multiply(0.5d);
        return tArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.ode.nonstiff.FieldButcherArrayProvider
    public T[] getB() {
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) getField().getZero().add(2.0d)).sqrt();
        T[] tArr = (T[]) ((RealFieldElement[]) MathArrays.buildArray(getField(), 4));
        tArr[0] = fraction(1, 6);
        tArr[1] = (RealFieldElement) ((RealFieldElement) realFieldElement.subtract(2.0d)).divide(-6.0d);
        tArr[2] = (RealFieldElement) ((RealFieldElement) realFieldElement.add(2.0d)).divide(6.0d);
        tArr[3] = tArr[0];
        return tArr;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.ode.nonstiff.RungeKuttaFieldIntegrator
    public GillFieldStepInterpolator<T> createInterpolator(boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldEquationsMapper<T> mapper) {
        return new GillFieldStepInterpolator<>(getField(), forward, yDotK, globalPreviousState, globalCurrentState, globalPreviousState, globalCurrentState, mapper);
    }
}
