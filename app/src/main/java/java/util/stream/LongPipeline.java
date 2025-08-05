package java.util.stream;

import java.util.Iterator;
import java.util.LongSummaryStatistics;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;
import java.util.stream.DoublePipeline;
import java.util.stream.IntPipeline;
import java.util.stream.MatchOps;
import java.util.stream.Node;
import java.util.stream.ReferencePipeline;
import java.util.stream.Sink;
import java.util.stream.StreamSpliterators;

/* loaded from: rt.jar:java/util/stream/LongPipeline.class */
abstract class LongPipeline<E_IN> extends AbstractPipeline<E_IN, Long, LongStream> implements LongStream {
    @Override // java.util.stream.AbstractPipeline, java.util.stream.BaseStream
    public /* bridge */ /* synthetic */ LongStream parallel() {
        return (LongStream) super.parallel();
    }

    @Override // java.util.stream.AbstractPipeline, java.util.stream.BaseStream
    public /* bridge */ /* synthetic */ LongStream sequential() {
        return (LongStream) super.sequential();
    }

    LongPipeline(Supplier<? extends Spliterator<Long>> supplier, int i2, boolean z2) {
        super(supplier, i2, z2);
    }

    LongPipeline(Spliterator<Long> spliterator, int i2, boolean z2) {
        super(spliterator, i2, z2);
    }

    LongPipeline(AbstractPipeline<?, E_IN, ?> abstractPipeline, int i2) {
        super(abstractPipeline, i2);
    }

    private static LongConsumer adapt(Sink<Long> sink) {
        if (sink instanceof LongConsumer) {
            return (LongConsumer) sink;
        }
        if (Tripwire.ENABLED) {
            Tripwire.trip(AbstractPipeline.class, "using LongStream.adapt(Sink<Long> s)");
        }
        sink.getClass();
        return sink::accept;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Spliterator.OfLong adapt(Spliterator<Long> spliterator) {
        if (spliterator instanceof Spliterator.OfLong) {
            return (Spliterator.OfLong) spliterator;
        }
        if (Tripwire.ENABLED) {
            Tripwire.trip(AbstractPipeline.class, "using LongStream.adapt(Spliterator<Long> s)");
        }
        throw new UnsupportedOperationException("LongStream.adapt(Spliterator<Long> s)");
    }

    @Override // java.util.stream.AbstractPipeline
    final StreamShape getOutputShape() {
        return StreamShape.LONG_VALUE;
    }

    @Override // java.util.stream.AbstractPipeline
    final <P_IN> Node<Long> evaluateToNode(PipelineHelper<Long> pipelineHelper, Spliterator<P_IN> spliterator, boolean z2, IntFunction<Long[]> intFunction) {
        return Nodes.collectLong(pipelineHelper, spliterator, z2);
    }

    @Override // java.util.stream.AbstractPipeline
    final <P_IN> Spliterator<Long> wrap(PipelineHelper<Long> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z2) {
        return new StreamSpliterators.LongWrappingSpliterator(pipelineHelper, supplier, z2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.util.stream.AbstractPipeline
    /* renamed from: lazySpliterator */
    public final Spliterator<Long> lazySpliterator2(Supplier<? extends Spliterator<Long>> supplier) {
        return new StreamSpliterators.DelegatingSpliterator.OfLong(supplier);
    }

    @Override // java.util.stream.AbstractPipeline
    final void forEachWithCancel(Spliterator<Long> spliterator, Sink<Long> sink) {
        Spliterator.OfLong ofLongAdapt = adapt(spliterator);
        LongConsumer longConsumerAdapt = adapt(sink);
        while (!sink.cancellationRequested() && ofLongAdapt.tryAdvance(longConsumerAdapt)) {
        }
    }

    @Override // java.util.stream.AbstractPipeline, java.util.stream.PipelineHelper
    final Node.Builder<Long> makeNodeBuilder(long j2, IntFunction<Long[]> intFunction) {
        return Nodes.longBuilder(j2);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.util.Spliterator$OfLong] */
    @Override // java.util.stream.BaseStream, java.util.stream.DoubleStream
    /* renamed from: iterator */
    public final Iterator<Long> iterator2() {
        return Spliterators.iterator((Spliterator.OfLong) spliterator2());
    }

    @Override // java.util.stream.AbstractPipeline, java.util.stream.BaseStream
    /* renamed from: spliterator */
    public final Spliterator<Long> spliterator2() {
        return adapt((Spliterator<Long>) super.spliterator2());
    }

    @Override // java.util.stream.LongStream
    public final DoubleStream asDoubleStream() {
        return new DoublePipeline.StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) { // from class: java.util.stream.LongPipeline.1
            @Override // java.util.stream.AbstractPipeline
            Sink<Long> opWrapSink(int i2, Sink<Double> sink) {
                return new Sink.ChainedLong<Double>(sink) { // from class: java.util.stream.LongPipeline.1.1
                    @Override // java.util.stream.Sink.OfLong, java.util.function.LongConsumer
                    public void accept(long j2) {
                        this.downstream.accept(j2);
                    }
                };
            }
        };
    }

    @Override // java.util.stream.LongStream
    public final Stream<Long> boxed() {
        return mapToObj(Long::valueOf);
    }

    @Override // java.util.stream.LongStream
    public final LongStream map(final LongUnaryOperator longUnaryOperator) {
        Objects.requireNonNull(longUnaryOperator);
        return new StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) { // from class: java.util.stream.LongPipeline.2
            @Override // java.util.stream.AbstractPipeline
            Sink<Long> opWrapSink(int i2, Sink<Long> sink) {
                return new Sink.ChainedLong<Long>(sink) { // from class: java.util.stream.LongPipeline.2.1
                    @Override // java.util.stream.Sink.OfLong, java.util.function.LongConsumer
                    public void accept(long j2) {
                        this.downstream.accept(longUnaryOperator.applyAsLong(j2));
                    }
                };
            }
        };
    }

    @Override // java.util.stream.LongStream
    public final <U> Stream<U> mapToObj(final LongFunction<? extends U> longFunction) {
        Objects.requireNonNull(longFunction);
        return new ReferencePipeline.StatelessOp<Long, U>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) { // from class: java.util.stream.LongPipeline.3
            @Override // java.util.stream.AbstractPipeline
            Sink<Long> opWrapSink(int i2, Sink<U> sink) {
                return new Sink.ChainedLong<U>(sink) { // from class: java.util.stream.LongPipeline.3.1
                    @Override // java.util.stream.Sink.OfLong, java.util.function.LongConsumer
                    public void accept(long j2) {
                        this.downstream.accept(longFunction.apply(j2));
                    }
                };
            }
        };
    }

