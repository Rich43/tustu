package java.util.stream;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.IntFunction;
import java.util.function.Supplier;
import java.util.stream.BaseStream;
import java.util.stream.Node;

/* loaded from: rt.jar:java/util/stream/AbstractPipeline.class */
abstract class AbstractPipeline<E_IN, E_OUT, S extends BaseStream<E_OUT, S>> extends PipelineHelper<E_OUT> implements BaseStream<E_OUT, S> {
    private static final String MSG_STREAM_LINKED = "stream has already been operated upon or closed";
    private static final String MSG_CONSUMED = "source already consumed or closed";
    private final AbstractPipeline sourceStage;
    private final AbstractPipeline previousStage;
    protected final int sourceOrOpFlags;
    private AbstractPipeline nextStage;
    private int depth;
    private int combinedFlags;
    private Spliterator<?> sourceSpliterator;
    private Supplier<? extends Spliterator<?>> sourceSupplier;
    private boolean linkedOrConsumed;
    private boolean sourceAnyStateful;
    private Runnable sourceCloseAction;
    private boolean parallel;
    static final /* synthetic */ boolean $assertionsDisabled;

    abstract StreamShape getOutputShape();

    abstract <P_IN> Node<E_OUT> evaluateToNode(PipelineHelper<E_OUT> pipelineHelper, Spliterator<P_IN> spliterator, boolean z2, IntFunction<E_OUT[]> intFunction);

    abstract <P_IN> Spliterator<E_OUT> wrap(PipelineHelper<E_OUT> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z2);

    abstract Spliterator<E_OUT> lazySpliterator(Supplier<? extends Spliterator<E_OUT>> supplier);

    abstract void forEachWithCancel(Spliterator<E_OUT> spliterator, Sink<E_OUT> sink);

    @Override // java.util.stream.PipelineHelper
    abstract Node.Builder<E_OUT> makeNodeBuilder(long j2, IntFunction<E_OUT[]> intFunction);

    abstract boolean opIsStateful();

    abstract Sink<E_IN> opWrapSink(int i2, Sink<E_OUT> sink);

    static {
        $assertionsDisabled = !AbstractPipeline.class.desiredAssertionStatus();
    }

    AbstractPipeline(Supplier<? extends Spliterator<?>> supplier, int i2, boolean z2) {
        this.previousStage = null;
        this.sourceSupplier = supplier;
        this.sourceStage = this;
        this.sourceOrOpFlags = i2 & StreamOpFlag.STREAM_MASK;
        this.combinedFlags = ((this.sourceOrOpFlags << 1) ^ (-1)) & StreamOpFlag.INITIAL_OPS_VALUE;
        this.depth = 0;
        this.parallel = z2;
    }

    AbstractPipeline(Spliterator<?> spliterator, int i2, boolean z2) {
        this.previousStage = null;
        this.sourceSpliterator = spliterator;
        this.sourceStage = this;
        this.sourceOrOpFlags = i2 & StreamOpFlag.STREAM_MASK;
        this.combinedFlags = ((this.sourceOrOpFlags << 1) ^ (-1)) & StreamOpFlag.INITIAL_OPS_VALUE;
        this.depth = 0;
        this.parallel = z2;
    }

    AbstractPipeline(AbstractPipeline<?, E_IN, ?> abstractPipeline, int i2) {
        if (abstractPipeline.linkedOrConsumed) {
            throw new IllegalStateException(MSG_STREAM_LINKED);
        }
        abstractPipeline.linkedOrConsumed = true;
        abstractPipeline.nextStage = this;
        this.previousStage = abstractPipeline;
        this.sourceOrOpFlags = i2 & StreamOpFlag.OP_MASK;
        this.combinedFlags = StreamOpFlag.combineOpFlags(i2, abstractPipeline.combinedFlags);
        this.sourceStage = abstractPipeline.sourceStage;
        if (opIsStateful()) {
            this.sourceStage.sourceAnyStateful = true;
        }
        this.depth = abstractPipeline.depth + 1;
    }

