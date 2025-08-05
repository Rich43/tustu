package java.util.stream;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/* loaded from: rt.jar:java/util/stream/Collector.class */
public interface Collector<T, A, R> {

    /* loaded from: rt.jar:java/util/stream/Collector$Characteristics.class */
    public enum Characteristics {
        CONCURRENT,
        UNORDERED,
        IDENTITY_FINISH
    }

    Supplier<A> supplier();

    BiConsumer<A, T> accumulator();

    BinaryOperator<A> combiner();

    Function<A, R> finisher();

    Set<Characteristics> characteristics();

    static <T, R> Collector<T, R, R> of(Supplier<R> supplier, BiConsumer<R, T> biConsumer, BinaryOperator<R> binaryOperator, Characteristics... characteristicsArr) {
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(biConsumer);
        Objects.requireNonNull(binaryOperator);
        Objects.requireNonNull(characteristicsArr);
        return new Collectors.CollectorImpl(supplier, biConsumer, binaryOperator, characteristicsArr.length == 0 ? Collectors.CH_ID : Collections.unmodifiableSet(EnumSet.of(Characteristics.IDENTITY_FINISH, characteristicsArr)));
    }

    static <T, A, R> Collector<T, A, R> of(Supplier<A> supplier, BiConsumer<A, T> biConsumer, BinaryOperator<A> binaryOperator, Function<A, R> function, Characteristics... characteristicsArr) {
        Objects.requireNonNull(supplier);
        Objects.requireNonNull(biConsumer);
        Objects.requireNonNull(binaryOperator);
        Objects.requireNonNull(function);
        Objects.requireNonNull(characteristicsArr);
        Set<Characteristics> setUnmodifiableSet = Collectors.CH_NOID;
        if (characteristicsArr.length > 0) {
            EnumSet enumSetNoneOf = EnumSet.noneOf(Characteristics.class);
            Collections.addAll(enumSetNoneOf, characteristicsArr);
            setUnmodifiableSet = Collections.unmodifiableSet(enumSetNoneOf);
        }
        return new Collectors.CollectorImpl(supplier, biConsumer, binaryOperator, function, setUnmodifiableSet);
    }
}
