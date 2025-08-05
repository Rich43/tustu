package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.sampling.StepInterpolator;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/HighamHall54StepInterpolator.class */
class HighamHall54StepInterpolator extends RungeKuttaStepInterpolator {
    private static final long serialVersionUID = 20111120;

    public HighamHall54StepInterpolator() {
    }

    HighamHall54StepInterpolator(HighamHall54StepInterpolator interpolator) {
        super(interpolator);
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected StepInterpolator doCopy() {
        return new HighamHall54StepInterpolator(this);
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
        double bDot0 = 1.0d + (theta * ((-7.5d) + (theta * (16.0d - (10.0d * theta)))));
        double bDot2 = theta * (28.6875d + (theta * ((-91.125d) + (67.5d * theta))));
        double bDot3 = theta * ((-44.0d) + (theta * (152.0d - (120.0d * theta))));
        double bDot4 = theta * (23.4375d + (theta * ((-78.125d) + (62.5d * theta))));
        double bDot5 = ((theta * 5.0d) / 8.0d) * ((2.0d * theta) - 1.0d);
        if (this.previousState != null && theta <= 0.5d) {
            double hTheta = this.f13067h * theta;
            double b0 = hTheta * (1.0d + (theta * ((-3.75d) + (theta * (5.333333333333333d - (2.5d * theta))))));
            double b2 = hTheta * theta * (14.34375d + (theta * ((-30.375d) + ((theta * 135.0d) / 8.0d))));
            double b3 = hTheta * theta * ((-22.0d) + (theta * (50.666666666666664d + (theta * (-30.0d)))));
            double b4 = hTheta * theta * (11.71875d + (theta * ((-26.041666666666668d) + ((theta * 125.0d) / 8.0d))));
            double b5 = hTheta * theta * ((-0.3125d) + ((theta * 5.0d) / 12.0d));
            for (int i2 = 0; i2 < this.interpolatedState.length; i2++) {
                double yDot0 = this.yDotK[0][i2];
                double yDot2 = this.yDotK[2][i2];
                double yDot3 = this.yDotK[3][i2];
                double yDot4 = this.yDotK[4][i2];
                double yDot5 = this.yDotK[5][i2];
                this.interpolatedState[i2] = this.previousState[i2] + (b0 * yDot0) + (b2 * yDot2) + (b3 * yDot3) + (b4 * yDot4) + (b5 * yDot5);
                this.interpolatedDerivatives[i2] = (bDot0 * yDot0) + (bDot2 * yDot2) + (bDot3 * yDot3) + (bDot4 * yDot4) + (bDot5 * yDot5);
            }
            return;
        }
        double theta2 = theta * theta;
        double b02 = this.f13067h * ((-0.08333333333333333d) + (theta * (1.0d + (theta * ((-3.75d) + (theta * (5.333333333333333d + ((theta * (-5.0d)) / 2.0d))))))));
        double b22 = this.f13067h * ((-0.84375d) + (theta2 * (14.34375d + (theta * ((-30.375d) + ((theta * 135.0d) / 8.0d))))));
        double b32 = this.f13067h * (1.3333333333333333d + (theta2 * ((-22.0d) + (theta * (50.666666666666664d + (theta * (-30.0d)))))));
        double b42 = this.f13067h * ((-1.3020833333333333d) + (theta2 * (11.71875d + (theta * ((-26.041666666666668d) + ((theta * 125.0d) / 8.0d))))));
        double b52 = this.f13067h * ((-0.10416666666666667d) + (theta2 * ((-0.3125d) + ((theta * 5.0d) / 12.0d))));
        for (int i3 = 0; i3 < this.interpolatedState.length; i3++) {
            double yDot02 = this.yDotK[0][i3];
            double yDot22 = this.yDotK[2][i3];
            double yDot32 = this.yDotK[3][i3];
            double yDot42 = this.yDotK[4][i3];
            double yDot52 = this.yDotK[5][i3];
            this.interpolatedState[i3] = this.currentState[i3] + (b02 * yDot02) + (b22 * yDot22) + (b32 * yDot32) + (b42 * yDot42) + (b52 * yDot52);
            this.interpolatedDerivatives[i3] = (bDot0 * yDot02) + (bDot2 * yDot22) + (bDot3 * yDot32) + (bDot4 * yDot42) + (bDot5 * yDot52);
        }
    }
}
