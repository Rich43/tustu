package java.util;

import java.util.function.LongConsumer;
import java.util.function.LongSupplier;
import java.util.function.Supplier;

/* loaded from: rt.jar:java/util/OptionalLong.class */
public final class OptionalLong {
    private static final OptionalLong EMPTY = new OptionalLong();
    private final boolean isPresent;
    private final long value;

    private OptionalLong() {
        this.isPresent = false;
        this.value = 0L;
    }

    public static OptionalLong empty() {
        return EMPTY;
    }

    private OptionalLong(long j2) {
        this.isPresent = true;
        this.value = j2;
    }

    public static OptionalLong of(long j2) {
        return new OptionalLong(j2);
    }

    public long getAsLong() {
        if (!this.isPresent) {
            throw new NoSuchElementException("No value present");
        }
        return this.value;
    }

    public boolean isPresent() {
        return this.isPresent;
    }

    public void ifPresent(LongConsumer longConsumer) {
        if (this.isPresent) {
            longConsumer.accept(this.value);
        }
    }

    public long orElse(long j2) {
        return this.isPresent ? this.value : j2;
    }

    public long orElseGet(LongSupplier longSupplier) {
        return this.isPresent ? this.value : longSupplier.getAsLong();
    }

    /* JADX INFO: Thrown type has an unknown type hierarchy: X extends java.lang.Throwable */
    public <X extends Throwable> long orElseThrow(Supplier<X> supplier) throws Throwable {
        if (this.isPresent) {
            return this.value;
        }
        throw supplier.get();
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OptionalLong)) {
            return false;
        }
        OptionalLong optionalLong = (OptionalLong) obj;
        return (this.isPresent && optionalLong.isPresent) ? this.value == optionalLong.value : this.isPresent == optionalLong.isPresent;
    }

    public int hashCode() {
        if (this.isPresent) {
            return Long.hashCode(this.value);
        }
        return 0;
    }

    public String toString() {
        return this.isPresent ? String.format("OptionalLong[%s]", Long.valueOf(this.value)) : "OptionalLong.empty";
    }
}
