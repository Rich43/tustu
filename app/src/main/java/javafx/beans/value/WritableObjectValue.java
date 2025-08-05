package javafx.beans.value;

/* loaded from: jfxrt.jar:javafx/beans/value/WritableObjectValue.class */
public interface WritableObjectValue<T> extends WritableValue<T> {
    T get();

    void set(T t2);
}
