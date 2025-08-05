package org.apache.commons.math3.analysis.function;

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

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/function/Logistic.class */
public class Logistic implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction {

    /* renamed from: a, reason: collision with root package name */
    private final double f12959a;

    /* renamed from: k, reason: collision with root package name */
    private final double f12960k;

    /* renamed from: b, reason: collision with root package name */
    private final double f12961b;
    private final double oneOverN;

    /* renamed from: q, reason: collision with root package name */
    private final double f12962q;

    /* renamed from: m, reason: collision with root package name */
    private final double f12963m;

    public Logistic(double k2, double m2, double b2, double q2, double a2, double n2) throws NotStrictlyPositiveException {
        if (n2 <= 0.0d) {
            throw new NotStrictlyPositiveException(Double.valueOf(n2));
        }
        this.f12960k = k2;
        this.f12963m = m2;
        this.f12961b = b2;
        this.f12962q = q2;
        this.f12959a = a2;
        this.oneOverN = 1.0d / n2;
    }

    @Override // org.apache.commons.math3.analysis.UnivariateFunction
    public double value(double x2) {
        return value(this.f12963m - x2, this.f12960k, this.f12961b, this.f12962q, this.f12959a, this.oneOverN);
    }

    @Override // org.apache.commons.math3.analysis.DifferentiableUnivariateFunction
    @Deprecated
    public UnivariateFunction derivative() {
        return FunctionUtils.toDifferentiableUnivariateFunction(this).derivative();
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/function/Logistic$Parametric.class */
    public static class Parametric implements ParametricUnivariateFunction {
        @Override // org.apache.commons.math3.analysis.ParametricUnivariateFunction
        public double value(double x2, double... param) throws NotStrictlyPositiveException, NullArgumentException, DimensionMismatchException {
            validateParameters(param);
            return Logistic.value(param[1] - x2, param[0], param[2], param[3], param[4], 1.0d / param[5]);
        }

        @Override // org.apache.commons.math3.analysis.ParametricUnivariateFunction
        public double[] gradient(double x2, double... param) throws NotStrictlyPositiveException, NullArgumentException, DimensionMismatchException {
            validateParameters(param);
            double b2 = param[2];
            double q2 = param[3];
            double mMinusX = param[1] - x2;
            double oneOverN = 1.0d / param[5];
            double exp = FastMath.exp(b2 * mMinusX);
            double qExp = q2 * exp;
            double qExp1 = qExp + 1.0d;
            double factor1 = ((param[0] - param[4]) * oneOverN) / FastMath.pow(qExp1, oneOverN);
            double factor2 = (-factor1) / qExp1;
            double gk = Logistic.value(mMinusX, 1.0d, b2, q2, 0.0d, oneOverN);
            double gm = factor2 * b2 * qExp;
            double gb = factor2 * mMinusX * qExp;
            double gq = factor2 * exp;
            double ga = Logistic.value(mMinusX, 0.0d, b2, q2, 1.0d, oneOverN);
            double gn = factor1 * FastMath.log(qExp1) * oneOverN;
            return new double[]{gk, gm, gb, gq, ga, gn};
        }

        private void validateParameters(double[] param) throws NotStrictlyPositiveException, NullArgumentException, DimensionMismatchException {
            if (param == null) {
                throw new NullArgumentException();
            }
            if (param.length != 6) {
                throw new DimensionMismatchException(param.length, 6);
            }
            if (param[5] <= 0.0d) {
                throw new NotStrictlyPositiveException(Double.valueOf(param[5]));
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double value(double mMinusX, double k2, double b2, double q2, double a2, double oneOverN) {
        return a2 + ((k2 - a2) / FastMath.pow(1.0d + (q2 * FastMath.exp(b2 * mMinusX)), oneOverN));
    }

    @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction
    public DerivativeStructure value(DerivativeStructure t2) {
        return t2.negate().add(this.f12963m).multiply(this.f12961b).exp().multiply(this.f12962q).add(1.0d).pow(this.oneOverN).reciprocal().multiply(this.f12960k - this.f12959a).add(this.f12959a);
    }
}
