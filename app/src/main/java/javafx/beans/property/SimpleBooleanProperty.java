package javafx.beans.property;

/* loaded from: jfxrt.jar:javafx/beans/property/SimpleBooleanProperty.class */
public class SimpleBooleanProperty extends BooleanPropertyBase {
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

    public SimpleBooleanProperty() {
        this(DEFAULT_BEAN, "");
    }

    public SimpleBooleanProperty(boolean initialValue) {
        this(DEFAULT_BEAN, "", initialValue);
    }

    public SimpleBooleanProperty(Object bean, String name) {
        this.bean = bean;
        this.name = name == null ? "" : name;
    }

    public SimpleBooleanProperty(Object bean, String name, boolean initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = name == null ? "" : name;
    }
}
