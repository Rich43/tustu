package java.util;

import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

/* loaded from: rt.jar:java/util/PrimitiveIterator.class */
public interface PrimitiveIterator<T, T_CONS> extends Iterator<T> {
    void forEachRemaining(T_CONS t_cons);

    /* loaded from: rt.jar:java/util/PrimitiveIterator$OfInt.class */
    public interface OfInt extends PrimitiveIterator<Integer, IntConsumer> {
        int nextInt();

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.PrimitiveIterator
        default void forEachRemaining(IntConsumer intConsumer) {
            Objects.requireNonNull(intConsumer);
            while (hasNext()) {
                intConsumer.accept(nextInt());
            }
        }

        @Override // java.util.Iterator
        default Integer next() {
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling PrimitiveIterator.OfInt.nextInt()");
            }
            return Integer.valueOf(nextInt());
        }

        @Override // java.util.Iterator
        default void forEachRemaining(Consumer<? super Integer> consumer) {
            if (consumer instanceof IntConsumer) {
                forEachRemaining((IntConsumer) consumer);
                return;
            }
            Objects.requireNonNull(consumer);
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling PrimitiveIterator.OfInt.forEachRemainingInt(action::accept)");
            }
            consumer.getClass();
            forEachRemaining((v1) -> {
                r1.accept(v1);
            });
        }
    }

    /* loaded from: rt.jar:java/util/PrimitiveIterator$OfLong.class */
    public interface OfLong extends PrimitiveIterator<Long, LongConsumer> {
        long nextLong();

        @Override // java.util.PrimitiveIterator
        default void forEachRemaining(LongConsumer longConsumer) {
            Objects.requireNonNull(longConsumer);
            while (hasNext()) {
                longConsumer.accept(nextLong());
            }
        }

        @Override // java.util.Iterator
        default Long next() {
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling PrimitiveIterator.OfLong.nextLong()");
            }
            return Long.valueOf(nextLong());
        }

        @Override // java.util.Iterator
        default void forEachRemaining(Consumer<? super Long> consumer) {
            if (consumer instanceof LongConsumer) {
                forEachRemaining((LongConsumer) consumer);
                return;
            }
            Objects.requireNonNull(consumer);
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling PrimitiveIterator.OfLong.forEachRemainingLong(action::accept)");
            }
            consumer.getClass();
            forEachRemaining((v1) -> {
                r1.accept(v1);
            });
        }
    }

    /* loaded from: rt.jar:java/util/PrimitiveIterator$OfDouble.class */
    public interface OfDouble extends PrimitiveIterator<Double, DoubleConsumer> {
        double nextDouble();

        @Override // java.util.PrimitiveIterator
        default void forEachRemaining(DoubleConsumer doubleConsumer) {
            Objects.requireNonNull(doubleConsumer);
            while (hasNext()) {
                doubleConsumer.accept(nextDouble());
            }
        }

        @Override // java.util.Iterator
        default Double next() {
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling PrimitiveIterator.OfDouble.nextLong()");
            }
            return Double.valueOf(nextDouble());
        }

        @Override // java.util.Iterator
        default void forEachRemaining(Consumer<? super Double> consumer) {
            if (consumer instanceof DoubleConsumer) {
                forEachRemaining((DoubleConsumer) consumer);
                return;
            }
            Objects.requireNonNull(consumer);
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling PrimitiveIterator.OfDouble.forEachRemainingDouble(action::accept)");
            }
            consumer.getClass();
            forEachRemaining((v1) -> {
                r1.accept(v1);
            });
        }
    }
}
