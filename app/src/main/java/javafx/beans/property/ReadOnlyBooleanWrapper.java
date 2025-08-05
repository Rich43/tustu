package javafx.beans.property;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyBooleanWrapper.class */
public class ReadOnlyBooleanWrapper extends SimpleBooleanProperty {
    private ReadOnlyPropertyImpl readOnlyProperty;

    public ReadOnlyBooleanWrapper() {
    }

    public ReadOnlyBooleanWrapper(boolean initialValue) {
        super(initialValue);
    }

    public ReadOnlyBooleanWrapper(Object bean, String name) {
        super(bean, name);
    }

    public ReadOnlyBooleanWrapper(Object bean, String name, boolean initialValue) {
        super(bean, name, initialValue);
    }

    public ReadOnlyBooleanProperty getReadOnlyProperty() {
        if (this.readOnlyProperty == null) {
            this.readOnlyProperty = new ReadOnlyPropertyImpl();
        }
        return this.readOnlyProperty;
    }

    @Override // javafx.beans.property.BooleanPropertyBase
    protected void fireValueChangedEvent() {
        super.fireValueChangedEvent();
        if (this.readOnlyProperty != null) {
            this.readOnlyProperty.fireValueChangedEvent();
        }
    }

    /* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyBooleanWrapper$ReadOnlyPropertyImpl.class */
    private class ReadOnlyPropertyImpl extends ReadOnlyBooleanPropertyBase {
        private ReadOnlyPropertyImpl() {
        }

        @Override // javafx.beans.value.ObservableBooleanValue
        public boolean get() {
            return ReadOnlyBooleanWrapper.this.get();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ReadOnlyBooleanWrapper.this.getBean();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return ReadOnlyBooleanWrapper.this.getName();
        }
    }
}
