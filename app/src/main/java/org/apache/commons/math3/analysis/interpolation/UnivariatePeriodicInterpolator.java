package org.apache.commons.math3.analysis.interpolation;

import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NumberIsTooSmallException;
import org.apache.commons.math3.util.MathArrays;
import org.apache.commons.math3.util.MathUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/analysis/interpolation/UnivariatePeriodicInterpolator.class */
public class UnivariatePeriodicInterpolator implements UnivariateInterpolator {
    public static final int DEFAULT_EXTEND = 5;
    private final UnivariateInterpolator interpolator;
    private final double period;
    private final int extend;

    public UnivariatePeriodicInterpolator(UnivariateInterpolator interpolator, double period, int extend) {
        this.interpolator = interpolator;
        this.period = period;
        this.extend = extend;
    }

    public UnivariatePeriodicInterpolator(UnivariateInterpolator interpolator, double period) {
        this(interpolator, period, 5);
    }

    /* JADX WARN: Type inference failed for: r1v11, types: [double[], double[][]] */
    @Override // org.apache.commons.math3.analysis.interpolation.UnivariateInterpolator
    public UnivariateFunction interpolate(double[] xval, double[] yval) throws MathIllegalArgumentException {
        if (xval.length < this.extend) {
            throw new NumberIsTooSmallException(Integer.valueOf(xval.length), Integer.valueOf(this.extend), true);
        }
        MathArrays.checkOrder(xval);
        final double offset = xval[0];
        int len = xval.length + (this.extend * 2);
        double[] x2 = new double[len];
        double[] y2 = new double[len];
        for (int i2 = 0; i2 < xval.length; i2++) {
            int index = i2 + this.extend;
            x2[index] = MathUtils.reduce(xval[i2], this.period, offset);
            y2[index] = yval[i2];
        }
        for (int i3 = 0; i3 < this.extend; i3++) {
            int index2 = (xval.length - this.extend) + i3;
            x2[i3] = MathUtils.reduce(xval[index2], this.period, offset) - this.period;
            y2[i3] = yval[index2];
            int index3 = (len - this.extend) + i3;
            x2[index3] = MathUtils.reduce(xval[i3], this.period, offset) + this.period;
            y2[index3] = yval[i3];
        }
        MathArrays.sortInPlace(x2, new double[]{y2});
        final UnivariateFunction f2 = this.interpolator.interpolate(x2, y2);
        return new UnivariateFunction() { // from class: org.apache.commons.math3.analysis.interpolation.UnivariatePeriodicInterpolator.1
            @Override // org.apache.commons.math3.analysis.UnivariateFunction
            public double value(double x3) throws MathIllegalArgumentException {
                return f2.value(MathUtils.reduce(x3, UnivariatePeriodicInterpolator.this.period, offset));
            }
        };
    }
}
