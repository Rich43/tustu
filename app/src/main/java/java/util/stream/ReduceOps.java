package java.util.stream;

import java.util.Objects;
import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.DoubleBinaryOperator;
import java.util.function.IntBinaryOperator;
import java.util.function.LongBinaryOperator;
import java.util.function.ObjDoubleConsumer;
import java.util.function.ObjIntConsumer;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Sink;

/* loaded from: rt.jar:java/util/stream/ReduceOps.class */
final class ReduceOps {

    /* loaded from: rt.jar:java/util/stream/ReduceOps$AccumulatingSink.class */
    private interface AccumulatingSink<T, R, K extends AccumulatingSink<T, R, K>> extends TerminalSink<T, R> {
        void combine(K k2);
    }

    private ReduceOps() {
    }

    public static <T, U> TerminalOp<T, U> makeRef(final U u2, final BiFunction<U, ? super T, U> biFunction, final BinaryOperator<U> binaryOperator) {
        Objects.requireNonNull(biFunction);
        Objects.requireNonNull(binaryOperator);
        return new ReduceOp<T, U, C1ReducingSink>(StreamShape.REFERENCE) { // from class: java.util.stream.ReduceOps.1
            @Override // java.util.stream.ReduceOps.ReduceOp
            public C1ReducingSink makeSink() {
                return new C1ReducingSink(u2, biFunction, binaryOperator);
            }
        };
    }

    /* JADX INFO: Add missing generic type declarations: [T, U] */
    /* renamed from: java.util.stream.ReduceOps$1ReducingSink, reason: invalid class name */
    /* loaded from: rt.jar:java/util/stream/ReduceOps$1ReducingSink.class */
    class C1ReducingSink<T, U> extends Box<U> implements AccumulatingSink<T, U, C1ReducingSink> {
        final /* synthetic */ Object val$seed;
        final /* synthetic */ BiFunction val$reducer;
        final /* synthetic */ BinaryOperator val$combiner;

        C1ReducingSink(Object obj, BiFunction biFunction, BinaryOperator binaryOperator) {
            this.val$seed = obj;
            this.val$reducer = biFunction;
            this.val$combiner = binaryOperator;
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            this.state = (U) this.val$seed;
        }

        @Override // java.util.function.Consumer
        public void accept(T t2) {
            this.state = (U) this.val$reducer.apply(this.state, t2);
        }

        @Override // java.util.stream.ReduceOps.AccumulatingSink
        public void combine(C1ReducingSink c1ReducingSink) {
            this.state = (U) this.val$combiner.apply(this.state, c1ReducingSink.state);
        }
    }

    public static <T> TerminalOp<T, Optional<T>> makeRef(final BinaryOperator<T> binaryOperator) {
        Objects.requireNonNull(binaryOperator);
        return new ReduceOp<T, Optional<T>, C2ReducingSink>(StreamShape.REFERENCE) { // from class: java.util.stream.ReduceOps.2
            @Override // java.util.stream.ReduceOps.ReduceOp
            public C2ReducingSink makeSink() {
                final BinaryOperator binaryOperator2 = binaryOperator;
                return new AccumulatingSink<T, Optional<T>, C2ReducingSink>() { // from class: java.util.stream.ReduceOps.2ReducingSink
                    private boolean empty;
                    private T state;

                    @Override // java.util.stream.Sink
                    public void begin(long j2) {
                        this.empty = true;
                        this.state = null;
                    }

                    @Override // java.util.function.Consumer
                    public void accept(T t2) {
                        if (this.empty) {
                            this.empty = false;
                            this.state = t2;
                        } else {
                            this.state = binaryOperator2.apply(this.state, t2);
                        }
                    }

                    @Override // java.util.function.Supplier
                    public Optional<T> get() {
                        return this.empty ? Optional.empty() : Optional.of(this.state);
                    }

                    @Override // java.util.stream.ReduceOps.AccumulatingSink
                    public void combine(C2ReducingSink c2ReducingSink) {
                        if (!c2ReducingSink.empty) {
                            accept((C2ReducingSink<T>) c2ReducingSink.state);
                        }
                    }
                };
            }
        };
    }

