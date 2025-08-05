package java.util.concurrent.atomic;

import sun.misc.Unsafe;

/* loaded from: rt.jar:java/util/concurrent/atomic/AtomicMarkableReference.class */
public class AtomicMarkableReference<V> {
    private volatile Pair<V> pair;
    private static final Unsafe UNSAFE = Unsafe.getUnsafe();
    private static final long pairOffset = objectFieldOffset(UNSAFE, "pair", AtomicMarkableReference.class);

    /* loaded from: rt.jar:java/util/concurrent/atomic/AtomicMarkableReference$Pair.class */
    private static class Pair<T> {
        final T reference;
        final boolean mark;

        private Pair(T t2, boolean z2) {
            this.reference = t2;
            this.mark = z2;
        }

        static <T> Pair<T> of(T t2, boolean z2) {
            return new Pair<>(t2, z2);
        }
    }

    public AtomicMarkableReference(V v2, boolean z2) {
        this.pair = Pair.of(v2, z2);
    }

    public V getReference() {
        return this.pair.reference;
    }

    public boolean isMarked() {
        return this.pair.mark;
    }

    public V get(boolean[] zArr) {
        Pair<V> pair = this.pair;
        zArr[0] = pair.mark;
        return pair.reference;
    }

    public boolean weakCompareAndSet(V v2, V v3, boolean z2, boolean z3) {
        return compareAndSet(v2, v3, z2, z3);
    }

    public boolean compareAndSet(V v2, V v3, boolean z2, boolean z3) {
        Pair<V> pair = this.pair;
        return v2 == pair.reference && z2 == pair.mark && ((v3 == pair.reference && z3 == pair.mark) || casPair(pair, Pair.of(v3, z3)));
    }

    public void set(V v2, boolean z2) {
        Pair<V> pair = this.pair;
        if (v2 != pair.reference || z2 != pair.mark) {
            this.pair = Pair.of(v2, z2);
        }
    }

    public boolean attemptMark(V v2, boolean z2) {
        Pair<V> pair = this.pair;
        return v2 == pair.reference && (z2 == pair.mark || casPair(pair, Pair.of(v2, z2)));
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