    @Override // java.util.stream.LongStream
    public final IntStream mapToInt(final LongToIntFunction longToIntFunction) {
        Objects.requireNonNull(longToIntFunction);
        return new IntPipeline.StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) { // from class: java.util.stream.LongPipeline.4
            @Override // java.util.stream.AbstractPipeline
            Sink<Long> opWrapSink(int i2, Sink<Integer> sink) {
                return new Sink.ChainedLong<Integer>(sink) { // from class: java.util.stream.LongPipeline.4.1
                    @Override // java.util.stream.Sink.OfLong, java.util.function.LongConsumer
                    public void accept(long j2) {
                        this.downstream.accept(longToIntFunction.applyAsInt(j2));
                    }
                };
            }
        };
    }

    @Override // java.util.stream.LongStream
    public final DoubleStream mapToDouble(final LongToDoubleFunction longToDoubleFunction) {
        Objects.requireNonNull(longToDoubleFunction);
        return new DoublePipeline.StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) { // from class: java.util.stream.LongPipeline.5
            @Override // java.util.stream.AbstractPipeline
            Sink<Long> opWrapSink(int i2, Sink<Double> sink) {
                return new Sink.ChainedLong<Double>(sink) { // from class: java.util.stream.LongPipeline.5.1
                    @Override // java.util.stream.Sink.OfLong, java.util.function.LongConsumer
                    public void accept(long j2) {
                        this.downstream.accept(longToDoubleFunction.applyAsDouble(j2));
                    }
                };
            }
        };
    }

    @Override // java.util.stream.LongStream
    public final LongStream flatMap(final LongFunction<? extends LongStream> longFunction) {
        Objects.requireNonNull(longFunction);
        return new StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) { // from class: java.util.stream.LongPipeline.6
            @Override // java.util.stream.AbstractPipeline
            Sink<Long> opWrapSink(int i2, Sink<Long> sink) {
                return new Sink.ChainedLong<Long>(sink) { // from class: java.util.stream.LongPipeline.6.1
                    boolean cancellationRequestedCalled;
                    LongConsumer downstreamAsLong;

                    {
                        Sink<? super E_OUT> sink2 = this.downstream;
                        sink2.getClass();
                        this.downstreamAsLong = sink2::accept;
                    }

                    @Override // java.util.stream.Sink.ChainedLong, java.util.stream.Sink
                    public void begin(long j2) {
                        this.downstream.begin(-1L);
                    }

                    /* JADX WARN: Type inference failed for: r0v19, types: [java.util.Spliterator$OfLong] */
                    @Override // java.util.stream.Sink.OfLong, java.util.function.LongConsumer
                    public void accept(long j2) {
                        LongStream longStream = (LongStream) longFunction.apply(j2);
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

                    @Override // java.util.stream.Sink.ChainedLong, java.util.stream.Sink
                    public boolean cancellationRequested() {
                        this.cancellationRequestedCalled = true;
                        return this.downstream.cancellationRequested();
                    }
                };
            }
        };
    }

    @Override // java.util.stream.BaseStream
    public LongStream unordered() {
        if (!isOrdered()) {
            return this;
        }
        return new StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_ORDERED) { // from class: java.util.stream.LongPipeline.7
            @Override // java.util.stream.AbstractPipeline
            Sink<Long> opWrapSink(int i2, Sink<Long> sink) {
                return sink;
            }
        };
    }

    @Override // java.util.stream.LongStream
    public final LongStream filter(final LongPredicate longPredicate) {
        Objects.requireNonNull(longPredicate);
        return new StatelessOp<Long>(this, StreamShape.LONG_VALUE, StreamOpFlag.NOT_SIZED) { // from class: java.util.stream.LongPipeline.8
            @Override // java.util.stream.AbstractPipeline
            Sink<Long> opWrapSink(int i2, Sink<Long> sink) {
                return new Sink.ChainedLong<Long>(sink) { // from class: java.util.stream.LongPipeline.8.1
                    @Override // java.util.stream.Sink.ChainedLong, java.util.stream.Sink
                    public void begin(long j2) {
                        this.downstream.begin(-1L);
                    }

                    @Override // java.util.stream.Sink.OfLong, java.util.function.LongConsumer
                    public void accept(long j2) {
                        if (longPredicate.test(j2)) {
                            this.downstream.accept(j2);
                        }
                    }
                };
            }
        };
    }

    @Override // java.util.stream.LongStream
    public final LongStream peek(final LongConsumer longConsumer) {
        Objects.requireNonNull(longConsumer);
        return new StatelessOp<Long>(this, StreamShape.LONG_VALUE, 0) { // from class: java.util.stream.LongPipeline.9
            @Override // java.util.stream.AbstractPipeline
            Sink<Long> opWrapSink(int i2, Sink<Long> sink) {
                return new Sink.ChainedLong<Long>(sink) { // from class: java.util.stream.LongPipeline.9.1
                    @Override // java.util.stream.Sink.OfLong, java.util.function.LongConsumer
                    public void accept(long j2) {
                        longConsumer.accept(j2);
                        this.downstream.accept(j2);
                    }
                };
            }
        };
    }

    @Override // java.util.stream.LongStream
    public final LongStream limit(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException(Long.toString(j2));
        }
        return SliceOps.makeLong(this, 0L, j2);
    }

    @Override // java.util.stream.LongStream
    public final LongStream skip(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException(Long.toString(j2));
        }
        if (j2 == 0) {
            return this;
        }
        return SliceOps.makeLong(this, j2, -1L);
    }

    @Override // java.util.stream.LongStream
    public final LongStream sorted() {
        return SortedOps.makeLong(this);
    }

    @Override // java.util.stream.LongStream
    public final LongStream distinct() {
        return boxed().distinct().mapToLong(l2 -> {
            return l2.longValue();
        });
    }

    @Override // java.util.stream.LongStream
    public void forEach(LongConsumer longConsumer) {
        evaluate(ForEachOps.makeLong(longConsumer, false));
    }

    @Override // java.util.stream.LongStream
    public void forEachOrdered(LongConsumer longConsumer) {
        evaluate(ForEachOps.makeLong(longConsumer, true));
    }

    @Override // java.util.stream.LongStream
    public final long sum() {
        return reduce(0L, Long::sum);
    }

    @Override // java.util.stream.LongStream
    public final OptionalLong min() {
        return reduce(Math::min);
    }

    @Override // java.util.stream.LongStream
    public final OptionalLong max() {
        return reduce(Math::max);
    }

    @Override // java.util.stream.LongStream
    public final OptionalDouble average() {
        if (((long[]) collect(() -> {
            return new long[2];
        }, (jArr, j2) -> {
            jArr[0] = jArr[0] + 1;
            jArr[1] = jArr[1] + j2;
        }, (jArr2, jArr3) -> {
            jArr2[0] = jArr2[0] + jArr3[0];
            jArr2[1] = jArr2[1] + jArr3[1];
        }))[0] > 0) {
            return OptionalDouble.of(r0[1] / r0[0]);
        }
        return OptionalDouble.empty();
    }

    @Override // java.util.stream.LongStream
    public final long count() {
        return map(j2 -> {
            return 1L;
        }).sum();
    }

    @Override // java.util.stream.LongStream
    public final LongSummaryStatistics summaryStatistics() {
        return (LongSummaryStatistics) collect(LongSummaryStatistics::new, (v0, v1) -> {
            v0.accept(v1);
        }, (v0, v1) -> {
            v0.combine(v1);
        });
    }

    @Override // java.util.stream.LongStream
    public final long reduce(long j2, LongBinaryOperator longBinaryOperator) {
        return ((Long) evaluate(ReduceOps.makeLong(j2, longBinaryOperator))).longValue();
    }

    @Override // java.util.stream.LongStream
    public final OptionalLong reduce(LongBinaryOperator longBinaryOperator) {
        return (OptionalLong) evaluate(ReduceOps.makeLong(longBinaryOperator));
    }

    @Override // java.util.stream.LongStream
    public final <R> R collect(Supplier<R> supplier, ObjLongConsumer<R> objLongConsumer, BiConsumer<R, R> biConsumer) {
        Objects.requireNonNull(biConsumer);
        return (R) evaluate(ReduceOps.makeLong(supplier, objLongConsumer, (obj, obj2) -> {
            biConsumer.accept(obj, obj2);
            return obj;
        }));
    }

    @Override // java.util.stream.LongStream
    public final boolean anyMatch(LongPredicate longPredicate) {
        return ((Boolean) evaluate(MatchOps.makeLong(longPredicate, MatchOps.MatchKind.ANY))).booleanValue();
    }

    @Override // java.util.stream.LongStream
    public final boolean allMatch(LongPredicate longPredicate) {
        return ((Boolean) evaluate(MatchOps.makeLong(longPredicate, MatchOps.MatchKind.ALL))).booleanValue();
    }

    @Override // java.util.stream.LongStream
    public final boolean noneMatch(LongPredicate longPredicate) {
        return ((Boolean) evaluate(MatchOps.makeLong(longPredicate, MatchOps.MatchKind.NONE))).booleanValue();
    }

    @Override // java.util.stream.LongStream
    public final OptionalLong findFirst() {
        return (OptionalLong) evaluate(FindOps.makeLong(true));
    }

    @Override // java.util.stream.LongStream
    public final OptionalLong findAny() {
        return (OptionalLong) evaluate(FindOps.makeLong(false));
    }

    @Override // java.util.stream.LongStream
    public final long[] toArray() {
        return Nodes.flattenLong((Node.OfLong) evaluateToArrayNode(i2 -> {
            return new Long[i2];
        })).asPrimitiveArray();
    }

    /* loaded from: rt.jar:java/util/stream/LongPipeline$Head.class */
    static class Head<E_IN> extends LongPipeline<E_IN> {
        @Override // java.util.stream.LongPipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ LongStream parallel() {
            return (LongStream) super.parallel();
        }

        @Override // java.util.stream.LongPipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ LongStream sequential() {
            return (LongStream) super.sequential();
        }

        @Override // java.util.stream.LongPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ BaseStream unordered() {
            return super.unordered();
        }

        @Override // java.util.stream.LongPipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        /* renamed from: spliterator */
        public /* bridge */ /* synthetic */ Spliterator<Long> spliterator2() {
            return super.spliterator2();
        }

        @Override // java.util.stream.LongPipeline, java.util.stream.BaseStream, java.util.stream.DoubleStream
        /* renamed from: iterator */
        public /* bridge */ /* synthetic */ Iterator<Long> iterator2() {
            return super.iterator2();
        }

        @Override // java.util.stream.LongPipeline, java.util.stream.AbstractPipeline
        /* renamed from: lazySpliterator */
        /* bridge */ /* synthetic */ Spliterator<Long> lazySpliterator2(Supplier<? extends Spliterator<Long>> supplier) {
            return super.lazySpliterator2(supplier);
        }

        Head(Supplier<? extends Spliterator<Long>> supplier, int i2, boolean z2) {
            super(supplier, i2, z2);
        }

        Head(Spliterator<Long> spliterator, int i2, boolean z2) {
            super(spliterator, i2, z2);
        }

        @Override // java.util.stream.AbstractPipeline
        final boolean opIsStateful() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.stream.AbstractPipeline
        final Sink<E_IN> opWrapSink(int i2, Sink<Long> sink) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.stream.LongPipeline, java.util.stream.LongStream
        public void forEach(LongConsumer longConsumer) {
            if (!isParallel()) {
                LongPipeline.adapt(sourceStageSpliterator()).forEachRemaining(longConsumer);
            } else {
                super.forEach(longConsumer);
            }
        }

        @Override // java.util.stream.LongPipeline, java.util.stream.LongStream
        public void forEachOrdered(LongConsumer longConsumer) {
            if (!isParallel()) {
                LongPipeline.adapt(sourceStageSpliterator()).forEachRemaining(longConsumer);
            } else {
                super.forEachOrdered(longConsumer);
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/LongPipeline$StatelessOp.class */
    static abstract class StatelessOp<E_IN> extends LongPipeline<E_IN> {
        static final /* synthetic */ boolean $assertionsDisabled;

        @Override // java.util.stream.LongPipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ LongStream parallel() {
            return (LongStream) super.parallel();
        }

        @Override // java.util.stream.LongPipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ LongStream sequential() {
            return (LongStream) super.sequential();
        }

        @Override // java.util.stream.LongPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ BaseStream unordered() {
            return super.unordered();
        }

        @Override // java.util.stream.LongPipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        /* renamed from: spliterator */
        public /* bridge */ /* synthetic */ Spliterator<Long> spliterator2() {
            return super.spliterator2();
        }

        @Override // java.util.stream.LongPipeline, java.util.stream.BaseStream, java.util.stream.DoubleStream
        /* renamed from: iterator */
        public /* bridge */ /* synthetic */ Iterator<Long> iterator2() {
            return super.iterator2();
        }

        @Override // java.util.stream.LongPipeline, java.util.stream.AbstractPipeline
        /* renamed from: lazySpliterator */
        /* bridge */ /* synthetic */ Spliterator<Long> lazySpliterator2(Supplier<? extends Spliterator<Long>> supplier) {
            return super.lazySpliterator2(supplier);
        }

        static {
            $assertionsDisabled = !LongPipeline.class.desiredAssertionStatus();
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

    /* loaded from: rt.jar:java/util/stream/LongPipeline$StatefulOp.class */
    static abstract class StatefulOp<E_IN> extends LongPipeline<E_IN> {
        static final /* synthetic */ boolean $assertionsDisabled;

        @Override // java.util.stream.AbstractPipeline
        abstract <P_IN> Node<Long> opEvaluateParallel(PipelineHelper<Long> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<Long[]> intFunction);

        @Override // java.util.stream.LongPipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ LongStream parallel() {
            return (LongStream) super.parallel();
        }

        @Override // java.util.stream.LongPipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ LongStream sequential() {
            return (LongStream) super.sequential();
        }

        @Override // java.util.stream.LongPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ BaseStream unordered() {
            return super.unordered();
        }

        @Override // java.util.stream.LongPipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        /* renamed from: spliterator */
        public /* bridge */ /* synthetic */ Spliterator<Long> spliterator2() {
            return super.spliterator2();
        }

        @Override // java.util.stream.LongPipeline, java.util.stream.BaseStream, java.util.stream.DoubleStream
        /* renamed from: iterator */
        public /* bridge */ /* synthetic */ Iterator<Long> iterator2() {
            return super.iterator2();
        }

        @Override // java.util.stream.LongPipeline, java.util.stream.AbstractPipeline
        /* renamed from: lazySpliterator */
        /* bridge */ /* synthetic */ Spliterator<Long> lazySpliterator2(Supplier<? extends Spliterator<Long>> supplier) {
            return super.lazySpliterator2(supplier);
        }

        static {
            $assertionsDisabled = !LongPipeline.class.desiredAssertionStatus();
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
