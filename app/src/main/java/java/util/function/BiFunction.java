package java.util.function;

import java.util.Objects;

@FunctionalInterface
/* loaded from: rt.jar:java/util/function/BiFunction.class */
public interface BiFunction<T, U, R> {
    R apply(T t2, U u2);

    default <V> BiFunction<T, U, V> andThen(Function<? super R, ? extends V> function) {
        Objects.requireNonNull(function);
        return (obj, obj2) -> {
            return function.apply(apply(obj, obj2));
        };
    }
}
