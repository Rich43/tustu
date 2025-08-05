package org.apache.commons.math3.ode.events;

import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.analysis.RealFieldUnivariateFunction;
import org.apache.commons.math3.analysis.solvers.AllowedSolution;
import org.apache.commons.math3.analysis.solvers.BracketedRealFieldUnivariateSolver;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.ode.FieldODEState;
import org.apache.commons.math3.ode.FieldODEStateAndDerivative;
import org.apache.commons.math3.ode.sampling.FieldStepInterpolator;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/ode/events/FieldEventState.class */
public class FieldEventState<T extends RealFieldElement<T>> {
    private final FieldEventHandler<T> handler;
    private final double maxCheckInterval;
    private final T convergence;
    private final int maxIterationCount;
    private boolean forward;
    private final BracketedRealFieldUnivariateSolver<T> solver;
    private T t0 = null;
    private T g0 = null;
    private boolean g0Positive = true;
    private boolean pendingEvent = false;
    private T pendingEventTime = null;
    private T previousEventTime = null;
    private boolean increasing = true;
    private Action nextAction = Action.CONTINUE;

    public FieldEventState(FieldEventHandler<T> handler, double maxCheckInterval, T convergence, int maxIterationCount, BracketedRealFieldUnivariateSolver<T> solver) {
        this.handler = handler;
        this.maxCheckInterval = maxCheckInterval;
        this.convergence = (T) convergence.abs();
        this.maxIterationCount = maxIterationCount;
        this.solver = solver;
    }

    public FieldEventHandler<T> getEventHandler() {
        return this.handler;
    }

    public double getMaxCheckInterval() {
        return this.maxCheckInterval;
    }

    public T getConvergence() {
        return this.convergence;
    }

    public int getMaxIterationCount() {
        return this.maxIterationCount;
    }

