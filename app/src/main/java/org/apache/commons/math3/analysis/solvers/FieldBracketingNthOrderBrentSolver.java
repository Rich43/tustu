package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.Field;
import org.apache.commons.math3.RealFieldElement;
import org.apache.commons.math3.analysis.RealFieldUnivariateFunction;
import org.apache.commons.math3.exception.NoBracketingException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.IntegerSequence;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/FieldBracketingNthOrderBrentSolver.class */
public class FieldBracketingNthOrderBrentSolver<T extends RealFieldElement<T>> implements BracketedRealFieldUnivariateSolver<T> {
    private static final int MAXIMAL_AGING = 2;
    private final Field<T> field;
    private final int maximalOrder;
    private final T functionValueAccuracy;
    private final T absoluteAccuracy;
    private final T relativeAccuracy;
    private IntegerSequence.Incrementor evaluations;

    public FieldBracketingNthOrderBrentSolver(T relativeAccuracy, T absoluteAccuracy, T functionValueAccuracy, int maximalOrder) throws NumberIsTooSmallException {
        if (maximalOrder < 2) {
            throw new NumberIsTooSmallException(Integer.valueOf(maximalOrder), 2, true);
        }
        this.field = relativeAccuracy.getField2();
        this.maximalOrder = maximalOrder;
        this.absoluteAccuracy = absoluteAccuracy;
        this.relativeAccuracy = relativeAccuracy;
        this.functionValueAccuracy = functionValueAccuracy;
        this.evaluations = IntegerSequence.Incrementor.create();
    }

    public int getMaximalOrder() {
        return this.maximalOrder;
    }

    @Override // org.apache.commons.math3.analysis.solvers.BracketedRealFieldUnivariateSolver
    public int getMaxEvaluations() {
        return this.evaluations.getMaximalCount();
    }

    @Override // org.apache.commons.math3.analysis.solvers.BracketedRealFieldUnivariateSolver
    public int getEvaluations() {
        return this.evaluations.getCount();
    }

    @Override // org.apache.commons.math3.analysis.solvers.BracketedRealFieldUnivariateSolver
    public T getAbsoluteAccuracy() {
        return this.absoluteAccuracy;
    }

    @Override // org.apache.commons.math3.analysis.solvers.BracketedRealFieldUnivariateSolver
    public T getRelativeAccuracy() {
        return this.relativeAccuracy;
    }

    @Override // org.apache.commons.math3.analysis.solvers.BracketedRealFieldUnivariateSolver
    public T getFunctionValueAccuracy() {
        return this.functionValueAccuracy;
    }

    @Override // org.apache.commons.math3.analysis.solvers.BracketedRealFieldUnivariateSolver
    public T solve(int i2, RealFieldUnivariateFunction<T> realFieldUnivariateFunction, T t2, T t3, AllowedSolution allowedSolution) throws NullArgumentException, NoBracketingException {
        return (T) solve(i2, realFieldUnivariateFunction, t2, t3, (RealFieldElement) ((RealFieldElement) t2.add(t3)).divide(2.0d), allowedSolution);
    }

    /* JADX WARN: Code restructure failed: missing block: B:112:?, code lost:
    
        return (T) r25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:113:?, code lost:
    
        return (T) r25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:114:?, code lost:
    
        return (T) r30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0256, code lost:
    
        switch(org.apache.commons.math3.analysis.solvers.FieldBracketingNthOrderBrentSolver.AnonymousClass1.$SwitchMap$org$apache$commons$math3$analysis$solvers$AllowedSolution[r17.ordinal()]) {
            case 1: goto L37;
            case 2: goto L42;
            case 3: goto L44;
            case 4: goto L46;
            case 5: goto L51;
            default: goto L56;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x028b, code lost:
    
        if (((org.apache.commons.math3.RealFieldElement) r28.subtract(r33)).getReal() >= 0.0d) goto L40;
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x0295, code lost:
    
        return (T) r30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x0298, code lost:
    
        return (T) r25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x029b, code lost:
    
        return (T) r30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x02a5, code lost:
    
        if (r26.getReal() > 0.0d) goto L49;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x02af, code lost:
    
        return (T) r30;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x02b9, code lost:
    
        if (r26.getReal() >= 0.0d) goto L54;
     */
    /* JADX WARN: Code restructure failed: missing block: B:55:0x02c3, code lost:
    
        return (T) r25;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x02cc, code lost:
    
        throw new org.apache.commons.math3.exception.MathInternalError(null);
     */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v143, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r0v222, types: [org.apache.commons.math3.RealFieldElement] */
    @Override // org.apache.commons.math3.analysis.solvers.BracketedRealFieldUnivariateSolver
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public T solve(int r12, org.apache.commons.math3.analysis.RealFieldUnivariateFunction<T> r13, T r14, T r15, T r16, org.apache.commons.math3.analysis.solvers.AllowedSolution r17) throws org.apache.commons.math3.exception.NullArgumentException, org.apache.commons.math3.exception.MaxCountExceededException, org.apache.commons.math3.exception.NoBracketingException {
        /*
            Method dump skipped, instructions count: 1247
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.analysis.solvers.FieldBracketingNthOrderBrentSolver.solve(int, org.apache.commons.math3.analysis.RealFieldUnivariateFunction, org.apache.commons.math3.RealFieldElement, org.apache.commons.math3.RealFieldElement, org.apache.commons.math3.RealFieldElement, org.apache.commons.math3.analysis.solvers.AllowedSolution):org.apache.commons.math3.RealFieldElement");
    }

    /* renamed from: org.apache.commons.math3.analysis.solvers.FieldBracketingNthOrderBrentSolver$1, reason: invalid class name */
    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/FieldBracketingNthOrderBrentSolver$1.class */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$org$apache$commons$math3$analysis$solvers$AllowedSolution = new int[AllowedSolution.values().length];

        static {
            try {
                $SwitchMap$org$apache$commons$math3$analysis$solvers$AllowedSolution[AllowedSolution.ANY_SIDE.ordinal()] = 1;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$apache$commons$math3$analysis$solvers$AllowedSolution[AllowedSolution.LEFT_SIDE.ordinal()] = 2;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$apache$commons$math3$analysis$solvers$AllowedSolution[AllowedSolution.RIGHT_SIDE.ordinal()] = 3;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$apache$commons$math3$analysis$solvers$AllowedSolution[AllowedSolution.BELOW_SIDE.ordinal()] = 4;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$apache$commons$math3$analysis$solvers$AllowedSolution[AllowedSolution.ABOVE_SIDE.ordinal()] = 5;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v11, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r0v13, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r2v4, types: [org.apache.commons.math3.RealFieldElement] */
    /* JADX WARN: Type inference failed for: r3v4, types: [java.lang.Object] */
    private T guessX(T targetY, T[] tArr, T[] y2, int start, int end) {
        for (int i2 = start; i2 < end - 1; i2++) {
            int delta = (i2 + 1) - start;
            for (int j2 = end - 1; j2 > i2; j2--) {
                tArr[j2] = (RealFieldElement) ((RealFieldElement) tArr[j2].subtract(tArr[j2 - 1])).divide((RealFieldElement) y2[j2].subtract(y2[j2 - delta]));
            }
        }
        T x0 = this.field.getZero();
        for (int j3 = end - 1; j3 >= start; j3--) {
            x0 = (RealFieldElement) tArr[j3].add(x0.multiply(targetY.subtract(y2[j3])));
        }
        return x0;
    }
}
