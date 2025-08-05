package org.apache.commons.math3.analysis.function;

import org.apache.commons.math3.analysis.DifferentiableUnivariateFunction;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.analysis.differentiation.DerivativeStructure;
import org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/function/Sinc.class */
public class Sinc implements UnivariateDifferentiableFunction, DifferentiableUnivariateFunction {
    private static final double SHORTCUT = 0.006d;
    private final boolean normalized;

    public Sinc() {
        this(false);
    }

    public Sinc(boolean normalized) {
        this.normalized = normalized;
    }

    @Override // org.apache.commons.math3.analysis.UnivariateFunction
    public double value(double x2) {
        double scaledX = this.normalized ? 3.141592653589793d * x2 : x2;
        if (FastMath.abs(scaledX) <= SHORTCUT) {
            double scaledX2 = scaledX * scaledX;
            return (((scaledX2 - 20.0d) * scaledX2) + 120.0d) / 120.0d;
        }
        return FastMath.sin(scaledX) / scaledX;
    }

    @Override // org.apache.commons.math3.analysis.DifferentiableUnivariateFunction
    @Deprecated
    public UnivariateFunction derivative() {
        return FunctionUtils.toDifferentiableUnivariateFunction(this).derivative();
    }

    @Override // org.apache.commons.math3.analysis.differentiation.UnivariateDifferentiableFunction
    public DerivativeStructure value(DerivativeStructure t2) throws DimensionMismatchException {
        int kStart;
        double scaledX = (this.normalized ? 3.141592653589793d : 1.0d) * t2.getValue();
        double scaledX2 = scaledX * scaledX;
        double[] f2 = new double[t2.getOrder() + 1];
        if (FastMath.abs(scaledX) <= SHORTCUT) {
            for (int i2 = 0; i2 < f2.length; i2++) {
                int k2 = i2 / 2;
                if ((i2 & 1) == 0) {
                    f2[i2] = ((k2 & 1) == 0 ? 1 : -1) * ((1.0d / (i2 + 1)) - (scaledX2 * ((1.0d / ((2 * i2) + 6)) - (scaledX2 / ((24 * i2) + 120)))));
                } else {
                    f2[i2] = ((k2 & 1) == 0 ? -scaledX : scaledX) * ((1.0d / (i2 + 2)) - (scaledX2 * ((1.0d / ((6 * i2) + 24)) - (scaledX2 / ((120 * i2) + 720)))));
                }
            }
        } else {
            double inv = 1.0d / scaledX;
            double cos = FastMath.cos(scaledX);
            double sin = FastMath.sin(scaledX);
            f2[0] = inv * sin;
            double[] sc = new double[f2.length];
            sc[0] = 1.0d;
            double coeff = inv;
            for (int n2 = 1; n2 < f2.length; n2++) {
                double s2 = 0.0d;
                double c2 = 0.0d;
                if ((n2 & 1) == 0) {
                    sc[n2] = 0.0d;
                    kStart = n2;
                } else {
                    sc[n2] = sc[n2 - 1];
                    c2 = sc[n2];
                    kStart = n2 - 1;
                }
                for (int k3 = kStart; k3 > 1; k3 -= 2) {
                    sc[k3] = ((k3 - n2) * sc[k3]) - sc[k3 - 1];
                    s2 = (s2 * scaledX2) + sc[k3];
                    sc[k3 - 1] = (((k3 - 1) - n2) * sc[k3 - 1]) + sc[k3 - 2];
                    c2 = (c2 * scaledX2) + sc[k3 - 1];
                }
                sc[0] = sc[0] * (-n2);
                coeff *= inv;
                f2[n2] = coeff * ((((s2 * scaledX2) + sc[0]) * sin) + (c2 * scaledX * cos));
            }
        }
        if (this.normalized) {
            double scale = 3.141592653589793d;
            for (int i3 = 1; i3 < f2.length; i3++) {
                int i4 = i3;
                f2[i4] = f2[i4] * scale;
                scale *= 3.141592653589793d;
            }
        }
        return t2.compose(f2);
    }
}
