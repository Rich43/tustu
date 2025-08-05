package java.util.stream;

import java.util.Comparator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.IntConsumer;
import java.util.function.LongConsumer;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.SpinedBuffer;
import java.util.stream.Stream;
import sun.security.pkcs11.wrapper.PKCS11Constants;

/* loaded from: rt.jar:java/util/stream/Streams.class */
final class Streams {
    static final Object NONE = new Object();

    private Streams() {
        throw new Error("no instances");
    }

    /* loaded from: rt.jar:java/util/stream/Streams$RangeIntSpliterator.class */
    static final class RangeIntSpliterator implements Spliterator.OfInt {
        private int from;
        private final int upTo;
        private int last;
        private static final int BALANCED_SPLIT_THRESHOLD = 16777216;
        private static final int RIGHT_BALANCED_SPLIT_RATIO = 8;

        RangeIntSpliterator(int i2, int i3, boolean z2) {
            this(i2, i3, z2 ? 1 : 0);
        }

        private RangeIntSpliterator(int i2, int i3, int i4) {
            this.from = i2;
            this.upTo = i3;
            this.last = i4;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(IntConsumer intConsumer) {
            Objects.requireNonNull(intConsumer);
            int i2 = this.from;
            if (i2 < this.upTo) {
                this.from++;
                intConsumer.accept(i2);
                return true;
            }
            if (this.last > 0) {
                this.last = 0;
                intConsumer.accept(i2);
                return true;
            }
            return false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive
        public void forEachRemaining(IntConsumer intConsumer) {
            Objects.requireNonNull(intConsumer);
            int i2 = this.from;
            int i3 = this.upTo;
            int i4 = this.last;
            this.from = this.upTo;
            this.last = 0;
            while (i2 < i3) {
                int i5 = i2;
                i2++;
                intConsumer.accept(i5);
            }
            if (i4 > 0) {
                intConsumer.accept(i2);
            }
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return (this.upTo - this.from) + this.last;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 17749;
        }

        @Override // java.util.Spliterator
        public Comparator<? super Integer> getComparator() {
            return null;
        }

        @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public Spliterator.OfInt trySplit() {
            long jEstimateSize = estimateSize();
            if (jEstimateSize <= 1) {
                return null;
            }
            int i2 = this.from;
            int iSplitPoint = this.from + splitPoint(jEstimateSize);
            this.from = iSplitPoint;
            return new RangeIntSpliterator(i2, iSplitPoint, 0);
        }

        private int splitPoint(long j2) {
            return (int) (j2 / (j2 < PKCS11Constants.CKF_EC_UNCOMPRESS ? 2 : 8));
        }
    }

    /* loaded from: rt.jar:java/util/stream/Streams$RangeLongSpliterator.class */
    static final class RangeLongSpliterator implements Spliterator.OfLong {
        private long from;
        private final long upTo;
        private int last;
        private static final long BALANCED_SPLIT_THRESHOLD = 16777216;
        private static final long RIGHT_BALANCED_SPLIT_RATIO = 8;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Streams.class.desiredAssertionStatus();
        }

        RangeLongSpliterator(long j2, long j3, boolean z2) {
            this(j2, j3, z2 ? 1 : 0);
        }

        private RangeLongSpliterator(long j2, long j3, int i2) {
            if (!$assertionsDisabled && (j3 - j2) + i2 <= 0) {
                throw new AssertionError();
            }
            this.from = j2;
            this.upTo = j3;
            this.last = i2;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(LongConsumer longConsumer) {
            Objects.requireNonNull(longConsumer);
            long j2 = this.from;
            if (j2 < this.upTo) {
                this.from++;
                longConsumer.accept(j2);
                return true;
            }
            if (this.last > 0) {
                this.last = 0;
                longConsumer.accept(j2);
                return true;
            }
            return false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        /* JADX WARN: Multi-variable type inference failed */
        @Override // java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive
        public void forEachRemaining(LongConsumer longConsumer) {
            Objects.requireNonNull(longConsumer);
            long j2 = this.from;
            long j3 = this.upTo;
            int i2 = this.last;
            this.from = this.upTo;
            this.last = 0;
            while (j2 < j3) {
                j2++;
                longConsumer.accept(longConsumer);
            }
            if (i2 > 0) {
                longConsumer.accept(j2);
            }
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return (this.upTo - this.from) + this.last;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 17749;
        }

        @Override // java.util.Spliterator
        public Comparator<? super Long> getComparator() {
            return null;
        }

        @Override // java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive, java.util.Spliterator
        public Spliterator.OfLong trySplit() {
            long jEstimateSize = estimateSize();
            if (jEstimateSize <= 1) {
                return null;
            }
            long j2 = this.from;
            long jSplitPoint = this.from + splitPoint(jEstimateSize);
            this.from = jSplitPoint;
            return new RangeLongSpliterator(j2, jSplitPoint, 0);
        }

        private long splitPoint(long j2) {
            return j2 / (j2 < 16777216 ? 2L : 8L);
        }
    }

    /* loaded from: rt.jar:java/util/stream/Streams$AbstractStreamBuilderImpl.class */
    private static abstract class AbstractStreamBuilderImpl<T, S extends Spliterator<T>> implements Spliterator<T> {
        int count;

        private AbstractStreamBuilderImpl() {
        }

        @Override // java.util.Spliterator
        public S trySplit() {
            return null;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return (-this.count) - 1;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 17488;
        }
    }

    /* loaded from: rt.jar:java/util/stream/Streams$StreamBuilderImpl.class */
    static final class StreamBuilderImpl<T> extends AbstractStreamBuilderImpl<T, Spliterator<T>> implements Stream.Builder<T> {
        T first;
        SpinedBuffer<T> buffer;

        StreamBuilderImpl() {
            super();
        }

        StreamBuilderImpl(T t2) {
            super();
            this.first = t2;
            this.count = -2;
        }

        @Override // java.util.stream.Stream.Builder, java.util.function.Consumer
        public void accept(T t2) {
            if (this.count == 0) {
                this.first = t2;
                this.count++;
            } else {
                if (this.count > 0) {
                    if (this.buffer == null) {
                        this.buffer = new SpinedBuffer<>();
                        this.buffer.accept(this.first);
                        this.count++;
                    }
                    this.buffer.accept(t2);
                    return;
                }
                throw new IllegalStateException();
            }
        }

        @Override // java.util.stream.Stream.Builder
        public Stream.Builder<T> add(T t2) {
            accept(t2);
            return this;
        }

        @Override // java.util.stream.Stream.Builder
        public Stream<T> build() {
            int i2 = this.count;
            if (i2 >= 0) {
                this.count = (-this.count) - 1;
                return i2 < 2 ? StreamSupport.stream(this, false) : StreamSupport.stream(this.buffer.spliterator(), false);
            }
            throw new IllegalStateException();
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super T> consumer) {
            Objects.requireNonNull(consumer);
            if (this.count == -2) {
                consumer.accept(this.first);
                this.count = -1;
                return true;
            }
            return false;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super T> consumer) {
            Objects.requireNonNull(consumer);
            if (this.count == -2) {
                consumer.accept(this.first);
                this.count = -1;
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/Streams$IntStreamBuilderImpl.class */
    static final class IntStreamBuilderImpl extends AbstractStreamBuilderImpl<Integer, Spliterator.OfInt> implements IntStream.Builder, Spliterator.OfInt {
        int first;
        SpinedBuffer.OfInt buffer;

        @Override // java.util.stream.Streams.AbstractStreamBuilderImpl, java.util.Spliterator
        public /* bridge */ /* synthetic */ Spliterator.OfInt trySplit() {
            return (Spliterator.OfInt) super.trySplit();
        }

        @Override // java.util.stream.Streams.AbstractStreamBuilderImpl, java.util.Spliterator
        public /* bridge */ /* synthetic */ Spliterator.OfPrimitive trySplit() {
            return (Spliterator.OfPrimitive) super.trySplit();
        }

        IntStreamBuilderImpl() {
            super();
        }

        IntStreamBuilderImpl(int i2) {
            super();
            this.first = i2;
            this.count = -2;
        }

        @Override // java.util.stream.IntStream.Builder, java.util.function.IntConsumer
        public void accept(int i2) {
            if (this.count == 0) {
                this.first = i2;
                this.count++;
            } else {
                if (this.count > 0) {
                    if (this.buffer == null) {
                        this.buffer = new SpinedBuffer.OfInt();
                        this.buffer.accept(this.first);
                        this.count++;
                    }
                    this.buffer.accept(i2);
                    return;
                }
                throw new IllegalStateException();
            }
        }

        @Override // java.util.stream.IntStream.Builder
        public IntStream build() {
            int i2 = this.count;
            if (i2 >= 0) {
                this.count = (-this.count) - 1;
                return i2 < 2 ? StreamSupport.intStream(this, false) : StreamSupport.intStream(this.buffer.spliterator(), false);
            }
            throw new IllegalStateException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(IntConsumer intConsumer) {
            Objects.requireNonNull(intConsumer);
            if (this.count == -2) {
                intConsumer.accept(this.first);
                this.count = -1;
                return true;
            }
            return false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive
        public void forEachRemaining(IntConsumer intConsumer) {
            Objects.requireNonNull(intConsumer);
            if (this.count == -2) {
                intConsumer.accept(this.first);
                this.count = -1;
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/Streams$LongStreamBuilderImpl.class */
    static final class LongStreamBuilderImpl extends AbstractStreamBuilderImpl<Long, Spliterator.OfLong> implements LongStream.Builder, Spliterator.OfLong {
        long first;
        SpinedBuffer.OfLong buffer;

        @Override // java.util.stream.Streams.AbstractStreamBuilderImpl, java.util.Spliterator
        public /* bridge */ /* synthetic */ Spliterator.OfLong trySplit() {
            return (Spliterator.OfLong) super.trySplit();
        }

        @Override // java.util.stream.Streams.AbstractStreamBuilderImpl, java.util.Spliterator
        public /* bridge */ /* synthetic */ Spliterator.OfPrimitive trySplit() {
            return (Spliterator.OfPrimitive) super.trySplit();
        }

        LongStreamBuilderImpl() {
            super();
        }

        LongStreamBuilderImpl(long j2) {
            super();
            this.first = j2;
            this.count = -2;
        }

        @Override // java.util.stream.LongStream.Builder, java.util.function.LongConsumer
        public void accept(long j2) {
            if (this.count == 0) {
                this.first = j2;
                this.count++;
            } else {
                if (this.count > 0) {
                    if (this.buffer == null) {
                        this.buffer = new SpinedBuffer.OfLong();
                        this.buffer.accept(this.first);
                        this.count++;
                    }
                    this.buffer.accept(j2);
                    return;
                }
                throw new IllegalStateException();
            }
        }

        @Override // java.util.stream.LongStream.Builder
        public LongStream build() {
            int i2 = this.count;
            if (i2 >= 0) {
                this.count = (-this.count) - 1;
                return i2 < 2 ? StreamSupport.longStream(this, false) : StreamSupport.longStream(this.buffer.spliterator(), false);
            }
            throw new IllegalStateException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(LongConsumer longConsumer) {
            Objects.requireNonNull(longConsumer);
            if (this.count == -2) {
                longConsumer.accept(this.first);
                this.count = -1;
                return true;
            }
            return false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive
        public void forEachRemaining(LongConsumer longConsumer) {
            Objects.requireNonNull(longConsumer);
            if (this.count == -2) {
                longConsumer.accept(this.first);
                this.count = -1;
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/Streams$DoubleStreamBuilderImpl.class */
    static final class DoubleStreamBuilderImpl extends AbstractStreamBuilderImpl<Double, Spliterator.OfDouble> implements DoubleStream.Builder, Spliterator.OfDouble {
        double first;
        SpinedBuffer.OfDouble buffer;

        @Override // java.util.stream.Streams.AbstractStreamBuilderImpl, java.util.Spliterator
        public /* bridge */ /* synthetic */ Spliterator.OfDouble trySplit() {
            return (Spliterator.OfDouble) super.trySplit();
        }

        @Override // java.util.stream.Streams.AbstractStreamBuilderImpl, java.util.Spliterator
        public /* bridge */ /* synthetic */ Spliterator.OfPrimitive trySplit() {
            return (Spliterator.OfPrimitive) super.trySplit();
        }

        DoubleStreamBuilderImpl() {
            super();
        }

        DoubleStreamBuilderImpl(double d2) {
            super();
            this.first = d2;
            this.count = -2;
        }

        @Override // java.util.stream.DoubleStream.Builder, java.util.function.DoubleConsumer
        public void accept(double d2) {
            if (this.count == 0) {
                this.first = d2;
                this.count++;
            } else {
                if (this.count > 0) {
                    if (this.buffer == null) {
                        this.buffer = new SpinedBuffer.OfDouble();
                        this.buffer.accept(this.first);
                        this.count++;
                    }
                    this.buffer.accept(d2);
                    return;
                }
                throw new IllegalStateException();
            }
        }

        @Override // java.util.stream.DoubleStream.Builder
        public DoubleStream build() {
            int i2 = this.count;
            if (i2 >= 0) {
                this.count = (-this.count) - 1;
                return i2 < 2 ? StreamSupport.doubleStream(this, false) : StreamSupport.doubleStream(this.buffer.spliterator(), false);
            }
            throw new IllegalStateException();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(DoubleConsumer doubleConsumer) {
            Objects.requireNonNull(doubleConsumer);
            if (this.count == -2) {
                doubleConsumer.accept(this.first);
                this.count = -1;
                return true;
            }
            return false;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive
        public void forEachRemaining(DoubleConsumer doubleConsumer) {
            Objects.requireNonNull(doubleConsumer);
            if (this.count == -2) {
                doubleConsumer.accept(this.first);
                this.count = -1;
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/Streams$ConcatSpliterator.class */
    static abstract class ConcatSpliterator<T, T_SPLITR extends Spliterator<T>> implements Spliterator<T> {
        protected final T_SPLITR aSpliterator;
        protected final T_SPLITR bSpliterator;
        boolean beforeSplit = true;
        final boolean unsized;

        public ConcatSpliterator(T_SPLITR t_splitr, T_SPLITR t_splitr2) {
            this.aSpliterator = t_splitr;
            this.bSpliterator = t_splitr2;
            this.unsized = t_splitr.estimateSize() + t_splitr2.estimateSize() < 0;
        }

        @Override // java.util.Spliterator
        public T_SPLITR trySplit() {
            T_SPLITR t_splitr = (T_SPLITR) (this.beforeSplit ? this.aSpliterator : this.bSpliterator.trySplit());
            this.beforeSplit = false;
            return t_splitr;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super T> consumer) {
            boolean zTryAdvance;
            if (this.beforeSplit) {
                zTryAdvance = this.aSpliterator.tryAdvance(consumer);
                if (!zTryAdvance) {
                    this.beforeSplit = false;
                    zTryAdvance = this.bSpliterator.tryAdvance(consumer);
                }
            } else {
                zTryAdvance = this.bSpliterator.tryAdvance(consumer);
            }
            return zTryAdvance;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super T> consumer) {
            if (this.beforeSplit) {
                this.aSpliterator.forEachRemaining(consumer);
            }
            this.bSpliterator.forEachRemaining(consumer);
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            if (this.beforeSplit) {
                long jEstimateSize = this.aSpliterator.estimateSize() + this.bSpliterator.estimateSize();
                return jEstimateSize >= 0 ? jEstimateSize : Long.MAX_VALUE;
            }
            return this.bSpliterator.estimateSize();
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            if (this.beforeSplit) {
                return this.aSpliterator.characteristics() & this.bSpliterator.characteristics() & ((5 | (this.unsized ? 16448 : 0)) ^ (-1));
            }
            return this.bSpliterator.characteristics();
        }

        @Override // java.util.Spliterator
        public Comparator<? super T> getComparator() {
            if (this.beforeSplit) {
                throw new IllegalStateException();
            }
            return this.bSpliterator.getComparator();
        }

        /* loaded from: rt.jar:java/util/stream/Streams$ConcatSpliterator$OfRef.class */
        static class OfRef<T> extends ConcatSpliterator<T, Spliterator<T>> {
            OfRef(Spliterator<T> spliterator, Spliterator<T> spliterator2) {
                super(spliterator, spliterator2);
            }
        }

        /* loaded from: rt.jar:java/util/stream/Streams$ConcatSpliterator$OfPrimitive.class */
        private static abstract class OfPrimitive<T, T_CONS, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>> extends ConcatSpliterator<T, T_SPLITR> implements Spliterator.OfPrimitive<T, T_CONS, T_SPLITR> {
            @Override // java.util.stream.Streams.ConcatSpliterator, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfPrimitive trySplit() {
                return (Spliterator.OfPrimitive) super.trySplit();
            }

            private OfPrimitive(T_SPLITR t_splitr, T_SPLITR t_splitr2) {
                super(t_splitr, t_splitr2);
            }

            @Override // java.util.Spliterator.OfPrimitive
            public boolean tryAdvance(T_CONS t_cons) {
                boolean zTryAdvance;
                if (this.beforeSplit) {
                    zTryAdvance = ((Spliterator.OfPrimitive) this.aSpliterator).tryAdvance((Spliterator.OfPrimitive) t_cons);
                    if (!zTryAdvance) {
                        this.beforeSplit = false;
                        zTryAdvance = ((Spliterator.OfPrimitive) this.bSpliterator).tryAdvance((Spliterator.OfPrimitive) t_cons);
                    }
                } else {
                    zTryAdvance = ((Spliterator.OfPrimitive) this.bSpliterator).tryAdvance((Spliterator.OfPrimitive) t_cons);
                }
                return zTryAdvance;
            }

            @Override // java.util.Spliterator.OfPrimitive
            public void forEachRemaining(T_CONS t_cons) {
                if (this.beforeSplit) {
                    ((Spliterator.OfPrimitive) this.aSpliterator).forEachRemaining((Spliterator.OfPrimitive) t_cons);
                }
                ((Spliterator.OfPrimitive) this.bSpliterator).forEachRemaining((Spliterator.OfPrimitive) t_cons);
            }
        }

        /* loaded from: rt.jar:java/util/stream/Streams$ConcatSpliterator$OfInt.class */
        static class OfInt extends OfPrimitive<Integer, IntConsumer, Spliterator.OfInt> implements Spliterator.OfInt {
            @Override // java.util.Spliterator.OfInt
            public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
                super.forEachRemaining((OfInt) intConsumer);
            }

            @Override // java.util.Spliterator.OfInt
            public /* bridge */ /* synthetic */ boolean tryAdvance(IntConsumer intConsumer) {
                return super.tryAdvance((OfInt) intConsumer);
            }

            @Override // java.util.stream.Streams.ConcatSpliterator.OfPrimitive, java.util.stream.Streams.ConcatSpliterator, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfInt trySplit() {
                return (Spliterator.OfInt) super.trySplit();
            }

            OfInt(Spliterator.OfInt ofInt, Spliterator.OfInt ofInt2) {
                super(ofInt, ofInt2);
            }
        }

        /* loaded from: rt.jar:java/util/stream/Streams$ConcatSpliterator$OfLong.class */
        static class OfLong extends OfPrimitive<Long, LongConsumer, Spliterator.OfLong> implements Spliterator.OfLong {
            @Override // java.util.Spliterator.OfLong
            public /* bridge */ /* synthetic */ void forEachRemaining(LongConsumer longConsumer) {
                super.forEachRemaining((OfLong) longConsumer);
            }

            @Override // java.util.Spliterator.OfLong
            public /* bridge */ /* synthetic */ boolean tryAdvance(LongConsumer longConsumer) {
                return super.tryAdvance((OfLong) longConsumer);
            }

            @Override // java.util.stream.Streams.ConcatSpliterator.OfPrimitive, java.util.stream.Streams.ConcatSpliterator, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfLong trySplit() {
                return (Spliterator.OfLong) super.trySplit();
            }

            OfLong(Spliterator.OfLong ofLong, Spliterator.OfLong ofLong2) {
                super(ofLong, ofLong2);
            }
        }

        /* loaded from: rt.jar:java/util/stream/Streams$ConcatSpliterator$OfDouble.class */
        static class OfDouble extends OfPrimitive<Double, DoubleConsumer, Spliterator.OfDouble> implements Spliterator.OfDouble {
            @Override // java.util.Spliterator.OfDouble
            public /* bridge */ /* synthetic */ void forEachRemaining(DoubleConsumer doubleConsumer) {
                super.forEachRemaining((OfDouble) doubleConsumer);
            }

            @Override // java.util.Spliterator.OfDouble
            public /* bridge */ /* synthetic */ boolean tryAdvance(DoubleConsumer doubleConsumer) {
                return super.tryAdvance((OfDouble) doubleConsumer);
            }

            @Override // java.util.stream.Streams.ConcatSpliterator.OfPrimitive, java.util.stream.Streams.ConcatSpliterator, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfDouble trySplit() {
                return (Spliterator.OfDouble) super.trySplit();
            }

            OfDouble(Spliterator.OfDouble ofDouble, Spliterator.OfDouble ofDouble2) {
                super(ofDouble, ofDouble2);
            }
        }
    }

    static Runnable composeWithExceptions(final Runnable runnable, final Runnable runnable2) {
        return new Runnable() { // from class: java.util.stream.Streams.1
            @Override // java.lang.Runnable
            public void run() {
                try {
                    runnable.run();
                    runnable2.run();
                } catch (Throwable th) {
                    try {
                        runnable2.run();
                    } catch (Throwable th2) {
                        try {
                            th.addSuppressed(th2);
                        } catch (Throwable th3) {
                        }
                    }
                    throw th;
                }
            }
        };
    }

    static Runnable composedClose(final BaseStream<?, ?> baseStream, final BaseStream<?, ?> baseStream2) {
        return new Runnable() { // from class: java.util.stream.Streams.2
            @Override // java.lang.Runnable
            public void run() {
                try {
                    baseStream.close();
                    baseStream2.close();
                } catch (Throwable th) {
                    try {
                        baseStream2.close();
                    } catch (Throwable th2) {
                        try {
                            th.addSuppressed(th2);
                        } catch (Throwable th3) {
                        }
                    }
                    throw th;
                }
            }
        };
    }
}
