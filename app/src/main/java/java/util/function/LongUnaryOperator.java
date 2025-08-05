package java.util.function;

import java.util.Objects;

@FunctionalInterface
/* loaded from: rt.jar:java/util/function/LongUnaryOperator.class */
public interface LongUnaryOperator {
    long applyAsLong(long j2);

    default LongUnaryOperator compose(LongUnaryOperator longUnaryOperator) {
        Objects.requireNonNull(longUnaryOperator);
        return j2 -> {
            return applyAsLong(longUnaryOperator.applyAsLong(j2));
        };
    }

    default LongUnaryOperator andThen(LongUnaryOperator longUnaryOperator) {
        Objects.requireNonNull(longUnaryOperator);
        return j2 -> {
            return longUnaryOperator.applyAsLong(applyAsLong(j2));
        };
    }

    static LongUnaryOperator identity() {
        return j2 -> {
            return j2;
        };
    }
}
