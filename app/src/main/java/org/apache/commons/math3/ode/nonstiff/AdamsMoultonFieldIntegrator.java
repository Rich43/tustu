package org.apache.commons.math3.ode.nonstiff;

import java.util.Arrays;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.linear.FieldMatrixPreservingVisitor;
import org.apache.commons.math3.ode.FieldExpandableODE;
import org.apache.commons.math3.ode.FieldODEState;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/AdamsMoultonFieldIntegrator.class */
public class AdamsMoultonFieldIntegrator<T extends RealFieldElement<T>> extends AdamsFieldIntegrator<T> {
    private static final String METHOD_NAME = "Adams-Moulton";

    public AdamsMoultonFieldIntegrator(Field<T> field, int nSteps, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) throws NumberIsTooSmallException {
        super(field, METHOD_NAME, nSteps, nSteps + 1, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    public AdamsMoultonFieldIntegrator(Field<T> field, int nSteps, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) throws IllegalArgumentException {
        super(field, METHOD_NAME, nSteps, nSteps + 1, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v105, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r0v123, types: [org.apache.commons.math3.RealFieldElement[]] */
    /* JADX WARN: Type inference failed for: r0v42, types: [T extends org.apache.commons.math3.RealFieldElement<T>[], org.apache.commons.math3.RealFieldElement[]] */
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
            RealFieldElement[] state = null;
            RealFieldElement[] realFieldElementArr = (RealFieldElement[]) MathArrays.buildArray(getField(), realFieldElementArrMapState.length);
            Array2DRowFieldMatrix<T> array2DRowFieldMatrixUpdateHighOrderDerivativesPhase1 = null;
            RealFieldElement realFieldElementWalkInOptimizedOrder = (RealFieldElement) getField().getZero().add(10.0d);
            while (((RealFieldElement) realFieldElementWalkInOptimizedOrder.subtract(1.0d)).getReal() >= 0.0d) {
                state = fieldODEStateAndDerivativeTaylor.getState();
                T[] tArrComputeDerivatives = computeDerivatives((T) fieldODEStateAndDerivativeTaylor.getTime(), state);
                for (int i2 = 0; i2 < realFieldElementArr.length; i2++) {
                    realFieldElementArr[i2] = (RealFieldElement) getStepSize().multiply(tArrComputeDerivatives[i2]);
                }
                array2DRowFieldMatrixUpdateHighOrderDerivativesPhase1 = updateHighOrderDerivativesPhase1(this.nordsieck);
                updateHighOrderDerivativesPhase2(this.scaled, realFieldElementArr, array2DRowFieldMatrixUpdateHighOrderDerivativesPhase1);
                realFieldElementWalkInOptimizedOrder = array2DRowFieldMatrixUpdateHighOrderDerivativesPhase1.walkInOptimizedOrder(new Corrector(realFieldElementArrMapState, realFieldElementArr, state));
                if (((RealFieldElement) realFieldElementWalkInOptimizedOrder.subtract(1.0d)).getReal() >= 0.0d) {
                    rescale(filterStep((RealFieldElement) getStepSize().multiply(computeStepGrowShrinkFactor(realFieldElementWalkInOptimizedOrder)), z2, false));
                    fieldODEStateAndDerivativeTaylor = AdamsFieldStepInterpolator.taylor(getStepStart(), (RealFieldElement) getStepStart().getTime().add(getStepSize()), getStepSize(), this.scaled, this.nordsieck);
                }
            }
            T[] tArrComputeDerivatives2 = computeDerivatives((T) fieldODEStateAndDerivativeTaylor.getTime(), state);
            ?? r0 = (T[]) ((RealFieldElement[]) MathArrays.buildArray(getField(), realFieldElementArrMapState.length));
            for (int i3 = 0; i3 < r0.length; i3++) {
                r0[i3] = (RealFieldElement) getStepSize().multiply(tArrComputeDerivatives2[i3]);
            }
            updateHighOrderDerivativesPhase2(realFieldElementArr, r0, array2DRowFieldMatrixUpdateHighOrderDerivativesPhase1);
            fieldODEStateAndDerivativeTaylor = new FieldODEStateAndDerivative((T) fieldODEStateAndDerivativeTaylor.getTime(), state, tArrComputeDerivatives2);
            setStepStart(acceptStep(new AdamsFieldStepInterpolator(getStepSize(), fieldODEStateAndDerivativeTaylor, r0, array2DRowFieldMatrixUpdateHighOrderDerivativesPhase1, z2, getStepStart(), fieldODEStateAndDerivativeTaylor, fieldExpandableODE.getMapper()), t2));
            this.scaled = r0;
            this.nordsieck = array2DRowFieldMatrixUpdateHighOrderDerivativesPhase1;
            if (!isLastStep()) {
                System.arraycopy(state, 0, realFieldElementArrMapState, 0, realFieldElementArrMapState.length);
                if (resetOccurred()) {
                    start(fieldExpandableODE, getStepStart(), t2);
                }
                RealFieldElement realFieldElement = (RealFieldElement) getStepSize().multiply(computeStepGrowShrinkFactor(realFieldElementWalkInOptimizedOrder));
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

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/AdamsMoultonFieldIntegrator$Corrector.class */
    private class Corrector implements FieldMatrixPreservingVisitor<T> {
        private final T[] previous;
        private final T[] scaled;
        private final T[] before;
        private final T[] after;

        Corrector(T[] tArr, T[] tArr2, T[] tArr3) {
            this.previous = tArr;
            this.scaled = tArr2;
            this.after = tArr3;
            this.before = (T[]) ((RealFieldElement[]) tArr3.clone());
        }

        @Override // org.apache.commons.math3.linear.FieldMatrixPreservingVisitor
        public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
            Arrays.fill(this.after, AdamsMoultonFieldIntegrator.this.getField().getZero());
        }

        @Override // org.apache.commons.math3.linear.FieldMatrixPreservingVisitor
        public void visit(int i2, int i3, T t2) {
            if ((i2 & 1) == 0) {
                ((T[]) this.after)[i3] = (RealFieldElement) this.after[i3].subtract(t2);
            } else {
                ((T[]) this.after)[i3] = (RealFieldElement) this.after[i3].add(t2);
            }
        }

        @Override // org.apache.commons.math3.linear.FieldMatrixPreservingVisitor
        public T end() {
            RealFieldElement realFieldElement = (RealFieldElement) AdamsMoultonFieldIntegrator.this.getField().getZero();
            for (int i2 = 0; i2 < this.after.length; i2++) {
                ((T[]) this.after)[i2] = (RealFieldElement) this.after[i2].add(this.previous[i2].add(this.scaled[i2]));
                if (i2 < AdamsMoultonFieldIntegrator.this.mainSetDimension) {
                    RealFieldElement realFieldElementMax = MathUtils.max((RealFieldElement) this.previous[i2].abs(), (RealFieldElement) this.after[i2].abs());
                    RealFieldElement realFieldElement2 = (RealFieldElement) ((RealFieldElement) this.after[i2].subtract(this.before[i2])).divide(AdamsMoultonFieldIntegrator.this.vecAbsoluteTolerance == null ? (RealFieldElement) ((RealFieldElement) realFieldElementMax.multiply(AdamsMoultonFieldIntegrator.this.scalRelativeTolerance)).add(AdamsMoultonFieldIntegrator.this.scalAbsoluteTolerance) : (RealFieldElement) ((RealFieldElement) realFieldElementMax.multiply(AdamsMoultonFieldIntegrator.this.vecRelativeTolerance[i2])).add(AdamsMoultonFieldIntegrator.this.vecAbsoluteTolerance[i2]));
                    realFieldElement = (RealFieldElement) realFieldElement.add((RealFieldElement) realFieldElement2.multiply(realFieldElement2));
                }
            }
            return (T) ((RealFieldElement) realFieldElement.divide(AdamsMoultonFieldIntegrator.this.mainSetDimension)).sqrt();
        }
    }
}
