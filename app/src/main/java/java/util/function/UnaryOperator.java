package java.util.function;

@FunctionalInterface
/* loaded from: rt.jar:java/util/function/UnaryOperator.class */
public interface UnaryOperator<T> extends Function<T, T> {
    static <T> UnaryOperator<T> identity() {
        return obj -> {
            return obj;
        };
    }
}
