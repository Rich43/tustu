package org.apache.commons.math3.ode.sampling;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/sampling/DummyStepHandler.class */
public class DummyStepHandler implements StepHandler {
    private DummyStepHandler() {
    }

    public static DummyStepHandler getInstance() {
        return LazyHolder.INSTANCE;
    }

    @Override // org.apache.commons.math3.ode.sampling.StepHandler
    public void init(double t0, double[] y0, double t2) {
    }

    @Override // org.apache.commons.math3.ode.sampling.StepHandler
    public void handleStep(StepInterpolator interpolator, boolean isLast) {
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/sampling/DummyStepHandler$LazyHolder.class */
    private static class LazyHolder {
        private static final DummyStepHandler INSTANCE = new DummyStepHandler();

        private LazyHolder() {
        }
    }

    private Object readResolve() {
        return LazyHolder.INSTANCE;
    }
}
