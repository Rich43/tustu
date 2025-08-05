package java.util.function;

import java.util.Objects;

@FunctionalInterface
/* loaded from: rt.jar:java/util/function/BiConsumer.class */
public interface BiConsumer<T, U> {
    void accept(T t2, U u2);

    default BiConsumer<T, U> andThen(BiConsumer<? super T, ? super U> biConsumer) {
        Objects.requireNonNull(biConsumer);
        return (obj, obj2) -> {
            accept(obj, obj2);
            biConsumer.accept(obj, obj2);
        };
    }
}
