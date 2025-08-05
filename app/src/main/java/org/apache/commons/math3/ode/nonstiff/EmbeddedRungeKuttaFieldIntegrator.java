package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.ode.FieldEquationsMapper;
import org.apache.commons.math3.ode.FieldExpandableODE;
import org.apache.commons.math3.ode.FieldODEState;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/EmbeddedRungeKuttaFieldIntegrator.class */
public abstract class EmbeddedRungeKuttaFieldIntegrator<T extends RealFieldElement<T>> extends AdaptiveStepsizeFieldIntegrator<T> implements FieldButcherArrayProvider<T> {
    private final int fsal;

    /* renamed from: c, reason: collision with root package name */
    private final T[] f13052c;

    /* renamed from: a, reason: collision with root package name */
    private final T[][] f13053a;

    /* renamed from: b, reason: collision with root package name */
    private final T[] f13054b;
    private final T exp;
    private T safety;
    private T minReduction;
    private T maxGrowth;

    protected abstract RungeKuttaFieldStepInterpolator<T> createInterpolator(boolean z2, T[][] tArr, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative, FieldODEStateAndDerivative<T> fieldODEStateAndDerivative2, FieldEquationsMapper<T> fieldEquationsMapper);

    public abstract int getOrder();

    protected abstract T estimateError(T[][] tArr, T[] tArr2, T[] tArr3, T t2);

