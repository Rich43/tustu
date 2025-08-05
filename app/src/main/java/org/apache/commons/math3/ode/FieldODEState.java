package org.apache.commons.math3.ode;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/FieldODEState.class */
public class FieldODEState<T extends RealFieldElement<T>> {
    private final T time;
    private final T[] state;
    private final T[][] secondaryState;

    public FieldODEState(T time, T[] state) {
        this(time, state, (RealFieldElement[][]) null);
    }

    public FieldODEState(T t2, T[] tArr, T[][] tArr2) {
        this.time = t2;
        this.state = (T[]) ((RealFieldElement[]) tArr.clone());
        this.secondaryState = (T[][]) copy(t2.getField(), tArr2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    protected T[][] copy(Field<T> field, T[][] tArr) {
        if (tArr == null) {
            return (T[][]) ((RealFieldElement[][]) null);
        }
        T[][] tArr2 = (T[][]) ((RealFieldElement[][]) MathArrays.buildArray(field, tArr.length, -1));
        for (int i2 = 0; i2 < tArr.length; i2++) {
            tArr2[i2] = (RealFieldElement[]) tArr[i2].clone();
        }
        return tArr2;
    }

    public T getTime() {
        return this.time;
    }

    public int getStateDimension() {
        return this.state.length;
    }

    public T[] getState() {
        return (T[]) ((RealFieldElement[]) this.state.clone());
    }

    public int getNumberOfSecondaryStates() {
        if (this.secondaryState == null) {
            return 0;
        }
        return this.secondaryState.length;
    }

    public int getSecondaryStateDimension(int index) {
        return index == 0 ? this.state.length : this.secondaryState[index - 1].length;
    }

    public T[] getSecondaryState(int i2) {
        return i2 == 0 ? (T[]) ((RealFieldElement[]) this.state.clone()) : (T[]) ((RealFieldElement[]) this.secondaryState[i2 - 1].clone());
    }
}
