package java.util.stream;

import java.util.DoubleSummaryStatistics;
import java.util.Iterator;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.IntFunction;
import java.util.function.ObjDoubleConsumer;
import java.util.function.Supplier;
import java.util.stream.IntPipeline;
import java.util.stream.LongPipeline;
import java.util.stream.MatchOps;
import java.util.stream.Node;
import java.util.stream.ReferencePipeline;
import java.util.stream.Sink;
import java.util.stream.StreamSpliterators;

/* loaded from: rt.jar:java/util/stream/DoublePipeline.class */
abstract class DoublePipeline<E_IN> extends AbstractPipeline<E_IN, Double, DoubleStream> implements DoubleStream {
    @Override // java.util.stream.AbstractPipeline, java.util.stream.BaseStream
    public /* bridge */ /* synthetic */ DoubleStream parallel() {
        return (DoubleStream) super.parallel();
    }

    @Override // java.util.stream.AbstractPipeline, java.util.stream.BaseStream
    public /* bridge */ /* synthetic */ DoubleStream sequential() {
        return (DoubleStream) super.sequential();
    }

    DoublePipeline(Supplier<? extends Spliterator<Double>> supplier, int i2, boolean z2) {
        super(supplier, i2, z2);
    }

    DoublePipeline(Spliterator<Double> spliterator, int i2, boolean z2) {
        super(spliterator, i2, z2);
    }

    DoublePipeline(AbstractPipeline<?, E_IN, ?> abstractPipeline, int i2) {
        super(abstractPipeline, i2);
    }

