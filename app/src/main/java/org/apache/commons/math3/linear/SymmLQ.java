package org.apache.commons.math3.linear;

import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MaxCountExceededException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.exception.util.ExceptionContext;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.IterationManager;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/SymmLQ.class */
public class SymmLQ extends PreconditionedIterativeLinearSolver {
    private static final String OPERATOR = "operator";
    private static final String THRESHOLD = "threshold";
    private static final String VECTOR = "vector";
    private static final String VECTOR1 = "vector1";
    private static final String VECTOR2 = "vector2";
    private final boolean check;
    private final double delta;

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/linear/SymmLQ$State.class */
    private static class State {

        /* renamed from: a, reason: collision with root package name */
        private final RealLinearOperator f13036a;

        /* renamed from: b, reason: collision with root package name */
        private final RealVector f13037b;
        private final boolean check;
        private final double delta;
        private double beta;
        private double beta1;
        private double bstep;
        private double cgnorm;
        private double dbar;
        private double gammaZeta;
        private double gbar;
        private double gmax;
        private double gmin;
        private final boolean goodb;
        private boolean hasConverged;
        private double lqnorm;

        /* renamed from: m, reason: collision with root package name */
        private final RealLinearOperator f13038m;
        private double minusEpsZeta;
        private final RealVector mb;
        private double oldb;
        private RealVector r1;
        private RealVector r2;
        private double rnorm;
        private final double shift;
        private double snprod;
        private double tnorm;
        private RealVector wbar;
        private final RealVector xL;

        /* renamed from: y, reason: collision with root package name */
        private RealVector f13039y;
        private double ynorm2;
        private boolean bIsNull;
        static final double MACH_PREC = FastMath.ulp(1.0d);
        static final double CBRT_MACH_PREC = FastMath.cbrt(MACH_PREC);

        State(RealLinearOperator a2, RealLinearOperator m2, RealVector b2, boolean goodb, double shift, double delta, boolean check) {
            this.f13036a = a2;
            this.f13038m = m2;
            this.f13037b = b2;
            this.xL = new ArrayRealVector(b2.getDimension());
            this.goodb = goodb;
            this.shift = shift;
            this.mb = m2 == null ? b2 : m2.operate(b2);
            this.hasConverged = false;
            this.check = check;
            this.delta = delta;
        }

        private static void checkSymmetry(RealLinearOperator l2, RealVector x2, RealVector y2, RealVector z2) throws NonSelfAdjointOperatorException, DimensionMismatchException {
            double s2 = y2.dotProduct(y2);
            double t2 = x2.dotProduct(z2);
            double epsa = (s2 + MACH_PREC) * CBRT_MACH_PREC;
            if (FastMath.abs(s2 - t2) > epsa) {
                NonSelfAdjointOperatorException e2 = new NonSelfAdjointOperatorException();
                ExceptionContext context = e2.getContext();
                context.setValue("operator", l2);
                context.setValue(SymmLQ.VECTOR1, x2);
                context.setValue(SymmLQ.VECTOR2, y2);
                context.setValue("threshold", Double.valueOf(epsa));
                throw e2;
            }
        }

        private static void throwNPDLOException(RealLinearOperator l2, RealVector v2) throws NonPositiveDefiniteOperatorException {
            NonPositiveDefiniteOperatorException e2 = new NonPositiveDefiniteOperatorException();
            ExceptionContext context = e2.getContext();
            context.setValue("operator", l2);
            context.setValue("vector", v2);
            throw e2;
        }

        private static void daxpy(double a2, RealVector x2, RealVector y2) throws OutOfRangeException {
            int n2 = x2.getDimension();
            for (int i2 = 0; i2 < n2; i2++) {
                y2.setEntry(i2, (a2 * x2.getEntry(i2)) + y2.getEntry(i2));
            }
        }

        private static void daxpbypz(double a2, RealVector x2, double b2, RealVector y2, RealVector z2) throws OutOfRangeException {
            int n2 = z2.getDimension();
            for (int i2 = 0; i2 < n2; i2++) {
                double zi = (a2 * x2.getEntry(i2)) + (b2 * y2.getEntry(i2)) + z2.getEntry(i2);
                z2.setEntry(i2, zi);
            }
        }

