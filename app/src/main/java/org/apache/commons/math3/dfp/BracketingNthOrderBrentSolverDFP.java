package org.apache.commons.math3.dfp;

import org.apache.commons.math3.analysis.RealFieldUnivariateFunction;
import org.apache.commons.math3.analysis.solvers.AllowedSolution;
import org.apache.commons.math3.analysis.solvers.FieldBracketingNthOrderBrentSolver;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.MathUtils;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/dfp/BracketingNthOrderBrentSolverDFP.class */
public class BracketingNthOrderBrentSolverDFP extends FieldBracketingNthOrderBrentSolver<Dfp> {
    public BracketingNthOrderBrentSolverDFP(Dfp relativeAccuracy, Dfp absoluteAccuracy, Dfp functionValueAccuracy, int maximalOrder) throws NumberIsTooSmallException {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, maximalOrder);
    }

    @Override // org.apache.commons.math3.analysis.solvers.FieldBracketingNthOrderBrentSolver, org.apache.commons.math3.analysis.solvers.BracketedRealFieldUnivariateSolver
    public Dfp getAbsoluteAccuracy() {
        return (Dfp) super.getAbsoluteAccuracy();
    }

    @Override // org.apache.commons.math3.analysis.solvers.FieldBracketingNthOrderBrentSolver, org.apache.commons.math3.analysis.solvers.BracketedRealFieldUnivariateSolver
    public Dfp getRelativeAccuracy() {
        return (Dfp) super.getRelativeAccuracy();
    }

    @Override // org.apache.commons.math3.analysis.solvers.FieldBracketingNthOrderBrentSolver, org.apache.commons.math3.analysis.solvers.BracketedRealFieldUnivariateSolver
    public Dfp getFunctionValueAccuracy() {
        return (Dfp) super.getFunctionValueAccuracy();
    }

    public Dfp solve(int maxEval, UnivariateDfpFunction f2, Dfp min, Dfp max, AllowedSolution allowedSolution) throws NullArgumentException, NoBracketingException {
        return solve(maxEval, f2, min, max, min.add(max).divide(2), allowedSolution);
    }

    public Dfp solve(int maxEval, final UnivariateDfpFunction f2, Dfp min, Dfp max, Dfp startValue, AllowedSolution allowedSolution) throws NullArgumentException, NoBracketingException {
        MathUtils.checkNotNull(f2);
        RealFieldUnivariateFunction<Dfp> fieldF = new RealFieldUnivariateFunction<Dfp>() { // from class: org.apache.commons.math3.dfp.BracketingNthOrderBrentSolverDFP.1
            @Override // org.apache.commons.math3.analysis.RealFieldUnivariateFunction
            public Dfp value(Dfp x2) {
                return f2.value(x2);
            }
        };
        return solve(maxEval, fieldF, min, max, startValue, allowedSolution);
    }
}