    private static DoubleConsumer adapt(Sink<Double> sink) {
        if (sink instanceof DoubleConsumer) {
            return (DoubleConsumer) sink;
        }
        if (Tripwire.ENABLED) {
            Tripwire.trip(AbstractPipeline.class, "using DoubleStream.adapt(Sink<Double> s)");
        }
        sink.getClass();
        return sink::accept;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Spliterator.OfDouble adapt(Spliterator<Double> spliterator) {
        if (spliterator instanceof Spliterator.OfDouble) {
            return (Spliterator.OfDouble) spliterator;
        }
        if (Tripwire.ENABLED) {
            Tripwire.trip(AbstractPipeline.class, "using DoubleStream.adapt(Spliterator<Double> s)");
        }
        throw new UnsupportedOperationException("DoubleStream.adapt(Spliterator<Double> s)");
    }

    @Override // java.util.stream.AbstractPipeline
    final StreamShape getOutputShape() {
        return StreamShape.DOUBLE_VALUE;
    }

    @Override // java.util.stream.AbstractPipeline
    final <P_IN> Node<Double> evaluateToNode(PipelineHelper<Double> pipelineHelper, Spliterator<P_IN> spliterator, boolean z2, IntFunction<Double[]> intFunction) {
        return Nodes.collectDouble(pipelineHelper, spliterator, z2);
    }

    @Override // java.util.stream.AbstractPipeline
    final <P_IN> Spliterator<Double> wrap(PipelineHelper<Double> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z2) {
        return new StreamSpliterators.DoubleWrappingSpliterator(pipelineHelper, supplier, z2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // java.util.stream.AbstractPipeline
    /* renamed from: lazySpliterator, reason: merged with bridge method [inline-methods] */
    public final Spliterator<Double> lazySpliterator2(Supplier<? extends Spliterator<Double>> supplier) {
        return new StreamSpliterators.DelegatingSpliterator.OfDouble(supplier);
    }

    @Override // java.util.stream.AbstractPipeline
    final void forEachWithCancel(Spliterator<Double> spliterator, Sink<Double> sink) {
        Spliterator.OfDouble ofDoubleAdapt = adapt(spliterator);
        DoubleConsumer doubleConsumerAdapt = adapt(sink);
        while (!sink.cancellationRequested() && ofDoubleAdapt.tryAdvance(doubleConsumerAdapt)) {
        }
    }

    @Override // java.util.stream.AbstractPipeline, java.util.stream.PipelineHelper
    final Node.Builder<Double> makeNodeBuilder(long j2, IntFunction<Double[]> intFunction) {
        return Nodes.doubleBuilder(j2);
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [java.util.Spliterator$OfDouble] */
    @Override // java.util.stream.BaseStream, java.util.stream.DoubleStream
    /* renamed from: iterator */
    public final Iterator<Double> iterator2() {
        return Spliterators.iterator((Spliterator.OfDouble) spliterator2());
    }

    @Override // java.util.stream.AbstractPipeline, java.util.stream.BaseStream
    /* renamed from: spliterator */
    public final Spliterator<Double> spliterator2() {
        return adapt((Spliterator<Double>) super.spliterator2());
    }

    @Override // java.util.stream.DoubleStream
    public final Stream<Double> boxed() {
        return mapToObj(Double::valueOf);
    }

    @Override // java.util.stream.DoubleStream
    public final DoubleStream map(final DoubleUnaryOperator doubleUnaryOperator) {
        Objects.requireNonNull(doubleUnaryOperator);
        return new StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) { // from class: java.util.stream.DoublePipeline.1
            @Override // java.util.stream.AbstractPipeline
            Sink<Double> opWrapSink(int i2, Sink<Double> sink) {
                return new Sink.ChainedDouble<Double>(sink) { // from class: java.util.stream.DoublePipeline.1.1
                    @Override // java.util.stream.Sink.OfDouble, java.util.stream.Sink, java.util.function.DoubleConsumer
                    public void accept(double d2) {
                        this.downstream.accept(doubleUnaryOperator.applyAsDouble(d2));
                    }
                };
            }
        };
    }

    @Override // java.util.stream.DoubleStream
    public final <U> Stream<U> mapToObj(final DoubleFunction<? extends U> doubleFunction) {
        Objects.requireNonNull(doubleFunction);
        return new ReferencePipeline.StatelessOp<Double, U>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) { // from class: java.util.stream.DoublePipeline.2
            @Override // java.util.stream.AbstractPipeline
            Sink<Double> opWrapSink(int i2, Sink<U> sink) {
                return new Sink.ChainedDouble<U>(sink) { // from class: java.util.stream.DoublePipeline.2.1
                    @Override // java.util.stream.Sink.OfDouble, java.util.stream.Sink, java.util.function.DoubleConsumer
                    public void accept(double d2) {
                        this.downstream.accept(doubleFunction.apply(d2));
                    }
                };
            }
        };
    }

    @Override // java.util.stream.DoubleStream
    public final IntStream mapToInt(final DoubleToIntFunction doubleToIntFunction) {
        Objects.requireNonNull(doubleToIntFunction);
        return new IntPipeline.StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) { // from class: java.util.stream.DoublePipeline.3
            @Override // java.util.stream.AbstractPipeline
            Sink<Double> opWrapSink(int i2, Sink<Integer> sink) {
                return new Sink.ChainedDouble<Integer>(sink) { // from class: java.util.stream.DoublePipeline.3.1
                    @Override // java.util.stream.Sink.OfDouble, java.util.stream.Sink, java.util.function.DoubleConsumer
                    public void accept(double d2) {
                        this.downstream.accept(doubleToIntFunction.applyAsInt(d2));
                    }
                };
            }
        };
    }

    @Override // java.util.stream.DoubleStream
    public final LongStream mapToLong(final DoubleToLongFunction doubleToLongFunction) {
        Objects.requireNonNull(doubleToLongFunction);
        return new LongPipeline.StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT) { // from class: java.util.stream.DoublePipeline.4
            @Override // java.util.stream.AbstractPipeline
            Sink<Double> opWrapSink(int i2, Sink<Long> sink) {
                return new Sink.ChainedDouble<Long>(sink) { // from class: java.util.stream.DoublePipeline.4.1
                    @Override // java.util.stream.Sink.OfDouble, java.util.stream.Sink, java.util.function.DoubleConsumer
                    public void accept(double d2) {
                        this.downstream.accept(doubleToLongFunction.applyAsLong(d2));
                    }
                };
            }
        };
    }

