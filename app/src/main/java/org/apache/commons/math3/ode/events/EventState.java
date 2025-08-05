package org.apache.commons.math3.ode.events;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.solvers.AllowedSolution;
import org.apache.commons.math3.analysis.solvers.BracketedUnivariateSolver;
import org.apache.commons.math3.analysis.solvers.PegasusSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolver;
import org.apache.commons.math3.analysis.solvers.UnivariateSolverUtils;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.EquationsMapper;
import org.apache.commons.math3.ode.ExpandableStatefulODE;
import org.apache.commons.math3.ode.events.EventHandler;
import org.apache.commons.math3.ode.sampling.StepInterpolator;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/events/EventState.class */
public class EventState {
    private final EventHandler handler;
    private final double maxCheckInterval;
    private final double convergence;
    private final int maxIterationCount;
    private boolean forward;
    private final UnivariateSolver solver;
    private ExpandableStatefulODE expandable = null;
    private double t0 = Double.NaN;
    private double g0 = Double.NaN;
    private boolean g0Positive = true;
    private boolean pendingEvent = false;
    private double pendingEventTime = Double.NaN;
    private double previousEventTime = Double.NaN;
    private boolean increasing = true;
    private EventHandler.Action nextAction = EventHandler.Action.CONTINUE;

    public EventState(EventHandler handler, double maxCheckInterval, double convergence, int maxIterationCount, UnivariateSolver solver) {
        this.handler = handler;
        this.maxCheckInterval = maxCheckInterval;
        this.convergence = FastMath.abs(convergence);
        this.maxIterationCount = maxIterationCount;
        this.solver = solver;
    }

    public EventHandler getEventHandler() {
        return this.handler;
    }

    public void setExpandable(ExpandableStatefulODE expandable) {
        this.expandable = expandable;
    }

    public double getMaxCheckInterval() {
        return this.maxCheckInterval;
    }

    public double getConvergence() {
        return this.convergence;
    }

    public int getMaxIterationCount() {
        return this.maxIterationCount;
    }

