package javafx.beans.property;

import javafx.collections.ObservableSet;

/* loaded from: jfxrt.jar:javafx/beans/property/SimpleSetProperty.class */
public class SimpleSetProperty<E> extends SetPropertyBase<E> {
    private static final Object DEFAULT_BEAN = null;
    private static final String DEFAULT_NAME = "";
    private final Object bean;
    private final String name;

    @Override // javafx.beans.property.ReadOnlyProperty
    public Object getBean() {
        return this.bean;
    }

    @Override // javafx.beans.property.ReadOnlyProperty
    public String getName() {
        return this.name;
    }

    public SimpleSetProperty() {
        this(DEFAULT_BEAN, "");
    }

    public SimpleSetProperty(ObservableSet<E> initialValue) {
        this(DEFAULT_BEAN, "", initialValue);
    }

    public SimpleSetProperty(Object bean, String name) {
        this.bean = bean;
        this.name = name == null ? "" : name;
    }

    public SimpleSetProperty(Object bean, String name, ObservableSet<E> initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = name == null ? "" : name;
    }
}
