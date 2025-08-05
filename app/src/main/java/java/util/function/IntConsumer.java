package java.util.function;

import java.util.Objects;

@FunctionalInterface
/* loaded from: rt.jar:java/util/function/IntConsumer.class */
public interface IntConsumer {
    void accept(int i2);

    default IntConsumer andThen(IntConsumer intConsumer) {
        Objects.requireNonNull(intConsumer);
        return i2 -> {
            accept(i2);
            intConsumer.accept(i2);
        };
    }
}
