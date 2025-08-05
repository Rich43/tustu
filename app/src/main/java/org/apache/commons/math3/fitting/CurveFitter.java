package org.apache.commons.math3.fitting;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.TooManyEvaluationsException;
import org.apache.commons.math3.optim.InitialGuess;
import org.apache.commons.math3.optim.MaxEval;
import org.apache.commons.math3.optim.PointVectorValuePair;
import org.apache.commons.math3.optim.nonlinear.vector.ModelFunction;
import org.apache.commons.math3.optim.nonlinear.vector.ModelFunctionJacobian;
import org.apache.commons.math3.optim.nonlinear.vector.MultivariateVectorOptimizer;
import org.apache.commons.math3.optim.nonlinear.vector.Target;
import org.apache.commons.math3.optim.nonlinear.vector.Weight;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/CurveFitter.class */
public class CurveFitter<T extends ParametricUnivariateFunction> {
    private final MultivariateVectorOptimizer optimizer;
    private final List<WeightedObservedPoint> observations = new ArrayList();

    public CurveFitter(MultivariateVectorOptimizer optimizer) {
        this.optimizer = optimizer;
    }

    public void addObservedPoint(double x2, double y2) {
        addObservedPoint(1.0d, x2, y2);
    }

    public void addObservedPoint(double weight, double x2, double y2) {
        this.observations.add(new WeightedObservedPoint(weight, x2, y2));
    }

    public void addObservedPoint(WeightedObservedPoint observed) {
        this.observations.add(observed);
    }

    public WeightedObservedPoint[] getObservations() {
        return (WeightedObservedPoint[]) this.observations.toArray(new WeightedObservedPoint[this.observations.size()]);
    }

    public void clearObservations() {
        this.observations.clear();
    }

    public double[] fit(T f2, double[] initialGuess) {
        return fit(Integer.MAX_VALUE, f2, initialGuess);
    }

    public double[] fit(int maxEval, T f2, double[] initialGuess) throws TooManyEvaluationsException, DimensionMismatchException {
        double[] target = new double[this.observations.size()];
        double[] weights = new double[this.observations.size()];
        int i2 = 0;
        for (WeightedObservedPoint point : this.observations) {
            target[i2] = point.getY();
            weights[i2] = point.getWeight();
            i2++;
        }
        CurveFitter<T>.TheoreticalValuesFunction model = new TheoreticalValuesFunction(f2);
        PointVectorValuePair optimum = this.optimizer.optimize(new MaxEval(maxEval), model.getModelFunction(), model.getModelFunctionJacobian(), new Target(target), new Weight(weights), new InitialGuess(initialGuess));
        return optimum.getPointRef();
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/CurveFitter$TheoreticalValuesFunction.class */
    private class TheoreticalValuesFunction {

        /* renamed from: f, reason: collision with root package name */
        private final ParametricUnivariateFunction f12995f;

        TheoreticalValuesFunction(ParametricUnivariateFunction f2) {
            this.f12995f = f2;
        }

        public ModelFunction getModelFunction() {
            return new ModelFunction(new MultivariateVectorFunction() { // from class: org.apache.commons.math3.fitting.CurveFitter.TheoreticalValuesFunction.1
                @Override // org.apache.commons.math3.analysis.MultivariateVectorFunction
                public double[] value(double[] point) {
                    double[] values = new double[CurveFitter.this.observations.size()];
                    int i2 = 0;
                    for (WeightedObservedPoint observed : CurveFitter.this.observations) {
                        int i3 = i2;
                        i2++;
                        values[i3] = TheoreticalValuesFunction.this.f12995f.value(observed.getX(), point);
                    }
                    return values;
                }
            });
        }

        public ModelFunctionJacobian getModelFunctionJacobian() {
            return new ModelFunctionJacobian(new MultivariateMatrixFunction() { // from class: org.apache.commons.math3.fitting.CurveFitter.TheoreticalValuesFunction.2
                /* JADX WARN: Type inference failed for: r0v5, types: [double[], double[][]] */
                @Override // org.apache.commons.math3.analysis.MultivariateMatrixFunction
                public double[][] value(double[] point) {
                    ?? r0 = new double[CurveFitter.this.observations.size()];
                    int i2 = 0;
                    for (WeightedObservedPoint observed : CurveFitter.this.observations) {
                        int i3 = i2;
                        i2++;
                        r0[i3] = TheoreticalValuesFunction.this.f12995f.gradient(observed.getX(), point);
                    }
                    return r0;
                }
            });
        }
    }
}
