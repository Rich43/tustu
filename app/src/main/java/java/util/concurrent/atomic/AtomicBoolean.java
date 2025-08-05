package java.util.concurrent.atomic;

import java.io.Serializable;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/atomic/AtomicBoolean.class */
public class AtomicBoolean implements Serializable {
    private static final long serialVersionUID = 4654671469794556979L;
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long valueOffset;
    private volatile int value;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset(AtomicBoolean.class.getDeclaredField("value"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    public AtomicBoolean(boolean z2) {
        this.value = z2 ? 1 : 0;
    }

    public AtomicBoolean() {
    }

    public final boolean get() {
        return this.value != 0;
    }

    public final boolean compareAndSet(boolean z2, boolean z3) {
        return unsafe.compareAndSwapInt(this, valueOffset, z2 ? 1 : 0, z3 ? 1 : 0);
    }

    public boolean weakCompareAndSet(boolean z2, boolean z3) {
        return unsafe.compareAndSwapInt(this, valueOffset, z2 ? 1 : 0, z3 ? 1 : 0);
    }

    public final void set(boolean z2) {
        this.value = z2 ? 1 : 0;
    }

    public final void lazySet(boolean z2) {
        unsafe.putOrderedInt(this, valueOffset, z2 ? 1 : 0);
    }

    public final boolean getAndSet(boolean z2) {
        boolean z3;
        do {
            z3 = get();
        } while (!compareAndSet(z3, z2));
        return z3;
    }

    public String toString() {
        return Boolean.toString(get());
    }
}