    public void reinitializeBegin(FieldStepInterpolator<T> fieldStepInterpolator) throws MaxCountExceededException {
        FieldODEStateAndDerivative<T> previousState = fieldStepInterpolator.getPreviousState();
        this.t0 = previousState.getTime();
        this.g0 = (T) this.handler.g(previousState);
        if (this.g0.getReal() == 0.0d) {
            this.g0 = (T) this.handler.g(fieldStepInterpolator.getInterpolatedState((RealFieldElement) this.t0.add(0.5d * FastMath.max(this.solver.getAbsoluteAccuracy().getReal(), FastMath.abs(((RealFieldElement) this.solver.getRelativeAccuracy().multiply(this.t0)).getReal())))));
        }
        this.g0Positive = this.g0.getReal() >= 0.0d;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v110, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r0v112, types: [org.apache.commons.math3.analysis.solvers.BracketedRealFieldUnivariateSolver, org.apache.commons.math3.analysis.solvers.BracketedRealFieldUnivariateSolver<T extends org.apache.commons.math3.RealFieldElement<T>>] */
    /* JADX WARN: Type inference failed for: r0v34, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r0v38, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r0v48, types: [org.apache.commons.math3.analysis.solvers.BracketedRealFieldUnivariateSolver, org.apache.commons.math3.analysis.solvers.BracketedRealFieldUnivariateSolver<T extends org.apache.commons.math3.RealFieldElement<T>>] */
    /* JADX WARN: Type inference failed for: r0v92, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r0v95, types: [org.apache.commons.math3.RealFieldElement] */
    public boolean evaluateStep(final FieldStepInterpolator<T> fieldStepInterpolator) throws MaxCountExceededException, NoBracketingException {
        this.forward = fieldStepInterpolator.isForward();
        T time = fieldStepInterpolator.getCurrentState().getTime();
        RealFieldElement realFieldElement = (RealFieldElement) time.subtract(this.t0);
        if (((RealFieldElement) ((RealFieldElement) realFieldElement.abs()).subtract(this.convergence)).getReal() < 0.0d) {
            return false;
        }
        int iMax = FastMath.max(1, (int) FastMath.ceil(FastMath.abs(realFieldElement.getReal()) / this.maxCheckInterval));
        RealFieldElement realFieldElement2 = (RealFieldElement) realFieldElement.divide(iMax);
        RealFieldUnivariateFunction realFieldUnivariateFunction = new RealFieldUnivariateFunction<T>() { // from class: org.apache.commons.math3.ode.events.FieldEventState.1
            @Override // org.apache.commons.math3.analysis.RealFieldUnivariateFunction
            public T value(T t2) {
                return (T) FieldEventState.this.handler.g(fieldStepInterpolator.getInterpolatedState(t2));
            }
        };
        T t2 = this.t0;
        T tValue = this.g0;
        int i2 = 0;
        while (i2 < iMax) {
            T t3 = i2 == iMax - 1 ? time : (RealFieldElement) this.t0.add(realFieldElement2.multiply(i2 + 1));
            ?? G2 = this.handler.g(fieldStepInterpolator.getInterpolatedState(t3));
            if (this.g0Positive ^ (G2.getReal() >= 0.0d)) {
                this.increasing = ((RealFieldElement) G2.subtract(tValue)).getReal() >= 0.0d;
                T t4 = (T) (this.forward ? this.solver.solve(this.maxIterationCount, realFieldUnivariateFunction, t2, t3, AllowedSolution.RIGHT_SIDE) : this.solver.solve(this.maxIterationCount, realFieldUnivariateFunction, t3, t2, AllowedSolution.LEFT_SIDE));
                if (this.previousEventTime != null && ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) t4.subtract(t2)).abs()).subtract(this.convergence)).getReal() <= 0.0d && ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) t4.subtract(this.previousEventTime)).abs()).subtract(this.convergence)).getReal() <= 0.0d) {
                    do {
                        t2 = this.forward ? (RealFieldElement) t2.add(this.convergence) : (RealFieldElement) t2.subtract(this.convergence);
                        tValue = realFieldUnivariateFunction.value(t2);
                        if (!(this.g0Positive ^ (tValue.getReal() >= 0.0d))) {
                            break;
                        }
                    } while (this.forward ^ (((RealFieldElement) t2.subtract(t3)).getReal() >= 0.0d));
                    if (this.forward ^ (((RealFieldElement) t2.subtract(t3)).getReal() >= 0.0d)) {
                        i2--;
                    } else {
                        this.pendingEventTime = t4;
                        this.pendingEvent = true;
                        return true;
                    }
                } else {
                    if (this.previousEventTime == null || ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.previousEventTime.subtract(t4)).abs()).subtract(this.convergence)).getReal() > 0.0d) {
                        this.pendingEventTime = t4;
                        this.pendingEvent = true;
                        return true;
                    }
                    t2 = t3;
                    tValue = G2;
                }
            } else {
                t2 = t3;
                tValue = G2;
            }
            i2++;
        }
        this.pendingEvent = false;
        this.pendingEventTime = null;
        return false;
    }

    public T getEventTime() {
        if (this.pendingEvent) {
            return this.pendingEventTime;
        }
        return (T) ((RealFieldElement) this.t0.getField().getZero()).add(this.forward ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY);
    }

    public void stepAccepted(FieldODEStateAndDerivative<T> fieldODEStateAndDerivative) {
        this.t0 = fieldODEStateAndDerivative.getTime();
        this.g0 = (T) this.handler.g(fieldODEStateAndDerivative);
        if (this.pendingEvent && ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.pendingEventTime.subtract(fieldODEStateAndDerivative.getTime())).abs()).subtract(this.convergence)).getReal() <= 0.0d) {
            this.previousEventTime = fieldODEStateAndDerivative.getTime();
            this.g0Positive = this.increasing;
            this.nextAction = this.handler.eventOccurred(fieldODEStateAndDerivative, !(this.increasing ^ this.forward));
        } else {
            this.g0Positive = this.g0.getReal() >= 0.0d;
            this.nextAction = Action.CONTINUE;
        }
    }

    public boolean stop() {
        return this.nextAction == Action.STOP;
    }

    public FieldODEState<T> reset(FieldODEStateAndDerivative<T> state) {
        FieldODEState<T> newState;
        if (!this.pendingEvent || ((RealFieldElement) ((RealFieldElement) ((RealFieldElement) this.pendingEventTime.subtract(state.getTime())).abs()).subtract(this.convergence)).getReal() > 0.0d) {
            return null;
        }
        if (this.nextAction == Action.RESET_STATE) {
            newState = this.handler.resetState(state);
        } else if (this.nextAction == Action.RESET_DERIVATIVES) {
            newState = state;
        } else {
            newState = null;
        }
        this.pendingEvent = false;
        this.pendingEventTime = null;
        return newState;
    }
}
