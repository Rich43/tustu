package java.util.function;

import java.util.Objects;

@FunctionalInterface
/* loaded from: rt.jar:java/util/function/Function.class */
public interface Function<T, R> {
    R apply(T t2);

    default <V> Function<V, R> compose(Function<? super V, ? extends T> function) {
        Objects.requireNonNull(function);
        return obj -> {
            return apply(function.apply(obj));
        };
    }

    default <V> Function<T, V> andThen(Function<? super R, ? extends V> function) {
        Objects.requireNonNull(function);
        return obj -> {
            return function.apply(apply(obj));
        };
    }

    static <T> Function<T, T> identity() {
        return obj -> {
            return obj;
        };
    }
}
