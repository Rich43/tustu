package org.apache.commons.math3.transform;

import java.util.Arrays;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.util.LocalizedFormats;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/transform/TransformUtils.class */
public class TransformUtils {
    private static final int[] POWERS_OF_TWO = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824};

    private TransformUtils() {
    }

    public static double[] scaleArray(double[] f2, double d2) {
        for (int i2 = 0; i2 < f2.length; i2++) {
            int i3 = i2;
            f2[i3] = f2[i3] * d2;
        }
        return f2;
    }

    public static Complex[] scaleArray(Complex[] f2, double d2) {
        for (int i2 = 0; i2 < f2.length; i2++) {
            f2[i2] = new Complex(d2 * f2[i2].getReal(), d2 * f2[i2].getImaginary());
        }
        return f2;
    }

    public static double[][] createRealImaginaryArray(Complex[] dataC) {
        double[][] dataRI = new double[2][dataC.length];
        double[] dataR = dataRI[0];
        double[] dataI = dataRI[1];
        for (int i2 = 0; i2 < dataC.length; i2++) {
            Complex c2 = dataC[i2];
            dataR[i2] = c2.getReal();
            dataI[i2] = c2.getImaginary();
        }
        return dataRI;
    }

    public static Complex[] createComplexArray(double[][] dataRI) throws DimensionMismatchException {
        if (dataRI.length != 2) {
            throw new DimensionMismatchException(dataRI.length, 2);
        }
        double[] dataR = dataRI[0];
        double[] dataI = dataRI[1];
        if (dataR.length != dataI.length) {
            throw new DimensionMismatchException(dataI.length, dataR.length);
        }
        int n2 = dataR.length;
        Complex[] c2 = new Complex[n2];
        for (int i2 = 0; i2 < n2; i2++) {
            c2[i2] = new Complex(dataR[i2], dataI[i2]);
        }
        return c2;
    }

    public static int exactLog2(int n2) throws MathIllegalArgumentException {
        int index = Arrays.binarySearch(POWERS_OF_TWO, n2);
        if (index < 0) {
            throw new MathIllegalArgumentException(LocalizedFormats.NOT_POWER_OF_TWO_CONSIDER_PADDING, Integer.valueOf(n2));
        }
        return index;
    }
}
