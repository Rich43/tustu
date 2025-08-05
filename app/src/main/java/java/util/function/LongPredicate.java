package java.util.function;

import java.util.Objects;

@FunctionalInterface
/* loaded from: rt.jar:java/util/function/LongPredicate.class */
public interface LongPredicate {
    boolean test(long j2);

    default LongPredicate and(LongPredicate longPredicate) {
        Objects.requireNonNull(longPredicate);
        return j2 -> {
            return test(j2) && longPredicate.test(j2);
        };
    }

    default LongPredicate negate() {
        return j2 -> {
            return !test(j2);
        };
    }

    default LongPredicate or(LongPredicate longPredicate) {
        Objects.requireNonNull(longPredicate);
        return j2 -> {
            return test(j2) || longPredicate.test(j2);
        };
    }
}
