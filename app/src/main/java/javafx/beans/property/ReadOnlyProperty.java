package javafx.beans.property;

import javafx.beans.value.ObservableValue;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyProperty.class */
public interface ReadOnlyProperty<T> extends ObservableValue<T> {
    Object getBean();

    String getName();
}
