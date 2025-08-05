package java.util.stream;

import com.sun.xml.internal.fastinfoset.EncodingConstants;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.Spliterators;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.function.UnaryOperator;
import java.util.stream.StreamSpliterators;
import java.util.stream.Streams;

/* loaded from: rt.jar:java/util/stream/Stream.class */
public interface Stream<T> extends BaseStream<T, Stream<T>> {
    Stream<T> filter(Predicate<? super T> predicate);

    <R> Stream<R> map(Function<? super T, ? extends R> function);

    IntStream mapToInt(ToIntFunction<? super T> toIntFunction);

    LongStream mapToLong(ToLongFunction<? super T> toLongFunction);

    DoubleStream mapToDouble(ToDoubleFunction<? super T> toDoubleFunction);

    <R> Stream<R> flatMap(Function<? super T, ? extends Stream<? extends R>> function);

    IntStream flatMapToInt(Function<? super T, ? extends IntStream> function);

    LongStream flatMapToLong(Function<? super T, ? extends LongStream> function);

    DoubleStream flatMapToDouble(Function<? super T, ? extends DoubleStream> function);

    Stream<T> distinct();

    Stream<T> sorted();

    Stream<T> sorted(Comparator<? super T> comparator);

    Stream<T> peek(Consumer<? super T> consumer);

    Stream<T> limit(long j2);

    Stream<T> skip(long j2);

    void forEach(Consumer<? super T> consumer);

    void forEachOrdered(Consumer<? super T> consumer);

    Object[] toArray();

    <A> A[] toArray(IntFunction<A[]> intFunction);

    T reduce(T t2, BinaryOperator<T> binaryOperator);

    Optional<T> reduce(BinaryOperator<T> binaryOperator);

    <U> U reduce(U u2, BiFunction<U, ? super T, U> biFunction, BinaryOperator<U> binaryOperator);

    <R> R collect(Supplier<R> supplier, BiConsumer<R, ? super T> biConsumer, BiConsumer<R, R> biConsumer2);

    <R, A> R collect(Collector<? super T, A, R> collector);

    Optional<T> min(Comparator<? super T> comparator);

    Optional<T> max(Comparator<? super T> comparator);

    long count();

    boolean anyMatch(Predicate<? super T> predicate);

    boolean allMatch(Predicate<? super T> predicate);

    boolean noneMatch(Predicate<? super T> predicate);

    Optional<T> findFirst();

    Optional<T> findAny();

    static <T> Builder<T> builder() {
        return new Streams.StreamBuilderImpl();
    }

    static <T> Stream<T> empty() {
        return StreamSupport.stream(Spliterators.emptySpliterator(), false);
    }

    static <T> Stream<T> of(T t2) {
        return StreamSupport.stream(new Streams.StreamBuilderImpl(t2), false);
    }

    @SafeVarargs
    static <T> Stream<T> of(T... tArr) {
        return Arrays.stream(tArr);
    }

    static <T> Stream<T> iterate(final T t2, final UnaryOperator<T> unaryOperator) {
        Objects.requireNonNull(unaryOperator);
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(new Iterator<T>() { // from class: java.util.stream.Stream.1

            /* renamed from: t, reason: collision with root package name */
            T f12613t = (T) Streams.NONE;

            @Override // java.util.Iterator
            public boolean hasNext() {
                return true;
            }

            @Override // java.util.Iterator
            public T next() {
                T tApply = this.f12613t == Streams.NONE ? (T) t2 : unaryOperator.apply(this.f12613t);
                this.f12613t = tApply;
                return tApply;
            }
        }, EncodingConstants.INTEGER_4TH_BIT_MEDIUM_LIMIT), false);
    }

    static <T> Stream<T> generate(Supplier<T> supplier) {
        Objects.requireNonNull(supplier);
        return StreamSupport.stream(new StreamSpliterators.InfiniteSupplyingSpliterator.OfRef(Long.MAX_VALUE, supplier), false);
    }

    static <T> Stream<T> concat(Stream<? extends T> stream, Stream<? extends T> stream2) {
        Objects.requireNonNull(stream);
        Objects.requireNonNull(stream2);
        return StreamSupport.stream(new Streams.ConcatSpliterator.OfRef(stream.spliterator2(), stream2.spliterator2()), stream.isParallel() || stream2.isParallel()).onClose(Streams.composedClose(stream, stream2));
    }

    /* loaded from: rt.jar:java/util/stream/Stream$Builder.class */
    public interface Builder<T> extends Consumer<T> {
        @Override // java.util.function.Consumer
        void accept(T t2);

        Stream<T> build();

        default Builder<T> add(T t2) {
            accept(t2);
            return this;
        }
    }
}
