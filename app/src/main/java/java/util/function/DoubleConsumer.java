package java.util.function;

import java.util.Objects;

@FunctionalInterface
/* loaded from: rt.jar:java/util/function/DoubleConsumer.class */
public interface DoubleConsumer {
    void accept(double d2);

    default DoubleConsumer andThen(DoubleConsumer doubleConsumer) {
        Objects.requireNonNull(doubleConsumer);
        return d2 -> {
            accept(d2);
            doubleConsumer.accept(d2);
        };
    }
}
