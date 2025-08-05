package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.ExceptionContext;
import org.apache.commons.math3.util.IterationManager;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/ConjugateGradient.class */
public class ConjugateGradient extends PreconditionedIterativeLinearSolver {
    public static final String OPERATOR = "operator";
    public static final String VECTOR = "vector";
    private boolean check;
    private final double delta;

    public ConjugateGradient(int maxIterations, double delta, boolean check) {
        super(maxIterations);
        this.delta = delta;
        this.check = check;
    }

    public ConjugateGradient(IterationManager manager, double delta, boolean check) throws NullArgumentException {
        super(manager);
        this.delta = delta;
        this.check = check;
    }

    public final boolean getCheck() {
        return this.check;
    }

    @Override // org.apache.commons.math3.linear.PreconditionedIterativeLinearSolver
    public RealVector solveInPlace(RealLinearOperator a2, RealLinearOperator m2, RealVector b2, RealVector x0) throws OutOfRangeException, NullArgumentException, MaxCountExceededException, DimensionMismatchException, NonPositiveDefiniteOperatorException {
        RealVector z2;
        IterativeLinearSolverEvent evt;
        checkParameters(a2, m2, b2, x0);
        IterationManager manager = getIterationManager();
        manager.resetIterationCount();
        double rmax = this.delta * b2.getNorm();
        RealVector bro = RealVector.unmodifiableRealVector(b2);
        manager.incrementIterationCount();
        RealVector xro = RealVector.unmodifiableRealVector(x0);
        RealVector p2 = x0.copy();
        RealVector r2 = b2.combine(1.0d, -1.0d, a2.operate(p2));
        RealVector rro = RealVector.unmodifiableRealVector(r2);
        double rnorm = r2.getNorm();
        if (m2 == null) {
            z2 = r2;
        } else {
            z2 = null;
        }
        IterativeLinearSolverEvent evt2 = new DefaultIterativeLinearSolverEvent(this, manager.getIterations(), xro, bro, rro, rnorm);
        manager.fireInitializationEvent(evt2);
        if (rnorm <= rmax) {
            manager.fireTerminationEvent(evt2);
            return x0;
        }
        double rhoPrev = 0.0d;
        do {
            manager.incrementIterationCount();
            manager.fireIterationStartedEvent(new DefaultIterativeLinearSolverEvent(this, manager.getIterations(), xro, bro, rro, rnorm));
            if (m2 != null) {
                z2 = m2.operate(r2);
            }
            double rhoNext = r2.dotProduct(z2);
            if (this.check && rhoNext <= 0.0d) {
                NonPositiveDefiniteOperatorException e2 = new NonPositiveDefiniteOperatorException();
                ExceptionContext context = e2.getContext();
                context.setValue(OPERATOR, m2);
                context.setValue(VECTOR, r2);
                throw e2;
            }
            if (manager.getIterations() == 2) {
                p2.setSubVector(0, z2);
            } else {
                p2.combineToSelf(rhoNext / rhoPrev, 1.0d, z2);
            }
            RealVector q2 = a2.operate(p2);
            double pq = p2.dotProduct(q2);
            if (this.check && pq <= 0.0d) {
                NonPositiveDefiniteOperatorException e3 = new NonPositiveDefiniteOperatorException();
                ExceptionContext context2 = e3.getContext();
                context2.setValue(OPERATOR, a2);
                context2.setValue(VECTOR, p2);
                throw e3;
            }
            double alpha = rhoNext / pq;
            x0.combineToSelf(1.0d, alpha, p2);
            r2.combineToSelf(1.0d, -alpha, q2);
            rhoPrev = rhoNext;
            rnorm = r2.getNorm();
            evt = new DefaultIterativeLinearSolverEvent(this, manager.getIterations(), xro, bro, rro, rnorm);
            manager.fireIterationPerformedEvent(evt);
        } while (rnorm > rmax);
        manager.fireTerminationEvent(evt);
        return x0;
    }
}
