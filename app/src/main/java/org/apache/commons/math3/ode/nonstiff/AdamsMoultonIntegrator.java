package org.apache.commons.math3.ode.nonstiff;

import java.util.Arrays;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.RealMatrixPreservingVisitor;
import org.apache.commons.math3.ode.EquationsMapper;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.sampling.NordsieckStepInterpolator;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/AdamsMoultonIntegrator.class */
public class AdamsMoultonIntegrator extends AdamsIntegrator {
    private static final String METHOD_NAME = "Adams-Moulton";

    public AdamsMoultonIntegrator(int nSteps, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) throws NumberIsTooSmallException {
        super(METHOD_NAME, nSteps, nSteps + 1, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
    }

    public AdamsMoultonIntegrator(int nSteps, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) throws IllegalArgumentException {
        super(METHOD_NAME, nSteps, nSteps + 1, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
    }

    @Override // org.apache.commons.math3.ode.nonstiff.AdamsIntegrator, org.apache.commons.math3.ode.nonstiff.AdaptiveStepsizeIntegrator, org.apache.commons.math3.ode.AbstractIntegrator
    public void integrate(ExpandableStatefulODE equations, double t2) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        sanityChecks(equations, t2);
        setEquations(equations);
        boolean forward = t2 > equations.getTime();
        double[] y0 = equations.getCompleteState();
        double[] y2 = (double[]) y0.clone();
        double[] yDot = new double[y2.length];
        double[] yTmp = new double[y2.length];
        double[] predictedScaled = new double[y2.length];
        Array2DRowRealMatrix nordsieckTmp = null;
        NordsieckStepInterpolator interpolator = new NordsieckStepInterpolator();
        interpolator.reinitialize(y2, forward, equations.getPrimaryMapper(), equations.getSecondaryMappers());
        initIntegration(equations.getTime(), y0, t2);
        start(equations.getTime(), y2, t2);
        interpolator.reinitialize(this.stepStart, this.stepSize, this.scaled, this.nordsieck);
        interpolator.storeTime(this.stepStart);
        double hNew = this.stepSize;
        interpolator.rescale(hNew);
        this.isLastStep = false;
        do {
            double error = 10.0d;
            while (error >= 1.0d) {
                this.stepSize = hNew;
                double stepEnd = this.stepStart + this.stepSize;
                interpolator.setInterpolatedTime(stepEnd);
                ExpandableStatefulODE expandable = getExpandable();
                EquationsMapper primary = expandable.getPrimaryMapper();
                primary.insertEquationData(interpolator.getInterpolatedState(), yTmp);
                int index = 0;
                EquationsMapper[] arr$ = expandable.getSecondaryMappers();
                for (EquationsMapper secondary : arr$) {
                    secondary.insertEquationData(interpolator.getInterpolatedSecondaryState(index), yTmp);
                    index++;
                }
                computeDerivatives(stepEnd, yTmp, yDot);
                for (int j2 = 0; j2 < y0.length; j2++) {
                    predictedScaled[j2] = this.stepSize * yDot[j2];
                }
                nordsieckTmp = updateHighOrderDerivativesPhase1(this.nordsieck);
                updateHighOrderDerivativesPhase2(this.scaled, predictedScaled, nordsieckTmp);
                error = nordsieckTmp.walkInOptimizedOrder(new Corrector(y2, predictedScaled, yTmp));
                if (error >= 1.0d) {
                    double factor = computeStepGrowShrinkFactor(error);
                    hNew = filterStep(this.stepSize * factor, forward, false);
                    interpolator.rescale(hNew);
                }
            }
            double stepEnd2 = this.stepStart + this.stepSize;
            computeDerivatives(stepEnd2, yTmp, yDot);
            double[] correctedScaled = new double[y0.length];
            for (int j3 = 0; j3 < y0.length; j3++) {
                correctedScaled[j3] = this.stepSize * yDot[j3];
            }
            updateHighOrderDerivativesPhase2(predictedScaled, correctedScaled, nordsieckTmp);
            System.arraycopy(yTmp, 0, y2, 0, y2.length);
            interpolator.reinitialize(stepEnd2, this.stepSize, correctedScaled, nordsieckTmp);
            interpolator.storeTime(this.stepStart);
            interpolator.shift();
            interpolator.storeTime(stepEnd2);
            this.stepStart = acceptStep(interpolator, y2, yDot, t2);
            this.scaled = correctedScaled;
            this.nordsieck = nordsieckTmp;
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

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/AdamsMoultonIntegrator$Corrector.class */
    private class Corrector implements RealMatrixPreservingVisitor {
        private final double[] previous;
        private final double[] scaled;
        private final double[] before;
        private final double[] after;

        Corrector(double[] previous, double[] scaled, double[] state) {
            this.previous = previous;
            this.scaled = scaled;
            this.after = state;
            this.before = (double[]) state.clone();
        }

        @Override // org.apache.commons.math3.linear.RealMatrixPreservingVisitor
        public void start(int rows, int columns, int startRow, int endRow, int startColumn, int endColumn) {
            Arrays.fill(this.after, 0.0d);
        }

        @Override // org.apache.commons.math3.linear.RealMatrixPreservingVisitor
        public void visit(int row, int column, double value) {
            if ((row & 1) == 0) {
                double[] dArr = this.after;
                dArr[column] = dArr[column] - value;
            } else {
                double[] dArr2 = this.after;
                dArr2[column] = dArr2[column] + value;
            }
        }

        @Override // org.apache.commons.math3.linear.RealMatrixPreservingVisitor
        public double end() {
            double error = 0.0d;
            for (int i2 = 0; i2 < this.after.length; i2++) {
                double[] dArr = this.after;
                int i3 = i2;
                dArr[i3] = dArr[i3] + this.previous[i2] + this.scaled[i2];
                if (i2 < AdamsMoultonIntegrator.this.mainSetDimension) {
                    double yScale = FastMath.max(FastMath.abs(this.previous[i2]), FastMath.abs(this.after[i2]));
                    double tol = AdamsMoultonIntegrator.this.vecAbsoluteTolerance == null ? AdamsMoultonIntegrator.this.scalAbsoluteTolerance + (AdamsMoultonIntegrator.this.scalRelativeTolerance * yScale) : AdamsMoultonIntegrator.this.vecAbsoluteTolerance[i2] + (AdamsMoultonIntegrator.this.vecRelativeTolerance[i2] * yScale);
                    double ratio = (this.after[i2] - this.before[i2]) / tol;
                    error += ratio * ratio;
                }
            }
            return FastMath.sqrt(error / AdamsMoultonIntegrator.this.mainSetDimension);
        }
    }
}
