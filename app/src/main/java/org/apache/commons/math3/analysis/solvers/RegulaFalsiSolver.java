package org.apache.commons.math3.analysis.solvers;

import org.apache.commons.math3.analysis.solvers.BaseSecantSolver;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/solvers/RegulaFalsiSolver.class */
public class RegulaFalsiSolver extends BaseSecantSolver {
    public RegulaFalsiSolver() {
        super(1.0E-6d, BaseSecantSolver.Method.REGULA_FALSI);
    }

    public RegulaFalsiSolver(double absoluteAccuracy) {
        super(absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
    }

    public RegulaFalsiSolver(double relativeAccuracy, double absoluteAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
    }

    public RegulaFalsiSolver(double relativeAccuracy, double absoluteAccuracy, double functionValueAccuracy) {
        super(relativeAccuracy, absoluteAccuracy, functionValueAccuracy, BaseSecantSolver.Method.REGULA_FALSI);
    }
}
