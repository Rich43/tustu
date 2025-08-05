package org.apache.commons.math3.ode;

import java.util.Collection;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.analysis.solvers.BracketedRealFieldUnivariateSolver;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.ode.events.FieldEventHandler;
import org.apache.commons.math3.ode.sampling.FieldStepHandler;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/FirstOrderFieldIntegrator.class */
public interface FirstOrderFieldIntegrator<T extends RealFieldElement<T>> {
    String getName();

    void addStepHandler(FieldStepHandler<T> fieldStepHandler);

    Collection<FieldStepHandler<T>> getStepHandlers();

    void clearStepHandlers();

    void addEventHandler(FieldEventHandler<T> fieldEventHandler, double d2, double d3, int i2);

    void addEventHandler(FieldEventHandler<T> fieldEventHandler, double d2, double d3, int i2, BracketedRealFieldUnivariateSolver<T> bracketedRealFieldUnivariateSolver);

    Collection<FieldEventHandler<T>> getEventHandlers();

    void clearEventHandlers();

    FieldODEStateAndDerivative<T> getCurrentStepStart();

    T getCurrentSignedStepsize();

    void setMaxEvaluations(int i2);

    int getMaxEvaluations();

    int getEvaluations();

    FieldODEStateAndDerivative<T> integrate(FieldExpandableODE<T> fieldExpandableODE, FieldODEState<T> fieldODEState, T t2) throws NumberIsTooSmallException, MaxCountExceededException, NoBracketingException;
}
