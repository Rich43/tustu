package org.apache.commons.math3.fitting.leastsquares;

import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;
import org.apache.commons.math3.optim.OptimizationProblem;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/leastsquares/LeastSquaresProblem.class */
public interface LeastSquaresProblem extends OptimizationProblem<Evaluation> {

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/leastsquares/LeastSquaresProblem$Evaluation.class */
    public interface Evaluation {
        RealMatrix getCovariances(double d2);

        RealVector getSigma(double d2);

        double getRMS();

        RealMatrix getJacobian();

        double getCost();

        RealVector getResiduals();

        RealVector getPoint();
    }

    RealVector getStart();

    int getObservationSize();

    int getParameterSize();

    Evaluation evaluate(RealVector realVector);
}
