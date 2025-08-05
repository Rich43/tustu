package org.apache.commons.math3.analysis.function;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.ParametricUnivariateFunction;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/function/HarmonicOscillator.class */
public class HarmonicOscillator implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction {
    private final double amplitude;
    private final double omega;
    private final double phase;

    public HarmonicOscillator(double amplitude, double omega, double phase) {
        this.amplitude = amplitude;
        this.omega = omega;
        this.phase = phase;
    }

    @Override // org.apache.commons.math3.analysis.UnivariateFunction
    public double value(double x2) {
        return value((this.omega * x2) + this.phase, this.amplitude);
    }

    @Override // org.apache.commons.math3.analysis.DifferentiableUnivariateFunction
    @Deprecated
    public UnivariateFunction derivative() {
        return FunctionUtils.toDifferentiableUnivariateFunction(this).derivative();
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/function/HarmonicOscillator$Parametric.class */
    public static class Parametric implements ParametricUnivariateFunction {
        @Override // org.apache.commons.math3.analysis.ParametricUnivariateFunction
        public double value(double x2, double... param) throws NullArgumentException, DimensionMismatchException {
            validateParameters(param);
            return HarmonicOscillator.value((x2 * param[1]) + param[2], param[0]);
        }

        @Override // org.apache.commons.math3.analysis.ParametricUnivariateFunction
        public double[] gradient(double x2, double... param) throws NullArgumentException, DimensionMismatchException {
            validateParameters(param);
            double amplitude = param[0];
            double omega = param[1];
            double phase = param[2];
            double xTimesOmegaPlusPhase = (omega * x2) + phase;
            double a2 = HarmonicOscillator.value(xTimesOmegaPlusPhase, 1.0d);
            double p2 = (-amplitude) * FastMath.sin(xTimesOmegaPlusPhase);
            double w2 = p2 * x2;
            return new double[]{a2, w2, p2};
        }

        private void validateParameters(double[] param) throws NullArgumentException, DimensionMismatchException {
            if (param == null) {
                throw new NullArgumentException();
            }
            if (param.length != 3) {
                throw new DimensionMismatchException(param.length, 3);
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static double value(double xTimesOmegaPlusPhase, double amplitude) {
        return amplitude * FastMath.cos(xTimesOmegaPlusPhase);
    }

    @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction
    public DerivativeStructure value(DerivativeStructure t2) throws DimensionMismatchException {
        double x2 = t2.getValue();
        double[] f2 = new double[t2.getOrder() + 1];
        double alpha = (this.omega * x2) + this.phase;
        f2[0] = this.amplitude * FastMath.cos(alpha);
        if (f2.length > 1) {
            f2[1] = (-this.amplitude) * this.omega * FastMath.sin(alpha);
            double mo2 = (-this.omega) * this.omega;
            for (int i2 = 2; i2 < f2.length; i2++) {
                f2[i2] = mo2 * f2[i2 - 2];
            }
        }
        return t2.compose(f2);
    }
}
