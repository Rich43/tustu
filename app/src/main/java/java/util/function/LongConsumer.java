package java.util.function;

import java.util.Objects;

@FunctionalInterface
/* loaded from: rt.jar:java/util/function/LongConsumer.class */
public interface LongConsumer {
    void accept(long j2);

    default LongConsumer andThen(LongConsumer longConsumer) {
        Objects.requireNonNull(longConsumer);
        return j2 -> {
            accept(j2);
            longConsumer.accept(j2);
        };
    }
}
