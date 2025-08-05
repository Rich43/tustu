package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/LutherFieldIntegrator.class */
public class LutherFieldIntegrator<T extends RealFieldElement<T>> extends RungeKuttaFieldIntegrator<T> {
    public LutherFieldIntegrator(Field<T> field, T step) {
        super(field, "Luther", step);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.ode.nonstiff.FieldButcherArrayProvider
    public T[] getC() {
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) getField().getZero().add(21.0d)).sqrt();
        T[] tArr = (T[]) ((RealFieldElement[]) MathArrays.buildArray(getField(), 6));
        tArr[0] = getField().getOne();
        tArr[1] = fraction(1, 2);
        tArr[2] = fraction(2, 3);
        tArr[3] = (RealFieldElement) ((RealFieldElement) realFieldElement.subtract(7.0d)).divide(-14.0d);
        tArr[4] = (RealFieldElement) ((RealFieldElement) realFieldElement.add(7.0d)).divide(14.0d);
        tArr[5] = getField().getOne();
        return tArr;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // org.apache.commons.math3.ode.nonstiff.FieldButcherArrayProvider
    public T[][] getA() {
        RealFieldElement realFieldElement = (RealFieldElement) ((RealFieldElement) getField().getZero().add(21.0d)).sqrt();
        T[][] tArr = (T[][]) ((RealFieldElement[][]) MathArrays.buildArray(getField(), 6, -1));
        for (int i2 = 0; i2 < tArr.length; i2++) {
            tArr[i2] = (RealFieldElement[]) MathArrays.buildArray(getField(), i2 + 1);
        }
        tArr[0][0] = getField().getOne();
        tArr[1][0] = fraction(3, 8);
        tArr[1][1] = fraction(1, 8);
        tArr[2][0] = fraction(8, 27);
        tArr[2][1] = fraction(2, 27);
        tArr[2][2] = tArr[2][0];
        tArr[3][0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(9)).add(-21.0d)).divide(392.0d);
        tArr[3][1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(8)).add(-56.0d)).divide(392.0d);
        tArr[3][2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(-48)).add(336.0d)).divide(392.0d);
        tArr[3][3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(3)).add(-63.0d)).divide(392.0d);
        tArr[4][0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(-255)).add(-1155.0d)).divide(1960.0d);
        tArr[4][1] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(-40)).add(-280.0d)).divide(1960.0d);
        tArr[4][2] = (RealFieldElement) ((RealFieldElement) realFieldElement.multiply(-320)).divide(1960.0d);
        tArr[4][3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(363)).add(63.0d)).divide(1960.0d);
        tArr[4][4] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(392)).add(2352.0d)).divide(1960.0d);
        tArr[5][0] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(105)).add(330.0d)).divide(180.0d);
        tArr[5][1] = fraction(2, 3);
        tArr[5][2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(280)).add(-200.0d)).divide(180.0d);
        tArr[5][3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(-189)).add(126.0d)).divide(180.0d);
        tArr[5][4] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(-126)).add(-686.0d)).divide(180.0d);
        tArr[5][5] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElement.multiply(-70)).add(490.0d)).divide(180.0d);
        return tArr;
    }

    @Override // org.apache.commons.math3.ode.nonstiff.FieldButcherArrayProvider
    public T[] getB() {
        T[] tArr = (T[]) ((RealFieldElement[]) MathArrays.buildArray(getField(), 7));
        tArr[0] = fraction(1, 20);
        tArr[1] = getField().getZero();
        tArr[2] = fraction(16, 45);
        tArr[3] = getField().getZero();
        tArr[4] = fraction(49, 180);
        tArr[5] = tArr[4];
        tArr[6] = tArr[0];
        return tArr;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.ode.nonstiff.RungeKuttaFieldIntegrator
    public LutherFieldStepInterpolator<T> createInterpolator(boolean forward, T[][] yDotK, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldEquationsMapper<T> mapper) {
        return new LutherFieldStepInterpolator<>(getField(), forward, yDotK, globalPreviousState, globalCurrentState, globalPreviousState, globalCurrentState, mapper);
    }
}
