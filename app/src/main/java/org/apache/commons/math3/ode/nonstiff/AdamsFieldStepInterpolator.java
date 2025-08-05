package org.apache.commons.math3.ode.nonstiff;

import java.util.Arrays;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.ode.sampling.AbstractFieldStepInterpolator;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/AdamsFieldStepInterpolator.class */
class AdamsFieldStepInterpolator<T extends RealFieldElement<T>> extends AbstractFieldStepInterpolator<T> {
    private T scalingH;
    private final FieldODEStateAndDerivative<T> reference;
    private final T[] scaled;
    private final Array2DRowFieldMatrix<T> nordsieck;

    AdamsFieldStepInterpolator(T stepSize, FieldODEStateAndDerivative<T> reference, T[] scaled, Array2DRowFieldMatrix<T> nordsieck, boolean isForward, FieldODEStateAndDerivative<T> globalPreviousState, FieldODEStateAndDerivative<T> globalCurrentState, FieldEquationsMapper<T> equationsMapper) {
        this(stepSize, reference, scaled, nordsieck, isForward, globalPreviousState, globalCurrentState, globalPreviousState, globalCurrentState, equationsMapper);
    }

    private AdamsFieldStepInterpolator(T t2, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative, T[] tArr, Array2DRowFieldMatrix<T> array2DRowFieldMatrix, boolean z2, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative2, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative3, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative4, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative5, FieldEquationsMapper<T> fieldEquationsMapper) {
        super(z2, fieldODEStateAndDerivative2, fieldODEStateAndDerivative3, fieldODEStateAndDerivative4, fieldODEStateAndDerivative5, fieldEquationsMapper);
        this.scalingH = t2;
        this.reference = fieldODEStateAndDerivative;
        this.scaled = (T[]) ((RealFieldElement[]) tArr.clone());
        this.nordsieck = new Array2DRowFieldMatrix<>(array2DRowFieldMatrix.getData(), false);
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // org.apache.commons.math3.ode.sampling.AbstractFieldStepInterpolator
    public AdamsFieldStepInterpolator<T> create(boolean newForward, FieldODEStateAndDerivative<T> newGlobalPreviousState, FieldODEStateAndDerivative<T> newGlobalCurrentState, FieldODEStateAndDerivative<T> newSoftPreviousState, FieldODEStateAndDerivative<T> newSoftCurrentState, FieldEquationsMapper<T> newMapper) {
        return new AdamsFieldStepInterpolator<>(this.scalingH, this.reference, this.scaled, this.nordsieck, newForward, newGlobalPreviousState, newGlobalCurrentState, newSoftPreviousState, newSoftCurrentState, newMapper);
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractFieldStepInterpolator
    protected FieldODEStateAndDerivative<T> computeInterpolatedStateAndDerivatives(FieldEquationsMapper<T> equationsMapper, T time, T theta, T thetaH, T oneMinusThetaH) {
        return taylor(this.reference, time, this.scalingH, this.scaled, this.nordsieck);
    }

    public static <S extends RealFieldElement<S>> FieldODEStateAndDerivative<S> taylor(FieldODEStateAndDerivative<S> fieldODEStateAndDerivative, S s2, S s3, S[] sArr, Array2DRowFieldMatrix<S> array2DRowFieldMatrix) {
        RealFieldElement realFieldElement = (RealFieldElement) s2.subtract(fieldODEStateAndDerivative.getTime());
        RealFieldElement realFieldElement2 = (RealFieldElement) realFieldElement.divide(s3);
        RealFieldElement[] realFieldElementArr = (RealFieldElement[]) MathArrays.buildArray(s2.getField2(), sArr.length);
        Arrays.fill(realFieldElementArr, s2.getField2().getZero());
        RealFieldElement[] realFieldElementArr2 = (RealFieldElement[]) MathArrays.buildArray(s2.getField2(), sArr.length);
        Arrays.fill(realFieldElementArr2, s2.getField2().getZero());
        RealFieldElement[][] realFieldElementArr3 = (RealFieldElement[][]) array2DRowFieldMatrix.getDataRef();
        for (int length = realFieldElementArr3.length - 1; length >= 0; length--) {
            int i2 = length + 2;
            RealFieldElement[] realFieldElementArr4 = realFieldElementArr3[length];
            RealFieldElement realFieldElement3 = (RealFieldElement) realFieldElement2.pow(i2);
            for (int i3 = 0; i3 < realFieldElementArr4.length; i3++) {
                RealFieldElement realFieldElement4 = (RealFieldElement) realFieldElementArr4[i3].multiply(realFieldElement3);
                realFieldElementArr[i3] = (RealFieldElement) realFieldElementArr[i3].add(realFieldElement4);
                realFieldElementArr2[i3] = (RealFieldElement) realFieldElementArr2[i3].add((RealFieldElement) realFieldElement4.multiply(i2));
            }
        }
        S[] state = fieldODEStateAndDerivative.getState();
        for (int i4 = 0; i4 < realFieldElementArr.length; i4++) {
            realFieldElementArr[i4] = (RealFieldElement) realFieldElementArr[i4].add((RealFieldElement) sArr[i4].multiply(realFieldElement2));
            state[i4] = (RealFieldElement) state[i4].add(realFieldElementArr[i4]);
            realFieldElementArr2[i4] = (RealFieldElement) ((RealFieldElement) realFieldElementArr2[i4].add((RealFieldElement) sArr[i4].multiply(realFieldElement2))).divide(realFieldElement);
        }
        return new FieldODEStateAndDerivative<>(s2, state, realFieldElementArr2);
    }
}
