package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.ode.sampling.AbstractFieldStepInterpolator;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/RungeKuttaFieldStepInterpolator.class */
abstract class RungeKuttaFieldStepInterpolator<T extends RealFieldElement<T>> extends AbstractFieldStepInterpolator<T> {
    private final Field<T> field;
    private final T[][] yDotK;

    protected abstract RungeKuttaFieldStepInterpolator<T> create(Field<T> field, boolean z2, T[][] tArr, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative2, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative3, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative4, FieldEquationsMapper<T> fieldEquationsMapper);

    protected RungeKuttaFieldStepInterpolator(Field<T> field, boolean z2, T[][] tArr, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative2, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative3, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative4, FieldEquationsMapper<T> fieldEquationsMapper) {
        super(z2, fieldODEStateAndDerivative, fieldODEStateAndDerivative2, fieldODEStateAndDerivative3, fieldODEStateAndDerivative4, fieldEquationsMapper);
        this.field = field;
        this.yDotK = (T[][]) ((RealFieldElement[][]) MathArrays.buildArray(field, tArr.length, -1));
        for (int i2 = 0; i2 < tArr.length; i2++) {
            ((T[][]) this.yDotK)[i2] = (RealFieldElement[]) tArr[i2].clone();
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.ode.sampling.AbstractFieldStepInterpolator
    public RungeKuttaFieldStepInterpolator<T> create(boolean newForward, FieldODEStateAndDerivative<T> newGlobalPreviousState, FieldODEStateAndDerivative<T> newGlobalCurrentState, FieldODEStateAndDerivative<T> newSoftPreviousState, FieldODEStateAndDerivative<T> newSoftCurrentState, FieldEquationsMapper<T> newMapper) {
        return create(this.field, newForward, this.yDotK, newGlobalPreviousState, newGlobalCurrentState, newSoftPreviousState, newSoftCurrentState, newMapper);
    }

    protected final T[] previousStateLinearCombination(T... tArr) {
        return (T[]) combine(getPreviousState().getState(), tArr);
    }

    protected T[] currentStateLinearCombination(T... tArr) {
        return (T[]) combine(getCurrentState().getState(), tArr);
    }

    protected T[] derivativeLinearCombination(T... tArr) {
        return (T[]) combine((RealFieldElement[]) MathArrays.buildArray(this.field, this.yDotK[0].length), tArr);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private T[] combine(T[] tArr, T... coefficients) {
        for (int i2 = 0; i2 < tArr.length; i2++) {
            for (int k2 = 0; k2 < coefficients.length; k2++) {
                tArr[i2] = (RealFieldElement) tArr[i2].add((DerivativeStructure) coefficients[k2].multiply(this.yDotK[k2][i2]));
            }
        }
        return tArr;
    }
}
