package org.apache.commons.math3.util;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import org.apache.commons.math3.Field;
import org.apache.commons.math3.distribution.UniformIntegerDistribution;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathArithmeticException;
import org.apache.commons.math3.exception.MathIllegalArgumentException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NoDataException;
import org.apache.commons.math3.exception.NonMonotonicSequenceException;
import org.apache.commons.math3.exception.NotANumberException;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NotStrictlyPositiveException;
import org.apache.commons.math3.exception.NullArgumentException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/MathArrays.class */
public class MathArrays {

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/MathArrays$Function.class */
    public interface Function {
        double evaluate(double[] dArr);

        double evaluate(double[] dArr, int i2, int i3);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/MathArrays$OrderDirection.class */
    public enum OrderDirection {
        INCREASING,
        DECREASING
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/MathArrays$Position.class */
    public enum Position {
        HEAD,
        TAIL
    }

    private MathArrays() {
    }

    public static double[] scale(double val, double[] arr) {
        double[] newArr = new double[arr.length];
        for (int i2 = 0; i2 < arr.length; i2++) {
            newArr[i2] = arr[i2] * val;
        }
        return newArr;
    }

    public static void scaleInPlace(double val, double[] arr) {
        for (int i2 = 0; i2 < arr.length; i2++) {
            int i3 = i2;
            arr[i3] = arr[i3] * val;
        }
    }

    public static double[] ebeAdd(double[] a2, double[] b2) throws DimensionMismatchException {
        checkEqualLength(a2, b2);
        double[] result = (double[]) a2.clone();
        for (int i2 = 0; i2 < a2.length; i2++) {
            int i3 = i2;
            result[i3] = result[i3] + b2[i2];
        }
        return result;
    }

    public static double[] ebeSubtract(double[] a2, double[] b2) throws DimensionMismatchException {
        checkEqualLength(a2, b2);
        double[] result = (double[]) a2.clone();
        for (int i2 = 0; i2 < a2.length; i2++) {
            int i3 = i2;
            result[i3] = result[i3] - b2[i2];
        }
        return result;
    }

    public static double[] ebeMultiply(double[] a2, double[] b2) throws DimensionMismatchException {
        checkEqualLength(a2, b2);
        double[] result = (double[]) a2.clone();
        for (int i2 = 0; i2 < a2.length; i2++) {
            int i3 = i2;
            result[i3] = result[i3] * b2[i2];
        }
        return result;
    }

    public static double[] ebeDivide(double[] a2, double[] b2) throws DimensionMismatchException {
        checkEqualLength(a2, b2);
        double[] result = (double[]) a2.clone();
        for (int i2 = 0; i2 < a2.length; i2++) {
            int i3 = i2;
            result[i3] = result[i3] / b2[i2];
        }
        return result;
    }

    public static double distance1(double[] p1, double[] p2) throws DimensionMismatchException {
        checkEqualLength(p1, p2);
        double sum = 0.0d;
        for (int i2 = 0; i2 < p1.length; i2++) {
            sum += FastMath.abs(p1[i2] - p2[i2]);
        }
        return sum;
    }

    public static int distance1(int[] p1, int[] p2) throws DimensionMismatchException {
        checkEqualLength(p1, p2);
        int sum = 0;
        for (int i2 = 0; i2 < p1.length; i2++) {
            sum += FastMath.abs(p1[i2] - p2[i2]);
        }
        return sum;
    }

    public static double distance(double[] p1, double[] p2) throws DimensionMismatchException {
        checkEqualLength(p1, p2);
        double sum = 0.0d;
        for (int i2 = 0; i2 < p1.length; i2++) {
            double dp = p1[i2] - p2[i2];
            sum += dp * dp;
        }
        return FastMath.sqrt(sum);
    }

    public static double cosAngle(double[] v1, double[] v2) {
        return linearCombination(v1, v2) / (safeNorm(v1) * safeNorm(v2));
    }

    public static double distance(int[] p1, int[] p2) throws DimensionMismatchException {
        checkEqualLength(p1, p2);
        double sum = 0.0d;
        for (int i2 = 0; i2 < p1.length; i2++) {
            double dp = p1[i2] - p2[i2];
            sum += dp * dp;
        }
        return FastMath.sqrt(sum);
    }

