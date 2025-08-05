package org.apache.commons.math3.ode.sampling;

import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/sampling/FieldStepNormalizer.class */
public class FieldStepNormalizer<T extends RealFieldElement<T>> implements FieldStepHandler<T> {

    /* renamed from: h, reason: collision with root package name */
    private double f13068h;
    private final FieldFixedStepHandler<T> handler;
    private FieldODEStateAndDerivative<T> first;
    private FieldODEStateAndDerivative<T> last;
    private boolean forward;
    private final StepNormalizerBounds bounds;
    private final StepNormalizerMode mode;

    public FieldStepNormalizer(double h2, FieldFixedStepHandler<T> handler) {
        this(h2, handler, StepNormalizerMode.INCREMENT, StepNormalizerBounds.FIRST);
    }

    public FieldStepNormalizer(double h2, FieldFixedStepHandler<T> handler, StepNormalizerMode mode) {
        this(h2, handler, mode, StepNormalizerBounds.FIRST);
    }

    public FieldStepNormalizer(double h2, FieldFixedStepHandler<T> handler, StepNormalizerBounds bounds) {
        this(h2, handler, StepNormalizerMode.INCREMENT, bounds);
    }

    public FieldStepNormalizer(double h2, FieldFixedStepHandler<T> handler, StepNormalizerMode mode, StepNormalizerBounds bounds) {
        this.f13068h = FastMath.abs(h2);
        this.handler = handler;
        this.mode = mode;
        this.bounds = bounds;
        this.first = null;
        this.last = null;
        this.forward = true;
    }

    @Override // org.apache.commons.math3.ode.sampling.FieldStepHandler
    public void init(FieldODEStateAndDerivative<T> initialState, T finalTime) {
        this.first = null;
        this.last = null;
        this.forward = true;
        this.handler.init(initialState, finalTime);
    }

    /* JADX WARN: Incorrect condition in loop: B:18:0x00c6 */
    @Override // org.apache.commons.math3.ode.sampling.FieldStepHandler
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void handleStep(org.apache.commons.math3.ode.sampling.FieldStepInterpolator<T> r7, boolean r8) throws org.apache.commons.math3.exception.MaxCountExceededException {
        /*
            Method dump skipped, instructions count: 328
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.ode.sampling.FieldStepNormalizer.handleStep(org.apache.commons.math3.ode.sampling.FieldStepInterpolator, boolean):void");
    }

    private boolean isNextInStep(T nextTime, FieldStepInterpolator<T> interpolator) {
        return this.forward ? nextTime.getReal() <= interpolator.getCurrentState().getTime().getReal() : nextTime.getReal() >= interpolator.getCurrentState().getTime().getReal();
    }

    private void doNormalizedStep(boolean isLast) {
        if (!this.bounds.firstIncluded() && this.first.getTime().getReal() == this.last.getTime().getReal()) {
            return;
        }
        this.handler.handleStep(this.last, isLast);
    }
}
