package org.apache.commons.math3.complex;

import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/complex/ComplexUtils.class */
public class ComplexUtils {
    private ComplexUtils() {
    }

    public static Complex polar2Complex(double r2, double theta) throws MathIllegalArgumentException {
        if (r2 < 0.0d) {
            throw new MathIllegalArgumentException(LocalizedFormats.NEGATIVE_COMPLEX_MODULE, Double.valueOf(r2));
        }
        return new Complex(r2 * FastMath.cos(theta), r2 * FastMath.sin(theta));
    }

    public static Complex[] convertToComplex(double[] real) {
        Complex[] c2 = new Complex[real.length];
        for (int i2 = 0; i2 < real.length; i2++) {
            c2[i2] = new Complex(real[i2], 0.0d);
        }
        return c2;
    }
}
