package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.AbstractIntegrator;
import org.apache.commons.math3.ode.EquationsMapper;
import org.apache.commons.math3.ode.sampling.StepInterpolator;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/DormandPrince54StepInterpolator.class */
class DormandPrince54StepInterpolator extends RungeKuttaStepInterpolator {
    private static final double A70 = 0.09114583333333333d;
    private static final double A72 = 0.44923629829290207d;
    private static final double A73 = 0.6510416666666666d;
    private static final double A74 = -0.322376179245283d;
    private static final double A75 = 0.13095238095238096d;
    private static final double D0 = -1.1270175653862835d;
    private static final double D2 = 2.675424484351598d;
    private static final double D3 = -5.685526961588504d;
    private static final double D4 = 3.5219323679207912d;
    private static final double D5 = -1.7672812570757455d;
    private static final double D6 = 2.382468931778144d;
    private static final long serialVersionUID = 20111120;
    private double[] v1;
    private double[] v2;
    private double[] v3;
    private double[] v4;
    private boolean vectorsInitialized;

    public DormandPrince54StepInterpolator() {
        this.v1 = null;
        this.v2 = null;
        this.v3 = null;
        this.v4 = null;
        this.vectorsInitialized = false;
    }

    DormandPrince54StepInterpolator(DormandPrince54StepInterpolator interpolator) {
        super(interpolator);
        if (interpolator.v1 == null) {
            this.v1 = null;
            this.v2 = null;
            this.v3 = null;
            this.v4 = null;
            this.vectorsInitialized = false;
            return;
        }
        this.v1 = (double[]) interpolator.v1.clone();
        this.v2 = (double[]) interpolator.v2.clone();
        this.v3 = (double[]) interpolator.v3.clone();
        this.v4 = (double[]) interpolator.v4.clone();
        this.vectorsInitialized = interpolator.vectorsInitialized;
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected StepInterpolator doCopy() {
        return new DormandPrince54StepInterpolator(this);
    }

    @Override // org.apache.commons.math3.ode.nonstiff.RungeKuttaStepInterpolator
    public void reinitialize(AbstractIntegrator integrator, double[] y2, double[][] yDotK, boolean forward, EquationsMapper primaryMapper, EquationsMapper[] secondaryMappers) {
        super.reinitialize(integrator, y2, yDotK, forward, primaryMapper, secondaryMappers);
        this.v1 = null;
        this.v2 = null;
        this.v3 = null;
        this.v4 = null;
        this.vectorsInitialized = false;
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    public void storeTime(double t2) {
        super.storeTime(t2);
        this.vectorsInitialized = false;
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
        if (!this.vectorsInitialized) {
            if (this.v1 == null) {
                this.v1 = new double[this.interpolatedState.length];
                this.v2 = new double[this.interpolatedState.length];
                this.v3 = new double[this.interpolatedState.length];
                this.v4 = new double[this.interpolatedState.length];
            }
            for (int i2 = 0; i2 < this.interpolatedState.length; i2++) {
                double yDot0 = this.yDotK[0][i2];
                double yDot2 = this.yDotK[2][i2];
                double yDot3 = this.yDotK[3][i2];
                double yDot4 = this.yDotK[4][i2];
                double yDot5 = this.yDotK[5][i2];
                double yDot6 = this.yDotK[6][i2];
                this.v1[i2] = (A70 * yDot0) + (A72 * yDot2) + (A73 * yDot3) + (A74 * yDot4) + (A75 * yDot5);
                this.v2[i2] = yDot0 - this.v1[i2];
                this.v3[i2] = (this.v1[i2] - this.v2[i2]) - yDot6;
                this.v4[i2] = (D0 * yDot0) + (D2 * yDot2) + (D3 * yDot3) + (D4 * yDot4) + (D5 * yDot5) + (D6 * yDot6);
            }
            this.vectorsInitialized = true;
        }
        double eta = 1.0d - theta;
        double twoTheta = 2.0d * theta;
        double dot2 = 1.0d - twoTheta;
        double dot3 = theta * (2.0d - (3.0d * theta));
        double dot4 = twoTheta * (1.0d + (theta * (twoTheta - 3.0d)));
        if (this.previousState != null && theta <= 0.5d) {
            for (int i3 = 0; i3 < this.interpolatedState.length; i3++) {
                this.interpolatedState[i3] = this.previousState[i3] + (theta * this.f13067h * (this.v1[i3] + (eta * (this.v2[i3] + (theta * (this.v3[i3] + (eta * this.v4[i3])))))));
                this.interpolatedDerivatives[i3] = this.v1[i3] + (dot2 * this.v2[i3]) + (dot3 * this.v3[i3]) + (dot4 * this.v4[i3]);
            }
            return;
        }
        for (int i4 = 0; i4 < this.interpolatedState.length; i4++) {
            this.interpolatedState[i4] = this.currentState[i4] - (oneMinusThetaH * (this.v1[i4] - (theta * (this.v2[i4] + (theta * (this.v3[i4] + (eta * this.v4[i4])))))));
            this.interpolatedDerivatives[i4] = this.v1[i4] + (dot2 * this.v2[i4]) + (dot3 * this.v3[i4]) + (dot4 * this.v4[i4]);
        }
    }
}
