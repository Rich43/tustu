package java.util.stream;

import java.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.function.IntFunction;
import java.util.stream.DoublePipeline;
import java.util.stream.IntPipeline;
import java.util.stream.LongPipeline;
import java.util.stream.Node;
import java.util.stream.ReferencePipeline;
import java.util.stream.Sink;
import java.util.stream.StreamSpliterators;

/* loaded from: rt.jar:java/util/stream/SliceOps.class */
final class SliceOps {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !SliceOps.class.desiredAssertionStatus();
    }

    private SliceOps() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long calcSize(long j2, long j3, long j4) {
        if (j2 >= 0) {
            return Math.max(-1L, Math.min(j2 - j3, j4));
        }
        return -1L;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long calcSliceFence(long j2, long j3) {
        long j4 = j3 >= 0 ? j2 + j3 : Long.MAX_VALUE;
        return j4 >= 0 ? j4 : Long.MAX_VALUE;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <P_IN> Spliterator<P_IN> sliceSpliterator(StreamShape streamShape, Spliterator<P_IN> spliterator, long j2, long j3) {
        if (!$assertionsDisabled && !spliterator.hasCharacteristics(16384)) {
            throw new AssertionError();
        }
        long jCalcSliceFence = calcSliceFence(j2, j3);
        switch (streamShape) {
            case REFERENCE:
                return new StreamSpliterators.SliceSpliterator.OfRef(spliterator, j2, jCalcSliceFence);
            case INT_VALUE:
                return new StreamSpliterators.SliceSpliterator.OfInt((Spliterator.OfInt) spliterator, j2, jCalcSliceFence);
            case LONG_VALUE:
                return new StreamSpliterators.SliceSpliterator.OfLong((Spliterator.OfLong) spliterator, j2, jCalcSliceFence);
            case DOUBLE_VALUE:
                return new StreamSpliterators.SliceSpliterator.OfDouble((Spliterator.OfDouble) spliterator, j2, jCalcSliceFence);
            default:
                throw new IllegalStateException("Unknown shape " + ((Object) streamShape));
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static <T> IntFunction<T[]> castingArray() {
        return i2 -> {
            return new Object[i2];
        };
    }

    public static <T> Stream<T> makeRef(AbstractPipeline<?, T, ?> abstractPipeline, final long j2, final long j3) {
        if (j2 < 0) {
            throw new IllegalArgumentException("Skip must be non-negative: " + j2);
        }
        return new ReferencePipeline.StatefulOp<T, T>(abstractPipeline, StreamShape.REFERENCE, flags(j3)) { // from class: java.util.stream.SliceOps.1
            Spliterator<T> unorderedSkipLimitSpliterator(Spliterator<T> spliterator, long j4, long j5, long j6) {
                if (j4 <= j6) {
                    j5 = j5 >= 0 ? Math.min(j5, j6 - j4) : j6 - j4;
                    j4 = 0;
                }
                return new StreamSpliterators.UnorderedSliceSpliterator.OfRef(spliterator, j4, j5);
            }

            @Override // java.util.stream.AbstractPipeline
            <P_IN> Spliterator<T> opEvaluateParallelLazy(PipelineHelper<T> pipelineHelper, Spliterator<P_IN> spliterator) {
                long jExactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
                if (jExactOutputSizeIfKnown > 0 && spliterator.hasCharacteristics(16384)) {
                    return new StreamSpliterators.SliceSpliterator.OfRef(pipelineHelper.wrapSpliterator(spliterator), j2, SliceOps.calcSliceFence(j2, j3));
                }
                if (!StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return unorderedSkipLimitSpliterator(pipelineHelper.wrapSpliterator(spliterator), j2, j3, jExactOutputSizeIfKnown);
                }
                return new SliceTask(this, pipelineHelper, spliterator, SliceOps.castingArray(), j2, j3).invoke().spliterator();
            }

            @Override // java.util.stream.ReferencePipeline.StatefulOp, java.util.stream.AbstractPipeline
            <P_IN> Node<T> opEvaluateParallel(PipelineHelper<T> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<T[]> intFunction) {
                long jExactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
                if (jExactOutputSizeIfKnown > 0 && spliterator.hasCharacteristics(16384)) {
                    return Nodes.collect(pipelineHelper, SliceOps.sliceSpliterator(pipelineHelper.getSourceShape(), spliterator, j2, j3), true, intFunction);
                }
                if (!StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return Nodes.collect(this, unorderedSkipLimitSpliterator(pipelineHelper.wrapSpliterator(spliterator), j2, j3, jExactOutputSizeIfKnown), true, intFunction);
                }
                return (Node) new SliceTask(this, pipelineHelper, spliterator, intFunction, j2, j3).invoke();
            }

            @Override // java.util.stream.AbstractPipeline
            Sink<T> opWrapSink(int i2, Sink<T> sink) {
                return new Sink.ChainedReference<T, T>(sink) { // from class: java.util.stream.SliceOps.1.1

                    /* renamed from: n, reason: collision with root package name */
                    long f12602n;

                    /* renamed from: m, reason: collision with root package name */
                    long f12603m;

                    {
                        this.f12602n = j2;
                        this.f12603m = j3 >= 0 ? j3 : Long.MAX_VALUE;
                    }

                    @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
                    public void begin(long j4) {
                        this.downstream.begin(SliceOps.calcSize(j4, j2, this.f12603m));
                    }

                    @Override // java.util.function.Consumer
                    public void accept(T t2) {
                        if (this.f12602n != 0) {
                            this.f12602n--;
                        } else if (this.f12603m > 0) {
                            this.f12603m--;
                            this.downstream.accept(t2);
                        }
                    }

                    @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
                    public boolean cancellationRequested() {
                        return this.f12603m == 0 || this.downstream.cancellationRequested();
                    }
                };
            }
        };
    }

    public static IntStream makeInt(AbstractPipeline<?, Integer, ?> abstractPipeline, final long j2, final long j3) {
        if (j2 < 0) {
            throw new IllegalArgumentException("Skip must be non-negative: " + j2);
        }
        return new IntPipeline.StatefulOp<Integer>(abstractPipeline, StreamShape.INT_VALUE, flags(j3)) { // from class: java.util.stream.SliceOps.2
            Spliterator.OfInt unorderedSkipLimitSpliterator(Spliterator.OfInt ofInt, long j4, long j5, long j6) {
                if (j4 <= j6) {
                    j5 = j5 >= 0 ? Math.min(j5, j6 - j4) : j6 - j4;
                    j4 = 0;
                }
                return new StreamSpliterators.UnorderedSliceSpliterator.OfInt(ofInt, j4, j5);
            }

            @Override // java.util.stream.AbstractPipeline
            <P_IN> Spliterator<Integer> opEvaluateParallelLazy(PipelineHelper<Integer> pipelineHelper, Spliterator<P_IN> spliterator) {
                long jExactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
                if (jExactOutputSizeIfKnown > 0 && spliterator.hasCharacteristics(16384)) {
                    return new StreamSpliterators.SliceSpliterator.OfInt((Spliterator.OfInt) pipelineHelper.wrapSpliterator(spliterator), j2, SliceOps.calcSliceFence(j2, j3));
                }
                if (!StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return unorderedSkipLimitSpliterator((Spliterator.OfInt) pipelineHelper.wrapSpliterator(spliterator), j2, j3, jExactOutputSizeIfKnown);
                }
                return new SliceTask(this, pipelineHelper, spliterator, i2 -> {
                    return new Integer[i2];
                }, j2, j3).invoke().spliterator();
            }

            @Override // java.util.stream.IntPipeline.StatefulOp, java.util.stream.AbstractPipeline
            <P_IN> Node<Integer> opEvaluateParallel(PipelineHelper<Integer> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<Integer[]> intFunction) {
                long jExactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
                if (jExactOutputSizeIfKnown > 0 && spliterator.hasCharacteristics(16384)) {
                    return Nodes.collectInt(pipelineHelper, SliceOps.sliceSpliterator(pipelineHelper.getSourceShape(), spliterator, j2, j3), true);
                }
                if (!StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return Nodes.collectInt(this, unorderedSkipLimitSpliterator((Spliterator.OfInt) pipelineHelper.wrapSpliterator(spliterator), j2, j3, jExactOutputSizeIfKnown), true);
                }
                return (Node) new SliceTask(this, pipelineHelper, spliterator, intFunction, j2, j3).invoke();
            }

            @Override // java.util.stream.AbstractPipeline
            Sink<Integer> opWrapSink(int i2, Sink<Integer> sink) {
                return new Sink.ChainedInt<Integer>(sink) { // from class: java.util.stream.SliceOps.2.1

                    /* renamed from: n, reason: collision with root package name */
                    long f12604n;

                    /* renamed from: m, reason: collision with root package name */
                    long f12605m;

                    {
                        this.f12604n = j2;
                        this.f12605m = j3 >= 0 ? j3 : Long.MAX_VALUE;
                    }

                    @Override // java.util.stream.Sink.ChainedInt, java.util.stream.Sink
                    public void begin(long j4) {
                        this.downstream.begin(SliceOps.calcSize(j4, j2, this.f12605m));
                    }

                    @Override // java.util.stream.Sink.OfInt, java.util.function.IntConsumer
                    public void accept(int i3) {
                        if (this.f12604n != 0) {
                            this.f12604n--;
                        } else if (this.f12605m > 0) {
                            this.f12605m--;
                            this.downstream.accept(i3);
                        }
                    }

                    @Override // java.util.stream.Sink.ChainedInt, java.util.stream.Sink
                    public boolean cancellationRequested() {
                        return this.f12605m == 0 || this.downstream.cancellationRequested();
                    }
                };
            }
        };
    }

    public static LongStream makeLong(AbstractPipeline<?, Long, ?> abstractPipeline, final long j2, final long j3) {
        if (j2 < 0) {
            throw new IllegalArgumentException("Skip must be non-negative: " + j2);
        }
        return new LongPipeline.StatefulOp<Long>(abstractPipeline, StreamShape.LONG_VALUE, flags(j3)) { // from class: java.util.stream.SliceOps.3
            Spliterator.OfLong unorderedSkipLimitSpliterator(Spliterator.OfLong ofLong, long j4, long j5, long j6) {
                if (j4 <= j6) {
                    j5 = j5 >= 0 ? Math.min(j5, j6 - j4) : j6 - j4;
                    j4 = 0;
                }
                return new StreamSpliterators.UnorderedSliceSpliterator.OfLong(ofLong, j4, j5);
            }

            @Override // java.util.stream.AbstractPipeline
            <P_IN> Spliterator<Long> opEvaluateParallelLazy(PipelineHelper<Long> pipelineHelper, Spliterator<P_IN> spliterator) {
                long jExactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
                if (jExactOutputSizeIfKnown > 0 && spliterator.hasCharacteristics(16384)) {
                    return new StreamSpliterators.SliceSpliterator.OfLong((Spliterator.OfLong) pipelineHelper.wrapSpliterator(spliterator), j2, SliceOps.calcSliceFence(j2, j3));
                }
                if (!StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return unorderedSkipLimitSpliterator((Spliterator.OfLong) pipelineHelper.wrapSpliterator(spliterator), j2, j3, jExactOutputSizeIfKnown);
                }
                return new SliceTask(this, pipelineHelper, spliterator, i2 -> {
                    return new Long[i2];
                }, j2, j3).invoke().spliterator();
            }

            @Override // java.util.stream.LongPipeline.StatefulOp, java.util.stream.AbstractPipeline
            <P_IN> Node<Long> opEvaluateParallel(PipelineHelper<Long> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<Long[]> intFunction) {
                long jExactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
                if (jExactOutputSizeIfKnown > 0 && spliterator.hasCharacteristics(16384)) {
                    return Nodes.collectLong(pipelineHelper, SliceOps.sliceSpliterator(pipelineHelper.getSourceShape(), spliterator, j2, j3), true);
                }
                if (!StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return Nodes.collectLong(this, unorderedSkipLimitSpliterator((Spliterator.OfLong) pipelineHelper.wrapSpliterator(spliterator), j2, j3, jExactOutputSizeIfKnown), true);
                }
                return (Node) new SliceTask(this, pipelineHelper, spliterator, intFunction, j2, j3).invoke();
            }

            @Override // java.util.stream.AbstractPipeline
            Sink<Long> opWrapSink(int i2, Sink<Long> sink) {
                return new Sink.ChainedLong<Long>(sink) { // from class: java.util.stream.SliceOps.3.1

                    /* renamed from: n, reason: collision with root package name */
                    long f12606n;

                    /* renamed from: m, reason: collision with root package name */
                    long f12607m;

                    {
                        this.f12606n = j2;
                        this.f12607m = j3 >= 0 ? j3 : Long.MAX_VALUE;
                    }

                    @Override // java.util.stream.Sink.ChainedLong, java.util.stream.Sink
                    public void begin(long j4) {
                        this.downstream.begin(SliceOps.calcSize(j4, j2, this.f12607m));
                    }

                    @Override // java.util.stream.Sink.OfLong, java.util.function.LongConsumer
                    public void accept(long j4) {
                        if (this.f12606n != 0) {
                            this.f12606n--;
                        } else if (this.f12607m > 0) {
                            this.f12607m--;
                            this.downstream.accept(j4);
                        }
                    }

                    @Override // java.util.stream.Sink.ChainedLong, java.util.stream.Sink
                    public boolean cancellationRequested() {
                        return this.f12607m == 0 || this.downstream.cancellationRequested();
                    }
                };
            }
        };
    }

    public static DoubleStream makeDouble(AbstractPipeline<?, Double, ?> abstractPipeline, final long j2, final long j3) {
        if (j2 < 0) {
            throw new IllegalArgumentException("Skip must be non-negative: " + j2);
        }
        return new DoublePipeline.StatefulOp<Double>(abstractPipeline, StreamShape.DOUBLE_VALUE, flags(j3)) { // from class: java.util.stream.SliceOps.4
            Spliterator.OfDouble unorderedSkipLimitSpliterator(Spliterator.OfDouble ofDouble, long j4, long j5, long j6) {
                if (j4 <= j6) {
                    j5 = j5 >= 0 ? Math.min(j5, j6 - j4) : j6 - j4;
                    j4 = 0;
                }
                return new StreamSpliterators.UnorderedSliceSpliterator.OfDouble(ofDouble, j4, j5);
            }

            @Override // java.util.stream.AbstractPipeline
            <P_IN> Spliterator<Double> opEvaluateParallelLazy(PipelineHelper<Double> pipelineHelper, Spliterator<P_IN> spliterator) {
                long jExactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
                if (jExactOutputSizeIfKnown > 0 && spliterator.hasCharacteristics(16384)) {
                    return new StreamSpliterators.SliceSpliterator.OfDouble((Spliterator.OfDouble) pipelineHelper.wrapSpliterator(spliterator), j2, SliceOps.calcSliceFence(j2, j3));
                }
                if (!StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return unorderedSkipLimitSpliterator((Spliterator.OfDouble) pipelineHelper.wrapSpliterator(spliterator), j2, j3, jExactOutputSizeIfKnown);
                }
                return new SliceTask(this, pipelineHelper, spliterator, i2 -> {
                    return new Double[i2];
                }, j2, j3).invoke().spliterator();
            }

            @Override // java.util.stream.DoublePipeline.StatefulOp, java.util.stream.AbstractPipeline
            <P_IN> Node<Double> opEvaluateParallel(PipelineHelper<Double> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<Double[]> intFunction) {
                long jExactOutputSizeIfKnown = pipelineHelper.exactOutputSizeIfKnown(spliterator);
                if (jExactOutputSizeIfKnown > 0 && spliterator.hasCharacteristics(16384)) {
                    return Nodes.collectDouble(pipelineHelper, SliceOps.sliceSpliterator(pipelineHelper.getSourceShape(), spliterator, j2, j3), true);
                }
                if (!StreamOpFlag.ORDERED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                    return Nodes.collectDouble(this, unorderedSkipLimitSpliterator((Spliterator.OfDouble) pipelineHelper.wrapSpliterator(spliterator), j2, j3, jExactOutputSizeIfKnown), true);
                }
                return (Node) new SliceTask(this, pipelineHelper, spliterator, intFunction, j2, j3).invoke();
            }

            @Override // java.util.stream.AbstractPipeline
            Sink<Double> opWrapSink(int i2, Sink<Double> sink) {
                return new Sink.ChainedDouble<Double>(sink) { // from class: java.util.stream.SliceOps.4.1

                    /* renamed from: n, reason: collision with root package name */
                    long f12608n;

                    /* renamed from: m, reason: collision with root package name */
                    long f12609m;

                    {
                        this.f12608n = j2;
                        this.f12609m = j3 >= 0 ? j3 : Long.MAX_VALUE;
                    }

                    @Override // java.util.stream.Sink.ChainedDouble, java.util.stream.Sink
                    public void begin(long j4) {
                        this.downstream.begin(SliceOps.calcSize(j4, j2, this.f12609m));
                    }

                    @Override // java.util.stream.Sink.OfDouble, java.util.stream.Sink, java.util.function.DoubleConsumer
                    public void accept(double d2) {
                        if (this.f12608n != 0) {
                            this.f12608n--;
                        } else if (this.f12609m > 0) {
                            this.f12609m--;
                            this.downstream.accept(d2);
                        }
                    }

                    @Override // java.util.stream.Sink.ChainedDouble, java.util.stream.Sink
                    public boolean cancellationRequested() {
                        return this.f12609m == 0 || this.downstream.cancellationRequested();
                    }
                };
            }
        };
    }

    private static int flags(long j2) {
        return StreamOpFlag.NOT_SIZED | (j2 != -1 ? StreamOpFlag.IS_SHORT_CIRCUIT : 0);
    }

    /* loaded from: rt.jar:java/util/stream/SliceOps$SliceTask.class */
    private static final class SliceTask<P_IN, P_OUT> extends AbstractShortCircuitTask<P_IN, P_OUT, Node<P_OUT>, SliceTask<P_IN, P_OUT>> {
        private final AbstractPipeline<P_OUT, P_OUT, ?> op;
        private final IntFunction<P_OUT[]> generator;
        private final long targetOffset;
        private final long targetSize;
        private long thisNodeSize;
        private volatile boolean completed;

        SliceTask(AbstractPipeline<P_OUT, P_OUT, ?> abstractPipeline, PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<P_OUT[]> intFunction, long j2, long j3) {
            super(pipelineHelper, spliterator);
            this.op = abstractPipeline;
            this.generator = intFunction;
            this.targetOffset = j2;
            this.targetSize = j3;
        }

        SliceTask(SliceTask<P_IN, P_OUT> sliceTask, Spliterator<P_IN> spliterator) {
            super(sliceTask, spliterator);
            this.op = sliceTask.op;
            this.generator = sliceTask.generator;
            this.targetOffset = sliceTask.targetOffset;
            this.targetSize = sliceTask.targetSize;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.util.stream.AbstractTask
        public SliceTask<P_IN, P_OUT> makeChild(Spliterator<P_IN> spliterator) {
            return new SliceTask<>(this, spliterator);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.util.stream.AbstractShortCircuitTask
        public final Node<P_OUT> getEmptyResult() {
            return Nodes.emptyNode(this.op.getOutputShape());
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.util.stream.AbstractTask
        public final Node<P_OUT> doLeaf() {
            if (isRoot()) {
                Node.Builder<P_OUT> builderMakeNodeBuilder = this.op.makeNodeBuilder(StreamOpFlag.SIZED.isPreserved(this.op.sourceOrOpFlags) ? this.op.exactOutputSizeIfKnown(this.spliterator) : -1L, this.generator);
                this.helper.copyIntoWithCancel(this.helper.wrapSink(this.op.opWrapSink(this.helper.getStreamAndOpFlags(), builderMakeNodeBuilder)), this.spliterator);
                return builderMakeNodeBuilder.build2();
            }
            Node<P_OUT> nodeBuild2 = ((Node.Builder) this.helper.wrapAndCopyInto(this.helper.makeNodeBuilder(-1L, this.generator), this.spliterator)).build2();
            this.thisNodeSize = nodeBuild2.count();
            this.completed = true;
            this.spliterator = null;
            return nodeBuild2;
        }

        @Override // java.util.stream.AbstractTask, java.util.concurrent.CountedCompleter
        public final void onCompletion(CountedCompleter<?> countedCompleter) {
            Node<P_OUT> nodeConc;
            if (!isLeaf()) {
                this.thisNodeSize = ((SliceTask) this.leftChild).thisNodeSize + ((SliceTask) this.rightChild).thisNodeSize;
                if (this.canceled) {
                    this.thisNodeSize = 0L;
                    nodeConc = getEmptyResult();
                } else if (this.thisNodeSize == 0) {
                    nodeConc = getEmptyResult();
                } else if (((SliceTask) this.leftChild).thisNodeSize == 0) {
                    nodeConc = ((SliceTask) this.rightChild).getLocalResult();
                } else {
                    nodeConc = Nodes.conc(this.op.getOutputShape(), ((SliceTask) this.leftChild).getLocalResult(), ((SliceTask) this.rightChild).getLocalResult());
                }
                setLocalResult(isRoot() ? doTruncate(nodeConc) : nodeConc);
                this.completed = true;
            }
            if (this.targetSize >= 0 && !isRoot() && isLeftCompleted(this.targetOffset + this.targetSize)) {
                cancelLaterNodes();
            }
            super.onCompletion(countedCompleter);
        }

        @Override // java.util.stream.AbstractShortCircuitTask
        protected void cancel() {
            super.cancel();
            if (this.completed) {
                setLocalResult(getEmptyResult());
            }
        }

        private Node<P_OUT> doTruncate(Node<P_OUT> node) {
            return node.truncate(this.targetOffset, this.targetSize >= 0 ? Math.min(node.count(), this.targetOffset + this.targetSize) : this.thisNodeSize, this.generator);
        }

        /* JADX WARN: Multi-variable type inference failed */
        private boolean isLeftCompleted(long j2) {
            SliceTask sliceTask;
            long jCompletedSize = this.completed ? this.thisNodeSize : completedSize(j2);
            if (jCompletedSize >= j2) {
                return true;
            }
            SliceTask<P_IN, P_OUT> sliceTask2 = this;
            for (SliceTask<P_IN, P_OUT> sliceTask3 = (SliceTask) getParent(); sliceTask3 != null; sliceTask3 = (SliceTask) sliceTask3.getParent()) {
                if (sliceTask2 == sliceTask3.rightChild && (sliceTask = (SliceTask) sliceTask3.leftChild) != null) {
                    jCompletedSize += sliceTask.completedSize(j2);
                    if (jCompletedSize >= j2) {
                        return true;
                    }
                }
                sliceTask2 = sliceTask3;
            }
            return jCompletedSize >= j2;
        }

        private long completedSize(long j2) {
            if (this.completed) {
                return this.thisNodeSize;
            }
            SliceTask sliceTask = (SliceTask) this.leftChild;
            SliceTask sliceTask2 = (SliceTask) this.rightChild;
            if (sliceTask == null || sliceTask2 == null) {
                return this.thisNodeSize;
            }
            long jCompletedSize = sliceTask.completedSize(j2);
            return jCompletedSize >= j2 ? jCompletedSize : jCompletedSize + sliceTask2.completedSize(j2);
        }
    }
}
