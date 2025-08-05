package java.util.concurrent.atomic;

import java.io.Serializable;
import java.util.function.BinaryOperator;
import java.util.function.UnaryOperator;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/atomic/AtomicReference.class */
public class AtomicReference<V> implements Serializable {
    private static final long serialVersionUID = -1848883965231344442L;
    private static final Unsafe unsafe = Unsafe.getUnsafe();
    private static final long valueOffset;
    private volatile V value;

    static {
        try {
            valueOffset = unsafe.objectFieldOffset(AtomicReference.class.getDeclaredField("value"));
        } catch (Exception e2) {
            throw new Error(e2);
        }
    }

    public AtomicReference(V v2) {
        this.value = v2;
    }

    public AtomicReference() {
    }

    public final V get() {
        return this.value;
    }

    public final void set(V v2) {
        this.value = v2;
    }

    public final void lazySet(V v2) {
        unsafe.putOrderedObject(this, valueOffset, v2);
    }

    public final boolean compareAndSet(V v2, V v3) {
        return unsafe.compareAndSwapObject(this, valueOffset, v2, v3);
    }

    public final boolean weakCompareAndSet(V v2, V v3) {
        return unsafe.compareAndSwapObject(this, valueOffset, v2, v3);
    }

    public final V getAndSet(V v2) {
        return (V) unsafe.getAndSetObject(this, valueOffset, v2);
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final V getAndUpdate(UnaryOperator<V> unaryOperator) {
        V v2;
        do {
            v2 = get();
        } while (!compareAndSet(v2, unaryOperator.apply(v2)));
        return v2;
    }

    public final V updateAndGet(UnaryOperator<V> unaryOperator) {
        V v2;
        V v3;
        do {
            v2 = get();
            v3 = (V) unaryOperator.apply(v2);
        } while (!compareAndSet(v2, v3));
        return v3;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public final V getAndAccumulate(V v2, BinaryOperator<V> binaryOperator) {
        V v3;
        do {
            v3 = get();
        } while (!compareAndSet(v3, binaryOperator.apply(v3, v2)));
        return v3;
    }

    public final V accumulateAndGet(V v2, BinaryOperator<V> binaryOperator) {
        V v3;
        V v4;
        do {
            v3 = get();
            v4 = (V) binaryOperator.apply(v3, v2);
        } while (!compareAndSet(v3, v4));
        return v4;
    }

    public String toString() {
        return String.valueOf(get());
    }
}
