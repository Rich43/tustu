package java.util.stream;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LongSummaryStatistics;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalLong;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.LongBinaryOperator;
import java.util.function.LongConsumer;
import java.util.function.LongFunction;
import java.util.function.LongPredicate;
import java.util.function.LongSupplier;
import java.util.function.LongToDoubleFunction;
import java.util.function.LongToIntFunction;
import java.util.function.LongUnaryOperator;
import java.util.function.ObjLongConsumer;
import java.util.function.Supplier;
import java.util.stream.StreamSpliterators;
import java.util.stream.Streams;

/* loaded from: rt.jar:java/util/stream/LongStream.class */
public interface LongStream extends BaseStream<Long, LongStream> {
    LongStream filter(LongPredicate longPredicate);

    LongStream map(LongUnaryOperator longUnaryOperator);

    <U> Stream<U> mapToObj(LongFunction<? extends U> longFunction);

    IntStream mapToInt(LongToIntFunction longToIntFunction);

    DoubleStream mapToDouble(LongToDoubleFunction longToDoubleFunction);

    LongStream flatMap(LongFunction<? extends LongStream> longFunction);

    LongStream distinct();

    LongStream sorted();

    LongStream peek(LongConsumer longConsumer);

    LongStream limit(long j2);

    LongStream skip(long j2);

    void forEach(LongConsumer longConsumer);

    void forEachOrdered(LongConsumer longConsumer);

    long[] toArray();

    long reduce(long j2, LongBinaryOperator longBinaryOperator);

    OptionalLong reduce(LongBinaryOperator longBinaryOperator);

    <R> R collect(Supplier<R> supplier, ObjLongConsumer<R> objLongConsumer, BiConsumer<R, R> biConsumer);

    long sum();

    OptionalLong min();

    OptionalLong max();

    long count();

    OptionalDouble average();

    LongSummaryStatistics summaryStatistics();

    boolean anyMatch(LongPredicate longPredicate);

    boolean allMatch(LongPredicate longPredicate);

    boolean noneMatch(LongPredicate longPredicate);

    OptionalLong findFirst();

    OptionalLong findAny();

    DoubleStream asDoubleStream();

    Stream<Long> boxed();

    @Override // java.util.stream.BaseStream
    LongStream sequential();

    @Override // java.util.stream.BaseStream
    LongStream parallel();

    @Override // java.util.stream.BaseStream, java.util.stream.DoubleStream
    /* renamed from: iterator */
    Iterator<Long> iterator2();

    @Override // java.util.stream.BaseStream
    /* renamed from: spliterator */
    Spliterator<Long> spliterator2();

    static Builder builder() {
        return new Streams.LongStreamBuilderImpl();
    }

    static LongStream empty() {
        return StreamSupport.longStream(Spliterators.emptyLongSpliterator(), false);
    }

    static LongStream of(long j2) {
        return StreamSupport.longStream(new Streams.LongStreamBuilderImpl(j2), false);
    }

    static LongStream of(long... jArr) {
        return Arrays.stream(jArr);
    }

    static LongStream iterate(final long j2, final LongUnaryOperator longUnaryOperator) {
        Objects.requireNonNull(longUnaryOperator);
        return StreamSupport.longStream(Spliterators.spliteratorUnknownSize(new PrimitiveIterator.OfLong() { // from class: java.util.stream.LongStream.1

            /* renamed from: t, reason: collision with root package name */
            long f12600t;

            {
                this.f12600t = j2;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return true;
            }

            @Override // java.util.PrimitiveIterator.OfLong
            public long nextLong() {
                long j3 = this.f12600t;
                this.f12600t = longUnaryOperator.applyAsLong(this.f12600t);
                return j3;
            }
        }, 1296), false);
    }

    static LongStream generate(LongSupplier longSupplier) {
        Objects.requireNonNull(longSupplier);
        return StreamSupport.longStream(new StreamSpliterators.InfiniteSupplyingSpliterator.OfLong(Long.MAX_VALUE, longSupplier), false);
    }

    static LongStream range(long j2, long j3) {
        if (j2 >= j3) {
            return empty();
        }
        if (j3 - j2 < 0) {
            long jDivideUnsigned = j2 + Long.divideUnsigned(j3 - j2, 2L) + 1;
            return concat(range(j2, jDivideUnsigned), range(jDivideUnsigned, j3));
        }
        return StreamSupport.longStream(new Streams.RangeLongSpliterator(j2, j3, false), false);
    }

    static LongStream rangeClosed(long j2, long j3) {
        if (j2 > j3) {
            return empty();
        }
        if ((j3 - j2) + 1 <= 0) {
            long jDivideUnsigned = j2 + Long.divideUnsigned(j3 - j2, 2L) + 1;
            return concat(range(j2, jDivideUnsigned), rangeClosed(jDivideUnsigned, j3));
        }
        return StreamSupport.longStream(new Streams.RangeLongSpliterator(j2, j3, true), false);
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [java.util.Spliterator$OfLong] */
    /* JADX WARN: Type inference failed for: r3v1, types: [java.util.Spliterator$OfLong] */
    static LongStream concat(LongStream longStream, LongStream longStream2) {
        Objects.requireNonNull(longStream);
        Objects.requireNonNull(longStream2);
        return StreamSupport.longStream(new Streams.ConcatSpliterator.OfLong(longStream.spliterator2(), longStream2.spliterator2()), longStream.isParallel() || longStream2.isParallel()).onClose(Streams.composedClose(longStream, longStream2));
    }

    /* loaded from: rt.jar:java/util/stream/LongStream$Builder.class */
    public interface Builder extends LongConsumer {
        @Override // java.util.function.LongConsumer
        void accept(long j2);

        LongStream build();

        default Builder add(long j2) {
            accept(j2);
            return this;
        }
    }
}
