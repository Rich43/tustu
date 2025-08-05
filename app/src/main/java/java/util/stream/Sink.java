package java.util.stream;

import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;

/* loaded from: rt.jar:java/util/stream/Sink.class */
interface Sink<T> extends Consumer<T> {
    default void begin(long j2) {
    }

    default void end() {
    }

    default boolean cancellationRequested() {
        return false;
    }

    default void accept(int i2) {
        throw new IllegalStateException("called wrong accept method");
    }

    default void accept(long j2) {
        throw new IllegalStateException("called wrong accept method");
    }

    default void accept(double d2) {
        throw new IllegalStateException("called wrong accept method");
    }

    /* loaded from: rt.jar:java/util/stream/Sink$OfInt.class */
    public interface OfInt extends Sink<Integer>, IntConsumer {
        @Override // java.util.function.IntConsumer
        void accept(int i2);

        @Override // java.util.function.Consumer
        default void accept(Integer num) {
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Sink.OfInt.accept(Integer)");
            }
            accept(num.intValue());
        }
    }

    /* loaded from: rt.jar:java/util/stream/Sink$OfLong.class */
    public interface OfLong extends Sink<Long>, LongConsumer {
        @Override // java.util.function.LongConsumer
        void accept(long j2);

        @Override // java.util.function.Consumer
        default void accept(Long l2) {
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Sink.OfLong.accept(Long)");
            }
            accept(l2.longValue());
        }
    }

    /* loaded from: rt.jar:java/util/stream/Sink$OfDouble.class */
    public interface OfDouble extends Sink<Double>, DoubleConsumer {
        @Override // java.util.stream.Sink, java.util.function.DoubleConsumer
        void accept(double d2);

        @Override // java.util.function.Consumer
        default void accept(Double d2) {
            if (Tripwire.ENABLED) {
                Tripwire.trip(getClass(), "{0} calling Sink.OfDouble.accept(Double)");
            }
            accept(d2.doubleValue());
        }
    }

    /* loaded from: rt.jar:java/util/stream/Sink$ChainedReference.class */
    public static abstract class ChainedReference<T, E_OUT> implements Sink<T> {
        protected final Sink<? super E_OUT> downstream;

        public ChainedReference(Sink<? super E_OUT> sink) {
            this.downstream = (Sink) Objects.requireNonNull(sink);
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            this.downstream.begin(j2);
        }

        @Override // java.util.stream.Sink
        public void end() {
            this.downstream.end();
        }

        @Override // java.util.stream.Sink
        public boolean cancellationRequested() {
            return this.downstream.cancellationRequested();
        }
    }

    /* loaded from: rt.jar:java/util/stream/Sink$ChainedInt.class */
    public static abstract class ChainedInt<E_OUT> implements OfInt {
        protected final Sink<? super E_OUT> downstream;

        public ChainedInt(Sink<? super E_OUT> sink) {
            this.downstream = (Sink) Objects.requireNonNull(sink);
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            this.downstream.begin(j2);
        }

        @Override // java.util.stream.Sink
        public void end() {
            this.downstream.end();
        }

        @Override // java.util.stream.Sink
        public boolean cancellationRequested() {
            return this.downstream.cancellationRequested();
        }
    }

    /* loaded from: rt.jar:java/util/stream/Sink$ChainedLong.class */
    public static abstract class ChainedLong<E_OUT> implements OfLong {
        protected final Sink<? super E_OUT> downstream;

        public ChainedLong(Sink<? super E_OUT> sink) {
            this.downstream = (Sink) Objects.requireNonNull(sink);
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            this.downstream.begin(j2);
        }

        @Override // java.util.stream.Sink
        public void end() {
            this.downstream.end();
        }

        @Override // java.util.stream.Sink
        public boolean cancellationRequested() {
            return this.downstream.cancellationRequested();
        }
    }

    /* loaded from: rt.jar:java/util/stream/Sink$ChainedDouble.class */
    public static abstract class ChainedDouble<E_OUT> implements OfDouble {
        protected final Sink<? super E_OUT> downstream;

        public ChainedDouble(Sink<? super E_OUT> sink) {
            this.downstream = (Sink) Objects.requireNonNull(sink);
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            this.downstream.begin(j2);
        }

        @Override // java.util.stream.Sink
        public void end() {
            this.downstream.end();
        }

        @Override // java.util.stream.Sink
        public boolean cancellationRequested() {
            return this.downstream.cancellationRequested();
        }
    }
}
