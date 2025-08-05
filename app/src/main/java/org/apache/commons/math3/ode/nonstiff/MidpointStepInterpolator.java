package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.sampling.StepInterpolator;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/MidpointStepInterpolator.class */
class MidpointStepInterpolator extends RungeKuttaStepInterpolator {
    private static final long serialVersionUID = 20111120;

    public MidpointStepInterpolator() {
    }

    MidpointStepInterpolator(MidpointStepInterpolator interpolator) {
        super(interpolator);
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected StepInterpolator doCopy() {
        return new MidpointStepInterpolator(this);
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
        double coeffDot2 = 2.0d * theta;
        double coeffDot1 = 1.0d - coeffDot2;
        if (this.previousState != null && theta <= 0.5d) {
            double coeff1 = theta * oneMinusThetaH;
            double coeff2 = theta * theta * this.f13067h;
            for (int i2 = 0; i2 < this.interpolatedState.length; i2++) {
                double yDot1 = this.yDotK[0][i2];
                double yDot2 = this.yDotK[1][i2];
                this.interpolatedState[i2] = this.previousState[i2] + (coeff1 * yDot1) + (coeff2 * yDot2);
                this.interpolatedDerivatives[i2] = (coeffDot1 * yDot1) + (coeffDot2 * yDot2);
            }
            return;
        }
        double coeff12 = oneMinusThetaH * theta;
        double coeff22 = oneMinusThetaH * (1.0d + theta);
        for (int i3 = 0; i3 < this.interpolatedState.length; i3++) {
            double yDot12 = this.yDotK[0][i3];
            double yDot22 = this.yDotK[1][i3];
            this.interpolatedState[i3] = (this.currentState[i3] + (coeff12 * yDot12)) - (coeff22 * yDot22);
            this.interpolatedDerivatives[i3] = (coeffDot1 * yDot12) + (coeffDot2 * yDot22);
        }
    }
}
