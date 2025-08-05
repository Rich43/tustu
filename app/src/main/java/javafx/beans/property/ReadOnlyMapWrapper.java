package javafx.beans.property;

import javafx.collections.MapChangeListener;
import javafx.collections.ObservableMap;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyMapWrapper.class */
public class ReadOnlyMapWrapper<K, V> extends SimpleMapProperty<K, V> {
    private ReadOnlyMapWrapper<K, V>.ReadOnlyPropertyImpl readOnlyProperty;

    public ReadOnlyMapWrapper() {
    }

    public ReadOnlyMapWrapper(ObservableMap<K, V> initialValue) {
        super(initialValue);
    }

    public ReadOnlyMapWrapper(Object bean, String name) {
        super(bean, name);
    }

    public ReadOnlyMapWrapper(Object bean, String name, ObservableMap<K, V> initialValue) {
        super(bean, name, initialValue);
    }

    public ReadOnlyMapProperty<K, V> getReadOnlyProperty() {
        if (this.readOnlyProperty == null) {
            this.readOnlyProperty = new ReadOnlyPropertyImpl();
        }
        return this.readOnlyProperty;
    }

    @Override // javafx.beans.property.MapPropertyBase
    protected void fireValueChangedEvent() {
        super.fireValueChangedEvent();
        if (this.readOnlyProperty != null) {
            this.readOnlyProperty.fireValueChangedEvent();
        }
    }

    @Override // javafx.beans.property.MapPropertyBase
    protected void fireValueChangedEvent(MapChangeListener.Change<? extends K, ? extends V> change) {
        super.fireValueChangedEvent(change);
        if (this.readOnlyProperty != null) {
            this.readOnlyProperty.fireValueChangedEvent(change);
        }
    }

    /* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyMapWrapper$ReadOnlyPropertyImpl.class */
    private class ReadOnlyPropertyImpl extends ReadOnlyMapPropertyBase<K, V> {
        private ReadOnlyPropertyImpl() {
        }

        @Override // javafx.beans.value.ObservableObjectValue
        public ObservableMap<K, V> get() {
            return ReadOnlyMapWrapper.this.get();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ReadOnlyMapWrapper.this.getBean();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return ReadOnlyMapWrapper.this.getName();
        }

        @Override // javafx.beans.binding.MapExpression
        public ReadOnlyIntegerProperty sizeProperty() {
            return ReadOnlyMapWrapper.this.sizeProperty();
        }

        @Override // javafx.beans.binding.MapExpression
        public ReadOnlyBooleanProperty emptyProperty() {
            return ReadOnlyMapWrapper.this.emptyProperty();
        }
    }
}
