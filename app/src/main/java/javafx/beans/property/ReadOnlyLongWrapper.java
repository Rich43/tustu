package javafx.beans.property;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyLongWrapper.class */
public class ReadOnlyLongWrapper extends SimpleLongProperty {
    private ReadOnlyPropertyImpl readOnlyProperty;

    public ReadOnlyLongWrapper() {
    }

    public ReadOnlyLongWrapper(long initialValue) {
        super(initialValue);
    }

    public ReadOnlyLongWrapper(Object bean, String name) {
        super(bean, name);
    }

    public ReadOnlyLongWrapper(Object bean, String name, long initialValue) {
        super(bean, name, initialValue);
    }

    public ReadOnlyLongProperty getReadOnlyProperty() {
        if (this.readOnlyProperty == null) {
            this.readOnlyProperty = new ReadOnlyPropertyImpl();
        }
        return this.readOnlyProperty;
    }

    @Override // javafx.beans.property.LongPropertyBase
    protected void fireValueChangedEvent() {
        super.fireValueChangedEvent();
        if (this.readOnlyProperty != null) {
            this.readOnlyProperty.fireValueChangedEvent();
        }
    }

    /* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyLongWrapper$ReadOnlyPropertyImpl.class */
    private class ReadOnlyPropertyImpl extends ReadOnlyLongPropertyBase {
        private ReadOnlyPropertyImpl() {
        }

        @Override // javafx.beans.value.ObservableLongValue
        public long get() {
            return ReadOnlyLongWrapper.this.get();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ReadOnlyLongWrapper.this.getBean();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return ReadOnlyLongWrapper.this.getName();
        }
    }
}
