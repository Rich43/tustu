package java.util.stream;

import java.util.Objects;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountedCompleter;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.Node;
import java.util.stream.Sink;

/* loaded from: rt.jar:java/util/stream/ForEachOps.class */
final class ForEachOps {
    private ForEachOps() {
    }

    public static <T> TerminalOp<T, Void> makeRef(Consumer<? super T> consumer, boolean z2) {
        Objects.requireNonNull(consumer);
        return new ForEachOp.OfRef(consumer, z2);
    }

    public static TerminalOp<Integer, Void> makeInt(IntConsumer intConsumer, boolean z2) {
        Objects.requireNonNull(intConsumer);
        return new ForEachOp.OfInt(intConsumer, z2);
    }

    public static TerminalOp<Long, Void> makeLong(LongConsumer longConsumer, boolean z2) {
        Objects.requireNonNull(longConsumer);
        return new ForEachOp.OfLong(longConsumer, z2);
    }

    public static TerminalOp<Double, Void> makeDouble(DoubleConsumer doubleConsumer, boolean z2) {
        Objects.requireNonNull(doubleConsumer);
        return new ForEachOp.OfDouble(doubleConsumer, z2);
    }

    /* loaded from: rt.jar:java/util/stream/ForEachOps$ForEachOp.class */
    static abstract class ForEachOp<T> implements TerminalOp<T, Void>, TerminalSink<T, Void> {
        private final boolean ordered;

        protected ForEachOp(boolean z2) {
            this.ordered = z2;
        }

