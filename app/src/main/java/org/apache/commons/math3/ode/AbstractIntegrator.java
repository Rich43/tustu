package org.apache.commons.math3.ode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import org.apache.commons.math3.analysis.solvers.BracketingNthOrderBrentSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.events.EventState;
import org.apache.commons.math3.ode.sampling.AbstractStepInterpolator;
import org.apache.commons.math3.ode.sampling.StepHandler;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Incrementor;
import org.apache.commons.math3.util.IntegerSequence;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/AbstractIntegrator.class */
public abstract class AbstractIntegrator implements FirstOrderIntegrator {
    protected Collection<StepHandler> stepHandlers;
    protected double stepStart;
    protected double stepSize;
    protected boolean isLastStep;
    protected boolean resetOccurred;
    private Collection<EventState> eventsStates;
    private boolean statesInitialized;
    private final String name;
    private IntegerSequence.Incrementor evaluations;
    private transient ExpandableStatefulODE expandable;

    public abstract void integrate(ExpandableStatefulODE expandableStatefulODE, double d2) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException;

    public AbstractIntegrator(String name) {
        this.name = name;
        this.stepHandlers = new ArrayList();
        this.stepStart = Double.NaN;
        this.stepSize = Double.NaN;
        this.eventsStates = new ArrayList();
        this.statesInitialized = false;
        this.evaluations = IntegerSequence.Incrementor.create().withMaximalCount(Integer.MAX_VALUE);
    }

    protected AbstractIntegrator() {
        this(null);
    }

    @Override // org.apache.commons.math3.ode.ODEIntegrator
    public String getName() {
        return this.name;
    }

    @Override // org.apache.commons.math3.ode.ODEIntegrator
    public void addStepHandler(StepHandler handler) {
        this.stepHandlers.add(handler);
    }

    @Override // org.apache.commons.math3.ode.ODEIntegrator
    public Collection<StepHandler> getStepHandlers() {
        return Collections.unmodifiableCollection(this.stepHandlers);
    }

    @Override // org.apache.commons.math3.ode.ODEIntegrator
    public void clearStepHandlers() {
        this.stepHandlers.clear();
    }

    @Override // org.apache.commons.math3.ode.ODEIntegrator
    public void addEventHandler(EventHandler handler, double maxCheckInterval, double convergence, int maxIterationCount) {
        addEventHandler(handler, maxCheckInterval, convergence, maxIterationCount, new BracketingNthOrderBrentSolver(convergence, 5));
    }

    @Override // org.apache.commons.math3.ode.ODEIntegrator
    public void addEventHandler(EventHandler handler, double maxCheckInterval, double convergence, int maxIterationCount, UnivariateSolver solver) {
        this.eventsStates.add(new EventState(handler, maxCheckInterval, convergence, maxIterationCount, solver));
    }

    @Override // org.apache.commons.math3.ode.ODEIntegrator
    public Collection<EventHandler> getEventHandlers() {
        List<EventHandler> list = new ArrayList<>(this.eventsStates.size());
        for (EventState state : this.eventsStates) {
            list.add(state.getEventHandler());
        }
        return Collections.unmodifiableCollection(list);
    }

    @Override // org.apache.commons.math3.ode.ODEIntegrator
    public void clearEventHandlers() {
        this.eventsStates.clear();
    }

    @Override // org.apache.commons.math3.ode.ODEIntegrator
    public double getCurrentStepStart() {
        return this.stepStart;
    }

    @Override // org.apache.commons.math3.ode.ODEIntegrator
    public double getCurrentSignedStepsize() {
        return this.stepSize;
    }

    @Override // org.apache.commons.math3.ode.ODEIntegrator
    public void setMaxEvaluations(int maxEvaluations) {
        this.evaluations = this.evaluations.withMaximalCount(maxEvaluations < 0 ? Integer.MAX_VALUE : maxEvaluations);
    }

    @Override // org.apache.commons.math3.ode.ODEIntegrator
    public int getMaxEvaluations() {
        return this.evaluations.getMaximalCount();
    }

    @Override // org.apache.commons.math3.ode.ODEIntegrator
    public int getEvaluations() {
        return this.evaluations.getCount();
    }

    protected void initIntegration(double t0, double[] y0, double t2) {
        this.evaluations = this.evaluations.withStart(0);
        for (EventState state : this.eventsStates) {
            state.setExpandable(this.expandable);
            state.getEventHandler().init(t0, y0, t2);
        }
        for (StepHandler handler : this.stepHandlers) {
            handler.init(t0, y0, t2);
        }
        setStateInitialized(false);
    }

    protected void setEquations(ExpandableStatefulODE equations) {
        this.expandable = equations;
    }

    protected ExpandableStatefulODE getExpandable() {
        return this.expandable;
    }

    @Deprecated
    protected Incrementor getEvaluationsCounter() {
        return Incrementor.wrap(this.evaluations);
    }

    protected IntegerSequence.Incrementor getCounter() {
        return this.evaluations;
    }

