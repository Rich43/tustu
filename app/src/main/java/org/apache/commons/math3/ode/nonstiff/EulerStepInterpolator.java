package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.sampling.StepInterpolator;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/EulerStepInterpolator.class */
class EulerStepInterpolator extends RungeKuttaStepInterpolator {
    private static final long serialVersionUID = 20111120;

    public EulerStepInterpolator() {
    }

    EulerStepInterpolator(EulerStepInterpolator interpolator) {
        super(interpolator);
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected StepInterpolator doCopy() {
        return new EulerStepInterpolator(this);
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
        if (this.previousState != null && theta <= 0.5d) {
            for (int i2 = 0; i2 < this.interpolatedState.length; i2++) {
                this.interpolatedState[i2] = this.previousState[i2] + (theta * this.f13067h * this.yDotK[0][i2]);
            }
            System.arraycopy(this.yDotK[0], 0, this.interpolatedDerivatives, 0, this.interpolatedDerivatives.length);
            return;
        }
        for (int i3 = 0; i3 < this.interpolatedState.length; i3++) {
            this.interpolatedState[i3] = this.currentState[i3] - (oneMinusThetaH * this.yDotK[0][i3]);
        }
        System.arraycopy(this.yDotK[0], 0, this.interpolatedDerivatives, 0, this.interpolatedDerivatives.length);
    }
}