    @Override // java.util.stream.DoubleStream
    public final DoubleStream flatMap(final DoubleFunction<? extends DoubleStream> doubleFunction) {
        Objects.requireNonNull(doubleFunction);
        return new StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_SORTED | StreamOpFlag.NOT_DISTINCT | StreamOpFlag.NOT_SIZED) { // from class: java.util.stream.DoublePipeline.5
            @Override // java.util.stream.AbstractPipeline
            Sink<Double> opWrapSink(int i2, Sink<Double> sink) {
                return new Sink.ChainedDouble<Double>(sink) { // from class: java.util.stream.DoublePipeline.5.1
                    boolean cancellationRequestedCalled;
                    DoubleConsumer downstreamAsDouble;

                    {
                        Sink<? super E_OUT> sink2 = this.downstream;
                        sink2.getClass();
                        this.downstreamAsDouble = sink2::accept;
                    }

                    @Override // java.util.stream.Sink.ChainedDouble, java.util.stream.Sink
                    public void begin(long j2) {
                        this.downstream.begin(-1L);
                    }

                    /* JADX WARN: Type inference failed for: r0v19, types: [java.util.Spliterator$OfDouble] */
                    @Override // java.util.stream.Sink.OfDouble, java.util.stream.Sink, java.util.function.DoubleConsumer
                    public void accept(double d2) {
                        DoubleStream doubleStream = (DoubleStream) doubleFunction.apply(d2);
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

                    @Override // java.util.stream.Sink.ChainedDouble, java.util.stream.Sink
                    public boolean cancellationRequested() {
                        this.cancellationRequestedCalled = true;
                        return this.downstream.cancellationRequested();
                    }
                };
            }
        };
    }