    protected EmbeddedRungeKuttaFieldIntegrator(Field<T> field, String name, int fsal, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(field, name, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        this.fsal = fsal;
        this.f13052c = getC();
        this.f13053a = getA();
        this.f13054b = getB();
        this.exp = (T) field.getOne().divide(-getOrder());
        setSafety((RealFieldElement) field.getZero().add(0.9d));
        setMinReduction((RealFieldElement) field.getZero().add(0.2d));
        setMaxGrowth((RealFieldElement) field.getZero().add(10.0d));
    }

    protected EmbeddedRungeKuttaFieldIntegrator(Field<T> field, String name, int fsal, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(field, name, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        this.fsal = fsal;
        this.f13052c = getC();
        this.f13053a = getA();
        this.f13054b = getB();
        this.exp = (T) field.getOne().divide(-getOrder());
        setSafety((RealFieldElement) field.getZero().add(0.9d));
        setMinReduction((RealFieldElement) field.getZero().add(0.2d));
        setMaxGrowth((RealFieldElement) field.getZero().add(10.0d));
    }

    protected T fraction(int p2, int q2) {
        return (T) ((RealFieldElement) getField().getOne().multiply(p2)).divide(q2);
    }

    protected T fraction(double p2, double q2) {
        return (T) ((RealFieldElement) getField().getOne().multiply(p2)).divide(q2);
    }

    public T getSafety() {
        return this.safety;
    }

    public void setSafety(T safety) {
        this.safety = safety;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v105, types: [org.apache.commons.math3.RealFieldElement] */
    @Override // org.apache.commons.math3.ode.FirstOrderFieldIntegrator
    public FieldODEStateAndDerivative<T> integrate(FieldExpandableODE<T> equations, FieldODEState<T> initialState, T finalTime) throws MaxCountExceededException, MathIllegalArgumentException {
        sanityChecks(initialState, finalTime);
        RealFieldElement time = initialState.getTime();
        RealFieldElement[] realFieldElementArrMapState = equations.getMapper().mapState(initialState);
        setStepStart(initIntegration(equations, time, realFieldElementArrMapState, finalTime));
        boolean forward = ((RealFieldElement) finalTime.subtract(initialState.getTime())).getReal() > 0.0d;
        int stages = this.f13052c.length + 1;
        RealFieldElement[] realFieldElementArrMapState2 = realFieldElementArrMapState;
        RealFieldElement[][] realFieldElementArr = (RealFieldElement[][]) MathArrays.buildArray(getField(), stages, -1);
        RealFieldElement[] realFieldElementArr2 = (RealFieldElement[]) MathArrays.buildArray(getField(), realFieldElementArrMapState.length);
        T hNew = getField().getZero();
        boolean firstTime = true;
        setIsLastStep(false);
        do {
            RealFieldElement realFieldElementEstimateError = (RealFieldElement) getField().getZero().add(10.0d);
            while (((RealFieldElement) realFieldElementEstimateError.subtract(1.0d)).getReal() >= 0.0d) {
                realFieldElementArrMapState2 = equations.getMapper().mapState(getStepStart());
                realFieldElementArr[0] = equations.getMapper().mapDerivative(getStepStart());
                if (firstTime) {
                    RealFieldElement[] realFieldElementArr3 = (RealFieldElement[]) MathArrays.buildArray(getField(), this.mainSetDimension);
                    if (this.vecAbsoluteTolerance == null) {
                        for (int i2 = 0; i2 < realFieldElementArr3.length; i2++) {
                            realFieldElementArr3[i2] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElementArrMapState2[i2].abs()).multiply(this.scalRelativeTolerance)).add(this.scalAbsoluteTolerance);
                        }
                    } else {
                        for (int i3 = 0; i3 < realFieldElementArr3.length; i3++) {
                            realFieldElementArr3[i3] = (RealFieldElement) ((RealFieldElement) ((RealFieldElement) realFieldElementArrMapState2[i3].abs()).multiply(this.vecRelativeTolerance[i3])).add(this.vecAbsoluteTolerance[i3]);
                        }
                    }
                    hNew = initializeStep(forward, getOrder(), realFieldElementArr3, getStepStart(), equations.getMapper());
                    firstTime = false;
                }
                setStepSize(hNew);
                if (forward) {
                    if (((RealFieldElement) ((RealFieldElement) getStepStart().getTime().add(getStepSize())).subtract(finalTime)).getReal() >= 0.0d) {
                        setStepSize((RealFieldElement) finalTime.subtract(getStepStart().getTime()));
                    }
                } else if (((RealFieldElement) ((RealFieldElement) getStepStart().getTime().add(getStepSize())).subtract(finalTime)).getReal() <= 0.0d) {
                    setStepSize((RealFieldElement) finalTime.subtract(getStepStart().getTime()));
                }
                for (int k2 = 1; k2 < stages; k2++) {
                    for (int j2 = 0; j2 < realFieldElementArrMapState.length; j2++) {
                        RealFieldElement realFieldElement = (RealFieldElement) realFieldElementArr[0][j2].multiply(this.f13053a[k2 - 1][0]);
                        for (int l2 = 1; l2 < k2; l2++) {
                            realFieldElement = (RealFieldElement) realFieldElement.add((RealFieldElement) realFieldElementArr[l2][j2].multiply(this.f13053a[k2 - 1][l2]));
                        }
                        realFieldElementArr2[j2] = (RealFieldElement) realFieldElementArrMapState2[j2].add((RealFieldElement) getStepSize().multiply(realFieldElement));
                    }
                    realFieldElementArr[k2] = computeDerivatives((RealFieldElement) getStepStart().getTime().add(getStepSize().multiply(this.f13052c[k2 - 1])), realFieldElementArr2);
                }
                for (int j3 = 0; j3 < realFieldElementArrMapState.length; j3++) {
                    RealFieldElement realFieldElement2 = (RealFieldElement) realFieldElementArr[0][j3].multiply(this.f13054b[0]);
                    for (int l3 = 1; l3 < stages; l3++) {
                        realFieldElement2 = (RealFieldElement) realFieldElement2.add((RealFieldElement) realFieldElementArr[l3][j3].multiply(this.f13054b[l3]));
                    }
                    realFieldElementArr2[j3] = (RealFieldElement) realFieldElementArrMapState2[j3].add((RealFieldElement) getStepSize().multiply(realFieldElement2));
                }
                realFieldElementEstimateError = estimateError(realFieldElementArr, realFieldElementArrMapState2, realFieldElementArr2, getStepSize());
                if (((RealFieldElement) realFieldElementEstimateError.subtract(1.0d)).getReal() >= 0.0d) {
                    hNew = filterStep((RealFieldElement) getStepSize().multiply(MathUtils.min(this.maxGrowth, MathUtils.max(this.minReduction, (RealFieldElement) this.safety.multiply(realFieldElementEstimateError.pow(this.exp))))), forward, false);
                }
            }
            RealFieldElement realFieldElement3 = (RealFieldElement) getStepStart().getTime().add(getStepSize());
            FieldODEStateAndDerivative<T> stateTmp = new FieldODEStateAndDerivative<>(realFieldElement3, realFieldElementArr2, this.fsal >= 0 ? realFieldElementArr[this.fsal] : computeDerivatives(realFieldElement3, realFieldElementArr2));
            System.arraycopy(realFieldElementArr2, 0, realFieldElementArrMapState2, 0, realFieldElementArrMapState.length);
            setStepStart(acceptStep(createInterpolator(forward, realFieldElementArr, getStepStart(), stateTmp, equations.getMapper()), finalTime));
            if (!isLastStep()) {
                RealFieldElement realFieldElement4 = (RealFieldElement) getStepSize().multiply(MathUtils.min(this.maxGrowth, MathUtils.max(this.minReduction, (RealFieldElement) this.safety.multiply(realFieldElementEstimateError.pow(this.exp)))));
                RealFieldElement realFieldElement5 = (RealFieldElement) getStepStart().getTime().add(realFieldElement4);
                boolean nextIsLast = forward ? ((RealFieldElement) realFieldElement5.subtract(finalTime)).getReal() >= 0.0d : ((RealFieldElement) realFieldElement5.subtract(finalTime)).getReal() <= 0.0d;
                hNew = filterStep(realFieldElement4, forward, nextIsLast);
                RealFieldElement realFieldElement6 = (RealFieldElement) getStepStart().getTime().add(hNew);
                boolean filteredNextIsLast = forward ? ((RealFieldElement) realFieldElement6.subtract(finalTime)).getReal() >= 0.0d : ((RealFieldElement) realFieldElement6.subtract(finalTime)).getReal() <= 0.0d;
                if (filteredNextIsLast) {
                    hNew = (RealFieldElement) finalTime.subtract(getStepStart().getTime());
                }
            }
        } while (!isLastStep());
        FieldODEStateAndDerivative<T> finalState = getStepStart();
        resetInternalState();
        return finalState;
    }

    public T getMinReduction() {
        return this.minReduction;
    }

    public void setMinReduction(T minReduction) {
        this.minReduction = minReduction;
    }

    public T getMaxGrowth() {
        return this.maxGrowth;
    }

    public void setMaxGrowth(T maxGrowth) {
        this.maxGrowth = maxGrowth;
    }
}