        void refineSolution(RealVector x2) throws OutOfRangeException {
            int n2 = this.xL.getDimension();
            if (this.lqnorm < this.cgnorm) {
                if (!this.goodb) {
                    x2.setSubVector(0, this.xL);
                    return;
                }
                double step = this.bstep / this.beta1;
                for (int i2 = 0; i2 < n2; i2++) {
                    double bi2 = this.mb.getEntry(i2);
                    double xi = this.xL.getEntry(i2);
                    x2.setEntry(i2, xi + (step * bi2));
                }
                return;
            }
            double anorm = FastMath.sqrt(this.tnorm);
            double diag = this.gbar == 0.0d ? anorm * MACH_PREC : this.gbar;
            double zbar = this.gammaZeta / diag;
            double step2 = (this.bstep + (this.snprod * zbar)) / this.beta1;
            if (!this.goodb) {
                for (int i3 = 0; i3 < n2; i3++) {
                    double xi2 = this.xL.getEntry(i3);
                    double wi = this.wbar.getEntry(i3);
                    x2.setEntry(i3, xi2 + (zbar * wi));
                }
                return;
            }
            for (int i4 = 0; i4 < n2; i4++) {
                double xi3 = this.xL.getEntry(i4);
                double wi2 = this.wbar.getEntry(i4);
                double bi3 = this.mb.getEntry(i4);
                x2.setEntry(i4, xi3 + (zbar * wi2) + (step2 * bi3));
            }
        }

        void init() throws OutOfRangeException, NonSelfAdjointOperatorException, DimensionMismatchException, NonPositiveDefiniteOperatorException {
            this.xL.set(0.0d);
            this.r1 = this.f13037b.copy();
            this.f13039y = this.f13038m == null ? this.f13037b.copy() : this.f13038m.operate(this.r1);
            if (this.f13038m != null && this.check) {
                checkSymmetry(this.f13038m, this.r1, this.f13039y, this.f13038m.operate(this.f13039y));
            }
            this.beta1 = this.r1.dotProduct(this.f13039y);
            if (this.beta1 < 0.0d) {
                throwNPDLOException(this.f13038m, this.f13039y);
            }
            if (this.beta1 == 0.0d) {
                this.bIsNull = true;
                return;
            }
            this.bIsNull = false;
            this.beta1 = FastMath.sqrt(this.beta1);
            RealVector v2 = this.f13039y.mapMultiply(1.0d / this.beta1);
            this.f13039y = this.f13036a.operate(v2);
            if (this.check) {
                checkSymmetry(this.f13036a, v2, this.f13039y, this.f13036a.operate(this.f13039y));
            }
            daxpy(-this.shift, v2, this.f13039y);
            double alpha = v2.dotProduct(this.f13039y);
            daxpy((-alpha) / this.beta1, this.r1, this.f13039y);
            double vty = v2.dotProduct(this.f13039y);
            double vtv = v2.dotProduct(v2);
            daxpy((-vty) / vtv, v2, this.f13039y);
            this.r2 = this.f13039y.copy();
            if (this.f13038m != null) {
                this.f13039y = this.f13038m.operate(this.r2);
            }
            this.oldb = this.beta1;
            this.beta = this.r2.dotProduct(this.f13039y);
            if (this.beta < 0.0d) {
                throwNPDLOException(this.f13038m, this.f13039y);
            }
            this.beta = FastMath.sqrt(this.beta);
            this.cgnorm = this.beta1;
            this.gbar = alpha;
            this.dbar = this.beta;
            this.gammaZeta = this.beta1;
            this.minusEpsZeta = 0.0d;
            this.bstep = 0.0d;
            this.snprod = 1.0d;
            this.tnorm = (alpha * alpha) + (this.beta * this.beta);
            this.ynorm2 = 0.0d;
            this.gmax = FastMath.abs(alpha) + MACH_PREC;
            this.gmin = this.gmax;
            if (this.goodb) {
                this.wbar = new ArrayRealVector(this.f13036a.getRowDimension());
                this.wbar.set(0.0d);
            } else {
                this.wbar = v2;
            }
            updateNorms();
        }

