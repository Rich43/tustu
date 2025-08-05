package java.util.function;

import java.util.Objects;

@FunctionalInterface
/* loaded from: rt.jar:java/util/function/DoublePredicate.class */
public interface DoublePredicate {
    boolean test(double d2);

    default DoublePredicate and(DoublePredicate doublePredicate) {
        Objects.requireNonNull(doublePredicate);
        return d2 -> {
            return test(d2) && doublePredicate.test(d2);
        };
    }

    default DoublePredicate negate() {
        return d2 -> {
            return !test(d2);
        };
    }

    default DoublePredicate or(DoublePredicate doublePredicate) {
        Objects.requireNonNull(doublePredicate);
        return d2 -> {
            return test(d2) || doublePredicate.test(d2);
        };
    }
}
