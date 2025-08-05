package org.apache.commons.math3.ode.sampling;

import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/sampling/StepNormalizer.class */
public class StepNormalizer implements StepHandler {

    /* renamed from: h, reason: collision with root package name */
    private double f13069h;
    private final FixedStepHandler handler;
    private double firstTime;
    private double lastTime;
    private double[] lastState;
    private double[] lastDerivatives;
    private boolean forward;
    private final StepNormalizerBounds bounds;
    private final StepNormalizerMode mode;

    public StepNormalizer(double h2, FixedStepHandler handler) {
        this(h2, handler, StepNormalizerMode.INCREMENT, StepNormalizerBounds.FIRST);
    }

    public StepNormalizer(double h2, FixedStepHandler handler, StepNormalizerMode mode) {
        this(h2, handler, mode, StepNormalizerBounds.FIRST);
    }

    public StepNormalizer(double h2, FixedStepHandler handler, StepNormalizerBounds bounds) {
        this(h2, handler, StepNormalizerMode.INCREMENT, bounds);
    }

    public StepNormalizer(double h2, FixedStepHandler handler, StepNormalizerMode mode, StepNormalizerBounds bounds) {
        this.f13069h = FastMath.abs(h2);
        this.handler = handler;
        this.mode = mode;
        this.bounds = bounds;
        this.firstTime = Double.NaN;
        this.lastTime = Double.NaN;
        this.lastState = null;
        this.lastDerivatives = null;
        this.forward = true;
    }

    @Override // org.apache.commons.math3.ode.sampling.StepHandler
    public void init(double t0, double[] y0, double t2) {
        this.firstTime = Double.NaN;
        this.lastTime = Double.NaN;
        this.lastState = null;
        this.lastDerivatives = null;
        this.forward = true;
        this.handler.init(t0, y0, t2);
    }

    /* JADX WARN: Incorrect condition in loop: B:22:0x00bd */
    @Override // org.apache.commons.math3.ode.sampling.StepHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void handleStep(org.apache.commons.math3.ode.sampling.StepInterpolator r7, boolean r8) throws org.apache.commons.math3.exception.MaxCountExceededException {
        /*
            Method dump skipped, instructions count: 292
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.ode.sampling.StepNormalizer.handleStep(org.apache.commons.math3.ode.sampling.StepInterpolator, boolean):void");
    }

    private boolean isNextInStep(double nextTime, StepInterpolator interpolator) {
        return this.forward ? nextTime <= interpolator.getCurrentTime() : nextTime >= interpolator.getCurrentTime();
    }

    private void doNormalizedStep(boolean isLast) {
        if (!this.bounds.firstIncluded() && this.firstTime == this.lastTime) {
            return;
        }
        this.handler.handleStep(this.lastTime, this.lastState, this.lastDerivatives, isLast);
    }

    private void storeStep(StepInterpolator interpolator, double t2) throws MaxCountExceededException {
        this.lastTime = t2;
        interpolator.setInterpolatedTime(this.lastTime);
        System.arraycopy(interpolator.getInterpolatedState(), 0, this.lastState, 0, this.lastState.length);
        System.arraycopy(interpolator.getInterpolatedDerivatives(), 0, this.lastDerivatives, 0, this.lastDerivatives.length);
    }
}
