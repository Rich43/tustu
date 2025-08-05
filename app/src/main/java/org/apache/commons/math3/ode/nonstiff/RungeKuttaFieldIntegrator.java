package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.AbstractFieldIntegrator;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldExpandableODE;
import org.apache.commons.math3.ode.FieldODEState;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.ode.FirstOrderFieldDifferentialEquations;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/RungeKuttaFieldIntegrator.class */
public abstract class RungeKuttaFieldIntegrator<T extends RealFieldElement<T>> extends AbstractFieldIntegrator<T> implements FieldButcherArrayProvider<T> {

    /* renamed from: c, reason: collision with root package name */
    private final T[] f13061c;

    /* renamed from: a, reason: collision with root package name */
    private final T[][] f13062a;

    /* renamed from: b, reason: collision with root package name */
    private final T[] f13063b;
    private final T step;

    protected abstract RungeKuttaFieldStepInterpolator<T> createInterpolator(boolean z2, T[][] tArr, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative2, FieldEquationsMapper<T> fieldEquationsMapper);

    protected RungeKuttaFieldIntegrator(Field<T> field, String name, T step) {
        super(field, name);
        this.f13061c = getC();
        this.f13062a = getA();
        this.f13063b = getB();
        this.step = (T) step.abs();
    }

    protected T fraction(int p2, int q2) {
        return (T) ((RealFieldElement) getField().getZero().add(p2)).divide(q2);
    }