        void update() throws OutOfRangeException, DimensionMismatchException, NonPositiveDefiniteOperatorException {
            RealVector v2 = this.f13039y.mapMultiply(1.0d / this.beta);
            this.f13039y = this.f13036a.operate(v2);
            daxpbypz(-this.shift, v2, (-this.beta) / this.oldb, this.r1, this.f13039y);
            double alpha = v2.dotProduct(this.f13039y);
            daxpy((-alpha) / this.beta, this.r2, this.f13039y);
            this.r1 = this.r2;
            this.r2 = this.f13039y;
            if (this.f13038m != null) {
                this.f13039y = this.f13038m.operate(this.r2);
            }
            this.oldb = this.beta;
            this.beta = this.r2.dotProduct(this.f13039y);
            if (this.beta < 0.0d) {
                throwNPDLOException(this.f13038m, this.f13039y);
            }
            this.beta = FastMath.sqrt(this.beta);
            this.tnorm += (alpha * alpha) + (this.oldb * this.oldb) + (this.beta * this.beta);
            double gamma = FastMath.sqrt((this.gbar * this.gbar) + (this.oldb * this.oldb));
            double c2 = this.gbar / gamma;
            double s2 = this.oldb / gamma;
            double deltak = (c2 * this.dbar) + (s2 * alpha);
            this.gbar = (s2 * this.dbar) - (c2 * alpha);
            double eps = s2 * this.beta;
            this.dbar = (-c2) * this.beta;
            double zeta = this.gammaZeta / gamma;
            double zetaC = zeta * c2;
            double zetaS = zeta * s2;
            int n2 = this.xL.getDimension();
            for (int i2 = 0; i2 < n2; i2++) {
                double xi = this.xL.getEntry(i2);
                double vi = v2.getEntry(i2);
                double wi = this.wbar.getEntry(i2);
                this.xL.setEntry(i2, xi + (wi * zetaC) + (vi * zetaS));
                this.wbar.setEntry(i2, (wi * s2) - (vi * c2));
            }
            this.bstep += this.snprod * c2 * zeta;
            this.snprod *= s2;
            this.gmax = FastMath.max(this.gmax, gamma);
            this.gmin = FastMath.min(this.gmin, gamma);
            this.ynorm2 += zeta * zeta;
            this.gammaZeta = this.minusEpsZeta - (deltak * zeta);
            this.minusEpsZeta = (-eps) * zeta;
            updateNorms();
        }

        private void updateNorms() {
            double acond;
            double anorm = FastMath.sqrt(this.tnorm);
            double ynorm = FastMath.sqrt(this.ynorm2);
            double epsa = anorm * MACH_PREC;
            double epsx = anorm * ynorm * MACH_PREC;
            double epsr = anorm * ynorm * this.delta;
            double diag = this.gbar == 0.0d ? epsa : this.gbar;
            this.lqnorm = FastMath.sqrt((this.gammaZeta * this.gammaZeta) + (this.minusEpsZeta * this.minusEpsZeta));
            double qrnorm = this.snprod * this.beta1;
            this.cgnorm = (qrnorm * this.beta) / FastMath.abs(diag);
            if (this.lqnorm <= this.cgnorm) {
                acond = this.gmax / this.gmin;
            } else {
                acond = this.gmax / FastMath.min(this.gmin, FastMath.abs(diag));
            }
            if (acond * MACH_PREC >= 0.1d) {
                throw new IllConditionedOperatorException(acond);
            }
            if (this.beta1 <= epsx) {
                throw new SingularOperatorException();
            }
            this.rnorm = FastMath.min(this.cgnorm, this.lqnorm);
            this.hasConverged = this.cgnorm <= epsx || this.cgnorm <= epsr;
        }

        boolean hasConverged() {
            return this.hasConverged;
        }

        boolean bEqualsNullVector() {
            return this.bIsNull;
        }

        boolean betaEqualsZero() {
            return this.beta < MACH_PREC;
        }

        double getNormOfResidual() {
            return this.rnorm;
        }
    }

    public SymmLQ(int maxIterations, double delta, boolean check) {
        super(maxIterations);
        this.delta = delta;
        this.check = check;
    }

    public SymmLQ(IterationManager manager, double delta, boolean check) {
        super(manager);
        this.delta = delta;
        this.check = check;
    }

    public final boolean getCheck() {
        return this.check;
    }

    @Override // org.apache.commons.math3.linear.PreconditionedIterativeLinearSolver
    public RealVector solve(RealLinearOperator a2, RealLinearOperator m2, RealVector b2) throws NullArgumentException, NonSelfAdjointOperatorException, DimensionMismatchException, MaxCountExceededException, NonPositiveDefiniteOperatorException, IllConditionedOperatorException {
        MathUtils.checkNotNull(a2);
        RealVector x2 = new ArrayRealVector(a2.getColumnDimension());
        return solveInPlace(a2, m2, b2, x2, false, 0.0d);
    }

    public RealVector solve(RealLinearOperator a2, RealLinearOperator m2, RealVector b2, boolean goodb, double shift) throws NullArgumentException, NonSelfAdjointOperatorException, DimensionMismatchException, MaxCountExceededException, NonPositiveDefiniteOperatorException, IllConditionedOperatorException {
        MathUtils.checkNotNull(a2);
        RealVector x2 = new ArrayRealVector(a2.getColumnDimension());
        return solveInPlace(a2, m2, b2, x2, goodb, shift);
    }

