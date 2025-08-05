package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.ode.EquationsMapper;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.sampling.NordsieckStepInterpolator;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/AdamsBashforthIntegrator.class */
public class AdamsBashforthIntegrator extends AdamsIntegrator {
    private static final String METHOD_NAME = "Adams-Bashforth";

    public AdamsBashforthIntegrator(int nSteps, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) throws NumberIsTooSmallException {
        super(METHOD_NAME, nSteps, nSteps, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    public AdamsBashforthIntegrator(int nSteps, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) throws IllegalArgumentException {
        super(METHOD_NAME, nSteps, nSteps, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

    private double errorEstimation(double[] previousState, double[] predictedState, double[] predictedScaled, RealMatrix predictedNordsieck) {
        double error = 0.0d;
        for (int i2 = 0; i2 < this.mainSetDimension; i2++) {
            double yScale = FastMath.abs(predictedState[i2]);
            double tol = this.vecAbsoluteTolerance == null ? this.scalAbsoluteTolerance + (this.scalRelativeTolerance * yScale) : this.vecAbsoluteTolerance[i2] + (this.vecRelativeTolerance[i2] * yScale);
            double variation = 0.0d;
            int sign = predictedNordsieck.getRowDimension() % 2 == 0 ? -1 : 1;
            for (int k2 = predictedNordsieck.getRowDimension() - 1; k2 >= 0; k2--) {
                variation += sign * predictedNordsieck.getEntry(k2, i2);
                sign = -sign;
            }
            double ratio = ((predictedState[i2] - previousState[i2]) + (variation - predictedScaled[i2])) / tol;
            error += ratio * ratio;
        }
        return FastMath.sqrt(error / this.mainSetDimension);
    }

    @Override // org.apache.commons.math3.ode.nonstiff.AdamsIntegrator, org.apache.commons.math3.ode.nonstiff.AdaptiveStepsizeIntegrator, org.apache.commons.math3.ode.AbstractIntegrator
    public void integrate(ExpandableStatefulODE equations, double t2) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        sanityChecks(equations, t2);
        setEquations(equations);
        boolean forward = t2 > equations.getTime();
        double[] y2 = equations.getCompleteState();
        double[] yDot = new double[y2.length];
        NordsieckStepInterpolator interpolator = new NordsieckStepInterpolator();
        interpolator.reinitialize(y2, forward, equations.getPrimaryMapper(), equations.getSecondaryMappers());
        initIntegration(equations.getTime(), y2, t2);
        start(equations.getTime(), y2, t2);
        interpolator.reinitialize(this.stepStart, this.stepSize, this.scaled, this.nordsieck);
        interpolator.storeTime(this.stepStart);
        double hNew = this.stepSize;
        interpolator.rescale(hNew);
        this.isLastStep = false;
        do {
            interpolator.shift();
            double[] predictedY = new double[y2.length];
            double[] predictedScaled = new double[y2.length];
            Array2DRowRealMatrix predictedNordsieck = null;
            double error = 10.0d;
            while (error >= 1.0d) {
                double stepEnd = this.stepStart + hNew;
                interpolator.storeTime(stepEnd);
                ExpandableStatefulODE expandable = getExpandable();
                EquationsMapper primary = expandable.getPrimaryMapper();
                primary.insertEquationData(interpolator.getInterpolatedState(), predictedY);
                int index = 0;
                EquationsMapper[] arr$ = expandable.getSecondaryMappers();
                for (EquationsMapper secondary : arr$) {
                    secondary.insertEquationData(interpolator.getInterpolatedSecondaryState(index), predictedY);
                    index++;
                }
                computeDerivatives(stepEnd, predictedY, yDot);
                for (int j2 = 0; j2 < predictedScaled.length; j2++) {
                    predictedScaled[j2] = hNew * yDot[j2];
                }
                predictedNordsieck = updateHighOrderDerivativesPhase1(this.nordsieck);
                updateHighOrderDerivativesPhase2(this.scaled, predictedScaled, predictedNordsieck);
                error = errorEstimation(y2, predictedY, predictedScaled, predictedNordsieck);
                if (error >= 1.0d) {
                    double factor = computeStepGrowShrinkFactor(error);
                    hNew = filterStep(hNew * factor, forward, false);
                    interpolator.rescale(hNew);
                }
            }
            this.stepSize = hNew;
            double stepEnd2 = this.stepStart + this.stepSize;
            interpolator.reinitialize(stepEnd2, this.stepSize, predictedScaled, predictedNordsieck);
            interpolator.storeTime(stepEnd2);
            System.arraycopy(predictedY, 0, y2, 0, y2.length);
            this.stepStart = acceptStep(interpolator, y2, yDot, t2);
            this.scaled = predictedScaled;
            this.nordsieck = predictedNordsieck;
            interpolator.reinitialize(stepEnd2, this.stepSize, this.scaled, this.nordsieck);
            if (!this.isLastStep) {
                interpolator.storeTime(this.stepStart);
                if (this.resetOccurred) {
                    start(this.stepStart, y2, t2);
                    interpolator.reinitialize(this.stepStart, this.stepSize, this.scaled, this.nordsieck);
                }
                double factor2 = computeStepGrowShrinkFactor(error);
                double scaledH = this.stepSize * factor2;
                double nextT = this.stepStart + scaledH;
                boolean nextIsLast = forward ? nextT >= t2 : nextT <= t2;
                hNew = filterStep(scaledH, forward, nextIsLast);
                double filteredNextT = this.stepStart + hNew;
                boolean filteredNextIsLast = forward ? filteredNextT >= t2 : filteredNextT <= t2;
                if (filteredNextIsLast) {
                    hNew = t2 - this.stepStart;
                }
                interpolator.rescale(hNew);
            }
        } while (!this.isLastStep);
        equations.setTime(this.stepStart);
        equations.setCompleteState(y2);
        resetInternalState();
    }
}
