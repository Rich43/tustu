package javafx.beans.property;

import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableValue;

/* loaded from: jfxrt.jar:javafx/beans/property/Property.class */
public interface Property<T> extends ReadOnlyProperty<T>, WritableValue<T> {
    void bind(ObservableValue<? extends T> observableValue);

    void unbind();

    boolean isBound();

    void bindBidirectional(Property<T> property);

    void unbindBidirectional(Property<T> property);
}
