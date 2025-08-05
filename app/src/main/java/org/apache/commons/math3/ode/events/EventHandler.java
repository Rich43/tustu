package org.apache.commons.math3.ode.events;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/events/EventHandler.class */
public interface EventHandler {

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/events/EventHandler$Action.class */
    public enum Action {
        STOP,
        RESET_STATE,
        RESET_DERIVATIVES,
        CONTINUE
    }

    void init(double d2, double[] dArr, double d3);

    double g(double d2, double[] dArr);

    Action eventOccurred(double d2, double[] dArr, boolean z2);

    void resetState(double d2, double[] dArr);
}
