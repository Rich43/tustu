package javafx.beans.property;

import javafx.collections.ObservableMap;

/* loaded from: jfxrt.jar:javafx/beans/property/SimpleMapProperty.class */
public class SimpleMapProperty<K, V> extends MapPropertyBase<K, V> {
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

    public SimpleMapProperty() {
        this(DEFAULT_BEAN, "");
    }

    public SimpleMapProperty(ObservableMap<K, V> initialValue) {
        this(DEFAULT_BEAN, "", initialValue);
    }

    public SimpleMapProperty(Object bean, String name) {
        this.bean = bean;
        this.name = name == null ? "" : name;
    }

    public SimpleMapProperty(Object bean, String name, ObservableMap<K, V> initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = name == null ? "" : name;
    }
}
