package org.apache.commons.math3.ode;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.ode.nonstiff.AdaptiveStepsizeIntegrator;
import org.apache.commons.math3.ode.nonstiff.DormandPrince853Integrator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/MultistepIntegrator.class */
public abstract class MultistepIntegrator extends AdaptiveStepsizeIntegrator {
    protected double[] scaled;
    protected Array2DRowRealMatrix nordsieck;
    private FirstOrderIntegrator starter;
    private final int nSteps;
    private double exp;
    private double safety;
    private double minReduction;
    private double maxGrowth;

    @Deprecated
    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/MultistepIntegrator$NordsieckTransformer.class */
    public interface NordsieckTransformer {
        Array2DRowRealMatrix initializeHighOrderDerivatives(double d2, double[] dArr, double[][] dArr2, double[][] dArr3);
    }

    protected abstract Array2DRowRealMatrix initializeHighOrderDerivatives(double d2, double[] dArr, double[][] dArr2, double[][] dArr3);

    protected MultistepIntegrator(String name, int nSteps, int order, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) throws NumberIsTooSmallException {
        super(name, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        if (nSteps < 2) {
            throw new NumberIsTooSmallException(LocalizedFormats.INTEGRATION_METHOD_NEEDS_AT_LEAST_TWO_PREVIOUS_POINTS, Integer.valueOf(nSteps), 2, true);
        }
        this.starter = new DormandPrince853Integrator(minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        this.nSteps = nSteps;
        this.exp = (-1.0d) / order;
        setSafety(0.9d);
        setMinReduction(0.2d);
        setMaxGrowth(FastMath.pow(2.0d, -this.exp));
    }

    protected MultistepIntegrator(String name, int nSteps, int order, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(name, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        this.starter = new DormandPrince853Integrator(minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        this.nSteps = nSteps;
        this.exp = (-1.0d) / order;
        setSafety(0.9d);
        setMinReduction(0.2d);
        setMaxGrowth(FastMath.pow(2.0d, -this.exp));
    }

    public ODEIntegrator getStarterIntegrator() {
        return this.starter;
    }

    public void setStarterIntegrator(FirstOrderIntegrator starterIntegrator) {
        this.starter = starterIntegrator;
    }

    protected void start(double t0, double[] y0, double t2) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        this.starter.clearEventHandlers();
        this.starter.clearStepHandlers();
        this.starter.addStepHandler(new NordsieckInitializer((this.nSteps + 3) / 2, y0.length));
        try {
            if (this.starter instanceof AbstractIntegrator) {
                ((AbstractIntegrator) this.starter).integrate(getExpandable(), t2);
            } else {
                this.starter.integrate(new FirstOrderDifferentialEquations() { // from class: org.apache.commons.math3.ode.MultistepIntegrator.1
                    @Override // org.apache.commons.math3.ode.FirstOrderDifferentialEquations
                    public int getDimension() {
                        return MultistepIntegrator.this.getExpandable().getTotalDimension();
                    }

                    @Override // org.apache.commons.math3.ode.FirstOrderDifferentialEquations
                    public void computeDerivatives(double t3, double[] y2, double[] yDot) throws MaxCountExceededException, DimensionMismatchException {
                        MultistepIntegrator.this.getExpandable().computeDerivatives(t3, y2, yDot);
                    }
                }, t0, y0, t2, new double[y0.length]);
            }
            throw new MathIllegalStateException(LocalizedFormats.MULTISTEP_STARTER_STOPPED_EARLY, new Object[0]);
        } catch (InitializationCompletedMarkerException e2) {
            getCounter().increment(this.starter.getEvaluations());
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

    protected double computeStepGrowShrinkFactor(double error) {
        return FastMath.min(this.maxGrowth, FastMath.max(this.minReduction, this.safety * FastMath.pow(error, this.exp)));
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/MultistepIntegrator$NordsieckInitializer.class */
    private class NordsieckInitializer implements StepHandler {
        private int count = 0;

        /* renamed from: t, reason: collision with root package name */
        private final double[] f13047t;

        /* renamed from: y, reason: collision with root package name */
        private final double[][] f13048y;
        private final double[][] yDot;

        NordsieckInitializer(int nbStartPoints, int n2) {
            this.f13047t = new double[nbStartPoints];
            this.f13048y = new double[nbStartPoints][n2];
            this.yDot = new double[nbStartPoints][n2];
        }

        @Override // org.apache.commons.math3.ode.sampling.StepHandler
        public void handleStep(StepInterpolator interpolator, boolean isLast) throws DimensionMismatchException, MaxCountExceededException {
            double prev = interpolator.getPreviousTime();
            double curr = interpolator.getCurrentTime();
            if (this.count == 0) {
                interpolator.setInterpolatedTime(prev);
                this.f13047t[0] = prev;
                ExpandableStatefulODE expandable = MultistepIntegrator.this.getExpandable();
                EquationsMapper primary = expandable.getPrimaryMapper();
                primary.insertEquationData(interpolator.getInterpolatedState(), this.f13048y[this.count]);
                primary.insertEquationData(interpolator.getInterpolatedDerivatives(), this.yDot[this.count]);
                int index = 0;
                EquationsMapper[] arr$ = expandable.getSecondaryMappers();
                for (EquationsMapper secondary : arr$) {
                    secondary.insertEquationData(interpolator.getInterpolatedSecondaryState(index), this.f13048y[this.count]);
                    secondary.insertEquationData(interpolator.getInterpolatedSecondaryDerivatives(index), this.yDot[this.count]);
                    index++;
                }
            }
            this.count++;
            interpolator.setInterpolatedTime(curr);
            this.f13047t[this.count] = curr;
            ExpandableStatefulODE expandable2 = MultistepIntegrator.this.getExpandable();
            EquationsMapper primary2 = expandable2.getPrimaryMapper();
            primary2.insertEquationData(interpolator.getInterpolatedState(), this.f13048y[this.count]);
            primary2.insertEquationData(interpolator.getInterpolatedDerivatives(), this.yDot[this.count]);
            int index2 = 0;
            EquationsMapper[] arr$2 = expandable2.getSecondaryMappers();
            for (EquationsMapper secondary2 : arr$2) {
                secondary2.insertEquationData(interpolator.getInterpolatedSecondaryState(index2), this.f13048y[this.count]);
                secondary2.insertEquationData(interpolator.getInterpolatedSecondaryDerivatives(index2), this.yDot[this.count]);
                index2++;
            }
            if (this.count == this.f13047t.length - 1) {
                MultistepIntegrator.this.stepStart = this.f13047t[0];
                MultistepIntegrator.this.stepSize = (this.f13047t[this.f13047t.length - 1] - this.f13047t[0]) / (this.f13047t.length - 1);
                MultistepIntegrator.this.scaled = (double[]) this.yDot[0].clone();
                for (int j2 = 0; j2 < MultistepIntegrator.this.scaled.length; j2++) {
                    double[] dArr = MultistepIntegrator.this.scaled;
                    int i2 = j2;
                    dArr[i2] = dArr[i2] * MultistepIntegrator.this.stepSize;
                }
                MultistepIntegrator.this.nordsieck = MultistepIntegrator.this.initializeHighOrderDerivatives(MultistepIntegrator.this.stepSize, this.f13047t, this.f13048y, this.yDot);
                throw new InitializationCompletedMarkerException();
            }
        }

        @Override // org.apache.commons.math3.ode.sampling.StepHandler
        public void init(double t0, double[] y0, double time) {
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/MultistepIntegrator$InitializationCompletedMarkerException.class */
    private static class InitializationCompletedMarkerException extends RuntimeException {
        private static final long serialVersionUID = -1914085471038046418L;

        InitializationCompletedMarkerException() {
            super((Throwable) null);
        }
    }
}
