package java.util.stream;

import java.util.Comparator;
import java.util.Objects;
import java.util.Spliterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleSupplier;
import java.util.function.IntConsumer;
import java.util.function.IntSupplier;
import java.util.function.LongConsumer;
import java.util.function.LongSupplier;
import java.util.function.Supplier;
import java.util.stream.SpinedBuffer;

/* loaded from: rt.jar:java/util/stream/StreamSpliterators.class */
class StreamSpliterators {
    StreamSpliterators() {
    }

    /* loaded from: rt.jar:java/util/stream/StreamSpliterators$AbstractWrappingSpliterator.class */
    private static abstract class AbstractWrappingSpliterator<P_IN, P_OUT, T_BUFFER extends AbstractSpinedBuffer> implements Spliterator<P_OUT> {
        final boolean isParallel;
        final PipelineHelper<P_OUT> ph;
        private Supplier<Spliterator<P_IN>> spliteratorSupplier;
        Spliterator<P_IN> spliterator;
        Sink<P_IN> bufferSink;
        BooleanSupplier pusher;
        long nextToConsume;
        T_BUFFER buffer;
        boolean finished;

        abstract AbstractWrappingSpliterator<P_IN, P_OUT, ?> wrap(Spliterator<P_IN> spliterator);

        abstract void initPartialTraversalState();

        AbstractWrappingSpliterator(PipelineHelper<P_OUT> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z2) {
            this.ph = pipelineHelper;
            this.spliteratorSupplier = supplier;
            this.spliterator = null;
            this.isParallel = z2;
        }

        AbstractWrappingSpliterator(PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator, boolean z2) {
            this.ph = pipelineHelper;
            this.spliteratorSupplier = null;
            this.spliterator = spliterator;
            this.isParallel = z2;
        }

        final void init() {
            if (this.spliterator == null) {
                this.spliterator = this.spliteratorSupplier.get();
                this.spliteratorSupplier = null;
            }
        }

        final boolean doAdvance() {
            if (this.buffer == null) {
                if (this.finished) {
                    return false;
                }
                init();
                initPartialTraversalState();
                this.nextToConsume = 0L;
                this.bufferSink.begin(this.spliterator.getExactSizeIfKnown());
                return fillBuffer();
            }
            this.nextToConsume++;
            boolean zFillBuffer = this.nextToConsume < this.buffer.count();
            if (!zFillBuffer) {
                this.nextToConsume = 0L;
                this.buffer.clear();
                zFillBuffer = fillBuffer();
            }
            return zFillBuffer;
        }

        @Override // java.util.Spliterator
        public Spliterator<P_OUT> trySplit() {
            if (this.isParallel && !this.finished) {
                init();
                Spliterator<P_IN> spliteratorTrySplit = this.spliterator.trySplit();
                if (spliteratorTrySplit == null) {
                    return null;
                }
                return wrap(spliteratorTrySplit);
            }
            return null;
        }

        private boolean fillBuffer() {
            while (this.buffer.count() == 0) {
                if (this.bufferSink.cancellationRequested() || !this.pusher.getAsBoolean()) {
                    if (this.finished) {
                        return false;
                    }
                    this.bufferSink.end();
                    this.finished = true;
                }
            }
            return true;
        }

        @Override // java.util.Spliterator
        public final long estimateSize() {
            init();
            return this.spliterator.estimateSize();
        }

        @Override // java.util.Spliterator
        public final long getExactSizeIfKnown() {
            init();
            if (StreamOpFlag.SIZED.isKnown(this.ph.getStreamAndOpFlags())) {
                return this.spliterator.getExactSizeIfKnown();
            }
            return -1L;
        }

        @Override // java.util.Spliterator
        public final int characteristics() {
            init();
            int characteristics = StreamOpFlag.toCharacteristics(StreamOpFlag.toStreamFlags(this.ph.getStreamAndOpFlags()));
            if ((characteristics & 64) != 0) {
                characteristics = (characteristics & (-16449)) | (this.spliterator.characteristics() & 16448);
            }
            return characteristics;
        }

        @Override // java.util.Spliterator
        public Comparator<? super P_OUT> getComparator() {
            if (!hasCharacteristics(4)) {
                throw new IllegalStateException();
            }
            return null;
        }

        public final String toString() {
            return String.format("%s[%s]", getClass().getName(), this.spliterator);
        }
    }

    /* loaded from: rt.jar:java/util/stream/StreamSpliterators$WrappingSpliterator.class */
    static final class WrappingSpliterator<P_IN, P_OUT> extends AbstractWrappingSpliterator<P_IN, P_OUT, SpinedBuffer<P_OUT>> {
        WrappingSpliterator(PipelineHelper<P_OUT> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z2) {
            super(pipelineHelper, supplier, z2);
        }

        WrappingSpliterator(PipelineHelper<P_OUT> pipelineHelper, Spliterator<P_IN> spliterator, boolean z2) {
            super(pipelineHelper, spliterator, z2);
        }

        /* JADX INFO: Access modifiers changed from: package-private */
        @Override // java.util.stream.StreamSpliterators.AbstractWrappingSpliterator
        public WrappingSpliterator<P_IN, P_OUT> wrap(Spliterator<P_IN> spliterator) {
            return new WrappingSpliterator<>(this.ph, spliterator, this.isParallel);
        }

