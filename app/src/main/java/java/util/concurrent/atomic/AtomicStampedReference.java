package java.util.concurrent.atomic;

import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/atomic/AtomicStampedReference.class */
public class AtomicStampedReference<V> {
    private volatile Pair<V> pair;
    private static final Unsafe UNSAFE = Unsafe.getUnsafe();
    private static final long pairOffset = objectFieldOffset(UNSAFE, "pair", AtomicStampedReference.class);

    /* loaded from: rt.jar:java/util/concurrent/atomic/AtomicStampedReference$Pair.class */
    private static class Pair<T> {
        final T reference;
        final int stamp;

        private Pair(T t2, int i2) {
            this.reference = t2;
            this.stamp = i2;
        }

        static <T> Pair<T> of(T t2, int i2) {
            return new Pair<>(t2, i2);
        }
    }

    public AtomicStampedReference(V v2, int i2) {
        this.pair = Pair.of(v2, i2);
    }

    public V getReference() {
        return this.pair.reference;
    }

    public int getStamp() {
        return this.pair.stamp;
    }

    public V get(int[] iArr) {
        Pair<V> pair = this.pair;
        iArr[0] = pair.stamp;
        return pair.reference;
    }

    public boolean weakCompareAndSet(V v2, V v3, int i2, int i3) {
        return compareAndSet(v2, v3, i2, i3);
    }

    public boolean compareAndSet(V v2, V v3, int i2, int i3) {
        Pair<V> pair = this.pair;
        return v2 == pair.reference && i2 == pair.stamp && ((v3 == pair.reference && i3 == pair.stamp) || casPair(pair, Pair.of(v3, i3)));
    }

    public void set(V v2, int i2) {
        Pair<V> pair = this.pair;
        if (v2 != pair.reference || i2 != pair.stamp) {
            this.pair = Pair.of(v2, i2);
        }
    }

    public boolean attemptStamp(V v2, int i2) {
        Pair<V> pair = this.pair;
        return v2 == pair.reference && (i2 == pair.stamp || casPair(pair, Pair.of(v2, i2)));
    }

    private boolean casPair(Pair<V> pair, Pair<V> pair2) {
        return UNSAFE.compareAndSwapObject(this, pairOffset, pair, pair2);
    }

    static long objectFieldOffset(Unsafe unsafe, String str, Class<?> cls) {
        try {
            return unsafe.objectFieldOffset(cls.getDeclaredField(str));
        } catch (NoSuchFieldException e2) {
            NoSuchFieldError noSuchFieldError = new NoSuchFieldError(str);
            noSuchFieldError.initCause(e2);
            throw noSuchFieldError;
        }
    }
}
