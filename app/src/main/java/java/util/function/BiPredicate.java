package java.util.function;

import java.util.Objects;

@FunctionalInterface
/* loaded from: rt.jar:java/util/function/BiPredicate.class */
public interface BiPredicate<T, U> {
    boolean test(T t2, U u2);

    default BiPredicate<T, U> and(BiPredicate<? super T, ? super U> biPredicate) {
        Objects.requireNonNull(biPredicate);
        return (obj, obj2) -> {
            return test(obj, obj2) && biPredicate.test(obj, obj2);
        };
    }

    default BiPredicate<T, U> negate() {
        return (obj, obj2) -> {
            return !test(obj, obj2);
        };
    }

    default BiPredicate<T, U> or(BiPredicate<? super T, ? super U> biPredicate) {
        Objects.requireNonNull(biPredicate);
        return (obj, obj2) -> {
            return test(obj, obj2) || biPredicate.test(obj, obj2);
        };
    }
}
