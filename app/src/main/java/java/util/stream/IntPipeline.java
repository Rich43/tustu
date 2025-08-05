package java.util.stream;

import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.stream.DoublePipeline;
import java.util.stream.LongPipeline;
import java.util.stream.MatchOps;
import java.util.stream.Node;
import java.util.stream.ReferencePipeline;
import java.util.stream.Sink;
import java.util.stream.StreamSpliterators;

/* loaded from: rt.jar:java/util/stream/IntPipeline.class */
abstract class IntPipeline<E_IN> extends AbstractPipeline<E_IN, Integer, IntStream> implements IntStream {
    @Override // java.util.stream.AbstractPipeline, java.util.stream.BaseStream
    public /* bridge */ /* synthetic */ IntStream parallel() {
        return (IntStream) super.parallel();
    }

    @Override // java.util.stream.AbstractPipeline, java.util.stream.BaseStream
    public /* bridge */ /* synthetic */ IntStream sequential() {
        return (IntStream) super.sequential();
    }

    IntPipeline(Supplier<? extends Spliterator<Integer>> supplier, int i2, boolean z2) {
        super(supplier, i2, z2);
    }

    IntPipeline(Spliterator<Integer> spliterator, int i2, boolean z2) {
        super(spliterator, i2, z2);
    }

    IntPipeline(AbstractPipeline<?, E_IN, ?> abstractPipeline, int i2) {
        super(abstractPipeline, i2);
    }

    private static IntConsumer adapt(Sink<Integer> sink) {
        if (sink instanceof IntConsumer) {
            return (IntConsumer) sink;
        }
        if (Tripwire.ENABLED) {
            Tripwire.trip(AbstractPipeline.class, "using IntStream.adapt(Sink<Integer> s)");
        }
        sink.getClass();
        return sink::accept;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Spliterator.OfInt adapt(Spliterator<Integer> spliterator) {
        if (spliterator instanceof Spliterator.OfInt) {
            return (Spliterator.OfInt) spliterator;
        }
        if (Tripwire.ENABLED) {
            Tripwire.trip(AbstractPipeline.class, "using IntStream.adapt(Spliterator<Integer> s)");
        }
        throw new UnsupportedOperationException("IntStream.adapt(Spliterator<Integer> s)");
    }

    @Override // java.util.stream.AbstractPipeline
    final StreamShape getOutputShape() {
        return StreamShape.INT_VALUE;
    }

    @Override // java.util.stream.AbstractPipeline
    final <P_IN> Node<Integer> evaluateToNode(PipelineHelper<Integer> pipelineHelper, Spliterator<P_IN> spliterator, boolean z2, IntFunction<Integer[]> intFunction) {
        return Nodes.collectInt(pipelineHelper, spliterator, z2);
    }

    @Override // java.util.stream.AbstractPipeline
    final <P_IN> Spliterator<Integer> wrap(PipelineHelper<Integer> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z2) {
        return new StreamSpliterators.IntWrappingSpliterator(pipelineHelper, supplier, z2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.util.stream.AbstractPipeline
    /* renamed from: lazySpliterator */
    public final Spliterator<Integer> lazySpliterator2(Supplier<? extends Spliterator<Integer>> supplier) {
        return new StreamSpliterators.DelegatingSpliterator.OfInt(supplier);
    }

    @Override // java.util.stream.AbstractPipeline
    final void forEachWithCancel(Spliterator<Integer> spliterator, Sink<Integer> sink) {
        Spliterator.OfInt ofIntAdapt = adapt(spliterator);
        IntConsumer intConsumerAdapt = adapt(sink);
        while (!sink.cancellationRequested() && ofIntAdapt.tryAdvance(intConsumerAdapt)) {
        }
    }

    @Override // java.util.stream.AbstractPipeline, java.util.stream.PipelineHelper
    final Node.Builder<Integer> makeNodeBuilder(long j2, IntFunction<Integer[]> intFunction) {
        return Nodes.intBuilder(j2);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.util.Spliterator$OfInt] */
    @Override // java.util.stream.BaseStream, java.util.stream.DoubleStream
    /* renamed from: iterator */
    public final Iterator<Integer> iterator2() {
        return Spliterators.iterator((Spliterator.OfInt) spliterator2());
    }

    @Override // java.util.stream.AbstractPipeline, java.util.stream.BaseStream
    /* renamed from: spliterator */
    public final Spliterator<Integer> spliterator2() {
        return adapt((Spliterator<Integer>) super.spliterator2());
    }

    @Override // java.util.stream.IntStream
    public final LongStream asLongStream() {
        return new LongPipeline.StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) { // from class: java.util.stream.IntPipeline.1
            @Override // java.util.stream.AbstractPipeline
            Sink<Integer> opWrapSink(int i2, Sink<Long> sink) {
                return new Sink.ChainedInt<Long>(sink) { // from class: java.util.stream.IntPipeline.1.1
                    @Override // java.util.stream.Sink.OfInt, java.util.function.IntConsumer
                    public void accept(int i3) {
                        this.downstream.accept(i3);
                    }
                };
            }
        };
    }

    @Override // java.util.stream.IntStream
    public final DoubleStream asDoubleStream() {
        return new DoublePipeline.StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) { // from class: java.util.stream.IntPipeline.2
            @Override // java.util.stream.AbstractPipeline
            Sink<Integer> opWrapSink(int i2, Sink<Double> sink) {
                return new Sink.ChainedInt<Double>(sink) { // from class: java.util.stream.IntPipeline.2.1
                    @Override // java.util.stream.Sink.OfInt, java.util.function.IntConsumer
                    public void accept(int i3) {
                        this.downstream.accept(i3);
                    }
                };
            }
        };
    }

    @Override // java.util.stream.IntStream
    public final Stream<Integer> boxed() {
        return mapToObj(Integer::valueOf);
    }

    @Override // java.util.stream.IntStream
    public final IntStream map(final IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        return new StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) { // from class: java.util.stream.IntPipeline.3
            @Override // java.util.stream.AbstractPipeline
            Sink<Integer> opWrapSink(int i2, Sink<Integer> sink) {
                return new Sink.ChainedInt<Integer>(sink) { // from class: java.util.stream.IntPipeline.3.1
                    @Override // java.util.stream.Sink.OfInt, java.util.function.IntConsumer
                    public void accept(int i3) {
                        this.downstream.accept(intUnaryOperator.applyAsInt(i3));
                    }
                };
            }
        };
    }

