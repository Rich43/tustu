package java.util;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/* loaded from: rt.jar:java/util/Optional.class */
public final class Optional<T> {
    private static final Optional<?> EMPTY = new Optional<>();
    private final T value;

    private Optional() {
        this.value = null;
    }

    public static <T> Optional<T> empty() {
        return (Optional<T>) EMPTY;
    }

    private Optional(T t2) {
        this.value = (T) Objects.requireNonNull(t2);
    }

    public static <T> Optional<T> of(T t2) {
        return new Optional<>(t2);
    }

    public static <T> Optional<T> ofNullable(T t2) {
        return t2 == null ? empty() : of(t2);
    }

    public T get() {
        if (this.value == null) {
            throw new NoSuchElementException("No value present");
        }
        return this.value;
    }

    public boolean isPresent() {
        return this.value != null;
    }

    public void ifPresent(Consumer<? super T> consumer) {
        if (this.value != null) {
            consumer.accept(this.value);
        }
    }

    public Optional<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (isPresent()) {
            return predicate.test(this.value) ? this : empty();
        }
        return this;
    }

    public <U> Optional<U> map(Function<? super T, ? extends U> function) {
        Objects.requireNonNull(function);
        if (!isPresent()) {
            return empty();
        }
        return ofNullable(function.apply(this.value));
    }

    public <U> Optional<U> flatMap(Function<? super T, Optional<U>> function) {
        Objects.requireNonNull(function);
        if (!isPresent()) {
            return empty();
        }
        return (Optional) Objects.requireNonNull(function.apply(this.value));
    }

    public T orElse(T t2) {
        return this.value != null ? this.value : t2;
    }

    public T orElseGet(Supplier<? extends T> supplier) {
        return this.value != null ? this.value : supplier.get();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: X extends java.lang.Throwable */
    public <X extends Throwable> T orElseThrow(Supplier<? extends X> supplier) throws Throwable {
        if (this.value != null) {
            return this.value;
        }
        throw supplier.get();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Optional)) {
            return false;
        }
        return Objects.equals(this.value, ((Optional) obj).value);
    }

    public int hashCode() {
        return Objects.hashCode(this.value);
    }

    public String toString() {
        return this.value != null ? String.format("Optional[%s]", this.value) : "Optional.empty";
    }
}
