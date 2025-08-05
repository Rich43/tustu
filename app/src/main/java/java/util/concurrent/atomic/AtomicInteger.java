package java.util.concurrent.atomic;

import java.io.Serializable;
import java.util.function.IntBinaryOperator;
import java.util.function.IntUnaryOperator;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/atomic/AtomicInteger.class */
public class AtomicInteger extends Number implements Serializable {
    private static final long serialVersionUID = 6214790243416807050L;
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long valueOffset;
    private volatile int value;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset(AtomicInteger.class.getDeclaredField("value"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    public AtomicInteger(int i2) {
        this.value = i2;
    }

    public AtomicInteger() {
    }

    public final int get() {
        return this.value;
    }

    public final void set(int i2) {
        this.value = i2;
    }

    public final void lazySet(int i2) {
        unsafe.putOrderedInt(this, valueOffset, i2);
    }

    public final int getAndSet(int i2) {
        return unsafe.getAndSetInt(this, valueOffset, i2);
    }

    public final boolean compareAndSet(int i2, int i3) {
        return unsafe.compareAndSwapInt(this, valueOffset, i2, i3);
    }

    public final boolean weakCompareAndSet(int i2, int i3) {
        return unsafe.compareAndSwapInt(this, valueOffset, i2, i3);
    }

    public final int getAndIncrement() {
        return unsafe.getAndAddInt(this, valueOffset, 1);
    }

    public final int getAndDecrement() {
        return unsafe.getAndAddInt(this, valueOffset, -1);
    }

    public final int getAndAdd(int i2) {
        return unsafe.getAndAddInt(this, valueOffset, i2);
    }

    public final int incrementAndGet() {
        return unsafe.getAndAddInt(this, valueOffset, 1) + 1;
    }

    public final int decrementAndGet() {
        return unsafe.getAndAddInt(this, valueOffset, -1) - 1;
    }

    public final int addAndGet(int i2) {
        return unsafe.getAndAddInt(this, valueOffset, i2) + i2;
    }

    public final int getAndUpdate(IntUnaryOperator intUnaryOperator) {
        int i2;
        do {
            i2 = get();
        } while (!compareAndSet(i2, intUnaryOperator.applyAsInt(i2)));
        return i2;
    }

    public final int updateAndGet(IntUnaryOperator intUnaryOperator) {
        int i2;
        int iApplyAsInt;
        do {
            i2 = get();
            iApplyAsInt = intUnaryOperator.applyAsInt(i2);
        } while (!compareAndSet(i2, iApplyAsInt));
        return iApplyAsInt;
    }

    public final int getAndAccumulate(int i2, IntBinaryOperator intBinaryOperator) {
        int i3;
        do {
            i3 = get();
        } while (!compareAndSet(i3, intBinaryOperator.applyAsInt(i3, i2)));
        return i3;
    }

    public final int accumulateAndGet(int i2, IntBinaryOperator intBinaryOperator) {
        int i3;
        int iApplyAsInt;
        do {
            i3 = get();
            iApplyAsInt = intBinaryOperator.applyAsInt(i3, i2);
        } while (!compareAndSet(i3, iApplyAsInt));
        return iApplyAsInt;
    }

    public String toString() {
        return Integer.toString(get());
    }

    @Override // java.lang.Number
    public int intValue() {
        return get();
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