    @Override // java.util.stream.IntStream
    public final <U> Stream<U> mapToObj(final IntFunction<? extends U> intFunction) {
        Objects.requireNonNull(intFunction);
        return new ReferencePipeline.StatelessOp<Integer, U>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) { // from class: java.util.stream.IntPipeline.4
            @Override // java.util.stream.AbstractPipeline
            Sink<Integer> opWrapSink(int i2, Sink<U> sink) {
                return new Sink.ChainedInt<U>(sink) { // from class: java.util.stream.IntPipeline.4.1
                    @Override // java.util.stream.Sink.OfInt, java.util.function.IntConsumer
                    public void accept(int i3) {
                        this.downstream.accept(intFunction.apply(i3));
                    }
                };
            }
        };
    }

    @Override // java.util.stream.IntStream
    public final LongStream mapToLong(final IntToLongFunction intToLongFunction) {
        Objects.requireNonNull(intToLongFunction);
        return new LongPipeline.StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) { // from class: java.util.stream.IntPipeline.5
            @Override // java.util.stream.AbstractPipeline
            Sink<Integer> opWrapSink(int i2, Sink<Long> sink) {
                return new Sink.ChainedInt<Long>(sink) { // from class: java.util.stream.IntPipeline.5.1
                    @Override // java.util.stream.Sink.OfInt, java.util.function.IntConsumer
                    public void accept(int i3) {
                        this.downstream.accept(intToLongFunction.applyAsLong(i3));
                    }
                };
            }
        };
    }

    @Override // java.util.stream.IntStream
    public final DoubleStream mapToDouble(final IntToDoubleFunction intToDoubleFunction) {
        Objects.requireNonNull(intToDoubleFunction);
        return new DoublePipeline.StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) { // from class: java.util.stream.IntPipeline.6
            @Override // java.util.stream.AbstractPipeline
            Sink<Integer> opWrapSink(int i2, Sink<Double> sink) {
                return new Sink.ChainedInt<Double>(sink) { // from class: java.util.stream.IntPipeline.6.1
                    @Override // java.util.stream.Sink.OfInt, java.util.function.IntConsumer
                    public void accept(int i3) {
                        this.downstream.accept(intToDoubleFunction.applyAsDouble(i3));
                    }
                };
            }
        };
    }

    @Override // java.util.stream.IntStream
    public final IntStream flatMap(final IntFunction<? extends IntStream> intFunction) {
        Objects.requireNonNull(intFunction);
        return new StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) { // from class: java.util.stream.IntPipeline.7
            @Override // java.util.stream.AbstractPipeline
            Sink<Integer> opWrapSink(int i2, Sink<Integer> sink) {
                return new Sink.ChainedInt<Integer>(sink) { // from class: java.util.stream.IntPipeline.7.1
                    boolean cancellationRequestedCalled;
                    IntConsumer downstreamAsInt;

                    {
                        Sink<? super E_OUT> sink2 = this.downstream;
                        sink2.getClass();
                        this.downstreamAsInt = sink2::accept;
                    }

                    @Override // java.util.stream.Sink.ChainedInt, java.util.stream.Sink
                    public void begin(long j2) {
                        this.downstream.begin(-1L);
                    }

                    /* JADX WARN: Type inference failed for: r0v19, types: [java.util.Spliterator$OfInt] */
                    @Override // java.util.stream.Sink.OfInt, java.util.function.IntConsumer
                    public void accept(int i3) {
                        IntStream intStream = (IntStream) intFunction.apply(i3);
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

                    @Override // java.util.stream.Sink.ChainedInt, java.util.stream.Sink
                    public boolean cancellationRequested() {
                        this.cancellationRequestedCalled = true;
                        return this.downstream.cancellationRequested();
                    }
                };
            }
        };
    }

    @Override // java.util.stream.BaseStream
    public IntStream unordered() {
        if (!isOrdered()) {
            return this;
        }
        return new StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_ORDERED) { // from class: java.util.stream.IntPipeline.8
            @Override // java.util.stream.AbstractPipeline
            Sink<Integer> opWrapSink(int i2, Sink<Integer> sink) {
                return sink;
            }
        };
    }

    @Override // java.util.stream.IntStream
    public final IntStream filter(final IntPredicate intPredicate) {
        Objects.requireNonNull(intPredicate);
        return new StatelessOp<Integer>(this, StreamShape.INT_VALUE, StreamOpFlag.NOT_SIZED) { // from class: java.util.stream.IntPipeline.9
            @Override // java.util.stream.AbstractPipeline
            Sink<Integer> opWrapSink(int i2, Sink<Integer> sink) {
                return new Sink.ChainedInt<Integer>(sink) { // from class: java.util.stream.IntPipeline.9.1
                    @Override // java.util.stream.Sink.ChainedInt, java.util.stream.Sink
                    public void begin(long j2) {
                        this.downstream.begin(-1L);
                    }

                    @Override // java.util.stream.Sink.OfInt, java.util.function.IntConsumer
                    public void accept(int i3) {
                        if (intPredicate.test(i3)) {
                            this.downstream.accept(i3);
                        }
                    }
                };
            }
        };
    }

    @Override // java.util.stream.IntStream
    public final IntStream peek(final IntConsumer intConsumer) {
        Objects.requireNonNull(intConsumer);
        return new StatelessOp<Integer>(this, StreamShape.INT_VALUE, 0) { // from class: java.util.stream.IntPipeline.10
            @Override // java.util.stream.AbstractPipeline
            Sink<Integer> opWrapSink(int i2, Sink<Integer> sink) {
                return new Sink.ChainedInt<Integer>(sink) { // from class: java.util.stream.IntPipeline.10.1
                    @Override // java.util.stream.Sink.OfInt, java.util.function.IntConsumer
                    public void accept(int i3) {
                        intConsumer.accept(i3);
                        this.downstream.accept(i3);
                    }
                };
            }
        };
    }

    @Override // java.util.stream.IntStream
    public final IntStream limit(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException(Long.toString(j2));
        }
        return SliceOps.makeInt(this, 0L, j2);
    }

    @Override // java.util.stream.IntStream
    public final IntStream skip(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException(Long.toString(j2));
        }
        if (j2 == 0) {
            return this;
        }
        return SliceOps.makeInt(this, j2, -1L);
    }

    @Override // java.util.stream.IntStream
    public final IntStream sorted() {
        return SortedOps.makeInt(this);
    }

    @Override // java.util.stream.IntStream
    public final IntStream distinct() {
        return boxed().distinct().mapToInt(num -> {
            return num.intValue();
        });
    }

    @Override // java.util.stream.IntStream
    public void forEach(IntConsumer intConsumer) {
        evaluate(ForEachOps.makeInt(intConsumer, false));
    }

    @Override // java.util.stream.IntStream
    public void forEachOrdered(IntConsumer intConsumer) {
        evaluate(ForEachOps.makeInt(intConsumer, true));
    }

    @Override // java.util.stream.IntStream
    public final int sum() {
        return reduce(0, Integer::sum);
    }

    @Override // java.util.stream.IntStream
    public final OptionalInt min() {
        return reduce(Math::min);
    }

    @Override // java.util.stream.IntStream
    public final OptionalInt max() {
        return reduce(Math::max);
    }

    @Override // java.util.stream.IntStream
    public final long count() {
        return mapToLong(i2 -> {
            return 1L;
        }).sum();
    }

    @Override // java.util.stream.IntStream
    public final OptionalDouble average() {
        if (((long[]) collect(() -> {
            return new long[2];
        }, (jArr, i2) -> {
            jArr[0] = jArr[0] + 1;
            jArr[1] = jArr[1] + i2;
        }, (jArr2, jArr3) -> {
            jArr2[0] = jArr2[0] + jArr3[0];
            jArr2[1] = jArr2[1] + jArr3[1];
        }))[0] > 0) {
            return OptionalDouble.of(r0[1] / r0[0]);
        }
        return OptionalDouble.empty();
    }

    @Override // java.util.stream.IntStream
    public final IntSummaryStatistics summaryStatistics() {
        return (IntSummaryStatistics) collect(IntSummaryStatistics::new, (v0, v1) -> {
            v0.accept(v1);
        }, (v0, v1) -> {
            v0.combine(v1);
        });
    }

    @Override // java.util.stream.IntStream
    public final int reduce(int i2, IntBinaryOperator intBinaryOperator) {
        return ((Integer) evaluate(ReduceOps.makeInt(i2, intBinaryOperator))).intValue();
    }

    @Override // java.util.stream.IntStream
    public final OptionalInt reduce(IntBinaryOperator intBinaryOperator) {
        return (OptionalInt) evaluate(ReduceOps.makeInt(intBinaryOperator));
    }

    @Override // java.util.stream.IntStream
    public final <R> R collect(Supplier<R> supplier, ObjIntConsumer<R> objIntConsumer, BiConsumer<R, R> biConsumer) {
        Objects.requireNonNull(biConsumer);
        return (R) evaluate(ReduceOps.makeInt(supplier, objIntConsumer, (obj, obj2) -> {
            biConsumer.accept(obj, obj2);
            return obj;
        }));
    }

    @Override // java.util.stream.IntStream
    public final boolean anyMatch(IntPredicate intPredicate) {
        return ((Boolean) evaluate(MatchOps.makeInt(intPredicate, MatchOps.MatchKind.ANY))).booleanValue();
    }

    @Override // java.util.stream.IntStream
    public final boolean allMatch(IntPredicate intPredicate) {
        return ((Boolean) evaluate(MatchOps.makeInt(intPredicate, MatchOps.MatchKind.ALL))).booleanValue();
    }

    @Override // java.util.stream.IntStream
    public final boolean noneMatch(IntPredicate intPredicate) {
        return ((Boolean) evaluate(MatchOps.makeInt(intPredicate, MatchOps.MatchKind.NONE))).booleanValue();
    }

    @Override // java.util.stream.IntStream
    public final OptionalInt findFirst() {
        return (OptionalInt) evaluate(FindOps.makeInt(true));
    }

    @Override // java.util.stream.IntStream
    public final OptionalInt findAny() {
        return (OptionalInt) evaluate(FindOps.makeInt(false));
    }

    @Override // java.util.stream.IntStream
    public final int[] toArray() {
        return Nodes.flattenInt((Node.OfInt) evaluateToArrayNode(i2 -> {
            return new Integer[i2];
        })).asPrimitiveArray();
    }

    /* loaded from: rt.jar:java/util/stream/IntPipeline$Head.class */
    static class Head<E_IN> extends IntPipeline<E_IN> {
        @Override // java.util.stream.IntPipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ IntStream parallel() {
            return (IntStream) super.parallel();
        }

        @Override // java.util.stream.IntPipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ IntStream sequential() {
            return (IntStream) super.sequential();
        }

        @Override // java.util.stream.IntPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ BaseStream unordered() {
            return super.unordered();
        }

        @Override // java.util.stream.IntPipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        /* renamed from: spliterator */
        public /* bridge */ /* synthetic */ Spliterator<Integer> spliterator2() {
            return super.spliterator2();
        }

        @Override // java.util.stream.IntPipeline, java.util.stream.BaseStream, java.util.stream.DoubleStream
        /* renamed from: iterator */
        public /* bridge */ /* synthetic */ Iterator<Integer> iterator2() {
            return super.iterator2();
        }

        @Override // java.util.stream.IntPipeline, java.util.stream.AbstractPipeline
        /* renamed from: lazySpliterator */
        /* bridge */ /* synthetic */ Spliterator<Integer> lazySpliterator2(Supplier<? extends Spliterator<Integer>> supplier) {
            return super.lazySpliterator2(supplier);
        }

        Head(Supplier<? extends Spliterator<Integer>> supplier, int i2, boolean z2) {
            super(supplier, i2, z2);
        }

        Head(Spliterator<Integer> spliterator, int i2, boolean z2) {
            super(spliterator, i2, z2);
        }

        @Override // java.util.stream.AbstractPipeline
        final boolean opIsStateful() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.stream.AbstractPipeline
        final Sink<E_IN> opWrapSink(int i2, Sink<Integer> sink) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.stream.IntPipeline, java.util.stream.IntStream
        public void forEach(IntConsumer intConsumer) {
            if (!isParallel()) {
                IntPipeline.adapt(sourceStageSpliterator()).forEachRemaining(intConsumer);
            } else {
                super.forEach(intConsumer);
            }
        }

        @Override // java.util.stream.IntPipeline, java.util.stream.IntStream
        public void forEachOrdered(IntConsumer intConsumer) {
            if (!isParallel()) {
                IntPipeline.adapt(sourceStageSpliterator()).forEachRemaining(intConsumer);
            } else {
                super.forEachOrdered(intConsumer);
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/IntPipeline$StatelessOp.class */
    static abstract class StatelessOp<E_IN> extends IntPipeline<E_IN> {
        static final /* synthetic */ boolean $assertionsDisabled;

        @Override // java.util.stream.IntPipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ IntStream parallel() {
            return (IntStream) super.parallel();
        }

        @Override // java.util.stream.IntPipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ IntStream sequential() {
            return (IntStream) super.sequential();
        }

        @Override // java.util.stream.IntPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ BaseStream unordered() {
            return super.unordered();
        }

        @Override // java.util.stream.IntPipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        /* renamed from: spliterator */
        public /* bridge */ /* synthetic */ Spliterator<Integer> spliterator2() {
            return super.spliterator2();
        }

        @Override // java.util.stream.IntPipeline, java.util.stream.BaseStream, java.util.stream.DoubleStream
        /* renamed from: iterator */
        public /* bridge */ /* synthetic */ Iterator<Integer> iterator2() {
            return super.iterator2();
        }

        @Override // java.util.stream.IntPipeline, java.util.stream.AbstractPipeline
        /* renamed from: lazySpliterator */
        /* bridge */ /* synthetic */ Spliterator<Integer> lazySpliterator2(Supplier<? extends Spliterator<Integer>> supplier) {
            return super.lazySpliterator2(supplier);
        }

        static {
            $assertionsDisabled = !IntPipeline.class.desiredAssertionStatus();
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

    /* loaded from: rt.jar:java/util/stream/IntPipeline$StatefulOp.class */
    static abstract class StatefulOp<E_IN> extends IntPipeline<E_IN> {
        static final /* synthetic */ boolean $assertionsDisabled;

        @Override // java.util.stream.AbstractPipeline
        abstract <P_IN> Node<Integer> opEvaluateParallel(PipelineHelper<Integer> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<Integer[]> intFunction);

        @Override // java.util.stream.IntPipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ IntStream parallel() {
            return (IntStream) super.parallel();
        }

        @Override // java.util.stream.IntPipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ IntStream sequential() {
            return (IntStream) super.sequential();
        }

        @Override // java.util.stream.IntPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ BaseStream unordered() {
            return super.unordered();
        }

        @Override // java.util.stream.IntPipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        /* renamed from: spliterator */
        public /* bridge */ /* synthetic */ Spliterator<Integer> spliterator2() {
            return super.spliterator2();
        }

        @Override // java.util.stream.IntPipeline, java.util.stream.BaseStream, java.util.stream.DoubleStream
        /* renamed from: iterator */
        public /* bridge */ /* synthetic */ Iterator<Integer> iterator2() {
            return super.iterator2();
        }

        @Override // java.util.stream.IntPipeline, java.util.stream.AbstractPipeline
        /* renamed from: lazySpliterator */
        /* bridge */ /* synthetic */ Spliterator<Integer> lazySpliterator2(Supplier<? extends Spliterator<Integer>> supplier) {
            return super.lazySpliterator2(supplier);
        }

        static {
            $assertionsDisabled = !IntPipeline.class.desiredAssertionStatus();
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
