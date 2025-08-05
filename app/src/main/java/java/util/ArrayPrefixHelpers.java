package java.util;

import java.util.concurrent.CountedCompleter;
import java.util.concurrent.ForkJoinPool;
import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.LongBinaryOperator;

/* loaded from: rt.jar:java/util/ArrayPrefixHelpers.class */
class ArrayPrefixHelpers {
    static final int CUMULATE = 1;
    static final int SUMMED = 2;
    static final int FINISHED = 4;
    static final int MIN_PARTITION = 16;

    private ArrayPrefixHelpers() {
    }

    /* loaded from: rt.jar:java/util/ArrayPrefixHelpers$CumulateTask.class */
    static final class CumulateTask<T> extends CountedCompleter<Void> {
        final T[] array;
        final BinaryOperator<T> function;
        CumulateTask<T> left;
        CumulateTask<T> right;
        T in;
        T out;
        final int lo;
        final int hi;
        final int origin;
        final int fence;
        final int threshold;

        public CumulateTask(CumulateTask<T> cumulateTask, BinaryOperator<T> binaryOperator, T[] tArr, int i2, int i3) {
            super(cumulateTask);
            this.function = binaryOperator;
            this.array = tArr;
            this.origin = i2;
            this.lo = i2;
            this.fence = i3;
            this.hi = i3;
            int commonPoolParallelism = (i3 - i2) / (ForkJoinPool.getCommonPoolParallelism() << 3);
            this.threshold = commonPoolParallelism <= 16 ? 16 : commonPoolParallelism;
        }