    public void reinitializeBegin(StepInterpolator interpolator) throws MaxCountExceededException {
        this.t0 = interpolator.getPreviousTime();
        interpolator.setInterpolatedTime(this.t0);
        this.g0 = this.handler.g(this.t0, getCompleteState(interpolator));
        if (this.g0 == 0.0d) {
            double epsilon = FastMath.max(this.solver.getAbsoluteAccuracy(), FastMath.abs(this.solver.getRelativeAccuracy() * this.t0));
            double tStart = this.t0 + (0.5d * epsilon);
            interpolator.setInterpolatedTime(tStart);
            this.g0 = this.handler.g(tStart, getCompleteState(interpolator));
        }
        this.g0Positive = this.g0 >= 0.0d;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public double[] getCompleteState(StepInterpolator interpolator) throws DimensionMismatchException {
        double[] complete = new double[this.expandable.getTotalDimension()];
        this.expandable.getPrimaryMapper().insertEquationData(interpolator.getInterpolatedState(), complete);
        int index = 0;
        EquationsMapper[] arr$ = this.expandable.getSecondaryMappers();
        for (EquationsMapper secondary : arr$) {
            int i2 = index;
            index++;
            secondary.insertEquationData(interpolator.getInterpolatedSecondaryState(i2), complete);
        }
        return complete;
    }

    public boolean evaluateStep(final StepInterpolator interpolator) throws MaxCountExceededException, NoBracketingException {
        double root;
        try {
            this.forward = interpolator.isForward();
            double t1 = interpolator.getCurrentTime();
            double dt = t1 - this.t0;
            if (FastMath.abs(dt) < this.convergence) {
                return false;
            }
            int n2 = FastMath.max(1, (int) FastMath.ceil(FastMath.abs(dt) / this.maxCheckInterval));
            double h2 = dt / n2;
            UnivariateFunction f2 = new UnivariateFunction() { // from class: org.apache.commons.math3.ode.events.EventState.1
                @Override // org.apache.commons.math3.analysis.UnivariateFunction
                public double value(double t2) throws LocalMaxCountExceededException {
                    try {
                        interpolator.setInterpolatedTime(t2);
                        return EventState.this.handler.g(t2, EventState.this.getCompleteState(interpolator));
                    } catch (MaxCountExceededException mcee) {
                        throw new LocalMaxCountExceededException(mcee);
                    }
                }
            };
            double ta = this.t0;
            double ga = this.g0;
            int i2 = 0;
            while (i2 < n2) {
                double tb = i2 == n2 - 1 ? t1 : this.t0 + ((i2 + 1) * h2);
                interpolator.setInterpolatedTime(tb);
                double gb = this.handler.g(tb, getCompleteState(interpolator));
                if (this.g0Positive ^ (gb >= 0.0d)) {
                    this.increasing = gb >= ga;
                    if (this.solver instanceof BracketedUnivariateSolver) {
                        BracketedUnivariateSolver<UnivariateFunction> bracketing = (BracketedUnivariateSolver) this.solver;
                        root = this.forward ? bracketing.solve(this.maxIterationCount, (int) f2, ta, tb, AllowedSolution.RIGHT_SIDE) : bracketing.solve(this.maxIterationCount, (int) f2, tb, ta, AllowedSolution.LEFT_SIDE);
                    } else {
                        double baseRoot = this.forward ? this.solver.solve(this.maxIterationCount, f2, ta, tb) : this.solver.solve(this.maxIterationCount, f2, tb, ta);
                        int remainingEval = this.maxIterationCount - this.solver.getEvaluations();
                        BracketedUnivariateSolver<UnivariateFunction> bracketing2 = new PegasusSolver(this.solver.getRelativeAccuracy(), this.solver.getAbsoluteAccuracy());
                        root = this.forward ? UnivariateSolverUtils.forceSide(remainingEval, f2, bracketing2, baseRoot, ta, tb, AllowedSolution.RIGHT_SIDE) : UnivariateSolverUtils.forceSide(remainingEval, f2, bracketing2, baseRoot, tb, ta, AllowedSolution.LEFT_SIDE);
                    }
                    if (!Double.isNaN(this.previousEventTime) && FastMath.abs(root - ta) <= this.convergence && FastMath.abs(root - this.previousEventTime) <= this.convergence) {
                        do {
                            ta = this.forward ? ta + this.convergence : ta - this.convergence;
                            ga = f2.value(ta);
                            if (!(this.g0Positive ^ (ga >= 0.0d))) {
                                break;
                            }
                        } while (this.forward ^ (ta >= tb));
                        if (this.forward ^ (ta >= tb)) {
                            i2--;
                        } else {
                            this.pendingEventTime = root;
                            this.pendingEvent = true;
                            return true;
                        }
                    } else {
                        if (Double.isNaN(this.previousEventTime) || FastMath.abs(this.previousEventTime - root) > this.convergence) {
                            this.pendingEventTime = root;
                            this.pendingEvent = true;
                            return true;
                        }
                        ta = tb;
                        ga = gb;
                    }
                } else {
                    ta = tb;
                    ga = gb;
                }
                i2++;
            }
            this.pendingEvent = false;
            this.pendingEventTime = Double.NaN;
            return false;
        } catch (LocalMaxCountExceededException lmcee) {
            throw lmcee.getException();
        }
    }

    public double getEventTime() {
        return this.pendingEvent ? this.pendingEventTime : this.forward ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
    }

    public void stepAccepted(double t2, double[] y2) {
        this.t0 = t2;
        this.g0 = this.handler.g(t2, y2);
        if (this.pendingEvent && FastMath.abs(this.pendingEventTime - t2) <= this.convergence) {
            this.previousEventTime = t2;
            this.g0Positive = this.increasing;
            this.nextAction = this.handler.eventOccurred(t2, y2, !(this.increasing ^ this.forward));
        } else {
            this.g0Positive = this.g0 >= 0.0d;
            this.nextAction = EventHandler.Action.CONTINUE;
        }
    }

    public boolean stop() {
        return this.nextAction == EventHandler.Action.STOP;
    }

    public boolean reset(double t2, double[] y2) {
        if (!this.pendingEvent || FastMath.abs(this.pendingEventTime - t2) > this.convergence) {
            return false;
        }
        if (this.nextAction == EventHandler.Action.RESET_STATE) {
            this.handler.resetState(t2, y2);
        }
        this.pendingEvent = false;
        this.pendingEventTime = Double.NaN;
        return this.nextAction == EventHandler.Action.RESET_STATE || this.nextAction == EventHandler.Action.RESET_DERIVATIVES;
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/events/EventState$LocalMaxCountExceededException.class */
    private static class LocalMaxCountExceededException extends RuntimeException {
        private static final long serialVersionUID = 20120901;
        private final MaxCountExceededException wrapped;

        LocalMaxCountExceededException(MaxCountExceededException exception) {
            this.wrapped = exception;
        }

        public MaxCountExceededException getException() {
            return this.wrapped;
        }
    }
}
