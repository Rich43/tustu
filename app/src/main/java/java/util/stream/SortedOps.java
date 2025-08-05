package java.util.stream;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.IntFunction;
import java.util.stream.DoublePipeline;
import java.util.stream.IntPipeline;
import java.util.stream.LongPipeline;
import java.util.stream.Node;
import java.util.stream.ReferencePipeline;
import java.util.stream.Sink;
import java.util.stream.SpinedBuffer;

/* loaded from: rt.jar:java/util/stream/SortedOps.class */
final class SortedOps {
    private SortedOps() {
    }

    static <T> Stream<T> makeRef(AbstractPipeline<?, T, ?> abstractPipeline) {
        return new OfRef(abstractPipeline);
    }

    static <T> Stream<T> makeRef(AbstractPipeline<?, T, ?> abstractPipeline, Comparator<? super T> comparator) {
        return new OfRef(abstractPipeline, comparator);
    }

    static <T> IntStream makeInt(AbstractPipeline<?, Integer, ?> abstractPipeline) {
        return new OfInt(abstractPipeline);
    }

    static <T> LongStream makeLong(AbstractPipeline<?, Long, ?> abstractPipeline) {
        return new OfLong(abstractPipeline);
    }

    static <T> DoubleStream makeDouble(AbstractPipeline<?, Double, ?> abstractPipeline) {
        return new OfDouble(abstractPipeline);
    }

    /* loaded from: rt.jar:java/util/stream/SortedOps$OfRef.class */
    private static final class OfRef<T> extends ReferencePipeline.StatefulOp<T, T> {
        private final boolean isNaturalSort;
        private final Comparator<? super T> comparator;

        OfRef(AbstractPipeline<?, T, ?> abstractPipeline) {
            super(abstractPipeline, StreamShape.REFERENCE, StreamOpFlag.IS_ORDERED | StreamOpFlag.IS_SORTED);
            this.isNaturalSort = true;
            this.comparator = Comparator.naturalOrder();
        }

        OfRef(AbstractPipeline<?, T, ?> abstractPipeline, Comparator<? super T> comparator) {
            super(abstractPipeline, StreamShape.REFERENCE, StreamOpFlag.IS_ORDERED | StreamOpFlag.NOT_SORTED);
            this.isNaturalSort = false;
            this.comparator = (Comparator) Objects.requireNonNull(comparator);
        }

        @Override // java.util.stream.AbstractPipeline
        public Sink<T> opWrapSink(int i2, Sink<T> sink) {
            Objects.requireNonNull(sink);
            if (StreamOpFlag.SORTED.isKnown(i2) && this.isNaturalSort) {
                return sink;
            }
            if (StreamOpFlag.SIZED.isKnown(i2)) {
                return new SizedRefSortingSink(sink, this.comparator);
            }
            return new RefSortingSink(sink, this.comparator);
        }

        @Override // java.util.stream.ReferencePipeline.StatefulOp, java.util.stream.AbstractPipeline
        public <P_IN> Node<T> opEvaluateParallel(PipelineHelper<T> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<T[]> intFunction) {
            if (StreamOpFlag.SORTED.isKnown(pipelineHelper.getStreamAndOpFlags()) && this.isNaturalSort) {
                return pipelineHelper.evaluate(spliterator, false, intFunction);
            }
            T[] tArrAsArray = pipelineHelper.evaluate(spliterator, true, intFunction).asArray(intFunction);
            Arrays.parallelSort(tArrAsArray, this.comparator);
            return Nodes.node(tArrAsArray);
        }
    }

    /* loaded from: rt.jar:java/util/stream/SortedOps$OfInt.class */
    private static final class OfInt extends IntPipeline.StatefulOp<Integer> {
        OfInt(AbstractPipeline<?, Integer, ?> abstractPipeline) {
            super(abstractPipeline, StreamShape.INT_VALUE, StreamOpFlag.IS_ORDERED | StreamOpFlag.IS_SORTED);
        }

        @Override // java.util.stream.AbstractPipeline
        public Sink<Integer> opWrapSink(int i2, Sink<Integer> sink) {
            Objects.requireNonNull(sink);
            if (StreamOpFlag.SORTED.isKnown(i2)) {
                return sink;
            }
            if (StreamOpFlag.SIZED.isKnown(i2)) {
                return new SizedIntSortingSink(sink);
            }
            return new IntSortingSink(sink);
        }