    @Override // java.util.stream.BaseStream
    public DoubleStream unordered() {
        if (!isOrdered()) {
            return this;
        }
        return new StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_ORDERED) { // from class: java.util.stream.DoublePipeline.6
            @Override // java.util.stream.AbstractPipeline
            Sink<Double> opWrapSink(int i2, Sink<Double> sink) {
                return sink;
            }
        };
    }

    @Override // java.util.stream.DoubleStream
    public final DoubleStream filter(final DoublePredicate doublePredicate) {
        Objects.requireNonNull(doublePredicate);
        return new StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, StreamOpFlag.NOT_SIZED) { // from class: java.util.stream.DoublePipeline.7
            @Override // java.util.stream.AbstractPipeline
            Sink<Double> opWrapSink(int i2, Sink<Double> sink) {
                return new Sink.ChainedDouble<Double>(sink) { // from class: java.util.stream.DoublePipeline.7.1
                    @Override // java.util.stream.Sink.ChainedDouble, java.util.stream.Sink
                    public void begin(long j2) {
                        this.downstream.begin(-1L);
                    }

                    @Override // java.util.stream.Sink.OfDouble, java.util.stream.Sink, java.util.function.DoubleConsumer
                    public void accept(double d2) {
                        if (doublePredicate.test(d2)) {
                            this.downstream.accept(d2);
                        }
                    }
                };
            }
        };
    }

    @Override // java.util.stream.DoubleStream
    public final DoubleStream peek(final DoubleConsumer doubleConsumer) {
        Objects.requireNonNull(doubleConsumer);
        return new StatelessOp<Double>(this, StreamShape.DOUBLE_VALUE, 0) { // from class: java.util.stream.DoublePipeline.8
            @Override // java.util.stream.AbstractPipeline
            Sink<Double> opWrapSink(int i2, Sink<Double> sink) {
                return new Sink.ChainedDouble<Double>(sink) { // from class: java.util.stream.DoublePipeline.8.1
                    @Override // java.util.stream.Sink.OfDouble, java.util.stream.Sink, java.util.function.DoubleConsumer
                    public void accept(double d2) {
                        doubleConsumer.accept(d2);
                        this.downstream.accept(d2);
                    }
                };
            }
        };
    }

    @Override // java.util.stream.DoubleStream
    public final DoubleStream limit(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException(Long.toString(j2));
        }
        return SliceOps.makeDouble(this, 0L, j2);
    }

    @Override // java.util.stream.DoubleStream
    public final DoubleStream skip(long j2) {
        if (j2 < 0) {
            throw new IllegalArgumentException(Long.toString(j2));
        }
        if (j2 == 0) {
            return this;
        }
        return SliceOps.makeDouble(this, j2, -1L);
    }

    @Override // java.util.stream.DoubleStream
    public final DoubleStream sorted() {
        return SortedOps.makeDouble(this);
    }

    @Override // java.util.stream.DoubleStream
    public final DoubleStream distinct() {
        return boxed().distinct().mapToDouble(d2 -> {
            return d2.doubleValue();
        });
    }

    @Override // java.util.stream.DoubleStream
    public void forEach(DoubleConsumer doubleConsumer) {
        evaluate(ForEachOps.makeDouble(doubleConsumer, false));
    }

    @Override // java.util.stream.DoubleStream
    public void forEachOrdered(DoubleConsumer doubleConsumer) {
        evaluate(ForEachOps.makeDouble(doubleConsumer, true));
    }

    @Override // java.util.stream.DoubleStream
    public final double sum() {
        return Collectors.computeFinalSum((double[]) collect(() -> {
            return new double[3];
        }, (dArr, d2) -> {
            Collectors.sumWithCompensation(dArr, d2);
            dArr[2] = dArr[2] + d2;
        }, (dArr2, dArr3) -> {
            Collectors.sumWithCompensation(dArr2, dArr3[0]);
            Collectors.sumWithCompensation(dArr2, dArr3[1]);
            dArr2[2] = dArr2[2] + dArr3[2];
        }));
    }

    @Override // java.util.stream.DoubleStream
    public final OptionalDouble min() {
        return reduce(Math::min);
    }

    @Override // java.util.stream.DoubleStream
    public final OptionalDouble max() {
        return reduce(Math::max);
    }

    @Override // java.util.stream.DoubleStream
    public final OptionalDouble average() {
        double[] dArr = (double[]) collect(() -> {
            return new double[4];
        }, (dArr2, d2) -> {
            dArr2[2] = dArr2[2] + 1.0d;
            Collectors.sumWithCompensation(dArr2, d2);
            dArr2[3] = dArr2[3] + d2;
        }, (dArr3, dArr4) -> {
            Collectors.sumWithCompensation(dArr3, dArr4[0]);
            Collectors.sumWithCompensation(dArr3, dArr4[1]);
            dArr3[2] = dArr3[2] + dArr4[2];
            dArr3[3] = dArr3[3] + dArr4[3];
        });
        if (dArr[2] > 0.0d) {
            return OptionalDouble.of(Collectors.computeFinalSum(dArr) / dArr[2]);
        }
        return OptionalDouble.empty();
    }

    @Override // java.util.stream.DoubleStream
    public final long count() {
        return mapToLong(d2 -> {
            return 1L;
        }).sum();
    }

    @Override // java.util.stream.DoubleStream
    public final DoubleSummaryStatistics summaryStatistics() {
        return (DoubleSummaryStatistics) collect(DoubleSummaryStatistics::new, (v0, v1) -> {
            v0.accept(v1);
        }, (v0, v1) -> {
            v0.combine(v1);
        });
    }

    @Override // java.util.stream.DoubleStream
    public final double reduce(double d2, DoubleBinaryOperator doubleBinaryOperator) {
        return ((Double) evaluate(ReduceOps.makeDouble(d2, doubleBinaryOperator))).doubleValue();
    }

    @Override // java.util.stream.DoubleStream
    public final OptionalDouble reduce(DoubleBinaryOperator doubleBinaryOperator) {
        return (OptionalDouble) evaluate(ReduceOps.makeDouble(doubleBinaryOperator));
    }

    @Override // java.util.stream.DoubleStream
    public final <R> R collect(Supplier<R> supplier, ObjDoubleConsumer<R> objDoubleConsumer, BiConsumer<R, R> biConsumer) {
        Objects.requireNonNull(biConsumer);
        return (R) evaluate(ReduceOps.makeDouble(supplier, objDoubleConsumer, (obj, obj2) -> {
            biConsumer.accept(obj, obj2);
            return obj;
        }));
    }

    @Override // java.util.stream.DoubleStream
    public final boolean anyMatch(DoublePredicate doublePredicate) {
        return ((Boolean) evaluate(MatchOps.makeDouble(doublePredicate, MatchOps.MatchKind.ANY))).booleanValue();
    }

    @Override // java.util.stream.DoubleStream
    public final boolean allMatch(DoublePredicate doublePredicate) {
        return ((Boolean) evaluate(MatchOps.makeDouble(doublePredicate, MatchOps.MatchKind.ALL))).booleanValue();
    }

    @Override // java.util.stream.DoubleStream
    public final boolean noneMatch(DoublePredicate doublePredicate) {
        return ((Boolean) evaluate(MatchOps.makeDouble(doublePredicate, MatchOps.MatchKind.NONE))).booleanValue();
    }

    @Override // java.util.stream.DoubleStream
    public final OptionalDouble findFirst() {
        return (OptionalDouble) evaluate(FindOps.makeDouble(true));
    }

    @Override // java.util.stream.DoubleStream
    public final OptionalDouble findAny() {
        return (OptionalDouble) evaluate(FindOps.makeDouble(false));
    }

    @Override // java.util.stream.DoubleStream
    public final double[] toArray() {
        return Nodes.flattenDouble((Node.OfDouble) evaluateToArrayNode(i2 -> {
            return new Double[i2];
        })).asPrimitiveArray();
    }

    /* loaded from: rt.jar:java/util/stream/DoublePipeline$Head.class */
    static class Head<E_IN> extends DoublePipeline<E_IN> {
        @Override // java.util.stream.DoublePipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ DoubleStream parallel() {
            return (DoubleStream) super.parallel();
        }

        @Override // java.util.stream.DoublePipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ DoubleStream sequential() {
            return (DoubleStream) super.sequential();
        }

        @Override // java.util.stream.DoublePipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ BaseStream unordered() {
            return super.unordered();
        }

        @Override // java.util.stream.DoublePipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        /* renamed from: spliterator */
        public /* bridge */ /* synthetic */ Spliterator<Double> spliterator2() {
            return super.spliterator2();
        }

        @Override // java.util.stream.DoublePipeline, java.util.stream.BaseStream, java.util.stream.DoubleStream
        /* renamed from: iterator */
        public /* bridge */ /* synthetic */ Iterator<Double> iterator2() {
            return super.iterator2();
        }

        @Override // java.util.stream.DoublePipeline, java.util.stream.AbstractPipeline
        /* renamed from: lazySpliterator */
        /* bridge */ /* synthetic */ Spliterator<Double> lazySpliterator2(Supplier<? extends Spliterator<Double>> supplier) {
            return super.lazySpliterator2(supplier);
        }

        Head(Supplier<? extends Spliterator<Double>> supplier, int i2, boolean z2) {
            super(supplier, i2, z2);
        }

        Head(Spliterator<Double> spliterator, int i2, boolean z2) {
            super(spliterator, i2, z2);
        }

        @Override // java.util.stream.AbstractPipeline
        final boolean opIsStateful() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.stream.AbstractPipeline
        final Sink<E_IN> opWrapSink(int i2, Sink<Double> sink) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.stream.DoublePipeline, java.util.stream.DoubleStream
        public void forEach(DoubleConsumer doubleConsumer) {
            if (!isParallel()) {
                DoublePipeline.adapt(sourceStageSpliterator()).forEachRemaining(doubleConsumer);
            } else {
                super.forEach(doubleConsumer);
            }
        }

        @Override // java.util.stream.DoublePipeline, java.util.stream.DoubleStream
        public void forEachOrdered(DoubleConsumer doubleConsumer) {
            if (!isParallel()) {
                DoublePipeline.adapt(sourceStageSpliterator()).forEachRemaining(doubleConsumer);
            } else {
                super.forEachOrdered(doubleConsumer);
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/DoublePipeline$StatelessOp.class */
    static abstract class StatelessOp<E_IN> extends DoublePipeline<E_IN> {
        static final /* synthetic */ boolean $assertionsDisabled;

        @Override // java.util.stream.DoublePipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ DoubleStream parallel() {
            return (DoubleStream) super.parallel();
        }

        @Override // java.util.stream.DoublePipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ DoubleStream sequential() {
            return (DoubleStream) super.sequential();
        }

        @Override // java.util.stream.DoublePipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ BaseStream unordered() {
            return super.unordered();
        }

        @Override // java.util.stream.DoublePipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        /* renamed from: spliterator */
        public /* bridge */ /* synthetic */ Spliterator<Double> spliterator2() {
            return super.spliterator2();
        }

        @Override // java.util.stream.DoublePipeline, java.util.stream.BaseStream, java.util.stream.DoubleStream
        /* renamed from: iterator */
        public /* bridge */ /* synthetic */ Iterator<Double> iterator2() {
            return super.iterator2();
        }

        @Override // java.util.stream.DoublePipeline, java.util.stream.AbstractPipeline
        /* renamed from: lazySpliterator */
        /* bridge */ /* synthetic */ Spliterator<Double> lazySpliterator2(Supplier<? extends Spliterator<Double>> supplier) {
            return super.lazySpliterator2(supplier);
        }

        static {
            $assertionsDisabled = !DoublePipeline.class.desiredAssertionStatus();
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

    /* loaded from: rt.jar:java/util/stream/DoublePipeline$StatefulOp.class */
    static abstract class StatefulOp<E_IN> extends DoublePipeline<E_IN> {
        static final /* synthetic */ boolean $assertionsDisabled;

        @Override // java.util.stream.AbstractPipeline
        abstract <P_IN> Node<Double> opEvaluateParallel(PipelineHelper<Double> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<Double[]> intFunction);

        @Override // java.util.stream.DoublePipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ DoubleStream parallel() {
            return (DoubleStream) super.parallel();
        }

        @Override // java.util.stream.DoublePipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ DoubleStream sequential() {
            return (DoubleStream) super.sequential();
        }

        @Override // java.util.stream.DoublePipeline, java.util.stream.BaseStream
        public /* bridge */ /* synthetic */ BaseStream unordered() {
            return super.unordered();
        }

        @Override // java.util.stream.DoublePipeline, java.util.stream.AbstractPipeline, java.util.stream.BaseStream
        /* renamed from: spliterator */
        public /* bridge */ /* synthetic */ Spliterator<Double> spliterator2() {
            return super.spliterator2();
        }

        @Override // java.util.stream.DoublePipeline, java.util.stream.BaseStream, java.util.stream.DoubleStream
        /* renamed from: iterator */
        public /* bridge */ /* synthetic */ Iterator<Double> iterator2() {
            return super.iterator2();
        }

        @Override // java.util.stream.DoublePipeline, java.util.stream.AbstractPipeline
        /* renamed from: lazySpliterator */
        /* bridge */ /* synthetic */ Spliterator<Double> lazySpliterator2(Supplier<? extends Spliterator<Double>> supplier) {
            return super.lazySpliterator2(supplier);
        }

        static {
            $assertionsDisabled = !DoublePipeline.class.desiredAssertionStatus();
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