        @Override // java.util.stream.StreamSpliterators.AbstractWrappingSpliterator
        void initPartialTraversalState() {
            SpinedBuffer spinedBuffer = new SpinedBuffer();
            this.buffer = spinedBuffer;
            PipelineHelper<P_OUT> pipelineHelper = this.ph;
            spinedBuffer.getClass();
            this.bufferSink = pipelineHelper.wrapSink(spinedBuffer::accept);
            this.pusher = () -> {
                return this.spliterator.tryAdvance(this.bufferSink);
            };
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super P_OUT> consumer) {
            Objects.requireNonNull(consumer);
            boolean zDoAdvance = doAdvance();
            if (zDoAdvance) {
                consumer.accept((Object) ((SpinedBuffer) this.buffer).get(this.nextToConsume));
            }
            return zDoAdvance;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super P_OUT> consumer) {
            if (this.buffer == 0 && !this.finished) {
                Objects.requireNonNull(consumer);
                init();
                PipelineHelper<P_OUT> pipelineHelper = this.ph;
                consumer.getClass();
                pipelineHelper.wrapAndCopyInto(consumer::accept, this.spliterator);
                this.finished = true;
                return;
            }
            while (tryAdvance(consumer)) {
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/StreamSpliterators$IntWrappingSpliterator.class */
    static final class IntWrappingSpliterator<P_IN> extends AbstractWrappingSpliterator<P_IN, Integer, SpinedBuffer.OfInt> implements Spliterator.OfInt {
        IntWrappingSpliterator(PipelineHelper<Integer> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z2) {
            super(pipelineHelper, supplier, z2);
        }

        IntWrappingSpliterator(PipelineHelper<Integer> pipelineHelper, Spliterator<P_IN> spliterator, boolean z2) {
            super(pipelineHelper, spliterator, z2);
        }

        @Override // java.util.stream.StreamSpliterators.AbstractWrappingSpliterator
        AbstractWrappingSpliterator<P_IN, Integer, ?> wrap(Spliterator<P_IN> spliterator) {
            return new IntWrappingSpliterator((PipelineHelper<Integer>) this.ph, (Spliterator) spliterator, this.isParallel);
        }

        @Override // java.util.stream.StreamSpliterators.AbstractWrappingSpliterator
        void initPartialTraversalState() {
            SpinedBuffer.OfInt ofInt = new SpinedBuffer.OfInt();
            this.buffer = ofInt;
            PipelineHelper<P_OUT> pipelineHelper = this.ph;
            ofInt.getClass();
            this.bufferSink = pipelineHelper.wrapSink(ofInt::accept);
            this.pusher = () -> {
                return this.spliterator.tryAdvance(this.bufferSink);
            };
        }

        @Override // java.util.stream.StreamSpliterators.AbstractWrappingSpliterator, java.util.Spliterator
        public Spliterator.OfInt trySplit() {
            return (Spliterator.OfInt) super.trySplit();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(IntConsumer intConsumer) {
            Objects.requireNonNull(intConsumer);
            boolean zDoAdvance = doAdvance();
            if (zDoAdvance) {
                intConsumer.accept(((SpinedBuffer.OfInt) this.buffer).get(this.nextToConsume));
            }
            return zDoAdvance;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive
        public void forEachRemaining(IntConsumer intConsumer) {
            if (this.buffer == 0 && !this.finished) {
                Objects.requireNonNull(intConsumer);
                init();
                PipelineHelper<P_OUT> pipelineHelper = this.ph;
                intConsumer.getClass();
                pipelineHelper.wrapAndCopyInto(intConsumer::accept, this.spliterator);
                this.finished = true;
                return;
            }
            while (tryAdvance(intConsumer)) {
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/StreamSpliterators$LongWrappingSpliterator.class */
    static final class LongWrappingSpliterator<P_IN> extends AbstractWrappingSpliterator<P_IN, Long, SpinedBuffer.OfLong> implements Spliterator.OfLong {
        LongWrappingSpliterator(PipelineHelper<Long> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z2) {
            super(pipelineHelper, supplier, z2);
        }

        LongWrappingSpliterator(PipelineHelper<Long> pipelineHelper, Spliterator<P_IN> spliterator, boolean z2) {
            super(pipelineHelper, spliterator, z2);
        }

        @Override // java.util.stream.StreamSpliterators.AbstractWrappingSpliterator
        AbstractWrappingSpliterator<P_IN, Long, ?> wrap(Spliterator<P_IN> spliterator) {
            return new LongWrappingSpliterator((PipelineHelper<Long>) this.ph, (Spliterator) spliterator, this.isParallel);
        }

        @Override // java.util.stream.StreamSpliterators.AbstractWrappingSpliterator
        void initPartialTraversalState() {
            SpinedBuffer.OfLong ofLong = new SpinedBuffer.OfLong();
            this.buffer = ofLong;
            PipelineHelper<P_OUT> pipelineHelper = this.ph;
            ofLong.getClass();
            this.bufferSink = pipelineHelper.wrapSink(ofLong::accept);
            this.pusher = () -> {
                return this.spliterator.tryAdvance(this.bufferSink);
            };
        }

        @Override // java.util.stream.StreamSpliterators.AbstractWrappingSpliterator, java.util.Spliterator
        public Spliterator.OfLong trySplit() {
            return (Spliterator.OfLong) super.trySplit();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(LongConsumer longConsumer) {
            Objects.requireNonNull(longConsumer);
            boolean zDoAdvance = doAdvance();
            if (zDoAdvance) {
                longConsumer.accept(((SpinedBuffer.OfLong) this.buffer).get(this.nextToConsume));
            }
            return zDoAdvance;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive
        public void forEachRemaining(LongConsumer longConsumer) {
            if (this.buffer == 0 && !this.finished) {
                Objects.requireNonNull(longConsumer);
                init();
                PipelineHelper<P_OUT> pipelineHelper = this.ph;
                longConsumer.getClass();
                pipelineHelper.wrapAndCopyInto(longConsumer::accept, this.spliterator);
                this.finished = true;
                return;
            }
            while (tryAdvance(longConsumer)) {
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/StreamSpliterators$DoubleWrappingSpliterator.class */
    static final class DoubleWrappingSpliterator<P_IN> extends AbstractWrappingSpliterator<P_IN, Double, SpinedBuffer.OfDouble> implements Spliterator.OfDouble {
        DoubleWrappingSpliterator(PipelineHelper<Double> pipelineHelper, Supplier<Spliterator<P_IN>> supplier, boolean z2) {
            super(pipelineHelper, supplier, z2);
        }

        DoubleWrappingSpliterator(PipelineHelper<Double> pipelineHelper, Spliterator<P_IN> spliterator, boolean z2) {
            super(pipelineHelper, spliterator, z2);
        }

        @Override // java.util.stream.StreamSpliterators.AbstractWrappingSpliterator
        AbstractWrappingSpliterator<P_IN, Double, ?> wrap(Spliterator<P_IN> spliterator) {
            return new DoubleWrappingSpliterator((PipelineHelper<Double>) this.ph, (Spliterator) spliterator, this.isParallel);
        }

        @Override // java.util.stream.StreamSpliterators.AbstractWrappingSpliterator
        void initPartialTraversalState() {
            SpinedBuffer.OfDouble ofDouble = new SpinedBuffer.OfDouble();
            this.buffer = ofDouble;
            PipelineHelper<P_OUT> pipelineHelper = this.ph;
            ofDouble.getClass();
            this.bufferSink = pipelineHelper.wrapSink(ofDouble::accept);
            this.pusher = () -> {
                return this.spliterator.tryAdvance(this.bufferSink);
            };
        }

        @Override // java.util.stream.StreamSpliterators.AbstractWrappingSpliterator, java.util.Spliterator
        public Spliterator.OfDouble trySplit() {
            return (Spliterator.OfDouble) super.trySplit();
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive
        public boolean tryAdvance(DoubleConsumer doubleConsumer) {
            Objects.requireNonNull(doubleConsumer);
            boolean zDoAdvance = doAdvance();
            if (zDoAdvance) {
                doubleConsumer.accept(((SpinedBuffer.OfDouble) this.buffer).get(this.nextToConsume));
            }
            return zDoAdvance;
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive
        public void forEachRemaining(DoubleConsumer doubleConsumer) {
            if (this.buffer == 0 && !this.finished) {
                Objects.requireNonNull(doubleConsumer);
                init();
                PipelineHelper<P_OUT> pipelineHelper = this.ph;
                doubleConsumer.getClass();
                pipelineHelper.wrapAndCopyInto(doubleConsumer::accept, this.spliterator);
                this.finished = true;
                return;
            }
            while (tryAdvance(doubleConsumer)) {
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/StreamSpliterators$DelegatingSpliterator.class */
    static class DelegatingSpliterator<T, T_SPLITR extends Spliterator<T>> implements Spliterator<T> {
        private final Supplier<? extends T_SPLITR> supplier;

        /* renamed from: s, reason: collision with root package name */
        private T_SPLITR f12614s;

        DelegatingSpliterator(Supplier<? extends T_SPLITR> supplier) {
            this.supplier = supplier;
        }

        T_SPLITR get() {
            if (this.f12614s == null) {
                this.f12614s = this.supplier.get();
            }
            return this.f12614s;
        }

        @Override // java.util.Spliterator
        public T_SPLITR trySplit() {
            return (T_SPLITR) get().trySplit();
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super T> consumer) {
            return get().tryAdvance(consumer);
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super T> consumer) {
            get().forEachRemaining(consumer);
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return get().estimateSize();
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return get().characteristics();
        }

        @Override // java.util.Spliterator
        public Comparator<? super T> getComparator() {
            return get().getComparator();
        }

        @Override // java.util.Spliterator
        public long getExactSizeIfKnown() {
            return get().getExactSizeIfKnown();
        }

        public String toString() {
            return getClass().getName() + "[" + ((Object) get()) + "]";
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$DelegatingSpliterator$OfPrimitive.class */
        static class OfPrimitive<T, T_CONS, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>> extends DelegatingSpliterator<T, T_SPLITR> implements Spliterator.OfPrimitive<T, T_CONS, T_SPLITR> {
            @Override // java.util.stream.StreamSpliterators.DelegatingSpliterator, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfPrimitive trySplit() {
                return (Spliterator.OfPrimitive) super.trySplit();
            }

            OfPrimitive(Supplier<? extends T_SPLITR> supplier) {
                super(supplier);
            }

            @Override // java.util.Spliterator.OfPrimitive
            public boolean tryAdvance(T_CONS t_cons) {
                return get().tryAdvance(t_cons);
            }

            @Override // java.util.Spliterator.OfPrimitive
            public void forEachRemaining(T_CONS t_cons) {
                get().forEachRemaining(t_cons);
            }
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$DelegatingSpliterator$OfInt.class */
        static final class OfInt extends OfPrimitive<Integer, IntConsumer, Spliterator.OfInt> implements Spliterator.OfInt {
            @Override // java.util.Spliterator.OfInt
            public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
                super.forEachRemaining((OfInt) intConsumer);
            }

            @Override // java.util.Spliterator.OfInt
            public /* bridge */ /* synthetic */ boolean tryAdvance(IntConsumer intConsumer) {
                return super.tryAdvance((OfInt) intConsumer);
            }

            @Override // java.util.stream.StreamSpliterators.DelegatingSpliterator.OfPrimitive, java.util.stream.StreamSpliterators.DelegatingSpliterator, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfInt trySplit() {
                return (Spliterator.OfInt) super.trySplit();
            }

            OfInt(Supplier<Spliterator.OfInt> supplier) {
                super(supplier);
            }
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$DelegatingSpliterator$OfLong.class */
        static final class OfLong extends OfPrimitive<Long, LongConsumer, Spliterator.OfLong> implements Spliterator.OfLong {
            @Override // java.util.Spliterator.OfLong
            public /* bridge */ /* synthetic */ void forEachRemaining(LongConsumer longConsumer) {
                super.forEachRemaining((OfLong) longConsumer);
            }

            @Override // java.util.Spliterator.OfLong
            public /* bridge */ /* synthetic */ boolean tryAdvance(LongConsumer longConsumer) {
                return super.tryAdvance((OfLong) longConsumer);
            }

            @Override // java.util.stream.StreamSpliterators.DelegatingSpliterator.OfPrimitive, java.util.stream.StreamSpliterators.DelegatingSpliterator, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfLong trySplit() {
                return (Spliterator.OfLong) super.trySplit();
            }

            OfLong(Supplier<Spliterator.OfLong> supplier) {
                super(supplier);
            }
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$DelegatingSpliterator$OfDouble.class */
        static final class OfDouble extends OfPrimitive<Double, DoubleConsumer, Spliterator.OfDouble> implements Spliterator.OfDouble {
            @Override // java.util.Spliterator.OfDouble
            public /* bridge */ /* synthetic */ void forEachRemaining(DoubleConsumer doubleConsumer) {
                super.forEachRemaining((OfDouble) doubleConsumer);
            }

            @Override // java.util.Spliterator.OfDouble
            public /* bridge */ /* synthetic */ boolean tryAdvance(DoubleConsumer doubleConsumer) {
                return super.tryAdvance((OfDouble) doubleConsumer);
            }

            @Override // java.util.stream.StreamSpliterators.DelegatingSpliterator.OfPrimitive, java.util.stream.StreamSpliterators.DelegatingSpliterator, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfDouble trySplit() {
                return (Spliterator.OfDouble) super.trySplit();
            }

            OfDouble(Supplier<Spliterator.OfDouble> supplier) {
                super(supplier);
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/StreamSpliterators$SliceSpliterator.class */
    static abstract class SliceSpliterator<T, T_SPLITR extends Spliterator<T>> {
        final long sliceOrigin;
        final long sliceFence;

        /* renamed from: s, reason: collision with root package name */
        T_SPLITR f12620s;
        long index;
        long fence;
        static final /* synthetic */ boolean $assertionsDisabled;

        protected abstract T_SPLITR makeSpliterator(T_SPLITR t_splitr, long j2, long j3, long j4, long j5);

        static {
            $assertionsDisabled = !StreamSpliterators.class.desiredAssertionStatus();
        }

        SliceSpliterator(T_SPLITR t_splitr, long j2, long j3, long j4, long j5) {
            if (!$assertionsDisabled && !t_splitr.hasCharacteristics(16384)) {
                throw new AssertionError();
            }
            this.f12620s = t_splitr;
            this.sliceOrigin = j2;
            this.sliceFence = j3;
            this.index = j4;
            this.fence = j5;
        }

        public T_SPLITR trySplit() {
            if (this.sliceOrigin >= this.fence || this.index >= this.fence) {
                return null;
            }
            while (true) {
                T_SPLITR t_splitr = (T_SPLITR) this.f12620s.trySplit();
                if (t_splitr == null) {
                    return null;
                }
                long jEstimateSize = this.index + t_splitr.estimateSize();
                long jMin = Math.min(jEstimateSize, this.sliceFence);
                if (this.sliceOrigin >= jMin) {
                    this.index = jMin;
                } else {
                    if (jMin < this.sliceFence) {
                        if (this.index >= this.sliceOrigin && jEstimateSize <= this.sliceFence) {
                            this.index = jMin;
                            return t_splitr;
                        }
                        long j2 = this.sliceOrigin;
                        long j3 = this.sliceFence;
                        long j4 = this.index;
                        this.index = jMin;
                        return (T_SPLITR) makeSpliterator(t_splitr, j2, j3, j4, jMin);
                    }
                    this.f12620s = t_splitr;
                    this.fence = jMin;
                }
            }
        }

        public long estimateSize() {
            if (this.sliceOrigin < this.fence) {
                return this.fence - Math.max(this.sliceOrigin, this.index);
            }
            return 0L;
        }

        public int characteristics() {
            return this.f12620s.characteristics();
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$SliceSpliterator$OfRef.class */
        static final class OfRef<T> extends SliceSpliterator<T, Spliterator<T>> implements Spliterator<T> {
            OfRef(Spliterator<T> spliterator, long j2, long j3) {
                this(spliterator, j2, j3, 0L, Math.min(spliterator.estimateSize(), j3));
            }

            private OfRef(Spliterator<T> spliterator, long j2, long j3, long j4, long j5) {
                super(spliterator, j2, j3, j4, j5);
            }

            @Override // java.util.stream.StreamSpliterators.SliceSpliterator
            protected Spliterator<T> makeSpliterator(Spliterator<T> spliterator, long j2, long j3, long j4, long j5) {
                return new OfRef(spliterator, j2, j3, j4, j5);
            }

            @Override // java.util.Spliterator
            public boolean tryAdvance(Consumer<? super T> consumer) {
                Objects.requireNonNull(consumer);
                if (this.sliceOrigin >= this.fence) {
                    return false;
                }
                while (this.sliceOrigin > this.index) {
                    this.f12620s.tryAdvance(obj -> {
                    });
                    this.index++;
                }
                if (this.index >= this.fence) {
                    return false;
                }
                this.index++;
                return this.f12620s.tryAdvance(consumer);
            }

            @Override // java.util.Spliterator
            public void forEachRemaining(Consumer<? super T> consumer) {
                Objects.requireNonNull(consumer);
                if (this.sliceOrigin < this.fence && this.index < this.fence) {
                    if (this.index >= this.sliceOrigin && this.index + this.f12620s.estimateSize() <= this.sliceFence) {
                        this.f12620s.forEachRemaining(consumer);
                        this.index = this.fence;
                        return;
                    }
                    while (this.sliceOrigin > this.index) {
                        this.f12620s.tryAdvance(obj -> {
                        });
                        this.index++;
                    }
                    while (this.index < this.fence) {
                        this.f12620s.tryAdvance(consumer);
                        this.index++;
                    }
                }
            }
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$SliceSpliterator$OfPrimitive.class */
        static abstract class OfPrimitive<T, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>, T_CONS> extends SliceSpliterator<T, T_SPLITR> implements Spliterator.OfPrimitive<T, T_CONS, T_SPLITR> {
            protected abstract T_CONS emptyConsumer();

            @Override // java.util.stream.StreamSpliterators.SliceSpliterator, java.util.Spliterator.OfPrimitive, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfPrimitive trySplit() {
                return (Spliterator.OfPrimitive) super.trySplit();
            }

            OfPrimitive(T_SPLITR t_splitr, long j2, long j3) {
                this(t_splitr, j2, j3, 0L, Math.min(t_splitr.estimateSize(), j3));
            }

            private OfPrimitive(T_SPLITR t_splitr, long j2, long j3, long j4, long j5) {
                super(t_splitr, j2, j3, j4, j5);
            }

            @Override // java.util.Spliterator.OfPrimitive
            public boolean tryAdvance(T_CONS t_cons) {
                Objects.requireNonNull(t_cons);
                if (this.sliceOrigin >= this.fence) {
                    return false;
                }
                while (this.sliceOrigin > this.index) {
                    ((Spliterator.OfPrimitive) this.f12620s).tryAdvance((Spliterator.OfPrimitive) emptyConsumer());
                    this.index++;
                }
                if (this.index >= this.fence) {
                    return false;
                }
                this.index++;
                return ((Spliterator.OfPrimitive) this.f12620s).tryAdvance((Spliterator.OfPrimitive) t_cons);
            }

            @Override // java.util.Spliterator.OfPrimitive
            public void forEachRemaining(T_CONS t_cons) {
                Objects.requireNonNull(t_cons);
                if (this.sliceOrigin < this.fence && this.index < this.fence) {
                    if (this.index >= this.sliceOrigin && this.index + ((Spliterator.OfPrimitive) this.f12620s).estimateSize() <= this.sliceFence) {
                        ((Spliterator.OfPrimitive) this.f12620s).forEachRemaining((Spliterator.OfPrimitive) t_cons);
                        this.index = this.fence;
                        return;
                    }
                    while (this.sliceOrigin > this.index) {
                        ((Spliterator.OfPrimitive) this.f12620s).tryAdvance((Spliterator.OfPrimitive) emptyConsumer());
                        this.index++;
                    }
                    while (this.index < this.fence) {
                        ((Spliterator.OfPrimitive) this.f12620s).tryAdvance((Spliterator.OfPrimitive) t_cons);
                        this.index++;
                    }
                }
            }
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$SliceSpliterator$OfInt.class */
        static final class OfInt extends OfPrimitive<Integer, Spliterator.OfInt, IntConsumer> implements Spliterator.OfInt {
            @Override // java.util.Spliterator.OfInt
            public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
                super.forEachRemaining((OfInt) intConsumer);
            }

            @Override // java.util.Spliterator.OfInt
            public /* bridge */ /* synthetic */ boolean tryAdvance(IntConsumer intConsumer) {
                return super.tryAdvance((OfInt) intConsumer);
            }

            @Override // java.util.stream.StreamSpliterators.SliceSpliterator.OfPrimitive, java.util.stream.StreamSpliterators.SliceSpliterator, java.util.Spliterator.OfPrimitive, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfInt trySplit() {
                return (Spliterator.OfInt) super.trySplit();
            }

            OfInt(Spliterator.OfInt ofInt, long j2, long j3) {
                super(ofInt, j2, j3);
            }

            OfInt(Spliterator.OfInt ofInt, long j2, long j3, long j4, long j5) {
                super(ofInt, j2, j3, j4, j5);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // java.util.stream.StreamSpliterators.SliceSpliterator
            public Spliterator.OfInt makeSpliterator(Spliterator.OfInt ofInt, long j2, long j3, long j4, long j5) {
                return new OfInt(ofInt, j2, j3, j4, j5);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.stream.StreamSpliterators.SliceSpliterator.OfPrimitive
            public IntConsumer emptyConsumer() {
                return i2 -> {
                };
            }
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$SliceSpliterator$OfLong.class */
        static final class OfLong extends OfPrimitive<Long, Spliterator.OfLong, LongConsumer> implements Spliterator.OfLong {
            @Override // java.util.Spliterator.OfLong
            public /* bridge */ /* synthetic */ void forEachRemaining(LongConsumer longConsumer) {
                super.forEachRemaining((OfLong) longConsumer);
            }

            @Override // java.util.Spliterator.OfLong
            public /* bridge */ /* synthetic */ boolean tryAdvance(LongConsumer longConsumer) {
                return super.tryAdvance((OfLong) longConsumer);
            }

            @Override // java.util.stream.StreamSpliterators.SliceSpliterator.OfPrimitive, java.util.stream.StreamSpliterators.SliceSpliterator, java.util.Spliterator.OfPrimitive, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfLong trySplit() {
                return (Spliterator.OfLong) super.trySplit();
            }

            OfLong(Spliterator.OfLong ofLong, long j2, long j3) {
                super(ofLong, j2, j3);
            }

            OfLong(Spliterator.OfLong ofLong, long j2, long j3, long j4, long j5) {
                super(ofLong, j2, j3, j4, j5);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // java.util.stream.StreamSpliterators.SliceSpliterator
            public Spliterator.OfLong makeSpliterator(Spliterator.OfLong ofLong, long j2, long j3, long j4, long j5) {
                return new OfLong(ofLong, j2, j3, j4, j5);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.stream.StreamSpliterators.SliceSpliterator.OfPrimitive
            public LongConsumer emptyConsumer() {
                return j2 -> {
                };
            }
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$SliceSpliterator$OfDouble.class */
        static final class OfDouble extends OfPrimitive<Double, Spliterator.OfDouble, DoubleConsumer> implements Spliterator.OfDouble {
            @Override // java.util.Spliterator.OfDouble
            public /* bridge */ /* synthetic */ void forEachRemaining(DoubleConsumer doubleConsumer) {
                super.forEachRemaining((OfDouble) doubleConsumer);
            }

            @Override // java.util.Spliterator.OfDouble
            public /* bridge */ /* synthetic */ boolean tryAdvance(DoubleConsumer doubleConsumer) {
                return super.tryAdvance((OfDouble) doubleConsumer);
            }

            @Override // java.util.stream.StreamSpliterators.SliceSpliterator.OfPrimitive, java.util.stream.StreamSpliterators.SliceSpliterator, java.util.Spliterator.OfPrimitive, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfDouble trySplit() {
                return (Spliterator.OfDouble) super.trySplit();
            }

            OfDouble(Spliterator.OfDouble ofDouble, long j2, long j3) {
                super(ofDouble, j2, j3);
            }

            OfDouble(Spliterator.OfDouble ofDouble, long j2, long j3, long j4, long j5) {
                super(ofDouble, j2, j3, j4, j5);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // java.util.stream.StreamSpliterators.SliceSpliterator
            public Spliterator.OfDouble makeSpliterator(Spliterator.OfDouble ofDouble, long j2, long j3, long j4, long j5) {
                return new OfDouble(ofDouble, j2, j3, j4, j5);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.stream.StreamSpliterators.SliceSpliterator.OfPrimitive
            public DoubleConsumer emptyConsumer() {
                return d2 -> {
                };
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/StreamSpliterators$UnorderedSliceSpliterator.class */
    static abstract class UnorderedSliceSpliterator<T, T_SPLITR extends Spliterator<T>> {
        static final int CHUNK_SIZE = 128;

        /* renamed from: s, reason: collision with root package name */
        protected final T_SPLITR f12621s;
        protected final boolean unlimited;
        protected final int chunkSize;
        private final long skipThreshold;
        private final AtomicLong permits;
        static final /* synthetic */ boolean $assertionsDisabled;

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$UnorderedSliceSpliterator$PermitStatus.class */
        enum PermitStatus {
            NO_MORE,
            MAYBE_MORE,
            UNLIMITED
        }

        protected abstract T_SPLITR makeSpliterator(T_SPLITR t_splitr);

        static {
            $assertionsDisabled = !StreamSpliterators.class.desiredAssertionStatus();
        }

        UnorderedSliceSpliterator(T_SPLITR t_splitr, long j2, long j3) {
            this.f12621s = t_splitr;
            this.unlimited = j3 < 0;
            this.skipThreshold = j3 >= 0 ? j3 : 0L;
            this.chunkSize = j3 >= 0 ? (int) Math.min(128L, ((j2 + j3) / AbstractTask.getLeafTarget()) + 1) : 128;
            this.permits = new AtomicLong(j3 >= 0 ? j2 + j3 : j2);
        }

        UnorderedSliceSpliterator(T_SPLITR t_splitr, UnorderedSliceSpliterator<T, T_SPLITR> unorderedSliceSpliterator) {
            this.f12621s = t_splitr;
            this.unlimited = unorderedSliceSpliterator.unlimited;
            this.permits = unorderedSliceSpliterator.permits;
            this.skipThreshold = unorderedSliceSpliterator.skipThreshold;
            this.chunkSize = unorderedSliceSpliterator.chunkSize;
        }

        protected final long acquirePermits(long j2) {
            long j3;
            long jMin;
            if (!$assertionsDisabled && j2 <= 0) {
                throw new AssertionError();
            }
            do {
                j3 = this.permits.get();
                if (j3 == 0) {
                    if (this.unlimited) {
                        return j2;
                    }
                    return 0L;
                }
                jMin = Math.min(j3, j2);
                if (jMin <= 0) {
                    break;
                }
            } while (!this.permits.compareAndSet(j3, j3 - jMin));
            if (this.unlimited) {
                return Math.max(j2 - jMin, 0L);
            }
            if (j3 > this.skipThreshold) {
                return Math.max(jMin - (j3 - this.skipThreshold), 0L);
            }
            return jMin;
        }

        protected final PermitStatus permitStatus() {
            if (this.permits.get() > 0) {
                return PermitStatus.MAYBE_MORE;
            }
            return this.unlimited ? PermitStatus.UNLIMITED : PermitStatus.NO_MORE;
        }

        public final T_SPLITR trySplit() {
            Spliterator<T> spliteratorTrySplit;
            if (this.permits.get() == 0 || (spliteratorTrySplit = this.f12621s.trySplit()) == null) {
                return null;
            }
            return (T_SPLITR) makeSpliterator(spliteratorTrySplit);
        }

        public final long estimateSize() {
            return this.f12621s.estimateSize();
        }

        public final int characteristics() {
            return this.f12621s.characteristics() & (-16465);
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$UnorderedSliceSpliterator$OfRef.class */
        static final class OfRef<T> extends UnorderedSliceSpliterator<T, Spliterator<T>> implements Spliterator<T>, Consumer<T> {
            T tmpSlot;

            OfRef(Spliterator<T> spliterator, long j2, long j3) {
                super(spliterator, j2, j3);
            }

            OfRef(Spliterator<T> spliterator, OfRef<T> ofRef) {
                super(spliterator, ofRef);
            }

            @Override // java.util.function.Consumer
            public final void accept(T t2) {
                this.tmpSlot = t2;
            }

            @Override // java.util.Spliterator
            public boolean tryAdvance(Consumer<? super T> consumer) {
                Objects.requireNonNull(consumer);
                while (permitStatus() != PermitStatus.NO_MORE && this.f12621s.tryAdvance(this)) {
                    if (acquirePermits(1L) == 1) {
                        consumer.accept(this.tmpSlot);
                        this.tmpSlot = null;
                        return true;
                    }
                }
                return false;
            }

            @Override // java.util.Spliterator
            public void forEachRemaining(Consumer<? super T> consumer) {
                Objects.requireNonNull(consumer);
                ArrayBuffer.OfRef ofRef = null;
                while (true) {
                    PermitStatus permitStatus = permitStatus();
                    if (permitStatus != PermitStatus.NO_MORE) {
                        if (permitStatus == PermitStatus.MAYBE_MORE) {
                            if (ofRef == null) {
                                ofRef = new ArrayBuffer.OfRef(this.chunkSize);
                            } else {
                                ofRef.reset();
                            }
                            long j2 = 0;
                            while (this.f12621s.tryAdvance(ofRef)) {
                                long j3 = j2 + 1;
                                j2 = j3;
                                if (j3 >= this.chunkSize) {
                                    break;
                                }
                            }
                            if (j2 == 0) {
                                return;
                            } else {
                                ofRef.forEach(consumer, acquirePermits(j2));
                            }
                        } else {
                            this.f12621s.forEachRemaining(consumer);
                            return;
                        }
                    } else {
                        return;
                    }
                }
            }

            @Override // java.util.stream.StreamSpliterators.UnorderedSliceSpliterator
            protected Spliterator<T> makeSpliterator(Spliterator<T> spliterator) {
                return new OfRef(spliterator, this);
            }
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$UnorderedSliceSpliterator$OfPrimitive.class */
        static abstract class OfPrimitive<T, T_CONS, T_BUFF extends ArrayBuffer.OfPrimitive<T_CONS>, T_SPLITR extends Spliterator.OfPrimitive<T, T_CONS, T_SPLITR>> extends UnorderedSliceSpliterator<T, T_SPLITR> implements Spliterator.OfPrimitive<T, T_CONS, T_SPLITR> {
            protected abstract void acceptConsumed(T_CONS t_cons);

            protected abstract T_BUFF bufferCreate(int i2);

            @Override // java.util.stream.StreamSpliterators.UnorderedSliceSpliterator, java.util.Spliterator.OfPrimitive, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfPrimitive trySplit() {
                return (Spliterator.OfPrimitive) super.trySplit();
            }

            OfPrimitive(T_SPLITR t_splitr, long j2, long j3) {
                super(t_splitr, j2, j3);
            }

            OfPrimitive(T_SPLITR t_splitr, OfPrimitive<T, T_CONS, T_BUFF, T_SPLITR> ofPrimitive) {
                super(t_splitr, ofPrimitive);
            }

            @Override // java.util.Spliterator.OfPrimitive
            public boolean tryAdvance(T_CONS t_cons) {
                Objects.requireNonNull(t_cons);
                while (permitStatus() != PermitStatus.NO_MORE && ((Spliterator.OfPrimitive) this.f12621s).tryAdvance(this)) {
                    if (acquirePermits(1L) == 1) {
                        acceptConsumed(t_cons);
                        return true;
                    }
                }
                return false;
            }

            @Override // java.util.Spliterator.OfPrimitive
            public void forEachRemaining(T_CONS t_cons) {
                Objects.requireNonNull(t_cons);
                ArrayBuffer.OfPrimitive ofPrimitiveBufferCreate = null;
                while (true) {
                    PermitStatus permitStatus = permitStatus();
                    if (permitStatus != PermitStatus.NO_MORE) {
                        if (permitStatus == PermitStatus.MAYBE_MORE) {
                            if (ofPrimitiveBufferCreate == null) {
                                ofPrimitiveBufferCreate = bufferCreate(this.chunkSize);
                            } else {
                                ofPrimitiveBufferCreate.reset();
                            }
                            ArrayBuffer.OfPrimitive ofPrimitive = ofPrimitiveBufferCreate;
                            long j2 = 0;
                            while (((Spliterator.OfPrimitive) this.f12621s).tryAdvance((Spliterator.OfPrimitive) ofPrimitive)) {
                                long j3 = j2 + 1;
                                j2 = j3;
                                if (j3 >= this.chunkSize) {
                                    break;
                                }
                            }
                            if (j2 == 0) {
                                return;
                            } else {
                                ofPrimitiveBufferCreate.forEach(t_cons, acquirePermits(j2));
                            }
                        } else {
                            ((Spliterator.OfPrimitive) this.f12621s).forEachRemaining((Spliterator.OfPrimitive) t_cons);
                            return;
                        }
                    } else {
                        return;
                    }
                }
            }
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$UnorderedSliceSpliterator$OfInt.class */
        static final class OfInt extends OfPrimitive<Integer, IntConsumer, ArrayBuffer.OfInt, Spliterator.OfInt> implements Spliterator.OfInt, IntConsumer {
            int tmpValue;

            @Override // java.util.Spliterator.OfInt
            public /* bridge */ /* synthetic */ void forEachRemaining(IntConsumer intConsumer) {
                super.forEachRemaining((OfInt) intConsumer);
            }

            @Override // java.util.Spliterator.OfInt
            public /* bridge */ /* synthetic */ boolean tryAdvance(IntConsumer intConsumer) {
                return super.tryAdvance((OfInt) intConsumer);
            }

            @Override // java.util.stream.StreamSpliterators.UnorderedSliceSpliterator.OfPrimitive, java.util.stream.StreamSpliterators.UnorderedSliceSpliterator, java.util.Spliterator.OfPrimitive, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfInt trySplit() {
                return (Spliterator.OfInt) super.trySplit();
            }

            OfInt(Spliterator.OfInt ofInt, long j2, long j3) {
                super(ofInt, j2, j3);
            }

            OfInt(Spliterator.OfInt ofInt, OfInt ofInt2) {
                super(ofInt, ofInt2);
            }

            @Override // java.util.function.IntConsumer
            public void accept(int i2) {
                this.tmpValue = i2;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // java.util.stream.StreamSpliterators.UnorderedSliceSpliterator.OfPrimitive
            public void acceptConsumed(IntConsumer intConsumer) {
                intConsumer.accept(this.tmpValue);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // java.util.stream.StreamSpliterators.UnorderedSliceSpliterator.OfPrimitive
            public ArrayBuffer.OfInt bufferCreate(int i2) {
                return new ArrayBuffer.OfInt(i2);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // java.util.stream.StreamSpliterators.UnorderedSliceSpliterator
            public Spliterator.OfInt makeSpliterator(Spliterator.OfInt ofInt) {
                return new OfInt(ofInt, this);
            }
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$UnorderedSliceSpliterator$OfLong.class */
        static final class OfLong extends OfPrimitive<Long, LongConsumer, ArrayBuffer.OfLong, Spliterator.OfLong> implements Spliterator.OfLong, LongConsumer {
            long tmpValue;

            @Override // java.util.Spliterator.OfLong
            public /* bridge */ /* synthetic */ void forEachRemaining(LongConsumer longConsumer) {
                super.forEachRemaining((OfLong) longConsumer);
            }

            @Override // java.util.Spliterator.OfLong
            public /* bridge */ /* synthetic */ boolean tryAdvance(LongConsumer longConsumer) {
                return super.tryAdvance((OfLong) longConsumer);
            }

            @Override // java.util.stream.StreamSpliterators.UnorderedSliceSpliterator.OfPrimitive, java.util.stream.StreamSpliterators.UnorderedSliceSpliterator, java.util.Spliterator.OfPrimitive, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfLong trySplit() {
                return (Spliterator.OfLong) super.trySplit();
            }

            OfLong(Spliterator.OfLong ofLong, long j2, long j3) {
                super(ofLong, j2, j3);
            }

            OfLong(Spliterator.OfLong ofLong, OfLong ofLong2) {
                super(ofLong, ofLong2);
            }

            @Override // java.util.function.LongConsumer
            public void accept(long j2) {
                this.tmpValue = j2;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // java.util.stream.StreamSpliterators.UnorderedSliceSpliterator.OfPrimitive
            public void acceptConsumed(LongConsumer longConsumer) {
                longConsumer.accept(this.tmpValue);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // java.util.stream.StreamSpliterators.UnorderedSliceSpliterator.OfPrimitive
            public ArrayBuffer.OfLong bufferCreate(int i2) {
                return new ArrayBuffer.OfLong(i2);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // java.util.stream.StreamSpliterators.UnorderedSliceSpliterator
            public Spliterator.OfLong makeSpliterator(Spliterator.OfLong ofLong) {
                return new OfLong(ofLong, this);
            }
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$UnorderedSliceSpliterator$OfDouble.class */
        static final class OfDouble extends OfPrimitive<Double, DoubleConsumer, ArrayBuffer.OfDouble, Spliterator.OfDouble> implements Spliterator.OfDouble, DoubleConsumer {
            double tmpValue;

            @Override // java.util.Spliterator.OfDouble
            public /* bridge */ /* synthetic */ void forEachRemaining(DoubleConsumer doubleConsumer) {
                super.forEachRemaining((OfDouble) doubleConsumer);
            }

            @Override // java.util.Spliterator.OfDouble
            public /* bridge */ /* synthetic */ boolean tryAdvance(DoubleConsumer doubleConsumer) {
                return super.tryAdvance((OfDouble) doubleConsumer);
            }

            @Override // java.util.stream.StreamSpliterators.UnorderedSliceSpliterator.OfPrimitive, java.util.stream.StreamSpliterators.UnorderedSliceSpliterator, java.util.Spliterator.OfPrimitive, java.util.Spliterator
            public /* bridge */ /* synthetic */ Spliterator.OfDouble trySplit() {
                return (Spliterator.OfDouble) super.trySplit();
            }

            OfDouble(Spliterator.OfDouble ofDouble, long j2, long j3) {
                super(ofDouble, j2, j3);
            }

            OfDouble(Spliterator.OfDouble ofDouble, OfDouble ofDouble2) {
                super(ofDouble, ofDouble2);
            }

            @Override // java.util.function.DoubleConsumer
            public void accept(double d2) {
                this.tmpValue = d2;
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // java.util.stream.StreamSpliterators.UnorderedSliceSpliterator.OfPrimitive
            public void acceptConsumed(DoubleConsumer doubleConsumer) {
                doubleConsumer.accept(this.tmpValue);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // java.util.stream.StreamSpliterators.UnorderedSliceSpliterator.OfPrimitive
            public ArrayBuffer.OfDouble bufferCreate(int i2) {
                return new ArrayBuffer.OfDouble(i2);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            @Override // java.util.stream.StreamSpliterators.UnorderedSliceSpliterator
            public Spliterator.OfDouble makeSpliterator(Spliterator.OfDouble ofDouble) {
                return new OfDouble(ofDouble, this);
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/StreamSpliterators$DistinctSpliterator.class */
    static final class DistinctSpliterator<T> implements Spliterator<T>, Consumer<T> {
        private static final Object NULL_VALUE = new Object();

        /* renamed from: s, reason: collision with root package name */
        private final Spliterator<T> f12615s;
        private final ConcurrentHashMap<T, Boolean> seen;
        private T tmpSlot;

        DistinctSpliterator(Spliterator<T> spliterator) {
            this(spliterator, new ConcurrentHashMap());
        }

        private DistinctSpliterator(Spliterator<T> spliterator, ConcurrentHashMap<T, Boolean> concurrentHashMap) {
            this.f12615s = spliterator;
            this.seen = concurrentHashMap;
        }

        @Override // java.util.function.Consumer
        public void accept(T t2) {
            this.tmpSlot = t2;
        }

        private T mapNull(T t2) {
            return t2 != null ? t2 : (T) NULL_VALUE;
        }

        @Override // java.util.Spliterator
        public boolean tryAdvance(Consumer<? super T> consumer) {
            while (this.f12615s.tryAdvance(this)) {
                if (this.seen.putIfAbsent(mapNull(this.tmpSlot), Boolean.TRUE) == null) {
                    consumer.accept(this.tmpSlot);
                    this.tmpSlot = null;
                    return true;
                }
            }
            return false;
        }

        @Override // java.util.Spliterator
        public void forEachRemaining(Consumer<? super T> consumer) {
            this.f12615s.forEachRemaining(obj -> {
                if (this.seen.putIfAbsent(mapNull(obj), Boolean.TRUE) == null) {
                    consumer.accept(obj);
                }
            });
        }

        @Override // java.util.Spliterator
        public Spliterator<T> trySplit() {
            Spliterator<T> spliteratorTrySplit = this.f12615s.trySplit();
            if (spliteratorTrySplit != null) {
                return new DistinctSpliterator(spliteratorTrySplit, this.seen);
            }
            return null;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.f12615s.estimateSize();
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return (this.f12615s.characteristics() & (-16469)) | 1;
        }

        @Override // java.util.Spliterator
        public Comparator<? super T> getComparator() {
            return this.f12615s.getComparator();
        }
    }

    /* loaded from: rt.jar:java/util/stream/StreamSpliterators$InfiniteSupplyingSpliterator.class */
    static abstract class InfiniteSupplyingSpliterator<T> implements Spliterator<T> {
        long estimate;

        protected InfiniteSupplyingSpliterator(long j2) {
            this.estimate = j2;
        }

        @Override // java.util.Spliterator
        public long estimateSize() {
            return this.estimate;
        }

        @Override // java.util.Spliterator
        public int characteristics() {
            return 1024;
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$InfiniteSupplyingSpliterator$OfRef.class */
        static final class OfRef<T> extends InfiniteSupplyingSpliterator<T> {

            /* renamed from: s, reason: collision with root package name */
            final Supplier<T> f12619s;

            OfRef(long j2, Supplier<T> supplier) {
                super(j2);
                this.f12619s = supplier;
            }

            @Override // java.util.Spliterator
            public boolean tryAdvance(Consumer<? super T> consumer) {
                Objects.requireNonNull(consumer);
                consumer.accept(this.f12619s.get());
                return true;
            }

            @Override // java.util.Spliterator
            public Spliterator<T> trySplit() {
                if (this.estimate == 0) {
                    return null;
                }
                long j2 = this.estimate >>> 1;
                this.estimate = j2;
                return new OfRef(j2, this.f12619s);
            }
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$InfiniteSupplyingSpliterator$OfInt.class */
        static final class OfInt extends InfiniteSupplyingSpliterator<Integer> implements Spliterator.OfInt {

            /* renamed from: s, reason: collision with root package name */
            final IntSupplier f12617s;

            OfInt(long j2, IntSupplier intSupplier) {
                super(j2);
                this.f12617s = intSupplier;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Spliterator.OfInt, java.util.Spliterator.OfPrimitive
            public boolean tryAdvance(IntConsumer intConsumer) {
                Objects.requireNonNull(intConsumer);
                intConsumer.accept(this.f12617s.getAsInt());
                return true;
            }

            @Override // java.util.Spliterator
            public Spliterator.OfInt trySplit() {
                if (this.estimate == 0) {
                    return null;
                }
                long j2 = this.estimate >>> 1;
                this.estimate = j2;
                return new OfInt(j2, this.f12617s);
            }
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$InfiniteSupplyingSpliterator$OfLong.class */
        static final class OfLong extends InfiniteSupplyingSpliterator<Long> implements Spliterator.OfLong {

            /* renamed from: s, reason: collision with root package name */
            final LongSupplier f12618s;

            OfLong(long j2, LongSupplier longSupplier) {
                super(j2);
                this.f12618s = longSupplier;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Spliterator.OfLong, java.util.Spliterator.OfPrimitive
            public boolean tryAdvance(LongConsumer longConsumer) {
                Objects.requireNonNull(longConsumer);
                longConsumer.accept(this.f12618s.getAsLong());
                return true;
            }

            @Override // java.util.Spliterator
            public Spliterator.OfLong trySplit() {
                if (this.estimate == 0) {
                    return null;
                }
                long j2 = this.estimate >>> 1;
                this.estimate = j2;
                return new OfLong(j2, this.f12618s);
            }
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$InfiniteSupplyingSpliterator$OfDouble.class */
        static final class OfDouble extends InfiniteSupplyingSpliterator<Double> implements Spliterator.OfDouble {

            /* renamed from: s, reason: collision with root package name */
            final DoubleSupplier f12616s;

            OfDouble(long j2, DoubleSupplier doubleSupplier) {
                super(j2);
                this.f12616s = doubleSupplier;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Spliterator.OfDouble, java.util.Spliterator.OfPrimitive
            public boolean tryAdvance(DoubleConsumer doubleConsumer) {
                Objects.requireNonNull(doubleConsumer);
                doubleConsumer.accept(this.f12616s.getAsDouble());
                return true;
            }

            @Override // java.util.Spliterator
            public Spliterator.OfDouble trySplit() {
                if (this.estimate == 0) {
                    return null;
                }
                long j2 = this.estimate >>> 1;
                this.estimate = j2;
                return new OfDouble(j2, this.f12616s);
            }
        }
    }

    /* loaded from: rt.jar:java/util/stream/StreamSpliterators$ArrayBuffer.class */
    static abstract class ArrayBuffer {
        int index;

        ArrayBuffer() {
        }

        void reset() {
            this.index = 0;
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$ArrayBuffer$OfRef.class */
        static final class OfRef<T> extends ArrayBuffer implements Consumer<T> {
            final Object[] array;

            OfRef(int i2) {
                this.array = new Object[i2];
            }

            @Override // java.util.function.Consumer
            public void accept(T t2) {
                Object[] objArr = this.array;
                int i2 = this.index;
                this.index = i2 + 1;
                objArr[i2] = t2;
            }

            public void forEach(Consumer<? super T> consumer, long j2) {
                for (int i2 = 0; i2 < j2; i2++) {
                    consumer.accept(this.array[i2]);
                }
            }
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$ArrayBuffer$OfPrimitive.class */
        static abstract class OfPrimitive<T_CONS> extends ArrayBuffer {
            int index;

            abstract void forEach(T_CONS t_cons, long j2);

            OfPrimitive() {
            }

            @Override // java.util.stream.StreamSpliterators.ArrayBuffer
            void reset() {
                this.index = 0;
            }
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$ArrayBuffer$OfInt.class */
        static final class OfInt extends OfPrimitive<IntConsumer> implements IntConsumer {
            final int[] array;

            OfInt(int i2) {
                this.array = new int[i2];
            }

            @Override // java.util.function.IntConsumer
            public void accept(int i2) {
                int[] iArr = this.array;
                int i3 = this.index;
                this.index = i3 + 1;
                iArr[i3] = i2;
            }

            @Override // java.util.stream.StreamSpliterators.ArrayBuffer.OfPrimitive
            public void forEach(IntConsumer intConsumer, long j2) {
                for (int i2 = 0; i2 < j2; i2++) {
                    intConsumer.accept(this.array[i2]);
                }
            }
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$ArrayBuffer$OfLong.class */
        static final class OfLong extends OfPrimitive<LongConsumer> implements LongConsumer {
            final long[] array;

            OfLong(int i2) {
                this.array = new long[i2];
            }

            @Override // java.util.function.LongConsumer
            public void accept(long j2) {
                long[] jArr = this.array;
                int i2 = this.index;
                this.index = i2 + 1;
                jArr[i2] = j2;
            }

            @Override // java.util.stream.StreamSpliterators.ArrayBuffer.OfPrimitive
            public void forEach(LongConsumer longConsumer, long j2) {
                for (int i2 = 0; i2 < j2; i2++) {
                    longConsumer.accept(this.array[i2]);
                }
            }
        }

        /* loaded from: rt.jar:java/util/stream/StreamSpliterators$ArrayBuffer$OfDouble.class */
        static final class OfDouble extends OfPrimitive<DoubleConsumer> implements DoubleConsumer {
            final double[] array;

            OfDouble(int i2) {
                this.array = new double[i2];
            }

            @Override // java.util.function.DoubleConsumer
            public void accept(double d2) {
                double[] dArr = this.array;
                int i2 = this.index;
                this.index = i2 + 1;
                dArr[i2] = d2;
            }

            /* JADX INFO: Access modifiers changed from: package-private */
            @Override // java.util.stream.StreamSpliterators.ArrayBuffer.OfPrimitive
            public void forEach(DoubleConsumer doubleConsumer, long j2) {
                for (int i2 = 0; i2 < j2; i2++) {
                    doubleConsumer.accept(this.array[i2]);
                }
            }
        }
    }
}