    public static <T, I> TerminalOp<T, I> makeRef(final Collector<? super T, I, ?> collector) {
        final Supplier supplier = ((Collector) Objects.requireNonNull(collector)).supplier();
        final BiConsumer<I, ? super T> biConsumerAccumulator = collector.accumulator();
        final BinaryOperator<I> binaryOperatorCombiner = collector.combiner();
        return new ReduceOp<T, I, C3ReducingSink>(StreamShape.REFERENCE) { // from class: java.util.stream.ReduceOps.3
            @Override // java.util.stream.ReduceOps.ReduceOp
            public C3ReducingSink makeSink() {
                return new C3ReducingSink(supplier, biConsumerAccumulator, binaryOperatorCombiner);
            }

            @Override // java.util.stream.TerminalOp
            public int getOpFlags() {
                if (collector.characteristics().contains(Collector.Characteristics.UNORDERED)) {
                    return StreamOpFlag.NOT_ORDERED;
                }
                return 0;
            }
        };
    }

    /* JADX INFO: Add missing generic type declarations: [T, I] */
    /* renamed from: java.util.stream.ReduceOps$3ReducingSink, reason: invalid class name */
    /* loaded from: rt.jar:java/util/stream/ReduceOps$3ReducingSink.class */
    class C3ReducingSink<I, T> extends Box<I> implements AccumulatingSink<T, I, C3ReducingSink> {
        final /* synthetic */ Supplier val$supplier;
        final /* synthetic */ BiConsumer val$accumulator;
        final /* synthetic */ BinaryOperator val$combiner;

        C3ReducingSink(Supplier supplier, BiConsumer biConsumer, BinaryOperator binaryOperator) {
            this.val$supplier = supplier;
            this.val$accumulator = biConsumer;
            this.val$combiner = binaryOperator;
        }

        /* JADX WARN: Type inference failed for: r1v2, types: [U, java.lang.Object] */
        @Override // java.util.stream.Sink
        public void begin(long j2) {
            this.state = this.val$supplier.get();
        }

        @Override // java.util.function.Consumer
        public void accept(T t2) {
            this.val$accumulator.accept(this.state, t2);
        }

        /* JADX WARN: Type inference failed for: r1v2, types: [U, java.lang.Object] */
        @Override // java.util.stream.ReduceOps.AccumulatingSink
        public void combine(C3ReducingSink c3ReducingSink) {
            this.state = this.val$combiner.apply(this.state, c3ReducingSink.state);
        }
    }

    public static <T, R> TerminalOp<T, R> makeRef(final Supplier<R> supplier, final BiConsumer<R, ? super T> biConsumer, final BiConsumer<R, R> biConsumer2) {
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(biConsumer);
        Objects.requireNonNull(biConsumer2);
        return new ReduceOp<T, R, C4ReducingSink>(StreamShape.REFERENCE) { // from class: java.util.stream.ReduceOps.4
            @Override // java.util.stream.ReduceOps.ReduceOp
            public C4ReducingSink makeSink() {
                return new C4ReducingSink(supplier, biConsumer, biConsumer2);
            }
        };
    }

    /* JADX INFO: Add missing generic type declarations: [R, T] */
    /* renamed from: java.util.stream.ReduceOps$4ReducingSink, reason: invalid class name */
    /* loaded from: rt.jar:java/util/stream/ReduceOps$4ReducingSink.class */
    class C4ReducingSink<R, T> extends Box<R> implements AccumulatingSink<T, R, C4ReducingSink> {
        final /* synthetic */ Supplier val$seedFactory;
        final /* synthetic */ BiConsumer val$accumulator;
        final /* synthetic */ BiConsumer val$reducer;

        C4ReducingSink(Supplier supplier, BiConsumer biConsumer, BiConsumer biConsumer2) {
            this.val$seedFactory = supplier;
            this.val$accumulator = biConsumer;
            this.val$reducer = biConsumer2;
        }

        /* JADX WARN: Type inference failed for: r1v2, types: [U, java.lang.Object] */
        @Override // java.util.stream.Sink
        public void begin(long j2) {
            this.state = this.val$seedFactory.get();
        }

