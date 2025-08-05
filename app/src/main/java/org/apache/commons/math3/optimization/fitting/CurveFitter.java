package org.apache.commons.math3.optimization.fitting;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.math3.analysis.DifferentiableMultivariateVectorFunction;
import org.apache.commons.math3.analysis.MultivariateMatrixFunction;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.MultivariateDifferentiableVectorFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.optimization.DifferentiableMultivariateVectorOptimizer;
import org.apache.commons.math3.optimization.MultivariateDifferentiableVectorOptimizer;
import org.apache.commons.math3.optimization.PointVectorValuePair;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/fitting/CurveFitter.class */
public class CurveFitter<T extends ParametricUnivariateFunction> {

    @Deprecated
    private final DifferentiableMultivariateVectorOptimizer oldOptimizer;
    private final MultivariateDifferentiableVectorOptimizer optimizer;
    private final List<WeightedObservedPoint> observations;

    @Deprecated
    public CurveFitter(DifferentiableMultivariateVectorOptimizer optimizer) {
        this.oldOptimizer = optimizer;
        this.optimizer = null;
        this.observations = new ArrayList();
    }

    public CurveFitter(MultivariateDifferentiableVectorOptimizer optimizer) {
        this.oldOptimizer = null;
        this.optimizer = optimizer;
        this.observations = new ArrayList();
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

    public double[] fit(int maxEval, T f2, double[] initialGuess) {
        PointVectorValuePair optimum;
        double[] target = new double[this.observations.size()];
        double[] weights = new double[this.observations.size()];
        int i2 = 0;
        for (WeightedObservedPoint point : this.observations) {
            target[i2] = point.getY();
            weights[i2] = point.getWeight();
            i2++;
        }
        if (this.optimizer == null) {
            optimum = this.oldOptimizer.optimize(maxEval, new OldTheoreticalValuesFunction(f2), target, weights, initialGuess);
        } else {
            optimum = this.optimizer.optimize(maxEval, new TheoreticalValuesFunction(f2), target, weights, initialGuess);
        }
        return optimum.getPointRef();
    }

    @Deprecated
    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/fitting/CurveFitter$OldTheoreticalValuesFunction.class */
    private class OldTheoreticalValuesFunction implements DifferentiableMultivariateVectorFunction {

        /* renamed from: f, reason: collision with root package name */
        private final ParametricUnivariateFunction f13077f;

        OldTheoreticalValuesFunction(ParametricUnivariateFunction f2) {
            this.f13077f = f2;
        }

        @Override // org.apache.commons.math3.analysis.DifferentiableMultivariateVectorFunction
        public MultivariateMatrixFunction jacobian() {
            return new MultivariateMatrixFunction() { // from class: org.apache.commons.math3.optimization.fitting.CurveFitter.OldTheoreticalValuesFunction.1
                /* JADX WARN: Type inference failed for: r0v5, types: [double[], double[][]] */
                @Override // org.apache.commons.math3.analysis.MultivariateMatrixFunction
                public double[][] value(double[] point) {
                    ?? r0 = new double[CurveFitter.this.observations.size()];
                    int i2 = 0;
                    for (WeightedObservedPoint observed : CurveFitter.this.observations) {
                        int i3 = i2;
                        i2++;
                        r0[i3] = OldTheoreticalValuesFunction.this.f13077f.gradient(observed.getX(), point);
                    }
                    return r0;
                }
            };
        }

        @Override // org.apache.commons.math3.analysis.MultivariateVectorFunction
        public double[] value(double[] point) {
            double[] values = new double[CurveFitter.this.observations.size()];
            int i2 = 0;
            for (WeightedObservedPoint observed : CurveFitter.this.observations) {
                int i3 = i2;
                i2++;
                values[i3] = this.f13077f.value(observed.getX(), point);
            }
            return values;
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/fitting/CurveFitter$TheoreticalValuesFunction.class */
    private class TheoreticalValuesFunction implements MultivariateDifferentiableVectorFunction {

        /* renamed from: f, reason: collision with root package name */
        private final ParametricUnivariateFunction f13078f;

        TheoreticalValuesFunction(ParametricUnivariateFunction f2) {
            this.f13078f = f2;
        }

        @Override // org.apache.commons.math3.analysis.MultivariateVectorFunction
        public double[] value(double[] point) {
            double[] values = new double[CurveFitter.this.observations.size()];
            int i2 = 0;
            for (WeightedObservedPoint observed : CurveFitter.this.observations) {
                int i3 = i2;
                i2++;
                values[i3] = this.f13078f.value(observed.getX(), point);
            }
            return values;
        }

        @Override // org.apache.commons.math3.analysis.differentiation.MultivariateDifferentiableVectorFunction
        public DerivativeStructure[] value(DerivativeStructure[] point) throws DimensionMismatchException {
            double[] parameters = new double[point.length];
            for (int k2 = 0; k2 < point.length; k2++) {
                parameters[k2] = point[k2].getValue();
            }
            DerivativeStructure[] values = new DerivativeStructure[CurveFitter.this.observations.size()];
            int i2 = 0;
            for (WeightedObservedPoint observed : CurveFitter.this.observations) {
                DerivativeStructure vi = new DerivativeStructure(point.length, 1, this.f13078f.value(observed.getX(), parameters));
                for (int k3 = 0; k3 < point.length; k3++) {
                    vi = vi.add(new DerivativeStructure(point.length, 1, k3, 0.0d));
                }
                int i3 = i2;
                i2++;
                values[i3] = vi;
            }
            return values;
        }
    }
}
