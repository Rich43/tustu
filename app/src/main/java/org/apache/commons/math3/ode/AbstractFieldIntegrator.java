package org.apache.commons.math3.ode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.analysis.solvers.BracketedRealFieldUnivariateSolver;
import org.apache.commons.math3.analysis.solvers.FieldBracketingNthOrderBrentSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.ode.events.FieldEventHandler;
import org.apache.commons.math3.ode.events.FieldEventState;
import org.apache.commons.math3.ode.sampling.AbstractFieldStepInterpolator;
import org.apache.commons.math3.ode.sampling.FieldStepHandler;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.IntegerSequence;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/AbstractFieldIntegrator.class */
public abstract class AbstractFieldIntegrator<T extends RealFieldElement<T>> implements FirstOrderFieldIntegrator<T> {
    private static final double DEFAULT_RELATIVE_ACCURACY = 1.0E-14d;
    private static final double DEFAULT_FUNCTION_VALUE_ACCURACY = 1.0E-15d;
    private boolean isLastStep;
    private boolean resetOccurred;
    private final Field<T> field;
    private final String name;
    private transient FieldExpandableODE<T> equations;
    private Collection<FieldStepHandler<T>> stepHandlers = new ArrayList();
    private FieldODEStateAndDerivative<T> stepStart = null;
    private T stepSize = null;
    private Collection<FieldEventState<T>> eventsStates = new ArrayList();
    private boolean statesInitialized = false;
    private IntegerSequence.Incrementor evaluations = IntegerSequence.Incrementor.create().withMaximalCount(Integer.MAX_VALUE);

    protected AbstractFieldIntegrator(Field<T> field, String name) {
        this.field = field;
        this.name = name;
    }

    public Field<T> getField() {
        return this.field;
    }

    @Override // org.apache.commons.math3.ode.FirstOrderFieldIntegrator
    public String getName() {
        return this.name;
    }

    @Override // org.apache.commons.math3.ode.FirstOrderFieldIntegrator
    public void addStepHandler(FieldStepHandler<T> handler) {
        this.stepHandlers.add(handler);
    }

    @Override // org.apache.commons.math3.ode.FirstOrderFieldIntegrator
    public Collection<FieldStepHandler<T>> getStepHandlers() {
        return Collections.unmodifiableCollection(this.stepHandlers);
    }

    @Override // org.apache.commons.math3.ode.FirstOrderFieldIntegrator
    public void clearStepHandlers() {
        this.stepHandlers.clear();
    }