        @Override // java.util.function.Consumer
        public void accept(T t2) {
            this.val$accumulator.accept(this.state, t2);
        }

        @Override // java.util.stream.ReduceOps.AccumulatingSink
        public void combine(C4ReducingSink c4ReducingSink) {
            this.val$reducer.accept(this.state, c4ReducingSink.state);
        }
    }

    /* renamed from: java.util.stream.ReduceOps$5ReducingSink, reason: invalid class name */
    /* loaded from: rt.jar:java/util/stream/ReduceOps$5ReducingSink.class */
    class C5ReducingSink implements AccumulatingSink<Integer, Integer, C5ReducingSink>, Sink.OfInt {
        private int state;
        final /* synthetic */ int val$identity;
        final /* synthetic */ IntBinaryOperator val$operator;

        C5ReducingSink(int i2, IntBinaryOperator intBinaryOperator) {
            this.val$identity = i2;
            this.val$operator = intBinaryOperator;
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            this.state = this.val$identity;
        }

        @Override // java.util.stream.Sink, java.util.stream.Sink.OfInt, java.util.function.IntConsumer
        public void accept(int i2) {
            this.state = this.val$operator.applyAsInt(this.state, i2);
        }

        @Override // java.util.function.Supplier
        public Integer get() {
            return Integer.valueOf(this.state);
        }

        @Override // java.util.stream.ReduceOps.AccumulatingSink
        public void combine(C5ReducingSink c5ReducingSink) {
            accept(c5ReducingSink.state);
        }
    }

    public static TerminalOp<Integer, Integer> makeInt(final int i2, final IntBinaryOperator intBinaryOperator) {
        Objects.requireNonNull(intBinaryOperator);
        return new ReduceOp<Integer, Integer, C5ReducingSink>(StreamShape.INT_VALUE) { // from class: java.util.stream.ReduceOps.5
            @Override // java.util.stream.ReduceOps.ReduceOp
            public C5ReducingSink makeSink() {
                return new C5ReducingSink(i2, intBinaryOperator);
            }
        };
    }

    /* renamed from: java.util.stream.ReduceOps$6ReducingSink, reason: invalid class name */
    /* loaded from: rt.jar:java/util/stream/ReduceOps$6ReducingSink.class */
    class C6ReducingSink implements AccumulatingSink<Integer, OptionalInt, C6ReducingSink>, Sink.OfInt {
        private boolean empty;
        private int state;
        final /* synthetic */ IntBinaryOperator val$operator;

        C6ReducingSink(IntBinaryOperator intBinaryOperator) {
            this.val$operator = intBinaryOperator;
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            this.empty = true;
            this.state = 0;
        }

        @Override // java.util.stream.Sink, java.util.stream.Sink.OfInt, java.util.function.IntConsumer
        public void accept(int i2) {
            if (this.empty) {
                this.empty = false;
                this.state = i2;
            } else {
                this.state = this.val$operator.applyAsInt(this.state, i2);
            }
        }

        @Override // java.util.function.Supplier
        public OptionalInt get() {
            return this.empty ? OptionalInt.empty() : OptionalInt.of(this.state);
        }

        @Override // java.util.stream.ReduceOps.AccumulatingSink
        public void combine(C6ReducingSink c6ReducingSink) {
            if (!c6ReducingSink.empty) {
                accept(c6ReducingSink.state);
            }
        }
    }

    public static TerminalOp<Integer, OptionalInt> makeInt(final IntBinaryOperator intBinaryOperator) {
        Objects.requireNonNull(intBinaryOperator);
        return new ReduceOp<Integer, OptionalInt, C6ReducingSink>(StreamShape.INT_VALUE) { // from class: java.util.stream.ReduceOps.6
            @Override // java.util.stream.ReduceOps.ReduceOp
            public C6ReducingSink makeSink() {
                return new C6ReducingSink(intBinaryOperator);
            }
        };
    }