    public static double distanceInf(double[] p1, double[] p2) throws DimensionMismatchException {
        checkEqualLength(p1, p2);
        double max = 0.0d;
        for (int i2 = 0; i2 < p1.length; i2++) {
            max = FastMath.max(max, FastMath.abs(p1[i2] - p2[i2]));
        }
        return max;
    }

    public static int distanceInf(int[] p1, int[] p2) throws DimensionMismatchException {
        checkEqualLength(p1, p2);
        int max = 0;
        for (int i2 = 0; i2 < p1.length; i2++) {
            max = FastMath.max(max, FastMath.abs(p1[i2] - p2[i2]));
        }
        return max;
    }

    public static <T extends Comparable<? super T>> boolean isMonotonic(T[] val, OrderDirection dir, boolean strict) {
        T previous = val[0];
        int max = val.length;
        for (int i2 = 1; i2 < max; i2++) {
            switch (dir) {
                case INCREASING:
                    int comp = previous.compareTo(val[i2]);
                    if (strict) {
                        if (comp >= 0) {
                            return false;
                        }
                        break;
                    } else {
                        if (comp > 0) {
                            return false;
                        }
                        break;
                    }
                case DECREASING:
                    int comp2 = val[i2].compareTo(previous);
                    if (strict) {
                        if (comp2 >= 0) {
                            return false;
                        }
                        break;
                    } else {
                        if (comp2 > 0) {
                            return false;
                        }
                        break;
                    }
                default:
                    throw new MathInternalError();
            }
            previous = val[i2];
        }
        return true;
    }

    public static boolean isMonotonic(double[] val, OrderDirection dir, boolean strict) {
        return checkOrder(val, dir, strict, false);
    }

    public static boolean checkEqualLength(double[] a2, double[] b2, boolean abort) {
        if (a2.length == b2.length) {
            return true;
        }
        if (abort) {
            throw new DimensionMismatchException(a2.length, b2.length);
        }
        return false;
    }

    public static void checkEqualLength(double[] a2, double[] b2) {
        checkEqualLength(a2, b2, true);
    }

    public static boolean checkEqualLength(int[] a2, int[] b2, boolean abort) {
        if (a2.length == b2.length) {
            return true;
        }
        if (abort) {
            throw new DimensionMismatchException(a2.length, b2.length);
        }
        return false;
    }

