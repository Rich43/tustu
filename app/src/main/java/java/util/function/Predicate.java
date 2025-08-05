package java.util.function;

import java.util.Objects;

@FunctionalInterface
/* loaded from: rt.jar:java/util/function/Predicate.class */
public interface Predicate<T> {
    boolean test(T t2);

    default Predicate<T> and(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        return obj -> {
            return test(obj) && predicate.test(obj);
        };
    }

    default Predicate<T> negate() {
        return obj -> {
            return !test(obj);
        };
    }

    default Predicate<T> or(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        return obj -> {
            return test(obj) || predicate.test(obj);
        };
    }

    static <T> Predicate<T> isEqual(Object obj) {
        return null == obj ? Objects::isNull : obj2 -> {
            return obj.equals(obj2);
        };
    }
}
