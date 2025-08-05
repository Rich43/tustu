package java.util;

import A.a;
import com.sun.xml.internal.fastinfoset.EncodingConstants;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.security.AccessController;
import java.util.ArrayPrefixHelpers;
import java.util.ArraysParallelSortHelpers;
import java.util.Spliterator;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.IntFunction;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.UnaryOperator;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import javafx.fxml.FXMLLoader;
import sun.security.action.GetBooleanAction;

/* loaded from: rt.jar:java/util/Arrays.class */
public class Arrays {
    private static final int MIN_ARRAY_SORT_GRAN = 8192;
    private static final int INSERTIONSORT_THRESHOLD = 7;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Arrays.class.desiredAssertionStatus();
    }

    private Arrays() {
    }

    /* loaded from: rt.jar:java/util/Arrays$NaturalOrder.class */
    static final class NaturalOrder implements Comparator<Object> {
        static final NaturalOrder INSTANCE = new NaturalOrder();

        NaturalOrder() {
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            return ((Comparable) obj).compareTo(obj2);
        }
    }

    private static void rangeCheck(int i2, int i3, int i4) {
        if (i3 > i4) {
            throw new IllegalArgumentException("fromIndex(" + i3 + ") > toIndex(" + i4 + ")");
        }
        if (i3 < 0) {
            throw new ArrayIndexOutOfBoundsException(i3);
        }
        if (i4 > i2) {
            throw new ArrayIndexOutOfBoundsException(i4);
        }
    }

    public static void sort(int[] iArr) {
        DualPivotQuicksort.sort(iArr, 0, iArr.length - 1, (int[]) null, 0, 0);
    }

    public static void sort(int[] iArr, int i2, int i3) {
        rangeCheck(iArr.length, i2, i3);
        DualPivotQuicksort.sort(iArr, i2, i3 - 1, (int[]) null, 0, 0);
    }

    public static void sort(long[] jArr) {
        DualPivotQuicksort.sort(jArr, 0, jArr.length - 1, (long[]) null, 0, 0);
    }

    public static void sort(long[] jArr, int i2, int i3) {
        rangeCheck(jArr.length, i2, i3);
        DualPivotQuicksort.sort(jArr, i2, i3 - 1, (long[]) null, 0, 0);
    }

    public static void sort(short[] sArr) {
        DualPivotQuicksort.sort(sArr, 0, sArr.length - 1, (short[]) null, 0, 0);
    }

    public static void sort(short[] sArr, int i2, int i3) {
        rangeCheck(sArr.length, i2, i3);
        DualPivotQuicksort.sort(sArr, i2, i3 - 1, (short[]) null, 0, 0);
    }

    public static void sort(char[] cArr) {
        DualPivotQuicksort.sort(cArr, 0, cArr.length - 1, (char[]) null, 0, 0);
    }

    public static void sort(char[] cArr, int i2, int i3) {
        rangeCheck(cArr.length, i2, i3);
        DualPivotQuicksort.sort(cArr, i2, i3 - 1, (char[]) null, 0, 0);
    }

    public static void sort(byte[] bArr) {
        DualPivotQuicksort.sort(bArr, 0, bArr.length - 1);
    }

    public static void sort(byte[] bArr, int i2, int i3) {
        rangeCheck(bArr.length, i2, i3);
        DualPivotQuicksort.sort(bArr, i2, i3 - 1);
    }

    public static void sort(float[] fArr) {
        DualPivotQuicksort.sort(fArr, 0, fArr.length - 1, (float[]) null, 0, 0);
    }

    public static void sort(float[] fArr, int i2, int i3) {
        rangeCheck(fArr.length, i2, i3);
        DualPivotQuicksort.sort(fArr, i2, i3 - 1, (float[]) null, 0, 0);
    }

    public static void sort(double[] dArr) {
        DualPivotQuicksort.sort(dArr, 0, dArr.length - 1, (double[]) null, 0, 0);
    }

    public static void sort(double[] dArr, int i2, int i3) {
        rangeCheck(dArr.length, i2, i3);
        DualPivotQuicksort.sort(dArr, i2, i3 - 1, (double[]) null, 0, 0);
    }

    public static void parallelSort(byte[] bArr) {
        int commonPoolParallelism;
        int length = bArr.length;
        if (length <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(bArr, 0, length - 1);
        } else {
            int i2 = length / (commonPoolParallelism << 2);
            new ArraysParallelSortHelpers.FJByte.Sorter(null, bArr, new byte[length], 0, length, 0, i2 <= 8192 ? 8192 : i2).invoke();
        }
    }

    public static void parallelSort(byte[] bArr, int i2, int i3) {
        int commonPoolParallelism;
        rangeCheck(bArr.length, i2, i3);
        int i4 = i3 - i2;
        if (i4 <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(bArr, i2, i3 - 1);
        } else {
            int i5 = i4 / (commonPoolParallelism << 2);
            new ArraysParallelSortHelpers.FJByte.Sorter(null, bArr, new byte[i4], i2, i4, 0, i5 <= 8192 ? 8192 : i5).invoke();
        }
    }

    public static void parallelSort(char[] cArr) {
        int commonPoolParallelism;
        int length = cArr.length;
        if (length <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(cArr, 0, length - 1, (char[]) null, 0, 0);
        } else {
            int i2 = length / (commonPoolParallelism << 2);
            new ArraysParallelSortHelpers.FJChar.Sorter(null, cArr, new char[length], 0, length, 0, i2 <= 8192 ? 8192 : i2).invoke();
        }
    }

    public static void parallelSort(char[] cArr, int i2, int i3) {
        int commonPoolParallelism;
        rangeCheck(cArr.length, i2, i3);
        int i4 = i3 - i2;
        if (i4 <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(cArr, i2, i3 - 1, (char[]) null, 0, 0);
        } else {
            int i5 = i4 / (commonPoolParallelism << 2);
            new ArraysParallelSortHelpers.FJChar.Sorter(null, cArr, new char[i4], i2, i4, 0, i5 <= 8192 ? 8192 : i5).invoke();
        }
    }

    public static void parallelSort(short[] sArr) {
        int commonPoolParallelism;
        int length = sArr.length;
        if (length <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(sArr, 0, length - 1, (short[]) null, 0, 0);
        } else {
            int i2 = length / (commonPoolParallelism << 2);
            new ArraysParallelSortHelpers.FJShort.Sorter(null, sArr, new short[length], 0, length, 0, i2 <= 8192 ? 8192 : i2).invoke();
        }
    }

    public static void parallelSort(short[] sArr, int i2, int i3) {
        int commonPoolParallelism;
        rangeCheck(sArr.length, i2, i3);
        int i4 = i3 - i2;
        if (i4 <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(sArr, i2, i3 - 1, (short[]) null, 0, 0);
        } else {
            int i5 = i4 / (commonPoolParallelism << 2);
            new ArraysParallelSortHelpers.FJShort.Sorter(null, sArr, new short[i4], i2, i4, 0, i5 <= 8192 ? 8192 : i5).invoke();
        }
    }

    public static void parallelSort(int[] iArr) {
        int commonPoolParallelism;
        int length = iArr.length;
        if (length <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(iArr, 0, length - 1, (int[]) null, 0, 0);
        } else {
            int i2 = length / (commonPoolParallelism << 2);
            new ArraysParallelSortHelpers.FJInt.Sorter(null, iArr, new int[length], 0, length, 0, i2 <= 8192 ? 8192 : i2).invoke();
        }
    }

    public static void parallelSort(int[] iArr, int i2, int i3) {
        int commonPoolParallelism;
        rangeCheck(iArr.length, i2, i3);
        int i4 = i3 - i2;
        if (i4 <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(iArr, i2, i3 - 1, (int[]) null, 0, 0);
        } else {
            int i5 = i4 / (commonPoolParallelism << 2);
            new ArraysParallelSortHelpers.FJInt.Sorter(null, iArr, new int[i4], i2, i4, 0, i5 <= 8192 ? 8192 : i5).invoke();
        }
    }

    public static void parallelSort(long[] jArr) {
        int commonPoolParallelism;
        int length = jArr.length;
        if (length <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(jArr, 0, length - 1, (long[]) null, 0, 0);
        } else {
            int i2 = length / (commonPoolParallelism << 2);
            new ArraysParallelSortHelpers.FJLong.Sorter(null, jArr, new long[length], 0, length, 0, i2 <= 8192 ? 8192 : i2).invoke();
        }
    }

    public static void parallelSort(long[] jArr, int i2, int i3) {
        int commonPoolParallelism;
        rangeCheck(jArr.length, i2, i3);
        int i4 = i3 - i2;
        if (i4 <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(jArr, i2, i3 - 1, (long[]) null, 0, 0);
        } else {
            int i5 = i4 / (commonPoolParallelism << 2);
            new ArraysParallelSortHelpers.FJLong.Sorter(null, jArr, new long[i4], i2, i4, 0, i5 <= 8192 ? 8192 : i5).invoke();
        }
    }

    public static void parallelSort(float[] fArr) {
        int commonPoolParallelism;
        int length = fArr.length;
        if (length <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(fArr, 0, length - 1, (float[]) null, 0, 0);
        } else {
            int i2 = length / (commonPoolParallelism << 2);
            new ArraysParallelSortHelpers.FJFloat.Sorter(null, fArr, new float[length], 0, length, 0, i2 <= 8192 ? 8192 : i2).invoke();
        }
    }

    public static void parallelSort(float[] fArr, int i2, int i3) {
        int commonPoolParallelism;
        rangeCheck(fArr.length, i2, i3);
        int i4 = i3 - i2;
        if (i4 <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(fArr, i2, i3 - 1, (float[]) null, 0, 0);
        } else {
            int i5 = i4 / (commonPoolParallelism << 2);
            new ArraysParallelSortHelpers.FJFloat.Sorter(null, fArr, new float[i4], i2, i4, 0, i5 <= 8192 ? 8192 : i5).invoke();
        }
    }

    public static void parallelSort(double[] dArr) {
        int commonPoolParallelism;
        int length = dArr.length;
        if (length <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(dArr, 0, length - 1, (double[]) null, 0, 0);
        } else {
            int i2 = length / (commonPoolParallelism << 2);
            new ArraysParallelSortHelpers.FJDouble.Sorter(null, dArr, new double[length], 0, length, 0, i2 <= 8192 ? 8192 : i2).invoke();
        }
    }

    public static void parallelSort(double[] dArr, int i2, int i3) {
        int commonPoolParallelism;
        rangeCheck(dArr.length, i2, i3);
        int i4 = i3 - i2;
        if (i4 <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            DualPivotQuicksort.sort(dArr, i2, i3 - 1, (double[]) null, 0, 0);
        } else {
            int i5 = i4 / (commonPoolParallelism << 2);
            new ArraysParallelSortHelpers.FJDouble.Sorter(null, dArr, new double[i4], i2, i4, 0, i5 <= 8192 ? 8192 : i5).invoke();
        }
    }

    public static <T extends Comparable<? super T>> void parallelSort(T[] tArr) {
        int commonPoolParallelism;
        int length = tArr.length;
        if (length <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            TimSort.sort(tArr, 0, length, NaturalOrder.INSTANCE, null, 0, 0);
        } else {
            int i2 = length / (commonPoolParallelism << 2);
            new ArraysParallelSortHelpers.FJObject.Sorter(null, tArr, (Comparable[]) Array.newInstance(tArr.getClass().getComponentType(), length), 0, length, 0, i2 <= 8192 ? 8192 : i2, NaturalOrder.INSTANCE).invoke();
        }
    }

    public static <T extends Comparable<? super T>> void parallelSort(T[] tArr, int i2, int i3) {
        int commonPoolParallelism;
        rangeCheck(tArr.length, i2, i3);
        int i4 = i3 - i2;
        if (i4 <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            TimSort.sort(tArr, i2, i3, NaturalOrder.INSTANCE, null, 0, 0);
        } else {
            int i5 = i4 / (commonPoolParallelism << 2);
            new ArraysParallelSortHelpers.FJObject.Sorter(null, tArr, (Comparable[]) Array.newInstance(tArr.getClass().getComponentType(), i4), i2, i4, 0, i5 <= 8192 ? 8192 : i5, NaturalOrder.INSTANCE).invoke();
        }
    }

    public static <T> void parallelSort(T[] tArr, Comparator<? super T> comparator) {
        int commonPoolParallelism;
        if (comparator == null) {
            comparator = NaturalOrder.INSTANCE;
        }
        int length = tArr.length;
        if (length <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            TimSort.sort(tArr, 0, length, comparator, null, 0, 0);
        } else {
            int i2 = length / (commonPoolParallelism << 2);
            new ArraysParallelSortHelpers.FJObject.Sorter(null, tArr, (Object[]) Array.newInstance(tArr.getClass().getComponentType(), length), 0, length, 0, i2 <= 8192 ? 8192 : i2, comparator).invoke();
        }
    }

    public static <T> void parallelSort(T[] tArr, int i2, int i3, Comparator<? super T> comparator) {
        int commonPoolParallelism;
        rangeCheck(tArr.length, i2, i3);
        if (comparator == null) {
            comparator = NaturalOrder.INSTANCE;
        }
        int i4 = i3 - i2;
        if (i4 <= 8192 || (commonPoolParallelism = ForkJoinPool.getCommonPoolParallelism()) == 1) {
            TimSort.sort(tArr, i2, i3, comparator, null, 0, 0);
        } else {
            int i5 = i4 / (commonPoolParallelism << 2);
            new ArraysParallelSortHelpers.FJObject.Sorter(null, tArr, (Object[]) Array.newInstance(tArr.getClass().getComponentType(), i4), i2, i4, 0, i5 <= 8192 ? 8192 : i5, comparator).invoke();
        }
    }

    /* loaded from: rt.jar:java/util/Arrays$LegacyMergeSort.class */
    static final class LegacyMergeSort {
        private static final boolean userRequested = ((Boolean) AccessController.doPrivileged(new GetBooleanAction("java.util.Arrays.useLegacyMergeSort"))).booleanValue();

        LegacyMergeSort() {
        }
    }

    public static void sort(Object[] objArr) {
        if (LegacyMergeSort.userRequested) {
            legacyMergeSort(objArr);
        } else {
            ComparableTimSort.sort(objArr, 0, objArr.length, null, 0, 0);
        }
    }

    private static void legacyMergeSort(Object[] objArr) {
        mergeSort((Object[]) objArr.clone(), objArr, 0, objArr.length, 0);
    }

    public static void sort(Object[] objArr, int i2, int i3) {
        rangeCheck(objArr.length, i2, i3);
        if (LegacyMergeSort.userRequested) {
            legacyMergeSort(objArr, i2, i3);
        } else {
            ComparableTimSort.sort(objArr, i2, i3, null, 0, 0);
        }
    }

    private static void legacyMergeSort(Object[] objArr, int i2, int i3) {
        mergeSort(copyOfRange(objArr, i2, i3), objArr, i2, i3, -i2);
    }

    private static void mergeSort(Object[] objArr, Object[] objArr2, int i2, int i3, int i4) {
        int i5 = i3 - i2;
        if (i5 < 7) {
            for (int i6 = i2; i6 < i3; i6++) {
                for (int i7 = i6; i7 > i2 && ((Comparable) objArr2[i7 - 1]).compareTo(objArr2[i7]) > 0; i7--) {
                    swap(objArr2, i7, i7 - 1);
                }
            }
            return;
        }
        int i8 = i2 + i4;
        int i9 = i3 + i4;
        int i10 = (i8 + i9) >>> 1;
        mergeSort(objArr2, objArr, i8, i10, -i4);
        mergeSort(objArr2, objArr, i10, i9, -i4);
        if (((Comparable) objArr[i10 - 1]).compareTo(objArr[i10]) <= 0) {
            System.arraycopy(objArr, i8, objArr2, i2, i5);
            return;
        }
        int i11 = i8;
        int i12 = i10;
        for (int i13 = i2; i13 < i3; i13++) {
            if (i12 >= i9 || (i11 < i10 && ((Comparable) objArr[i11]).compareTo(objArr[i12]) <= 0)) {
                int i14 = i11;
                i11++;
                objArr2[i13] = objArr[i14];
            } else {
                int i15 = i12;
                i12++;
                objArr2[i13] = objArr[i15];
            }
        }
    }

    private static void swap(Object[] objArr, int i2, int i3) {
        Object obj = objArr[i2];
        objArr[i2] = objArr[i3];
        objArr[i3] = obj;
    }

    public static <T> void sort(T[] tArr, Comparator<? super T> comparator) {
        if (comparator != null) {
            if (LegacyMergeSort.userRequested) {
                legacyMergeSort(tArr, comparator);
                return;
            } else {
                TimSort.sort(tArr, 0, tArr.length, comparator, null, 0, 0);
                return;
            }
        }
        sort(tArr);
    }

    private static <T> void legacyMergeSort(T[] tArr, Comparator<? super T> comparator) {
        Object[] objArr = (Object[]) tArr.clone();
        if (comparator == null) {
            mergeSort(objArr, tArr, 0, tArr.length, 0);
        } else {
            mergeSort(objArr, tArr, 0, tArr.length, 0, comparator);
        }
    }

    public static <T> void sort(T[] tArr, int i2, int i3, Comparator<? super T> comparator) {
        if (comparator == null) {
            sort(tArr, i2, i3);
            return;
        }
        rangeCheck(tArr.length, i2, i3);
        if (LegacyMergeSort.userRequested) {
            legacyMergeSort(tArr, i2, i3, comparator);
        } else {
            TimSort.sort(tArr, i2, i3, comparator, null, 0, 0);
        }
    }

    private static <T> void legacyMergeSort(T[] tArr, int i2, int i3, Comparator<? super T> comparator) {
        Object[] objArrCopyOfRange = copyOfRange(tArr, i2, i3);
        if (comparator == null) {
            mergeSort(objArrCopyOfRange, tArr, i2, i3, -i2);
        } else {
            mergeSort(objArrCopyOfRange, tArr, i2, i3, -i2, comparator);
        }
    }

    private static void mergeSort(Object[] objArr, Object[] objArr2, int i2, int i3, int i4, Comparator comparator) {
        int i5 = i3 - i2;
        if (i5 < 7) {
            for (int i6 = i2; i6 < i3; i6++) {
                for (int i7 = i6; i7 > i2 && comparator.compare(objArr2[i7 - 1], objArr2[i7]) > 0; i7--) {
                    swap(objArr2, i7, i7 - 1);
                }
            }
            return;
        }
        int i8 = i2 + i4;
        int i9 = i3 + i4;
        int i10 = (i8 + i9) >>> 1;
        mergeSort(objArr2, objArr, i8, i10, -i4, comparator);
        mergeSort(objArr2, objArr, i10, i9, -i4, comparator);
        if (comparator.compare(objArr[i10 - 1], objArr[i10]) <= 0) {
            System.arraycopy(objArr, i8, objArr2, i2, i5);
            return;
        }
        int i11 = i8;
        int i12 = i10;
        for (int i13 = i2; i13 < i3; i13++) {
            if (i12 >= i9 || (i11 < i10 && comparator.compare(objArr[i11], objArr[i12]) <= 0)) {
                int i14 = i11;
                i11++;
                objArr2[i13] = objArr[i14];
            } else {
                int i15 = i12;
                i12++;
                objArr2[i13] = objArr[i15];
            }
        }
    }

    public static <T> void parallelPrefix(T[] tArr, BinaryOperator<T> binaryOperator) {
        Objects.requireNonNull(binaryOperator);
        if (tArr.length > 0) {
            new ArrayPrefixHelpers.CumulateTask(null, binaryOperator, tArr, 0, tArr.length).invoke();
        }
    }

    public static <T> void parallelPrefix(T[] tArr, int i2, int i3, BinaryOperator<T> binaryOperator) {
        Objects.requireNonNull(binaryOperator);
        rangeCheck(tArr.length, i2, i3);
        if (i2 < i3) {
            new ArrayPrefixHelpers.CumulateTask(null, binaryOperator, tArr, i2, i3).invoke();
        }
    }

    public static void parallelPrefix(long[] jArr, LongBinaryOperator longBinaryOperator) {
        Objects.requireNonNull(longBinaryOperator);
        if (jArr.length > 0) {
            new ArrayPrefixHelpers.LongCumulateTask(null, longBinaryOperator, jArr, 0, jArr.length).invoke();
        }
    }

    public static void parallelPrefix(long[] jArr, int i2, int i3, LongBinaryOperator longBinaryOperator) {
        Objects.requireNonNull(longBinaryOperator);
        rangeCheck(jArr.length, i2, i3);
        if (i2 < i3) {
            new ArrayPrefixHelpers.LongCumulateTask(null, longBinaryOperator, jArr, i2, i3).invoke();
        }
    }

    public static void parallelPrefix(double[] dArr, DoubleBinaryOperator doubleBinaryOperator) {
        Objects.requireNonNull(doubleBinaryOperator);
        if (dArr.length > 0) {
            new ArrayPrefixHelpers.DoubleCumulateTask(null, doubleBinaryOperator, dArr, 0, dArr.length).invoke();
        }
    }

    public static void parallelPrefix(double[] dArr, int i2, int i3, DoubleBinaryOperator doubleBinaryOperator) {
        Objects.requireNonNull(doubleBinaryOperator);
        rangeCheck(dArr.length, i2, i3);
        if (i2 < i3) {
            new ArrayPrefixHelpers.DoubleCumulateTask(null, doubleBinaryOperator, dArr, i2, i3).invoke();
        }
    }

    public static void parallelPrefix(int[] iArr, IntBinaryOperator intBinaryOperator) {
        Objects.requireNonNull(intBinaryOperator);
        if (iArr.length > 0) {
            new ArrayPrefixHelpers.IntCumulateTask(null, intBinaryOperator, iArr, 0, iArr.length).invoke();
        }
    }

    public static void parallelPrefix(int[] iArr, int i2, int i3, IntBinaryOperator intBinaryOperator) {
        Objects.requireNonNull(intBinaryOperator);
        rangeCheck(iArr.length, i2, i3);
        if (i2 < i3) {
            new ArrayPrefixHelpers.IntCumulateTask(null, intBinaryOperator, iArr, i2, i3).invoke();
        }
    }

    public static int binarySearch(long[] jArr, long j2) {
        return binarySearch0(jArr, 0, jArr.length, j2);
    }

    public static int binarySearch(long[] jArr, int i2, int i3, long j2) {
        rangeCheck(jArr.length, i2, i3);
        return binarySearch0(jArr, i2, i3, j2);
    }

    private static int binarySearch0(long[] jArr, int i2, int i3, long j2) {
        int i4 = i2;
        int i5 = i3 - 1;
        while (i4 <= i5) {
            int i6 = (i4 + i5) >>> 1;
            long j3 = jArr[i6];
            if (j3 < j2) {
                i4 = i6 + 1;
            } else if (j3 > j2) {
                i5 = i6 - 1;
            } else {
                return i6;
            }
        }
        return -(i4 + 1);
    }

    public static int binarySearch(int[] iArr, int i2) {
        return binarySearch0(iArr, 0, iArr.length, i2);
    }

    public static int binarySearch(int[] iArr, int i2, int i3, int i4) {
        rangeCheck(iArr.length, i2, i3);
        return binarySearch0(iArr, i2, i3, i4);
    }

    private static int binarySearch0(int[] iArr, int i2, int i3, int i4) {
        int i5 = i2;
        int i6 = i3 - 1;
        while (i5 <= i6) {
            int i7 = (i5 + i6) >>> 1;
            int i8 = iArr[i7];
            if (i8 < i4) {
                i5 = i7 + 1;
            } else if (i8 > i4) {
                i6 = i7 - 1;
            } else {
                return i7;
            }
        }
        return -(i5 + 1);
    }

    public static int binarySearch(short[] sArr, short s2) {
        return binarySearch0(sArr, 0, sArr.length, s2);
    }

    public static int binarySearch(short[] sArr, int i2, int i3, short s2) {
        rangeCheck(sArr.length, i2, i3);
        return binarySearch0(sArr, i2, i3, s2);
    }

    private static int binarySearch0(short[] sArr, int i2, int i3, short s2) {
        int i4 = i2;
        int i5 = i3 - 1;
        while (i4 <= i5) {
            int i6 = (i4 + i5) >>> 1;
            short s3 = sArr[i6];
            if (s3 < s2) {
                i4 = i6 + 1;
            } else if (s3 > s2) {
                i5 = i6 - 1;
            } else {
                return i6;
            }
        }
        return -(i4 + 1);
    }

    public static int binarySearch(char[] cArr, char c2) {
        return binarySearch0(cArr, 0, cArr.length, c2);
    }

    public static int binarySearch(char[] cArr, int i2, int i3, char c2) {
        rangeCheck(cArr.length, i2, i3);
        return binarySearch0(cArr, i2, i3, c2);
    }

    private static int binarySearch0(char[] cArr, int i2, int i3, char c2) {
        int i4 = i2;
        int i5 = i3 - 1;
        while (i4 <= i5) {
            int i6 = (i4 + i5) >>> 1;
            char c3 = cArr[i6];
            if (c3 < c2) {
                i4 = i6 + 1;
            } else if (c3 > c2) {
                i5 = i6 - 1;
            } else {
                return i6;
            }
        }
        return -(i4 + 1);
    }

    public static int binarySearch(byte[] bArr, byte b2) {
        return binarySearch0(bArr, 0, bArr.length, b2);
    }

    public static int binarySearch(byte[] bArr, int i2, int i3, byte b2) {
        rangeCheck(bArr.length, i2, i3);
        return binarySearch0(bArr, i2, i3, b2);
    }

    private static int binarySearch0(byte[] bArr, int i2, int i3, byte b2) {
        int i4 = i2;
        int i5 = i3 - 1;
        while (i4 <= i5) {
            int i6 = (i4 + i5) >>> 1;
            byte b3 = bArr[i6];
            if (b3 < b2) {
                i4 = i6 + 1;
            } else if (b3 > b2) {
                i5 = i6 - 1;
            } else {
                return i6;
            }
        }
        return -(i4 + 1);
    }

    public static int binarySearch(double[] dArr, double d2) {
        return binarySearch0(dArr, 0, dArr.length, d2);
    }

    public static int binarySearch(double[] dArr, int i2, int i3, double d2) {
        rangeCheck(dArr.length, i2, i3);
        return binarySearch0(dArr, i2, i3, d2);
    }

    private static int binarySearch0(double[] dArr, int i2, int i3, double d2) {
        int i4 = i2;
        int i5 = i3 - 1;
        while (i4 <= i5) {
            int i6 = (i4 + i5) >>> 1;
            double d3 = dArr[i6];
            if (d3 < d2) {
                i4 = i6 + 1;
            } else if (d3 > d2) {
                i5 = i6 - 1;
            } else {
                long jDoubleToLongBits = Double.doubleToLongBits(d3);
                long jDoubleToLongBits2 = Double.doubleToLongBits(d2);
                if (jDoubleToLongBits == jDoubleToLongBits2) {
                    return i6;
                }
                if (jDoubleToLongBits < jDoubleToLongBits2) {
                    i4 = i6 + 1;
                } else {
                    i5 = i6 - 1;
                }
            }
        }
        return -(i4 + 1);
    }

    public static int binarySearch(float[] fArr, float f2) {
        return binarySearch0(fArr, 0, fArr.length, f2);
    }

    public static int binarySearch(float[] fArr, int i2, int i3, float f2) {
        rangeCheck(fArr.length, i2, i3);
        return binarySearch0(fArr, i2, i3, f2);
    }

    private static int binarySearch0(float[] fArr, int i2, int i3, float f2) {
        int i4 = i2;
        int i5 = i3 - 1;
        while (i4 <= i5) {
            int i6 = (i4 + i5) >>> 1;
            float f3 = fArr[i6];
            if (f3 < f2) {
                i4 = i6 + 1;
            } else if (f3 > f2) {
                i5 = i6 - 1;
            } else {
                int iFloatToIntBits = Float.floatToIntBits(f3);
                int iFloatToIntBits2 = Float.floatToIntBits(f2);
                if (iFloatToIntBits == iFloatToIntBits2) {
                    return i6;
                }
                if (iFloatToIntBits < iFloatToIntBits2) {
                    i4 = i6 + 1;
                } else {
                    i5 = i6 - 1;
                }
            }
        }
        return -(i4 + 1);
    }

    public static int binarySearch(Object[] objArr, Object obj) {
        return binarySearch0(objArr, 0, objArr.length, obj);
    }

    public static int binarySearch(Object[] objArr, int i2, int i3, Object obj) {
        rangeCheck(objArr.length, i2, i3);
        return binarySearch0(objArr, i2, i3, obj);
    }

    private static int binarySearch0(Object[] objArr, int i2, int i3, Object obj) {
        int i4 = i2;
        int i5 = i3 - 1;
        while (i4 <= i5) {
            int i6 = (i4 + i5) >>> 1;
            int iCompareTo = ((Comparable) objArr[i6]).compareTo(obj);
            if (iCompareTo < 0) {
                i4 = i6 + 1;
            } else if (iCompareTo > 0) {
                i5 = i6 - 1;
            } else {
                return i6;
            }
        }
        return -(i4 + 1);
    }

    public static <T> int binarySearch(T[] tArr, T t2, Comparator<? super T> comparator) {
        return binarySearch0(tArr, 0, tArr.length, t2, comparator);
    }

    public static <T> int binarySearch(T[] tArr, int i2, int i3, T t2, Comparator<? super T> comparator) {
        rangeCheck(tArr.length, i2, i3);
        return binarySearch0(tArr, i2, i3, t2, comparator);
    }

    private static <T> int binarySearch0(T[] tArr, int i2, int i3, T t2, Comparator<? super T> comparator) {
        if (comparator == null) {
            return binarySearch0(tArr, i2, i3, t2);
        }
        int i4 = i2;
        int i5 = i3 - 1;
        while (i4 <= i5) {
            int i6 = (i4 + i5) >>> 1;
            int iCompare = comparator.compare(tArr[i6], t2);
            if (iCompare < 0) {
                i4 = i6 + 1;
            } else if (iCompare > 0) {
                i5 = i6 - 1;
            } else {
                return i6;
            }
        }
        return -(i4 + 1);
    }

    public static boolean equals(long[] jArr, long[] jArr2) {
        int length;
        if (jArr == jArr2) {
            return true;
        }
        if (jArr == null || jArr2 == null || jArr2.length != (length = jArr.length)) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (jArr[i2] != jArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(int[] iArr, int[] iArr2) {
        int length;
        if (iArr == iArr2) {
            return true;
        }
        if (iArr == null || iArr2 == null || iArr2.length != (length = iArr.length)) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (iArr[i2] != iArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(short[] sArr, short[] sArr2) {
        int length;
        if (sArr == sArr2) {
            return true;
        }
        if (sArr == null || sArr2 == null || sArr2.length != (length = sArr.length)) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (sArr[i2] != sArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(char[] cArr, char[] cArr2) {
        int length;
        if (cArr == cArr2) {
            return true;
        }
        if (cArr == null || cArr2 == null || cArr2.length != (length = cArr.length)) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (cArr[i2] != cArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(byte[] bArr, byte[] bArr2) {
        int length;
        if (bArr == bArr2) {
            return true;
        }
        if (bArr == null || bArr2 == null || bArr2.length != (length = bArr.length)) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (bArr[i2] != bArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(boolean[] zArr, boolean[] zArr2) {
        int length;
        if (zArr == zArr2) {
            return true;
        }
        if (zArr == null || zArr2 == null || zArr2.length != (length = zArr.length)) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (zArr[i2] != zArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(double[] dArr, double[] dArr2) {
        int length;
        if (dArr == dArr2) {
            return true;
        }
        if (dArr == null || dArr2 == null || dArr2.length != (length = dArr.length)) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (Double.doubleToLongBits(dArr[i2]) != Double.doubleToLongBits(dArr2[i2])) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(float[] fArr, float[] fArr2) {
        int length;
        if (fArr == fArr2) {
            return true;
        }
        if (fArr == null || fArr2 == null || fArr2.length != (length = fArr.length)) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            if (Float.floatToIntBits(fArr[i2]) != Float.floatToIntBits(fArr2[i2])) {
                return false;
            }
        }
        return true;
    }

    public static boolean equals(Object[] objArr, Object[] objArr2) {
        int length;
        if (objArr == objArr2) {
            return true;
        }
        if (objArr == null || objArr2 == null || objArr2.length != (length = objArr.length)) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            Object obj = objArr[i2];
            Object obj2 = objArr2[i2];
            if (obj == null) {
                if (obj2 != null) {
                    return false;
                }
            } else if (!obj.equals(obj2)) {
                return false;
            }
        }
        return true;
    }

    public static void fill(long[] jArr, long j2) {
        int length = jArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            jArr[i2] = j2;
        }
    }

    public static void fill(long[] jArr, int i2, int i3, long j2) {
        rangeCheck(jArr.length, i2, i3);
        for (int i4 = i2; i4 < i3; i4++) {
            jArr[i4] = j2;
        }
    }

    public static void fill(int[] iArr, int i2) {
        int length = iArr.length;
        for (int i3 = 0; i3 < length; i3++) {
            iArr[i3] = i2;
        }
    }

    public static void fill(int[] iArr, int i2, int i3, int i4) {
        rangeCheck(iArr.length, i2, i3);
        for (int i5 = i2; i5 < i3; i5++) {
            iArr[i5] = i4;
        }
    }

    public static void fill(short[] sArr, short s2) {
        int length = sArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            sArr[i2] = s2;
        }
    }

    public static void fill(short[] sArr, int i2, int i3, short s2) {
        rangeCheck(sArr.length, i2, i3);
        for (int i4 = i2; i4 < i3; i4++) {
            sArr[i4] = s2;
        }
    }

    public static void fill(char[] cArr, char c2) {
        int length = cArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            cArr[i2] = c2;
        }
    }

    public static void fill(char[] cArr, int i2, int i3, char c2) {
        rangeCheck(cArr.length, i2, i3);
        for (int i4 = i2; i4 < i3; i4++) {
            cArr[i4] = c2;
        }
    }

    public static void fill(byte[] bArr, byte b2) {
        int length = bArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            bArr[i2] = b2;
        }
    }

    public static void fill(byte[] bArr, int i2, int i3, byte b2) {
        rangeCheck(bArr.length, i2, i3);
        for (int i4 = i2; i4 < i3; i4++) {
            bArr[i4] = b2;
        }
    }

    public static void fill(boolean[] zArr, boolean z2) {
        int length = zArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            zArr[i2] = z2;
        }
    }

    public static void fill(boolean[] zArr, int i2, int i3, boolean z2) {
        rangeCheck(zArr.length, i2, i3);
        for (int i4 = i2; i4 < i3; i4++) {
            zArr[i4] = z2;
        }
    }

    public static void fill(double[] dArr, double d2) {
        int length = dArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            dArr[i2] = d2;
        }
    }

    public static void fill(double[] dArr, int i2, int i3, double d2) {
        rangeCheck(dArr.length, i2, i3);
        for (int i4 = i2; i4 < i3; i4++) {
            dArr[i4] = d2;
        }
    }

    public static void fill(float[] fArr, float f2) {
        int length = fArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            fArr[i2] = f2;
        }
    }

    public static void fill(float[] fArr, int i2, int i3, float f2) {
        rangeCheck(fArr.length, i2, i3);
        for (int i4 = i2; i4 < i3; i4++) {
            fArr[i4] = f2;
        }
    }

    public static void fill(Object[] objArr, Object obj) {
        int length = objArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            objArr[i2] = obj;
        }
    }

    public static void fill(Object[] objArr, int i2, int i3, Object obj) {
        rangeCheck(objArr.length, i2, i3);
        for (int i4 = i2; i4 < i3; i4++) {
            objArr[i4] = obj;
        }
    }

    public static <T> T[] copyOf(T[] tArr, int i2) {
        return (T[]) copyOf(tArr, i2, tArr.getClass());
    }

    public static <T, U> T[] copyOf(U[] uArr, int i2, Class<? extends T[]> cls) {
        T[] tArr = (T[]) (cls == Object[].class ? new Object[i2] : (Object[]) Array.newInstance(cls.getComponentType(), i2));
        System.arraycopy(uArr, 0, tArr, 0, Math.min(uArr.length, i2));
        return tArr;
    }

    public static byte[] copyOf(byte[] bArr, int i2) {
        byte[] bArr2 = new byte[i2];
        System.arraycopy(bArr, 0, bArr2, 0, Math.min(bArr.length, i2));
        return bArr2;
    }

    public static short[] copyOf(short[] sArr, int i2) {
        short[] sArr2 = new short[i2];
        System.arraycopy(sArr, 0, sArr2, 0, Math.min(sArr.length, i2));
        return sArr2;
    }

    public static int[] copyOf(int[] iArr, int i2) {
        int[] iArr2 = new int[i2];
        System.arraycopy(iArr, 0, iArr2, 0, Math.min(iArr.length, i2));
        return iArr2;
    }

    public static long[] copyOf(long[] jArr, int i2) {
        long[] jArr2 = new long[i2];
        System.arraycopy(jArr, 0, jArr2, 0, Math.min(jArr.length, i2));
        return jArr2;
    }

    public static char[] copyOf(char[] cArr, int i2) {
        char[] cArr2 = new char[i2];
        System.arraycopy(cArr, 0, cArr2, 0, Math.min(cArr.length, i2));
        return cArr2;
    }

    public static float[] copyOf(float[] fArr, int i2) {
        float[] fArr2 = new float[i2];
        System.arraycopy(fArr, 0, fArr2, 0, Math.min(fArr.length, i2));
        return fArr2;
    }

    public static double[] copyOf(double[] dArr, int i2) {
        double[] dArr2 = new double[i2];
        System.arraycopy(dArr, 0, dArr2, 0, Math.min(dArr.length, i2));
        return dArr2;
    }

    public static boolean[] copyOf(boolean[] zArr, int i2) {
        boolean[] zArr2 = new boolean[i2];
        System.arraycopy(zArr, 0, zArr2, 0, Math.min(zArr.length, i2));
        return zArr2;
    }

    public static <T> T[] copyOfRange(T[] tArr, int i2, int i3) {
        return (T[]) copyOfRange(tArr, i2, i3, tArr.getClass());
    }

    public static <T, U> T[] copyOfRange(U[] uArr, int i2, int i3, Class<? extends T[]> cls) {
        int i4 = i3 - i2;
        if (i4 < 0) {
            throw new IllegalArgumentException(i2 + " > " + i3);
        }
        T[] tArr = (T[]) (cls == Object[].class ? new Object[i4] : (Object[]) Array.newInstance(cls.getComponentType(), i4));
        System.arraycopy(uArr, i2, tArr, 0, Math.min(uArr.length - i2, i4));
        return tArr;
    }

    public static byte[] copyOfRange(byte[] bArr, int i2, int i3) {
        int i4 = i3 - i2;
        if (i4 < 0) {
            throw new IllegalArgumentException(i2 + " > " + i3);
        }
        byte[] bArr2 = new byte[i4];
        System.arraycopy(bArr, i2, bArr2, 0, Math.min(bArr.length - i2, i4));
        return bArr2;
    }

    public static short[] copyOfRange(short[] sArr, int i2, int i3) {
        int i4 = i3 - i2;
        if (i4 < 0) {
            throw new IllegalArgumentException(i2 + " > " + i3);
        }
        short[] sArr2 = new short[i4];
        System.arraycopy(sArr, i2, sArr2, 0, Math.min(sArr.length - i2, i4));
        return sArr2;
    }

    public static int[] copyOfRange(int[] iArr, int i2, int i3) {
        int i4 = i3 - i2;
        if (i4 < 0) {
            throw new IllegalArgumentException(i2 + " > " + i3);
        }
        int[] iArr2 = new int[i4];
        System.arraycopy(iArr, i2, iArr2, 0, Math.min(iArr.length - i2, i4));
        return iArr2;
    }

    public static long[] copyOfRange(long[] jArr, int i2, int i3) {
        int i4 = i3 - i2;
        if (i4 < 0) {
            throw new IllegalArgumentException(i2 + " > " + i3);
        }
        long[] jArr2 = new long[i4];
        System.arraycopy(jArr, i2, jArr2, 0, Math.min(jArr.length - i2, i4));
        return jArr2;
    }

    public static char[] copyOfRange(char[] cArr, int i2, int i3) {
        int i4 = i3 - i2;
        if (i4 < 0) {
            throw new IllegalArgumentException(i2 + " > " + i3);
        }
        char[] cArr2 = new char[i4];
        System.arraycopy(cArr, i2, cArr2, 0, Math.min(cArr.length - i2, i4));
        return cArr2;
    }

    public static float[] copyOfRange(float[] fArr, int i2, int i3) {
        int i4 = i3 - i2;
        if (i4 < 0) {
            throw new IllegalArgumentException(i2 + " > " + i3);
        }
        float[] fArr2 = new float[i4];
        System.arraycopy(fArr, i2, fArr2, 0, Math.min(fArr.length - i2, i4));
        return fArr2;
    }

    public static double[] copyOfRange(double[] dArr, int i2, int i3) {
        int i4 = i3 - i2;
        if (i4 < 0) {
            throw new IllegalArgumentException(i2 + " > " + i3);
        }
        double[] dArr2 = new double[i4];
        System.arraycopy(dArr, i2, dArr2, 0, Math.min(dArr.length - i2, i4));
        return dArr2;
    }

    public static boolean[] copyOfRange(boolean[] zArr, int i2, int i3) {
        int i4 = i3 - i2;
        if (i4 < 0) {
            throw new IllegalArgumentException(i2 + " > " + i3);
        }
        boolean[] zArr2 = new boolean[i4];
        System.arraycopy(zArr, i2, zArr2, 0, Math.min(zArr.length - i2, i4));
        return zArr2;
    }

    @SafeVarargs
    public static <T> List<T> asList(T... tArr) {
        return new ArrayList(tArr);
    }

    /* loaded from: rt.jar:java/util/Arrays$ArrayList.class */
    private static class ArrayList<E> extends AbstractList<E> implements RandomAccess, Serializable {
        private static final long serialVersionUID = -2764017481108945198L;

        /* renamed from: a, reason: collision with root package name */
        private final E[] f12495a;

        ArrayList(E[] eArr) {
            this.f12495a = (E[]) ((Object[]) Objects.requireNonNull(eArr));
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public int size() {
            return this.f12495a.length;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.List
        public Object[] toArray() {
            return (Object[]) this.f12495a.clone();
        }

        @Override // java.util.AbstractCollection, java.util.Collection
        public <T> T[] toArray(T[] tArr) {
            int size = size();
            if (tArr.length < size) {
                return (T[]) Arrays.copyOf(this.f12495a, size, tArr.getClass());
            }
            System.arraycopy(this.f12495a, 0, tArr, 0, size);
            if (tArr.length > size) {
                tArr[size] = null;
            }
            return tArr;
        }

        @Override // java.util.AbstractList, java.util.List
        public E get(int i2) {
            return this.f12495a[i2];
        }

        @Override // java.util.AbstractList, java.util.List
        public E set(int i2, E e2) {
            E e3 = this.f12495a[i2];
            this.f12495a[i2] = e2;
            return e3;
        }

        @Override // java.util.AbstractList, java.util.List
        public int indexOf(Object obj) {
            E[] eArr = this.f12495a;
            if (obj == null) {
                for (int i2 = 0; i2 < eArr.length; i2++) {
                    if (eArr[i2] == null) {
                        return i2;
                    }
                }
                return -1;
            }
            for (int i3 = 0; i3 < eArr.length; i3++) {
                if (obj.equals(eArr[i3])) {
                    return i3;
                }
            }
            return -1;
        }

        @Override // java.util.AbstractCollection, java.util.Collection, java.util.Set
        public boolean contains(Object obj) {
            return indexOf(obj) != -1;
        }

        @Override // java.util.Collection, java.lang.Iterable
        public Spliterator<E> spliterator() {
            return Spliterators.spliterator(this.f12495a, 16);
        }

        @Override // java.lang.Iterable
        public void forEach(Consumer<? super E> consumer) {
            Objects.requireNonNull(consumer);
            for (a aVar : this.f12495a) {
                consumer.accept(aVar);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.List
        public void replaceAll(UnaryOperator<E> unaryOperator) {
            Objects.requireNonNull(unaryOperator);
            E[] eArr = this.f12495a;
            for (int i2 = 0; i2 < eArr.length; i2++) {
                eArr[i2] = unaryOperator.apply(eArr[i2]);
            }
        }

        @Override // java.util.List, com.sun.javafx.collections.SortableList
        public void sort(Comparator<? super E> comparator) {
            Arrays.sort(this.f12495a, comparator);
        }
    }

    public static int hashCode(long[] jArr) {
        if (jArr == null) {
            return 0;
        }
        int i2 = 1;
        for (long j2 : jArr) {
            i2 = (31 * i2) + ((int) (j2 ^ (j2 >>> 32)));
        }
        return i2;
    }

    public static int hashCode(int[] iArr) {
        if (iArr == null) {
            return 0;
        }
        int i2 = 1;
        for (int i3 : iArr) {
            i2 = (31 * i2) + i3;
        }
        return i2;
    }

    public static int hashCode(short[] sArr) {
        if (sArr == null) {
            return 0;
        }
        int i2 = 1;
        for (short s2 : sArr) {
            i2 = (31 * i2) + s2;
        }
        return i2;
    }

    public static int hashCode(char[] cArr) {
        if (cArr == null) {
            return 0;
        }
        int i2 = 1;
        for (char c2 : cArr) {
            i2 = (31 * i2) + c2;
        }
        return i2;
    }

    public static int hashCode(byte[] bArr) {
        if (bArr == null) {
            return 0;
        }
        int i2 = 1;
        for (byte b2 : bArr) {
            i2 = (31 * i2) + b2;
        }
        return i2;
    }

    public static int hashCode(boolean[] zArr) {
        if (zArr == null) {
            return 0;
        }
        int i2 = 1;
        for (boolean z2 : zArr) {
            i2 = (31 * i2) + (z2 ? 1231 : 1237);
        }
        return i2;
    }

    public static int hashCode(float[] fArr) {
        if (fArr == null) {
            return 0;
        }
        int iFloatToIntBits = 1;
        for (float f2 : fArr) {
            iFloatToIntBits = (31 * iFloatToIntBits) + Float.floatToIntBits(f2);
        }
        return iFloatToIntBits;
    }

    public static int hashCode(double[] dArr) {
        if (dArr == null) {
            return 0;
        }
        int i2 = 1;
        for (double d2 : dArr) {
            long jDoubleToLongBits = Double.doubleToLongBits(d2);
            i2 = (31 * i2) + ((int) (jDoubleToLongBits ^ (jDoubleToLongBits >>> 32)));
        }
        return i2;
    }

    public static int hashCode(Object[] objArr) {
        if (objArr == null) {
            return 0;
        }
        int iHashCode = 1;
        int length = objArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            Object obj = objArr[i2];
            iHashCode = (31 * iHashCode) + (obj == null ? 0 : obj.hashCode());
        }
        return iHashCode;
    }

    public static int deepHashCode(Object[] objArr) {
        if (objArr == null) {
            return 0;
        }
        int i2 = 1;
        for (Object obj : objArr) {
            int iHashCode = 0;
            if (obj instanceof Object[]) {
                iHashCode = deepHashCode((Object[]) obj);
            } else if (obj instanceof byte[]) {
                iHashCode = hashCode((byte[]) obj);
            } else if (obj instanceof short[]) {
                iHashCode = hashCode((short[]) obj);
            } else if (obj instanceof int[]) {
                iHashCode = hashCode((int[]) obj);
            } else if (obj instanceof long[]) {
                iHashCode = hashCode((long[]) obj);
            } else if (obj instanceof char[]) {
                iHashCode = hashCode((char[]) obj);
            } else if (obj instanceof float[]) {
                iHashCode = hashCode((float[]) obj);
            } else if (obj instanceof double[]) {
                iHashCode = hashCode((double[]) obj);
            } else if (obj instanceof boolean[]) {
                iHashCode = hashCode((boolean[]) obj);
            } else if (obj != null) {
                iHashCode = obj.hashCode();
            }
            i2 = (31 * i2) + iHashCode;
        }
        return i2;
    }

    public static boolean deepEquals(Object[] objArr, Object[] objArr2) {
        int length;
        if (objArr == objArr2) {
            return true;
        }
        if (objArr == null || objArr2 == null || objArr2.length != (length = objArr.length)) {
            return false;
        }
        for (int i2 = 0; i2 < length; i2++) {
            Object obj = objArr[i2];
            Object obj2 = objArr2[i2];
            if (obj != obj2 && (obj == null || !deepEquals0(obj, obj2))) {
                return false;
            }
        }
        return true;
    }

    static boolean deepEquals0(Object obj, Object obj2) {
        boolean zEquals;
        if (!$assertionsDisabled && obj == null) {
            throw new AssertionError();
        }
        if ((obj instanceof Object[]) && (obj2 instanceof Object[])) {
            zEquals = deepEquals((Object[]) obj, (Object[]) obj2);
        } else if ((obj instanceof byte[]) && (obj2 instanceof byte[])) {
            zEquals = equals((byte[]) obj, (byte[]) obj2);
        } else if ((obj instanceof short[]) && (obj2 instanceof short[])) {
            zEquals = equals((short[]) obj, (short[]) obj2);
        } else if ((obj instanceof int[]) && (obj2 instanceof int[])) {
            zEquals = equals((int[]) obj, (int[]) obj2);
        } else if ((obj instanceof long[]) && (obj2 instanceof long[])) {
            zEquals = equals((long[]) obj, (long[]) obj2);
        } else if ((obj instanceof char[]) && (obj2 instanceof char[])) {
            zEquals = equals((char[]) obj, (char[]) obj2);
        } else if ((obj instanceof float[]) && (obj2 instanceof float[])) {
            zEquals = equals((float[]) obj, (float[]) obj2);
        } else if ((obj instanceof double[]) && (obj2 instanceof double[])) {
            zEquals = equals((double[]) obj, (double[]) obj2);
        } else if ((obj instanceof boolean[]) && (obj2 instanceof boolean[])) {
            zEquals = equals((boolean[]) obj, (boolean[]) obj2);
        } else {
            zEquals = obj.equals(obj2);
        }
        return zEquals;
    }

    public static String toString(long[] jArr) {
        if (jArr == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        int length = jArr.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int i2 = 0;
        while (true) {
            sb.append(jArr[i2]);
            if (i2 == length) {
                return sb.append(']').toString();
            }
            sb.append(", ");
            i2++;
        }
    }

    public static String toString(int[] iArr) {
        if (iArr == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        int length = iArr.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int i2 = 0;
        while (true) {
            sb.append(iArr[i2]);
            if (i2 == length) {
                return sb.append(']').toString();
            }
            sb.append(", ");
            i2++;
        }
    }

    public static String toString(short[] sArr) {
        if (sArr == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        int length = sArr.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int i2 = 0;
        while (true) {
            sb.append((int) sArr[i2]);
            if (i2 == length) {
                return sb.append(']').toString();
            }
            sb.append(", ");
            i2++;
        }
    }

    public static String toString(char[] cArr) {
        if (cArr == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        int length = cArr.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int i2 = 0;
        while (true) {
            sb.append(cArr[i2]);
            if (i2 == length) {
                return sb.append(']').toString();
            }
            sb.append(", ");
            i2++;
        }
    }

    public static String toString(byte[] bArr) {
        if (bArr == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        int length = bArr.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int i2 = 0;
        while (true) {
            sb.append((int) bArr[i2]);
            if (i2 == length) {
                return sb.append(']').toString();
            }
            sb.append(", ");
            i2++;
        }
    }

    public static String toString(boolean[] zArr) {
        if (zArr == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        int length = zArr.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int i2 = 0;
        while (true) {
            sb.append(zArr[i2]);
            if (i2 == length) {
                return sb.append(']').toString();
            }
            sb.append(", ");
            i2++;
        }
    }

    public static String toString(float[] fArr) {
        if (fArr == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        int length = fArr.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int i2 = 0;
        while (true) {
            sb.append(fArr[i2]);
            if (i2 == length) {
                return sb.append(']').toString();
            }
            sb.append(", ");
            i2++;
        }
    }

    public static String toString(double[] dArr) {
        if (dArr == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        int length = dArr.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int i2 = 0;
        while (true) {
            sb.append(dArr[i2]);
            if (i2 == length) {
                return sb.append(']').toString();
            }
            sb.append(", ");
            i2++;
        }
    }

    public static String toString(Object[] objArr) {
        if (objArr == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        int length = objArr.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int i2 = 0;
        while (true) {
            sb.append(String.valueOf(objArr[i2]));
            if (i2 == length) {
                return sb.append(']').toString();
            }
            sb.append(", ");
            i2++;
        }
    }

    public static String deepToString(Object[] objArr) {
        if (objArr == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        int length = 20 * objArr.length;
        if (objArr.length != 0 && length <= 0) {
            length = Integer.MAX_VALUE;
        }
        StringBuilder sb = new StringBuilder(length);
        deepToString(objArr, sb, new HashSet());
        return sb.toString();
    }

    private static void deepToString(Object[] objArr, StringBuilder sb, Set<Object[]> set) {
        if (objArr == null) {
            sb.append(FXMLLoader.NULL_KEYWORD);
            return;
        }
        int length = objArr.length - 1;
        if (length == -1) {
            sb.append("[]");
            return;
        }
        set.add(objArr);
        sb.append('[');
        int i2 = 0;
        while (true) {
            Object obj = objArr[i2];
            if (obj == null) {
                sb.append(FXMLLoader.NULL_KEYWORD);
            } else {
                Class<?> cls = obj.getClass();
                if (cls.isArray()) {
                    if (cls == byte[].class) {
                        sb.append(toString((byte[]) obj));
                    } else if (cls == short[].class) {
                        sb.append(toString((short[]) obj));
                    } else if (cls == int[].class) {
                        sb.append(toString((int[]) obj));
                    } else if (cls == long[].class) {
                        sb.append(toString((long[]) obj));
                    } else if (cls == char[].class) {
                        sb.append(toString((char[]) obj));
                    } else if (cls == float[].class) {
                        sb.append(toString((float[]) obj));
                    } else if (cls == double[].class) {
                        sb.append(toString((double[]) obj));
                    } else if (cls == boolean[].class) {
                        sb.append(toString((boolean[]) obj));
                    } else if (set.contains(obj)) {
                        sb.append("[...]");
                    } else {
                        deepToString((Object[]) obj, sb, set);
                    }
                } else {
                    sb.append(obj.toString());
                }
            }
            if (i2 != length) {
                sb.append(", ");
                i2++;
            } else {
                sb.append(']');
                set.remove(objArr);
                return;
            }
        }
    }

    public static <T> void setAll(T[] tArr, IntFunction<? extends T> intFunction) {
        Objects.requireNonNull(intFunction);
        for (int i2 = 0; i2 < tArr.length; i2++) {
            tArr[i2] = intFunction.apply(i2);
        }
    }

    public static <T> void parallelSetAll(T[] tArr, IntFunction<? extends T> intFunction) {
        Objects.requireNonNull(intFunction);
        IntStream.range(0, tArr.length).parallel().forEach(i2 -> {
            tArr[i2] = intFunction.apply(i2);
        });
    }

    public static void setAll(int[] iArr, IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr[i2] = intUnaryOperator.applyAsInt(i2);
        }
    }

    public static void parallelSetAll(int[] iArr, IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        IntStream.range(0, iArr.length).parallel().forEach(i2 -> {
            iArr[i2] = intUnaryOperator.applyAsInt(i2);
        });
    }

    public static void setAll(long[] jArr, IntToLongFunction intToLongFunction) {
        Objects.requireNonNull(intToLongFunction);
        for (int i2 = 0; i2 < jArr.length; i2++) {
            jArr[i2] = intToLongFunction.applyAsLong(i2);
        }
    }

    public static void parallelSetAll(long[] jArr, IntToLongFunction intToLongFunction) {
        Objects.requireNonNull(intToLongFunction);
        IntStream.range(0, jArr.length).parallel().forEach(i2 -> {
            jArr[i2] = intToLongFunction.applyAsLong(i2);
        });
    }

    public static void setAll(double[] dArr, IntToDoubleFunction intToDoubleFunction) {
        Objects.requireNonNull(intToDoubleFunction);
        for (int i2 = 0; i2 < dArr.length; i2++) {
            dArr[i2] = intToDoubleFunction.applyAsDouble(i2);
        }
    }

    public static void parallelSetAll(double[] dArr, IntToDoubleFunction intToDoubleFunction) {
        Objects.requireNonNull(intToDoubleFunction);
        IntStream.range(0, dArr.length).parallel().forEach(i2 -> {
            dArr[i2] = intToDoubleFunction.applyAsDouble(i2);
        });
    }

    public static <T> Spliterator<T> spliterator(T[] tArr) {
        return Spliterators.spliterator(tArr, EncodingConstants.INTEGER_4TH_BIT_MEDIUM_LIMIT);
    }

    public static <T> Spliterator<T> spliterator(T[] tArr, int i2, int i3) {
        return Spliterators.spliterator(tArr, i2, i3, EncodingConstants.INTEGER_4TH_BIT_MEDIUM_LIMIT);
    }

    public static Spliterator.OfInt spliterator(int[] iArr) {
        return Spliterators.spliterator(iArr, EncodingConstants.INTEGER_4TH_BIT_MEDIUM_LIMIT);
    }

    public static Spliterator.OfInt spliterator(int[] iArr, int i2, int i3) {
        return Spliterators.spliterator(iArr, i2, i3, EncodingConstants.INTEGER_4TH_BIT_MEDIUM_LIMIT);
    }

    public static Spliterator.OfLong spliterator(long[] jArr) {
        return Spliterators.spliterator(jArr, EncodingConstants.INTEGER_4TH_BIT_MEDIUM_LIMIT);
    }

    public static Spliterator.OfLong spliterator(long[] jArr, int i2, int i3) {
        return Spliterators.spliterator(jArr, i2, i3, EncodingConstants.INTEGER_4TH_BIT_MEDIUM_LIMIT);
    }

    public static Spliterator.OfDouble spliterator(double[] dArr) {
        return Spliterators.spliterator(dArr, EncodingConstants.INTEGER_4TH_BIT_MEDIUM_LIMIT);
    }

    public static Spliterator.OfDouble spliterator(double[] dArr, int i2, int i3) {
        return Spliterators.spliterator(dArr, i2, i3, EncodingConstants.INTEGER_4TH_BIT_MEDIUM_LIMIT);
    }

    public static <T> Stream<T> stream(T[] tArr) {
        return stream(tArr, 0, tArr.length);
    }

    public static <T> Stream<T> stream(T[] tArr, int i2, int i3) {
        return StreamSupport.stream(spliterator(tArr, i2, i3), false);
    }

    public static IntStream stream(int[] iArr) {
        return stream(iArr, 0, iArr.length);
    }

    public static IntStream stream(int[] iArr, int i2, int i3) {
        return StreamSupport.intStream(spliterator(iArr, i2, i3), false);
    }

    public static LongStream stream(long[] jArr) {
        return stream(jArr, 0, jArr.length);
    }

    public static LongStream stream(long[] jArr, int i2, int i3) {
        return StreamSupport.longStream(spliterator(jArr, i2, i3), false);
    }

    public static DoubleStream stream(double[] dArr) {
        return stream(dArr, 0, dArr.length);
    }

    public static DoubleStream stream(double[] dArr, int i2, int i3) {
        return StreamSupport.doubleStream(spliterator(dArr, i2, i3), false);
    }
}
