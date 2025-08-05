package javafx.beans.property;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyDoubleWrapper.class */
public class ReadOnlyDoubleWrapper extends SimpleDoubleProperty {
    private ReadOnlyPropertyImpl readOnlyProperty;

    public ReadOnlyDoubleWrapper() {
    }

    public ReadOnlyDoubleWrapper(double initialValue) {
        super(initialValue);
    }

    public ReadOnlyDoubleWrapper(Object bean, String name) {
        super(bean, name);
    }

    public ReadOnlyDoubleWrapper(Object bean, String name, double initialValue) {
        super(bean, name, initialValue);
    }

    public ReadOnlyDoubleProperty getReadOnlyProperty() {
        if (this.readOnlyProperty == null) {
            this.readOnlyProperty = new ReadOnlyPropertyImpl();
        }
        return this.readOnlyProperty;
    }

    @Override // javafx.beans.property.DoublePropertyBase
    protected void fireValueChangedEvent() {
        super.fireValueChangedEvent();
        if (this.readOnlyProperty != null) {
            this.readOnlyProperty.fireValueChangedEvent();
        }
    }

    /* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyDoubleWrapper$ReadOnlyPropertyImpl.class */
    private class ReadOnlyPropertyImpl extends ReadOnlyDoublePropertyBase {
        private ReadOnlyPropertyImpl() {
        }

        @Override // javafx.beans.value.ObservableDoubleValue
        public double get() {
            return ReadOnlyDoubleWrapper.this.get();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ReadOnlyDoubleWrapper.this.getBean();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return ReadOnlyDoubleWrapper.this.getName();
        }
    }
}
