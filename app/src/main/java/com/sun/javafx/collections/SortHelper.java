package com.sun.javafx.collections;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/* loaded from: jfxrt.jar:com/sun/javafx/collections/SortHelper.class */
public class SortHelper {
    private int[] permutation;
    private int[] reversePermutation;
    private static final int INSERTIONSORT_THRESHOLD = 7;

    /* JADX WARN: Multi-variable type inference failed */
    public <T extends Comparable<? super T>> int[] sort(List<T> list) {
        try {
            Comparable[] comparableArr = (Comparable[]) list.toArray((Comparable[]) Array.newInstance((Class<?>) Comparable.class, list.size()));
            int[] iArrSort = sort(comparableArr);
            ListIterator<T> listIterator = list.listIterator();
            for (Comparable comparable : comparableArr) {
                listIterator.next();
                listIterator.set(comparable);
            }
            return iArrSort;
        } catch (ArrayStoreException e2) {
            throw new ClassCastException();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <T> int[] sort(List<T> list, Comparator<? super T> c2) {
        Object[] a2 = list.toArray();
        int[] result = sort(a2, c2);
        ListIterator i2 = list.listIterator();
        for (Object obj : a2) {
            i2.next();
            i2.set(obj);
        }
        return result;
    }

    public <T extends Comparable<? super T>> int[] sort(T[] a2) {
        return sort(a2, (Comparator) null);
    }

    public <T> int[] sort(T[] a2, Comparator<? super T> c2) {
        Object[] objArr = (Object[]) a2.clone();
        int[] result = initPermutation(a2.length);
        if (c2 == null) {
            mergeSort(objArr, a2, 0, a2.length, 0);
        } else {
            mergeSort(objArr, a2, 0, a2.length, 0, c2);
        }
        this.reversePermutation = null;
        this.permutation = null;
        return result;
    }

    public <T> int[] sort(T[] a2, int fromIndex, int toIndex, Comparator<? super T> c2) {
        rangeCheck(a2.length, fromIndex, toIndex);
        Object[] objArrCopyOfRange = copyOfRange(a2, fromIndex, toIndex);
        int[] result = initPermutation(a2.length);
        if (c2 == null) {
            mergeSort(objArrCopyOfRange, a2, fromIndex, toIndex, -fromIndex);
        } else {
            mergeSort(objArrCopyOfRange, a2, fromIndex, toIndex, -fromIndex, c2);
        }
        this.reversePermutation = null;
        this.permutation = null;
        return Arrays.copyOfRange(result, fromIndex, toIndex);
    }

    public int[] sort(int[] a2, int fromIndex, int toIndex) {
        rangeCheck(a2.length, fromIndex, toIndex);
        int[] aux = copyOfRange(a2, fromIndex, toIndex);
        int[] result = initPermutation(a2.length);
        mergeSort(aux, a2, fromIndex, toIndex, -fromIndex);
        this.reversePermutation = null;
        this.permutation = null;
        return Arrays.copyOfRange(result, fromIndex, toIndex);
    }

    private static void rangeCheck(int arrayLen, int fromIndex, int toIndex) {
        if (fromIndex > toIndex) {
            throw new IllegalArgumentException("fromIndex(" + fromIndex + ") > toIndex(" + toIndex + ")");
        }
        if (fromIndex < 0) {
            throw new ArrayIndexOutOfBoundsException(fromIndex);
        }
        if (toIndex > arrayLen) {
            throw new ArrayIndexOutOfBoundsException(toIndex);
        }
    }

    private static int[] copyOfRange(int[] original, int from, int to) {
        int newLength = to - from;
        if (newLength < 0) {
            throw new IllegalArgumentException(from + " > " + to);
        }
        int[] copy = new int[newLength];
        System.arraycopy(original, from, copy, 0, Math.min(original.length - from, newLength));
        return copy;
    }

    private static <T> T[] copyOfRange(T[] tArr, int i2, int i3) {
        return (T[]) copyOfRange(tArr, i2, i3, tArr.getClass());
    }

    private static <T, U> T[] copyOfRange(U[] uArr, int i2, int i3, Class<? extends T[]> cls) {
        int i4 = i3 - i2;
        if (i4 < 0) {
            throw new IllegalArgumentException(i2 + " > " + i3);
        }
        T[] tArr = (T[]) (cls == Object[].class ? new Object[i4] : (Object[]) Array.newInstance(cls.getComponentType(), i4));
        System.arraycopy(uArr, i2, tArr, 0, Math.min(uArr.length - i2, i4));
        return tArr;
    }

    private void mergeSort(int[] src, int[] dest, int low, int high, int off) {
        int length = high - low;
        if (length < 7) {
            for (int i2 = low; i2 < high; i2++) {
                for (int j2 = i2; j2 > low && Integer.valueOf(dest[j2 - 1]).compareTo(Integer.valueOf(dest[j2])) > 0; j2--) {
                    swap(dest, j2, j2 - 1);
                }
            }
            return;
        }
        int low2 = low + off;
        int high2 = high + off;
        int mid = (low2 + high2) >>> 1;
        mergeSort(dest, src, low2, mid, -off);
        mergeSort(dest, src, mid, high2, -off);
        if (Integer.valueOf(src[mid - 1]).compareTo(Integer.valueOf(src[mid])) <= 0) {
            System.arraycopy(src, low2, dest, low, length);
            return;
        }
        int p2 = low2;
        int q2 = mid;
        for (int i3 = low; i3 < high; i3++) {
            if (q2 >= high2 || (p2 < mid && Integer.valueOf(src[p2]).compareTo(Integer.valueOf(src[q2])) <= 0)) {
                dest[i3] = src[p2];
                int i4 = p2;
                p2++;
                this.permutation[this.reversePermutation[i4]] = i3;
            } else {
                dest[i3] = src[q2];
                int i5 = q2;
                q2++;
                this.permutation[this.reversePermutation[i5]] = i3;
            }
        }
        for (int i6 = low; i6 < high; i6++) {
            this.reversePermutation[this.permutation[i6]] = i6;
        }
    }

    private void mergeSort(Object[] src, Object[] dest, int low, int high, int off) {
        int length = high - low;
        if (length < 7) {
            for (int i2 = low; i2 < high; i2++) {
                for (int j2 = i2; j2 > low && ((Comparable) dest[j2 - 1]).compareTo(dest[j2]) > 0; j2--) {
                    swap(dest, j2, j2 - 1);
                }
            }
            return;
        }
        int low2 = low + off;
        int high2 = high + off;
        int mid = (low2 + high2) >>> 1;
        mergeSort(dest, src, low2, mid, -off);
        mergeSort(dest, src, mid, high2, -off);
        if (((Comparable) src[mid - 1]).compareTo(src[mid]) <= 0) {
            System.arraycopy(src, low2, dest, low, length);
            return;
        }
        int p2 = low2;
        int q2 = mid;
        for (int i3 = low; i3 < high; i3++) {
            if (q2 >= high2 || (p2 < mid && ((Comparable) src[p2]).compareTo(src[q2]) <= 0)) {
                dest[i3] = src[p2];
                int i4 = p2;
                p2++;
                this.permutation[this.reversePermutation[i4]] = i3;
            } else {
                dest[i3] = src[q2];
                int i5 = q2;
                q2++;
                this.permutation[this.reversePermutation[i5]] = i3;
            }
        }
        for (int i6 = low; i6 < high; i6++) {
            this.reversePermutation[this.permutation[i6]] = i6;
        }
    }

    private void mergeSort(Object[] src, Object[] dest, int low, int high, int off, Comparator c2) {
        int length = high - low;
        if (length < 7) {
            for (int i2 = low; i2 < high; i2++) {
                for (int j2 = i2; j2 > low && c2.compare(dest[j2 - 1], dest[j2]) > 0; j2--) {
                    swap(dest, j2, j2 - 1);
                }
            }
            return;
        }
        int low2 = low + off;
        int high2 = high + off;
        int mid = (low2 + high2) >>> 1;
        mergeSort(dest, src, low2, mid, -off, c2);
        mergeSort(dest, src, mid, high2, -off, c2);
        if (c2.compare(src[mid - 1], src[mid]) <= 0) {
            System.arraycopy(src, low2, dest, low, length);
            return;
        }
        int p2 = low2;
        int q2 = mid;
        for (int i3 = low; i3 < high; i3++) {
            if (q2 >= high2 || (p2 < mid && c2.compare(src[p2], src[q2]) <= 0)) {
                dest[i3] = src[p2];
                int i4 = p2;
                p2++;
                this.permutation[this.reversePermutation[i4]] = i3;
            } else {
                dest[i3] = src[q2];
                int i5 = q2;
                q2++;
                this.permutation[this.reversePermutation[i5]] = i3;
            }
        }
        for (int i6 = low; i6 < high; i6++) {
            this.reversePermutation[this.permutation[i6]] = i6;
        }
    }

    private void swap(int[] x2, int a2, int b2) {
        int t2 = x2[a2];
        x2[a2] = x2[b2];
        x2[b2] = t2;
        this.permutation[this.reversePermutation[a2]] = b2;
        this.permutation[this.reversePermutation[b2]] = a2;
        int tp = this.reversePermutation[a2];
        this.reversePermutation[a2] = this.reversePermutation[b2];
        this.reversePermutation[b2] = tp;
    }

    private void swap(Object[] x2, int a2, int b2) {
        Object t2 = x2[a2];
        x2[a2] = x2[b2];
        x2[b2] = t2;
        this.permutation[this.reversePermutation[a2]] = b2;
        this.permutation[this.reversePermutation[b2]] = a2;
        int tp = this.reversePermutation[a2];
        this.reversePermutation[a2] = this.reversePermutation[b2];
        this.reversePermutation[b2] = tp;
    }

    private int[] initPermutation(int length) {
        this.permutation = new int[length];
        this.reversePermutation = new int[length];
        for (int i2 = 0; i2 < length; i2++) {
            int i3 = i2;
            this.reversePermutation[i2] = i3;
            this.permutation[i2] = i3;
        }
        return this.permutation;
    }
}
