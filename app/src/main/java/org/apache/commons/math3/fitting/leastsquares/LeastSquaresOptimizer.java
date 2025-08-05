package org.apache.commons.math3.fitting.leastsquares;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/leastsquares/LeastSquaresOptimizer.class */
public interface LeastSquaresOptimizer {

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/leastsquares/LeastSquaresOptimizer$Optimum.class */
    public interface Optimum extends LeastSquaresProblem.Evaluation {
        int getEvaluations();

        int getIterations();
    }

    Optimum optimize(LeastSquaresProblem leastSquaresProblem);
}
