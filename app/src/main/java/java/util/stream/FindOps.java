package java.util.stream;

import java.util.Optional;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.OptionalLong;
import java.util.Spliterator;
import java.util.concurrent.CountedCompleter;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Sink;

/* loaded from: rt.jar:java/util/stream/FindOps.class */
final class FindOps {
    private FindOps() {
    }

    public static <T> TerminalOp<T, Optional<T>> makeRef(boolean z2) {
        return new FindOp(z2, StreamShape.REFERENCE, Optional.empty(), (v0) -> {
            return v0.isPresent();
        }, FindSink.OfRef::new);
    }

    public static TerminalOp<Integer, OptionalInt> makeInt(boolean z2) {
        return new FindOp(z2, StreamShape.INT_VALUE, OptionalInt.empty(), (v0) -> {
            return v0.isPresent();
        }, FindSink.OfInt::new);
    }

    public static TerminalOp<Long, OptionalLong> makeLong(boolean z2) {
        return new FindOp(z2, StreamShape.LONG_VALUE, OptionalLong.empty(), (v0) -> {
            return v0.isPresent();
        }, FindSink.OfLong::new);
    }

    public static TerminalOp<Double, OptionalDouble> makeDouble(boolean z2) {
        return new FindOp(z2, StreamShape.DOUBLE_VALUE, OptionalDouble.empty(), (v0) -> {
            return v0.isPresent();
        }, FindSink.OfDouble::new);
    }

    /* loaded from: rt.jar:java/util/stream/FindOps$FindOp.class */
    private static final class FindOp<T, O> implements TerminalOp<T, O> {
        private final StreamShape shape;
        final boolean mustFindFirst;
        final O emptyValue;
        final Predicate<O> presentPredicate;
        final Supplier<TerminalSink<T, O>> sinkSupplier;

        FindOp(boolean z2, StreamShape streamShape, O o2, Predicate<O> predicate, Supplier<TerminalSink<T, O>> supplier) {
            this.mustFindFirst = z2;
            this.shape = streamShape;
            this.emptyValue = o2;
            this.presentPredicate = predicate;
            this.sinkSupplier = supplier;
        }

        @Override // java.util.stream.TerminalOp
        public int getOpFlags() {
            return StreamOpFlag.IS_SHORT_CIRCUIT | (this.mustFindFirst ? 0 : StreamOpFlag.NOT_ORDERED);
        }

        @Override // java.util.stream.TerminalOp
        public StreamShape inputShape() {
            return this.shape;
        }

        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.stream.TerminalOp
        public <S> O evaluateSequential(PipelineHelper<T> pipelineHelper, Spliterator<S> spliterator) {
            O o2 = (O) ((TerminalSink) pipelineHelper.wrapAndCopyInto(this.sinkSupplier.get(), spliterator)).get();
            return o2 != null ? o2 : this.emptyValue;
        }

        @Override // java.util.stream.TerminalOp
        public <P_IN> O evaluateParallel(PipelineHelper<T> pipelineHelper, Spliterator<P_IN> spliterator) {
            return new FindTask(this, pipelineHelper, spliterator).invoke();
        }
    }

    /* loaded from: rt.jar:java/util/stream/FindOps$FindSink.class */
    private static abstract class FindSink<T, O> implements TerminalSink<T, O> {
        boolean hasValue;
        T value;

        FindSink() {
        }

        @Override // java.util.function.Consumer
        public void accept(T t2) {
            if (!this.hasValue) {
                this.hasValue = true;
                this.value = t2;
            }
        }

        @Override // java.util.stream.Sink
        public boolean cancellationRequested() {
            return this.hasValue;
        }

        /* loaded from: rt.jar:java/util/stream/FindOps$FindSink$OfRef.class */
        static final class OfRef<T> extends FindSink<T, Optional<T>> {
            OfRef() {
            }

            @Override // java.util.function.Supplier
            public Optional<T> get() {
                if (this.hasValue) {
                    return Optional.of(this.value);
                }
                return null;
            }
        }

        /* loaded from: rt.jar:java/util/stream/FindOps$FindSink$OfInt.class */
        static final class OfInt extends FindSink<Integer, OptionalInt> implements Sink.OfInt {
            OfInt() {
            }

            @Override // java.util.stream.Sink.OfInt
            public /* bridge */ /* synthetic */ void accept(Integer num) {
                super.accept((OfInt) num);
            }

