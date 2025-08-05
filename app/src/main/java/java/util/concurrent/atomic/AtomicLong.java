package java.util.concurrent.atomic;

import java.io.Serializable;
import java.util.function.LongBinaryOperator;
import java.util.function.LongUnaryOperator;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/atomic/AtomicLong.class */
public class AtomicLong extends Number implements Serializable {
    private static final long serialVersionUID = 1927816293512124184L;
    private static final long valueOffset;
    private volatile long value;
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    static final boolean VM_SUPPORTS_LONG_CAS = VMSupportsCS8();

    private static native boolean VMSupportsCS8();

    static {
        try {
            valueOffset = unsafe.objectFieldOffset(AtomicLong.class.getDeclaredField("value"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    public AtomicLong(long j2) {
        this.value = j2;
    }

    public AtomicLong() {
    }

    public final long get() {
        return this.value;
    }

    public final void set(long j2) {
        this.value = j2;
    }

    public final void lazySet(long j2) {
        unsafe.putOrderedLong(this, valueOffset, j2);
    }

    public final long getAndSet(long j2) {
        return unsafe.getAndSetLong(this, valueOffset, j2);
    }

    public final boolean compareAndSet(long j2, long j3) {
        return unsafe.compareAndSwapLong(this, valueOffset, j2, j3);
    }

    public final boolean weakCompareAndSet(long j2, long j3) {
        return unsafe.compareAndSwapLong(this, valueOffset, j2, j3);
    }

    public final long getAndIncrement() {
        return unsafe.getAndAddLong(this, valueOffset, 1L);
    }

    public final long getAndDecrement() {
        return unsafe.getAndAddLong(this, valueOffset, -1L);
    }

    public final long getAndAdd(long j2) {
        return unsafe.getAndAddLong(this, valueOffset, j2);
    }

    public final long incrementAndGet() {
        return unsafe.getAndAddLong(this, valueOffset, 1L) + 1;
    }

    public final long decrementAndGet() {
        return unsafe.getAndAddLong(this, valueOffset, -1L) - 1;
    }

    public final long addAndGet(long j2) {
        return unsafe.getAndAddLong(this, valueOffset, j2) + j2;
    }

    public final long getAndUpdate(LongUnaryOperator longUnaryOperator) {
        long j2;
        do {
            j2 = get();
        } while (!compareAndSet(j2, longUnaryOperator.applyAsLong(j2)));
        return j2;
    }

    public final long updateAndGet(LongUnaryOperator longUnaryOperator) {
        long j2;
        long jApplyAsLong;
        do {
            j2 = get();
            jApplyAsLong = longUnaryOperator.applyAsLong(j2);
        } while (!compareAndSet(j2, jApplyAsLong));
        return jApplyAsLong;
    }

    public final long getAndAccumulate(long j2, LongBinaryOperator longBinaryOperator) {
        long j3;
        do {
            j3 = get();
        } while (!compareAndSet(j3, longBinaryOperator.applyAsLong(j3, j2)));
        return j3;
    }

    public final long accumulateAndGet(long j2, LongBinaryOperator longBinaryOperator) {
        long j3;
        long jApplyAsLong;
        do {
            j3 = get();
            jApplyAsLong = longBinaryOperator.applyAsLong(j3, j2);
        } while (!compareAndSet(j3, jApplyAsLong));
        return jApplyAsLong;
    }

    public String toString() {
        return Long.toString(get());
    }

    @Override // java.lang.Number
    public int intValue() {
        return (int) get();
    }

    @Override // java.lang.Number
    public long longValue() {
        return get();
    }

    @Override // java.lang.Number
    public float floatValue() {
        return get();
    }

    @Override // java.lang.Number
    public double doubleValue() {
        return get();
    }
}
