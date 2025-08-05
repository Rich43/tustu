package jdk.nashorn.internal.runtime.options;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/options/Option.class */
public class Option<T> {
    protected T value;

    Option(T value) {
        this.value = value;
    }

    public T getValue() {
        return this.value;
    }

    public String toString() {
        return ((Object) getValue()) + " [" + ((Object) getValue().getClass()) + "]";
    }
}