        @Override // java.util.stream.TerminalOp
        public int getOpFlags() {
            if (this.ordered) {
                return 0;
            }
            return StreamOpFlag.NOT_ORDERED;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.stream.TerminalOp
        public <S> Void evaluateSequential(PipelineHelper<T> pipelineHelper, Spliterator<S> spliterator) {
            return ((ForEachOp) pipelineHelper.wrapAndCopyInto(this, spliterator)).get();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.stream.TerminalOp
        public <S> Void evaluateParallel(PipelineHelper<T> pipelineHelper, Spliterator<S> spliterator) {
            if (this.ordered) {
                new ForEachOrderedTask(pipelineHelper, spliterator, this).invoke();
                return null;
            }
            new ForEachTask(pipelineHelper, spliterator, pipelineHelper.wrapSink(this)).invoke();
            return null;
        }

        @Override // java.util.function.Supplier
        public Void get() {
            return null;
        }

        /* loaded from: rt.jar:java/util/stream/ForEachOps$ForEachOp$OfRef.class */
        static final class OfRef<T> extends ForEachOp<T> {
            final Consumer<? super T> consumer;

            @Override // java.util.stream.ForEachOps.ForEachOp, java.util.stream.TerminalOp
            public /* bridge */ /* synthetic */ Void evaluateSequential(PipelineHelper pipelineHelper, Spliterator spliterator) {
                return super.evaluateSequential(pipelineHelper, spliterator);
            }

            @Override // java.util.stream.ForEachOps.ForEachOp, java.util.stream.TerminalOp
            public /* bridge */ /* synthetic */ Void evaluateParallel(PipelineHelper pipelineHelper, Spliterator spliterator) {
                return super.evaluateParallel(pipelineHelper, spliterator);
            }

            @Override // java.util.stream.ForEachOps.ForEachOp, java.util.function.Supplier
            public /* bridge */ /* synthetic */ Object get() {
                return super.get();
            }

            OfRef(Consumer<? super T> consumer, boolean z2) {
                super(z2);
                this.consumer = consumer;
            }

            @Override // java.util.function.Consumer
            public void accept(T t2) {
                this.consumer.accept(t2);
            }
        }

        /* loaded from: rt.jar:java/util/stream/ForEachOps$ForEachOp$OfInt.class */
        static final class OfInt extends ForEachOp<Integer> implements Sink.OfInt {
            final IntConsumer consumer;

            @Override // java.util.stream.ForEachOps.ForEachOp, java.util.stream.TerminalOp
            public /* bridge */ /* synthetic */ Void evaluateSequential(PipelineHelper pipelineHelper, Spliterator spliterator) {
                return super.evaluateSequential(pipelineHelper, spliterator);
            }

            @Override // java.util.stream.ForEachOps.ForEachOp, java.util.stream.TerminalOp
            public /* bridge */ /* synthetic */ Void evaluateParallel(PipelineHelper pipelineHelper, Spliterator spliterator) {
                return super.evaluateParallel(pipelineHelper, spliterator);
            }

            @Override // java.util.stream.ForEachOps.ForEachOp, java.util.function.Supplier
            public /* bridge */ /* synthetic */ Object get() {
                return super.get();
            }

            OfInt(IntConsumer intConsumer, boolean z2) {
                super(z2);
                this.consumer = intConsumer;
            }

            @Override // java.util.stream.TerminalOp
            public StreamShape inputShape() {
                return StreamShape.INT_VALUE;
            }

            @Override // java.util.stream.Sink, java.util.stream.Sink.OfInt, java.util.function.IntConsumer
            public void accept(int i2) {
                this.consumer.accept(i2);
            }
        }

        /* loaded from: rt.jar:java/util/stream/ForEachOps$ForEachOp$OfLong.class */
        static final class OfLong extends ForEachOp<Long> implements Sink.OfLong {
            final LongConsumer consumer;

            @Override // java.util.stream.ForEachOps.ForEachOp, java.util.stream.TerminalOp
            public /* bridge */ /* synthetic */ Void evaluateSequential(PipelineHelper pipelineHelper, Spliterator spliterator) {
                return super.evaluateSequential(pipelineHelper, spliterator);
            }

            @Override // java.util.stream.ForEachOps.ForEachOp, java.util.stream.TerminalOp
            public /* bridge */ /* synthetic */ Void evaluateParallel(PipelineHelper pipelineHelper, Spliterator spliterator) {
                return super.evaluateParallel(pipelineHelper, spliterator);
            }

            @Override // java.util.stream.ForEachOps.ForEachOp, java.util.function.Supplier
            public /* bridge */ /* synthetic */ Object get() {
                return super.get();
            }

            OfLong(LongConsumer longConsumer, boolean z2) {
                super(z2);
                this.consumer = longConsumer;
            }

            @Override // java.util.stream.TerminalOp
            public StreamShape inputShape() {
                return StreamShape.LONG_VALUE;
            }

            @Override // java.util.stream.Sink, java.util.stream.Sink.OfLong, java.util.function.LongConsumer
            public void accept(long j2) {
                this.consumer.accept(j2);
            }
        }

        /* loaded from: rt.jar:java/util/stream/ForEachOps$ForEachOp$OfDouble.class */
        static final class OfDouble extends ForEachOp<Double> implements Sink.OfDouble {
            final DoubleConsumer consumer;

            @Override // java.util.stream.ForEachOps.ForEachOp, java.util.stream.TerminalOp
            public /* bridge */ /* synthetic */ Void evaluateSequential(PipelineHelper pipelineHelper, Spliterator spliterator) {
                return super.evaluateSequential(pipelineHelper, spliterator);
            }

            @Override // java.util.stream.ForEachOps.ForEachOp, java.util.stream.TerminalOp
            public /* bridge */ /* synthetic */ Void evaluateParallel(PipelineHelper pipelineHelper, Spliterator spliterator) {
                return super.evaluateParallel(pipelineHelper, spliterator);
            }

            @Override // java.util.stream.ForEachOps.ForEachOp, java.util.function.Supplier
            public /* bridge */ /* synthetic */ Object get() {
                return super.get();
            }

            OfDouble(DoubleConsumer doubleConsumer, boolean z2) {
                super(z2);
                this.consumer = doubleConsumer;
            }

            @Override // java.util.stream.TerminalOp
            public StreamShape inputShape() {
                return StreamShape.DOUBLE_VALUE;
            }

            @Override // java.util.stream.Sink, java.util.function.DoubleConsumer
            public void accept(double d2) {
                this.consumer.accept(d2);
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/ForEachOps$ForEachTask.class */
    static final class ForEachTask<S, T> extends CountedCompleter<Void> {
        private Spliterator<S> spliterator;
        private final Sink<S> sink;
        private final PipelineHelper<T> helper;
        private long targetSize;

        ForEachTask(PipelineHelper<T> pipelineHelper, Spliterator<S> spliterator, Sink<S> sink) {
            super(null);
            this.sink = sink;
            this.helper = pipelineHelper;
            this.spliterator = spliterator;
            this.targetSize = 0L;
        }

        ForEachTask(ForEachTask<S, T> forEachTask, Spliterator<S> spliterator) {
            super(forEachTask);
            this.spliterator = spliterator;
            this.sink = forEachTask.sink;
            this.targetSize = forEachTask.targetSize;
            this.helper = forEachTask.helper;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.concurrent.CountedCompleter
        public void compute() {
            Spliterator<S> spliteratorTrySplit;
            ForEachTask forEachTask;
            Spliterator<S> spliterator = this.spliterator;
            long jEstimateSize = spliterator.estimateSize();
            long j2 = this.targetSize;
            long jSuggestTargetSize = j2;
            if (j2 == 0) {
                jSuggestTargetSize = AbstractTask.suggestTargetSize(jEstimateSize);
                this.targetSize = this;
            }
            boolean zIsKnown = StreamOpFlag.SHORT_CIRCUIT.isKnown(this.helper.getStreamAndOpFlags());
            boolean z2 = false;
            Sink<S> sink = this.sink;
            ForEachTask forEachTask2 = this;
            while (true) {
                if (zIsKnown && sink.cancellationRequested()) {
                    break;
                }
                if (jEstimateSize <= jSuggestTargetSize || (spliteratorTrySplit = spliterator.trySplit()) == null) {
                    break;
                }
                ForEachTask forEachTask3 = new ForEachTask(forEachTask2, spliteratorTrySplit);
                forEachTask2.addToPendingCount(1);
                if (z2) {
                    z2 = false;
                    spliterator = spliteratorTrySplit;
                    forEachTask = forEachTask2;
                    forEachTask2 = forEachTask3;
                } else {
                    z2 = true;
                    forEachTask = forEachTask3;
                }
                forEachTask.fork();
                jEstimateSize = spliterator.estimateSize();
            }
            forEachTask2.helper.copyInto(sink, spliterator);
            forEachTask2.spliterator = null;
            forEachTask2.propagateCompletion();
        }
    }

    /* loaded from: rt.jar:java/util/stream/ForEachOps$ForEachOrderedTask.class */
    static final class ForEachOrderedTask<S, T> extends CountedCompleter<Void> {
        private final PipelineHelper<T> helper;
        private Spliterator<S> spliterator;
        private final long targetSize;
        private final ConcurrentHashMap<ForEachOrderedTask<S, T>, ForEachOrderedTask<S, T>> completionMap;
        private final Sink<T> action;
        private final ForEachOrderedTask<S, T> leftPredecessor;
        private Node<T> node;

        protected ForEachOrderedTask(PipelineHelper<T> pipelineHelper, Spliterator<S> spliterator, Sink<T> sink) {
            super(null);
            this.helper = pipelineHelper;
            this.spliterator = spliterator;
            this.targetSize = AbstractTask.suggestTargetSize(spliterator.estimateSize());
            this.completionMap = new ConcurrentHashMap<>(Math.max(16, AbstractTask.getLeafTarget() << 1));
            this.action = sink;
            this.leftPredecessor = null;
        }

        ForEachOrderedTask(ForEachOrderedTask<S, T> forEachOrderedTask, Spliterator<S> spliterator, ForEachOrderedTask<S, T> forEachOrderedTask2) {
            super(forEachOrderedTask);
            this.helper = forEachOrderedTask.helper;
            this.spliterator = spliterator;
            this.targetSize = forEachOrderedTask.targetSize;
            this.completionMap = forEachOrderedTask.completionMap;
            this.action = forEachOrderedTask.action;
            this.leftPredecessor = forEachOrderedTask2;
        }

        @Override // java.util.concurrent.CountedCompleter
        public final void compute() {
            doCompute(this);
        }

        private static <S, T> void doCompute(ForEachOrderedTask<S, T> forEachOrderedTask) {
            Spliterator<S> spliteratorTrySplit;
            ForEachOrderedTask<S, T> forEachOrderedTask2;
            Spliterator<S> spliterator = ((ForEachOrderedTask) forEachOrderedTask).spliterator;
            long j2 = ((ForEachOrderedTask) forEachOrderedTask).targetSize;
            boolean z2 = false;
            while (spliterator.estimateSize() > j2 && (spliteratorTrySplit = spliterator.trySplit()) != null) {
                ForEachOrderedTask<S, T> forEachOrderedTask3 = new ForEachOrderedTask<>(forEachOrderedTask, spliteratorTrySplit, ((ForEachOrderedTask) forEachOrderedTask).leftPredecessor);
                ForEachOrderedTask<S, T> forEachOrderedTask4 = new ForEachOrderedTask<>(forEachOrderedTask, spliterator, forEachOrderedTask3);
                forEachOrderedTask.addToPendingCount(1);
                forEachOrderedTask4.addToPendingCount(1);
                ((ForEachOrderedTask) forEachOrderedTask).completionMap.put(forEachOrderedTask3, forEachOrderedTask4);
                if (((ForEachOrderedTask) forEachOrderedTask).leftPredecessor != null) {
                    forEachOrderedTask3.addToPendingCount(1);
                    if (((ForEachOrderedTask) forEachOrderedTask).completionMap.replace(((ForEachOrderedTask) forEachOrderedTask).leftPredecessor, forEachOrderedTask, forEachOrderedTask3)) {
                        forEachOrderedTask.addToPendingCount(-1);
                    } else {
                        forEachOrderedTask3.addToPendingCount(-1);
                    }
                }
                if (z2) {
                    z2 = false;
                    spliterator = spliteratorTrySplit;
                    forEachOrderedTask = forEachOrderedTask3;
                    forEachOrderedTask2 = forEachOrderedTask4;
                } else {
                    z2 = true;
                    forEachOrderedTask = forEachOrderedTask4;
                    forEachOrderedTask2 = forEachOrderedTask3;
                }
                forEachOrderedTask2.fork();
            }
            if (forEachOrderedTask.getPendingCount() > 0) {
                ((ForEachOrderedTask) forEachOrderedTask).node = ((Node.Builder) ((ForEachOrderedTask) forEachOrderedTask).helper.wrapAndCopyInto(((ForEachOrderedTask) forEachOrderedTask).helper.makeNodeBuilder(((ForEachOrderedTask) forEachOrderedTask).helper.exactOutputSizeIfKnown(spliterator), i2 -> {
                    return new Object[i2];
                }), spliterator)).build2();
                ((ForEachOrderedTask) forEachOrderedTask).spliterator = null;
            }
            forEachOrderedTask.tryComplete();
        }

        @Override // java.util.concurrent.CountedCompleter
        public void onCompletion(CountedCompleter<?> countedCompleter) {
            if (this.node != null) {
                this.node.forEach(this.action);
                this.node = null;
            } else if (this.spliterator != null) {
                this.helper.wrapAndCopyInto(this.action, this.spliterator);
                this.spliterator = null;
            }
            ForEachOrderedTask<S, T> forEachOrderedTaskRemove = this.completionMap.remove(this);
            if (forEachOrderedTaskRemove != null) {
                forEachOrderedTaskRemove.tryComplete();
            }
        }
    }
}