    public static void checkEqualLength(int[] a2, int[] b2) {
        checkEqualLength(a2, b2, true);
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x008b A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x008d  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static boolean checkOrder(double[] r8, org.apache.commons.math3.util.MathArrays.OrderDirection r9, boolean r10, boolean r11) throws org.apache.commons.math3.exception.NonMonotonicSequenceException {
        /*
            r0 = r8
            r1 = 0
            r0 = r0[r1]
            r12 = r0
            r0 = r8
            int r0 = r0.length
            r14 = r0
            r0 = 1
            r15 = r0
        Lc:
            r0 = r15
            r1 = r14
            if (r0 >= r1) goto L84
            int[] r0 = org.apache.commons.math3.util.MathArrays.AnonymousClass3.$SwitchMap$org$apache$commons$math3$util$MathArrays$OrderDirection
            r1 = r9
            int r1 = r1.ordinal()
            r0 = r0[r1]
            switch(r0) {
                case 1: goto L34;
                case 2: goto L52;
                default: goto L70;
            }
        L34:
            r0 = r10
            if (r0 == 0) goto L45
            r0 = r8
            r1 = r15
            r0 = r0[r1]
            r1 = r12
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 > 0) goto L78
            goto L84
        L45:
            r0 = r8
            r1 = r15
            r0 = r0[r1]
            r1 = r12
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 >= 0) goto L78
            goto L84
        L52:
            r0 = r10
            if (r0 == 0) goto L63
            r0 = r8
            r1 = r15
            r0 = r0[r1]
            r1 = r12
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 < 0) goto L78
            goto L84
        L63:
            r0 = r8
            r1 = r15
            r0 = r0[r1]
            r1 = r12
            int r0 = (r0 > r1 ? 1 : (r0 == r1 ? 0 : -1))
            if (r0 <= 0) goto L78
            goto L84
        L70:
            org.apache.commons.math3.exception.MathInternalError r0 = new org.apache.commons.math3.exception.MathInternalError
            r1 = r0
            r1.<init>()
            throw r0
        L78:
            r0 = r8
            r1 = r15
            r0 = r0[r1]
            r12 = r0
            int r15 = r15 + 1
            goto Lc
        L84:
            r0 = r15
            r1 = r14
            if (r0 != r1) goto L8d
            r0 = 1
            return r0
        L8d:
            r0 = r11
            if (r0 == 0) goto La9
            org.apache.commons.math3.exception.NonMonotonicSequenceException r0 = new org.apache.commons.math3.exception.NonMonotonicSequenceException
            r1 = r0
            r2 = r8
            r3 = r15
            r2 = r2[r3]
            java.lang.Double r2 = java.lang.Double.valueOf(r2)
            r3 = r12
            java.lang.Double r3 = java.lang.Double.valueOf(r3)
            r4 = r15
            r5 = r9
            r6 = r10
            r1.<init>(r2, r3, r4, r5, r6)
            throw r0
        La9:
            r0 = 0
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: org.apache.commons.math3.util.MathArrays.checkOrder(double[], org.apache.commons.math3.util.MathArrays$OrderDirection, boolean, boolean):boolean");
    }

    public static void checkOrder(double[] val, OrderDirection dir, boolean strict) throws NonMonotonicSequenceException {
        checkOrder(val, dir, strict, true);
    }

    public static void checkOrder(double[] val) throws NonMonotonicSequenceException {
        checkOrder(val, OrderDirection.INCREASING, true);
    }

    public static void checkRectangular(long[][] in) throws NullArgumentException, DimensionMismatchException {
        MathUtils.checkNotNull(in);
        for (int i2 = 1; i2 < in.length; i2++) {
            if (in[i2].length != in[0].length) {
                throw new DimensionMismatchException(LocalizedFormats.DIFFERENT_ROWS_LENGTHS, in[i2].length, in[0].length);
            }
        }
    }

    public static void checkPositive(double[] in) throws NotStrictlyPositiveException {
        for (int i2 = 0; i2 < in.length; i2++) {
            if (in[i2] <= 0.0d) {
                throw new NotStrictlyPositiveException(Double.valueOf(in[i2]));
            }
        }
    }

    public static void checkNotNaN(double[] in) throws NotANumberException {
        for (double d2 : in) {
            if (Double.isNaN(d2)) {
                throw new NotANumberException();
            }
        }
    }

    public static void checkNonNegative(long[] in) throws NotPositiveException {
        for (int i2 = 0; i2 < in.length; i2++) {
            if (in[i2] < 0) {
                throw new NotPositiveException(Long.valueOf(in[i2]));
            }
        }
    }

    public static void checkNonNegative(long[][] in) throws NotPositiveException {
        for (int i2 = 0; i2 < in.length; i2++) {
            for (int j2 = 0; j2 < in[i2].length; j2++) {
                if (in[i2][j2] < 0) {
                    throw new NotPositiveException(Long.valueOf(in[i2][j2]));
                }
            }
        }
    }

    public static double safeNorm(double[] v2) {
        double norm;
        double s1 = 0.0d;
        double s2 = 0.0d;
        double s3 = 0.0d;
        double x1max = 0.0d;
        double x3max = 0.0d;
        double floatn = v2.length;
        double agiant = 1.304E19d / floatn;
        for (double d2 : v2) {
            double xabs = FastMath.abs(d2);
            if (xabs >= 3.834E-20d && xabs <= agiant) {
                s2 += xabs * xabs;
            } else if (xabs > 3.834E-20d) {
                if (xabs > x1max) {
                    double r2 = x1max / xabs;
                    s1 = 1.0d + (s1 * r2 * r2);
                    x1max = xabs;
                } else {
                    double r3 = xabs / x1max;
                    s1 += r3 * r3;
                }
            } else if (xabs > x3max) {
                double r4 = x3max / xabs;
                s3 = 1.0d + (s3 * r4 * r4);
                x3max = xabs;
            } else if (xabs != 0.0d) {
                double r5 = xabs / x3max;
                s3 += r5 * r5;
            }
        }
        if (s1 != 0.0d) {
            norm = x1max * Math.sqrt(s1 + ((s2 / x1max) / x1max));
        } else if (s2 == 0.0d) {
            norm = x3max * Math.sqrt(s3);
        } else if (s2 >= x3max) {
            norm = Math.sqrt(s2 * (1.0d + ((x3max / s2) * x3max * s3)));
        } else {
            norm = Math.sqrt(x3max * ((s2 / x3max) + (x3max * s3)));
        }
        return norm;
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/MathArrays$PairDoubleInteger.class */
    private static class PairDoubleInteger {
        private final double key;
        private final int value;

        PairDoubleInteger(double key, int value) {
            this.key = key;
            this.value = value;
        }

        public double getKey() {
            return this.key;
        }

        public int getValue() {
            return this.value;
        }
    }

    public static void sortInPlace(double[] x2, double[]... yList) throws NullArgumentException, DimensionMismatchException {
        sortInPlace(x2, OrderDirection.INCREASING, yList);
    }

    public static void sortInPlace(double[] x2, OrderDirection dir, double[]... yList) throws NullArgumentException, DimensionMismatchException {
        if (x2 == null) {
            throw new NullArgumentException();
        }
        int len = x2.length;
        for (double[] y2 : yList) {
            if (y2 == null) {
                throw new NullArgumentException();
            }
            if (y2.length != len) {
                throw new DimensionMismatchException(y2.length, len);
            }
        }
        List<PairDoubleInteger> list = new ArrayList<>(len);
        for (int i2 = 0; i2 < len; i2++) {
            list.add(new PairDoubleInteger(x2[i2], i2));
        }
        Comparator<PairDoubleInteger> comp = dir == OrderDirection.INCREASING ? new Comparator<PairDoubleInteger>() { // from class: org.apache.commons.math3.util.MathArrays.1
            @Override // java.util.Comparator
            public int compare(PairDoubleInteger o1, PairDoubleInteger o2) {
                return Double.compare(o1.getKey(), o2.getKey());
            }
        } : new Comparator<PairDoubleInteger>() { // from class: org.apache.commons.math3.util.MathArrays.2
            @Override // java.util.Comparator
            public int compare(PairDoubleInteger o1, PairDoubleInteger o2) {
                return Double.compare(o2.getKey(), o1.getKey());
            }
        };
        Collections.sort(list, comp);
        int[] indices = new int[len];
        for (int i3 = 0; i3 < len; i3++) {
            PairDoubleInteger e2 = list.get(i3);
            x2[i3] = e2.getKey();
            indices[i3] = e2.getValue();
        }
        for (double[] yInPlace : yList) {
            double[] yOrig = (double[]) yInPlace.clone();
            for (int i4 = 0; i4 < len; i4++) {
                yInPlace[i4] = yOrig[indices[i4]];
            }
        }
    }

    public static int[] copyOf(int[] source) {
        return copyOf(source, source.length);
    }

    public static double[] copyOf(double[] source) {
        return copyOf(source, source.length);
    }

    public static int[] copyOf(int[] source, int len) {
        int[] output = new int[len];
        System.arraycopy(source, 0, output, 0, FastMath.min(len, source.length));
        return output;
    }

    public static double[] copyOf(double[] source, int len) {
        double[] output = new double[len];
        System.arraycopy(source, 0, output, 0, FastMath.min(len, source.length));
        return output;
    }

    public static double[] copyOfRange(double[] source, int from, int to) {
        int len = to - from;
        double[] output = new double[len];
        System.arraycopy(source, from, output, 0, FastMath.min(len, source.length - from));
        return output;
    }

    public static double linearCombination(double[] a2, double[] b2) throws DimensionMismatchException {
        checkEqualLength(a2, b2);
        int len = a2.length;
        if (len == 1) {
            return a2[0] * b2[0];
        }
        double[] prodHigh = new double[len];
        double prodLowSum = 0.0d;
        for (int i2 = 0; i2 < len; i2++) {
            double ai2 = a2[i2];
            double aHigh = Double.longBitsToDouble(Double.doubleToRawLongBits(ai2) & (-134217728));
            double aLow = ai2 - aHigh;
            double bi2 = b2[i2];
            double bHigh = Double.longBitsToDouble(Double.doubleToRawLongBits(bi2) & (-134217728));
            double bLow = bi2 - bHigh;
            prodHigh[i2] = ai2 * bi2;
            double prodLow = (aLow * bLow) - (((prodHigh[i2] - (aHigh * bHigh)) - (aLow * bHigh)) - (aHigh * bLow));
            prodLowSum += prodLow;
        }
        double prodHighCur = prodHigh[0];
        double prodHighNext = prodHigh[1];
        double sHighPrev = prodHighCur + prodHighNext;
        double sPrime = sHighPrev - prodHighNext;
        double sLowSum = (prodHighNext - (sHighPrev - sPrime)) + (prodHighCur - sPrime);
        int lenMinusOne = len - 1;
        for (int i3 = 1; i3 < lenMinusOne; i3++) {
            double prodHighNext2 = prodHigh[i3 + 1];
            double sHighCur = sHighPrev + prodHighNext2;
            double sPrime2 = sHighCur - prodHighNext2;
            sLowSum += (prodHighNext2 - (sHighCur - sPrime2)) + (sHighPrev - sPrime2);
            sHighPrev = sHighCur;
        }
        double result = sHighPrev + prodLowSum + sLowSum;
        if (Double.isNaN(result)) {
            result = 0.0d;
            for (int i4 = 0; i4 < len; i4++) {
                result += a2[i4] * b2[i4];
            }
        }
        return result;
    }

    public static double linearCombination(double a1, double b1, double a2, double b2) {
        double a1High = Double.longBitsToDouble(Double.doubleToRawLongBits(a1) & (-134217728));
        double a1Low = a1 - a1High;
        double b1High = Double.longBitsToDouble(Double.doubleToRawLongBits(b1) & (-134217728));
        double b1Low = b1 - b1High;
        double prod1High = a1 * b1;
        double prod1Low = (a1Low * b1Low) - (((prod1High - (a1High * b1High)) - (a1Low * b1High)) - (a1High * b1Low));
        double a2High = Double.longBitsToDouble(Double.doubleToRawLongBits(a2) & (-134217728));
        double a2Low = a2 - a2High;
        double b2High = Double.longBitsToDouble(Double.doubleToRawLongBits(b2) & (-134217728));
        double b2Low = b2 - b2High;
        double prod2High = a2 * b2;
        double prod2Low = (a2Low * b2Low) - (((prod2High - (a2High * b2High)) - (a2Low * b2High)) - (a2High * b2Low));
        double s12High = prod1High + prod2High;
        double s12Prime = s12High - prod2High;
        double s12Low = (prod2High - (s12High - s12Prime)) + (prod1High - s12Prime);
        double result = s12High + prod1Low + prod2Low + s12Low;
        if (Double.isNaN(result)) {
            result = (a1 * b1) + (a2 * b2);
        }
        return result;
    }

    public static double linearCombination(double a1, double b1, double a2, double b2, double a3, double b3) {
        double a1High = Double.longBitsToDouble(Double.doubleToRawLongBits(a1) & (-134217728));
        double a1Low = a1 - a1High;
        double b1High = Double.longBitsToDouble(Double.doubleToRawLongBits(b1) & (-134217728));
        double b1Low = b1 - b1High;
        double prod1High = a1 * b1;
        double prod1Low = (a1Low * b1Low) - (((prod1High - (a1High * b1High)) - (a1Low * b1High)) - (a1High * b1Low));
        double a2High = Double.longBitsToDouble(Double.doubleToRawLongBits(a2) & (-134217728));
        double a2Low = a2 - a2High;
        double b2High = Double.longBitsToDouble(Double.doubleToRawLongBits(b2) & (-134217728));
        double b2Low = b2 - b2High;
        double prod2High = a2 * b2;
        double prod2Low = (a2Low * b2Low) - (((prod2High - (a2High * b2High)) - (a2Low * b2High)) - (a2High * b2Low));
        double a3High = Double.longBitsToDouble(Double.doubleToRawLongBits(a3) & (-134217728));
        double a3Low = a3 - a3High;
        double b3High = Double.longBitsToDouble(Double.doubleToRawLongBits(b3) & (-134217728));
        double b3Low = b3 - b3High;
        double prod3High = a3 * b3;
        double prod3Low = (a3Low * b3Low) - (((prod3High - (a3High * b3High)) - (a3Low * b3High)) - (a3High * b3Low));
        double s12High = prod1High + prod2High;
        double s12Prime = s12High - prod2High;
        double s12Low = (prod2High - (s12High - s12Prime)) + (prod1High - s12Prime);
        double s123High = s12High + prod3High;
        double s123Prime = s123High - prod3High;
        double s123Low = (prod3High - (s123High - s123Prime)) + (s12High - s123Prime);
        double result = s123High + prod1Low + prod2Low + prod3Low + s12Low + s123Low;
        if (Double.isNaN(result)) {
            result = (a1 * b1) + (a2 * b2) + (a3 * b3);
        }
        return result;
    }

    public static double linearCombination(double a1, double b1, double a2, double b2, double a3, double b3, double a4, double b4) {
        double a1High = Double.longBitsToDouble(Double.doubleToRawLongBits(a1) & (-134217728));
        double a1Low = a1 - a1High;
        double b1High = Double.longBitsToDouble(Double.doubleToRawLongBits(b1) & (-134217728));
        double b1Low = b1 - b1High;
        double prod1High = a1 * b1;
        double prod1Low = (a1Low * b1Low) - (((prod1High - (a1High * b1High)) - (a1Low * b1High)) - (a1High * b1Low));
        double a2High = Double.longBitsToDouble(Double.doubleToRawLongBits(a2) & (-134217728));
        double a2Low = a2 - a2High;
        double b2High = Double.longBitsToDouble(Double.doubleToRawLongBits(b2) & (-134217728));
        double b2Low = b2 - b2High;
        double prod2High = a2 * b2;
        double prod2Low = (a2Low * b2Low) - (((prod2High - (a2High * b2High)) - (a2Low * b2High)) - (a2High * b2Low));
        double a3High = Double.longBitsToDouble(Double.doubleToRawLongBits(a3) & (-134217728));
        double a3Low = a3 - a3High;
        double b3High = Double.longBitsToDouble(Double.doubleToRawLongBits(b3) & (-134217728));
        double b3Low = b3 - b3High;
        double prod3High = a3 * b3;
        double prod3Low = (a3Low * b3Low) - (((prod3High - (a3High * b3High)) - (a3Low * b3High)) - (a3High * b3Low));
        double a4High = Double.longBitsToDouble(Double.doubleToRawLongBits(a4) & (-134217728));
        double a4Low = a4 - a4High;
        double b4High = Double.longBitsToDouble(Double.doubleToRawLongBits(b4) & (-134217728));
        double b4Low = b4 - b4High;
        double prod4High = a4 * b4;
        double prod4Low = (a4Low * b4Low) - (((prod4High - (a4High * b4High)) - (a4Low * b4High)) - (a4High * b4Low));
        double s12High = prod1High + prod2High;
        double s12Prime = s12High - prod2High;
        double s12Low = (prod2High - (s12High - s12Prime)) + (prod1High - s12Prime);
        double s123High = s12High + prod3High;
        double s123Prime = s123High - prod3High;
        double s123Low = (prod3High - (s123High - s123Prime)) + (s12High - s123Prime);
        double s1234High = s123High + prod4High;
        double s1234Prime = s1234High - prod4High;
        double s1234Low = (prod4High - (s1234High - s1234Prime)) + (s123High - s1234Prime);
        double result = s1234High + prod1Low + prod2Low + prod3Low + prod4Low + s12Low + s123Low + s1234Low;
        if (Double.isNaN(result)) {
            result = (a1 * b1) + (a2 * b2) + (a3 * b3) + (a4 * b4);
        }
        return result;
    }

    public static boolean equals(float[] x2, float[] y2) {
        if (x2 == null || y2 == null) {
            return !((x2 == null) ^ (y2 == null));
        }
        if (x2.length != y2.length) {
            return false;
        }
        for (int i2 = 0; i2 < x2.length; i2++) {
            if (!Precision.equals(x2[i2], y2[i2])) {
                return false;
            }
        }
        return true;
    }

    public static boolean equalsIncludingNaN(float[] x2, float[] y2) {
        if (x2 == null || y2 == null) {
            return !((x2 == null) ^ (y2 == null));
        }
        if (x2.length != y2.length) {
            return false;
        }
        for (int i2 = 0; i2 < x2.length; i2++) {
            if (!Precision.equalsIncludingNaN(x2[i2], y2[i2])) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(double[] x2, double[] y2) {
        if (x2 == null || y2 == null) {
            return !((x2 == null) ^ (y2 == null));
        }
        if (x2.length != y2.length) {
            return false;
        }
        for (int i2 = 0; i2 < x2.length; i2++) {
            if (!Precision.equals(x2[i2], y2[i2])) {
                return false;
            }
        }
        return true;
    }

    public static boolean equalsIncludingNaN(double[] x2, double[] y2) {
        if (x2 == null || y2 == null) {
            return !((x2 == null) ^ (y2 == null));
        }
        if (x2.length != y2.length) {
            return false;
        }
        for (int i2 = 0; i2 < x2.length; i2++) {
            if (!Precision.equalsIncludingNaN(x2[i2], y2[i2])) {
                return false;
            }
        }
        return true;
    }

    public static double[] normalizeArray(double[] values, double normalizedSum) throws MathIllegalArgumentException, MathArithmeticException {
        if (Double.isInfinite(normalizedSum)) {
            throw new MathIllegalArgumentException(LocalizedFormats.NORMALIZE_INFINITE, new Object[0]);
        }
        if (Double.isNaN(normalizedSum)) {
            throw new MathIllegalArgumentException(LocalizedFormats.NORMALIZE_NAN, new Object[0]);
        }
        double sum = 0.0d;
        int len = values.length;
        double[] out = new double[len];
        for (int i2 = 0; i2 < len; i2++) {
            if (Double.isInfinite(values[i2])) {
                throw new MathIllegalArgumentException(LocalizedFormats.INFINITE_ARRAY_ELEMENT, Double.valueOf(values[i2]), Integer.valueOf(i2));
            }
            if (!Double.isNaN(values[i2])) {
                sum += values[i2];
            }
        }
        if (sum == 0.0d) {
            throw new MathArithmeticException(LocalizedFormats.ARRAY_SUMS_TO_ZERO, new Object[0]);
        }
        for (int i3 = 0; i3 < len; i3++) {
            if (Double.isNaN(values[i3])) {
                out[i3] = Double.NaN;
            } else {
                out[i3] = (values[i3] * normalizedSum) / sum;
            }
        }
        return out;
    }

    public static <T> T[] buildArray(Field<T> field, int i2) {
        T[] tArr = (T[]) ((Object[]) Array.newInstance(field.getRuntimeClass(), i2));
        Arrays.fill(tArr, field.getZero());
        return tArr;
    }

    public static <T> T[][] buildArray(Field<T> field, int i2, int i3) {
        Object[][] objArr;
        if (i3 < 0) {
            objArr = (Object[][]) Array.newInstance(buildArray(field, 0).getClass(), i2);
        } else {
            objArr = (Object[][]) Array.newInstance(field.getRuntimeClass(), i2, i3);
            for (int i4 = 0; i4 < i2; i4++) {
                Arrays.fill(objArr[i4], field.getZero());
            }
        }
        return (T[][]) objArr;
    }

    public static double[] convolve(double[] x2, double[] h2) throws NullArgumentException, NoDataException {
        MathUtils.checkNotNull(x2);
        MathUtils.checkNotNull(h2);
        int xLen = x2.length;
        int hLen = h2.length;
        if (xLen == 0 || hLen == 0) {
            throw new NoDataException();
        }
        int totalLength = (xLen + hLen) - 1;
        double[] y2 = new double[totalLength];
        for (int n2 = 0; n2 < totalLength; n2++) {
            double yn = 0.0d;
            int k2 = FastMath.max(0, (n2 + 1) - xLen);
            int j2 = n2 - k2;
            while (k2 < hLen && j2 >= 0) {
                int i2 = j2;
                j2--;
                int i3 = k2;
                k2++;
                yn += x2[i2] * h2[i3];
            }
            y2[n2] = yn;
        }
        return y2;
    }

    public static void shuffle(int[] list, int start, Position pos) {
        shuffle(list, start, pos, new Well19937c());
    }

    public static void shuffle(int[] list, int start, Position pos, RandomGenerator rng) {
        int iSample;
        int iSample2;
        switch (pos) {
            case TAIL:
                for (int i2 = list.length - 1; i2 >= start; i2--) {
                    if (i2 == start) {
                        iSample2 = start;
                    } else {
                        iSample2 = new UniformIntegerDistribution(rng, start, i2).sample();
                    }
                    int target = iSample2;
                    int temp = list[target];
                    list[target] = list[i2];
                    list[i2] = temp;
                }
                return;
            case HEAD:
                for (int i3 = 0; i3 <= start; i3++) {
                    if (i3 == start) {
                        iSample = start;
                    } else {
                        iSample = new UniformIntegerDistribution(rng, i3, start).sample();
                    }
                    int target2 = iSample;
                    int temp2 = list[target2];
                    list[target2] = list[i3];
                    list[i3] = temp2;
                }
                return;
            default:
                throw new MathInternalError();
        }
    }

    public static void shuffle(int[] list, RandomGenerator rng) {
        shuffle(list, 0, Position.TAIL, rng);
    }

    public static void shuffle(int[] list) {
        shuffle(list, new Well19937c());
    }

    public static int[] natural(int n2) {
        return sequence(n2, 0, 1);
    }

    public static int[] sequence(int size, int start, int stride) {
        int[] a2 = new int[size];
        for (int i2 = 0; i2 < size; i2++) {
            a2[i2] = start + (i2 * stride);
        }
        return a2;
    }

    public static boolean verifyValues(double[] values, int begin, int length) throws MathIllegalArgumentException {
        return verifyValues(values, begin, length, false);
    }

    public static boolean verifyValues(double[] values, int begin, int length, boolean allowEmpty) throws MathIllegalArgumentException {
        if (values == null) {
            throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
        }
        if (begin < 0) {
            throw new NotPositiveException(LocalizedFormats.START_POSITION, Integer.valueOf(begin));
        }
        if (length < 0) {
            throw new NotPositiveException(LocalizedFormats.LENGTH, Integer.valueOf(length));
        }
        if (begin + length > values.length) {
            throw new NumberIsTooLargeException(LocalizedFormats.SUBARRAY_ENDS_AFTER_ARRAY_END, Integer.valueOf(begin + length), Integer.valueOf(values.length), true);
        }
        if (length == 0 && !allowEmpty) {
            return false;
        }
        return true;
    }

    public static boolean verifyValues(double[] values, double[] weights, int begin, int length) throws MathIllegalArgumentException {
        return verifyValues(values, weights, begin, length, false);
    }

    public static boolean verifyValues(double[] values, double[] weights, int begin, int length, boolean allowEmpty) throws MathIllegalArgumentException {
        if (weights == null || values == null) {
            throw new NullArgumentException(LocalizedFormats.INPUT_ARRAY, new Object[0]);
        }
        checkEqualLength(weights, values);
        boolean containsPositiveWeight = false;
        for (int i2 = begin; i2 < begin + length; i2++) {
            double weight = weights[i2];
            if (Double.isNaN(weight)) {
                throw new MathIllegalArgumentException(LocalizedFormats.NAN_ELEMENT_AT_INDEX, Integer.valueOf(i2));
            }
            if (Double.isInfinite(weight)) {
                throw new MathIllegalArgumentException(LocalizedFormats.INFINITE_ARRAY_ELEMENT, Double.valueOf(weight), Integer.valueOf(i2));
            }
            if (weight < 0.0d) {
                throw new MathIllegalArgumentException(LocalizedFormats.NEGATIVE_ELEMENT_AT_INDEX, Integer.valueOf(i2), Double.valueOf(weight));
            }
            if (!containsPositiveWeight && weight > 0.0d) {
                containsPositiveWeight = true;
            }
        }
        if (!containsPositiveWeight) {
            throw new MathIllegalArgumentException(LocalizedFormats.WEIGHT_AT_LEAST_ONE_NON_ZERO, new Object[0]);
        }
        return verifyValues(values, begin, length, allowEmpty);
    }

    public static double[] concatenate(double[]... x2) {
        int combinedLength = 0;
        for (double[] a2 : x2) {
            combinedLength += a2.length;
        }
        int offset = 0;
        double[] combined = new double[combinedLength];
        for (int i2 = 0; i2 < x2.length; i2++) {
            int curLength = x2[i2].length;
            System.arraycopy(x2[i2], 0, combined, offset, curLength);
            offset += curLength;
        }
        return combined;
    }

    public static double[] unique(double[] data) {
        TreeSet<Double> values = new TreeSet<>();
        for (double d2 : data) {
            values.add(Double.valueOf(d2));
        }
        int count = values.size();
        double[] out = new double[count];
        Iterator<Double> iterator = values.iterator();
        int i2 = 0;
        while (iterator.hasNext()) {
            i2++;
            out[count - i2] = iterator.next().doubleValue();
        }
        return out;
    }
}
