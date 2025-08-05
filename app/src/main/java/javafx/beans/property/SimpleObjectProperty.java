package javafx.beans.property;

/* loaded from: jfxrt.jar:javafx/beans/property/SimpleObjectProperty.class */
public class SimpleObjectProperty<T> extends ObjectPropertyBase<T> {
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

    public SimpleObjectProperty() {
        this(DEFAULT_BEAN, "");
    }

    public SimpleObjectProperty(T initialValue) {
        this(DEFAULT_BEAN, "", initialValue);
    }

    public SimpleObjectProperty(Object bean, String name) {
        this.bean = bean;
        this.name = name == null ? "" : name;
    }

    public SimpleObjectProperty(Object bean, String name, T initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = name == null ? "" : name;
    }
}
