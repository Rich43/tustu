package java.util.concurrent.atomic;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.DoubleBinaryOperator;
import java.util.function.LongBinaryOperator;
import sun.misc.Contended;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/atomic/Striped64.class */
abstract class Striped64 extends Number {
    static final int NCPU = Runtime.getRuntime().availableProcessors();
    volatile transient Cell[] cells;
    volatile transient long base;
    volatile transient int cellsBusy;
    private static final Unsafe UNSAFE;
    private static final long BASE;
    private static final long CELLSBUSY;
    private static final long PROBE;

    @Contended
    /* loaded from: rt.jar:java/util/concurrent/atomic/Striped64$Cell.class */
    static final class Cell {
        volatile long value;
        private static final Unsafe UNSAFE;
        private static final long valueOffset;

        Cell(long j2) {
            this.value = j2;
        }

        final boolean cas(long j2, long j3) {
            return UNSAFE.compareAndSwapLong(this, valueOffset, j2, j3);
        }

        static {
            try {
                UNSAFE = Unsafe.getUnsafe();
                valueOffset = UNSAFE.objectFieldOffset(Cell.class.getDeclaredField("value"));
            } catch (Exception e2) {
                throw new Error(e2);
            }
        }
    }

    static {
        try {
            UNSAFE = Unsafe.getUnsafe();
            BASE = UNSAFE.objectFieldOffset(Striped64.class.getDeclaredField("base"));
            CELLSBUSY = UNSAFE.objectFieldOffset(Striped64.class.getDeclaredField("cellsBusy"));
            PROBE = UNSAFE.objectFieldOffset(Thread.class.getDeclaredField("threadLocalRandomProbe"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    Striped64() {
    }

    final boolean casBase(long j2, long j3) {
        return UNSAFE.compareAndSwapLong(this, BASE, j2, j3);
    }

    final boolean casCellsBusy() {
        return UNSAFE.compareAndSwapInt(this, CELLSBUSY, 0, 1);
    }

    static final int getProbe() {
        return UNSAFE.getInt(Thread.currentThread(), PROBE);
    }

    static final int advanceProbe(int i2) {
        int i3 = i2 ^ (i2 << 13);
        int i4 = i3 ^ (i3 >>> 17);
        int i5 = i4 ^ (i4 << 5);
        UNSAFE.putInt(Thread.currentThread(), PROBE, i5);
        return i5;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v27, types: [java.util.concurrent.atomic.Striped64$Cell, long] */
    final void longAccumulate(long j2, LongBinaryOperator longBinaryOperator, boolean z2) {
        int length;
        int length2;
        int probe = getProbe();
        int iAdvanceProbe = probe;
        if (probe == 0) {
            ThreadLocalRandom.current();
            iAdvanceProbe = getProbe();
            z2 = true;
        }
        boolean z3 = false;
        while (true) {
            Cell[] cellArr = this.cells;
            if (cellArr != 0 && (length = cellArr.length) > 0) {
                ?? r0 = cellArr[(length - 1) & iAdvanceProbe];
                if (r0 == 0) {
                    if (this.cellsBusy == 0) {
                        Cell cell = new Cell(j2);
                        if (this.cellsBusy == 0 && casCellsBusy()) {
                            boolean z4 = false;
                            try {
                                Cell[] cellArr2 = this.cells;
                                if (cellArr2 != null && (length2 = cellArr2.length) > 0) {
                                    int i2 = (length2 - 1) & iAdvanceProbe;
                                    if (cellArr2[i2] == null) {
                                        cellArr2[i2] = cell;
                                        z4 = true;
                                    }
                                }
                                if (z4) {
                                    return;
                                }
                            } finally {
                                this.cellsBusy = 0;
                            }
                        }
                    }
                    z3 = false;
                    iAdvanceProbe = advanceProbe(iAdvanceProbe);
                } else {
                    if (!z2) {
                        z2 = true;
                    } else {
                        long j3 = r0.value;
                        if (!r0.cas(r0, longBinaryOperator == null ? j3 + j2 : longBinaryOperator.applyAsLong(j3, j2))) {
                            if (length >= NCPU || this.cells != cellArr) {
                                z3 = false;
                            } else if (!z3) {
                                z3 = true;
                            } else if (this.cellsBusy == 0 && casCellsBusy()) {
                                try {
                                    if (this.cells == cellArr) {
                                        Cell[] cellArr3 = new Cell[length << 1];
                                        for (int i3 = 0; i3 < length; i3++) {
                                            cellArr3[i3] = cellArr[i3];
                                        }
                                        this.cells = cellArr3;
                                    }
                                    this.cellsBusy = 0;
                                    z3 = false;
                                } finally {
                                    this.cellsBusy = 0;
                                }
                            }
                        } else {
                            return;
                        }
                    }
                    iAdvanceProbe = advanceProbe(iAdvanceProbe);
                }
            } else if (this.cellsBusy == 0 && this.cells == cellArr && casCellsBusy()) {
                boolean z5 = false;
                try {
                    if (this.cells == cellArr) {
                        Cell[] cellArr4 = new Cell[2];
                        cellArr4[iAdvanceProbe & 1] = new Cell(j2);
                        this.cells = cellArr4;
                        z5 = true;
                    }
                    this.cellsBusy = 0;
                    if (z5) {
                        return;
                    }
                } finally {
                    this.cellsBusy = 0;
                }
            } else {
                long j4 = this.base;
                if (casBase(j4, longBinaryOperator == null ? j4 + j2 : longBinaryOperator.applyAsLong(j4, j2))) {
                    return;
                }
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v27, types: [java.util.concurrent.atomic.Striped64$Cell, long] */
    final void doubleAccumulate(double d2, DoubleBinaryOperator doubleBinaryOperator, boolean z2) {
        long jDoubleToRawLongBits;
        int length;
        int length2;
        long jDoubleToRawLongBits2;
        int probe = getProbe();
        int iAdvanceProbe = probe;
        if (probe == 0) {
            ThreadLocalRandom.current();
            iAdvanceProbe = getProbe();
            z2 = true;
        }
        boolean z3 = false;
        while (true) {
            Cell[] cellArr = this.cells;
            if (cellArr != 0 && (length = cellArr.length) > 0) {
                ?? r0 = cellArr[(length - 1) & iAdvanceProbe];
                if (r0 == 0) {
                    if (this.cellsBusy == 0) {
                        Cell cell = new Cell(Double.doubleToRawLongBits(d2));
                        if (this.cellsBusy == 0 && casCellsBusy()) {
                            boolean z4 = false;
                            try {
                                Cell[] cellArr2 = this.cells;
                                if (cellArr2 != null && (length2 = cellArr2.length) > 0) {
                                    int i2 = (length2 - 1) & iAdvanceProbe;
                                    if (cellArr2[i2] == null) {
                                        cellArr2[i2] = cell;
                                        z4 = true;
                                    }
                                }
                                if (z4) {
                                    return;
                                }
                            } finally {
                                this.cellsBusy = 0;
                            }
                        }
                    }
                    z3 = false;
                    iAdvanceProbe = advanceProbe(iAdvanceProbe);
                } else {
                    if (!z2) {
                        z2 = true;
                    } else {
                        long j2 = r0.value;
                        if (doubleBinaryOperator == null) {
                            jDoubleToRawLongBits2 = Double.doubleToRawLongBits(Double.longBitsToDouble(j2) + d2);
                        } else {
                            jDoubleToRawLongBits2 = Double.doubleToRawLongBits(doubleBinaryOperator.applyAsDouble(Double.longBitsToDouble(j2), d2));
                        }
                        if (!r0.cas(r0, jDoubleToRawLongBits2)) {
                            if (length >= NCPU || this.cells != cellArr) {
                                z3 = false;
                            } else if (!z3) {
                                z3 = true;
                            } else if (this.cellsBusy == 0 && casCellsBusy()) {
                                try {
                                    if (this.cells == cellArr) {
                                        Cell[] cellArr3 = new Cell[length << 1];
                                        for (int i3 = 0; i3 < length; i3++) {
                                            cellArr3[i3] = cellArr[i3];
                                        }
                                        this.cells = cellArr3;
                                    }
                                    this.cellsBusy = 0;
                                    z3 = false;
                                } finally {
                                    this.cellsBusy = 0;
                                }
                            }
                        } else {
                            return;
                        }
                    }
                    iAdvanceProbe = advanceProbe(iAdvanceProbe);
                }
            } else if (this.cellsBusy == 0 && this.cells == cellArr && casCellsBusy()) {
                boolean z5 = false;
                try {
                    if (this.cells == cellArr) {
                        Cell[] cellArr4 = new Cell[2];
                        cellArr4[iAdvanceProbe & 1] = new Cell(Double.doubleToRawLongBits(d2));
                        this.cells = cellArr4;
                        z5 = true;
                    }
                    this.cellsBusy = 0;
                    if (z5) {
                        return;
                    }
                } finally {
                    this.cellsBusy = 0;
                }
            } else {
                long j3 = this.base;
                if (doubleBinaryOperator == null) {
                    jDoubleToRawLongBits = Double.doubleToRawLongBits(Double.longBitsToDouble(j3) + d2);
                } else {
                    jDoubleToRawLongBits = Double.doubleToRawLongBits(doubleBinaryOperator.applyAsDouble(Double.longBitsToDouble(j3), d2));
                }
                if (casBase(j3, jDoubleToRawLongBits)) {
                    return;
                }
            }
        }
    }
}
