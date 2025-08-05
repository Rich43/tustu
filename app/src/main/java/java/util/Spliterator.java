package java.util;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

/* loaded from: rt.jar:java/util/Spliterator.class */
public interface Spliterator<T> {
    public static final int ORDERED = 16;
    public static final int DISTINCT = 1;
    public static final int SORTED = 4;
    public static final int SIZED = 64;
    public static final int NONNULL = 256;
    public static final int IMMUTABLE = 1024;
    public static final int CONCURRENT = 4096;
    public static final int SUBSIZED = 16384;

    boolean tryAdvance(Consumer<? super T> consumer);

    Spliterator<T> trySplit();

    long estimateSize();

    int characteristics();

    default void forEachRemaining(Consumer<? super T> consumer) {
        while (tryAdvance(consumer)) {
        }
    }

    default long getExactSizeIfKnown() {
        if ((characteristics() & 64) == 0) {
            return -1L;
        }
        return estimateSize();
    }

    default boolean hasCharacteristics(int i2) {
        return (characteristics() & i2) == i2;
    }

    default Comparator<? super T> getComparator() {
        throw new IllegalStateException();
    }

    /* loaded from: rt.jar:java/util/Spliterator$OfPrimitive.class */
    public interface OfPrimitive<T, T_CONS, T_SPLITR extends OfPrimitive<T, T_CONS, T_SPLITR>> extends Spliterator<T> {
        @Override // java.util.Spliterator
        T_SPLITR trySplit();

        boolean tryAdvance(T_CONS t_cons);

        default void forEachRemaining(T_CONS t_cons) {
            while (tryAdvance((OfPrimitive<T, T_CONS, T_SPLITR>) t_cons)) {
            }
        }
    }

    /* loaded from: rt.jar:java/util/Spliterator$OfInt.class */
    public interface OfInt extends OfPrimitive<Integer, IntConsumer, OfInt> {
        @Override // java.util.Spliterator.OfPrimitive, java.util.Spliterator
        OfInt trySplit();

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfPrimitive
        boolean tryAdvance(IntConsumer intConsumer);

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfPrimitive
        default void forEachRemaining(IntConsumer intConsumer) {
            while (tryAdvance(intConsumer)) {
            }
        }

        @Override // java.util.Spliterator
        default boolean tryAdvance(Consumer<? super Integer> consumer) {
            if (consumer instanceof IntConsumer) {
                return tryAdvance((IntConsumer) consumer);
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Spliterator.OfInt.tryAdvance((IntConsumer) action::accept)");
            }
            consumer.getClass();
            return tryAdvance((v1) -> {
                r1.accept(v1);
            });
        }

        @Override // java.util.Spliterator
        default void forEachRemaining(Consumer<? super Integer> consumer) {
            if (consumer instanceof IntConsumer) {
                forEachRemaining((IntConsumer) consumer);
                return;
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Spliterator.OfInt.forEachRemaining((IntConsumer) action::accept)");
            }
            consumer.getClass();
            forEachRemaining((v1) -> {
                r1.accept(v1);
            });
        }
    }

    /* loaded from: rt.jar:java/util/Spliterator$OfLong.class */
    public interface OfLong extends OfPrimitive<Long, LongConsumer, OfLong> {
        @Override // java.util.Spliterator.OfPrimitive, java.util.Spliterator
        OfLong trySplit();

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfPrimitive
        boolean tryAdvance(LongConsumer longConsumer);

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfPrimitive
        default void forEachRemaining(LongConsumer longConsumer) {
            while (tryAdvance(longConsumer)) {
            }
        }

        @Override // java.util.Spliterator
        default boolean tryAdvance(Consumer<? super Long> consumer) {
            if (consumer instanceof LongConsumer) {
                return tryAdvance((LongConsumer) consumer);
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Spliterator.OfLong.tryAdvance((LongConsumer) action::accept)");
            }
            consumer.getClass();
            return tryAdvance((v1) -> {
                r1.accept(v1);
            });
        }

        @Override // java.util.Spliterator
        default void forEachRemaining(Consumer<? super Long> consumer) {
            if (consumer instanceof LongConsumer) {
                forEachRemaining((LongConsumer) consumer);
                return;
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Spliterator.OfLong.forEachRemaining((LongConsumer) action::accept)");
            }
            consumer.getClass();
            forEachRemaining((v1) -> {
                r1.accept(v1);
            });
        }
    }

    /* loaded from: rt.jar:java/util/Spliterator$OfDouble.class */
    public interface OfDouble extends OfPrimitive<Double, DoubleConsumer, OfDouble> {
        @Override // java.util.Spliterator.OfPrimitive, java.util.Spliterator
        OfDouble trySplit();

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfPrimitive
        boolean tryAdvance(DoubleConsumer doubleConsumer);

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfPrimitive
        default void forEachRemaining(DoubleConsumer doubleConsumer) {
            while (tryAdvance(doubleConsumer)) {
            }
        }

        @Override // java.util.Spliterator
        default boolean tryAdvance(Consumer<? super Double> consumer) {
            if (consumer instanceof DoubleConsumer) {
                return tryAdvance((DoubleConsumer) consumer);
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Spliterator.OfDouble.tryAdvance((DoubleConsumer) action::accept)");
            }
            consumer.getClass();
            return tryAdvance((v1) -> {
                r1.accept(v1);
            });
        }

        @Override // java.util.Spliterator
        default void forEachRemaining(Consumer<? super Double> consumer) {
            if (consumer instanceof DoubleConsumer) {
                forEachRemaining((DoubleConsumer) consumer);
                return;
            }
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Spliterator.OfDouble.forEachRemaining((DoubleConsumer) action::accept)");
            }
            consumer.getClass();
            forEachRemaining((v1) -> {
                r1.accept(v1);
            });
        }
    }
}