    final <R> R evaluate(TerminalOp<E_OUT, R> terminalOp) {
        if (!$assertionsDisabled && getOutputShape() != terminalOp.inputShape()) {
            throw new AssertionError();
        }
        if (this.linkedOrConsumed) {
            throw new IllegalStateException(MSG_STREAM_LINKED);
        }
        this.linkedOrConsumed = true;
        if (isParallel()) {
            return terminalOp.evaluateParallel(this, sourceSpliterator(terminalOp.getOpFlags()));
        }
        return terminalOp.evaluateSequential(this, sourceSpliterator(terminalOp.getOpFlags()));
    }

    final Node<E_OUT> evaluateToArrayNode(IntFunction<E_OUT[]> intFunction) {
        if (this.linkedOrConsumed) {
            throw new IllegalStateException(MSG_STREAM_LINKED);
        }
        this.linkedOrConsumed = true;
        if (isParallel() && this.previousStage != null && opIsStateful()) {
            this.depth = 0;
            return opEvaluateParallel(this.previousStage, this.previousStage.sourceSpliterator(0), intFunction);
        }
        return evaluate(sourceSpliterator(0), true, intFunction);
    }

    final Spliterator<E_OUT> sourceStageSpliterator() {
        if (this != this.sourceStage) {
            throw new IllegalStateException();
        }
        if (this.linkedOrConsumed) {
            throw new IllegalStateException(MSG_STREAM_LINKED);
        }
        this.linkedOrConsumed = true;
        if (this.sourceStage.sourceSpliterator != null) {
            Spliterator<E_OUT> spliterator = (Spliterator<E_OUT>) this.sourceStage.sourceSpliterator;
            this.sourceStage.sourceSpliterator = null;
            return spliterator;
        }
        if (this.sourceStage.sourceSupplier != null) {
            Spliterator<E_OUT> spliterator2 = (Spliterator) this.sourceStage.sourceSupplier.get();
            this.sourceStage.sourceSupplier = null;
            return spliterator2;
        }
        throw new IllegalStateException(MSG_CONSUMED);
    }

    @Override // java.util.stream.BaseStream
    public final S sequential() {
        this.sourceStage.parallel = false;
        return this;
    }

    @Override // java.util.stream.BaseStream
    public final S parallel() {
        this.sourceStage.parallel = true;
        return this;
    }

    @Override // java.util.stream.BaseStream, java.lang.AutoCloseable
    public void close() {
        this.linkedOrConsumed = true;
        this.sourceSupplier = null;
        this.sourceSpliterator = null;
        if (this.sourceStage.sourceCloseAction != null) {
            Runnable runnable = this.sourceStage.sourceCloseAction;
            this.sourceStage.sourceCloseAction = null;
            runnable.run();
        }
    }

    @Override // java.util.stream.BaseStream
    public S onClose(Runnable runnable) {
        Objects.requireNonNull(runnable);
        Runnable runnable2 = this.sourceStage.sourceCloseAction;
        this.sourceStage.sourceCloseAction = runnable2 == null ? runnable : Streams.composeWithExceptions(runnable2, runnable);
        return this;
    }

    @Override // java.util.stream.BaseStream
    /* renamed from: spliterator */
    public Spliterator<E_OUT> spliterator2() {
        if (this.linkedOrConsumed) {
            throw new IllegalStateException(MSG_STREAM_LINKED);
        }
        this.linkedOrConsumed = true;
        if (this == this.sourceStage) {
            if (this.sourceStage.sourceSpliterator != null) {
                Spliterator<E_OUT> spliterator = (Spliterator<E_OUT>) this.sourceStage.sourceSpliterator;
                this.sourceStage.sourceSpliterator = null;
                return spliterator;
            }
            if (this.sourceStage.sourceSupplier != null) {
                Supplier<? extends Spliterator<?>> supplier = this.sourceStage.sourceSupplier;
                this.sourceStage.sourceSupplier = null;
                return lazySpliterator(supplier);
            }
            throw new IllegalStateException(MSG_CONSUMED);
        }
        return wrap(this, () -> {
            return sourceSpliterator(0);
        }, isParallel());
    }

