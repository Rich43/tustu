package org.apache.commons.math3.optim.nonlinear.scalar;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.MultivariateVectorFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.linear.RealMatrix;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optim/nonlinear/scalar/LeastSquaresConverter.class */
public class LeastSquaresConverter implements MultivariateFunction {
    private final MultivariateVectorFunction function;
    private final double[] observations;
    private final double[] weights;
    private final RealMatrix scale;

    public LeastSquaresConverter(MultivariateVectorFunction function, double[] observations) {
        this.function = function;
        this.observations = (double[]) observations.clone();
        this.weights = null;
        this.scale = null;
    }

    public LeastSquaresConverter(MultivariateVectorFunction function, double[] observations, double[] weights) {
        if (observations.length != weights.length) {
            throw new DimensionMismatchException(observations.length, weights.length);
        }
        this.function = function;
        this.observations = (double[]) observations.clone();
        this.weights = (double[]) weights.clone();
        this.scale = null;
    }

    public LeastSquaresConverter(MultivariateVectorFunction function, double[] observations, RealMatrix scale) {
        if (observations.length != scale.getColumnDimension()) {
            throw new DimensionMismatchException(observations.length, scale.getColumnDimension());
        }
        this.function = function;
        this.observations = (double[]) observations.clone();
        this.weights = null;
        this.scale = scale.copy();
    }

    @Override // org.apache.commons.math3.analysis.MultivariateFunction
    public double value(double[] point) throws IllegalArgumentException {
        double[] residuals = this.function.value(point);
        if (residuals.length != this.observations.length) {
            throw new DimensionMismatchException(residuals.length, this.observations.length);
        }
        for (int i2 = 0; i2 < residuals.length; i2++) {
            int i3 = i2;
            residuals[i3] = residuals[i3] - this.observations[i2];
        }
        double sumSquares = 0.0d;
        if (this.weights != null) {
            for (int i4 = 0; i4 < residuals.length; i4++) {
                double ri = residuals[i4];
                sumSquares += this.weights[i4] * ri * ri;
            }
        } else if (this.scale != null) {
            double[] arr$ = this.scale.operate(residuals);
            for (double yi : arr$) {
                sumSquares += yi * yi;
            }
        } else {
            for (double ri2 : residuals) {
                sumSquares += ri2 * ri2;
            }
        }
        return sumSquares;
    }
}
