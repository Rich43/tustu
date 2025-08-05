package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.complex.ComplexUtils;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/LaguerreSolver.class */
public class LaguerreSolver extends AbstractPolynomialSolver {
    private static final double DEFAULT_ABSOLUTE_ACCURACY = 1.0E-6d;
    private final ComplexSolver complexSolver;

    public LaguerreSolver() {
        this(1.0E-6d);
    }

    public LaguerreSolver(double absoluteAccuracy) {
        super(absoluteAccuracy);
        this.complexSolver = new ComplexSolver();
    }

    public LaguerreSolver(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy);
        this.complexSolver = new ComplexSolver();
    }

    public LaguerreSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy);
        this.complexSolver = new ComplexSolver();
    }

    @Override // org.apache.commons.math3.analysis.solvers.BaseAbstractUnivariateSolver
    public double doSolve() throws TooManyEvaluationsException, NumberIsTooLargeException, NoBracketingException {
        double min = getMin();
        double max = getMax();
        double initial = getStartValue();
        double functionValueAccuracy = getFunctionValueAccuracy();
        verifySequence(min, initial, max);
        double yInitial = computeObjectiveValue(initial);
        if (FastMath.abs(yInitial) <= functionValueAccuracy) {
            return initial;
        }
        double yMin = computeObjectiveValue(min);
        if (FastMath.abs(yMin) <= functionValueAccuracy) {
            return min;
        }
        if (yInitial * yMin < 0.0d) {
            return laguerre(min, initial, yMin, yInitial);
        }
        double yMax = computeObjectiveValue(max);
        if (FastMath.abs(yMax) <= functionValueAccuracy) {
            return max;
        }
        if (yInitial * yMax < 0.0d) {
            return laguerre(initial, max, yInitial, yMax);
        }
        throw new NoBracketingException(min, max, yMin, yMax);
    }

    @Deprecated
    public double laguerre(double lo, double hi, double fLo, double fHi) throws NullArgumentException, NoDataException, TooManyEvaluationsException {
        Complex[] c2 = ComplexUtils.convertToComplex(getCoefficients());
        Complex initial = new Complex(0.5d * (lo + hi), 0.0d);
        Complex z2 = this.complexSolver.solve(c2, initial);
        if (this.complexSolver.isRoot(lo, hi, z2)) {
            return z2.getReal();
        }
        double r2 = Double.NaN;
        Complex[] root = this.complexSolver.solveAll(c2, initial);
        int i2 = 0;
        while (true) {
            if (i2 >= root.length) {
                break;
            }
            if (!this.complexSolver.isRoot(lo, hi, root[i2])) {
                i2++;
            } else {
                r2 = root[i2].getReal();
                break;
            }
        }
        return r2;
    }

    public Complex[] solveAllComplex(double[] coefficients, double initial) throws NullArgumentException, NoDataException, TooManyEvaluationsException {
        return solveAllComplex(coefficients, initial, Integer.MAX_VALUE);
    }

    public Complex[] solveAllComplex(double[] coefficients, double initial, int maxEval) throws NullArgumentException, NoDataException, TooManyEvaluationsException {
        setup(maxEval, new PolynomialFunction(coefficients), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, initial);
        return this.complexSolver.solveAll(ComplexUtils.convertToComplex(coefficients), new Complex(initial, 0.0d));
    }

    public Complex solveComplex(double[] coefficients, double initial) throws NullArgumentException, NoDataException, TooManyEvaluationsException {
        return solveComplex(coefficients, initial, Integer.MAX_VALUE);
    }

    public Complex solveComplex(double[] coefficients, double initial, int maxEval) throws NullArgumentException, NoDataException, TooManyEvaluationsException {
        setup(maxEval, new PolynomialFunction(coefficients), Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, initial);
        return this.complexSolver.solve(ComplexUtils.convertToComplex(coefficients), new Complex(initial, 0.0d));
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/LaguerreSolver$ComplexSolver.class */
    private class ComplexSolver {
        private ComplexSolver() {
        }

        public boolean isRoot(double min, double max, Complex z2) {
            if (LaguerreSolver.this.isSequence(min, z2.getReal(), max)) {
                double tolerance = FastMath.max(LaguerreSolver.this.getRelativeAccuracy() * z2.abs(), LaguerreSolver.this.getAbsoluteAccuracy());
                return FastMath.abs(z2.getImaginary()) <= tolerance || z2.abs() <= LaguerreSolver.this.getFunctionValueAccuracy();
            }
            return false;
        }

        public Complex[] solveAll(Complex[] coefficients, Complex initial) throws NullArgumentException, NoDataException, TooManyEvaluationsException {
            if (coefficients == null) {
                throw new NullArgumentException();
            }
            int n2 = coefficients.length - 1;
            if (n2 == 0) {
                throw new NoDataException(LocalizedFormats.POLYNOMIAL);
            }
            Complex[] c2 = new Complex[n2 + 1];
            for (int i2 = 0; i2 <= n2; i2++) {
                c2[i2] = coefficients[i2];
            }
            Complex[] root = new Complex[n2];
            for (int i3 = 0; i3 < n2; i3++) {
                Complex[] subarray = new Complex[(n2 - i3) + 1];
                System.arraycopy(c2, 0, subarray, 0, subarray.length);
                root[i3] = solve(subarray, initial);
                Complex newc = c2[n2 - i3];
                for (int j2 = (n2 - i3) - 1; j2 >= 0; j2--) {
                    Complex oldc = c2[j2];
                    c2[j2] = newc;
                    newc = oldc.add(newc.multiply(root[i3]));
                }
            }
            return root;
        }

        public Complex solve(Complex[] coefficients, Complex initial) throws NullArgumentException, NoDataException, TooManyEvaluationsException {
            if (coefficients == null) {
                throw new NullArgumentException();
            }
            int n2 = coefficients.length - 1;
            if (n2 == 0) {
                throw new NoDataException(LocalizedFormats.POLYNOMIAL);
            }
            double absoluteAccuracy = LaguerreSolver.this.getAbsoluteAccuracy();
            double relativeAccuracy = LaguerreSolver.this.getRelativeAccuracy();
            double functionValueAccuracy = LaguerreSolver.this.getFunctionValueAccuracy();
            Complex nC = new Complex(n2, 0.0d);
            Complex n1C = new Complex(n2 - 1, 0.0d);
            Complex z2 = initial;
            Complex oldz = new Complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
            while (true) {
                Complex pv = coefficients[n2];
                Complex dv = Complex.ZERO;
                Complex d2v = Complex.ZERO;
                for (int j2 = n2 - 1; j2 >= 0; j2--) {
                    d2v = dv.add(z2.multiply(d2v));
                    dv = pv.add(z2.multiply(dv));
                    pv = coefficients[j2].add(z2.multiply(pv));
                }
                Complex d2v2 = d2v.multiply(new Complex(2.0d, 0.0d));
                double tolerance = FastMath.max(relativeAccuracy * z2.abs(), absoluteAccuracy);
                if (z2.subtract(oldz).abs() <= tolerance) {
                    return z2;
                }
                if (pv.abs() <= functionValueAccuracy) {
                    return z2;
                }
                Complex G2 = dv.divide(pv);
                Complex G22 = G2.multiply(G2);
                Complex H2 = G22.subtract(d2v2.divide(pv));
                Complex delta = n1C.multiply(nC.multiply(H2).subtract(G22));
                Complex deltaSqrt = delta.sqrt();
                Complex dplus = G2.add(deltaSqrt);
                Complex dminus = G2.subtract(deltaSqrt);
                Complex denominator = dplus.abs() > dminus.abs() ? dplus : dminus;
                if (denominator.equals(new Complex(0.0d, 0.0d))) {
                    z2 = z2.add(new Complex(absoluteAccuracy, absoluteAccuracy));
                    oldz = new Complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY);
                } else {
                    oldz = z2;
                    z2 = z2.subtract(nC.divide(denominator));
                }
                LaguerreSolver.this.incrementEvaluationCount();
            }
        }
    }
}
