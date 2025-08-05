package javafx.beans.property;

/* loaded from: jfxrt.jar:javafx/beans/property/SimpleLongProperty.class */
public class SimpleLongProperty extends LongPropertyBase {
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

    public SimpleLongProperty() {
        this(DEFAULT_BEAN, "");
    }

    public SimpleLongProperty(long initialValue) {
        this(DEFAULT_BEAN, "", initialValue);
    }

    public SimpleLongProperty(Object bean, String name) {
        this.bean = bean;
        this.name = name == null ? "" : name;
    }

    public SimpleLongProperty(Object bean, String name, long initialValue) {
        super(initialValue);
        this.bean = bean;
        this.name = name == null ? "" : name;
    }
}
