package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.FieldElement;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.FieldMatrix;
import org.apache.commons.math3.ode.FieldExpandableODE;
import org.apache.commons.math3.ode.FieldODEState;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/AdamsBashforthFieldIntegrator.class */
public class AdamsBashforthFieldIntegrator<T extends RealFieldElement<T>> extends AdamsFieldIntegrator<T> {
    private static final String METHOD_NAME = "Adams-Bashforth";

    public AdamsBashforthFieldIntegrator(Field<T> field, int nSteps, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) throws NumberIsTooSmallException {
        super(field, METHOD_NAME, nSteps, nSteps, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    public AdamsBashforthFieldIntegrator(Field<T> field, int nSteps, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) throws IllegalArgumentException {
        super(field, METHOD_NAME, nSteps, nSteps, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v49, types: [org.apache.commons.math3.RealFieldElement] */
    private T errorEstimation(T[] previousState, T[] predictedState, T[] predictedScaled, FieldMatrix<T> predictedNordsieck) {
        T error = getField().getZero();
        for (int i2 = 0; i2 < this.mainSetDimension; i2++) {
            RealFieldElement realFieldElement = (RealFieldElement) predictedState[i2].abs();
            RealFieldElement realFieldElement2 = this.vecAbsoluteTolerance == null ? (RealFieldElement) ((RealFieldElement) realFieldElement.multiply(this.scalRelativeTolerance)).add(this.scalAbsoluteTolerance) : (RealFieldElement) ((RealFieldElement) realFieldElement.multiply(this.vecRelativeTolerance[i2])).add(this.vecAbsoluteTolerance[i2]);
            FieldElement variation = (RealFieldElement) getField().getZero();
            int sign = predictedNordsieck.getRowDimension() % 2 == 0 ? -1 : 1;
            for (int k2 = predictedNordsieck.getRowDimension() - 1; k2 >= 0; k2--) {
                variation = (RealFieldElement) variation.add(((RealFieldElement) predictedNordsieck.getEntry(k2, i2)).multiply(sign));
                sign = -sign;
            }
            RealFieldElement realFieldElement3 = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) predictedState[i2].subtract(previousState[i2])).add((RealFieldElement) variation.subtract(predictedScaled[i2]))).divide(realFieldElement2);
            error = (RealFieldElement) error.add(realFieldElement3.multiply(realFieldElement3));
        }
        return (T) ((RealFieldElement) error.divide(this.mainSetDimension)).sqrt();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v24, types: [T extends org.apache.commons.math3.RealFieldElement<T>[], org.apache.commons.math3.RealFieldElement[]] */
    /* JADX WARN: Type inference failed for: r0v95, types: [org.apache.commons.math3.RealFieldElement] */
    @Override // org.apache.commons.math3.ode.nonstiff.AdamsFieldIntegrator, org.apache.commons.math3.ode.FirstOrderFieldIntegrator
    public FieldODEStateAndDerivative<T> integrate(FieldExpandableODE<T> fieldExpandableODE, FieldODEState<T> fieldODEState, T t2) throws MaxCountExceededException, MathIllegalArgumentException {
        sanityChecks(fieldODEState, t2);
        RealFieldElement time = fieldODEState.getTime();
        RealFieldElement[] realFieldElementArrMapState = fieldExpandableODE.getMapper().mapState(fieldODEState);
        setStepStart(initIntegration(fieldExpandableODE, time, realFieldElementArrMapState, t2));
        boolean z2 = ((RealFieldElement) t2.subtract(fieldODEState.getTime())).getReal() > 0.0d;
        start(fieldExpandableODE, getStepStart(), t2);
        FieldODEStateAndDerivative<T> stepStart = getStepStart();
        FieldODEStateAndDerivative fieldODEStateAndDerivativeTaylor = AdamsFieldStepInterpolator.taylor(stepStart, (RealFieldElement) stepStart.getTime().add(getStepSize()), getStepSize(), this.scaled, this.nordsieck);
        setIsLastStep(false);
        do {
            Object state = null;
            ?? r0 = (T[]) ((RealFieldElement[]) MathArrays.buildArray(getField(), realFieldElementArrMapState.length));
            Array2DRowFieldMatrix<T> array2DRowFieldMatrixUpdateHighOrderDerivativesPhase1 = null;
            RealFieldElement realFieldElementErrorEstimation = (RealFieldElement) getField().getZero().add(10.0d);
            while (((RealFieldElement) realFieldElementErrorEstimation.subtract(1.0d)).getReal() >= 0.0d) {
                state = fieldODEStateAndDerivativeTaylor.getState();
                T[] tArrComputeDerivatives = computeDerivatives((T) fieldODEStateAndDerivativeTaylor.getTime(), state);
                for (int i2 = 0; i2 < r0.length; i2++) {
                    r0[i2] = (RealFieldElement) getStepSize().multiply(tArrComputeDerivatives[i2]);
                }
                array2DRowFieldMatrixUpdateHighOrderDerivativesPhase1 = updateHighOrderDerivativesPhase1(this.nordsieck);
                updateHighOrderDerivativesPhase2(this.scaled, r0, array2DRowFieldMatrixUpdateHighOrderDerivativesPhase1);
                realFieldElementErrorEstimation = errorEstimation(realFieldElementArrMapState, state, r0, array2DRowFieldMatrixUpdateHighOrderDerivativesPhase1);
                if (((RealFieldElement) realFieldElementErrorEstimation.subtract(1.0d)).getReal() >= 0.0d) {
                    rescale(filterStep((RealFieldElement) getStepSize().multiply(computeStepGrowShrinkFactor(realFieldElementErrorEstimation)), z2, false));
                    fieldODEStateAndDerivativeTaylor = AdamsFieldStepInterpolator.taylor(getStepStart(), (RealFieldElement) getStepStart().getTime().add(getStepSize()), getStepSize(), this.scaled, this.nordsieck);
                }
            }
            setStepStart(acceptStep(new AdamsFieldStepInterpolator(getStepSize(), fieldODEStateAndDerivativeTaylor, r0, array2DRowFieldMatrixUpdateHighOrderDerivativesPhase1, z2, getStepStart(), fieldODEStateAndDerivativeTaylor, fieldExpandableODE.getMapper()), t2));
            this.scaled = r0;
            this.nordsieck = array2DRowFieldMatrixUpdateHighOrderDerivativesPhase1;
            if (!isLastStep()) {
                System.arraycopy(state, 0, realFieldElementArrMapState, 0, realFieldElementArrMapState.length);
                if (resetOccurred()) {
                    start(fieldExpandableODE, getStepStart(), t2);
                }
                RealFieldElement realFieldElement = (RealFieldElement) getStepSize().multiply(computeStepGrowShrinkFactor(realFieldElementErrorEstimation));
                RealFieldElement realFieldElement2 = (RealFieldElement) getStepStart().getTime().add(realFieldElement);
                T tFilterStep = filterStep(realFieldElement, z2, z2 ? ((RealFieldElement) realFieldElement2.subtract(t2)).getReal() >= 0.0d : ((RealFieldElement) realFieldElement2.subtract(t2)).getReal() <= 0.0d);
                RealFieldElement realFieldElement3 = (RealFieldElement) getStepStart().getTime().add(tFilterStep);
                if (z2 ? ((RealFieldElement) realFieldElement3.subtract(t2)).getReal() >= 0.0d : ((RealFieldElement) realFieldElement3.subtract(t2)).getReal() <= 0.0d) {
                    tFilterStep = (RealFieldElement) t2.subtract(getStepStart().getTime());
                }
                rescale(tFilterStep);
                fieldODEStateAndDerivativeTaylor = AdamsFieldStepInterpolator.taylor(getStepStart(), (RealFieldElement) getStepStart().getTime().add(getStepSize()), getStepSize(), this.scaled, this.nordsieck);
            }
        } while (!isLastStep());
        FieldODEStateAndDerivative<T> stepStart2 = getStepStart();
        setStepStart(null);
        setStepSize(null);
        return stepStart2;
    }
}