        @Override // java.util.stream.IntPipeline.StatefulOp, java.util.stream.AbstractPipeline
        public <P_IN> Node<Integer> opEvaluateParallel(PipelineHelper<Integer> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<Integer[]> intFunction) {
            if (StreamOpFlag.SORTED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                return pipelineHelper.evaluate(spliterator, false, intFunction);
            }
            int[] iArrAsPrimitiveArray = ((Node.OfInt) pipelineHelper.evaluate(spliterator, true, intFunction)).asPrimitiveArray();
            Arrays.parallelSort(iArrAsPrimitiveArray);
            return Nodes.node(iArrAsPrimitiveArray);
        }
    }

    /* loaded from: rt.jar:java/util/stream/SortedOps$OfLong.class */
    private static final class OfLong extends LongPipeline.StatefulOp<Long> {
        OfLong(AbstractPipeline<?, Long, ?> abstractPipeline) {
            super(abstractPipeline, StreamShape.LONG_VALUE, StreamOpFlag.IS_ORDERED | StreamOpFlag.IS_SORTED);
        }

        @Override // java.util.stream.AbstractPipeline
        public Sink<Long> opWrapSink(int i2, Sink<Long> sink) {
            Objects.requireNonNull(sink);
            if (StreamOpFlag.SORTED.isKnown(i2)) {
                return sink;
            }
            if (StreamOpFlag.SIZED.isKnown(i2)) {
                return new SizedLongSortingSink(sink);
            }
            return new LongSortingSink(sink);
        }

        @Override // java.util.stream.LongPipeline.StatefulOp, java.util.stream.AbstractPipeline
        public <P_IN> Node<Long> opEvaluateParallel(PipelineHelper<Long> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<Long[]> intFunction) {
            if (StreamOpFlag.SORTED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                return pipelineHelper.evaluate(spliterator, false, intFunction);
            }
            long[] jArrAsPrimitiveArray = ((Node.OfLong) pipelineHelper.evaluate(spliterator, true, intFunction)).asPrimitiveArray();
            Arrays.parallelSort(jArrAsPrimitiveArray);
            return Nodes.node(jArrAsPrimitiveArray);
        }
    }

    /* loaded from: rt.jar:java/util/stream/SortedOps$OfDouble.class */
    private static final class OfDouble extends DoublePipeline.StatefulOp<Double> {
        OfDouble(AbstractPipeline<?, Double, ?> abstractPipeline) {
            super(abstractPipeline, StreamShape.DOUBLE_VALUE, StreamOpFlag.IS_ORDERED | StreamOpFlag.IS_SORTED);
        }

        @Override // java.util.stream.AbstractPipeline
        public Sink<Double> opWrapSink(int i2, Sink<Double> sink) {
            Objects.requireNonNull(sink);
            if (StreamOpFlag.SORTED.isKnown(i2)) {
                return sink;
            }
            if (StreamOpFlag.SIZED.isKnown(i2)) {
                return new SizedDoubleSortingSink(sink);
            }
            return new DoubleSortingSink(sink);
        }

        @Override // java.util.stream.DoublePipeline.StatefulOp, java.util.stream.AbstractPipeline
        public <P_IN> Node<Double> opEvaluateParallel(PipelineHelper<Double> pipelineHelper, Spliterator<P_IN> spliterator, IntFunction<Double[]> intFunction) {
            if (StreamOpFlag.SORTED.isKnown(pipelineHelper.getStreamAndOpFlags())) {
                return pipelineHelper.evaluate(spliterator, false, intFunction);
            }
            double[] dArrAsPrimitiveArray = ((Node.OfDouble) pipelineHelper.evaluate(spliterator, true, intFunction)).asPrimitiveArray();
            Arrays.parallelSort(dArrAsPrimitiveArray);
            return Nodes.node(dArrAsPrimitiveArray);
        }
    }

    /* loaded from: rt.jar:java/util/stream/SortedOps$AbstractRefSortingSink.class */
    private static abstract class AbstractRefSortingSink<T> extends Sink.ChainedReference<T, T> {
        protected final Comparator<? super T> comparator;
        protected boolean cancellationRequestedCalled;

        AbstractRefSortingSink(Sink<? super T> sink, Comparator<? super T> comparator) {
            super(sink);
            this.comparator = comparator;
        }

        @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
        public final boolean cancellationRequested() {
            this.cancellationRequestedCalled = true;
            return false;
        }
    }

