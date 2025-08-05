package java.util.stream;

import java.util.Arrays;
import java.util.IntSummaryStatistics;
import java.util.Iterator;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.IntBinaryOperator;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;
import java.util.function.IntSupplier;
import java.util.function.IntToDoubleFunction;
import java.util.function.IntToLongFunction;
import java.util.function.IntUnaryOperator;
import java.util.function.ObjIntConsumer;
import java.util.function.Supplier;
import java.util.stream.StreamSpliterators;
import java.util.stream.Streams;

/* loaded from: rt.jar:java/util/stream/IntStream.class */
public interface IntStream extends BaseStream<Integer, IntStream> {
    IntStream filter(IntPredicate intPredicate);

    IntStream map(IntUnaryOperator intUnaryOperator);

    <U> Stream<U> mapToObj(IntFunction<? extends U> intFunction);

    LongStream mapToLong(IntToLongFunction intToLongFunction);

    DoubleStream mapToDouble(IntToDoubleFunction intToDoubleFunction);

    IntStream flatMap(IntFunction<? extends IntStream> intFunction);

    IntStream distinct();

    IntStream sorted();

    IntStream peek(IntConsumer intConsumer);

    IntStream limit(long j2);

    IntStream skip(long j2);

    void forEach(IntConsumer intConsumer);

    void forEachOrdered(IntConsumer intConsumer);

    int[] toArray();

    int reduce(int i2, IntBinaryOperator intBinaryOperator);

    OptionalInt reduce(IntBinaryOperator intBinaryOperator);

    <R> R collect(Supplier<R> supplier, ObjIntConsumer<R> objIntConsumer, BiConsumer<R, R> biConsumer);

    int sum();

    OptionalInt min();

    OptionalInt max();

    long count();

    OptionalDouble average();

    IntSummaryStatistics summaryStatistics();

    boolean anyMatch(IntPredicate intPredicate);

    boolean allMatch(IntPredicate intPredicate);

    boolean noneMatch(IntPredicate intPredicate);

    OptionalInt findFirst();

    OptionalInt findAny();

    LongStream asLongStream();

    DoubleStream asDoubleStream();

    Stream<Integer> boxed();

    @Override // java.util.stream.BaseStream
    IntStream sequential();

    @Override // java.util.stream.BaseStream
    IntStream parallel();

    @Override // java.util.stream.BaseStream, java.util.stream.DoubleStream
    /* renamed from: iterator, reason: merged with bridge method [inline-methods] */
    Iterator<Integer> iterator2();

    @Override // java.util.stream.BaseStream
    /* renamed from: spliterator, reason: merged with bridge method [inline-methods] */
    Spliterator<Integer> spliterator2();

    static Builder builder() {
        return new Streams.IntStreamBuilderImpl();
    }

    static IntStream empty() {
        return StreamSupport.intStream(Spliterators.emptyIntSpliterator(), false);
    }

    static IntStream of(int i2) {
        return StreamSupport.intStream(new Streams.IntStreamBuilderImpl(i2), false);
    }

    static IntStream of(int... iArr) {
        return Arrays.stream(iArr);
    }

    static IntStream iterate(final int i2, final IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        return StreamSupport.intStream(Spliterators.spliteratorUnknownSize(new PrimitiveIterator.OfInt() { // from class: java.util.stream.IntStream.1

            /* renamed from: t, reason: collision with root package name */
            int f12599t;

            {
                this.f12599t = i2;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return true;
            }

            @Override // java.util.PrimitiveIterator.OfInt
            public int nextInt() {
                int i3 = this.f12599t;
                this.f12599t = intUnaryOperator.applyAsInt(this.f12599t);
                return i3;
            }
        }, 1296), false);
    }

    static IntStream generate(IntSupplier intSupplier) {
        Objects.requireNonNull(intSupplier);
        return StreamSupport.intStream(new StreamSpliterators.InfiniteSupplyingSpliterator.OfInt(Long.MAX_VALUE, intSupplier), false);
    }

    static IntStream range(int i2, int i3) {
        if (i2 >= i3) {
            return empty();
        }
        return StreamSupport.intStream(new Streams.RangeIntSpliterator(i2, i3, false), false);
    }

    static IntStream rangeClosed(int i2, int i3) {
        if (i2 > i3) {
            return empty();
        }
        return StreamSupport.intStream(new Streams.RangeIntSpliterator(i2, i3, true), false);
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [java.util.Spliterator$OfInt] */
    /* JADX WARN: Type inference failed for: r3v1, types: [java.util.Spliterator$OfInt] */
    static IntStream concat(IntStream intStream, IntStream intStream2) {
        Objects.requireNonNull(intStream);
        Objects.requireNonNull(intStream2);
        return StreamSupport.intStream(new Streams.ConcatSpliterator.OfInt(intStream.spliterator2(), intStream2.spliterator2()), intStream.isParallel() || intStream2.isParallel()).onClose(Streams.composedClose(intStream, intStream2));
    }

    /* loaded from: rt.jar:java/util/stream/IntStream$Builder.class */
    public interface Builder extends IntConsumer {
        @Override // java.util.function.IntConsumer
        void accept(int i2);

        IntStream build();

        default Builder add(int i2) {
            accept(i2);
            return this;
        }
    }
}
