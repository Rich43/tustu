package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/MultivariateFunctionPenaltyAdapter.class */
public class MultivariateFunctionPenaltyAdapter implements MultivariateFunction {
    private final MultivariateFunction bounded;
    private final double[] lower;
    private final double[] upper;
    private final double offset;
    private final double[] scale;

    public MultivariateFunctionPenaltyAdapter(MultivariateFunction bounded, double[] lower, double[] upper, double offset, double[] scale) throws NullArgumentException {
        MathUtils.checkNotNull(lower);
        MathUtils.checkNotNull(upper);
        MathUtils.checkNotNull(scale);
        if (lower.length != upper.length) {
            throw new DimensionMismatchException(lower.length, upper.length);
        }
        if (lower.length != scale.length) {
            throw new DimensionMismatchException(lower.length, scale.length);
        }
        for (int i2 = 0; i2 < lower.length; i2++) {
            if (upper[i2] < lower[i2]) {
                throw new NumberIsTooSmallException(Double.valueOf(upper[i2]), Double.valueOf(lower[i2]), true);
            }
        }
        this.bounded = bounded;
        this.lower = (double[]) lower.clone();
        this.upper = (double[]) upper.clone();
        this.offset = offset;
        this.scale = (double[]) scale.clone();
    }

    @Override // org.apache.commons.math3.analysis.MultivariateFunction
    public double value(double[] point) {
        double d2;
        for (int i2 = 0; i2 < this.scale.length; i2++) {
            if (point[i2] < this.lower[i2] || point[i2] > this.upper[i2]) {
                double sum = 0.0d;
                for (int j2 = i2; j2 < this.scale.length; j2++) {
                    if (point[j2] < this.lower[j2]) {
                        d2 = this.scale[j2] * (this.lower[j2] - point[j2]);
                    } else if (point[j2] > this.upper[j2]) {
                        d2 = this.scale[j2] * (point[j2] - this.upper[j2]);
                    } else {
                        d2 = 0.0d;
                    }
                    double overshoot = d2;
                    sum += FastMath.sqrt(overshoot);
                }
                return this.offset + sum;
            }
        }
        return this.bounded.value(point);
    }
}