    public static <R> TerminalOp<Integer, R> makeInt(final Supplier<R> supplier, final ObjIntConsumer<R> objIntConsumer, final BinaryOperator<R> binaryOperator) {
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(objIntConsumer);
        Objects.requireNonNull(binaryOperator);
        return new ReduceOp<Integer, R, C7ReducingSink>(StreamShape.INT_VALUE) { // from class: java.util.stream.ReduceOps.7
            @Override // java.util.stream.ReduceOps.ReduceOp
            public C7ReducingSink makeSink() {
                return new C7ReducingSink(supplier, objIntConsumer, binaryOperator);
            }
        };
    }

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* renamed from: java.util.stream.ReduceOps$7ReducingSink, reason: invalid class name */
    /* loaded from: rt.jar:java/util/stream/ReduceOps$7ReducingSink.class */
    class C7ReducingSink<R> extends Box<R> implements AccumulatingSink<Integer, R, C7ReducingSink>, Sink.OfInt {
        final /* synthetic */ Supplier val$supplier;
        final /* synthetic */ ObjIntConsumer val$accumulator;
        final /* synthetic */ BinaryOperator val$combiner;

        C7ReducingSink(Supplier supplier, ObjIntConsumer objIntConsumer, BinaryOperator binaryOperator) {
            this.val$supplier = supplier;
            this.val$accumulator = objIntConsumer;
            this.val$combiner = binaryOperator;
        }

        /* JADX WARN: Type inference failed for: r1v2, types: [U, java.lang.Object] */
        @Override // java.util.stream.Sink
        public void begin(long j2) {
            this.state = this.val$supplier.get();
        }

        @Override // java.util.stream.Sink, java.util.stream.Sink.OfInt, java.util.function.IntConsumer
        public void accept(int i2) {
            this.val$accumulator.accept(this.state, i2);
        }

        /* JADX WARN: Type inference failed for: r1v2, types: [U, java.lang.Object] */
        @Override // java.util.stream.ReduceOps.AccumulatingSink
        public void combine(C7ReducingSink c7ReducingSink) {
            this.state = this.val$combiner.apply(this.state, c7ReducingSink.state);
        }
    }

    /* renamed from: java.util.stream.ReduceOps$8ReducingSink, reason: invalid class name */
    /* loaded from: rt.jar:java/util/stream/ReduceOps$8ReducingSink.class */
    class C8ReducingSink implements AccumulatingSink<Long, Long, C8ReducingSink>, Sink.OfLong {
        private long state;
        final /* synthetic */ long val$identity;
        final /* synthetic */ LongBinaryOperator val$operator;

        C8ReducingSink(long j2, LongBinaryOperator longBinaryOperator) {
            this.val$identity = j2;
            this.val$operator = longBinaryOperator;
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            this.state = this.val$identity;
        }

        @Override // java.util.stream.Sink, java.util.stream.Sink.OfLong, java.util.function.LongConsumer
        public void accept(long j2) {
            this.state = this.val$operator.applyAsLong(this.state, j2);
        }

        @Override // java.util.function.Supplier
        public Long get() {
            return Long.valueOf(this.state);
        }

        @Override // java.util.stream.ReduceOps.AccumulatingSink
        public void combine(C8ReducingSink c8ReducingSink) {
            accept(c8ReducingSink.state);
        }
    }

    public static TerminalOp<Long, Long> makeLong(final long j2, final LongBinaryOperator longBinaryOperator) {
        Objects.requireNonNull(longBinaryOperator);
        return new ReduceOp<Long, Long, C8ReducingSink>(StreamShape.LONG_VALUE) { // from class: java.util.stream.ReduceOps.8
            @Override // java.util.stream.ReduceOps.ReduceOp
            public C8ReducingSink makeSink() {
                return new C8ReducingSink(j2, longBinaryOperator);
            }
        };
    }

    /* renamed from: java.util.stream.ReduceOps$9ReducingSink, reason: invalid class name */
    /* loaded from: rt.jar:java/util/stream/ReduceOps$9ReducingSink.class */
    class C9ReducingSink implements AccumulatingSink<Long, OptionalLong, C9ReducingSink>, Sink.OfLong {
        private boolean empty;
        private long state;
        final /* synthetic */ LongBinaryOperator val$operator;

        C9ReducingSink(LongBinaryOperator longBinaryOperator) {
            this.val$operator = longBinaryOperator;
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            this.empty = true;
            this.state = 0L;
        }

