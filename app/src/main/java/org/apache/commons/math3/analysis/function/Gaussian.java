package org.apache.commons.math3.analysis.function;

import java.util.Arrays;
import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.Precision;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/function/Gaussian.class */
public class Gaussian implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction {
    private final double mean;
    private final double is;
    private final double i2s2;
    private final double norm;

    public Gaussian(double norm, double mean, double sigma) throws NotStrictlyPositiveException {
        if (sigma <= 0.0d) {
            throw new NotStrictlyPositiveException(Double.valueOf(sigma));
        }
        this.norm = norm;
        this.mean = mean;
        this.is = 1.0d / sigma;
        this.i2s2 = 0.5d * this.is * this.is;
    }

    public Gaussian(double mean, double sigma) throws NotStrictlyPositiveException {
        this(1.0d / (sigma * FastMath.sqrt(6.283185307179586d)), mean, sigma);
    }

    public Gaussian() {
        this(0.0d, 1.0d);
    }

    @Override // org.apache.commons.math3.analysis.UnivariateFunction
    public double value(double x2) {
        return value(x2 - this.mean, this.norm, this.i2s2);
    }

    @Override // org.apache.commons.math3.analysis.DifferentiableUnivariateFunction
    @Deprecated
    public UnivariateFunction derivative() {
        return FunctionUtils.toDifferentiableUnivariateFunction(this).derivative();
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/function/Gaussian$Parametric.class */
    public static class Parametric implements ParametricUnivariateFunction {
        @Override // org.apache.commons.math3.analysis.ParametricUnivariateFunction
        public double value(double x2, double... param) throws NotStrictlyPositiveException, NullArgumentException, DimensionMismatchException {
            validateParameters(param);
            double diff = x2 - param[1];
            double i2s2 = 1.0d / ((2.0d * param[2]) * param[2]);
            return Gaussian.value(diff, param[0], i2s2);
        }

        @Override // org.apache.commons.math3.analysis.ParametricUnivariateFunction
        public double[] gradient(double x2, double... param) throws NotStrictlyPositiveException, NullArgumentException, DimensionMismatchException {
            validateParameters(param);
            double norm = param[0];
            double diff = x2 - param[1];
            double sigma = param[2];
            double i2s2 = 1.0d / ((2.0d * sigma) * sigma);
            double n2 = Gaussian.value(diff, 1.0d, i2s2);
            double m2 = norm * n2 * 2.0d * i2s2 * diff;
            double s2 = (m2 * diff) / sigma;
            return new double[]{n2, m2, s2};
        }

        private void validateParameters(double[] param) throws NotStrictlyPositiveException, NullArgumentException, DimensionMismatchException {
            if (param == null) {
                throw new NullArgumentException();
            }
            if (param.length != 3) {
                throw new DimensionMismatchException(param.length, 3);
            }
            if (param[2] <= 0.0d) {
                throw new NotStrictlyPositiveException(Double.valueOf(param[2]));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double value(double xMinusMean, double norm, double i2s2) {
        return norm * FastMath.exp((-xMinusMean) * xMinusMean * i2s2);
    }

    @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction
    public DerivativeStructure value(DerivativeStructure t2) throws DimensionMismatchException {
        double u2 = this.is * (t2.getValue() - this.mean);
        double[] f2 = new double[t2.getOrder() + 1];
        double[] p2 = new double[f2.length];
        p2[0] = 1.0d;
        double u22 = u2 * u2;
        double coeff = this.norm * FastMath.exp((-0.5d) * u22);
        if (coeff <= Precision.SAFE_MIN) {
            Arrays.fill(f2, 0.0d);
        } else {
            f2[0] = coeff;
            for (int n2 = 1; n2 < f2.length; n2++) {
                double v2 = 0.0d;
                p2[n2] = -p2[n2 - 1];
                for (int k2 = n2; k2 >= 0; k2 -= 2) {
                    v2 = (v2 * u22) + p2[k2];
                    if (k2 > 2) {
                        p2[k2 - 2] = ((k2 - 1) * p2[k2 - 1]) - p2[k2 - 3];
                    } else if (k2 == 2) {
                        p2[0] = p2[1];
                    }
                }
                if ((n2 & 1) == 1) {
                    v2 *= u2;
                }
                coeff *= this.is;
                f2[n2] = coeff * v2;
            }
        }
        return t2.compose(f2);
    }
}