    /* loaded from: rt.jar:java/util/stream/SortedOps$SizedRefSortingSink.class */
    private static final class SizedRefSortingSink<T> extends AbstractRefSortingSink<T> {
        private T[] array;
        private int offset;

        SizedRefSortingSink(Sink<? super T> sink, Comparator<? super T> comparator) {
            super(sink, comparator);
        }

        @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
        public void begin(long j2) {
            if (j2 >= 2147483639) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.array = (T[]) new Object[(int) j2];
        }

        @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
        public void end() {
            Arrays.sort(this.array, 0, this.offset, this.comparator);
            this.downstream.begin(this.offset);
            if (!this.cancellationRequestedCalled) {
                for (int i2 = 0; i2 < this.offset; i2++) {
                    this.downstream.accept(this.array[i2]);
                }
            } else {
                for (int i3 = 0; i3 < this.offset && !this.downstream.cancellationRequested(); i3++) {
                    this.downstream.accept(this.array[i3]);
                }
            }
            this.downstream.end();
            this.array = null;
        }

        @Override // java.util.function.Consumer
        public void accept(T t2) {
            T[] tArr = this.array;
            int i2 = this.offset;
            this.offset = i2 + 1;
            tArr[i2] = t2;
        }
    }

    /* loaded from: rt.jar:java/util/stream/SortedOps$RefSortingSink.class */
    private static final class RefSortingSink<T> extends AbstractRefSortingSink<T> {
        private ArrayList<T> list;

        RefSortingSink(Sink<? super T> sink, Comparator<? super T> comparator) {
            super(sink, comparator);
        }

        @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
        public void begin(long j2) {
            if (j2 >= 2147483639) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.list = j2 >= 0 ? new ArrayList<>((int) j2) : new ArrayList<>();
        }

        @Override // java.util.stream.Sink.ChainedReference, java.util.stream.Sink
        public void end() {
            this.list.sort(this.comparator);
            this.downstream.begin(this.list.size());
            if (!this.cancellationRequestedCalled) {
                ArrayList<T> arrayList = this.list;
                Sink<? super E_OUT> sink = this.downstream;
                sink.getClass();
                arrayList.forEach(sink::accept);
            } else {
                Iterator<T> it = this.list.iterator();
                while (it.hasNext()) {
                    T next = it.next();
                    if (this.downstream.cancellationRequested()) {
                        break;
                    } else {
                        this.downstream.accept(next);
                    }
                }
            }
            this.downstream.end();
            this.list = null;
        }

        @Override // java.util.function.Consumer
        public void accept(T t2) {
            this.list.add(t2);
        }
    }

    /* loaded from: rt.jar:java/util/stream/SortedOps$AbstractIntSortingSink.class */
    private static abstract class AbstractIntSortingSink extends Sink.ChainedInt<Integer> {
        protected boolean cancellationRequestedCalled;

        AbstractIntSortingSink(Sink<? super Integer> sink) {
            super(sink);
        }

        @Override // java.util.stream.Sink.ChainedInt, java.util.stream.Sink
        public final boolean cancellationRequested() {
            this.cancellationRequestedCalled = true;
            return false;
        }
    }

    /* loaded from: rt.jar:java/util/stream/SortedOps$SizedIntSortingSink.class */
    private static final class SizedIntSortingSink extends AbstractIntSortingSink {
        private int[] array;
        private int offset;

        SizedIntSortingSink(Sink<? super Integer> sink) {
            super(sink);
        }

        @Override // java.util.stream.Sink.ChainedInt, java.util.stream.Sink
        public void begin(long j2) {
            if (j2 >= 2147483639) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.array = new int[(int) j2];
        }

        @Override // java.util.stream.Sink.ChainedInt, java.util.stream.Sink
        public void end() {
            Arrays.sort(this.array, 0, this.offset);
            this.downstream.begin(this.offset);
            if (!this.cancellationRequestedCalled) {
                for (int i2 = 0; i2 < this.offset; i2++) {
                    this.downstream.accept(this.array[i2]);
                }
            } else {
                for (int i3 = 0; i3 < this.offset && !this.downstream.cancellationRequested(); i3++) {
                    this.downstream.accept(this.array[i3]);
                }
            }
            this.downstream.end();
            this.array = null;
        }

