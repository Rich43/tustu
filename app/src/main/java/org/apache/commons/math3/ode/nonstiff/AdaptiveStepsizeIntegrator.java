package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/AdaptiveStepsizeIntegrator.class */
public abstract class AdaptiveStepsizeIntegrator extends AbstractIntegrator {
    protected double scalAbsoluteTolerance;
    protected double scalRelativeTolerance;
    protected double[] vecAbsoluteTolerance;
    protected double[] vecRelativeTolerance;
    protected int mainSetDimension;
    private double initialStep;
    private double minStep;
    private double maxStep;

    @Override // org.apache.commons.math3.ode.AbstractIntegrator
    public abstract void integrate(ExpandableStatefulODE expandableStatefulODE, double d2) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException;

    public AdaptiveStepsizeIntegrator(String name, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(name);
        setStepSizeControl(minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        resetInternalState();
    }

    public AdaptiveStepsizeIntegrator(String name, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(name);
        setStepSizeControl(minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        resetInternalState();
    }

    public void setStepSizeControl(double minimalStep, double maximalStep, double absoluteTolerance, double relativeTolerance) {
        this.minStep = FastMath.abs(minimalStep);
        this.maxStep = FastMath.abs(maximalStep);
        this.initialStep = -1.0d;
        this.scalAbsoluteTolerance = absoluteTolerance;
        this.scalRelativeTolerance = relativeTolerance;
        this.vecAbsoluteTolerance = null;
        this.vecRelativeTolerance = null;
    }

    public void setStepSizeControl(double minimalStep, double maximalStep, double[] absoluteTolerance, double[] relativeTolerance) {
        this.minStep = FastMath.abs(minimalStep);
        this.maxStep = FastMath.abs(maximalStep);
        this.initialStep = -1.0d;
        this.scalAbsoluteTolerance = 0.0d;
        this.scalRelativeTolerance = 0.0d;
        this.vecAbsoluteTolerance = (double[]) absoluteTolerance.clone();
        this.vecRelativeTolerance = (double[]) relativeTolerance.clone();
    }

    public void setInitialStepSize(double initialStepSize) {
        if (initialStepSize < this.minStep || initialStepSize > this.maxStep) {
            this.initialStep = -1.0d;
        } else {
            this.initialStep = initialStepSize;
        }
    }

    @Override // org.apache.commons.math3.ode.AbstractIntegrator
    protected void sanityChecks(ExpandableStatefulODE equations, double t2) throws NumberIsTooSmallException, DimensionMismatchException {
        super.sanityChecks(equations, t2);
        this.mainSetDimension = equations.getPrimaryMapper().getDimension();
        if (this.vecAbsoluteTolerance != null && this.vecAbsoluteTolerance.length != this.mainSetDimension) {
            throw new DimensionMismatchException(this.mainSetDimension, this.vecAbsoluteTolerance.length);
        }
        if (this.vecRelativeTolerance != null && this.vecRelativeTolerance.length != this.mainSetDimension) {
            throw new DimensionMismatchException(this.mainSetDimension, this.vecRelativeTolerance.length);
        }
    }

    public double initializeStep(boolean forward, int order, double[] scale, double t0, double[] y0, double[] yDot0, double[] y1, double[] yDot1) throws MaxCountExceededException, DimensionMismatchException {
        if (this.initialStep > 0.0d) {
            return forward ? this.initialStep : -this.initialStep;
        }
        double yOnScale2 = 0.0d;
        double yDotOnScale2 = 0.0d;
        for (int j2 = 0; j2 < scale.length; j2++) {
            double ratio = y0[j2] / scale[j2];
            yOnScale2 += ratio * ratio;
            double ratio2 = yDot0[j2] / scale[j2];
            yDotOnScale2 += ratio2 * ratio2;
        }
        double h2 = (yOnScale2 < 1.0E-10d || yDotOnScale2 < 1.0E-10d) ? 1.0E-6d : 0.01d * FastMath.sqrt(yOnScale2 / yDotOnScale2);
        if (!forward) {
            h2 = -h2;
        }
        for (int j3 = 0; j3 < y0.length; j3++) {
            y1[j3] = y0[j3] + (h2 * yDot0[j3]);
        }
        computeDerivatives(t0 + h2, y1, yDot1);
        double yDDotOnScale = 0.0d;
        for (int j4 = 0; j4 < scale.length; j4++) {
            double ratio3 = (yDot1[j4] - yDot0[j4]) / scale[j4];
            yDDotOnScale += ratio3 * ratio3;
        }
        double maxInv2 = FastMath.max(FastMath.sqrt(yDotOnScale2), FastMath.sqrt(yDDotOnScale) / h2);
        double h1 = maxInv2 < 1.0E-15d ? FastMath.max(1.0E-6d, 0.001d * FastMath.abs(h2)) : FastMath.pow(0.01d / maxInv2, 1.0d / order);
        double h3 = FastMath.max(FastMath.min(100.0d * FastMath.abs(h2), h1), 1.0E-12d * FastMath.abs(t0));
        if (h3 < getMinStep()) {
            h3 = getMinStep();
        }
        if (h3 > getMaxStep()) {
            h3 = getMaxStep();
        }
        if (!forward) {
            h3 = -h3;
        }
        return h3;
    }

    protected double filterStep(double h2, boolean forward, boolean acceptSmall) throws NumberIsTooSmallException {
        double filteredH = h2;
        if (FastMath.abs(h2) < this.minStep) {
            if (acceptSmall) {
                filteredH = forward ? this.minStep : -this.minStep;
            } else {
                throw new NumberIsTooSmallException(LocalizedFormats.MINIMAL_STEPSIZE_REACHED_DURING_INTEGRATION, Double.valueOf(FastMath.abs(h2)), Double.valueOf(this.minStep), true);
            }
        }
        if (filteredH > this.maxStep) {
            filteredH = this.maxStep;
        } else if (filteredH < (-this.maxStep)) {
            filteredH = -this.maxStep;
        }
        return filteredH;
    }

    @Override // org.apache.commons.math3.ode.AbstractIntegrator, org.apache.commons.math3.ode.ODEIntegrator
    public double getCurrentStepStart() {
        return this.stepStart;
    }

    protected void resetInternalState() {
        this.stepStart = Double.NaN;
        this.stepSize = FastMath.sqrt(this.minStep * this.maxStep);
    }

    public double getMinStep() {
        return this.minStep;
    }

    public double getMaxStep() {
        return this.maxStep;
    }
}