        CumulateTask(CumulateTask<T> cumulateTask, BinaryOperator<T> binaryOperator, T[] tArr, int i2, int i3, int i4, int i5, int i6) {
            super(cumulateTask);
            this.function = binaryOperator;
            this.array = tArr;
            this.origin = i2;
            this.fence = i3;
            this.threshold = i4;
            this.lo = i5;
            this.hi = i6;
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            T[] tArr;
            int i2;
            int pendingCount;
            int i3;
            T tApply;
            CumulateTask<T> cumulateTask;
            int i4;
            CumulateTask<T> cumulateTask2;
            BinaryOperator<T> binaryOperator = this.function;
            if (binaryOperator == null || (tArr = this.array) == null) {
                throw new NullPointerException();
            }
            int i5 = this.threshold;
            int i6 = this.origin;
            int i7 = this.fence;
            CumulateTask<T> cumulateTask3 = this;
            while (true) {
                int i8 = cumulateTask3.lo;
                if (i8 >= 0 && (i2 = cumulateTask3.hi) <= tArr.length) {
                    if (i2 - i8 > i5) {
                        CumulateTask<T> cumulateTask4 = cumulateTask3.left;
                        CumulateTask<T> cumulateTask5 = cumulateTask3.right;
                        if (cumulateTask4 == null) {
                            int i9 = (i8 + i2) >>> 1;
                            CumulateTask<T> cumulateTask6 = new CumulateTask<>(cumulateTask3, binaryOperator, tArr, i6, i7, i5, i9, i2);
                            cumulateTask3.right = cumulateTask6;
                            cumulateTask2 = cumulateTask6;
                            CumulateTask<T> cumulateTask7 = new CumulateTask<>(cumulateTask3, binaryOperator, tArr, i6, i7, i5, i8, i9);
                            cumulateTask3.left = cumulateTask7;
                            cumulateTask3 = cumulateTask7;
                        } else {
                            T t2 = cumulateTask3.in;
                            cumulateTask4.in = t2;
                            cumulateTask3 = null;
                            cumulateTask2 = null;
                            if (cumulateTask5 != null) {
                                T t3 = cumulateTask4.out;
                                cumulateTask5.in = i8 == i6 ? t3 : binaryOperator.apply(t2, t3);
                                while (true) {
                                    int pendingCount2 = cumulateTask5.getPendingCount();
                                    if ((pendingCount2 & 1) == 0) {
                                        if (cumulateTask5.compareAndSetPendingCount(pendingCount2, pendingCount2 | 1)) {
                                            cumulateTask3 = cumulateTask5;
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                            while (true) {
                                int pendingCount3 = cumulateTask4.getPendingCount();
                                if ((pendingCount3 & 1) != 0) {
                                    break;
                                }
                                if (cumulateTask4.compareAndSetPendingCount(pendingCount3, pendingCount3 | 1)) {
                                    if (cumulateTask3 != null) {
                                        cumulateTask2 = cumulateTask3;
                                    }
                                    cumulateTask3 = cumulateTask4;
                                }
                            }
                            if (cumulateTask3 == null) {
                                return;
                            }
                        }
                        if (cumulateTask2 != null) {
                            cumulateTask2.fork();
                        }
                    } else {
                        do {
                            pendingCount = cumulateTask3.getPendingCount();
                            if ((pendingCount & 4) == 0) {
                                i3 = (pendingCount & 1) != 0 ? 4 : i8 > i6 ? 2 : 6;
                            } else {
                                return;
                            }
                        } while (!cumulateTask3.compareAndSetPendingCount(pendingCount, pendingCount | i3));
                        if (i3 != 2) {
                            if (i8 == i6) {
                                tApply = tArr[i6];
                                i4 = i6 + 1;
                            } else {
                                tApply = cumulateTask3.in;
                                i4 = i8;
                            }
                            for (int i10 = i4; i10 < i2; i10++) {
                                T tApply2 = binaryOperator.apply(tApply, tArr[i10]);
                                tApply = tApply2;
                                tArr[i10] = tApply2;
                            }
                        } else if (i2 < i7) {
                            tApply = tArr[i8];
                            for (int i11 = i8 + 1; i11 < i2; i11++) {
                                tApply = binaryOperator.apply(tApply, tArr[i11]);
                            }
                        } else {
                            tApply = cumulateTask3.in;
                        }
                        cumulateTask3.out = tApply;
                        while (true) {
                            CumulateTask<T> cumulateTask8 = (CumulateTask) cumulateTask3.getCompleter();
                            if (cumulateTask8 == null) {
                                if ((i3 & 4) != 0) {
                                    cumulateTask3.quietlyComplete();
                                    return;
                                }
                                return;
                            }
                            int pendingCount4 = cumulateTask8.getPendingCount();
                            if ((pendingCount4 & i3 & 4) != 0) {
                                cumulateTask3 = cumulateTask8;
                            } else if ((pendingCount4 & i3 & 2) != 0) {
                                CumulateTask<T> cumulateTask9 = cumulateTask8.left;
                                if (cumulateTask9 != null && (cumulateTask = cumulateTask8.right) != null) {
                                    T t4 = cumulateTask9.out;
                                    cumulateTask8.out = cumulateTask.hi == i7 ? t4 : binaryOperator.apply(t4, cumulateTask.out);
                                }
                                int i12 = ((pendingCount4 & 1) == 0 && cumulateTask8.lo == i6) ? 1 : 0;
                                int i13 = pendingCount4 | i3 | i12;
                                if (i13 == pendingCount4 || cumulateTask8.compareAndSetPendingCount(pendingCount4, i13)) {
                                    i3 = 2;
                                    cumulateTask3 = cumulateTask8;
                                    if (i12 != 0) {
                                        cumulateTask8.fork();
                                    }
                                }
                            } else if (cumulateTask8.compareAndSetPendingCount(pendingCount4, pendingCount4 | i3)) {
                                return;
                            }
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/ArrayPrefixHelpers$LongCumulateTask.class */
    static final class LongCumulateTask extends CountedCompleter<Void> {
        final long[] array;
        final LongBinaryOperator function;
        LongCumulateTask left;
        LongCumulateTask right;
        long in;
        long out;
        final int lo;
        final int hi;
        final int origin;
        final int fence;
        final int threshold;

        public LongCumulateTask(LongCumulateTask longCumulateTask, LongBinaryOperator longBinaryOperator, long[] jArr, int i2, int i3) {
            super(longCumulateTask);
            this.function = longBinaryOperator;
            this.array = jArr;
            this.origin = i2;
            this.lo = i2;
            this.fence = i3;
            this.hi = i3;
            int commonPoolParallelism = (i3 - i2) / (ForkJoinPool.getCommonPoolParallelism() << 3);
            this.threshold = commonPoolParallelism <= 16 ? 16 : commonPoolParallelism;
        }

        LongCumulateTask(LongCumulateTask longCumulateTask, LongBinaryOperator longBinaryOperator, long[] jArr, int i2, int i3, int i4, int i5, int i6) {
            super(longCumulateTask);
            this.function = longBinaryOperator;
            this.array = jArr;
            this.origin = i2;
            this.fence = i3;
            this.threshold = i4;
            this.lo = i5;
            this.hi = i6;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r4v1, types: [long] */
        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            long[] jArr;
            int i2;
            int pendingCount;
            int i3;
            long jApplyAsLong;
            LongCumulateTask longCumulateTask;
            int i4;
            LongCumulateTask longCumulateTask2;
            LongBinaryOperator longBinaryOperator = this.function;
            if (longBinaryOperator == 0 || (jArr = this.array) == 0) {
                throw new NullPointerException();
            }
            int i5 = this.threshold;
            int i6 = this.origin;
            int i7 = this.fence;
            LongCumulateTask longCumulateTask3 = this;
            while (true) {
                int i8 = longCumulateTask3.lo;
                if (i8 >= 0 && (i2 = longCumulateTask3.hi) <= jArr.length) {
                    if (i2 - i8 > i5) {
                        LongCumulateTask longCumulateTask4 = longCumulateTask3.left;
                        LongCumulateTask longCumulateTask5 = longCumulateTask3.right;
                        if (longCumulateTask4 == null) {
                            int i9 = (i8 + i2) >>> 1;
                            LongCumulateTask longCumulateTask6 = new LongCumulateTask(longCumulateTask3, longBinaryOperator, jArr, i6, i7, i5, i9, i2);
                            longCumulateTask3.right = longCumulateTask6;
                            longCumulateTask2 = longCumulateTask6;
                            LongCumulateTask longCumulateTask7 = new LongCumulateTask(longCumulateTask3, longBinaryOperator, jArr, i6, i7, i5, i8, i9);
                            longCumulateTask3.left = longCumulateTask7;
                            longCumulateTask3 = longCumulateTask7;
                        } else {
                            long j2 = longCumulateTask3.in;
                            longCumulateTask4.in = j2;
                            longCumulateTask3 = null;
                            longCumulateTask2 = null;
                            if (longCumulateTask5 != null) {
                                long j3 = longCumulateTask4.out;
                                longCumulateTask5.in = i8 == i6 ? j3 : longBinaryOperator.applyAsLong(j2, j3);
                                while (true) {
                                    int pendingCount2 = longCumulateTask5.getPendingCount();
                                    if ((pendingCount2 & 1) == 0) {
                                        if (longCumulateTask5.compareAndSetPendingCount(pendingCount2, pendingCount2 | 1)) {
                                            longCumulateTask3 = longCumulateTask5;
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                            while (true) {
                                int pendingCount3 = longCumulateTask4.getPendingCount();
                                if ((pendingCount3 & 1) != 0) {
                                    break;
                                }
                                if (longCumulateTask4.compareAndSetPendingCount(pendingCount3, pendingCount3 | 1)) {
                                    if (longCumulateTask3 != null) {
                                        longCumulateTask2 = longCumulateTask3;
                                    }
                                    longCumulateTask3 = longCumulateTask4;
                                }
                            }
                            if (longCumulateTask3 == null) {
                                return;
                            }
                        }
                        if (longCumulateTask2 != null) {
                            longCumulateTask2.fork();
                        }
                    } else {
                        do {
                            pendingCount = longCumulateTask3.getPendingCount();
                            if ((pendingCount & 4) == 0) {
                                i3 = (pendingCount & 1) != 0 ? 4 : i8 > i6 ? 2 : 6;
                            } else {
                                return;
                            }
                        } while (!longCumulateTask3.compareAndSetPendingCount(pendingCount, pendingCount | i3));
                        if (i3 != 2) {
                            if (i8 == i6) {
                                jApplyAsLong = jArr[i6];
                                i4 = i6 + 1;
                            } else {
                                jApplyAsLong = longCumulateTask3.in;
                                i4 = i8;
                            }
                            for (int i10 = i4; i10 < i2; i10++) {
                                ?? r4 = jArr[i10];
                                long jApplyAsLong2 = longBinaryOperator.applyAsLong(jApplyAsLong, r4);
                                jApplyAsLong = jApplyAsLong2;
                                r4[i10] = jApplyAsLong2;
                            }
                        } else if (i2 < i7) {
                            jApplyAsLong = jArr[i8];
                            for (int i11 = i8 + 1; i11 < i2; i11++) {
                                jApplyAsLong = longBinaryOperator.applyAsLong(jApplyAsLong, jArr[i11]);
                            }
                        } else {
                            jApplyAsLong = longCumulateTask3.in;
                        }
                        longCumulateTask3.out = jApplyAsLong;
                        while (true) {
                            LongCumulateTask longCumulateTask8 = (LongCumulateTask) longCumulateTask3.getCompleter();
                            if (longCumulateTask8 == null) {
                                if ((i3 & 4) != 0) {
                                    longCumulateTask3.quietlyComplete();
                                    return;
                                }
                                return;
                            }
                            int pendingCount4 = longCumulateTask8.getPendingCount();
                            if ((pendingCount4 & i3 & 4) != 0) {
                                longCumulateTask3 = longCumulateTask8;
                            } else if ((pendingCount4 & i3 & 2) != 0) {
                                LongCumulateTask longCumulateTask9 = longCumulateTask8.left;
                                if (longCumulateTask9 != null && (longCumulateTask = longCumulateTask8.right) != null) {
                                    long j4 = longCumulateTask9.out;
                                    longCumulateTask8.out = longCumulateTask.hi == i7 ? j4 : longBinaryOperator.applyAsLong(j4, longCumulateTask.out);
                                }
                                int i12 = ((pendingCount4 & 1) == 0 && longCumulateTask8.lo == i6) ? 1 : 0;
                                int i13 = pendingCount4 | i3 | i12;
                                if (i13 == pendingCount4 || longCumulateTask8.compareAndSetPendingCount(pendingCount4, i13)) {
                                    i3 = 2;
                                    longCumulateTask3 = longCumulateTask8;
                                    if (i12 != 0) {
                                        longCumulateTask8.fork();
                                    }
                                }
                            } else if (longCumulateTask8.compareAndSetPendingCount(pendingCount4, pendingCount4 | i3)) {
                                return;
                            }
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/ArrayPrefixHelpers$DoubleCumulateTask.class */
    static final class DoubleCumulateTask extends CountedCompleter<Void> {
        final double[] array;
        final DoubleBinaryOperator function;
        DoubleCumulateTask left;
        DoubleCumulateTask right;
        double in;
        double out;
        final int lo;
        final int hi;
        final int origin;
        final int fence;
        final int threshold;

        public DoubleCumulateTask(DoubleCumulateTask doubleCumulateTask, DoubleBinaryOperator doubleBinaryOperator, double[] dArr, int i2, int i3) {
            super(doubleCumulateTask);
            this.function = doubleBinaryOperator;
            this.array = dArr;
            this.origin = i2;
            this.lo = i2;
            this.fence = i3;
            this.hi = i3;
            int commonPoolParallelism = (i3 - i2) / (ForkJoinPool.getCommonPoolParallelism() << 3);
            this.threshold = commonPoolParallelism <= 16 ? 16 : commonPoolParallelism;
        }

        DoubleCumulateTask(DoubleCumulateTask doubleCumulateTask, DoubleBinaryOperator doubleBinaryOperator, double[] dArr, int i2, int i3, int i4, int i5, int i6) {
            super(doubleCumulateTask);
            this.function = doubleBinaryOperator;
            this.array = dArr;
            this.origin = i2;
            this.fence = i3;
            this.threshold = i4;
            this.lo = i5;
            this.hi = i6;
        }

        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r4v1, types: [double] */
        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            double[] dArr;
            int i2;
            int pendingCount;
            int i3;
            double dApplyAsDouble;
            DoubleCumulateTask doubleCumulateTask;
            int i4;
            DoubleCumulateTask doubleCumulateTask2;
            DoubleBinaryOperator doubleBinaryOperator = this.function;
            if (doubleBinaryOperator == 0 || (dArr = this.array) == 0) {
                throw new NullPointerException();
            }
            int i5 = this.threshold;
            int i6 = this.origin;
            int i7 = this.fence;
            DoubleCumulateTask doubleCumulateTask3 = this;
            while (true) {
                int i8 = doubleCumulateTask3.lo;
                if (i8 >= 0 && (i2 = doubleCumulateTask3.hi) <= dArr.length) {
                    if (i2 - i8 > i5) {
                        DoubleCumulateTask doubleCumulateTask4 = doubleCumulateTask3.left;
                        DoubleCumulateTask doubleCumulateTask5 = doubleCumulateTask3.right;
                        if (doubleCumulateTask4 == null) {
                            int i9 = (i8 + i2) >>> 1;
                            DoubleCumulateTask doubleCumulateTask6 = new DoubleCumulateTask(doubleCumulateTask3, doubleBinaryOperator, dArr, i6, i7, i5, i9, i2);
                            doubleCumulateTask3.right = doubleCumulateTask6;
                            doubleCumulateTask2 = doubleCumulateTask6;
                            DoubleCumulateTask doubleCumulateTask7 = new DoubleCumulateTask(doubleCumulateTask3, doubleBinaryOperator, dArr, i6, i7, i5, i8, i9);
                            doubleCumulateTask3.left = doubleCumulateTask7;
                            doubleCumulateTask3 = doubleCumulateTask7;
                        } else {
                            double d2 = doubleCumulateTask3.in;
                            doubleCumulateTask4.in = d2;
                            doubleCumulateTask3 = null;
                            doubleCumulateTask2 = null;
                            if (doubleCumulateTask5 != null) {
                                double d3 = doubleCumulateTask4.out;
                                doubleCumulateTask5.in = i8 == i6 ? d3 : doubleBinaryOperator.applyAsDouble(d2, d3);
                                while (true) {
                                    int pendingCount2 = doubleCumulateTask5.getPendingCount();
                                    if ((pendingCount2 & 1) == 0) {
                                        if (doubleCumulateTask5.compareAndSetPendingCount(pendingCount2, pendingCount2 | 1)) {
                                            doubleCumulateTask3 = doubleCumulateTask5;
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                            while (true) {
                                int pendingCount3 = doubleCumulateTask4.getPendingCount();
                                if ((pendingCount3 & 1) != 0) {
                                    break;
                                }
                                if (doubleCumulateTask4.compareAndSetPendingCount(pendingCount3, pendingCount3 | 1)) {
                                    if (doubleCumulateTask3 != null) {
                                        doubleCumulateTask2 = doubleCumulateTask3;
                                    }
                                    doubleCumulateTask3 = doubleCumulateTask4;
                                }
                            }
                            if (doubleCumulateTask3 == null) {
                                return;
                            }
                        }
                        if (doubleCumulateTask2 != null) {
                            doubleCumulateTask2.fork();
                        }
                    } else {
                        do {
                            pendingCount = doubleCumulateTask3.getPendingCount();
                            if ((pendingCount & 4) == 0) {
                                i3 = (pendingCount & 1) != 0 ? 4 : i8 > i6 ? 2 : 6;
                            } else {
                                return;
                            }
                        } while (!doubleCumulateTask3.compareAndSetPendingCount(pendingCount, pendingCount | i3));
                        if (i3 != 2) {
                            if (i8 == i6) {
                                dApplyAsDouble = dArr[i6];
                                i4 = i6 + 1;
                            } else {
                                dApplyAsDouble = doubleCumulateTask3.in;
                                i4 = i8;
                            }
                            for (int i10 = i4; i10 < i2; i10++) {
                                ?? r4 = dArr[i10];
                                double dApplyAsDouble2 = doubleBinaryOperator.applyAsDouble(dApplyAsDouble, r4);
                                dApplyAsDouble = dApplyAsDouble2;
                                r4[i10] = dApplyAsDouble2;
                            }
                        } else if (i2 < i7) {
                            dApplyAsDouble = dArr[i8];
                            for (int i11 = i8 + 1; i11 < i2; i11++) {
                                dApplyAsDouble = doubleBinaryOperator.applyAsDouble(dApplyAsDouble, dArr[i11]);
                            }
                        } else {
                            dApplyAsDouble = doubleCumulateTask3.in;
                        }
                        doubleCumulateTask3.out = dApplyAsDouble;
                        while (true) {
                            DoubleCumulateTask doubleCumulateTask8 = (DoubleCumulateTask) doubleCumulateTask3.getCompleter();
                            if (doubleCumulateTask8 == null) {
                                if ((i3 & 4) != 0) {
                                    doubleCumulateTask3.quietlyComplete();
                                    return;
                                }
                                return;
                            }
                            int pendingCount4 = doubleCumulateTask8.getPendingCount();
                            if ((pendingCount4 & i3 & 4) != 0) {
                                doubleCumulateTask3 = doubleCumulateTask8;
                            } else if ((pendingCount4 & i3 & 2) != 0) {
                                DoubleCumulateTask doubleCumulateTask9 = doubleCumulateTask8.left;
                                if (doubleCumulateTask9 != null && (doubleCumulateTask = doubleCumulateTask8.right) != null) {
                                    double d4 = doubleCumulateTask9.out;
                                    doubleCumulateTask8.out = doubleCumulateTask.hi == i7 ? d4 : doubleBinaryOperator.applyAsDouble(d4, doubleCumulateTask.out);
                                }
                                int i12 = ((pendingCount4 & 1) == 0 && doubleCumulateTask8.lo == i6) ? 1 : 0;
                                int i13 = pendingCount4 | i3 | i12;
                                if (i13 == pendingCount4 || doubleCumulateTask8.compareAndSetPendingCount(pendingCount4, i13)) {
                                    i3 = 2;
                                    doubleCumulateTask3 = doubleCumulateTask8;
                                    if (i12 != 0) {
                                        doubleCumulateTask8.fork();
                                    }
                                }
                            } else if (doubleCumulateTask8.compareAndSetPendingCount(pendingCount4, pendingCount4 | i3)) {
                                return;
                            }
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }

    /* loaded from: rt.jar:java/util/ArrayPrefixHelpers$IntCumulateTask.class */
    static final class IntCumulateTask extends CountedCompleter<Void> {
        final int[] array;
        final IntBinaryOperator function;
        IntCumulateTask left;
        IntCumulateTask right;
        int in;
        int out;
        final int lo;
        final int hi;
        final int origin;
        final int fence;
        final int threshold;

        public IntCumulateTask(IntCumulateTask intCumulateTask, IntBinaryOperator intBinaryOperator, int[] iArr, int i2, int i3) {
            super(intCumulateTask);
            this.function = intBinaryOperator;
            this.array = iArr;
            this.origin = i2;
            this.lo = i2;
            this.fence = i3;
            this.hi = i3;
            int commonPoolParallelism = (i3 - i2) / (ForkJoinPool.getCommonPoolParallelism() << 3);
            this.threshold = commonPoolParallelism <= 16 ? 16 : commonPoolParallelism;
        }

        IntCumulateTask(IntCumulateTask intCumulateTask, IntBinaryOperator intBinaryOperator, int[] iArr, int i2, int i3, int i4, int i5, int i6) {
            super(intCumulateTask);
            this.function = intBinaryOperator;
            this.array = iArr;
            this.origin = i2;
            this.fence = i3;
            this.threshold = i4;
            this.lo = i5;
            this.hi = i6;
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            int[] iArr;
            int i2;
            int pendingCount;
            int i3;
            int iApplyAsInt;
            IntCumulateTask intCumulateTask;
            int i4;
            IntCumulateTask intCumulateTask2;
            IntBinaryOperator intBinaryOperator = this.function;
            if (intBinaryOperator == null || (iArr = this.array) == null) {
                throw new NullPointerException();
            }
            int i5 = this.threshold;
            int i6 = this.origin;
            int i7 = this.fence;
            IntCumulateTask intCumulateTask3 = this;
            while (true) {
                int i8 = intCumulateTask3.lo;
                if (i8 >= 0 && (i2 = intCumulateTask3.hi) <= iArr.length) {
                    if (i2 - i8 > i5) {
                        IntCumulateTask intCumulateTask4 = intCumulateTask3.left;
                        IntCumulateTask intCumulateTask5 = intCumulateTask3.right;
                        if (intCumulateTask4 == null) {
                            int i9 = (i8 + i2) >>> 1;
                            IntCumulateTask intCumulateTask6 = new IntCumulateTask(intCumulateTask3, intBinaryOperator, iArr, i6, i7, i5, i9, i2);
                            intCumulateTask3.right = intCumulateTask6;
                            intCumulateTask2 = intCumulateTask6;
                            IntCumulateTask intCumulateTask7 = new IntCumulateTask(intCumulateTask3, intBinaryOperator, iArr, i6, i7, i5, i8, i9);
                            intCumulateTask3.left = intCumulateTask7;
                            intCumulateTask3 = intCumulateTask7;
                        } else {
                            int i10 = intCumulateTask3.in;
                            intCumulateTask4.in = i10;
                            intCumulateTask3 = null;
                            intCumulateTask2 = null;
                            if (intCumulateTask5 != null) {
                                int i11 = intCumulateTask4.out;
                                intCumulateTask5.in = i8 == i6 ? i11 : intBinaryOperator.applyAsInt(i10, i11);
                                while (true) {
                                    int pendingCount2 = intCumulateTask5.getPendingCount();
                                    if ((pendingCount2 & 1) == 0) {
                                        if (intCumulateTask5.compareAndSetPendingCount(pendingCount2, pendingCount2 | 1)) {
                                            intCumulateTask3 = intCumulateTask5;
                                            break;
                                        }
                                    } else {
                                        break;
                                    }
                                }
                            }
                            while (true) {
                                int pendingCount3 = intCumulateTask4.getPendingCount();
                                if ((pendingCount3 & 1) != 0) {
                                    break;
                                }
                                if (intCumulateTask4.compareAndSetPendingCount(pendingCount3, pendingCount3 | 1)) {
                                    if (intCumulateTask3 != null) {
                                        intCumulateTask2 = intCumulateTask3;
                                    }
                                    intCumulateTask3 = intCumulateTask4;
                                }
                            }
                            if (intCumulateTask3 == null) {
                                return;
                            }
                        }
                        if (intCumulateTask2 != null) {
                            intCumulateTask2.fork();
                        }
                    } else {
                        do {
                            pendingCount = intCumulateTask3.getPendingCount();
                            if ((pendingCount & 4) == 0) {
                                i3 = (pendingCount & 1) != 0 ? 4 : i8 > i6 ? 2 : 6;
                            } else {
                                return;
                            }
                        } while (!intCumulateTask3.compareAndSetPendingCount(pendingCount, pendingCount | i3));
                        if (i3 != 2) {
                            if (i8 == i6) {
                                iApplyAsInt = iArr[i6];
                                i4 = i6 + 1;
                            } else {
                                iApplyAsInt = intCumulateTask3.in;
                                i4 = i8;
                            }
                            for (int i12 = i4; i12 < i2; i12++) {
                                int iApplyAsInt2 = intBinaryOperator.applyAsInt(iApplyAsInt, iArr[i12]);
                                iApplyAsInt = iApplyAsInt2;
                                iArr[i12] = iApplyAsInt2;
                            }
                        } else if (i2 < i7) {
                            iApplyAsInt = iArr[i8];
                            for (int i13 = i8 + 1; i13 < i2; i13++) {
                                iApplyAsInt = intBinaryOperator.applyAsInt(iApplyAsInt, iArr[i13]);
                            }
                        } else {
                            iApplyAsInt = intCumulateTask3.in;
                        }
                        intCumulateTask3.out = iApplyAsInt;
                        while (true) {
                            IntCumulateTask intCumulateTask8 = (IntCumulateTask) intCumulateTask3.getCompleter();
                            if (intCumulateTask8 == null) {
                                if ((i3 & 4) != 0) {
                                    intCumulateTask3.quietlyComplete();
                                    return;
                                }
                                return;
                            }
                            int pendingCount4 = intCumulateTask8.getPendingCount();
                            if ((pendingCount4 & i3 & 4) != 0) {
                                intCumulateTask3 = intCumulateTask8;
                            } else if ((pendingCount4 & i3 & 2) != 0) {
                                IntCumulateTask intCumulateTask9 = intCumulateTask8.left;
                                if (intCumulateTask9 != null && (intCumulateTask = intCumulateTask8.right) != null) {
                                    int i14 = intCumulateTask9.out;
                                    intCumulateTask8.out = intCumulateTask.hi == i7 ? i14 : intBinaryOperator.applyAsInt(i14, intCumulateTask.out);
                                }
                                int i15 = ((pendingCount4 & 1) == 0 && intCumulateTask8.lo == i6) ? 1 : 0;
                                int i16 = pendingCount4 | i3 | i15;
                                if (i16 == pendingCount4 || intCumulateTask8.compareAndSetPendingCount(pendingCount4, i16)) {
                                    i3 = 2;
                                    intCumulateTask3 = intCumulateTask8;
                                    if (i15 != 0) {
                                        intCumulateTask8.fork();
                                    }
                                }
                            } else if (intCumulateTask8.compareAndSetPendingCount(pendingCount4, pendingCount4 | i3)) {
                                return;
                            }
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }
}
