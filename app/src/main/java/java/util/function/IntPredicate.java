package java.util.function;

import java.util.Objects;

@FunctionalInterface
/* loaded from: rt.jar:java/util/function/IntPredicate.class */
public interface IntPredicate {
    boolean test(int i2);

    default IntPredicate and(IntPredicate intPredicate) {
        Objects.requireNonNull(intPredicate);
        return i2 -> {
            return test(i2) && intPredicate.test(i2);
        };
    }

    default IntPredicate negate() {
        return i2 -> {
            return !test(i2);
        };
    }

    default IntPredicate or(IntPredicate intPredicate) {
        Objects.requireNonNull(intPredicate);
        return i2 -> {
            return test(i2) || intPredicate.test(i2);
        };
    }
}