        @Override // java.util.stream.Sink, java.util.stream.Sink.OfLong, java.util.function.LongConsumer
        public void accept(long j2) {
            if (this.empty) {
                this.empty = false;
                this.state = j2;
            } else {
                this.state = this.val$operator.applyAsLong(this.state, j2);
            }
        }

        @Override // java.util.function.Supplier
        public OptionalLong get() {
            return this.empty ? OptionalLong.empty() : OptionalLong.of(this.state);
        }

        @Override // java.util.stream.ReduceOps.AccumulatingSink
        public void combine(C9ReducingSink c9ReducingSink) {
            if (!c9ReducingSink.empty) {
                accept(c9ReducingSink.state);
            }
        }
    }

    public static TerminalOp<Long, OptionalLong> makeLong(final LongBinaryOperator longBinaryOperator) {
        Objects.requireNonNull(longBinaryOperator);
        return new ReduceOp<Long, OptionalLong, C9ReducingSink>(StreamShape.LONG_VALUE) { // from class: java.util.stream.ReduceOps.9
            @Override // java.util.stream.ReduceOps.ReduceOp
            public C9ReducingSink makeSink() {
                return new C9ReducingSink(longBinaryOperator);
            }
        };
    }

    public static <R> TerminalOp<Long, R> makeLong(final Supplier<R> supplier, final ObjLongConsumer<R> objLongConsumer, final BinaryOperator<R> binaryOperator) {
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(objLongConsumer);
        Objects.requireNonNull(binaryOperator);
        return new ReduceOp<Long, R, C10ReducingSink>(StreamShape.LONG_VALUE) { // from class: java.util.stream.ReduceOps.10
            @Override // java.util.stream.ReduceOps.ReduceOp
            public C10ReducingSink makeSink() {
                return new C10ReducingSink(supplier, objLongConsumer, binaryOperator);
            }
        };
    }

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* renamed from: java.util.stream.ReduceOps$10ReducingSink, reason: invalid class name */
    /* loaded from: rt.jar:java/util/stream/ReduceOps$10ReducingSink.class */
    class C10ReducingSink<R> extends Box<R> implements AccumulatingSink<Long, R, C10ReducingSink>, Sink.OfLong {
        final /* synthetic */ Supplier val$supplier;
        final /* synthetic */ ObjLongConsumer val$accumulator;
        final /* synthetic */ BinaryOperator val$combiner;

        C10ReducingSink(Supplier supplier, ObjLongConsumer objLongConsumer, BinaryOperator binaryOperator) {
            this.val$supplier = supplier;
            this.val$accumulator = objLongConsumer;
            this.val$combiner = binaryOperator;
        }

        /* JADX WARN: Type inference failed for: r1v2, types: [U, java.lang.Object] */
        @Override // java.util.stream.Sink
        public void begin(long j2) {
            this.state = this.val$supplier.get();
        }

        @Override // java.util.stream.Sink, java.util.stream.Sink.OfLong, java.util.function.LongConsumer
        public void accept(long j2) {
            this.val$accumulator.accept(this.state, j2);
        }

        /* JADX WARN: Type inference failed for: r1v2, types: [U, java.lang.Object] */
        @Override // java.util.stream.ReduceOps.AccumulatingSink
        public void combine(C10ReducingSink c10ReducingSink) {
            this.state = this.val$combiner.apply(this.state, c10ReducingSink.state);
        }
    }

    /* renamed from: java.util.stream.ReduceOps$11ReducingSink, reason: invalid class name */
    /* loaded from: rt.jar:java/util/stream/ReduceOps$11ReducingSink.class */
    class C11ReducingSink implements AccumulatingSink<Double, Double, C11ReducingSink>, Sink.OfDouble {
        private double state;
        final /* synthetic */ double val$identity;
        final /* synthetic */ DoubleBinaryOperator val$operator;

        C11ReducingSink(double d2, DoubleBinaryOperator doubleBinaryOperator) {
            this.val$identity = d2;
            this.val$operator = doubleBinaryOperator;
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            this.state = this.val$identity;
        }

