package java.util.function;

import java.util.Comparator;
import java.util.Objects;

@FunctionalInterface
/* loaded from: rt.jar:java/util/function/BinaryOperator.class */
public interface BinaryOperator<T> extends BiFunction<T, T, T> {
    static <T> BinaryOperator<T> minBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return (obj, obj2) -> {
            return comparator.compare(obj, obj2) <= 0 ? obj : obj2;
        };
    }

    static <T> BinaryOperator<T> maxBy(Comparator<? super T> comparator) {
        Objects.requireNonNull(comparator);
        return (obj, obj2) -> {
            return comparator.compare(obj, obj2) >= 0 ? obj : obj2;
        };
    }
}
