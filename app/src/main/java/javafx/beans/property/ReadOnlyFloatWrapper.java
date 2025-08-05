package javafx.beans.property;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyFloatWrapper.class */
public class ReadOnlyFloatWrapper extends SimpleFloatProperty {
    private ReadOnlyPropertyImpl readOnlyProperty;

    public ReadOnlyFloatWrapper() {
    }

    public ReadOnlyFloatWrapper(float initialValue) {
        super(initialValue);
    }

    public ReadOnlyFloatWrapper(Object bean, String name) {
        super(bean, name);
    }

    public ReadOnlyFloatWrapper(Object bean, String name, float initialValue) {
        super(bean, name, initialValue);
    }

    public ReadOnlyFloatProperty getReadOnlyProperty() {
        if (this.readOnlyProperty == null) {
            this.readOnlyProperty = new ReadOnlyPropertyImpl();
        }
        return this.readOnlyProperty;
    }

    @Override // javafx.beans.property.FloatPropertyBase
    protected void fireValueChangedEvent() {
        super.fireValueChangedEvent();
        if (this.readOnlyProperty != null) {
            this.readOnlyProperty.fireValueChangedEvent();
        }
    }

    /* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyFloatWrapper$ReadOnlyPropertyImpl.class */
    private class ReadOnlyPropertyImpl extends ReadOnlyFloatPropertyBase {
        private ReadOnlyPropertyImpl() {
        }

        @Override // javafx.beans.value.ObservableFloatValue
        public float get() {
            return ReadOnlyFloatWrapper.this.get();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ReadOnlyFloatWrapper.this.getBean();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return ReadOnlyFloatWrapper.this.getName();
        }
    }
}
