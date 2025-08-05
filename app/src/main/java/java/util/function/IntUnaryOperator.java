package java.util.function;

import java.util.Objects;

@FunctionalInterface
/* loaded from: rt.jar:java/util/function/IntUnaryOperator.class */
public interface IntUnaryOperator {
    int applyAsInt(int i2);

    default IntUnaryOperator compose(IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        return i2 -> {
            return applyAsInt(intUnaryOperator.applyAsInt(i2));
        };
    }

    default IntUnaryOperator andThen(IntUnaryOperator intUnaryOperator) {
        Objects.requireNonNull(intUnaryOperator);
        return i2 -> {
            return intUnaryOperator.applyAsInt(applyAsInt(i2));
        };
    }

    static IntUnaryOperator identity() {
        return i2 -> {
            return i2;
        };
    }
}