        @Override // java.util.stream.Sink, java.util.function.DoubleConsumer
        public void accept(double d2) {
            this.state = this.val$operator.applyAsDouble(this.state, d2);
        }

        @Override // java.util.function.Supplier
        public Double get() {
            return Double.valueOf(this.state);
        }

        @Override // java.util.stream.ReduceOps.AccumulatingSink
        public void combine(C11ReducingSink c11ReducingSink) {
            accept(c11ReducingSink.state);
        }
    }

    public static TerminalOp<Double, Double> makeDouble(final double d2, final DoubleBinaryOperator doubleBinaryOperator) {
        Objects.requireNonNull(doubleBinaryOperator);
        return new ReduceOp<Double, Double, C11ReducingSink>(StreamShape.DOUBLE_VALUE) { // from class: java.util.stream.ReduceOps.11
            @Override // java.util.stream.ReduceOps.ReduceOp
            public C11ReducingSink makeSink() {
                return new C11ReducingSink(d2, doubleBinaryOperator);
            }
        };
    }

    /* renamed from: java.util.stream.ReduceOps$12ReducingSink, reason: invalid class name */
    /* loaded from: rt.jar:java/util/stream/ReduceOps$12ReducingSink.class */
    class C12ReducingSink implements AccumulatingSink<Double, OptionalDouble, C12ReducingSink>, Sink.OfDouble {
        private boolean empty;
        private double state;
        final /* synthetic */ DoubleBinaryOperator val$operator;

        C12ReducingSink(DoubleBinaryOperator doubleBinaryOperator) {
            this.val$operator = doubleBinaryOperator;
        }

        @Override // java.util.stream.Sink
        public void begin(long j2) {
            this.empty = true;
            this.state = 0.0d;
        }

        @Override // java.util.stream.Sink, java.util.function.DoubleConsumer
        public void accept(double d2) {
            if (this.empty) {
                this.empty = false;
                this.state = d2;
            } else {
                this.state = this.val$operator.applyAsDouble(this.state, d2);
            }
        }

        @Override // java.util.function.Supplier
        public OptionalDouble get() {
            return this.empty ? OptionalDouble.empty() : OptionalDouble.of(this.state);
        }

        @Override // java.util.stream.ReduceOps.AccumulatingSink
        public void combine(C12ReducingSink c12ReducingSink) {
            if (!c12ReducingSink.empty) {
                accept(c12ReducingSink.state);
            }
        }
    }

    public static TerminalOp<Double, OptionalDouble> makeDouble(final DoubleBinaryOperator doubleBinaryOperator) {
        Objects.requireNonNull(doubleBinaryOperator);
        return new ReduceOp<Double, OptionalDouble, C12ReducingSink>(StreamShape.DOUBLE_VALUE) { // from class: java.util.stream.ReduceOps.12
            @Override // java.util.stream.ReduceOps.ReduceOp
            public C12ReducingSink makeSink() {
                return new C12ReducingSink(doubleBinaryOperator);
            }
        };
    }

    public static <R> TerminalOp<Double, R> makeDouble(final Supplier<R> supplier, final ObjDoubleConsumer<R> objDoubleConsumer, final BinaryOperator<R> binaryOperator) {
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(objDoubleConsumer);
        Objects.requireNonNull(binaryOperator);
        return new ReduceOp<Double, R, C13ReducingSink>(StreamShape.DOUBLE_VALUE) { // from class: java.util.stream.ReduceOps.13
            @Override // java.util.stream.ReduceOps.ReduceOp
            public C13ReducingSink makeSink() {
                return new C13ReducingSink(supplier, objDoubleConsumer, binaryOperator);
            }
        };
    }

    /* JADX INFO: Add missing generic type declarations: [R] */
    /* renamed from: java.util.stream.ReduceOps$13ReducingSink, reason: invalid class name */
    /* loaded from: rt.jar:java/util/stream/ReduceOps$13ReducingSink.class */
    class C13ReducingSink<R> extends Box<R> implements AccumulatingSink<Double, R, C13ReducingSink>, Sink.OfDouble {
        final /* synthetic */ Supplier val$supplier;
        final /* synthetic */ ObjDoubleConsumer val$accumulator;
        final /* synthetic */ BinaryOperator val$combiner;

