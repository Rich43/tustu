package java.util.concurrent.atomic;

import java.io.Serializable;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/atomic/AtomicLongArray.class */
public class AtomicLongArray implements Serializable {
    private static final long serialVersionUID = -2308431214976778248L;
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final int base = unsafe.arrayBaseOffset(long[].class);
    private static final int shift;
    private final long[] array;

    static {
        int iArrayIndexScale = unsafe.arrayIndexScale(long[].class);
        if ((iArrayIndexScale & (iArrayIndexScale - 1)) != 0) {
            throw new Error("data type scale not a power of two");
        }
        shift = 31 - Integer.numberOfLeadingZeros(iArrayIndexScale);
    }

    private long checkedByteOffset(int i2) {
        if (i2 < 0 || i2 >= this.array.length) {
            throw new IndexOutOfBoundsException("index " + i2);
        }
        return byteOffset(i2);
    }

    private static long byteOffset(int i2) {
        return (i2 << shift) + base;
    }

    public AtomicLongArray(int i2) {
        this.array = new long[i2];
    }

    public AtomicLongArray(long[] jArr) {
        this.array = (long[]) jArr.clone();
    }

    public final int length() {
        return this.array.length;
    }

    public final long get(int i2) {
        return getRaw(checkedByteOffset(i2));
    }

    private long getRaw(long j2) {
        return unsafe.getLongVolatile(this.array, j2);
    }

    public final void set(int i2, long j2) {
        unsafe.putLongVolatile(this.array, checkedByteOffset(i2), j2);
    }

    public final void lazySet(int i2, long j2) {
        unsafe.putOrderedLong(this.array, checkedByteOffset(i2), j2);
    }

    public final long getAndSet(int i2, long j2) {
        return unsafe.getAndSetLong(this.array, checkedByteOffset(i2), j2);
    }

    public final boolean compareAndSet(int i2, long j2, long j3) {
        return compareAndSetRaw(checkedByteOffset(i2), j2, j3);
    }

    private boolean compareAndSetRaw(long j2, long j3, long j4) {
        return unsafe.compareAndSwapLong(this.array, j2, j3, j4);
    }

    public final boolean weakCompareAndSet(int i2, long j2, long j3) {
        return compareAndSet(i2, j2, j3);
    }

    public final long getAndIncrement(int i2) {
        return getAndAdd(i2, 1L);
    }

    public final long getAndDecrement(int i2) {
        return getAndAdd(i2, -1L);
    }

    public final long getAndAdd(int i2, long j2) {
        return unsafe.getAndAddLong(this.array, checkedByteOffset(i2), j2);
    }

    public final long incrementAndGet(int i2) {
        return getAndAdd(i2, 1L) + 1;
    }

    public final long decrementAndGet(int i2) {
        return getAndAdd(i2, -1L) - 1;
    }

    public long addAndGet(int i2, long j2) {
        return getAndAdd(i2, j2) + j2;
    }

    public final long getAndUpdate(int i2, LongUnaryOperator longUnaryOperator) {
        long raw;
        long jCheckedByteOffset = checkedByteOffset(i2);
        do {
            raw = getRaw(jCheckedByteOffset);
        } while (!compareAndSetRaw(jCheckedByteOffset, raw, longUnaryOperator.applyAsLong(raw)));
        return raw;
    }

    public final long updateAndGet(int i2, LongUnaryOperator longUnaryOperator) {
        long raw;
        long jApplyAsLong;
        long jCheckedByteOffset = checkedByteOffset(i2);
        do {
            raw = getRaw(jCheckedByteOffset);
            jApplyAsLong = longUnaryOperator.applyAsLong(raw);
        } while (!compareAndSetRaw(jCheckedByteOffset, raw, jApplyAsLong));
        return jApplyAsLong;
    }

    public final long getAndAccumulate(int i2, long j2, LongBinaryOperator longBinaryOperator) {
        long raw;
        long jCheckedByteOffset = checkedByteOffset(i2);
        do {
            raw = getRaw(jCheckedByteOffset);
        } while (!compareAndSetRaw(jCheckedByteOffset, raw, longBinaryOperator.applyAsLong(raw, j2)));
        return raw;
    }

    public final long accumulateAndGet(int i2, long j2, LongBinaryOperator longBinaryOperator) {
        long raw;
        long jApplyAsLong;
        long jCheckedByteOffset = checkedByteOffset(i2);
        do {
            raw = getRaw(jCheckedByteOffset);
            jApplyAsLong = longBinaryOperator.applyAsLong(raw, j2);
        } while (!compareAndSetRaw(jCheckedByteOffset, raw, jApplyAsLong));
        return jApplyAsLong;
    }

    public String toString() {
        int length = this.array.length - 1;
        if (length == -1) {
            return "[]";
        }
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int i2 = 0;
        while (true) {
            sb.append(getRaw(byteOffset(i2)));
            if (i2 == length) {
                return sb.append(']').toString();
            }
            sb.append(',').append(' ');
            i2++;
        }
    }
}