    @Override // org.apache.commons.math3.ode.FirstOrderFieldIntegrator
    public void addEventHandler(FieldEventHandler<T> handler, double maxCheckInterval, double convergence, int maxIterationCount) {
        addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount, new FieldBracketingNthOrderBrentSolver((RealFieldElement) this.field.getZero().add(DEFAULT_RELATIVE_ACCURACY), (RealFieldElement) this.field.getZero().add(convergence), (RealFieldElement) this.field.getZero().add(1.0E-15d), 5));
    }

    @Override // org.apache.commons.math3.ode.FirstOrderFieldIntegrator
    public void addEventHandler(FieldEventHandler<T> handler, double maxCheckInterval, double convergence, int maxIterationCount, BracketedRealFieldUnivariateSolver<T> solver) {
        this.eventsStates.add(new FieldEventState<>(handler, maxCheckInterval, (RealFieldElement) this.field.getZero().add(convergence), maxIterationCount, solver));
    }

    @Override // org.apache.commons.math3.ode.FirstOrderFieldIntegrator
    public Collection<FieldEventHandler<T>> getEventHandlers() {
        List<FieldEventHandler<T>> list = new ArrayList<>(this.eventsStates.size());
        for (FieldEventState<T> state : this.eventsStates) {
            list.add(state.getEventHandler());
        }
        return Collections.unmodifiableCollection(list);
    }

    @Override // org.apache.commons.math3.ode.FirstOrderFieldIntegrator
    public void clearEventHandlers() {
        this.eventsStates.clear();
    }

    @Override // org.apache.commons.math3.ode.FirstOrderFieldIntegrator
    public FieldODEStateAndDerivative<T> getCurrentStepStart() {
        return this.stepStart;
    }

    @Override // org.apache.commons.math3.ode.FirstOrderFieldIntegrator
    public T getCurrentSignedStepsize() {
        return this.stepSize;
    }

    @Override // org.apache.commons.math3.ode.FirstOrderFieldIntegrator
    public void setMaxEvaluations(int maxEvaluations) {
        this.evaluations = this.evaluations.withMaximalCount(maxEvaluations < 0 ? Integer.MAX_VALUE : maxEvaluations);
    }

    @Override // org.apache.commons.math3.ode.FirstOrderFieldIntegrator
    public int getMaxEvaluations() {
        return this.evaluations.getMaximalCount();
    }

    @Override // org.apache.commons.math3.ode.FirstOrderFieldIntegrator
    public int getEvaluations() {
        return this.evaluations.getCount();
    }

    protected FieldODEStateAndDerivative<T> initIntegration(FieldExpandableODE<T> eqn, T t0, T[] y0, T t2) throws MathIllegalArgumentException {
        this.equations = eqn;
        this.evaluations = this.evaluations.withStart(0);
        eqn.init(t0, y0, t2);
        FieldODEStateAndDerivative<T> state0 = new FieldODEStateAndDerivative<>(t0, y0, computeDerivatives(t0, y0));
        for (FieldEventState<T> state : this.eventsStates) {
            state.getEventHandler().init(state0, t2);
        }
        for (FieldStepHandler<T> handler : this.stepHandlers) {
            handler.init(state0, t2);
        }
        setStateInitialized(false);
        return state0;
    }

    protected FieldExpandableODE<T> getEquations() {
        return this.equations;
    }

    protected IntegerSequence.Incrementor getEvaluationsCounter() {
        return this.evaluations;
    }

    public T[] computeDerivatives(T t2, T[] tArr) throws MaxCountExceededException, DimensionMismatchException, NullPointerException {
        this.evaluations.increment();
        return (T[]) this.equations.computeDerivatives(t2, tArr);
    }

    protected void setStateInitialized(boolean stateInitialized) {
        this.statesInitialized = stateInitialized;
    }

    protected FieldODEStateAndDerivative<T> acceptStep(AbstractFieldStepInterpolator<T> interpolator, T tEnd) throws MaxCountExceededException, NullPointerException, MathIllegalArgumentException {
        FieldODEStateAndDerivative<T> previousState = interpolator.getGlobalPreviousState();
        FieldODEStateAndDerivative<T> currentState = interpolator.getGlobalCurrentState();
        if (!this.statesInitialized) {
            Iterator i$ = this.eventsStates.iterator();
            while (i$.hasNext()) {
                i$.next().reinitializeBegin(interpolator);
            }
            this.statesInitialized = true;
        }
        final int orderingSign = interpolator.isForward() ? 1 : -1;
        SortedSet<FieldEventState<T>> occurringEvents = new TreeSet<>(new Comparator<FieldEventState<T>>() { // from class: org.apache.commons.math3.ode.AbstractFieldIntegrator.1
            @Override // java.util.Comparator
            public int compare(FieldEventState<T> es0, FieldEventState<T> es1) {
                return orderingSign * Double.compare(es0.getEventTime().getReal(), es1.getEventTime().getReal());
            }
        });
        for (FieldEventState<T> state : this.eventsStates) {
            if (state.evaluateStep(interpolator)) {
                occurringEvents.add(state);
            }
        }
        AbstractFieldStepInterpolator<T> restricted = interpolator;
        while (!occurringEvents.isEmpty()) {
            Iterator<FieldEventState<T>> iterator = occurringEvents.iterator();
            FieldEventState<T> currentEvent = iterator.next();
            iterator.remove();
            FieldODEStateAndDerivative<T> eventState = restricted.getInterpolatedState(currentEvent.getEventTime());
            AbstractFieldStepInterpolator<T> restricted2 = restricted.restrictStep(previousState, eventState);
            for (FieldEventState<T> state2 : this.eventsStates) {
                state2.stepAccepted(eventState);
                this.isLastStep = this.isLastStep || state2.stop();
            }
            for (FieldStepHandler<T> handler : this.stepHandlers) {
                handler.handleStep(restricted2, this.isLastStep);
            }
            if (this.isLastStep) {
                return eventState;
            }
            this.resetOccurred = false;
            Iterator i$2 = this.eventsStates.iterator();
            while (i$2.hasNext()) {
                FieldODEState<T> newState = i$2.next().reset(eventState);
                if (newState != null) {
                    RealFieldElement[] realFieldElementArrMapState = this.equations.getMapper().mapState(newState);
                    RealFieldElement[] realFieldElementArrComputeDerivatives = computeDerivatives(newState.getTime(), realFieldElementArrMapState);
                    this.resetOccurred = true;
                    return this.equations.getMapper().mapStateAndDerivative(newState.getTime(), realFieldElementArrMapState, realFieldElementArrComputeDerivatives);
                }
            }
            previousState = eventState;
            restricted = restricted2.restrictStep(eventState, currentState);
            if (currentEvent.evaluateStep(restricted)) {
                occurringEvents.add(currentEvent);
            }
        }
        for (FieldEventState<T> state3 : this.eventsStates) {
            state3.stepAccepted(currentState);
            this.isLastStep = this.isLastStep || state3.stop();
        }
        this.isLastStep = this.isLastStep || ((RealFieldElement) ((RealFieldElement) currentState.getTime().subtract(tEnd)).abs()).getReal() <= FastMath.ulp(tEnd.getReal());
        for (FieldStepHandler<T> handler2 : this.stepHandlers) {
            handler2.handleStep(restricted, this.isLastStep);
        }
        return currentState;
    }

    protected void sanityChecks(FieldODEState<T> eqn, T t2) throws NumberIsTooSmallException, DimensionMismatchException {
        double threshold = 1000.0d * FastMath.ulp(FastMath.max(FastMath.abs(eqn.getTime().getReal()), FastMath.abs(t2.getReal())));
        double dt = ((RealFieldElement) ((RealFieldElement) eqn.getTime().subtract(t2)).abs()).getReal();
        if (dt <= threshold) {
            throw new NumberIsTooSmallException(LocalizedFormats.TOO_SMALL_INTEGRATION_INTERVAL, Double.valueOf(dt), Double.valueOf(threshold), false);
        }
    }

    protected boolean resetOccurred() {
        return this.resetOccurred;
    }

    protected void setStepSize(T stepSize) {
        this.stepSize = stepSize;
    }

    protected T getStepSize() {
        return this.stepSize;
    }

    protected void setStepStart(FieldODEStateAndDerivative<T> stepStart) {
        this.stepStart = stepStart;
    }

    protected FieldODEStateAndDerivative<T> getStepStart() {
        return this.stepStart;
    }

    protected void setIsLastStep(boolean isLastStep) {
        this.isLastStep = isLastStep;
    }

    protected boolean isLastStep() {
        return this.isLastStep;
    }
}