    @Override // java.util.stream.BaseStream
    public final boolean isParallel() {
        return this.sourceStage.parallel;
    }

    final int getStreamFlags() {
        return StreamOpFlag.toStreamFlags(this.combinedFlags);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v3, types: [java.util.stream.AbstractPipeline] */
    private Spliterator<?> sourceSpliterator(int i2) {
        Spliterator spliteratorOpEvaluateParallelLazy;
        if (this.sourceStage.sourceSpliterator != null) {
            spliteratorOpEvaluateParallelLazy = this.sourceStage.sourceSpliterator;
            this.sourceStage.sourceSpliterator = null;
        } else if (this.sourceStage.sourceSupplier != null) {
            spliteratorOpEvaluateParallelLazy = this.sourceStage.sourceSupplier.get();
            this.sourceStage.sourceSupplier = null;
        } else {
            throw new IllegalStateException(MSG_CONSUMED);
        }
        if (isParallel() && this.sourceStage.sourceAnyStateful) {
            int i3 = 1;
            AbstractPipeline<E_IN, E_OUT, S> abstractPipeline = this.sourceStage;
            AbstractPipeline<E_IN, E_OUT, S> abstractPipeline2 = this.sourceStage.nextStage;
            while (abstractPipeline != this) {
                int i4 = abstractPipeline2.sourceOrOpFlags;
                if (abstractPipeline2.opIsStateful()) {
                    i3 = 0;
                    if (StreamOpFlag.SHORT_CIRCUIT.isKnown(i4)) {
                        i4 &= StreamOpFlag.IS_SHORT_CIRCUIT ^ (-1);
                    }
                    spliteratorOpEvaluateParallelLazy = abstractPipeline2.opEvaluateParallelLazy(abstractPipeline, spliteratorOpEvaluateParallelLazy);
                    i4 = spliteratorOpEvaluateParallelLazy.hasCharacteristics(64) ? (i4 & (StreamOpFlag.NOT_SIZED ^ (-1))) | StreamOpFlag.IS_SIZED : (i4 & (StreamOpFlag.IS_SIZED ^ (-1))) | StreamOpFlag.NOT_SIZED;
                }
                int i5 = i3;
                i3++;
                abstractPipeline2.depth = i5;
                abstractPipeline2.combinedFlags = StreamOpFlag.combineOpFlags(i4, abstractPipeline.combinedFlags);
                abstractPipeline = abstractPipeline2;
                abstractPipeline2 = abstractPipeline2.nextStage;
            }
        }
        if (i2 != 0) {
            this.combinedFlags = StreamOpFlag.combineOpFlags(i2, this.combinedFlags);
        }
        return spliteratorOpEvaluateParallelLazy;
    }

    @Override // java.util.stream.PipelineHelper
    final StreamShape getSourceShape() {
        AbstractPipeline<E_IN, E_OUT, S> abstractPipeline = this;
        while (true) {
            AbstractPipeline<E_IN, E_OUT, S> abstractPipeline2 = abstractPipeline;
            if (abstractPipeline2.depth > 0) {
                abstractPipeline = abstractPipeline2.previousStage;
            } else {
                return abstractPipeline2.getOutputShape();
            }
        }
    }

    @Override // java.util.stream.PipelineHelper
    final <P_IN> long exactOutputSizeIfKnown(Spliterator<P_IN> spliterator) {
        if (StreamOpFlag.SIZED.isKnown(getStreamAndOpFlags())) {
            return spliterator.getExactSizeIfKnown();
        }
        return -1L;
    }

    /* JADX WARN: Incorrect return type in method signature: <P_IN:Ljava/lang/Object;S::Ljava/util/stream/Sink<TE_OUT;>;>(TS;Ljava/util/Spliterator<TP_IN;>;)TS; */
    @Override // java.util.stream.PipelineHelper
    final Sink wrapAndCopyInto(Sink sink, Spliterator spliterator) {
        copyInto(wrapSink((Sink) Objects.requireNonNull(sink)), spliterator);
        return sink;
    }

    @Override // java.util.stream.PipelineHelper
    final <P_IN> void copyInto(Sink<P_IN> sink, Spliterator<P_IN> spliterator) {
        Objects.requireNonNull(sink);
        if (!StreamOpFlag.SHORT_CIRCUIT.isKnown(getStreamAndOpFlags())) {
            sink.begin(spliterator.getExactSizeIfKnown());
            spliterator.forEachRemaining(sink);
            sink.end();
            return;
        }
        copyIntoWithCancel(sink, spliterator);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.stream.PipelineHelper
    final <P_IN> void copyIntoWithCancel(Sink<P_IN> sink, Spliterator<P_IN> spliterator) {
        AbstractPipeline<E_IN, E_OUT, S> abstractPipeline = this;
        while (true) {
            AbstractPipeline<E_IN, E_OUT, S> abstractPipeline2 = abstractPipeline;
            if (abstractPipeline2.depth > 0) {
                abstractPipeline = abstractPipeline2.previousStage;
            } else {
                sink.begin(spliterator.getExactSizeIfKnown());
                abstractPipeline2.forEachWithCancel(spliterator, sink);
                sink.end();
                return;
            }
        }
    }

    @Override // java.util.stream.PipelineHelper
    final int getStreamAndOpFlags() {
        return this.combinedFlags;
    }

    final boolean isOrdered() {
        return StreamOpFlag.ORDERED.isKnown(this.combinedFlags);
    }

    @Override // java.util.stream.PipelineHelper
    final <P_IN> Sink<P_IN> wrapSink(Sink<E_OUT> sink) {
        Objects.requireNonNull(sink);
        AbstractPipeline<E_IN, E_OUT, S> abstractPipeline = this;
        while (true) {
            AbstractPipeline<E_IN, E_OUT, S> abstractPipeline2 = abstractPipeline;
            if (abstractPipeline2.depth > 0) {
                sink = abstractPipeline2.opWrapSink(abstractPipeline2.previousStage.combinedFlags, sink);
                abstractPipeline = abstractPipeline2.previousStage;
            } else {
                return (Sink<P_IN>) sink;
            }
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.util.stream.PipelineHelper
    final <P_IN> Spliterator<E_OUT> wrapSpliterator(Spliterator<P_IN> spliterator) {
        if (this.depth == 0) {
            return spliterator;
        }
        return wrap(this, () -> {
            return spliterator;
        }, isParallel());
    }

    @Override // java.util.stream.PipelineHelper
    final <P_IN> Node<E_OUT> evaluate(Spliterator<P_IN> spliterator, boolean z2, IntFunction<E_OUT[]> intFunction) {
        if (isParallel()) {
            return evaluateToNode(this, spliterator, z2, intFunction);
        }
        return ((Node.Builder) wrapAndCopyInto(makeNodeBuilder(exactOutputSizeIfKnown(spliterator), intFunction), spliterator)).build2();
    }

    <P_IN> Node<E_OUT> opEvaluateParallel(PipelineHelper<E_OUT> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<E_OUT[]> intFunction) {
        throw new UnsupportedOperationException("Parallel evaluation is not supported");
    }

    <P_IN> Spliterator<E_OUT> opEvaluateParallelLazy(PipelineHelper<E_OUT> pipelineHelper, Spliterator<P_IN> spliterator) {
        return opEvaluateParallel(pipelineHelper, spliterator, i2 -> {
            return new Object[i2];
        }).spliterator();
    }
}
