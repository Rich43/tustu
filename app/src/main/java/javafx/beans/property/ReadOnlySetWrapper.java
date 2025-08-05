package javafx.beans.property;

import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlySetWrapper.class */
public class ReadOnlySetWrapper<E> extends SimpleSetProperty<E> {
    private ReadOnlySetWrapper<E>.ReadOnlyPropertyImpl readOnlyProperty;

    public ReadOnlySetWrapper() {
    }

    public ReadOnlySetWrapper(ObservableSet<E> initialValue) {
        super(initialValue);
    }

    public ReadOnlySetWrapper(Object bean, String name) {
        super(bean, name);
    }

    public ReadOnlySetWrapper(Object bean, String name, ObservableSet<E> initialValue) {
        super(bean, name, initialValue);
    }

    public ReadOnlySetProperty<E> getReadOnlyProperty() {
        if (this.readOnlyProperty == null) {
            this.readOnlyProperty = new ReadOnlyPropertyImpl();
        }
        return this.readOnlyProperty;
    }

    @Override // javafx.beans.property.SetPropertyBase
    protected void fireValueChangedEvent() {
        super.fireValueChangedEvent();
        if (this.readOnlyProperty != null) {
            this.readOnlyProperty.fireValueChangedEvent();
        }
    }

    @Override // javafx.beans.property.SetPropertyBase
    protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
        super.fireValueChangedEvent(change);
        if (this.readOnlyProperty != null) {
            this.readOnlyProperty.fireValueChangedEvent(change);
        }
    }

    /* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlySetWrapper$ReadOnlyPropertyImpl.class */
    private class ReadOnlyPropertyImpl extends ReadOnlySetPropertyBase<E> {
        private ReadOnlyPropertyImpl() {
        }

        @Override // javafx.beans.value.ObservableObjectValue
        public ObservableSet<E> get() {
            return ReadOnlySetWrapper.this.get();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ReadOnlySetWrapper.this.getBean();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return ReadOnlySetWrapper.this.getName();
        }

        @Override // javafx.beans.binding.SetExpression
        public ReadOnlyIntegerProperty sizeProperty() {
            return ReadOnlySetWrapper.this.sizeProperty();
        }

        @Override // javafx.beans.binding.SetExpression
        public ReadOnlyBooleanProperty emptyProperty() {
            return ReadOnlySetWrapper.this.emptyProperty();
        }
    }
}
