package javafx.beans.property;

/* loaded from: jfxrt.jar:javafx/beans/property/SimpleStringProperty.class */
public class SimpleStringProperty extends StringPropertyBase {
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

    public SimpleStringProperty() {
        this(DEFAULT_BEAN, "");
    }

    public SimpleStringProperty(String initialValue) {
        this(DEFAULT_BEAN, "", initialValue);
    }

    public SimpleStringProperty(Object bean, String name) {
        this.bean = bean;
        this.name = name == null ? "" : name;
    }

    public SimpleStringProperty(Object bean, String name, String initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = name == null ? "" : name;
    }
}
