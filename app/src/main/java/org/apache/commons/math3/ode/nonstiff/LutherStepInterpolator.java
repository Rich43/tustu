package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/LutherStepInterpolator.class */
class LutherStepInterpolator extends RungeKuttaStepInterpolator {
    private static final long serialVersionUID = 20140416;

    /* renamed from: Q, reason: collision with root package name */
    private static final double f13060Q = FastMath.sqrt(21.0d);

    public LutherStepInterpolator() {
    }

    LutherStepInterpolator(LutherStepInterpolator interpolator) {
        super(interpolator);
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected StepInterpolator doCopy() {
        return new LutherStepInterpolator(this);
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
        double coeffDot1 = 1.0d + (theta * ((-10.8d) + (theta * (36.0d + (theta * ((-47.0d) + (theta * 21.0d)))))));
        double coeffDot3 = theta * ((-13.866666666666667d) + (theta * (106.66666666666667d + (theta * ((-202.66666666666666d) + (theta * 112.0d))))));
        double coeffDot4 = theta * (12.96d + (theta * ((-97.2d) + (theta * (194.4d + ((theta * (-567.0d)) / 5.0d))))));
        double coeffDot5 = theta * (((833.0d + (343.0d * f13060Q)) / 150.0d) + (theta * ((((-637.0d) - (357.0d * f13060Q)) / 30.0d) + (theta * (((392.0d + (287.0d * f13060Q)) / 15.0d) + ((theta * ((-49.0d) - (49.0d * f13060Q))) / 5.0d))))));
        double coeffDot6 = theta * (((833.0d - (343.0d * f13060Q)) / 150.0d) + (theta * ((((-637.0d) + (357.0d * f13060Q)) / 30.0d) + (theta * (((392.0d - (287.0d * f13060Q)) / 15.0d) + ((theta * ((-49.0d) + (49.0d * f13060Q))) / 5.0d))))));
        double coeffDot7 = theta * (0.6d + (theta * ((-3.0d) + (theta * 3.0d))));
        if (this.previousState != null && theta <= 0.5d) {
            double coeff1 = 1.0d + (theta * ((-5.4d) + (theta * (12.0d + (theta * ((-11.75d) + ((theta * 21.0d) / 5.0d)))))));
            double coeff3 = theta * ((-6.933333333333334d) + (theta * (35.55555555555556d + (theta * ((-50.666666666666664d) + ((theta * 112.0d) / 5.0d))))));
            double coeff4 = theta * (6.48d + (theta * ((-32.4d) + (theta * (48.6d + ((theta * (-567.0d)) / 25.0d))))));
            double coeff5 = theta * (((833.0d + (343.0d * f13060Q)) / 300.0d) + (theta * ((((-637.0d) - (357.0d * f13060Q)) / 90.0d) + (theta * (((392.0d + (287.0d * f13060Q)) / 60.0d) + ((theta * ((-49.0d) - (49.0d * f13060Q))) / 25.0d))))));
            double coeff6 = theta * (((833.0d - (343.0d * f13060Q)) / 300.0d) + (theta * ((((-637.0d) + (357.0d * f13060Q)) / 90.0d) + (theta * (((392.0d - (287.0d * f13060Q)) / 60.0d) + ((theta * ((-49.0d) + (49.0d * f13060Q))) / 25.0d))))));
            double coeff7 = theta * (0.3d + (theta * ((-1.0d) + (theta * 0.75d))));
            for (int i2 = 0; i2 < this.interpolatedState.length; i2++) {
                double yDot1 = this.yDotK[0][i2];
                double yDot2 = this.yDotK[1][i2];
                double yDot3 = this.yDotK[2][i2];
                double yDot4 = this.yDotK[3][i2];
                double yDot5 = this.yDotK[4][i2];
                double yDot6 = this.yDotK[5][i2];
                double yDot7 = this.yDotK[6][i2];
                this.interpolatedState[i2] = this.previousState[i2] + (theta * this.f13067h * ((coeff1 * yDot1) + (0.0d * yDot2) + (coeff3 * yDot3) + (coeff4 * yDot4) + (coeff5 * yDot5) + (coeff6 * yDot6) + (coeff7 * yDot7)));
                this.interpolatedDerivatives[i2] = (coeffDot1 * yDot1) + (0.0d * yDot2) + (coeffDot3 * yDot3) + (coeffDot4 * yDot4) + (coeffDot5 * yDot5) + (coeffDot6 * yDot6) + (coeffDot7 * yDot7);
            }
            return;
        }
        double coeff12 = (-0.05d) + (theta * (0.95d + (theta * ((-4.45d) + (theta * (7.55d + ((theta * (-21.0d)) / 5.0d)))))));
        double coeff32 = (-0.35555555555555557d) + (theta * ((-0.35555555555555557d) + (theta * ((-7.288888888888889d) + (theta * (28.266666666666666d + ((theta * (-112.0d)) / 5.0d)))))));
        double coeff42 = theta * theta * (6.48d + (theta * ((-25.92d) + ((theta * 567.0d) / 25.0d))));
        double coeff52 = (-0.2722222222222222d) + (theta * ((-0.2722222222222222d) + (theta * (((2254.0d + (1029.0d * f13060Q)) / 900.0d) + (theta * ((((-1372.0d) - (847.0d * f13060Q)) / 300.0d) + ((theta * (49.0d + (49.0d * f13060Q))) / 25.0d)))))));
        double coeff62 = (-0.2722222222222222d) + (theta * ((-0.2722222222222222d) + (theta * (((2254.0d - (1029.0d * f13060Q)) / 900.0d) + (theta * ((((-1372.0d) + (847.0d * f13060Q)) / 300.0d) + ((theta * (49.0d - (49.0d * f13060Q))) / 25.0d)))))));
        double coeff72 = (-0.05d) + (theta * ((-0.05d) + (theta * (0.25d + (theta * (-0.75d))))));
        for (int i3 = 0; i3 < this.interpolatedState.length; i3++) {
            double yDot12 = this.yDotK[0][i3];
            double yDot22 = this.yDotK[1][i3];
            double yDot32 = this.yDotK[2][i3];
            double yDot42 = this.yDotK[3][i3];
            double yDot52 = this.yDotK[4][i3];
            double yDot62 = this.yDotK[5][i3];
            double yDot72 = this.yDotK[6][i3];
            this.interpolatedState[i3] = this.currentState[i3] + (oneMinusThetaH * ((coeff12 * yDot12) + (0.0d * yDot22) + (coeff32 * yDot32) + (coeff42 * yDot42) + (coeff52 * yDot52) + (coeff62 * yDot62) + (coeff72 * yDot72)));
            this.interpolatedDerivatives[i3] = (coeffDot1 * yDot12) + (0.0d * yDot22) + (coeffDot3 * yDot32) + (coeffDot4 * yDot42) + (coeffDot5 * yDot52) + (coeffDot6 * yDot62) + (coeffDot7 * yDot72);
        }
    }
}