        @Override // java.util.stream.Sink.OfInt, java.util.function.IntConsumer
        public void accept(int i2) {
            int[] iArr = this.array;
            int i3 = this.offset;
            this.offset = i3 + 1;
            iArr[i3] = i2;
        }
    }

    /* loaded from: rt.jar:java/util/stream/SortedOps$IntSortingSink.class */
    private static final class IntSortingSink extends AbstractIntSortingSink {

        /* renamed from: b, reason: collision with root package name */
        private SpinedBuffer.OfInt f12611b;

        IntSortingSink(Sink<? super Integer> sink) {
            super(sink);
        }

        @Override // java.util.stream.Sink.ChainedInt, java.util.stream.Sink
        public void begin(long j2) {
            if (j2 >= 2147483639) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.f12611b = j2 > 0 ? new SpinedBuffer.OfInt((int) j2) : new SpinedBuffer.OfInt();
        }

        @Override // java.util.stream.Sink.ChainedInt, java.util.stream.Sink
        public void end() {
            int[] iArrAsPrimitiveArray = this.f12611b.asPrimitiveArray();
            Arrays.sort(iArrAsPrimitiveArray);
            this.downstream.begin(iArrAsPrimitiveArray.length);
            if (this.cancellationRequestedCalled) {
                for (int i2 : iArrAsPrimitiveArray) {
                    if (this.downstream.cancellationRequested()) {
                        break;
                    }
                    this.downstream.accept(i2);
                }
            } else {
                for (int i3 : iArrAsPrimitiveArray) {
                    this.downstream.accept(i3);
                }
            }
            this.downstream.end();
        }

        @Override // java.util.stream.Sink.OfInt, java.util.function.IntConsumer
        public void accept(int i2) {
            this.f12611b.accept(i2);
        }
    }

    /* loaded from: rt.jar:java/util/stream/SortedOps$AbstractLongSortingSink.class */
    private static abstract class AbstractLongSortingSink extends Sink.ChainedLong<Long> {
        protected boolean cancellationRequestedCalled;

        AbstractLongSortingSink(Sink<? super Long> sink) {
            super(sink);
        }

        @Override // java.util.stream.Sink.ChainedLong, java.util.stream.Sink
        public final boolean cancellationRequested() {
            this.cancellationRequestedCalled = true;
            return false;
        }
    }

    /* loaded from: rt.jar:java/util/stream/SortedOps$SizedLongSortingSink.class */
    private static final class SizedLongSortingSink extends AbstractLongSortingSink {
        private long[] array;
        private int offset;

        SizedLongSortingSink(Sink<? super Long> sink) {
            super(sink);
        }

        @Override // java.util.stream.Sink.ChainedLong, java.util.stream.Sink
        public void begin(long j2) {
            if (j2 >= 2147483639) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.array = new long[(int) j2];
        }

        @Override // java.util.stream.Sink.ChainedLong, java.util.stream.Sink
        public void end() {
            Arrays.sort(this.array, 0, this.offset);
            this.downstream.begin(this.offset);
            if (!this.cancellationRequestedCalled) {
                for (int i2 = 0; i2 < this.offset; i2++) {
                    this.downstream.accept(this.array[i2]);
                }
            } else {
                for (int i3 = 0; i3 < this.offset && !this.downstream.cancellationRequested(); i3++) {
                    this.downstream.accept(this.array[i3]);
                }
            }
            this.downstream.end();
            this.array = null;
        }

        @Override // java.util.stream.Sink.OfLong, java.util.function.LongConsumer
        public void accept(long j2) {
            long[] jArr = this.array;
            int i2 = this.offset;
            this.offset = i2 + 1;
            jArr[i2] = j2;
        }
    }

    /* loaded from: rt.jar:java/util/stream/SortedOps$LongSortingSink.class */
    private static final class LongSortingSink extends AbstractLongSortingSink {

        /* renamed from: b, reason: collision with root package name */
        private SpinedBuffer.OfLong f12612b;

        LongSortingSink(Sink<? super Long> sink) {
            super(sink);
        }

        @Override // java.util.stream.Sink.ChainedLong, java.util.stream.Sink
        public void begin(long j2) {
            if (j2 >= 2147483639) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.f12612b = j2 > 0 ? new SpinedBuffer.OfLong((int) j2) : new SpinedBuffer.OfLong();
        }

