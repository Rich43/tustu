package java.util.stream;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.Function;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.LongConsumer;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoublePipeline;
import java.util.stream.IntPipeline;
import java.util.stream.LongPipeline;
import java.util.stream.MatchOps;
import java.util.stream.Node;
import java.util.stream.Sink;
import java.util.stream.StreamSpliterators;

/* loaded from: rt.jar:java/util/stream/ReferencePipeline.class */
abstract class ReferencePipeline<P_IN, P_OUT> extends AbstractPipeline<P_IN, P_OUT, Stream<P_OUT>> implements Stream<P_OUT> {
    ReferencePipeline(Supplier<? extends Spliterator<?>> supplier, int i2, boolean z2) {
        super(supplier, i2, z2);
    }

    ReferencePipeline(Spliterator<?> spliterator, int i2, boolean z2) {
        super(spliterator, i2, z2);
    }

    ReferencePipeline(AbstractPipeline<?, P_IN, ?> abstractPipeline, int i2) {
        super(abstractPipeline, i2);
    }

    @Override // java.util.stream.AbstractPipeline
    final StreamShape getOutputShape() {
        return StreamShape.REFERENCE;
    }

    @Override // java.util.stream.AbstractPipeline
    final <P_IN> Node<P_OUT> evaluateToNode(PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator, boolean z2, IntFunction<P_OUT[]> intFunction) {
        return Nodes.collect(pipelineHelper, spliterator, z2, intFunction);
    }

    @Override // java.util.stream.AbstractPipeline
    final <P_IN> Spliterator<P_OUT> wrap(PipelineHelper<P_OUT> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z2) {
        return new StreamSpliterators.WrappingSpliterator(pipelineHelper, supplier, z2);
    }

    @Override // java.util.stream.AbstractPipeline
    /* renamed from: lazySpliterator */
    final Spliterator<P_OUT> lazySpliterator2(Supplier<? extends Spliterator<P_OUT>> supplier) {
        return new StreamSpliterators.DelegatingSpliterator(supplier);
    }

    @Override // java.util.stream.AbstractPipeline
    final void forEachWithCancel(Spliterator<P_OUT> spliterator, Sink<P_OUT> sink) {
        while (!sink.cancellationRequested() && spliterator.tryAdvance(sink)) {
        }
    }

    @Override // java.util.stream.AbstractPipeline, java.util.stream.PipelineHelper
    final Node.Builder<P_OUT> makeNodeBuilder(long j2, IntFunction<P_OUT[]> intFunction) {
        return Nodes.builder(j2, intFunction);
    }

    @Override // java.util.stream.BaseStream, java.util.stream.DoubleStream
    /* renamed from: iterator */
    public final Iterator<P_OUT> iterator2() {
        return Spliterators.iterator(spliterator2());
    }

