package org.apache.commons.math3.ode.nonstiff;

import org.apache.commons.math3.ode.sampling.StepInterpolator;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/nonstiff/ClassicalRungeKuttaStepInterpolator.class */
class ClassicalRungeKuttaStepInterpolator extends RungeKuttaStepInterpolator {
    private static final long serialVersionUID = 20111120;

    public ClassicalRungeKuttaStepInterpolator() {
    }

    ClassicalRungeKuttaStepInterpolator(ClassicalRungeKuttaStepInterpolator interpolator) {
        super(interpolator);
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected StepInterpolator doCopy() {
        return new ClassicalRungeKuttaStepInterpolator(this);
    }

    @Override // org.apache.commons.math3.ode.sampling.AbstractStepInterpolator
    protected void computeInterpolatedStateAndDerivatives(double theta, double oneMinusThetaH) {
        double oneMinusTheta = 1.0d - theta;
        double oneMinus2Theta = 1.0d - (2.0d * theta);
        double coeffDot1 = oneMinusTheta * oneMinus2Theta;
        double coeffDot23 = 2.0d * theta * oneMinusTheta;
        double coeffDot4 = (-theta) * oneMinus2Theta;
        if (this.previousState != null && theta <= 0.5d) {
            double fourTheta2 = 4.0d * theta * theta;
            double s2 = (theta * this.f13067h) / 6.0d;
            double coeff1 = s2 * ((6.0d - (9.0d * theta)) + fourTheta2);
            double coeff23 = s2 * ((6.0d * theta) - fourTheta2);
            double coeff4 = s2 * (((-3.0d) * theta) + fourTheta2);
            for (int i2 = 0; i2 < this.interpolatedState.length; i2++) {
                double yDot1 = this.yDotK[0][i2];
                double yDot23 = this.yDotK[1][i2] + this.yDotK[2][i2];
                double yDot4 = this.yDotK[3][i2];
                this.interpolatedState[i2] = this.previousState[i2] + (coeff1 * yDot1) + (coeff23 * yDot23) + (coeff4 * yDot4);
                this.interpolatedDerivatives[i2] = (coeffDot1 * yDot1) + (coeffDot23 * yDot23) + (coeffDot4 * yDot4);
            }
            return;
        }
        double fourTheta = 4.0d * theta;
        double s3 = oneMinusThetaH / 6.0d;
        double coeff12 = s3 * ((((-fourTheta) + 5.0d) * theta) - 1.0d);
        double coeff232 = s3 * (((fourTheta - 2.0d) * theta) - 2.0d);
        double coeff42 = s3 * ((((-fourTheta) - 1.0d) * theta) - 1.0d);
        for (int i3 = 0; i3 < this.interpolatedState.length; i3++) {
            double yDot12 = this.yDotK[0][i3];
            double yDot232 = this.yDotK[1][i3] + this.yDotK[2][i3];
            double yDot42 = this.yDotK[3][i3];
            this.interpolatedState[i3] = this.currentState[i3] + (coeff12 * yDot12) + (coeff232 * yDot232) + (coeff42 * yDot42);
            this.interpolatedDerivatives[i3] = (coeffDot1 * yDot12) + (coeffDot23 * yDot232) + (coeffDot4 * yDot42);
        }
    }
}