        C13ReducingSink(Supplier supplier, ObjDoubleConsumer objDoubleConsumer, BinaryOperator binaryOperator) {
            this.val$supplier = supplier;
            this.val$accumulator = objDoubleConsumer;
            this.val$combiner = binaryOperator;
        }

        /* JADX WARN: Type inference failed for: r1v2, types: [U, java.lang.Object] */
        @Override // java.util.stream.Sink
        public void begin(long j2) {
            this.state = this.val$supplier.get();
        }

        @Override // java.util.stream.Sink, java.util.function.DoubleConsumer
        public void accept(double d2) {
            this.val$accumulator.accept(this.state, d2);
        }

        /* JADX WARN: Type inference failed for: r1v2, types: [U, java.lang.Object] */
        @Override // java.util.stream.ReduceOps.AccumulatingSink
        public void combine(C13ReducingSink c13ReducingSink) {
            this.state = this.val$combiner.apply(this.state, c13ReducingSink.state);
        }
    }

    /* loaded from: rt.jar:java/util/stream/ReduceOps$Box.class */
    private static abstract class Box<U> {
        U state;

        Box() {
        }

        public U get() {
            return this.state;
        }
    }

    /* loaded from: rt.jar:java/util/stream/ReduceOps$ReduceOp.class */
    private static abstract class ReduceOp<T, R, S extends AccumulatingSink<T, R, S>> implements TerminalOp<T, R> {
        private final StreamShape inputShape;

        public abstract S makeSink();

        ReduceOp(StreamShape streamShape) {
            this.inputShape = streamShape;
        }

        @Override // java.util.stream.TerminalOp
        public StreamShape inputShape() {
            return this.inputShape;
        }

        @Override // java.util.stream.TerminalOp
        public <P_IN> R evaluateSequential(PipelineHelper<T> pipelineHelper, Spliterator<P_IN> spliterator) {
            return ((AccumulatingSink) pipelineHelper.wrapAndCopyInto(makeSink(), spliterator)).get();
        }

        @Override // java.util.stream.TerminalOp
        public <P_IN> R evaluateParallel(PipelineHelper<T> pipelineHelper, Spliterator<P_IN> spliterator) {
            return ((AccumulatingSink) new ReduceTask(this, pipelineHelper, spliterator).invoke()).get();
        }
    }

    /* loaded from: rt.jar:java/util/stream/ReduceOps$ReduceTask.class */
    private static final class ReduceTask<P_IN, P_OUT, R, S extends AccumulatingSink<P_OUT, R, S>> extends AbstractTask<P_IN, P_OUT, S, ReduceTask<P_IN, P_OUT, R, S>> {
        private final ReduceOp<P_OUT, R, S> op;

        ReduceTask(ReduceOp<P_OUT, R, S> reduceOp, PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator) {
            super(pipelineHelper, spliterator);
            this.op = reduceOp;
        }

        ReduceTask(ReduceTask<P_IN, P_OUT, R, S> reduceTask, Spliterator<P_IN> spliterator) {
            super(reduceTask, spliterator);
            this.op = reduceTask.op;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.util.stream.AbstractTask
        public ReduceTask<P_IN, P_OUT, R, S> makeChild(Spliterator<P_IN> spliterator) {
            return new ReduceTask<>(this, spliterator);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.util.stream.AbstractTask
        public S doLeaf() {
            return (S) this.helper.wrapAndCopyInto(this.op.makeSink(), this.spliterator);
        }

        @Override // java.util.stream.AbstractTask, java.util.concurrent.CountedCompleter
        public void onCompletion(CountedCompleter<?> countedCompleter) {
            if (!isLeaf()) {
                AccumulatingSink accumulatingSink = (AccumulatingSink) ((ReduceTask) this.leftChild).getLocalResult();
                accumulatingSink.combine((AccumulatingSink) ((ReduceTask) this.rightChild).getLocalResult());
                setLocalResult(accumulatingSink);
            }
            super.onCompletion(countedCompleter);
        }
    }
}