    @Override // org.apache.commons.math3.ode.FirstOrderFieldIntegrator
    public FieldODEStateAndDerivative<T> integrate(FieldExpandableODE<T> equations, FieldODEState<T> initialState, T finalTime) throws MaxCountExceededException, MathIllegalArgumentException {
        sanityChecks(initialState, finalTime);
        RealFieldElement time = initialState.getTime();
        RealFieldElement[] realFieldElementArrMapState = equations.getMapper().mapState(initialState);
        setStepStart(initIntegration(equations, time, realFieldElementArrMapState, finalTime));
        boolean forward = ((RealFieldElement) finalTime.subtract(initialState.getTime())).getReal() > 0.0d;
        int stages = this.f13061c.length + 1;
        RealFieldElement[][] realFieldElementArr = (RealFieldElement[][]) MathArrays.buildArray(getField(), stages, -1);
        RealFieldElement[] realFieldElementArr2 = (RealFieldElement[]) MathArrays.buildArray(getField(), realFieldElementArrMapState.length);
        if (forward) {
            if (((RealFieldElement) ((RealFieldElement) getStepStart().getTime().add(this.step)).subtract(finalTime)).getReal() >= 0.0d) {
                setStepSize((RealFieldElement) finalTime.subtract(getStepStart().getTime()));
            } else {
                setStepSize(this.step);
            }
        } else if (((RealFieldElement) ((RealFieldElement) getStepStart().getTime().subtract(this.step)).subtract(finalTime)).getReal() <= 0.0d) {
            setStepSize((RealFieldElement) finalTime.subtract(getStepStart().getTime()));
        } else {
            setStepSize((RealFieldElement) this.step.negate());
        }
        setIsLastStep(false);
        do {
            RealFieldElement[] realFieldElementArrMapState2 = equations.getMapper().mapState(getStepStart());
            realFieldElementArr[0] = equations.getMapper().mapDerivative(getStepStart());
            for (int k2 = 1; k2 < stages; k2++) {
                for (int j2 = 0; j2 < realFieldElementArrMapState.length; j2++) {
                    RealFieldElement realFieldElement = (RealFieldElement) realFieldElementArr[0][j2].multiply(this.f13062a[k2 - 1][0]);
                    for (int l2 = 1; l2 < k2; l2++) {
                        realFieldElement = (RealFieldElement) realFieldElement.add((RealFieldElement) realFieldElementArr[l2][j2].multiply(this.f13062a[k2 - 1][l2]));
                    }
                    realFieldElementArr2[j2] = (RealFieldElement) realFieldElementArrMapState2[j2].add((RealFieldElement) getStepSize().multiply(realFieldElement));
                }
                realFieldElementArr[k2] = computeDerivatives((RealFieldElement) getStepStart().getTime().add(getStepSize().multiply(this.f13061c[k2 - 1])), realFieldElementArr2);
            }
            for (int j3 = 0; j3 < realFieldElementArrMapState.length; j3++) {
                RealFieldElement realFieldElement2 = (RealFieldElement) realFieldElementArr[0][j3].multiply(this.f13063b[0]);
                for (int l3 = 1; l3 < stages; l3++) {
                    realFieldElement2 = (RealFieldElement) realFieldElement2.add((RealFieldElement) realFieldElementArr[l3][j3].multiply(this.f13063b[l3]));
                }
                realFieldElementArr2[j3] = (RealFieldElement) realFieldElementArrMapState2[j3].add((RealFieldElement) getStepSize().multiply(realFieldElement2));
            }
            RealFieldElement realFieldElement3 = (RealFieldElement) getStepStart().getTime().add(getStepSize());
            T[] yDotTmp = computeDerivatives(realFieldElement3, realFieldElementArr2);
            FieldODEStateAndDerivative<T> stateTmp = new FieldODEStateAndDerivative<>(realFieldElement3, realFieldElementArr2, yDotTmp);
            System.arraycopy(realFieldElementArr2, 0, realFieldElementArrMapState2, 0, realFieldElementArrMapState.length);
            setStepStart(acceptStep(createInterpolator(forward, realFieldElementArr, getStepStart(), stateTmp, equations.getMapper()), finalTime));
            if (!isLastStep()) {
                RealFieldElement realFieldElement4 = (RealFieldElement) getStepStart().getTime().add(getStepSize());
                boolean nextIsLast = forward ? ((RealFieldElement) realFieldElement4.subtract(finalTime)).getReal() >= 0.0d : ((RealFieldElement) realFieldElement4.subtract(finalTime)).getReal() <= 0.0d;
                if (nextIsLast) {
                    setStepSize((RealFieldElement) finalTime.subtract(getStepStart().getTime()));
                }
            }
        } while (!isLastStep());
        FieldODEStateAndDerivative<T> finalState = getStepStart();
        setStepStart(null);
        setStepSize(null);
        return finalState;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public T[] singleStep(FirstOrderFieldDifferentialEquations<T> firstOrderFieldDifferentialEquations, T t2, T[] tArr, T t3) {
        T[] tArr2 = (T[]) ((RealFieldElement[]) tArr.clone());
        int length = this.f13061c.length + 1;
        RealFieldElement[][] realFieldElementArr = (RealFieldElement[][]) MathArrays.buildArray(getField(), length, -1);
        RealFieldElement[] realFieldElementArr2 = (RealFieldElement[]) tArr.clone();
        RealFieldElement realFieldElement = (RealFieldElement) t3.subtract(t2);
        realFieldElementArr[0] = firstOrderFieldDifferentialEquations.computeDerivatives(t2, tArr2);
        for (int i2 = 1; i2 < length; i2++) {
            for (int i3 = 0; i3 < tArr.length; i3++) {
                RealFieldElement realFieldElement2 = (RealFieldElement) realFieldElementArr[0][i3].multiply(this.f13062a[i2 - 1][0]);
                for (int i4 = 1; i4 < i2; i4++) {
                    realFieldElement2 = (RealFieldElement) realFieldElement2.add((RealFieldElement) realFieldElementArr[i4][i3].multiply(this.f13062a[i2 - 1][i4]));
                }
                realFieldElementArr2[i3] = (RealFieldElement) tArr2[i3].add((DerivativeStructure) realFieldElement.multiply(realFieldElement2));
            }
            realFieldElementArr[i2] = firstOrderFieldDifferentialEquations.computeDerivatives((RealFieldElement) t2.add(realFieldElement.multiply(this.f13061c[i2 - 1])), realFieldElementArr2);
        }
        for (int i5 = 0; i5 < tArr.length; i5++) {
            RealFieldElement realFieldElement3 = (RealFieldElement) realFieldElementArr[0][i5].multiply(this.f13063b[0]);
            for (int i6 = 1; i6 < length; i6++) {
                realFieldElement3 = (RealFieldElement) realFieldElement3.add((RealFieldElement) realFieldElementArr[i6][i5].multiply(this.f13063b[i6]));
            }
            tArr2[i5] = (RealFieldElement) tArr2[i5].add((DerivativeStructure) realFieldElement.multiply(realFieldElement3));
        }
        return tArr2;
    }
}
