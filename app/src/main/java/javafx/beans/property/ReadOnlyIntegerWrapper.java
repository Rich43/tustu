package javafx.beans.property;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyIntegerWrapper.class */
public class ReadOnlyIntegerWrapper extends SimpleIntegerProperty {
    private ReadOnlyPropertyImpl readOnlyProperty;

    public ReadOnlyIntegerWrapper() {
    }

    public ReadOnlyIntegerWrapper(int initialValue) {
        super(initialValue);
    }

    public ReadOnlyIntegerWrapper(Object bean, String name) {
        super(bean, name);
    }

    public ReadOnlyIntegerWrapper(Object bean, String name, int initialValue) {
        super(bean, name, initialValue);
    }

    public ReadOnlyIntegerProperty getReadOnlyProperty() {
        if (this.readOnlyProperty == null) {
            this.readOnlyProperty = new ReadOnlyPropertyImpl();
        }
        return this.readOnlyProperty;
    }

    @Override // javafx.beans.property.IntegerPropertyBase
    protected void fireValueChangedEvent() {
        super.fireValueChangedEvent();
        if (this.readOnlyProperty != null) {
            this.readOnlyProperty.fireValueChangedEvent();
        }
    }

    /* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyIntegerWrapper$ReadOnlyPropertyImpl.class */
    private class ReadOnlyPropertyImpl extends ReadOnlyIntegerPropertyBase {
        private ReadOnlyPropertyImpl() {
        }

        @Override // javafx.beans.value.ObservableIntegerValue
        public int get() {
            return ReadOnlyIntegerWrapper.this.get();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ReadOnlyIntegerWrapper.this.getBean();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return ReadOnlyIntegerWrapper.this.getName();
        }
    }
}