    @Override // java.util.stream.BaseStream
    public Stream<P_OUT> unordered() {
        if (!isOrdered()) {
            return this;
        }
        return new StatelessOp<P_OUT, P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_ORDERED) { // from class: java.util.stream.ReferencePipeline.1
            @Override // java.util.stream.AbstractPipeline
            Sink<P_OUT> opWrapSink(int i2, Sink<P_OUT> sink) {
                return sink;
            }
        };
    }

    @Override // java.util.stream.Stream
    public final Stream<P_OUT> filter(final Predicate<? super P_OUT> predicate) {
        Objects.requireNonNull(predicate);
        return new StatelessOp<P_OUT, P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SIZED) { // from class: java.util.stream.ReferencePipeline.2
            @Override // java.util.stream.AbstractPipeline
            Sink<P_OUT> opWrapSink(int i2, Sink<P_OUT> sink) {
                return new Sink.ChainedReference<P_OUT, P_OUT>(sink) { // from class: java.util.stream.ReferencePipeline.2.1
                    @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
                    public void begin(long j2) {
                        this.downstream.begin(-1L);
                    }

                    @Override // java.util.function.Consumer
                    public void accept(P_OUT p_out) {
                        if (predicate.test(p_out)) {
                            this.downstream.accept(p_out);
                        }
                    }
                };
            }
        };
    }

    @Override // java.util.stream.Stream
    public final <R> Stream<R> map(final Function<? super P_OUT, ? extends R> function) {
        Objects.requireNonNull(function);
        return new StatelessOp<P_OUT, R>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) { // from class: java.util.stream.ReferencePipeline.3
            @Override // java.util.stream.AbstractPipeline
            Sink<P_OUT> opWrapSink(int i2, Sink<R> sink) {
                return new Sink.ChainedReference<P_OUT, R>(sink) { // from class: java.util.stream.ReferencePipeline.3.1
                    @Override // java.util.function.Consumer
                    public void accept(P_OUT p_out) {
                        this.downstream.accept(function.apply(p_out));
                    }
                };
            }
        };
    }

    @Override // java.util.stream.Stream
    public final IntStream mapToInt(final ToIntFunction<? super P_OUT> toIntFunction) {
        Objects.requireNonNull(toIntFunction);
        return new IntPipeline.StatelessOp<P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) { // from class: java.util.stream.ReferencePipeline.4
            @Override // java.util.stream.AbstractPipeline
            Sink<P_OUT> opWrapSink(int i2, Sink<Integer> sink) {
                return new Sink.ChainedReference<P_OUT, Integer>(sink) { // from class: java.util.stream.ReferencePipeline.4.1
                    @Override // java.util.function.Consumer
                    public void accept(P_OUT p_out) {
                        this.downstream.accept(toIntFunction.applyAsInt(p_out));
                    }
                };
            }
        };
    }

    @Override // java.util.stream.Stream
    public final LongStream mapToLong(final ToLongFunction<? super P_OUT> toLongFunction) {
        Objects.requireNonNull(toLongFunction);
        return new LongPipeline.StatelessOp<P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) { // from class: java.util.stream.ReferencePipeline.5
            @Override // java.util.stream.AbstractPipeline
            Sink<P_OUT> opWrapSink(int i2, Sink<Long> sink) {
                return new Sink.ChainedReference<P_OUT, Long>(sink) { // from class: java.util.stream.ReferencePipeline.5.1
                    @Override // java.util.function.Consumer
                    public void accept(P_OUT p_out) {
                        this.downstream.accept(toLongFunction.applyAsLong(p_out));
                    }
                };
            }
        };
    }

    @Override // java.util.stream.Stream
    public final DoubleStream mapToDouble(final ToDoubleFunction<? super P_OUT> toDoubleFunction) {
        Objects.requireNonNull(toDoubleFunction);
        return new DoublePipeline.StatelessOp<P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) { // from class: java.util.stream.ReferencePipeline.6
            @Override // java.util.stream.AbstractPipeline
            Sink<P_OUT> opWrapSink(int i2, Sink<Double> sink) {
                return new Sink.ChainedReference<P_OUT, Double>(sink) { // from class: java.util.stream.ReferencePipeline.6.1
                    @Override // java.util.function.Consumer
                    public void accept(P_OUT p_out) {
                        this.downstream.accept(toDoubleFunction.applyAsDouble(p_out));
                    }
                };
            }
        };
    }

    @Override // java.util.stream.Stream
    public final <R> Stream<R> flatMap(final Function<? super P_OUT, ? extends Stream<? extends R>> function) {
        Objects.requireNonNull(function);
        return new StatelessOp<P_OUT, R>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) { // from class: java.util.stream.ReferencePipeline.7
            @Override // java.util.stream.AbstractPipeline
            Sink<P_OUT> opWrapSink(int i2, Sink<R> sink) {
                return new Sink.ChainedReference<P_OUT, R>(sink) { // from class: java.util.stream.ReferencePipeline.7.1
                    boolean cancellationRequestedCalled;

                    @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
                    public void begin(long j2) {
                        this.downstream.begin(-1L);
                    }

                    @Override // java.util.function.Consumer
                    public void accept(P_OUT p_out) {
                        Stream stream = (Stream) function.apply(p_out);
                        Throwable th = null;
                        if (stream != null) {
                            try {
                                try {
                                    if (!this.cancellationRequestedCalled) {
                                        stream.sequential().forEach(this.downstream);
                                    } else {
                                        Spliterator<T> spliterator = stream.sequential().spliterator2();
                                        while (!this.downstream.cancellationRequested() && spliterator.tryAdvance(this.downstream)) {
                                        }
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    throw th2;
                                }
                            } catch (Throwable th3) {
                                if (stream != null) {
                                    if (th != null) {
                                        try {
                                            stream.close();
                                        } catch (Throwable th4) {
                                            th.addSuppressed(th4);
                                        }
                                    } else {
                                        stream.close();
                                    }
                                }
                                throw th3;
                            }
                        }
                        if (stream != null) {
                            if (0 != 0) {
                                try {
                                    stream.close();
                                    return;
                                } catch (Throwable th5) {
                                    th.addSuppressed(th5);
                                    return;
                                }
                            }
                            stream.close();
                        }
                    }

                    @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
                    public boolean cancellationRequested() {
                        this.cancellationRequestedCalled = true;
                        return this.downstream.cancellationRequested();
                    }
                };
            }
        };
    }

    @Override // java.util.stream.Stream
    public final IntStream flatMapToInt(final Function<? super P_OUT, ? extends IntStream> function) {
        Objects.requireNonNull(function);
        return new IntPipeline.StatelessOp<P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) { // from class: java.util.stream.ReferencePipeline.8
            @Override // java.util.stream.AbstractPipeline
            Sink<P_OUT> opWrapSink(int i2, Sink<Integer> sink) {
                return new Sink.ChainedReference<P_OUT, Integer>(sink) { // from class: java.util.stream.ReferencePipeline.8.1
                    boolean cancellationRequestedCalled;
                    IntConsumer downstreamAsInt;

                    {
                        Sink<? super E_OUT> sink2 = this.downstream;
                        sink2.getClass();
                        this.downstreamAsInt = sink2::accept;
                    }

                    @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
                    public void begin(long j2) {
                        this.downstream.begin(-1L);
                    }

                    /* JADX WARN: Type inference failed for: r0v19, types: [java.util.Spliterator$OfInt] */
                    @Override // java.util.function.Consumer
                    public void accept(P_OUT p_out) {
                        IntStream intStream = (IntStream) function.apply(p_out);
                        Throwable th = null;
                        if (intStream != null) {
                            try {
                                try {
                                    if (!this.cancellationRequestedCalled) {
                                        intStream.sequential().forEach(this.downstreamAsInt);
                                    } else {
                                        ?? Spliterator2 = intStream.sequential().spliterator2();
                                        while (!this.downstream.cancellationRequested() && Spliterator2.tryAdvance(this.downstreamAsInt)) {
                                        }
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    throw th2;
                                }
                            } catch (Throwable th3) {
                                if (intStream != null) {
                                    if (th != null) {
                                        try {
                                            intStream.close();
                                        } catch (Throwable th4) {
                                            th.addSuppressed(th4);
                                        }
                                    } else {
                                        intStream.close();
                                    }
                                }
                                throw th3;
                            }
                        }
                        if (intStream != null) {
                            if (0 != 0) {
                                try {
                                    intStream.close();
                                    return;
                                } catch (Throwable th5) {
                                    th.addSuppressed(th5);
                                    return;
                                }
                            }
                            intStream.close();
                        }
                    }

                    @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
                    public boolean cancellationRequested() {
                        this.cancellationRequestedCalled = true;
                        return this.downstream.cancellationRequested();
                    }
                };
            }
        };
    }

    @Override // java.util.stream.Stream
    public final DoubleStream flatMapToDouble(final Function<? super P_OUT, ? extends DoubleStream> function) {
        Objects.requireNonNull(function);
        return new DoublePipeline.StatelessOp<P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) { // from class: java.util.stream.ReferencePipeline.9
            @Override // java.util.stream.AbstractPipeline
            Sink<P_OUT> opWrapSink(int i2, Sink<Double> sink) {
                return new Sink.ChainedReference<P_OUT, Double>(sink) { // from class: java.util.stream.ReferencePipeline.9.1
                    boolean cancellationRequestedCalled;
                    DoubleConsumer downstreamAsDouble;

                    {
                        Sink<? super E_OUT> sink2 = this.downstream;
                        sink2.getClass();
                        this.downstreamAsDouble = sink2::accept;
                    }

                    @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
                    public void begin(long j2) {
                        this.downstream.begin(-1L);
                    }

                    /* JADX WARN: Type inference failed for: r0v19, types: [java.util.Spliterator$OfDouble] */
                    @Override // java.util.function.Consumer
                    public void accept(P_OUT p_out) {
                        DoubleStream doubleStream = (DoubleStream) function.apply(p_out);
                        Throwable th = null;
                        if (doubleStream != null) {
                            try {
                                try {
                                    if (!this.cancellationRequestedCalled) {
                                        doubleStream.sequential().forEach(this.downstreamAsDouble);
                                    } else {
                                        ?? Spliterator2 = doubleStream.sequential().spliterator2();
                                        while (!this.downstream.cancellationRequested() && Spliterator2.tryAdvance(this.downstreamAsDouble)) {
                                        }
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    throw th2;
                                }
                            } catch (Throwable th3) {
                                if (doubleStream != null) {
                                    if (th != null) {
                                        try {
                                            doubleStream.close();
                                        } catch (Throwable th4) {
                                            th.addSuppressed(th4);
                                        }
                                    } else {
                                        doubleStream.close();
                                    }
                                }
                                throw th3;
                            }
                        }
                        if (doubleStream != null) {
                            if (0 != 0) {
                                try {
                                    doubleStream.close();
                                    return;
                                } catch (Throwable th5) {
                                    th.addSuppressed(th5);
                                    return;
                                }
                            }
                            doubleStream.close();
                        }
                    }

                    @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
                    public boolean cancellationRequested() {
                        this.cancellationRequestedCalled = true;
                        return this.downstream.cancellationRequested();
                    }
                };
            }
        };
    }

    @Override // java.util.stream.Stream
    public final LongStream flatMapToLong(final Function<? super P_OUT, ? extends LongStream> function) {
        Objects.requireNonNull(function);
        return new LongPipeline.StatelessOp<P_OUT>(this, StreamShape.REFERENCE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) { // from class: java.util.stream.ReferencePipeline.10
            @Override // java.util.stream.AbstractPipeline
            Sink<P_OUT> opWrapSink(int i2, Sink<Long> sink) {
                return new Sink.ChainedReference<P_OUT, Long>(sink) { // from class: java.util.stream.ReferencePipeline.10.1
                    boolean cancellationRequestedCalled;
                    LongConsumer downstreamAsLong;

                    {
                        Sink<? super E_OUT> sink2 = this.downstream;
                        sink2.getClass();
                        this.downstreamAsLong = sink2::accept;
                    }

                    @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
                    public void begin(long j2) {
                        this.downstream.begin(-1L);
                    }

                    /* JADX WARN: Type inference failed for: r0v19, types: [java.util.Spliterator$OfLong] */
                    @Override // java.util.function.Consumer
                    public void accept(P_OUT p_out) {
                        LongStream longStream = (LongStream) function.apply(p_out);
                        Throwable th = null;
                        if (longStream != null) {
                            try {
                                try {
                                    if (!this.cancellationRequestedCalled) {
                                        longStream.sequential().forEach(this.downstreamAsLong);
                                    } else {
                                        ?? Spliterator2 = longStream.sequential().spliterator2();
                                        while (!this.downstream.cancellationRequested() && Spliterator2.tryAdvance(this.downstreamAsLong)) {
                                        }
                                    }
                                } catch (Throwable th2) {
                                    th = th2;
                                    throw th2;
                                }
                            } catch (Throwable th3) {
                                if (longStream != null) {
                                    if (th != null) {
                                        try {
                                            longStream.close();
                                        } catch (Throwable th4) {
                                            th.addSuppressed(th4);
                                        }
                                    } else {
                                        longStream.close();
                                    }
                                }
                                throw th3;
                            }
                        }
                        if (longStream != null) {
                            if (0 != 0) {
                                try {
                                    longStream.close();
                                    return;
                                } catch (Throwable th5) {
                                    th.addSuppressed(th5);
                                    return;
                                }
                            }
                            longStream.close();
                        }
                    }

                    @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
                    public boolean cancellationRequested() {
                        this.cancellationRequestedCalled = true;
                        return this.downstream.cancellationRequested();
                    }
                };
            }
        };
    }

    @Override // java.util.stream.Stream
    public final Stream<P_OUT> peek(final Consumer<? super P_OUT> consumer) {
        Objects.requireNonNull(consumer);
        return new StatelessOp<P_OUT, P_OUT>(this, StreamShape.REFERENCE, 0) { // from class: java.util.stream.ReferencePipeline.11
            @Override // java.util.stream.AbstractPipeline
            Sink<P_OUT> opWrapSink(int i2, Sink<P_OUT> sink) {
                return new Sink.ChainedReference<P_OUT, P_OUT>(sink) { // from class: java.util.stream.ReferencePipeline.11.1
                    @Override // java.util.function.Consumer
                    public void accept(P_OUT p_out) {
                        consumer.accept(p_out);
                        this.downstream.accept(p_out);
                    }
                };
            }
        };
    }

    @Override // java.util.stream.Stream
    public final Stream<P_OUT> distinct() {
        return DistinctOps.makeRef(this);
    }

    @Override // java.util.stream.Stream
    public final Stream<P_OUT> sorted() {
        return SortedOps.makeRef(this);
    }

    @Override // java.util.stream.Stream
    public final Stream<P_OUT> sorted(Comparator<? super P_OUT> comparator) {
        return SortedOps.makeRef(this, comparator);
    }

    @Override // java.util.stream.Stream
    public final Stream<P_OUT> limit(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException(Long.toString(j2));
        }
        return SliceOps.makeRef(this, 0L, j2);
    }

    @Override // java.util.stream.Stream
    public final Stream<P_OUT> skip(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException(Long.toString(j2));
        }
        if (j2 == 0) {
            return this;
        }
        return SliceOps.makeRef(this, j2, -1L);
    }

    @Override // java.util.stream.Stream
    public void forEach(Consumer<? super P_OUT> consumer) {
        evaluate(ForEachOps.makeRef(consumer, false));
    }

    @Override // java.util.stream.Stream
    public void forEachOrdered(Consumer<? super P_OUT> consumer) {
        evaluate(ForEachOps.makeRef(consumer, true));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.stream.Stream
    public final <A> A[] toArray(IntFunction<A[]> intFunction) {
        return (A[]) Nodes.flatten(evaluateToArrayNode(intFunction), intFunction).asArray(intFunction);
    }

    @Override // java.util.stream.Stream
    public final Object[] toArray() {
        return toArray(i2 -> {
            return new Object[i2];
        });
    }

    @Override // java.util.stream.Stream
    public final boolean anyMatch(Predicate<? super P_OUT> predicate) {
        return ((Boolean) evaluate(MatchOps.makeRef(predicate, MatchOps.MatchKind.ANY))).booleanValue();
    }

    @Override // java.util.stream.Stream
    public final boolean allMatch(Predicate<? super P_OUT> predicate) {
        return ((Boolean) evaluate(MatchOps.makeRef(predicate, MatchOps.MatchKind.ALL))).booleanValue();
    }

    @Override // java.util.stream.Stream
    public final boolean noneMatch(Predicate<? super P_OUT> predicate) {
        return ((Boolean) evaluate(MatchOps.makeRef(predicate, MatchOps.MatchKind.NONE))).booleanValue();
    }

    @Override // java.util.stream.Stream
    public final Optional<P_OUT> findFirst() {
        return (Optional) evaluate(FindOps.makeRef(true));
    }

    @Override // java.util.stream.Stream
    public final Optional<P_OUT> findAny() {
        return (Optional) evaluate(FindOps.makeRef(false));
    }

    @Override // java.util.stream.Stream
    public final P_OUT reduce(P_OUT p_out, BinaryOperator<P_OUT> binaryOperator) {
        return (P_OUT) evaluate(ReduceOps.makeRef(p_out, binaryOperator, binaryOperator));
    }

    @Override // java.util.stream.Stream
    public final Optional<P_OUT> reduce(BinaryOperator<P_OUT> binaryOperator) {
        return (Optional) evaluate(ReduceOps.makeRef(binaryOperator));
    }

    @Override // java.util.stream.Stream
    public final <R> R reduce(R r2, BiFunction<R, ? super P_OUT, R> biFunction, BinaryOperator<R> binaryOperator) {
        return (R) evaluate(ReduceOps.makeRef(r2, (BiFunction<R, ? super T, R>) biFunction, binaryOperator));
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.stream.Stream
    public final <R, A> R collect(Collector<? super P_OUT, A, R> collector) {
        A aEvaluate;
        if (isParallel() && collector.characteristics().contains(Collector.Characteristics.CONCURRENT) && (!isOrdered() || collector.characteristics().contains(Collector.Characteristics.UNORDERED))) {
            aEvaluate = collector.supplier().get();
            BiConsumer<A, ? super P_OUT> biConsumerAccumulator = collector.accumulator();
            forEach(obj -> {
                biConsumerAccumulator.accept(aEvaluate, obj);
            });
        } else {
            aEvaluate = evaluate(ReduceOps.makeRef(collector));
        }
        return collector.characteristics().contains(Collector.Characteristics.IDENTITY_FINISH) ? (R) aEvaluate : collector.finisher().apply(aEvaluate);
    }

    @Override // java.util.stream.Stream
    public final <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super P_OUT> biConsumer, BiConsumer<R, R> biConsumer2) {
        return (R) evaluate(ReduceOps.makeRef(supplier, biConsumer, biConsumer2));
    }

    @Override // java.util.stream.Stream
    public final Optional<P_OUT> max(Comparator<? super P_OUT> comparator) {
        return reduce(BinaryOperator.maxBy(comparator));
    }

    @Override // java.util.stream.Stream
    public final Optional<P_OUT> min(Comparator<? super P_OUT> comparator) {
        return reduce(BinaryOperator.minBy(comparator));
    }

    @Override // java.util.stream.Stream
    public final long count() {
        return mapToLong(obj -> {
            return 1L;
        }).sum();
    }

    /* loaded from: rt.jar:java/util/stream/ReferencePipeline$Head.class */
    static class Head<E_IN, E_OUT> extends ReferencePipeline<E_IN, E_OUT> {
        @Override // java.util.stream.ReferencePipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ BaseStream unordered() {
            return super.unordered();
        }

        Head(Supplier<? extends Spliterator<?>> supplier, int i2, boolean z2) {
            super(supplier, i2, z2);
        }

        Head(Spliterator<?> spliterator, int i2, boolean z2) {
            super(spliterator, i2, z2);
        }

        @Override // java.util.stream.AbstractPipeline
        final boolean opIsStateful() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.stream.AbstractPipeline
        final Sink<E_IN> opWrapSink(int i2, Sink<E_OUT> sink) {
            throw new UnsupportedOperationException();
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.stream.ReferencePipeline, java.util.stream.Stream
        public void forEach(Consumer<? super E_OUT> consumer) {
            if (!isParallel()) {
                sourceStageSpliterator().forEachRemaining(consumer);
            } else {
                super.forEach(consumer);
            }
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.stream.ReferencePipeline, java.util.stream.Stream
        public void forEachOrdered(Consumer<? super E_OUT> consumer) {
            if (!isParallel()) {
                sourceStageSpliterator().forEachRemaining(consumer);
            } else {
                super.forEachOrdered(consumer);
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/ReferencePipeline$StatelessOp.class */
    static abstract class StatelessOp<E_IN, E_OUT> extends ReferencePipeline<E_IN, E_OUT> {
        static final /* synthetic */ boolean $assertionsDisabled;

        @Override // java.util.stream.ReferencePipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ BaseStream unordered() {
            return super.unordered();
        }

        static {
            $assertionsDisabled = !ReferencePipeline.class.desiredAssertionStatus();
        }

        StatelessOp(AbstractPipeline<?, E_IN, ?> abstractPipeline, StreamShape streamShape, int i2) {
            super(abstractPipeline, i2);
            if (!$assertionsDisabled && abstractPipeline.getOutputShape() != streamShape) {
                throw new AssertionError();
            }
        }

        @Override // java.util.stream.AbstractPipeline
        final boolean opIsStateful() {
            return false;
        }
    }

    /* loaded from: rt.jar:java/util/stream/ReferencePipeline$StatefulOp.class */
    static abstract class StatefulOp<E_IN, E_OUT> extends ReferencePipeline<E_IN, E_OUT> {
        static final /* synthetic */ boolean $assertionsDisabled;

        @Override // java.util.stream.AbstractPipeline
        abstract <P_IN> Node<E_OUT> opEvaluateParallel(PipelineHelper<E_OUT> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<E_OUT[]> intFunction);

        @Override // java.util.stream.ReferencePipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ BaseStream unordered() {
            return super.unordered();
        }

        static {
            $assertionsDisabled = !ReferencePipeline.class.desiredAssertionStatus();
        }

        StatefulOp(AbstractPipeline<?, E_IN, ?> abstractPipeline, StreamShape streamShape, int i2) {
            super(abstractPipeline, i2);
            if (!$assertionsDisabled && abstractPipeline.getOutputShape() != streamShape) {
                throw new AssertionError();
            }
        }

        @Override // java.util.stream.AbstractPipeline
        final boolean opIsStateful() {
            return true;
        }
    }
}
