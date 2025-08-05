package java.util.stream;

import java.util.Objects;
import java.util.Spliterator;
import java.util.function.DoublePredicate;
import java.util.function.IntPredicate;
import java.util.function.LongPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Sink;

/* loaded from: rt.jar:java/util/stream/MatchOps.class */
final class MatchOps {
    private MatchOps() {
    }

    /* loaded from: rt.jar:java/util/stream/MatchOps$MatchKind.class */
    enum MatchKind {
        ANY(true, true),
        ALL(false, false),
        NONE(true, false);

        private final boolean stopOnPredicateMatches;
        private final boolean shortCircuitResult;

        MatchKind(boolean z2, boolean z3) {
            this.stopOnPredicateMatches = z2;
            this.shortCircuitResult = z3;
        }
    }

    public static <T> TerminalOp<T, Boolean> makeRef(Predicate<? super T> predicate, MatchKind matchKind) {
        Objects.requireNonNull(predicate);
        Objects.requireNonNull(matchKind);
        return new MatchOp(StreamShape.REFERENCE, matchKind, () -> {
            return new BooleanTerminalSink<T>(predicate) { // from class: java.util.stream.MatchOps.1MatchSink
                final /* synthetic */ Predicate val$predicate;

                /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
                {
                    super(this.val$matchKind);
                    this.val$predicate = predicate;
                }

                @Override // java.util.function.Consumer
                public void accept(T t2) {
                    if (!this.stop && this.val$predicate.test(t2) == this.val$matchKind.stopOnPredicateMatches) {
                        this.stop = true;
                        this.value = this.val$matchKind.shortCircuitResult;
                    }
                }
            };
        });
    }

    public static TerminalOp<Integer, Boolean> makeInt(IntPredicate intPredicate, MatchKind matchKind) {
        Objects.requireNonNull(intPredicate);
        Objects.requireNonNull(matchKind);
        return new MatchOp(StreamShape.INT_VALUE, matchKind, () -> {
            return new C2MatchSink(matchKind, intPredicate);
        });
    }

    /* renamed from: java.util.stream.MatchOps$2MatchSink, reason: invalid class name */
    /* loaded from: rt.jar:java/util/stream/MatchOps$2MatchSink.class */
    class C2MatchSink extends BooleanTerminalSink<Integer> implements Sink.OfInt {
        final /* synthetic */ MatchKind val$matchKind;
        final /* synthetic */ IntPredicate val$predicate;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C2MatchSink(MatchKind matchKind, IntPredicate intPredicate) {
            super(matchKind);
            this.val$matchKind = matchKind;
            this.val$predicate = intPredicate;
        }

        @Override // java.util.stream.Sink, java.util.stream.Sink.OfInt, java.util.function.IntConsumer
        public void accept(int i2) {
            if (!this.stop && this.val$predicate.test(i2) == this.val$matchKind.stopOnPredicateMatches) {
                this.stop = true;
                this.value = this.val$matchKind.shortCircuitResult;
            }
        }
    }

    public static TerminalOp<Long, Boolean> makeLong(LongPredicate longPredicate, MatchKind matchKind) {
        Objects.requireNonNull(longPredicate);
        Objects.requireNonNull(matchKind);
        return new MatchOp(StreamShape.LONG_VALUE, matchKind, () -> {
            return new C3MatchSink(matchKind, longPredicate);
        });
    }

    /* renamed from: java.util.stream.MatchOps$3MatchSink, reason: invalid class name */
    /* loaded from: rt.jar:java/util/stream/MatchOps$3MatchSink.class */
    class C3MatchSink extends BooleanTerminalSink<Long> implements Sink.OfLong {
        final /* synthetic */ MatchKind val$matchKind;
        final /* synthetic */ LongPredicate val$predicate;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C3MatchSink(MatchKind matchKind, LongPredicate longPredicate) {
            super(matchKind);
            this.val$matchKind = matchKind;
            this.val$predicate = longPredicate;
        }

        @Override // java.util.stream.Sink, java.util.stream.Sink.OfLong, java.util.function.LongConsumer
        public void accept(long j2) {
            if (!this.stop && this.val$predicate.test(j2) == this.val$matchKind.stopOnPredicateMatches) {
                this.stop = true;
                this.value = this.val$matchKind.shortCircuitResult;
            }
        }
    }

    public static TerminalOp<Double, Boolean> makeDouble(DoublePredicate doublePredicate, MatchKind matchKind) {
        Objects.requireNonNull(doublePredicate);
        Objects.requireNonNull(matchKind);
        return new MatchOp(StreamShape.DOUBLE_VALUE, matchKind, () -> {
            return new C4MatchSink(matchKind, doublePredicate);
        });
    }

