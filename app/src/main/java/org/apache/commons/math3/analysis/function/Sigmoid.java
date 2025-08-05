package org.apache.commons.math3.analysis.function;

import java.util.Arrays;
import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/function/Sigmoid.class */
public class Sigmoid implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction {
    private final double lo;
    private final double hi;

    public Sigmoid() {
        this(0.0d, 1.0d);
    }

    public Sigmoid(double lo, double hi) {
        this.lo = lo;
        this.hi = hi;
    }

    @Override // org.apache.commons.math3.analysis.DifferentiableUnivariateFunction
    @Deprecated
    public UnivariateFunction derivative() {
        return FunctionUtils.toDifferentiableUnivariateFunction(this).derivative();
    }

    @Override // org.apache.commons.math3.analysis.UnivariateFunction
    public double value(double x2) {
        return value(x2, this.lo, this.hi);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/function/Sigmoid$Parametric.class */
    public static class Parametric implements ParametricUnivariateFunction {
        @Override // org.apache.commons.math3.analysis.ParametricUnivariateFunction
        public double value(double x2, double... param) throws NullArgumentException, DimensionMismatchException {
            validateParameters(param);
            return Sigmoid.value(x2, param[0], param[1]);
        }

        @Override // org.apache.commons.math3.analysis.ParametricUnivariateFunction
        public double[] gradient(double x2, double... param) throws NullArgumentException, DimensionMismatchException {
            validateParameters(param);
            double invExp1 = 1.0d / (1.0d + FastMath.exp(-x2));
            return new double[]{1.0d - invExp1, invExp1};
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
    public static double value(double x2, double lo, double hi) {
        return lo + ((hi - lo) / (1.0d + FastMath.exp(-x2)));
    }

    @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction
    public DerivativeStructure value(DerivativeStructure t2) throws DimensionMismatchException {
        double[] f2 = new double[t2.getOrder() + 1];
        double exp = FastMath.exp(-t2.getValue());
        if (Double.isInfinite(exp)) {
            f2[0] = this.lo;
            Arrays.fill(f2, 1, f2.length, 0.0d);
        } else {
            double[] p2 = new double[f2.length];
            double inv = 1.0d / (1.0d + exp);
            double coeff = this.hi - this.lo;
            for (int n2 = 0; n2 < f2.length; n2++) {
                double v2 = 0.0d;
                p2[n2] = 1.0d;
                for (int k2 = n2; k2 >= 0; k2--) {
                    v2 = (v2 * exp) + p2[k2];
                    if (k2 > 1) {
                        p2[k2 - 1] = (((n2 - k2) + 2) * p2[k2 - 2]) - ((k2 - 1) * p2[k2 - 1]);
                    } else {
                        p2[0] = 0.0d;
                    }
                }
                coeff *= inv;
                f2[n2] = coeff * v2;
            }
            f2[0] = f2[0] + this.lo;
        }
        return t2.compose(f2);
    }
}
