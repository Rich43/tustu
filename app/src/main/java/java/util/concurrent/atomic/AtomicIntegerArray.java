package java.util.concurrent.atomic;

import java.io.Serializable;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/atomic/AtomicIntegerArray.class */
public class AtomicIntegerArray implements Serializable {
    private static final long serialVersionUID = 2862133569453604235L;
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final int base = unsafe.arrayBaseOffset(int[].class);
    private static final int shift;
    private final int[] array;

    static {
        int iArrayIndexScale = unsafe.arrayIndexScale(int[].class);
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

    public AtomicIntegerArray(int i2) {
        this.array = new int[i2];
    }

    public AtomicIntegerArray(int[] iArr) {
        this.array = (int[]) iArr.clone();
    }

    public final int length() {
        return this.array.length;
    }

    public final int get(int i2) {
        return getRaw(checkedByteOffset(i2));
    }

    private int getRaw(long j2) {
        return unsafe.getIntVolatile(this.array, j2);
    }

    public final void set(int i2, int i3) {
        unsafe.putIntVolatile(this.array, checkedByteOffset(i2), i3);
    }

    public final void lazySet(int i2, int i3) {
        unsafe.putOrderedInt(this.array, checkedByteOffset(i2), i3);
    }

    public final int getAndSet(int i2, int i3) {
        return unsafe.getAndSetInt(this.array, checkedByteOffset(i2), i3);
    }

    public final boolean compareAndSet(int i2, int i3, int i4) {
        return compareAndSetRaw(checkedByteOffset(i2), i3, i4);
    }

    private boolean compareAndSetRaw(long j2, int i2, int i3) {
        return unsafe.compareAndSwapInt(this.array, j2, i2, i3);
    }

    public final boolean weakCompareAndSet(int i2, int i3, int i4) {
        return compareAndSet(i2, i3, i4);
    }

    public final int getAndIncrement(int i2) {
        return getAndAdd(i2, 1);
    }

    public final int getAndDecrement(int i2) {
        return getAndAdd(i2, -1);
    }

    public final int getAndAdd(int i2, int i3) {
        return unsafe.getAndAddInt(this.array, checkedByteOffset(i2), i3);
    }

    public final int incrementAndGet(int i2) {
        return getAndAdd(i2, 1) + 1;
    }

    public final int decrementAndGet(int i2) {
        return getAndAdd(i2, -1) - 1;
    }

    public final int addAndGet(int i2, int i3) {
        return getAndAdd(i2, i3) + i3;
    }

    public final int getAndUpdate(int i2, IntUnaryOperator intUnaryOperator) {
        int raw;
        long jCheckedByteOffset = checkedByteOffset(i2);
        do {
            raw = getRaw(jCheckedByteOffset);
        } while (!compareAndSetRaw(jCheckedByteOffset, raw, intUnaryOperator.applyAsInt(raw)));
        return raw;
    }

    public final int updateAndGet(int i2, IntUnaryOperator intUnaryOperator) {
        int raw;
        int iApplyAsInt;
        long jCheckedByteOffset = checkedByteOffset(i2);
        do {
            raw = getRaw(jCheckedByteOffset);
            iApplyAsInt = intUnaryOperator.applyAsInt(raw);
        } while (!compareAndSetRaw(jCheckedByteOffset, raw, iApplyAsInt));
        return iApplyAsInt;
    }

    public final int getAndAccumulate(int i2, int i3, IntBinaryOperator intBinaryOperator) {
        int raw;
        long jCheckedByteOffset = checkedByteOffset(i2);
        do {
            raw = getRaw(jCheckedByteOffset);
        } while (!compareAndSetRaw(jCheckedByteOffset, raw, intBinaryOperator.applyAsInt(raw, i3)));
        return raw;
    }

    public final int accumulateAndGet(int i2, int i3, IntBinaryOperator intBinaryOperator) {
        int raw;
        int iApplyAsInt;
        long jCheckedByteOffset = checkedByteOffset(i2);
        do {
            raw = getRaw(jCheckedByteOffset);
            iApplyAsInt = intBinaryOperator.applyAsInt(raw, i3);
        } while (!compareAndSetRaw(jCheckedByteOffset, raw, iApplyAsInt));
        return iApplyAsInt;
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