    /* renamed from: java.util.stream.MatchOps$4MatchSink, reason: invalid class name */
    /* loaded from: rt.jar:java/util/stream/MatchOps$4MatchSink.class */
    class C4MatchSink extends BooleanTerminalSink<Double> implements Sink.OfDouble {
        final /* synthetic */ MatchKind val$matchKind;
        final /* synthetic */ DoublePredicate val$predicate;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        C4MatchSink(MatchKind matchKind, DoublePredicate doublePredicate) {
            super(matchKind);
            this.val$matchKind = matchKind;
            this.val$predicate = doublePredicate;
        }

        @Override // java.util.stream.Sink, java.util.function.DoubleConsumer
        public void accept(double d2) {
            if (!this.stop && this.val$predicate.test(d2) == this.val$matchKind.stopOnPredicateMatches) {
                this.stop = true;
                this.value = this.val$matchKind.shortCircuitResult;
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/MatchOps$MatchOp.class */
    private static final class MatchOp<T> implements TerminalOp<T, Boolean> {
        private final StreamShape inputShape;
        final MatchKind matchKind;
        final Supplier<BooleanTerminalSink<T>> sinkSupplier;

        MatchOp(StreamShape streamShape, MatchKind matchKind, Supplier<BooleanTerminalSink<T>> supplier) {
            this.inputShape = streamShape;
            this.matchKind = matchKind;
            this.sinkSupplier = supplier;
        }

        @Override // java.util.stream.TerminalOp
        public int getOpFlags() {
            return StreamOpFlag.IS_SHORT_CIRCUIT | StreamOpFlag.NOT_ORDERED;
        }

        @Override // java.util.stream.TerminalOp
        public StreamShape inputShape() {
            return this.inputShape;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.stream.TerminalOp
        public <S> Boolean evaluateSequential(PipelineHelper<T> pipelineHelper, Spliterator<S> spliterator) {
            return Boolean.valueOf(((BooleanTerminalSink) pipelineHelper.wrapAndCopyInto(this.sinkSupplier.get(), spliterator)).getAndClearState());
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.stream.TerminalOp
        public <S> Boolean evaluateParallel(PipelineHelper<T> pipelineHelper, Spliterator<S> spliterator) {
            return new MatchTask(this, pipelineHelper, spliterator).invoke();
        }
    }

    /* loaded from: rt.jar:java/util/stream/MatchOps$BooleanTerminalSink.class */
    private static abstract class BooleanTerminalSink<T> implements Sink<T> {
        boolean stop;
        boolean value;

        BooleanTerminalSink(MatchKind matchKind) {
            this.value = !matchKind.shortCircuitResult;
        }

        public boolean getAndClearState() {
            return this.value;
        }

        @Override // java.util.stream.Sink
        public boolean cancellationRequested() {
            return this.stop;
        }
    }

    /* loaded from: rt.jar:java/util/stream/MatchOps$MatchTask.class */
    private static final class MatchTask<P_IN, P_OUT> extends AbstractShortCircuitTask<P_IN, P_OUT, Boolean, MatchTask<P_IN, P_OUT>> {
        private final MatchOp<P_OUT> op;

        MatchTask(MatchOp<P_OUT> matchOp, PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator) {
            super(pipelineHelper, spliterator);
            this.op = matchOp;
        }

        MatchTask(MatchTask<P_IN, P_OUT> matchTask, Spliterator<P_IN> spliterator) {
            super(matchTask, spliterator);
            this.op = matchTask.op;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.util.stream.AbstractTask
        public MatchTask<P_IN, P_OUT> makeChild(Spliterator<P_IN> spliterator) {
            return new MatchTask<>(this, spliterator);
        }

        /* JADX INFO: Access modifiers changed from: protected */
        @Override // java.util.stream.AbstractTask
        public Boolean doLeaf() {
            boolean andClearState = ((BooleanTerminalSink) this.helper.wrapAndCopyInto(this.op.sinkSupplier.get(), this.spliterator)).getAndClearState();
            if (andClearState == this.op.matchKind.shortCircuitResult) {
                shortCircuit(Boolean.valueOf(andClearState));
                return null;
            }
            return null;
        }

        /* JADX INFO: Access modifiers changed from: protected */
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.stream.AbstractShortCircuitTask
        public Boolean getEmptyResult() {
            return Boolean.valueOf(!this.op.matchKind.shortCircuitResult);
        }
    }
}
