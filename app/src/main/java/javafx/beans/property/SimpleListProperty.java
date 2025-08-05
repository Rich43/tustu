package javafx.beans.property;

import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/beans/property/SimpleListProperty.class */
public class SimpleListProperty<E> extends ListPropertyBase<E> {
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

    public SimpleListProperty() {
        this(DEFAULT_BEAN, "");
    }

    public SimpleListProperty(ObservableList<E> initialValue) {
        this(DEFAULT_BEAN, "", initialValue);
    }

    public SimpleListProperty(Object bean, String name) {
        this.bean = bean;
        this.name = name == null ? "" : name;
    }

    public SimpleListProperty(Object bean, String name, ObservableList<E> initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = name == null ? "" : name;
    }
}
