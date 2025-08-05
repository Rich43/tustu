package javafx.beans.property;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyObjectWrapper.class */
public class ReadOnlyObjectWrapper<T> extends SimpleObjectProperty<T> {
    private ReadOnlyObjectWrapper<T>.ReadOnlyPropertyImpl readOnlyProperty;

    public ReadOnlyObjectWrapper() {
    }

    public ReadOnlyObjectWrapper(T initialValue) {
        super(initialValue);
    }

    public ReadOnlyObjectWrapper(Object bean, String name) {
        super(bean, name);
    }

    public ReadOnlyObjectWrapper(Object bean, String name, T initialValue) {
        super(bean, name, initialValue);
    }

    public ReadOnlyObjectProperty<T> getReadOnlyProperty() {
        if (this.readOnlyProperty == null) {
            this.readOnlyProperty = new ReadOnlyPropertyImpl();
        }
        return this.readOnlyProperty;
    }

    @Override // javafx.beans.property.ObjectPropertyBase
    protected void fireValueChangedEvent() {
        super.fireValueChangedEvent();
        if (this.readOnlyProperty != null) {
            this.readOnlyProperty.fireValueChangedEvent();
        }
    }

    /* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyObjectWrapper$ReadOnlyPropertyImpl.class */
    private class ReadOnlyPropertyImpl extends ReadOnlyObjectPropertyBase<T> {
        private ReadOnlyPropertyImpl() {
        }

        @Override // javafx.beans.value.ObservableObjectValue
        public T get() {
            return ReadOnlyObjectWrapper.this.get();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ReadOnlyObjectWrapper.this.getBean();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return ReadOnlyObjectWrapper.this.getName();
        }
    }
}
