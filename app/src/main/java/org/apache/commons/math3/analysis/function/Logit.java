package org.apache.commons.math3.analysis.function;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.OutOfRangeException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/function/Logit.class */
public class Logit implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction {
    private final double lo;
    private final double hi;

    public Logit() {
        this(0.0d, 1.0d);
    }

    public Logit(double lo, double hi) {
        this.lo = lo;
        this.hi = hi;
    }

    @Override // org.apache.commons.math3.analysis.UnivariateFunction
    public double value(double x2) throws OutOfRangeException {
        return value(x2, this.lo, this.hi);
    }

    @Override // org.apache.commons.math3.analysis.DifferentiableUnivariateFunction
    @Deprecated
    public UnivariateFunction derivative() {
        return FunctionUtils.toDifferentiableUnivariateFunction(this).derivative();
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/function/Logit$Parametric.class */
    public static class Parametric implements ParametricUnivariateFunction {
        @Override // org.apache.commons.math3.analysis.ParametricUnivariateFunction
        public double value(double x2, double... param) throws NullArgumentException, DimensionMismatchException {
            validateParameters(param);
            return Logit.value(x2, param[0], param[1]);
        }

        @Override // org.apache.commons.math3.analysis.ParametricUnivariateFunction
        public double[] gradient(double x2, double... param) throws NullArgumentException, DimensionMismatchException {
            validateParameters(param);
            double lo = param[0];
            double hi = param[1];
            return new double[]{1.0d / (lo - x2), 1.0d / (hi - x2)};
        }

        private void validateParameters(double[] param) throws NullArgumentException, DimensionMismatchException {
            if (param == null) {
                throw new NullArgumentException();
            }
            if (param.length != 2) {
                throw new DimensionMismatchException(param.length, 2);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double value(double x2, double lo, double hi) throws OutOfRangeException {
        if (x2 < lo || x2 > hi) {
            throw new OutOfRangeException(Double.valueOf(x2), Double.valueOf(lo), Double.valueOf(hi));
        }
        return FastMath.log((x2 - lo) / (hi - x2));
    }

    @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction
    public DerivativeStructure value(DerivativeStructure t2) throws OutOfRangeException {
        double x2 = t2.getValue();
        if (x2 < this.lo || x2 > this.hi) {
            throw new OutOfRangeException(Double.valueOf(x2), Double.valueOf(this.lo), Double.valueOf(this.hi));
        }
        double[] f2 = new double[t2.getOrder() + 1];
        f2[0] = FastMath.log((x2 - this.lo) / (this.hi - x2));
        if (Double.isInfinite(f2[0])) {
            if (f2.length > 1) {
                f2[1] = Double.POSITIVE_INFINITY;
            }
            for (int i2 = 2; i2 < f2.length; i2++) {
                f2[i2] = f2[i2 - 2];
            }
        } else {
            double invL = 1.0d / (x2 - this.lo);
            double xL = invL;
            double invH = 1.0d / (this.hi - x2);
            double xH = invH;
            for (int i3 = 1; i3 < f2.length; i3++) {
                f2[i3] = xL + xH;
                xL *= (-i3) * invL;
                xH *= i3 * invH;
            }
        }
        return t2.compose(f2);
    }
}
