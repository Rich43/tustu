package org.apache.commons.math3.ode;

import java.util.Collection;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepHandler;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/ODEIntegrator.class */
public interface ODEIntegrator {
    String getName();

    void addStepHandler(StepHandler stepHandler);

    Collection<StepHandler> getStepHandlers();

    void clearStepHandlers();

    void addEventHandler(EventHandler eventHandler, double d2, double d3, int i2);

    void addEventHandler(EventHandler eventHandler, double d2, double d3, int i2, UnivariateSolver univariateSolver);

    Collection<EventHandler> getEventHandlers();

    void clearEventHandlers();

    double getCurrentStepStart();

    double getCurrentSignedStepsize();

    void setMaxEvaluations(int i2);

    int getMaxEvaluations();

    int getEvaluations();
}
