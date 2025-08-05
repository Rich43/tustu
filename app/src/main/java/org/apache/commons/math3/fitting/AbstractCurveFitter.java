package org.apache.commons.math3.fitting;

import java.util.Collection;
import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer;
import org.apache.commons.math3.fitting.leastsquares.LeastSquaresProblem;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/AbstractCurveFitter.class */
public abstract class AbstractCurveFitter {
    protected abstract LeastSquaresProblem getProblem(Collection<WeightedObservedPoint> collection);

    public double[] fit(Collection<WeightedObservedPoint> points) {
        return getOptimizer().optimize(getProblem(points)).getPoint().toArray();
    }

    protected LeastSquaresOptimizer getOptimizer() {
        return new LevenbergMarquardtOptimizer();
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/fitting/AbstractCurveFitter$TheoreticalValuesFunction.class */
    protected static class TheoreticalValuesFunction {

        /* renamed from: f, reason: collision with root package name */
        private final ParametricUnivariateFunction f12994f;
        private final double[] points;

        public TheoreticalValuesFunction(ParametricUnivariateFunction f2, Collection<WeightedObservedPoint> observations) {
            this.f12994f = f2;
            int len = observations.size();
            this.points = new double[len];
            int i2 = 0;
            for (WeightedObservedPoint obs : observations) {
                int i3 = i2;
                i2++;
                this.points[i3] = obs.getX();
            }
        }

        public MultivariateVectorFunction getModelFunction() {
            return new MultivariateVectorFunction() { // from class: org.apache.commons.math3.fitting.AbstractCurveFitter.TheoreticalValuesFunction.1
                @Override // org.apache.commons.math3.analysis.MultivariateVectorFunction
                public double[] value(double[] p2) {
                    int len = TheoreticalValuesFunction.this.points.length;
                    double[] values = new double[len];
                    for (int i2 = 0; i2 < len; i2++) {
                        values[i2] = TheoreticalValuesFunction.this.f12994f.value(TheoreticalValuesFunction.this.points[i2], p2);
                    }
                    return values;
                }
            };
        }

        public MultivariateMatrixFunction getModelFunctionJacobian() {
            return new MultivariateMatrixFunction() { // from class: org.apache.commons.math3.fitting.AbstractCurveFitter.TheoreticalValuesFunction.2
                /* JADX WARN: Type inference failed for: r0v5, types: [double[], double[][]] */
                @Override // org.apache.commons.math3.analysis.MultivariateMatrixFunction
                public double[][] value(double[] p2) {
                    int len = TheoreticalValuesFunction.this.points.length;
                    ?? r0 = new double[len];
                    for (int i2 = 0; i2 < len; i2++) {
                        r0[i2] = TheoreticalValuesFunction.this.f12994f.gradient(TheoreticalValuesFunction.this.points[i2], p2);
                    }
                    return r0;
                }
            };
        }
    }
}
