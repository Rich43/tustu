package javafx.beans.binding;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/beans/binding/Binding.class */
public interface Binding<T> extends ObservableValue<T> {
    boolean isValid();

    void invalidate();

    ObservableList<?> getDependencies();

    void dispose();
}
