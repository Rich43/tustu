package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.FirstOrderDifferentialEquations;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/RungeKuttaIntegrator.class */
public abstract class RungeKuttaIntegrator extends AbstractIntegrator {

    /* renamed from: c, reason: collision with root package name */
    private final double[] f13064c;

    /* renamed from: a, reason: collision with root package name */
    private final double[][] f13065a;

    /* renamed from: b, reason: collision with root package name */
    private final double[] f13066b;
    private final RungeKuttaStepInterpolator prototype;
    private final double step;

    protected RungeKuttaIntegrator(String name, double[] c2, double[][] a2, double[] b2, RungeKuttaStepInterpolator prototype, double step) {
        super(name);
        this.f13064c = c2;
        this.f13065a = a2;
        this.f13066b = b2;
        this.prototype = prototype;
        this.step = FastMath.abs(step);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v16, types: [double[], double[][]] */
    @Override // org.apache.commons.math3.ode.AbstractIntegrator
    public void integrate(ExpandableStatefulODE equations, double t2) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        sanityChecks(equations, t2);
        setEquations(equations);
        boolean forward = t2 > equations.getTime();
        double[] y0 = equations.getCompleteState();
        double[] y2 = (double[]) y0.clone();
        int stages = this.f13064c.length + 1;
        ?? r0 = new double[stages];
        for (int i2 = 0; i2 < stages; i2++) {
            r0[i2] = new double[y0.length];
        }
        double[] yTmp = (double[]) y0.clone();
        double[] yDotTmp = new double[y0.length];
        RungeKuttaStepInterpolator rungeKuttaStepInterpolator = (RungeKuttaStepInterpolator) this.prototype.copy();
        rungeKuttaStepInterpolator.reinitialize(this, yTmp, r0, forward, equations.getPrimaryMapper(), equations.getSecondaryMappers());
        rungeKuttaStepInterpolator.storeTime(equations.getTime());
        this.stepStart = equations.getTime();
        if (forward) {
            if (this.stepStart + this.step >= t2) {
                this.stepSize = t2 - this.stepStart;
            } else {
                this.stepSize = this.step;
            }
        } else if (this.stepStart - this.step <= t2) {
            this.stepSize = t2 - this.stepStart;
        } else {
            this.stepSize = -this.step;
        }
        initIntegration(equations.getTime(), y0, t2);
        this.isLastStep = false;
        do {
            rungeKuttaStepInterpolator.shift();
            computeDerivatives(this.stepStart, y2, r0[0]);
            for (int k2 = 1; k2 < stages; k2++) {
                for (int j2 = 0; j2 < y0.length; j2++) {
                    double sum = this.f13065a[k2 - 1][0] * r0[0][j2];
                    for (int l2 = 1; l2 < k2; l2++) {
                        sum += this.f13065a[k2 - 1][l2] * r0[l2][j2];
                    }
                    yTmp[j2] = y2[j2] + (this.stepSize * sum);
                }
                computeDerivatives(this.stepStart + (this.f13064c[k2 - 1] * this.stepSize), yTmp, r0[k2]);
            }
            for (int j3 = 0; j3 < y0.length; j3++) {
                double sum2 = this.f13066b[0] * r0[0][j3];
                for (int l3 = 1; l3 < stages; l3++) {
                    sum2 += this.f13066b[l3] * r0[l3][j3];
                }
                yTmp[j3] = y2[j3] + (this.stepSize * sum2);
            }
            rungeKuttaStepInterpolator.storeTime(this.stepStart + this.stepSize);
            System.arraycopy(yTmp, 0, y2, 0, y0.length);
            System.arraycopy(r0[stages - 1], 0, yDotTmp, 0, y0.length);
            this.stepStart = acceptStep(rungeKuttaStepInterpolator, y2, yDotTmp, t2);
            if (!this.isLastStep) {
                rungeKuttaStepInterpolator.storeTime(this.stepStart);
                double nextT = this.stepStart + this.stepSize;
                boolean nextIsLast = forward ? nextT >= t2 : nextT <= t2;
                if (nextIsLast) {
                    this.stepSize = t2 - this.stepStart;
                }
            }
        } while (!this.isLastStep);
        equations.setTime(this.stepStart);
        equations.setCompleteState(y2);
        this.stepStart = Double.NaN;
        this.stepSize = Double.NaN;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public double[] singleStep(FirstOrderDifferentialEquations firstOrderDifferentialEquations, double t0, double[] y0, double t2) throws MaxCountExceededException, DimensionMismatchException {
        double[] y2 = (double[]) y0.clone();
        int stages = this.f13064c.length + 1;
        double[] dArr = new double[stages];
        for (int i2 = 0; i2 < stages; i2++) {
            dArr[i2] = new double[y0.length];
        }
        double[] yTmp = (double[]) y0.clone();
        double h2 = t2 - t0;
        firstOrderDifferentialEquations.computeDerivatives(t0, y2, dArr[0]);
        for (int k2 = 1; k2 < stages; k2++) {
            for (int j2 = 0; j2 < y0.length; j2++) {
                double sum = this.f13065a[k2 - 1][0] * dArr[0][j2];
                for (int l2 = 1; l2 < k2; l2++) {
                    sum += this.f13065a[k2 - 1][l2] * dArr[l2][j2];
                }
                yTmp[j2] = y2[j2] + (h2 * sum);
            }
            firstOrderDifferentialEquations.computeDerivatives(t0 + (this.f13064c[k2 - 1] * h2), yTmp, dArr[k2]);
        }
        for (int j3 = 0; j3 < y0.length; j3++) {
            double sum2 = this.f13066b[0] * dArr[0][j3];
            for (int l3 = 1; l3 < stages; l3++) {
                sum2 += this.f13066b[l3] * dArr[l3][j3];
            }
            int i3 = j3;
            y2[i3] = y2[i3] + (h2 * sum2);
        }
        return y2;
    }
}
