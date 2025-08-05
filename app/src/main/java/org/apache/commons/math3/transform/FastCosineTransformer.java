package org.apache.commons.math3.transform;

import java.io.Serializable;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.FastMath;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/transform/FastCosineTransformer.class */
public class FastCosineTransformer implements RealTransformer, Serializable {
    static final long serialVersionUID = 20120212;
    private final DctNormalization normalization;

    public FastCosineTransformer(DctNormalization normalization) {
        this.normalization = normalization;
    }

    @Override // org.apache.commons.math3.transform.RealTransformer
    public double[] transform(double[] f2, TransformType type) throws MathIllegalArgumentException {
        double s1;
        if (type == TransformType.FORWARD) {
            if (this.normalization == DctNormalization.ORTHOGONAL_DCT_I) {
                double s2 = FastMath.sqrt(2.0d / (f2.length - 1));
                return TransformUtils.scaleArray(fct(f2), s2);
            }
            return fct(f2);
        }
        double s22 = 2.0d / (f2.length - 1);
        if (this.normalization == DctNormalization.ORTHOGONAL_DCT_I) {
            s1 = FastMath.sqrt(s22);
        } else {
            s1 = s22;
        }
        return TransformUtils.scaleArray(fct(f2), s1);
    }

    @Override // org.apache.commons.math3.transform.RealTransformer
    public double[] transform(UnivariateFunction f2, double min, double max, int n2, TransformType type) throws MathIllegalArgumentException {
        double[] data = FunctionUtils.sample(f2, min, max, n2);
        return transform(data, type);
    }

    protected double[] fct(double[] f2) throws MathIllegalArgumentException {
        double[] transformed = new double[f2.length];
        int n2 = f2.length - 1;
        if (!ArithmeticUtils.isPowerOfTwo(n2)) {
            throw new MathIllegalArgumentException(LocalizedFormats.NOT_POWER_OF_TWO_PLUS_ONE, Integer.valueOf(f2.length));
        }
        if (n2 == 1) {
            transformed[0] = 0.5d * (f2[0] + f2[1]);
            transformed[1] = 0.5d * (f2[0] - f2[1]);
            return transformed;
        }
        double[] x2 = new double[n2];
        x2[0] = 0.5d * (f2[0] + f2[n2]);
        x2[n2 >> 1] = f2[n2 >> 1];
        double t1 = 0.5d * (f2[0] - f2[n2]);
        for (int i2 = 1; i2 < (n2 >> 1); i2++) {
            double a2 = 0.5d * (f2[i2] + f2[n2 - i2]);
            double b2 = FastMath.sin((i2 * 3.141592653589793d) / n2) * (f2[i2] - f2[n2 - i2]);
            double c2 = FastMath.cos((i2 * 3.141592653589793d) / n2) * (f2[i2] - f2[n2 - i2]);
            x2[i2] = a2 - b2;
            x2[n2 - i2] = a2 + b2;
            t1 += c2;
        }
        FastFourierTransformer transformer = new FastFourierTransformer(DftNormalization.STANDARD);
        Complex[] y2 = transformer.transform(x2, TransformType.FORWARD);
        transformed[0] = y2[0].getReal();
        transformed[1] = t1;
        for (int i3 = 1; i3 < (n2 >> 1); i3++) {
            transformed[2 * i3] = y2[i3].getReal();
            transformed[(2 * i3) + 1] = transformed[(2 * i3) - 1] - y2[i3].getImaginary();
        }
        transformed[n2] = y2[n2 >> 1].getReal();
        return transformed;
    }
}
