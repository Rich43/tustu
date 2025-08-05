package javafx.beans.property.adapter;

import javafx.beans.property.ReadOnlyProperty;

/* loaded from: jfxrt.jar:javafx/beans/property/adapter/ReadOnlyJavaBeanProperty.class */
public interface ReadOnlyJavaBeanProperty<T> extends ReadOnlyProperty<T> {
    void fireValueChangedEvent();

    void dispose();
}
