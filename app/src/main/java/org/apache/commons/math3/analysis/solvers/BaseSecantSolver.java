package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.ConvergenceException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/BaseSecantSolver.class */
public abstract class BaseSecantSolver extends AbstractUnivariateSolver implements BracketedUnivariateSolver<UnivariateFunction> {
    protected static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;
    private AllowedSolution allowed;
    private final Method method;

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/BaseSecantSolver$Method.class */
    protected enum Method {
        REGULA_FALSI,
        ILLINOIS,
        PEGASUS
    }

    protected BaseSecantSolver(double absoluteAccuracy, Method method) {
        super(absoluteAccuracy);
        this.allowed = AllowedSolution.ANY_SIDE;
        this.method = method;
    }

    protected BaseSecantSolver(double relativeAccuracy, double absoluteAccuracy, Method method) {
        super(relativeAccuracy, absoluteAccuracy);
        this.allowed = AllowedSolution.ANY_SIDE;
        this.method = method;
    }

    protected BaseSecantSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy, Method method) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
        this.allowed = AllowedSolution.ANY_SIDE;
        this.method = method;
    }

    @Override // org.apache.commons.math3.analysis.solvers.BracketedUnivariateSolver
    public double solve(int maxEval, UnivariateFunction f2, double min, double max, AllowedSolution allowedSolution) {
        return solve(maxEval, f2, min, max, min + (0.5d * (max - min)), allowedSolution);
    }

    @Override // org.apache.commons.math3.analysis.solvers.BracketedUnivariateSolver
    public double solve(int maxEval, UnivariateFunction f2, double min, double max, double startValue, AllowedSolution allowedSolution) {
        this.allowed = allowedSolution;
        return super.solve(maxEval, (int) f2, min, max, startValue);
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver, org.apache.commons.math3.analysis.solvers.BaseUnivariateSolver
    public double solve(int maxEval, UnivariateFunction f2, double min, double max, double startValue) {
        return solve(maxEval, f2, min, max, startValue, AllowedSolution.ANY_SIDE);
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver
    protected final double doSolve() throws ConvergenceException {
        double x0 = getMin();
        double x1 = getMax();
        double f0 = computeObjectiveValue(x0);
        double f1 = computeObjectiveValue(x1);
        if (f0 == 0.0d) {
            return x0;
        }
        if (f1 == 0.0d) {
            return x1;
        }
        verifyBracketing(x0, x1);
        double ftol = getFunctionValueAccuracy();
        double atol = getAbsoluteAccuracy();
        double rtol = getRelativeAccuracy();
        boolean inverted = false;
        do {
            double x2 = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
            double fx = computeObjectiveValue(x2);
            if (fx == 0.0d) {
                return x2;
            }
            if (f1 * fx < 0.0d) {
                x0 = x1;
                f0 = f1;
                inverted = !inverted;
            } else {
                switch (this.method) {
                    case ILLINOIS:
                        f0 *= 0.5d;
                        break;
                    case PEGASUS:
                        f0 *= f1 / (f1 + fx);
                        break;
                    case REGULA_FALSI:
                        if (x2 == x1) {
                            throw new ConvergenceException();
                        }
                        break;
                    default:
                        throw new MathInternalError();
                }
            }
            x1 = x2;
            f1 = fx;
            if (FastMath.abs(f1) <= ftol) {
                switch (this.allowed) {
                    case ANY_SIDE:
                        return x1;
                    case LEFT_SIDE:
                        if (inverted) {
                            return x1;
                        }
                        break;
                    case RIGHT_SIDE:
                        if (!inverted) {
                            return x1;
                        }
                        break;
                    case BELOW_SIDE:
                        if (f1 <= 0.0d) {
                            return x1;
                        }
                        break;
                    case ABOVE_SIDE:
                        if (f1 >= 0.0d) {
                            return x1;
                        }
                        break;
                    default:
                        throw new MathInternalError();
                }
            }
        } while (FastMath.abs(x1 - x0) >= FastMath.max(rtol * FastMath.abs(x1), atol));
        switch (this.allowed) {
            case ANY_SIDE:
                return x1;
            case LEFT_SIDE:
                return inverted ? x1 : x0;
            case RIGHT_SIDE:
                return inverted ? x0 : x1;
            case BELOW_SIDE:
                return f1 <= 0.0d ? x1 : x0;
            case ABOVE_SIDE:
                return f1 >= 0.0d ? x1 : x0;
            default:
                throw new MathInternalError();
        }
    }
}
