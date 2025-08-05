package org.apache.commons.math3.ode;

import org.apache.commons.math3.RealFieldElement;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/FieldODEStateAndDerivative.class */
public class FieldODEStateAndDerivative<T extends RealFieldElement<T>> extends FieldODEState<T> {
    private final T[] derivative;
    private final T[][] secondaryDerivative;

    public FieldODEStateAndDerivative(T time, T[] state, T[] derivative) {
        this(time, state, derivative, (RealFieldElement[][]) null, (RealFieldElement[][]) null);
    }

    public FieldODEStateAndDerivative(T t2, T[] tArr, T[] tArr2, T[][] tArr3, T[][] tArr4) {
        super(t2, tArr, tArr3);
        this.derivative = (T[]) ((RealFieldElement[]) tArr2.clone());
        this.secondaryDerivative = copy(t2.getField(), tArr4);
    }

    public T[] getDerivative() {
        return (T[]) ((RealFieldElement[]) this.derivative.clone());
    }

    public T[] getSecondaryDerivative(int i2) {
        return i2 == 0 ? (T[]) ((RealFieldElement[]) this.derivative.clone()) : (T[]) ((RealFieldElement[]) this.secondaryDerivative[i2 - 1].clone());
    }
}