        @Override // java.util.stream.Sink.ChainedLong, java.util.stream.Sink
        public void end() {
            long[] jArrAsPrimitiveArray = this.f12612b.asPrimitiveArray();
            Arrays.sort(jArrAsPrimitiveArray);
            this.downstream.begin(jArrAsPrimitiveArray.length);
            if (this.cancellationRequestedCalled) {
                for (long j2 : jArrAsPrimitiveArray) {
                    if (this.downstream.cancellationRequested()) {
                        break;
                    }
                    this.downstream.accept(j2);
                }
            } else {
                for (long j3 : jArrAsPrimitiveArray) {
                    this.downstream.accept(j3);
                }
            }
            this.downstream.end();
        }

        @Override // java.util.stream.Sink.OfLong, java.util.function.LongConsumer
        public void accept(long j2) {
            this.f12612b.accept(j2);
        }
    }

    /* loaded from: rt.jar:java/util/stream/SortedOps$AbstractDoubleSortingSink.class */
    private static abstract class AbstractDoubleSortingSink extends Sink.ChainedDouble<Double> {
        protected boolean cancellationRequestedCalled;

        AbstractDoubleSortingSink(Sink<? super Double> sink) {
            super(sink);
        }

        @Override // java.util.stream.Sink.ChainedDouble, java.util.stream.Sink
        public final boolean cancellationRequested() {
            this.cancellationRequestedCalled = true;
            return false;
        }
    }

    /* loaded from: rt.jar:java/util/stream/SortedOps$SizedDoubleSortingSink.class */
    private static final class SizedDoubleSortingSink extends AbstractDoubleSortingSink {
        private double[] array;
        private int offset;

        SizedDoubleSortingSink(Sink<? super Double> sink) {
            super(sink);
        }

        @Override // java.util.stream.Sink.ChainedDouble, java.util.stream.Sink
        public void begin(long j2) {
            if (j2 >= 2147483639) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.array = new double[(int) j2];
        }

        @Override // java.util.stream.Sink.ChainedDouble, java.util.stream.Sink
        public void end() {
            Arrays.sort(this.array, 0, this.offset);
            this.downstream.begin(this.offset);
            if (!this.cancellationRequestedCalled) {
                for (int i2 = 0; i2 < this.offset; i2++) {
                    this.downstream.accept(this.array[i2]);
                }
            } else {
                for (int i3 = 0; i3 < this.offset && !this.downstream.cancellationRequested(); i3++) {
                    this.downstream.accept(this.array[i3]);
                }
            }
            this.downstream.end();
            this.array = null;
        }

        @Override // java.util.stream.Sink.OfDouble, java.util.stream.Sink, java.util.function.DoubleConsumer
        public void accept(double d2) {
            double[] dArr = this.array;
            int i2 = this.offset;
            this.offset = i2 + 1;
            dArr[i2] = d2;
        }
    }

    /* loaded from: rt.jar:java/util/stream/SortedOps$DoubleSortingSink.class */
    private static final class DoubleSortingSink extends AbstractDoubleSortingSink {

        /* renamed from: b, reason: collision with root package name */
        private SpinedBuffer.OfDouble f12610b;

        DoubleSortingSink(Sink<? super Double> sink) {
            super(sink);
        }

        @Override // java.util.stream.Sink.ChainedDouble, java.util.stream.Sink
        public void begin(long j2) {
            if (j2 >= 2147483639) {
                throw new IllegalArgumentException("Stream size exceeds max array size");
            }
            this.f12610b = j2 > 0 ? new SpinedBuffer.OfDouble((int) j2) : new SpinedBuffer.OfDouble();
        }

        @Override // java.util.stream.Sink.ChainedDouble, java.util.stream.Sink
        public void end() {
            double[] dArrAsPrimitiveArray = this.f12610b.asPrimitiveArray();
            Arrays.sort(dArrAsPrimitiveArray);
            this.downstream.begin(dArrAsPrimitiveArray.length);
            if (this.cancellationRequestedCalled) {
                for (double d2 : dArrAsPrimitiveArray) {
                    if (this.downstream.cancellationRequested()) {
                        break;
                    }
                    this.downstream.accept(d2);
                }
            } else {
                for (double d3 : dArrAsPrimitiveArray) {
                    this.downstream.accept(d3);
                }
            }
            this.downstream.end();
        }

        @Override // java.util.stream.Sink.OfDouble, java.util.stream.Sink, java.util.function.DoubleConsumer
        public void accept(double d2) {
            this.f12610b.accept(d2);
        }
    }
}
