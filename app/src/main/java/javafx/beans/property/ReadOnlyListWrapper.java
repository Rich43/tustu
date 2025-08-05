package javafx.beans.property;

import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyListWrapper.class */
public class ReadOnlyListWrapper<E> extends SimpleListProperty<E> {
    private ReadOnlyListWrapper<E>.ReadOnlyPropertyImpl readOnlyProperty;

    public ReadOnlyListWrapper() {
    }

    public ReadOnlyListWrapper(ObservableList<E> initialValue) {
        super(initialValue);
    }

    public ReadOnlyListWrapper(Object bean, String name) {
        super(bean, name);
    }

    public ReadOnlyListWrapper(Object bean, String name, ObservableList<E> initialValue) {
        super(bean, name, initialValue);
    }

    public ReadOnlyListProperty<E> getReadOnlyProperty() {
        if (this.readOnlyProperty == null) {
            this.readOnlyProperty = new ReadOnlyPropertyImpl();
        }
        return this.readOnlyProperty;
    }

    @Override // javafx.beans.property.ListPropertyBase
    protected void fireValueChangedEvent() {
        super.fireValueChangedEvent();
        if (this.readOnlyProperty != null) {
            this.readOnlyProperty.fireValueChangedEvent();
        }
    }

    @Override // javafx.beans.property.ListPropertyBase
    protected void fireValueChangedEvent(ListChangeListener.Change<? extends E> change) {
        super.fireValueChangedEvent(change);
        if (this.readOnlyProperty != null) {
            change.reset();
            this.readOnlyProperty.fireValueChangedEvent(change);
        }
    }

    /* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyListWrapper$ReadOnlyPropertyImpl.class */
    private class ReadOnlyPropertyImpl extends ReadOnlyListPropertyBase<E> {
        private ReadOnlyPropertyImpl() {
        }

        @Override // javafx.beans.value.ObservableObjectValue
        public ObservableList<E> get() {
            return ReadOnlyListWrapper.this.get();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ReadOnlyListWrapper.this.getBean();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return ReadOnlyListWrapper.this.getName();
        }

        @Override // javafx.beans.binding.ListExpression
        public ReadOnlyIntegerProperty sizeProperty() {
            return ReadOnlyListWrapper.this.sizeProperty();
        }

        @Override // javafx.beans.binding.ListExpression
        public ReadOnlyBooleanProperty emptyProperty() {
            return ReadOnlyListWrapper.this.emptyProperty();
        }
    }
}
