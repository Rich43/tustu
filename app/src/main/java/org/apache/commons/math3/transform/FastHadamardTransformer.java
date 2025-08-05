package org.apache.commons.math3.transform;

import java.io.Serializable;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.ArithmeticUtils;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/transform/FastHadamardTransformer.class */
public class FastHadamardTransformer implements RealTransformer, Serializable {
    static final long serialVersionUID = 20120211;

    @Override // org.apache.commons.math3.transform.RealTransformer
    public double[] transform(double[] f2, TransformType type) {
        if (type == TransformType.FORWARD) {
            return fht(f2);
        }
        return TransformUtils.scaleArray(fht(f2), 1.0d / f2.length);
    }

    @Override // org.apache.commons.math3.transform.RealTransformer
    public double[] transform(UnivariateFunction f2, double min, double max, int n2, TransformType type) {
        return transform(FunctionUtils.sample(f2, min, max, n2), type);
    }

    public int[] transform(int[] f2) {
        return fht(f2);
    }

    protected double[] fht(double[] x2) throws MathIllegalArgumentException {
        int n2 = x2.length;
        int halfN = n2 / 2;
        if (!ArithmeticUtils.isPowerOfTwo(n2)) {
            throw new MathIllegalArgumentException(LocalizedFormats.NOT_POWER_OF_TWO, Integer.valueOf(n2));
        }
        double[] yPrevious = new double[n2];
        double[] yCurrent = (double[]) x2.clone();
        int i2 = 1;
        while (true) {
            int j2 = i2;
            if (j2 < n2) {
                double[] yTmp = yCurrent;
                yCurrent = yPrevious;
                yPrevious = yTmp;
                for (int i3 = 0; i3 < halfN; i3++) {
                    int twoI = 2 * i3;
                    yCurrent[i3] = yPrevious[twoI] + yPrevious[twoI + 1];
                }
                for (int i4 = halfN; i4 < n2; i4++) {
                    int twoI2 = 2 * i4;
                    yCurrent[i4] = yPrevious[twoI2 - n2] - yPrevious[(twoI2 - n2) + 1];
                }
                i2 = j2 << 1;
            } else {
                return yCurrent;
            }
        }
    }

    protected int[] fht(int[] x2) throws MathIllegalArgumentException {
        int n2 = x2.length;
        int halfN = n2 / 2;
        if (!ArithmeticUtils.isPowerOfTwo(n2)) {
            throw new MathIllegalArgumentException(LocalizedFormats.NOT_POWER_OF_TWO, Integer.valueOf(n2));
        }
        int[] yPrevious = new int[n2];
        int[] yCurrent = (int[]) x2.clone();
        int i2 = 1;
        while (true) {
            int j2 = i2;
            if (j2 < n2) {
                int[] yTmp = yCurrent;
                yCurrent = yPrevious;
                yPrevious = yTmp;
                for (int i3 = 0; i3 < halfN; i3++) {
                    int twoI = 2 * i3;
                    yCurrent[i3] = yPrevious[twoI] + yPrevious[twoI + 1];
                }
                for (int i4 = halfN; i4 < n2; i4++) {
                    int twoI2 = 2 * i4;
                    yCurrent[i4] = yPrevious[twoI2 - n2] - yPrevious[(twoI2 - n2) + 1];
                }
                i2 = j2 << 1;
            } else {
                return yCurrent;
            }
        }
    }
}
