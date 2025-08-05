package org.apache.commons.math3.optimization.direct;

import org.apache.commons.math3.analysis.MultivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.function.Logit;
import org.apache.commons.math3.analysis.function.Sigmoid;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathUtils;

@Deprecated
/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/MultivariateFunctionMappingAdapter.class */
public class MultivariateFunctionMappingAdapter implements MultivariateFunction {
    private final MultivariateFunction bounded;
    private final Mapper[] mappers;

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/MultivariateFunctionMappingAdapter$Mapper.class */
    private interface Mapper {
        double unboundedToBounded(double d2);

        double boundedToUnbounded(double d2);
    }

    public MultivariateFunctionMappingAdapter(MultivariateFunction bounded, double[] lower, double[] upper) throws NullArgumentException {
        MathUtils.checkNotNull(lower);
        MathUtils.checkNotNull(upper);
        if (lower.length != upper.length) {
            throw new DimensionMismatchException(lower.length, upper.length);
        }
        for (int i2 = 0; i2 < lower.length; i2++) {
            if (upper[i2] < lower[i2]) {
                throw new NumberIsTooSmallException(Double.valueOf(upper[i2]), Double.valueOf(lower[i2]), true);
            }
        }
        this.bounded = bounded;
        this.mappers = new Mapper[lower.length];
        for (int i3 = 0; i3 < this.mappers.length; i3++) {
            if (Double.isInfinite(lower[i3])) {
                if (Double.isInfinite(upper[i3])) {
                    this.mappers[i3] = new NoBoundsMapper();
                } else {
                    this.mappers[i3] = new UpperBoundMapper(upper[i3]);
                }
            } else if (Double.isInfinite(upper[i3])) {
                this.mappers[i3] = new LowerBoundMapper(lower[i3]);
            } else {
                this.mappers[i3] = new LowerUpperBoundMapper(lower[i3], upper[i3]);
            }
        }
    }

    public double[] unboundedToBounded(double[] point) {
        double[] mapped = new double[this.mappers.length];
        for (int i2 = 0; i2 < this.mappers.length; i2++) {
            mapped[i2] = this.mappers[i2].unboundedToBounded(point[i2]);
        }
        return mapped;
    }

    public double[] boundedToUnbounded(double[] point) {
        double[] mapped = new double[this.mappers.length];
        for (int i2 = 0; i2 < this.mappers.length; i2++) {
            mapped[i2] = this.mappers[i2].boundedToUnbounded(point[i2]);
        }
        return mapped;
    }

    @Override // org.apache.commons.math3.analysis.MultivariateFunction
    public double value(double[] point) {
        return this.bounded.value(unboundedToBounded(point));
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/MultivariateFunctionMappingAdapter$NoBoundsMapper.class */
    private static class NoBoundsMapper implements Mapper {
        NoBoundsMapper() {
        }

        @Override // org.apache.commons.math3.optimization.direct.MultivariateFunctionMappingAdapter.Mapper
        public double unboundedToBounded(double y2) {
            return y2;
        }

        @Override // org.apache.commons.math3.optimization.direct.MultivariateFunctionMappingAdapter.Mapper
        public double boundedToUnbounded(double x2) {
            return x2;
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/MultivariateFunctionMappingAdapter$LowerBoundMapper.class */
    private static class LowerBoundMapper implements Mapper {
        private final double lower;

        LowerBoundMapper(double lower) {
            this.lower = lower;
        }

        @Override // org.apache.commons.math3.optimization.direct.MultivariateFunctionMappingAdapter.Mapper
        public double unboundedToBounded(double y2) {
            return this.lower + FastMath.exp(y2);
        }

        @Override // org.apache.commons.math3.optimization.direct.MultivariateFunctionMappingAdapter.Mapper
        public double boundedToUnbounded(double x2) {
            return FastMath.log(x2 - this.lower);
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/MultivariateFunctionMappingAdapter$UpperBoundMapper.class */
    private static class UpperBoundMapper implements Mapper {
        private final double upper;

        UpperBoundMapper(double upper) {
            this.upper = upper;
        }

        @Override // org.apache.commons.math3.optimization.direct.MultivariateFunctionMappingAdapter.Mapper
        public double unboundedToBounded(double y2) {
            return this.upper - FastMath.exp(-y2);
        }

        @Override // org.apache.commons.math3.optimization.direct.MultivariateFunctionMappingAdapter.Mapper
        public double boundedToUnbounded(double x2) {
            return -FastMath.log(this.upper - x2);
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/optimization/direct/MultivariateFunctionMappingAdapter$LowerUpperBoundMapper.class */
    private static class LowerUpperBoundMapper implements Mapper {
        private final UnivariateFunction boundingFunction;
        private final UnivariateFunction unboundingFunction;

        LowerUpperBoundMapper(double lower, double upper) {
            this.boundingFunction = new Sigmoid(lower, upper);
            this.unboundingFunction = new Logit(lower, upper);
        }

        @Override // org.apache.commons.math3.optimization.direct.MultivariateFunctionMappingAdapter.Mapper
        public double unboundedToBounded(double y2) {
            return this.boundingFunction.value(y2);
        }

        @Override // org.apache.commons.math3.optimization.direct.MultivariateFunctionMappingAdapter.Mapper
        public double boundedToUnbounded(double x2) {
            return this.unboundingFunction.value(x2);
        }
    }
}
