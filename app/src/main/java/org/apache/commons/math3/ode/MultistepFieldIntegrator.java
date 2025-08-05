package org.apache.commons.math3.ode;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowFieldMatrix;
import org.apache.commons.math3.ode.nonstiff.AdaptiveStepsizeFieldIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853FieldIntegrator;
import org.apache.commons.math3.ode.sampling.FieldStepHandler;
import org.apache.commons.math3.ode.sampling.FieldStepInterpolator;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/MultistepFieldIntegrator.class */
public abstract class MultistepFieldIntegrator<T extends RealFieldElement<T>> extends AdaptiveStepsizeFieldIntegrator<T> {
    protected T[] scaled;
    protected Array2DRowFieldMatrix<T> nordsieck;
    private FirstOrderFieldIntegrator<T> starter;
    private final int nSteps;
    private double exp;
    private double safety;
    private double minReduction;
    private double maxGrowth;

    protected abstract Array2DRowFieldMatrix<T> initializeHighOrderDerivatives(T t2, T[] tArr, T[][] tArr2, T[][] tArr3);

    protected MultistepFieldIntegrator(Field<T> field, String name, int nSteps, int order, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) throws NumberIsTooSmallException {
        super(field, name, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        if (nSteps < 2) {
            throw new NumberIsTooSmallException(LocalizedFormats.INTEGRATION_METHOD_NEEDS_AT_LEAST_TWO_PREVIOUS_POINTS, Integer.valueOf(nSteps), 2, true);
        }
        this.starter = new DormandPrince853FieldIntegrator(field, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        this.nSteps = nSteps;
        this.exp = (-1.0d) / order;
        setSafety(0.9d);
        setMinReduction(0.2d);
        setMaxGrowth(FastMath.pow(2.0d, -this.exp));
    }

    protected MultistepFieldIntegrator(Field<T> field, String name, int nSteps, int order, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(field, name, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        this.starter = new DormandPrince853FieldIntegrator(field, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        this.nSteps = nSteps;
        this.exp = (-1.0d) / order;
        setSafety(0.9d);
        setMinReduction(0.2d);
        setMaxGrowth(FastMath.pow(2.0d, -this.exp));
    }

    public FirstOrderFieldIntegrator<T> getStarterIntegrator() {
        return this.starter;
    }

    public void setStarterIntegrator(FirstOrderFieldIntegrator<T> starterIntegrator) {
        this.starter = starterIntegrator;
    }

    protected void start(FieldExpandableODE<T> equations, FieldODEState<T> initialState, T t2) throws NumberIsTooSmallException, MaxCountExceededException, DimensionMismatchException, NoBracketingException {
        this.starter.clearEventHandlers();
        this.starter.clearStepHandlers();
        this.starter.addStepHandler(new FieldNordsieckInitializer(equations.getMapper(), (this.nSteps + 3) / 2));
        try {
            this.starter.integrate(equations, initialState, t2);
            throw new MathIllegalStateException(LocalizedFormats.MULTISTEP_STARTER_STOPPED_EARLY, new Object[0]);
        } catch (InitializationCompletedMarkerException e2) {
            getEvaluationsCounter().increment(this.starter.getEvaluations());
            this.starter.clearStepHandlers();
        }
    }

    public double getMinReduction() {
        return this.minReduction;
    }

    public void setMinReduction(double minReduction) {
        this.minReduction = minReduction;
    }

    public double getMaxGrowth() {
        return this.maxGrowth;
    }

    public void setMaxGrowth(double maxGrowth) {
        this.maxGrowth = maxGrowth;
    }

    public double getSafety() {
        return this.safety;
    }

    public void setSafety(double safety) {
        this.safety = safety;
    }

    public int getNSteps() {
        return this.nSteps;
    }

    protected void rescale(T t2) {
        RealFieldElement realFieldElement = (RealFieldElement) t2.divide(getStepSize());
        for (int i2 = 0; i2 < this.scaled.length; i2++) {
            ((T[]) this.scaled)[i2] = (RealFieldElement) this.scaled[i2].multiply(realFieldElement);
        }
        RealFieldElement realFieldElement2 = realFieldElement;
        for (RealFieldElement[] realFieldElementArr : (RealFieldElement[][]) this.nordsieck.getDataRef()) {
            realFieldElement2 = (RealFieldElement) realFieldElement2.multiply(realFieldElement);
            for (int i3 = 0; i3 < realFieldElementArr.length; i3++) {
                realFieldElementArr[i3] = (RealFieldElement) realFieldElementArr[i3].multiply(realFieldElement2);
            }
        }
        setStepSize(t2);
    }

    protected T computeStepGrowShrinkFactor(T t2) {
        return (T) MathUtils.min((RealFieldElement) ((RealFieldElement) t2.getField2().getZero()).add(this.maxGrowth), MathUtils.max((RealFieldElement) ((RealFieldElement) t2.getField2().getZero()).add(this.minReduction), (RealFieldElement) ((RealFieldElement) t2.pow(this.exp)).multiply(this.safety)));
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/MultistepFieldIntegrator$FieldNordsieckInitializer.class */
    private class FieldNordsieckInitializer implements FieldStepHandler<T> {
        private final FieldEquationsMapper<T> mapper;
        private int count = 0;
        private FieldODEStateAndDerivative<T> savedStart;

        /* renamed from: t, reason: collision with root package name */
        private final T[] f13045t;

        /* renamed from: y, reason: collision with root package name */
        private final T[][] f13046y;
        private final T[][] yDot;

        FieldNordsieckInitializer(FieldEquationsMapper<T> fieldEquationsMapper, int i2) {
            this.mapper = fieldEquationsMapper;
            this.f13045t = (T[]) ((RealFieldElement[]) MathArrays.buildArray(MultistepFieldIntegrator.this.getField(), i2));
            this.f13046y = (T[][]) ((RealFieldElement[][]) MathArrays.buildArray(MultistepFieldIntegrator.this.getField(), i2, -1));
            this.yDot = (T[][]) ((RealFieldElement[][]) MathArrays.buildArray(MultistepFieldIntegrator.this.getField(), i2, -1));
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // org.apache.commons.math3.ode.sampling.FieldStepHandler
        public void handleStep(FieldStepInterpolator<T> fieldStepInterpolator, boolean z2) throws MaxCountExceededException {
            if (this.count == 0) {
                FieldODEStateAndDerivative<T> previousState = fieldStepInterpolator.getPreviousState();
                this.savedStart = previousState;
                this.f13045t[this.count] = previousState.getTime();
                ((T[][]) this.f13046y)[this.count] = this.mapper.mapState(previousState);
                ((T[][]) this.yDot)[this.count] = this.mapper.mapDerivative(previousState);
            }
            this.count++;
            FieldODEStateAndDerivative<T> currentState = fieldStepInterpolator.getCurrentState();
            this.f13045t[this.count] = currentState.getTime();
            ((T[][]) this.f13046y)[this.count] = this.mapper.mapState(currentState);
            ((T[][]) this.yDot)[this.count] = this.mapper.mapDerivative(currentState);
            if (this.count == this.f13045t.length - 1) {
                MultistepFieldIntegrator.this.setStepSize((RealFieldElement) ((RealFieldElement) this.f13045t[this.f13045t.length - 1].subtract(this.f13045t[0])).divide(this.f13045t.length - 1));
                MultistepFieldIntegrator.this.scaled = (T[]) ((RealFieldElement[]) MathArrays.buildArray(MultistepFieldIntegrator.this.getField(), this.yDot[0].length));
                for (int i2 = 0; i2 < MultistepFieldIntegrator.this.scaled.length; i2++) {
                    ((T[]) MultistepFieldIntegrator.this.scaled)[i2] = (RealFieldElement) this.yDot[0][i2].multiply(MultistepFieldIntegrator.this.getStepSize());
                }
                MultistepFieldIntegrator.this.nordsieck = MultistepFieldIntegrator.this.initializeHighOrderDerivatives(MultistepFieldIntegrator.this.getStepSize(), this.f13045t, this.f13046y, this.yDot);
                MultistepFieldIntegrator.this.setStepStart(this.savedStart);
                throw new InitializationCompletedMarkerException();
            }
        }

        @Override // org.apache.commons.math3.ode.sampling.FieldStepHandler
        public void init(FieldODEStateAndDerivative<T> initialState, T finalTime) {
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/MultistepFieldIntegrator$InitializationCompletedMarkerException.class */
    private static class InitializationCompletedMarkerException extends RuntimeException {
        private static final long serialVersionUID = -1914085471038046418L;

        InitializationCompletedMarkerException() {
            super((Throwable) null);
        }
    }
}
