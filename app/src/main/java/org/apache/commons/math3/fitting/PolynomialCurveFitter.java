package org.apache.commons.math3.fitting;

import java.util.Collection;
import org.apache.commons.math3.analysis.polynomials.PolynomialFunction;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.fitting.AbstractCurveFitter;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresBuilder;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.linear.DiagonalMatrix;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/PolynomialCurveFitter.class */
public class PolynomialCurveFitter extends AbstractCurveFitter {
    private static final PolynomialFunction.Parametric FUNCTION = new PolynomialFunction.Parametric();
    private final double[] initialGuess;
    private final int maxIter;

    private PolynomialCurveFitter(double[] initialGuess, int maxIter) {
        this.initialGuess = initialGuess;
        this.maxIter = maxIter;
    }

    public static PolynomialCurveFitter create(int degree) {
        return new PolynomialCurveFitter(new double[degree + 1], Integer.MAX_VALUE);
    }

    public PolynomialCurveFitter withStartPoint(double[] newStart) {
        return new PolynomialCurveFitter((double[]) newStart.clone(), this.maxIter);
    }

    public PolynomialCurveFitter withMaxIterations(int newMaxIter) {
        return new PolynomialCurveFitter(this.initialGuess, newMaxIter);
    }

    @Override // org.apache.commons.math3.fitting.AbstractCurveFitter
    protected LeastSquaresProblem getProblem(Collection<WeightedObservedPoint> observations) {
        int len = observations.size();
        double[] target = new double[len];
        double[] weights = new double[len];
        int i2 = 0;
        for (WeightedObservedPoint obs : observations) {
            target[i2] = obs.getY();
            weights[i2] = obs.getWeight();
            i2++;
        }
        AbstractCurveFitter.TheoreticalValuesFunction model = new AbstractCurveFitter.TheoreticalValuesFunction(FUNCTION, observations);
        if (this.initialGuess == null) {
            throw new MathInternalError();
        }
        return new LeastSquaresBuilder().maxEvaluations(Integer.MAX_VALUE).maxIterations(this.maxIter).start(this.initialGuess).target(target).weight(new DiagonalMatrix(weights)).model(model.getModelFunction(), model.getModelFunctionJacobian()).build();
    }
}
