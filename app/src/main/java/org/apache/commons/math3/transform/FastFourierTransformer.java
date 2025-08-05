package org.apache.commons.math3.transform;

import java.io.Serializable;
import java.lang.reflect.Array;
import org.apache.commons.math3.analysis.FunctionUtils;
import org.apache.commons.math3.analysis.UnivariateFunction;
import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathIllegalStateException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.util.ArithmeticUtils;
import org.apache.commons.math3.util.FastMath;
import org.apache.commons.math3.util.MathArrays;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/transform/FastFourierTransformer.class */
public class FastFourierTransformer implements Serializable {
    static final long serialVersionUID = 20120210;
    private static final double[] W_SUB_N_R;
    private static final double[] W_SUB_N_I;
    private final DftNormalization normalization;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !FastFourierTransformer.class.desiredAssertionStatus();
        W_SUB_N_R = new double[]{1.0d, -1.0d, 6.123233995736766E-17d, 0.7071067811865476d, 0.9238795325112867d, 0.9807852804032304d, 0.9951847266721969d, 0.9987954562051724d, 0.9996988186962042d, 0.9999247018391445d, 0.9999811752826011d, 0.9999952938095762d, 0.9999988234517019d, 0.9999997058628822d, 0.9999999264657179d, 0.9999999816164293d, 0.9999999954041073d, 0.9999999988510269d, 0.9999999997127567d, 0.9999999999281892d, 0.9999999999820472d, 0.9999999999955118d, 0.999999999998878d, 0.9999999999997194d, 0.9999999999999298d, 0.9999999999999825d, 0.9999999999999957d, 0.9999999999999989d, 0.9999999999999998d, 0.9999999999999999d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d, 1.0d};
        W_SUB_N_I = new double[]{2.4492935982947064E-16d, -1.2246467991473532E-16d, -1.0d, -0.7071067811865475d, -0.3826834323650898d, -0.19509032201612825d, -0.0980171403295606d, -0.049067674327418015d, -0.024541228522912288d, -0.012271538285719925d, -0.006135884649154475d, -0.003067956762965976d, -0.0015339801862847655d, -7.669903187427045E-4d, -3.8349518757139556E-4d, -1.917475973107033E-4d, -9.587379909597734E-5d, -4.793689960306688E-5d, -2.396844980841822E-5d, -1.1984224905069705E-5d, -5.9921124526424275E-6d, -2.996056226334661E-6d, -1.4980281131690111E-6d, -7.490140565847157E-7d, -3.7450702829238413E-7d, -1.8725351414619535E-7d, -9.362675707309808E-8d, -4.681337853654909E-8d, -2.340668926827455E-8d, -1.1703344634137277E-8d, -5.8516723170686385E-9d, -2.9258361585343192E-9d, -1.4629180792671596E-9d, -7.314590396335798E-10d, -3.657295198167899E-10d, -1.8286475990839495E-10d, -9.143237995419748E-11d, -4.571618997709874E-11d, -2.285809498854937E-11d, -1.1429047494274685E-11d, -5.714523747137342E-12d, -2.857261873568671E-12d, -1.4286309367843356E-12d, -7.143154683921678E-13d, -3.571577341960839E-13d, -1.7857886709804195E-13d, -8.928943354902097E-14d, -4.4644716774510487E-14d, -2.2322358387255243E-14d, -1.1161179193627622E-14d, -5.580589596813811E-15d, -2.7902947984069054E-15d, -1.3951473992034527E-15d, -6.975736996017264E-16d, -3.487868498008632E-16d, -1.743934249004316E-16d, -8.71967124502158E-17d, -4.35983562251079E-17d, -2.179917811255395E-17d, -1.0899589056276974E-17d, -5.449794528138487E-18d, -2.7248972640692436E-18d, -1.3624486320346218E-18d};
    }

    public FastFourierTransformer(DftNormalization normalization) {
        this.normalization = normalization;
    }

    private static void bitReversalShuffle2(double[] a2, double[] b2) {
        int k2;
        int n2 = a2.length;
        if (!$assertionsDisabled && b2.length != n2) {
            throw new AssertionError();
        }
        int halfOfN = n2 >> 1;
        int j2 = 0;
        for (int i2 = 0; i2 < n2; i2++) {
            if (i2 < j2) {
                double temp = a2[i2];
                a2[i2] = a2[j2];
                a2[j2] = temp;
                double temp2 = b2[i2];
                b2[i2] = b2[j2];
                b2[j2] = temp2;
            }
            int i3 = halfOfN;
            while (true) {
                k2 = i3;
                if (k2 > j2 || k2 <= 0) {
                    break;
                }
                j2 -= k2;
                i3 = k2 >> 1;
            }
            j2 += k2;
        }
    }

    private static void normalizeTransformedData(double[][] dataRI, DftNormalization normalization, TransformType type) {
        double[] dataR = dataRI[0];
        double[] dataI = dataRI[1];
        int n2 = dataR.length;
        if (!$assertionsDisabled && dataI.length != n2) {
            throw new AssertionError();
        }
        switch (normalization) {
            case STANDARD:
                if (type == TransformType.INVERSE) {
                    double scaleFactor = 1.0d / n2;
                    for (int i2 = 0; i2 < n2; i2++) {
                        int i3 = i2;
                        dataR[i3] = dataR[i3] * scaleFactor;
                        int i4 = i2;
                        dataI[i4] = dataI[i4] * scaleFactor;
                    }
                    return;
                }
                return;
            case UNITARY:
                double scaleFactor2 = 1.0d / FastMath.sqrt(n2);
                for (int i5 = 0; i5 < n2; i5++) {
                    int i6 = i5;
                    dataR[i6] = dataR[i6] * scaleFactor2;
                    int i7 = i5;
                    dataI[i7] = dataI[i7] * scaleFactor2;
                }
                return;
            default:
                throw new MathIllegalStateException();
        }
    }

    public static void transformInPlace(double[][] dataRI, DftNormalization normalization, TransformType type) {
        if (dataRI.length != 2) {
            throw new DimensionMismatchException(dataRI.length, 2);
        }
        double[] dataR = dataRI[0];
        double[] dataI = dataRI[1];
        if (dataR.length != dataI.length) {
            throw new DimensionMismatchException(dataI.length, dataR.length);
        }
        int n2 = dataR.length;
        if (!ArithmeticUtils.isPowerOfTwo(n2)) {
            throw new MathIllegalArgumentException(LocalizedFormats.NOT_POWER_OF_TWO_CONSIDER_PADDING, Integer.valueOf(n2));
        }
        if (n2 == 1) {
            return;
        }
        if (n2 == 2) {
            double srcR0 = dataR[0];
            double srcI0 = dataI[0];
            double srcR1 = dataR[1];
            double srcI1 = dataI[1];
            dataR[0] = srcR0 + srcR1;
            dataI[0] = srcI0 + srcI1;
            dataR[1] = srcR0 - srcR1;
            dataI[1] = srcI0 - srcI1;
            normalizeTransformedData(dataRI, normalization, type);
            return;
        }
        bitReversalShuffle2(dataR, dataI);
        if (type == TransformType.INVERSE) {
            for (int i0 = 0; i0 < n2; i0 += 4) {
                int i1 = i0 + 1;
                int i2 = i0 + 2;
                int i3 = i0 + 3;
                double srcR02 = dataR[i0];
                double srcI02 = dataI[i0];
                double srcR12 = dataR[i2];
                double srcI12 = dataI[i2];
                double srcR2 = dataR[i1];
                double srcI2 = dataI[i1];
                double srcR3 = dataR[i3];
                double srcI3 = dataI[i3];
                dataR[i0] = srcR02 + srcR12 + srcR2 + srcR3;
                dataI[i0] = srcI02 + srcI12 + srcI2 + srcI3;
                dataR[i1] = (srcR02 - srcR2) + (srcI3 - srcI12);
                dataI[i1] = (srcI02 - srcI2) + (srcR12 - srcR3);
                dataR[i2] = ((srcR02 - srcR12) + srcR2) - srcR3;
                dataI[i2] = ((srcI02 - srcI12) + srcI2) - srcI3;
                dataR[i3] = (srcR02 - srcR2) + (srcI12 - srcI3);
                dataI[i3] = (srcI02 - srcI2) + (srcR3 - srcR12);
            }
        } else {
            for (int i02 = 0; i02 < n2; i02 += 4) {
                int i12 = i02 + 1;
                int i22 = i02 + 2;
                int i32 = i02 + 3;
                double srcR03 = dataR[i02];
                double srcI03 = dataI[i02];
                double srcR13 = dataR[i22];
                double srcI13 = dataI[i22];
                double srcR22 = dataR[i12];
                double srcI22 = dataI[i12];
                double srcR32 = dataR[i32];
                double srcI32 = dataI[i32];
                dataR[i02] = srcR03 + srcR13 + srcR22 + srcR32;
                dataI[i02] = srcI03 + srcI13 + srcI22 + srcI32;
                dataR[i12] = (srcR03 - srcR22) + (srcI13 - srcI32);
                dataI[i12] = (srcI03 - srcI22) + (srcR32 - srcR13);
                dataR[i22] = ((srcR03 - srcR13) + srcR22) - srcR32;
                dataI[i22] = ((srcI03 - srcI13) + srcI22) - srcI32;
                dataR[i32] = (srcR03 - srcR22) + (srcI32 - srcI13);
                dataI[i32] = (srcI03 - srcI22) + (srcR13 - srcR32);
            }
        }
        int lastN0 = 4;
        int i4 = 2;
        while (true) {
            int lastLogN0 = i4;
            if (lastN0 < n2) {
                int n0 = lastN0 << 1;
                int logN0 = lastLogN0 + 1;
                double wSubN0R = W_SUB_N_R[logN0];
                double wSubN0I = W_SUB_N_I[logN0];
                if (type == TransformType.INVERSE) {
                    wSubN0I = -wSubN0I;
                }
                int i5 = 0;
                while (true) {
                    int destEvenStartIndex = i5;
                    if (destEvenStartIndex < n2) {
                        int destOddStartIndex = destEvenStartIndex + lastN0;
                        double wSubN0ToRR = 1.0d;
                        double wSubN0ToRI = 0.0d;
                        for (int r2 = 0; r2 < lastN0; r2++) {
                            double grR = dataR[destEvenStartIndex + r2];
                            double grI = dataI[destEvenStartIndex + r2];
                            double hrR = dataR[destOddStartIndex + r2];
                            double hrI = dataI[destOddStartIndex + r2];
                            dataR[destEvenStartIndex + r2] = (grR + (wSubN0ToRR * hrR)) - (wSubN0ToRI * hrI);
                            dataI[destEvenStartIndex + r2] = grI + (wSubN0ToRR * hrI) + (wSubN0ToRI * hrR);
                            dataR[destOddStartIndex + r2] = grR - ((wSubN0ToRR * hrR) - (wSubN0ToRI * hrI));
                            dataI[destOddStartIndex + r2] = grI - ((wSubN0ToRR * hrI) + (wSubN0ToRI * hrR));
                            double nextWsubN0ToRR = (wSubN0ToRR * wSubN0R) - (wSubN0ToRI * wSubN0I);
                            double nextWsubN0ToRI = (wSubN0ToRR * wSubN0I) + (wSubN0ToRI * wSubN0R);
                            wSubN0ToRR = nextWsubN0ToRR;
                            wSubN0ToRI = nextWsubN0ToRI;
                        }
                        i5 = destEvenStartIndex + n0;
                    }
                }
                lastN0 = n0;
                i4 = logN0;
            } else {
                normalizeTransformedData(dataRI, normalization, type);
                return;
            }
        }
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [double[], double[][]] */
    public Complex[] transform(double[] f2, TransformType type) {
        ?? r0 = {MathArrays.copyOf(f2, f2.length), new double[f2.length]};
        transformInPlace(r0, this.normalization, type);
        return TransformUtils.createComplexArray(r0);
    }

    public Complex[] transform(UnivariateFunction f2, double min, double max, int n2, TransformType type) throws NotStrictlyPositiveException, NumberIsTooLargeException {
        double[] data = FunctionUtils.sample(f2, min, max, n2);
        return transform(data, type);
    }

    public Complex[] transform(Complex[] f2, TransformType type) {
        double[][] dataRI = TransformUtils.createRealImaginaryArray(f2);
        transformInPlace(dataRI, this.normalization, type);
        return TransformUtils.createComplexArray(dataRI);
    }

    @Deprecated
    public Object mdfft(Object mdca, TransformType type) throws DimensionMismatchException {
        MultiDimensionalComplexMatrix mdcm = (MultiDimensionalComplexMatrix) new MultiDimensionalComplexMatrix(mdca).clone();
        int[] dimensionSize = mdcm.getDimensionSizes();
        for (int i2 = 0; i2 < dimensionSize.length; i2++) {
            mdfft(mdcm, type, i2, new int[0]);
        }
        return mdcm.getArray();
    }

    @Deprecated
    private void mdfft(MultiDimensionalComplexMatrix mdcm, TransformType type, int d2, int[] subVector) throws DimensionMismatchException {
        int[] dimensionSize = mdcm.getDimensionSizes();
        if (subVector.length == dimensionSize.length) {
            Complex[] temp = new Complex[dimensionSize[d2]];
            for (int i2 = 0; i2 < dimensionSize[d2]; i2++) {
                subVector[d2] = i2;
                temp[i2] = mdcm.get(subVector);
            }
            Complex[] temp2 = transform(temp, type);
            for (int i3 = 0; i3 < dimensionSize[d2]; i3++) {
                subVector[d2] = i3;
                mdcm.set(temp2[i3], subVector);
            }
            return;
        }
        int[] vector = new int[subVector.length + 1];
        System.arraycopy(subVector, 0, vector, 0, subVector.length);
        if (subVector.length == d2) {
            vector[d2] = 0;
            mdfft(mdcm, type, d2, vector);
            return;
        }
        for (int i4 = 0; i4 < dimensionSize[subVector.length]; i4++) {
            vector[subVector.length] = i4;
            mdfft(mdcm, type, d2, vector);
        }
    }

    @Deprecated
    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/transform/FastFourierTransformer$MultiDimensionalComplexMatrix.class */
    private static class MultiDimensionalComplexMatrix implements Cloneable {
        protected int[] dimensionSize;
        protected Object multiDimensionalComplexArray;

        MultiDimensionalComplexMatrix(Object multiDimensionalComplexArray) {
            this.multiDimensionalComplexArray = multiDimensionalComplexArray;
            int numOfDimensions = 0;
            Object obj = multiDimensionalComplexArray;
            while (true) {
                Object lastDimension = obj;
                if (!(lastDimension instanceof Object[])) {
                    break;
                }
                numOfDimensions++;
                obj = ((Object[]) lastDimension)[0];
            }
            this.dimensionSize = new int[numOfDimensions];
            int numOfDimensions2 = 0;
            Object obj2 = multiDimensionalComplexArray;
            while (true) {
                Object lastDimension2 = obj2;
                if (lastDimension2 instanceof Object[]) {
                    Object[] array = (Object[]) lastDimension2;
                    int i2 = numOfDimensions2;
                    numOfDimensions2++;
                    this.dimensionSize[i2] = array.length;
                    obj2 = array[0];
                } else {
                    return;
                }
            }
        }

        public Complex get(int... vector) throws DimensionMismatchException {
            if (vector == null) {
                if (this.dimensionSize.length > 0) {
                    throw new DimensionMismatchException(0, this.dimensionSize.length);
                }
                return null;
            }
            if (vector.length != this.dimensionSize.length) {
                throw new DimensionMismatchException(vector.length, this.dimensionSize.length);
            }
            Object lastDimension = this.multiDimensionalComplexArray;
            for (int i2 = 0; i2 < this.dimensionSize.length; i2++) {
                lastDimension = ((Object[]) lastDimension)[vector[i2]];
            }
            return (Complex) lastDimension;
        }

        public Complex set(Complex magnitude, int... vector) throws DimensionMismatchException {
            if (vector == null) {
                if (this.dimensionSize.length > 0) {
                    throw new DimensionMismatchException(0, this.dimensionSize.length);
                }
                return null;
            }
            if (vector.length != this.dimensionSize.length) {
                throw new DimensionMismatchException(vector.length, this.dimensionSize.length);
            }
            Object[] lastDimension = (Object[]) this.multiDimensionalComplexArray;
            for (int i2 = 0; i2 < this.dimensionSize.length - 1; i2++) {
                lastDimension = (Object[]) lastDimension[vector[i2]];
            }
            Complex lastValue = (Complex) lastDimension[vector[this.dimensionSize.length - 1]];
            lastDimension[vector[this.dimensionSize.length - 1]] = magnitude;
            return lastValue;
        }

        public int[] getDimensionSizes() {
            return (int[]) this.dimensionSize.clone();
        }

        public Object getArray() {
            return this.multiDimensionalComplexArray;
        }

        public Object clone() throws DimensionMismatchException {
            MultiDimensionalComplexMatrix mdcm = new MultiDimensionalComplexMatrix(Array.newInstance((Class<?>) Complex.class, this.dimensionSize));
            clone(mdcm);
            return mdcm;
        }

        private void clone(MultiDimensionalComplexMatrix mdcm) throws DimensionMismatchException {
            int[] vector = new int[this.dimensionSize.length];
            int size = 1;
            for (int i2 = 0; i2 < this.dimensionSize.length; i2++) {
                size *= this.dimensionSize[i2];
            }
            int[][] vectorList = new int[size][this.dimensionSize.length];
            for (int[] iArr : vectorList) {
                System.arraycopy(vector, 0, iArr, 0, this.dimensionSize.length);
                for (int i3 = 0; i3 < this.dimensionSize.length; i3++) {
                    int i4 = i3;
                    vector[i4] = vector[i4] + 1;
                    if (vector[i3] < this.dimensionSize[i3]) {
                        break;
                    }
                    vector[i3] = 0;
                }
            }
            for (int[] nextVector : vectorList) {
                mdcm.set(get(nextVector), nextVector);
            }
        }
    }
}