            @Override // java.util.stream.Sink, java.util.stream.Sink.OfInt, java.util.function.IntConsumer
            public void accept(int i2) {
                accept((OfInt) Integer.valueOf(i2));
            }

            @Override // java.util.function.Supplier
            public OptionalInt get() {
                if (this.hasValue) {
                    return OptionalInt.of(((Integer) this.value).intValue());
                }
                return null;
            }
        }

        /* loaded from: rt.jar:java/util/stream/FindOps$FindSink$OfLong.class */
        static final class OfLong extends FindSink<Long, OptionalLong> implements Sink.OfLong {
            OfLong() {
            }

            @Override // java.util.stream.Sink.OfLong
            public /* bridge */ /* synthetic */ void accept(Long l2) {
                super.accept((OfLong) l2);
            }

            @Override // java.util.stream.Sink, java.util.stream.Sink.OfLong, java.util.function.LongConsumer
            public void accept(long j2) {
                accept((OfLong) Long.valueOf(j2));
            }

            @Override // java.util.function.Supplier
            public OptionalLong get() {
                if (this.hasValue) {
                    return OptionalLong.of(((Long) this.value).longValue());
                }
                return null;
            }
        }

        /* loaded from: rt.jar:java/util/stream/FindOps$FindSink$OfDouble.class */
        static final class OfDouble extends FindSink<Double, OptionalDouble> implements Sink.OfDouble {
            OfDouble() {
            }

            @Override // java.util.stream.Sink.OfDouble
            public /* bridge */ /* synthetic */ void accept(Double d2) {
                super.accept((OfDouble) d2);
            }

            @Override // java.util.stream.Sink, java.util.function.DoubleConsumer
            public void accept(double d2) {
                accept((OfDouble) Double.valueOf(d2));
            }

            @Override // java.util.function.Supplier
            public OptionalDouble get() {
                if (this.hasValue) {
                    return OptionalDouble.of(((Double) this.value).doubleValue());
                }
                return null;
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/FindOps$FindTask.class */
    private static final class FindTask<P_IN, P_OUT, O> extends AbstractShortCircuitTask<P_IN, P_OUT, O, FindTask<P_IN, P_OUT, O>> {
        private final FindOp<P_OUT, O> op;

        FindTask(FindOp<P_OUT, O> findOp, PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator) {
            super(pipelineHelper, spliterator);
            this.op = findOp;
        }

        FindTask(FindTask<P_IN, P_OUT, O> findTask, Spliterator<P_IN> spliterator) {
            super(findTask, spliterator);
            this.op = findTask.op;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.util.stream.AbstractTask
        public FindTask<P_IN, P_OUT, O> makeChild(Spliterator<P_IN> spliterator) {
            return new FindTask<>(this, spliterator);
        }

        @Override // java.util.stream.AbstractShortCircuitTask
        protected O getEmptyResult() {
            return this.op.emptyValue;
        }

        private void foundResult(O o2) {
            if (isLeftmostNode()) {
                shortCircuit(o2);
            } else {
                cancelLaterNodes();
            }
        }

        @Override // java.util.stream.AbstractTask
        protected O doLeaf() {
            O o2 = (O) ((TerminalSink) this.helper.wrapAndCopyInto(this.op.sinkSupplier.get(), this.spliterator)).get();
            if (!this.op.mustFindFirst) {
                if (o2 != null) {
                    shortCircuit(o2);
                    return null;
                }
                return null;
            }
            if (o2 != null) {
                foundResult(o2);
                return o2;
            }
            return null;
        }

        @Override // java.util.stream.AbstractTask, java.util.concurrent.CountedCompleter
        public void onCompletion(CountedCompleter<?> countedCompleter) {
            if (this.op.mustFindFirst) {
                FindTask findTask = (FindTask) this.leftChild;
                FindTask findTask2 = null;
                while (true) {
                    if (findTask != findTask2) {
                        O localResult = findTask.getLocalResult();
                        if (localResult == null || !this.op.presentPredicate.test(localResult)) {
                            findTask2 = findTask;
                            findTask = (FindTask) this.rightChild;
                        } else {
                            setLocalResult(localResult);
                            foundResult(localResult);
                            break;
                        }
                    } else {
                        break;
                    }
                }
            }
            super.onCompletion(countedCompleter);
        }
    }
}
