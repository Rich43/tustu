package javafx.beans.property;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyStringWrapper.class */
public class ReadOnlyStringWrapper extends SimpleStringProperty {
    private ReadOnlyPropertyImpl readOnlyProperty;

    public ReadOnlyStringWrapper() {
    }

    public ReadOnlyStringWrapper(String initialValue) {
        super(initialValue);
    }

    public ReadOnlyStringWrapper(Object bean, String name) {
        super(bean, name);
    }

    public ReadOnlyStringWrapper(Object bean, String name, String initialValue) {
        super(bean, name, initialValue);
    }

    public ReadOnlyStringProperty getReadOnlyProperty() {
        if (this.readOnlyProperty == null) {
            this.readOnlyProperty = new ReadOnlyPropertyImpl();
        }
        return this.readOnlyProperty;
    }

    @Override // javafx.beans.property.StringPropertyBase
    protected void fireValueChangedEvent() {
        super.fireValueChangedEvent();
        if (this.readOnlyProperty != null) {
            this.readOnlyProperty.fireValueChangedEvent();
        }
    }

    /* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyStringWrapper$ReadOnlyPropertyImpl.class */
    private class ReadOnlyPropertyImpl extends ReadOnlyStringPropertyBase {
        private ReadOnlyPropertyImpl() {
        }

        /* JADX WARN: Can't rename method to resolve collision */
        @Override // javafx.beans.value.ObservableObjectValue
        public String get() {
            return ReadOnlyStringWrapper.this.get();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ReadOnlyStringWrapper.this.getBean();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return ReadOnlyStringWrapper.this.getName();
        }
    }
}
