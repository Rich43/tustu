package org.apache.commons.math3.transform;

import java.io.Serializable;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/transform/FastSineTransformer.class */
public class FastSineTransformer implements RealTransformer, Serializable {
    static final long serialVersionUID = 20120211;
    private final DstNormalization normalization;

    public FastSineTransformer(DstNormalization normalization) {
        this.normalization = normalization;
    }

    @Override // org.apache.commons.math3.transform.RealTransformer
    public double[] transform(double[] f2, TransformType type) {
        if (this.normalization == DstNormalization.ORTHOGONAL_DST_I) {
            double s2 = FastMath.sqrt(2.0d / f2.length);
            return TransformUtils.scaleArray(fst(f2), s2);
        }
        if (type == TransformType.FORWARD) {
            return fst(f2);
        }
        double s3 = 2.0d / f2.length;
        return TransformUtils.scaleArray(fst(f2), s3);
    }

    @Override // org.apache.commons.math3.transform.RealTransformer
    public double[] transform(UnivariateFunction f2, double min, double max, int n2, TransformType type) throws NotStrictlyPositiveException, NumberIsTooLargeException {
        double[] data = FunctionUtils.sample(f2, min, max, n2);
        data[0] = 0.0d;
        return transform(data, type);
    }

    protected double[] fst(double[] f2) throws MathIllegalArgumentException {
        double[] transformed = new double[f2.length];
        if (!ArithmeticUtils.isPowerOfTwo(f2.length)) {
            throw new MathIllegalArgumentException(LocalizedFormats.NOT_POWER_OF_TWO_CONSIDER_PADDING, Integer.valueOf(f2.length));
        }
        if (f2[0] != 0.0d) {
            throw new MathIllegalArgumentException(LocalizedFormats.FIRST_ELEMENT_NOT_ZERO, Double.valueOf(f2[0]));
        }
        int n2 = f2.length;
        if (n2 == 1) {
            transformed[0] = 0.0d;
            return transformed;
        }
        double[] x2 = new double[n2];
        x2[0] = 0.0d;
        x2[n2 >> 1] = 2.0d * f2[n2 >> 1];
        for (int i2 = 1; i2 < (n2 >> 1); i2++) {
            double a2 = FastMath.sin((i2 * 3.141592653589793d) / n2) * (f2[i2] + f2[n2 - i2]);
            double b2 = 0.5d * (f2[i2] - f2[n2 - i2]);
            x2[i2] = a2 + b2;
            x2[n2 - i2] = a2 - b2;
        }
        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] y2 = transformer.transform(x2, TransformType.FORWARD);
        transformed[0] = 0.0d;
        transformed[1] = 0.5d * y2[0].getReal();
        for (int i3 = 1; i3 < (n2 >> 1); i3++) {
            transformed[2 * i3] = -y2[i3].getImaginary();
            transformed[(2 * i3) + 1] = y2[i3].getReal() + transformed[(2 * i3) - 1];
        }
        return transformed;
    }
}
