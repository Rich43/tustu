package java.util.function;

import java.util.Objects;

@FunctionalInterface
/* loaded from: rt.jar:java/util/function/DoubleUnaryOperator.class */
public interface DoubleUnaryOperator {
    double applyAsDouble(double d2);

    default DoubleUnaryOperator compose(DoubleUnaryOperator doubleUnaryOperator) {
        Objects.requireNonNull(doubleUnaryOperator);
        return d2 -> {
            return applyAsDouble(doubleUnaryOperator.applyAsDouble(d2));
        };
    }

    default DoubleUnaryOperator andThen(DoubleUnaryOperator doubleUnaryOperator) {
        Objects.requireNonNull(doubleUnaryOperator);
        return d2 -> {
            return doubleUnaryOperator.applyAsDouble(applyAsDouble(d2));
        };
    }

    static DoubleUnaryOperator identity() {
        return d2 -> {
            return d2;
        };
    }
}
