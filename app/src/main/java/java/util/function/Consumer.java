package java.util.function;

import java.util.Objects;

@FunctionalInterface
/* loaded from: rt.jar:java/util/function/Consumer.class */
public interface Consumer<T> {
    void accept(T t2);

    default Consumer<T> andThen(Consumer<? super T> consumer) {
        Objects.requireNonNull(consumer);
        return obj -> {
            accept(obj);
            consumer.accept(obj);
        };
    }
}
