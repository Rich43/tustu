package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.sampling.StepInterpolator;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/ThreeEighthesStepInterpolator.class */
class ThreeEighthesStepInterpolator extends RungeKuttaStepInterpolator {
    private static final long serialVersionUID = 20111120;

    public ThreeEighthesStepInterpolator() {
    }

    ThreeEighthesStepInterpolator(ThreeEighthesStepInterpolator interpolator) {
        super(interpolator);
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected StepInterpolator doCopy() {
        return new ThreeEighthesStepInterpolator(this);
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
        double coeffDot3 = 0.75d * theta;
        double coeffDot1 = (coeffDot3 * ((4.0d * theta) - 5.0d)) + 1.0d;
        double coeffDot2 = coeffDot3 * (5.0d - (6.0d * theta));
        double coeffDot4 = coeffDot3 * ((2.0d * theta) - 1.0d);
        if (this.previousState != null && theta <= 0.5d) {
            double s2 = (theta * this.f13067h) / 8.0d;
            double fourTheta2 = 4.0d * theta * theta;
            double coeff1 = s2 * ((8.0d - (15.0d * theta)) + (2.0d * fourTheta2));
            double coeff2 = 3.0d * s2 * ((5.0d * theta) - fourTheta2);
            double coeff3 = 3.0d * s2 * theta;
            double coeff4 = s2 * (((-3.0d) * theta) + fourTheta2);
            for (int i2 = 0; i2 < this.interpolatedState.length; i2++) {
                double yDot1 = this.yDotK[0][i2];
                double yDot2 = this.yDotK[1][i2];
                double yDot3 = this.yDotK[2][i2];
                double yDot4 = this.yDotK[3][i2];
                this.interpolatedState[i2] = this.previousState[i2] + (coeff1 * yDot1) + (coeff2 * yDot2) + (coeff3 * yDot3) + (coeff4 * yDot4);
                this.interpolatedDerivatives[i2] = (coeffDot1 * yDot1) + (coeffDot2 * yDot2) + (coeffDot3 * yDot3) + (coeffDot4 * yDot4);
            }
            return;
        }
        double s3 = oneMinusThetaH / 8.0d;
        double fourTheta22 = 4.0d * theta * theta;
        double coeff12 = s3 * ((1.0d - (7.0d * theta)) + (2.0d * fourTheta22));
        double coeff22 = 3.0d * s3 * ((1.0d + theta) - fourTheta22);
        double coeff32 = 3.0d * s3 * (1.0d + theta);
        double coeff42 = s3 * (1.0d + theta + fourTheta22);
        for (int i3 = 0; i3 < this.interpolatedState.length; i3++) {
            double yDot12 = this.yDotK[0][i3];
            double yDot22 = this.yDotK[1][i3];
            double yDot32 = this.yDotK[2][i3];
            double yDot42 = this.yDotK[3][i3];
            this.interpolatedState[i3] = (((this.currentState[i3] - (coeff12 * yDot12)) - (coeff22 * yDot22)) - (coeff32 * yDot32)) - (coeff42 * yDot42);
            this.interpolatedDerivatives[i3] = (coeffDot1 * yDot12) + (coeffDot2 * yDot22) + (coeffDot3 * yDot32) + (coeffDot4 * yDot42);
        }
    }
}