    @Override // org.apache.commons.math3.linear.PreconditionedIterativeLinearSolver
    public RealVector solve(RealLinearOperator a2, RealLinearOperator m2, RealVector b2, RealVector x2) throws NullArgumentException, NonSelfAdjointOperatorException, DimensionMismatchException, MaxCountExceededException, NonPositiveDefiniteOperatorException, IllConditionedOperatorException {
        MathUtils.checkNotNull(x2);
        return solveInPlace(a2, m2, b2, x2.copy(), false, 0.0d);
    }

    @Override // org.apache.commons.math3.linear.PreconditionedIterativeLinearSolver, org.apache.commons.math3.linear.IterativeLinearSolver
    public RealVector solve(RealLinearOperator a2, RealVector b2) throws OutOfRangeException, NullArgumentException, NonSelfAdjointOperatorException, DimensionMismatchException, MaxCountExceededException, IllConditionedOperatorException {
        MathUtils.checkNotNull(a2);
        RealVector x2 = new ArrayRealVector(a2.getColumnDimension());
        x2.set(0.0d);
        return solveInPlace(a2, null, b2, x2, false, 0.0d);
    }

    public RealVector solve(RealLinearOperator a2, RealVector b2, boolean goodb, double shift) throws NullArgumentException, NonSelfAdjointOperatorException, DimensionMismatchException, MaxCountExceededException, IllConditionedOperatorException {
        MathUtils.checkNotNull(a2);
        RealVector x2 = new ArrayRealVector(a2.getColumnDimension());
        return solveInPlace(a2, null, b2, x2, goodb, shift);
    }

    @Override // org.apache.commons.math3.linear.PreconditionedIterativeLinearSolver, org.apache.commons.math3.linear.IterativeLinearSolver
    public RealVector solve(RealLinearOperator a2, RealVector b2, RealVector x2) throws NullArgumentException, NonSelfAdjointOperatorException, DimensionMismatchException, MaxCountExceededException, IllConditionedOperatorException {
        MathUtils.checkNotNull(x2);
        return solveInPlace(a2, null, b2, x2.copy(), false, 0.0d);
    }

    @Override // org.apache.commons.math3.linear.PreconditionedIterativeLinearSolver
    public RealVector solveInPlace(RealLinearOperator a2, RealLinearOperator m2, RealVector b2, RealVector x2) throws NullArgumentException, NonSelfAdjointOperatorException, DimensionMismatchException, MaxCountExceededException, NonPositiveDefiniteOperatorException, IllConditionedOperatorException {
        return solveInPlace(a2, m2, b2, x2, false, 0.0d);
    }

    public RealVector solveInPlace(RealLinearOperator a2, RealLinearOperator m2, RealVector b2, RealVector x2, boolean goodb, double shift) throws OutOfRangeException, NonSelfAdjointOperatorException, NullArgumentException, MaxCountExceededException, DimensionMismatchException, NonPositiveDefiniteOperatorException, IllConditionedOperatorException {
        checkParameters(a2, m2, b2, x2);
        IterationManager manager = getIterationManager();
        manager.resetIterationCount();
        manager.incrementIterationCount();
        State state = new State(a2, m2, b2, goodb, shift, this.delta, this.check);
        state.init();
        state.refineSolution(x2);
        IterativeLinearSolverEvent event = new DefaultIterativeLinearSolverEvent(this, manager.getIterations(), x2, b2, state.getNormOfResidual());
        if (state.bEqualsNullVector()) {
            manager.fireTerminationEvent(event);
            return x2;
        }
        boolean earlyStop = state.betaEqualsZero() || state.hasConverged();
        manager.fireInitializationEvent(event);
        if (!earlyStop) {
            do {
                manager.incrementIterationCount();
                manager.fireIterationStartedEvent(new DefaultIterativeLinearSolverEvent(this, manager.getIterations(), x2, b2, state.getNormOfResidual()));
                state.update();
                state.refineSolution(x2);
                manager.fireIterationPerformedEvent(new DefaultIterativeLinearSolverEvent(this, manager.getIterations(), x2, b2, state.getNormOfResidual()));
            } while (!state.hasConverged());
        }
        manager.fireTerminationEvent(new DefaultIterativeLinearSolverEvent(this, manager.getIterations(), x2, b2, state.getNormOfResidual()));
        return x2;
    }

    @Override // org.apache.commons.math3.linear.PreconditionedIterativeLinearSolver, org.apache.commons.math3.linear.IterativeLinearSolver
    public RealVector solveInPlace(RealLinearOperator a2, RealVector b2, RealVector x2) throws NullArgumentException, NonSelfAdjointOperatorException, DimensionMismatchException, MaxCountExceededException, IllConditionedOperatorException {
        return solveInPlace(a2, null, b2, x2, false, 0.0d);
    }
}
