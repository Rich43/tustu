package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/ThreeEighthesFieldIntegrator.class */
public class ThreeEighthesFieldIntegrator<T extends RealFieldElement<T>> extends RungeKuttaFieldIntegrator<T> {
    public ThreeEighthesFieldIntegrator(Field<T> field, T step) {
        super(field, "3/8", step);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.ode.nonstiff.FieldButcherArrayProvider
    public T[] getC() {
        T[] tArr = (T[]) ((RealFieldElement[]) MathArrays.buildArray(getField(), 3));
        tArr[0] = fraction(1, 3);
        tArr[1] = (RealFieldElement) tArr[0].add((DerivativeStructure) tArr[0]);
        tArr[2] = getField().getOne();
        return tArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.ode.nonstiff.FieldButcherArrayProvider
    public T[][] getA() {
        T[][] tArr = (T[][]) ((RealFieldElement[][]) MathArrays.buildArray(getField(), 3, -1));
        for (int i2 = 0; i2 < tArr.length; i2++) {
            tArr[i2] = (RealFieldElement[]) MathArrays.buildArray(getField(), i2 + 1);
        }
        tArr[0][0] = fraction(1, 3);
        tArr[1][0] = (RealFieldElement) tArr[0][0].negate();
        tArr[1][1] = getField().getOne();
        tArr[2][0] = getField().getOne();
        tArr[2][1] = (RealFieldElement) getField().getOne().negate();
        tArr[2][2] = getField().getOne();
        return tArr;
    }

    @Override // org.apache.commons.math3.ode.nonstiff.FieldButcherArrayProvider
    public T[] getB() {
        T[] tArr = (T[]) ((RealFieldElement[]) MathArrays.buildArray(getField(), 4));
        tArr[0] = fraction(1, 8);
        tArr[1] = fraction(3, 8);
        tArr[2] = tArr[1];
        tArr[3] = tArr[0];
        return tArr;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.ode.nonstiff.RungeKuttaFieldIntegrator
    public ThreeEighthesFieldStepInterpolator<T> createInterpolator(boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldEquationsMapper<T> mapper) {
        return new ThreeEighthesFieldStepInterpolator<>(getField(), forward, yDotK, globalPreviousState, globalCurrentState, globalPreviousState, globalCurrentState, mapper);
    }
}