    @Override // org.apache.commons.math3.ode.FirstOrderIntegrator
    public double integrate(FirstOrderDifferentialEquations equations, double t0, double[] y0, double t2, double[] y2) throws NumberIsTooSmallException, DimensionMismatchException, MaxCountExceededException, NoBracketingException {
        if (y0.length != equations.getDimension()) {
            throw new DimensionMismatchException(y0.length, equations.getDimension());
        }
        if (y2.length != equations.getDimension()) {
            throw new DimensionMismatchException(y2.length, equations.getDimension());
        }
        ExpandableStatefulODE expandableODE = new ExpandableStatefulODE(equations);
        expandableODE.setTime(t0);
        expandableODE.setPrimaryState(y0);
        integrate(expandableODE, t2);
        System.arraycopy(expandableODE.getPrimaryState(), 0, y2, 0, y2.length);
        return expandableODE.getTime();
    }

    public void computeDerivatives(double t2, double[] y2, double[] yDot) throws MaxCountExceededException, DimensionMismatchException, NullPointerException {
        this.evaluations.increment();
        this.expandable.computeDerivatives(t2, y2, yDot);
    }

    protected void setStateInitialized(boolean stateInitialized) {
        this.statesInitialized = stateInitialized;
    }

    protected double acceptStep(AbstractStepInterpolator interpolator, double[] y2, double[] yDot, double tEnd) throws MaxCountExceededException, DimensionMismatchException, NullPointerException, NoBracketingException {
        double previousT = interpolator.getGlobalPreviousTime();
        double currentT = interpolator.getGlobalCurrentTime();
        if (!this.statesInitialized) {
            Iterator i$ = this.eventsStates.iterator();
            while (i$.hasNext()) {
                i$.next().reinitializeBegin(interpolator);
            }
            this.statesInitialized = true;
        }
        final int orderingSign = interpolator.isForward() ? 1 : -1;
        SortedSet<EventState> occurringEvents = new TreeSet<>(new Comparator<EventState>() { // from class: org.apache.commons.math3.ode.AbstractIntegrator.1
            @Override // java.util.Comparator
            public int compare(EventState es0, EventState es1) {
                return orderingSign * Double.compare(es0.getEventTime(), es1.getEventTime());
            }
        });
        for (EventState state : this.eventsStates) {
            if (state.evaluateStep(interpolator)) {
                occurringEvents.add(state);
            }
        }
        while (!occurringEvents.isEmpty()) {
            Iterator<EventState> iterator = occurringEvents.iterator();
            EventState currentEvent = iterator.next();
            iterator.remove();
            double eventT = currentEvent.getEventTime();
            interpolator.setSoftPreviousTime(previousT);
            interpolator.setSoftCurrentTime(eventT);
            interpolator.setInterpolatedTime(eventT);
            double[] eventYComplete = new double[y2.length];
            this.expandable.getPrimaryMapper().insertEquationData(interpolator.getInterpolatedState(), eventYComplete);
            int index = 0;
            EquationsMapper[] arr$ = this.expandable.getSecondaryMappers();
            for (EquationsMapper secondary : arr$) {
                int i2 = index;
                index++;
                secondary.insertEquationData(interpolator.getInterpolatedSecondaryState(i2), eventYComplete);
            }
            for (EventState state2 : this.eventsStates) {
                state2.stepAccepted(eventT, eventYComplete);
                this.isLastStep = this.isLastStep || state2.stop();
            }
            for (StepHandler handler : this.stepHandlers) {
                handler.handleStep(interpolator, this.isLastStep);
            }
            if (this.isLastStep) {
                System.arraycopy(eventYComplete, 0, y2, 0, y2.length);
                return eventT;
            }
            this.resetOccurred = false;
            boolean needReset = currentEvent.reset(eventT, eventYComplete);
            if (needReset) {
                interpolator.setInterpolatedTime(eventT);
                System.arraycopy(eventYComplete, 0, y2, 0, y2.length);
                computeDerivatives(eventT, y2, yDot);
                this.resetOccurred = true;
                return eventT;
            }
            previousT = eventT;
            interpolator.setSoftPreviousTime(eventT);
            interpolator.setSoftCurrentTime(currentT);
            if (currentEvent.evaluateStep(interpolator)) {
                occurringEvents.add(currentEvent);
            }
        }
        interpolator.setInterpolatedTime(currentT);
        double[] currentY = new double[y2.length];
        this.expandable.getPrimaryMapper().insertEquationData(interpolator.getInterpolatedState(), currentY);
        int index2 = 0;
        EquationsMapper[] arr$2 = this.expandable.getSecondaryMappers();
        for (EquationsMapper secondary2 : arr$2) {
            int i3 = index2;
            index2++;
            secondary2.insertEquationData(interpolator.getInterpolatedSecondaryState(i3), currentY);
        }
        for (EventState state3 : this.eventsStates) {
            state3.stepAccepted(currentT, currentY);
            this.isLastStep = this.isLastStep || state3.stop();
        }
        this.isLastStep = this.isLastStep || Precision.equals(currentT, tEnd, 1);
        for (StepHandler handler2 : this.stepHandlers) {
            handler2.handleStep(interpolator, this.isLastStep);
        }
        return currentT;
    }

    protected void sanityChecks(ExpandableStatefulODE equations, double t2) throws NumberIsTooSmallException, DimensionMismatchException {
        double threshold = 1000.0d * FastMath.ulp(FastMath.max(FastMath.abs(equations.getTime()), FastMath.abs(t2)));
        double dt = FastMath.abs(equations.getTime() - t2);
        if (dt <= threshold) {
            throw new NumberIsTooSmallException(LocalizedFormats.TOO_SMALL_INTEGRATION_INTERVAL, Double.valueOf(dt), Double.valueOf(threshold), false);
        }
    }
}
