package java.util.stream;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;
import java.util.Iterator;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.PrimitiveIterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.DoubleBinaryOperator;
import java.util.function.DoubleConsumer;
import java.util.function.DoubleFunction;
import java.util.function.DoublePredicate;
import java.util.function.DoubleSupplier;
import java.util.function.DoubleToIntFunction;
import java.util.function.DoubleToLongFunction;
import java.util.function.DoubleUnaryOperator;
import java.util.function.ObjDoubleConsumer;
import java.util.function.Supplier;
import java.util.stream.StreamSpliterators;
import java.util.stream.Streams;

/* loaded from: rt.jar:java/util/stream/DoubleStream.class */
public interface DoubleStream extends BaseStream<Double, DoubleStream> {
    DoubleStream filter(DoublePredicate doublePredicate);

    DoubleStream map(DoubleUnaryOperator doubleUnaryOperator);

    <U> Stream<U> mapToObj(DoubleFunction<? extends U> doubleFunction);

    IntStream mapToInt(DoubleToIntFunction doubleToIntFunction);

    LongStream mapToLong(DoubleToLongFunction doubleToLongFunction);

    DoubleStream flatMap(DoubleFunction<? extends DoubleStream> doubleFunction);

    DoubleStream distinct();

    DoubleStream sorted();

    DoubleStream peek(DoubleConsumer doubleConsumer);

    DoubleStream limit(long j2);

    DoubleStream skip(long j2);

    void forEach(DoubleConsumer doubleConsumer);

    void forEachOrdered(DoubleConsumer doubleConsumer);

    double[] toArray();

    double reduce(double d2, DoubleBinaryOperator doubleBinaryOperator);

    OptionalDouble reduce(DoubleBinaryOperator doubleBinaryOperator);

    <R> R collect(Supplier<R> supplier, ObjDoubleConsumer<R> objDoubleConsumer, BiConsumer<R, R> biConsumer);

    double sum();

    OptionalDouble min();

    OptionalDouble max();

    long count();

    OptionalDouble average();

    DoubleSummaryStatistics summaryStatistics();

    boolean anyMatch(DoublePredicate doublePredicate);

    boolean allMatch(DoublePredicate doublePredicate);

    boolean noneMatch(DoublePredicate doublePredicate);

    OptionalDouble findFirst();

    OptionalDouble findAny();

    Stream<Double> boxed();

    @Override // java.util.stream.BaseStream
    DoubleStream sequential();

    @Override // java.util.stream.BaseStream
    DoubleStream parallel();

    @Override // java.util.stream.BaseStream, java.util.stream.DoubleStream
    /* renamed from: iterator */
    Iterator<Double> iterator2();

    @Override // java.util.stream.BaseStream
    /* renamed from: spliterator */
    Spliterator<Double> spliterator2();

    static Builder builder() {
        return new Streams.DoubleStreamBuilderImpl();
    }

    static DoubleStream empty() {
        return StreamSupport.doubleStream(Spliterators.emptyDoubleSpliterator(), false);
    }

    static DoubleStream of(double d2) {
        return StreamSupport.doubleStream(new Streams.DoubleStreamBuilderImpl(d2), false);
    }

    static DoubleStream of(double... dArr) {
        return Arrays.stream(dArr);
    }

    static DoubleStream iterate(final double d2, final DoubleUnaryOperator doubleUnaryOperator) {
        Objects.requireNonNull(doubleUnaryOperator);
        return StreamSupport.doubleStream(Spliterators.spliteratorUnknownSize(new PrimitiveIterator.OfDouble() { // from class: java.util.stream.DoubleStream.1

            /* renamed from: t, reason: collision with root package name */
            double f12598t;

            {
                this.f12598t = d2;
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return true;
            }

            @Override // java.util.PrimitiveIterator.OfDouble
            public double nextDouble() {
                double d3 = this.f12598t;
                this.f12598t = doubleUnaryOperator.applyAsDouble(this.f12598t);
                return d3;
            }
        }, 1296), false);
    }

    static DoubleStream generate(DoubleSupplier doubleSupplier) {
        Objects.requireNonNull(doubleSupplier);
        return StreamSupport.doubleStream(new StreamSpliterators.InfiniteSupplyingSpliterator.OfDouble(Long.MAX_VALUE, doubleSupplier), false);
    }

    /* JADX WARN: Type inference failed for: r2v1, types: [java.util.Spliterator$OfDouble] */
    /* JADX WARN: Type inference failed for: r3v1, types: [java.util.Spliterator$OfDouble] */
    static DoubleStream concat(DoubleStream doubleStream, DoubleStream doubleStream2) {
        Objects.requireNonNull(doubleStream);
        Objects.requireNonNull(doubleStream2);
        return StreamSupport.doubleStream(new Streams.ConcatSpliterator.OfDouble(doubleStream.spliterator2(), doubleStream2.spliterator2()), doubleStream.isParallel() || doubleStream2.isParallel()).onClose(Streams.composedClose(doubleStream, doubleStream2));
    }

    /* loaded from: rt.jar:java/util/stream/DoubleStream$Builder.class */
    public interface Builder extends DoubleConsumer {
        @Override // java.util.function.DoubleConsumer
        void accept(double d2);

        DoubleStream build();

        default Builder add(double d2) {
            accept(d2);
            return this;
        }
    }
}
