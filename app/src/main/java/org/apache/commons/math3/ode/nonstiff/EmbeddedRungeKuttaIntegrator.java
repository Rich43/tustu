package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/EmbeddedRungeKuttaIntegrator.class */
public abstract class EmbeddedRungeKuttaIntegrator extends AdaptiveStepsizeIntegrator {
    private final boolean fsal;

    /* renamed from: c, reason: collision with root package name */
    private final double[] f13055c;

    /* renamed from: a, reason: collision with root package name */
    private final double[][] f13056a;

    /* renamed from: b, reason: collision with root package name */
    private final double[] f13057b;
    private final RungeKuttaStepInterpolator prototype;
    private final double exp;
    private double safety;
    private double minReduction;
    private double maxGrowth;

    public abstract int getOrder();

    protected abstract double estimateError(double[][] dArr, double[] dArr2, double[] dArr3, double d2);

    protected EmbeddedRungeKuttaIntegrator(String name, boolean fsal, double[] c2, double[][] a2, double[] b2, RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double scalAbsoluteTolerance, double scalRelativeTolerance) {
        super(name, minStep, maxStep, scalAbsoluteTolerance, scalRelativeTolerance);
        this.fsal = fsal;
        this.f13055c = c2;
        this.f13056a = a2;
        this.f13057b = b2;
        this.prototype = prototype;
        this.exp = (-1.0d) / getOrder();
        setSafety(0.9d);
        setMinReduction(0.2d);
        setMaxGrowth(10.0d);
    }

    protected EmbeddedRungeKuttaIntegrator(String name, boolean fsal, double[] c2, double[][] a2, double[] b2, RungeKuttaStepInterpolator prototype, double minStep, double maxStep, double[] vecAbsoluteTolerance, double[] vecRelativeTolerance) {
        super(name, minStep, maxStep, vecAbsoluteTolerance, vecRelativeTolerance);
        this.fsal = fsal;
        this.f13055c = c2;
        this.f13056a = a2;
        this.f13057b = b2;
        this.prototype = prototype;
        this.exp = (-1.0d) / getOrder();
        setSafety(0.9d);
        setMinReduction(0.2d);
        setMaxGrowth(10.0d);
    }

    public double getSafety() {
        return this.safety;
    }

    public void setSafety(double safety) {
        this.safety = safety;
    }

    @Override // org.apache.commons.math3.ode.nonstiff.AdaptiveStepsizeIntegrator, org.apache.commons.math3.ode.AbstractIntegrator
    public void integrate(ExpandableStatefulODE equations, double t2) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        sanityChecks(equations, t2);
        setEquations(equations);
        boolean forward = t2 > equations.getTime();
        double[] y0 = equations.getCompleteState();
        double[] y2 = (double[]) y0.clone();
        int stages = this.f13055c.length + 1;
        double[][] yDotK = new double[stages][y2.length];
        double[] yTmp = (double[]) y0.clone();
        double[] yDotTmp = new double[y2.length];
        RungeKuttaStepInterpolator interpolator = (RungeKuttaStepInterpolator) this.prototype.copy();
        interpolator.reinitialize(this, yTmp, yDotK, forward, equations.getPrimaryMapper(), equations.getSecondaryMappers());
        interpolator.storeTime(equations.getTime());
        this.stepStart = equations.getTime();
        double hNew = 0.0d;
        boolean firstTime = true;
        initIntegration(equations.getTime(), y0, t2);
        this.isLastStep = false;
        do {
            interpolator.shift();
            double error = 10.0d;
            while (error >= 1.0d) {
                if (firstTime || !this.fsal) {
                    computeDerivatives(this.stepStart, y2, yDotK[0]);
                }
                if (firstTime) {
                    double[] scale = new double[this.mainSetDimension];
                    if (this.vecAbsoluteTolerance == null) {
                        for (int i2 = 0; i2 < scale.length; i2++) {
                            scale[i2] = this.scalAbsoluteTolerance + (this.scalRelativeTolerance * FastMath.abs(y2[i2]));
                        }
                    } else {
                        for (int i3 = 0; i3 < scale.length; i3++) {
                            scale[i3] = this.vecAbsoluteTolerance[i3] + (this.vecRelativeTolerance[i3] * FastMath.abs(y2[i3]));
                        }
                    }
                    hNew = initializeStep(forward, getOrder(), scale, this.stepStart, y2, yDotK[0], yTmp, yDotK[1]);
                    firstTime = false;
                }
                this.stepSize = hNew;
                if (forward) {
                    if (this.stepStart + this.stepSize >= t2) {
                        this.stepSize = t2 - this.stepStart;
                    }
                } else if (this.stepStart + this.stepSize <= t2) {
                    this.stepSize = t2 - this.stepStart;
                }
                for (int k2 = 1; k2 < stages; k2++) {
                    for (int j2 = 0; j2 < y0.length; j2++) {
                        double sum = this.f13056a[k2 - 1][0] * yDotK[0][j2];
                        for (int l2 = 1; l2 < k2; l2++) {
                            sum += this.f13056a[k2 - 1][l2] * yDotK[l2][j2];
                        }
                        yTmp[j2] = y2[j2] + (this.stepSize * sum);
                    }
                    computeDerivatives(this.stepStart + (this.f13055c[k2 - 1] * this.stepSize), yTmp, yDotK[k2]);
                }
                for (int j3 = 0; j3 < y0.length; j3++) {
                    double sum2 = this.f13057b[0] * yDotK[0][j3];
                    for (int l3 = 1; l3 < stages; l3++) {
                        sum2 += this.f13057b[l3] * yDotK[l3][j3];
                    }
                    yTmp[j3] = y2[j3] + (this.stepSize * sum2);
                }
                error = estimateError(yDotK, y2, yTmp, this.stepSize);
                if (error >= 1.0d) {
                    double factor = FastMath.min(this.maxGrowth, FastMath.max(this.minReduction, this.safety * FastMath.pow(error, this.exp)));
                    hNew = filterStep(this.stepSize * factor, forward, false);
                }
            }
            interpolator.storeTime(this.stepStart + this.stepSize);
            System.arraycopy(yTmp, 0, y2, 0, y0.length);
            System.arraycopy(yDotK[stages - 1], 0, yDotTmp, 0, y0.length);
            this.stepStart = acceptStep(interpolator, y2, yDotTmp, t2);
            System.arraycopy(y2, 0, yTmp, 0, y2.length);
            if (!this.isLastStep) {
                interpolator.storeTime(this.stepStart);
                if (this.fsal) {
                    System.arraycopy(yDotTmp, 0, yDotK[0], 0, y0.length);
                }
                double factor2 = FastMath.min(this.maxGrowth, FastMath.max(this.minReduction, this.safety * FastMath.pow(error, this.exp)));
                double scaledH = this.stepSize * factor2;
                double nextT = this.stepStart + scaledH;
                boolean nextIsLast = forward ? nextT >= t2 : nextT <= t2;
                hNew = filterStep(scaledH, forward, nextIsLast);
                double filteredNextT = this.stepStart + hNew;
                boolean filteredNextIsLast = forward ? filteredNextT >= t2 : filteredNextT <= t2;
                if (filteredNextIsLast) {
                    hNew = t2 - this.stepStart;
                }
            }
        } while (!this.isLastStep);
        equations.setTime(this.stepStart);
        equations.setCompleteState(y2);
        resetInternalState();
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
}
