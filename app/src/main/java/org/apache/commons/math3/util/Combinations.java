package org.apache.commons.math3.util;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.apache.commons.math3.exception.DimensionMismatchException;
import org.apache.commons.math3.exception.MathInternalError;
import org.apache.commons.math3.exception.NotPositiveException;
import org.apache.commons.math3.exception.NumberIsTooLargeException;
import org.apache.commons.math3.exception.OutOfRangeException;

/* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/Combinations.class */
public class Combinations implements Iterable<int[]> {

    /* renamed from: n, reason: collision with root package name */
    private final int f13113n;

    /* renamed from: k, reason: collision with root package name */
    private final int f13114k;
    private final IterationOrder iterationOrder;

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/Combinations$IterationOrder.class */
    private enum IterationOrder {
        LEXICOGRAPHIC
    }

    public Combinations(int n2, int k2) {
        this(n2, k2, IterationOrder.LEXICOGRAPHIC);
    }

    private Combinations(int n2, int k2, IterationOrder iterationOrder) throws NotPositiveException, NumberIsTooLargeException {
        CombinatoricsUtils.checkBinomial(n2, k2);
        this.f13113n = n2;
        this.f13114k = k2;
        this.iterationOrder = iterationOrder;
    }

    public int getN() {
        return this.f13113n;
    }

    public int getK() {
        return this.f13114k;
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<int[]> iterator() {
        if (this.f13114k == 0 || this.f13114k == this.f13113n) {
            return new SingletonIterator(MathArrays.natural(this.f13114k));
        }
        switch (this.iterationOrder) {
            case LEXICOGRAPHIC:
                return new LexicographicIterator(this.f13113n, this.f13114k);
            default:
                throw new MathInternalError();
        }
    }

    public Comparator<int[]> comparator() {
        return new LexicographicComparator(this.f13113n, this.f13114k);
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/Combinations$LexicographicIterator.class */
    private static class LexicographicIterator implements Iterator<int[]> {

        /* renamed from: k, reason: collision with root package name */
        private final int f13117k;

        /* renamed from: c, reason: collision with root package name */
        private final int[] f13118c;
        private boolean more;

        /* renamed from: j, reason: collision with root package name */
        private int f13119j;

        LexicographicIterator(int n2, int k2) {
            this.more = true;
            this.f13117k = k2;
            this.f13118c = new int[k2 + 3];
            if (k2 == 0 || k2 >= n2) {
                this.more = false;
                return;
            }
            for (int i2 = 1; i2 <= k2; i2++) {
                this.f13118c[i2] = i2 - 1;
            }
            this.f13118c[k2 + 1] = n2;
            this.f13118c[k2 + 2] = 0;
            this.f13119j = k2;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.more;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public int[] next() {
            if (!this.more) {
                throw new NoSuchElementException();
            }
            int[] ret = new int[this.f13117k];
            System.arraycopy(this.f13118c, 1, ret, 0, this.f13117k);
            int x2 = 0;
            if (this.f13119j > 0) {
                int x3 = this.f13119j;
                this.f13118c[this.f13119j] = x3;
                this.f13119j--;
                return ret;
            }
            if (this.f13118c[1] + 1 < this.f13118c[2]) {
                int[] iArr = this.f13118c;
                iArr[1] = iArr[1] + 1;
                return ret;
            }
            this.f13119j = 2;
            boolean stepDone = false;
            while (!stepDone) {
                this.f13118c[this.f13119j - 1] = this.f13119j - 2;
                x2 = this.f13118c[this.f13119j] + 1;
                if (x2 == this.f13118c[this.f13119j + 1]) {
                    this.f13119j++;
                } else {
                    stepDone = true;
                }
            }
            if (this.f13119j > this.f13117k) {
                this.more = false;
                return ret;
            }
            this.f13118c[this.f13119j] = x2;
            this.f13119j--;
            return ret;
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/Combinations$SingletonIterator.class */
    private static class SingletonIterator implements Iterator<int[]> {
        private final int[] singleton;
        private boolean more = true;

        SingletonIterator(int[] singleton) {
            this.singleton = singleton;
        }

        @Override // java.util.Iterator
        public boolean hasNext() {
            return this.more;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Iterator
        public int[] next() {
            if (this.more) {
                this.more = false;
                return this.singleton;
            }
            throw new NoSuchElementException();
        }

        @Override // java.util.Iterator
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    /* loaded from: commons-math3-3.6.1.jar:org/apache/commons/math3/util/Combinations$LexicographicComparator.class */
    private static class LexicographicComparator implements Comparator<int[]>, Serializable {
        private static final long serialVersionUID = 20130906;

        /* renamed from: n, reason: collision with root package name */
        private final int f13115n;

        /* renamed from: k, reason: collision with root package name */
        private final int f13116k;

        LexicographicComparator(int n2, int k2) {
            this.f13115n = n2;
            this.f13116k = k2;
        }

        @Override // java.util.Comparator
        public int compare(int[] c1, int[] c2) {
            if (c1.length != this.f13116k) {
                throw new DimensionMismatchException(c1.length, this.f13116k);
            }
            if (c2.length != this.f13116k) {
                throw new DimensionMismatchException(c2.length, this.f13116k);
            }
            int[] c1s = MathArrays.copyOf(c1);
            Arrays.sort(c1s);
            int[] c2s = MathArrays.copyOf(c2);
            Arrays.sort(c2s);
            long v1 = lexNorm(c1s);
            long v2 = lexNorm(c2s);
            if (v1 < v2) {
                return -1;
            }
            if (v1 > v2) {
                return 1;
            }
            return 0;
        }

        private long lexNorm(int[] c2) {
            long ret = 0;
            for (int i2 = 0; i2 < c2.length; i2++) {
                int digit = c2[i2];
                if (digit < 0 || digit >= this.f13115n) {
                    throw new OutOfRangeException(Integer.valueOf(digit), 0, Integer.valueOf(this.f13115n - 1));
                }
                ret += c2[i2] * ArithmeticUtils.pow(this.f13115n, i2);
            }
            return ret;
        }
    }
}
