package javafx.beans.property;

/* loaded from: jfxrt.jar:javafx/beans/property/SimpleDoubleProperty.class */
public class SimpleDoubleProperty extends DoublePropertyBase {
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

    public SimpleDoubleProperty() {
        this(DEFAULT_BEAN, "");
    }

    public SimpleDoubleProperty(double initialValue) {
        this(DEFAULT_BEAN, "", initialValue);
    }

    public SimpleDoubleProperty(Object bean, String name) {
        this.bean = bean;
        this.name = name == null ? "" : name;
    }

    public SimpleDoubleProperty(Object bean, String name, double initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = name == null ? "" : name;
    }
}
