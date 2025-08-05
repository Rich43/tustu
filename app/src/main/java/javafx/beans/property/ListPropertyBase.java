package javafx.beans.property;

import com.sun.javafx.binding.ListExpressionHelper;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/beans/property/ListPropertyBase.class */
public abstract class ListPropertyBase<E> extends ListProperty<E> {
    private ObservableList<E> value;
    private ListPropertyBase<E>.SizeProperty size0;
    private ListPropertyBase<E>.EmptyProperty empty0;
    private final ListChangeListener<E> listChangeListener = change -> {
        invalidateProperties();
        invalidated();
        fireValueChangedEvent(change);
    };
    private ObservableValue<? extends ObservableList<E>> observable = null;
    private InvalidationListener listener = null;
    private boolean valid = true;
    private ListExpressionHelper<E> helper = null;

    public ListPropertyBase() {
    }

    public ListPropertyBase(ObservableList<E> initialValue) {
        this.value = initialValue;
        if (initialValue != null) {
            initialValue.addListener(this.listChangeListener);
        }
    }

    @Override // javafx.beans.binding.ListExpression
    public ReadOnlyIntegerProperty sizeProperty() {
        if (this.size0 == null) {
            this.size0 = new SizeProperty();
        }
        return this.size0;
    }

    /* loaded from: jfxrt.jar:javafx/beans/property/ListPropertyBase$SizeProperty.class */
    private class SizeProperty extends ReadOnlyIntegerPropertyBase {
        private SizeProperty() {
        }

        @Override // javafx.beans.value.ObservableIntegerValue
        public int get() {
            return ListPropertyBase.this.size();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ListPropertyBase.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return "size";
        }

        @Override // javafx.beans.property.ReadOnlyIntegerPropertyBase
        protected void fireValueChangedEvent() {
            super.fireValueChangedEvent();
        }
    }

    @Override // javafx.beans.binding.ListExpression
    public ReadOnlyBooleanProperty emptyProperty() {
        if (this.empty0 == null) {
            this.empty0 = new EmptyProperty();
        }
        return this.empty0;
    }

    /* loaded from: jfxrt.jar:javafx/beans/property/ListPropertyBase$EmptyProperty.class */
    private class EmptyProperty extends ReadOnlyBooleanPropertyBase {
        private EmptyProperty() {
        }

        @Override // javafx.beans.value.ObservableBooleanValue
        public boolean get() {
            return ListPropertyBase.this.isEmpty();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return ListPropertyBase.this;
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public String getName() {
            return Constants.ELEMNAME_EMPTY_STRING;
        }

        @Override // javafx.beans.property.ReadOnlyBooleanPropertyBase
        protected void fireValueChangedEvent() {
            super.fireValueChangedEvent();
        }
    }

    @Override // javafx.beans.Observable
    public void addListener(InvalidationListener listener) {
        this.helper = ListExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.beans.Observable
    public void removeListener(InvalidationListener listener) {
        this.helper = ListExpressionHelper.removeListener(this.helper, listener);
    }

    @Override // javafx.beans.value.ObservableValue
    public void addListener(ChangeListener<? super ObservableList<E>> listener) {
        this.helper = ListExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.beans.value.ObservableValue
    public void removeListener(ChangeListener<? super ObservableList<E>> listener) {
        this.helper = ListExpressionHelper.removeListener(this.helper, listener);
    }

    @Override // javafx.collections.ObservableList
    public void addListener(ListChangeListener<? super E> listener) {
        this.helper = ListExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.collections.ObservableList
    public void removeListener(ListChangeListener<? super E> listener) {
        this.helper = ListExpressionHelper.removeListener(this.helper, listener);
    }

    protected void fireValueChangedEvent() {
        ListExpressionHelper.fireValueChangedEvent(this.helper);
    }

    protected void fireValueChangedEvent(ListChangeListener.Change<? extends E> change) {
        ListExpressionHelper.fireValueChangedEvent(this.helper, change);
    }

    private void invalidateProperties() {
        if (this.size0 != null) {
            this.size0.fireValueChangedEvent();
        }
        if (this.empty0 != null) {
            this.empty0.fireValueChangedEvent();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void markInvalid(ObservableList<E> oldValue) {
        if (this.valid) {
            if (oldValue != null) {
                oldValue.removeListener(this.listChangeListener);
            }
            this.valid = false;
            invalidateProperties();
            invalidated();
            fireValueChangedEvent();
        }
    }

    protected void invalidated() {
    }

    @Override // javafx.beans.value.ObservableObjectValue
    public ObservableList<E> get() {
        if (!this.valid) {
            this.value = this.observable == null ? this.value : this.observable.getValue2();
            this.valid = true;
            if (this.value != null) {
                this.value.addListener(this.listChangeListener);
            }
        }
        return this.value;
    }

    @Override // javafx.beans.value.WritableObjectValue
    public void set(ObservableList<E> newValue) {
        if (isBound()) {
            throw new RuntimeException(((getBean() == null || getName() == null) ? "" : getBean().getClass().getSimpleName() + "." + getName() + " : ") + "A bound value cannot be set.");
        }
        if (this.value != newValue) {
            ObservableList<E> oldValue = this.value;
            this.value = newValue;
            markInvalid(oldValue);
        }
    }

    @Override // javafx.beans.property.Property
    public boolean isBound() {
        return this.observable != null;
    }

    @Override // javafx.beans.property.Property
    public void bind(ObservableValue<? extends ObservableList<E>> newObservable) {
        if (newObservable == null) {
            throw new NullPointerException("Cannot bind to null");
        }
        if (!newObservable.equals(this.observable)) {
            unbind();
            this.observable = newObservable;
            if (this.listener == null) {
                this.listener = new Listener(this);
            }
            this.observable.addListener(this.listener);
            markInvalid(this.value);
        }
    }

    @Override // javafx.beans.property.Property
    public void unbind() {
        if (this.observable != null) {
            this.value = this.observable.getValue2();
            this.observable.removeListener(this.listener);
            this.observable = null;
        }
    }

    @Override // javafx.beans.property.ListProperty, javafx.beans.property.ReadOnlyListProperty
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("ListProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if (name != null && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        if (isBound()) {
            result.append("bound, ");
            if (this.valid) {
                result.append("value: ").append((Object) get());
            } else {
                result.append("invalid");
            }
        } else {
            result.append("value: ").append((Object) get());
        }
        result.append("]");
        return result.toString();
    }

    /* loaded from: jfxrt.jar:javafx/beans/property/ListPropertyBase$Listener.class */
    private static class Listener<E> implements InvalidationListener {
        private final WeakReference<ListPropertyBase<E>> wref;

        public Listener(ListPropertyBase<E> ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable observable) {
            ListPropertyBase<E> ref = this.wref.get();
            if (ref != null) {
                ref.markInvalid(((ListPropertyBase) ref).value);
            } else {
                observable.removeListener(this);
            }
        }
    }
}
