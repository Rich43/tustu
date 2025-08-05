package javafx.beans.property;

import com.sun.javafx.binding.SetExpressionHelper;
import com.sun.org.apache.xalan.internal.templates.Constants;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

/* loaded from: jfxrt.jar:javafx/beans/property/SetPropertyBase.class */
public abstract class SetPropertyBase<E> extends SetProperty<E> {
    private ObservableSet<E> value;
    private SetPropertyBase<E>.SizeProperty size0;
    private SetPropertyBase<E>.EmptyProperty empty0;
    private final SetChangeListener<E> setChangeListener = change -> {
        invalidateProperties();
        invalidated();
        fireValueChangedEvent(change);
    };
    private ObservableValue<? extends ObservableSet<E>> observable = null;
    private InvalidationListener listener = null;
    private boolean valid = true;
    private SetExpressionHelper<E> helper = null;

    public SetPropertyBase() {
    }

    public SetPropertyBase(ObservableSet<E> initialValue) {
        this.value = initialValue;
        if (initialValue != null) {
            initialValue.addListener(this.setChangeListener);
        }
    }

    @Override // javafx.beans.binding.SetExpression
    public ReadOnlyIntegerProperty sizeProperty() {
        if (this.size0 == null) {
            this.size0 = new SizeProperty();
        }
        return this.size0;
    }

    /* loaded from: jfxrt.jar:javafx/beans/property/SetPropertyBase$SizeProperty.class */
    private class SizeProperty extends ReadOnlyIntegerPropertyBase {
        private SizeProperty() {
        }

        @Override // javafx.beans.value.ObservableIntegerValue
        public int get() {
            return SetPropertyBase.this.size();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return SetPropertyBase.this;
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

    @Override // javafx.beans.binding.SetExpression
    public ReadOnlyBooleanProperty emptyProperty() {
        if (this.empty0 == null) {
            this.empty0 = new EmptyProperty();
        }
        return this.empty0;
    }

    /* loaded from: jfxrt.jar:javafx/beans/property/SetPropertyBase$EmptyProperty.class */
    private class EmptyProperty extends ReadOnlyBooleanPropertyBase {
        private EmptyProperty() {
        }

        @Override // javafx.beans.value.ObservableBooleanValue
        public boolean get() {
            return SetPropertyBase.this.isEmpty();
        }

        @Override // javafx.beans.property.ReadOnlyProperty
        public Object getBean() {
            return SetPropertyBase.this;
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
        this.helper = SetExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.beans.Observable
    public void removeListener(InvalidationListener listener) {
        this.helper = SetExpressionHelper.removeListener(this.helper, listener);
    }

    @Override // javafx.beans.value.ObservableValue
    public void addListener(ChangeListener<? super ObservableSet<E>> listener) {
        this.helper = SetExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.beans.value.ObservableValue
    public void removeListener(ChangeListener<? super ObservableSet<E>> listener) {
        this.helper = SetExpressionHelper.removeListener(this.helper, listener);
    }

    @Override // javafx.collections.ObservableSet
    public void addListener(SetChangeListener<? super E> listener) {
        this.helper = SetExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.collections.ObservableSet
    public void removeListener(SetChangeListener<? super E> listener) {
        this.helper = SetExpressionHelper.removeListener(this.helper, listener);
    }

    protected void fireValueChangedEvent() {
        SetExpressionHelper.fireValueChangedEvent(this.helper);
    }

    protected void fireValueChangedEvent(SetChangeListener.Change<? extends E> change) {
        SetExpressionHelper.fireValueChangedEvent(this.helper, change);
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
    public void markInvalid(ObservableSet<E> oldValue) {
        if (this.valid) {
            if (oldValue != null) {
                oldValue.removeListener(this.setChangeListener);
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
    public ObservableSet<E> get() {
        if (!this.valid) {
            this.value = this.observable == null ? this.value : this.observable.getValue2();
            this.valid = true;
            if (this.value != null) {
                this.value.addListener(this.setChangeListener);
            }
        }
        return this.value;
    }

    @Override // javafx.beans.value.WritableObjectValue
    public void set(ObservableSet<E> newValue) {
        if (isBound()) {
            throw new RuntimeException(((getBean() == null || getName() == null) ? "" : getBean().getClass().getSimpleName() + "." + getName() + " : ") + "A bound value cannot be set.");
        }
        if (this.value != newValue) {
            ObservableSet<E> oldValue = this.value;
            this.value = newValue;
            markInvalid(oldValue);
        }
    }

    @Override // javafx.beans.property.Property
    public boolean isBound() {
        return this.observable != null;
    }

    @Override // javafx.beans.property.Property
    public void bind(ObservableValue<? extends ObservableSet<E>> newObservable) {
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

    @Override // javafx.beans.property.SetProperty, javafx.beans.property.ReadOnlySetProperty
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("SetProperty [");
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

    /* loaded from: jfxrt.jar:javafx/beans/property/SetPropertyBase$Listener.class */
    private static class Listener<E> implements InvalidationListener {
        private final WeakReference<SetPropertyBase<E>> wref;

        public Listener(SetPropertyBase<E> ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable observable) {
            SetPropertyBase<E> ref = this.wref.get();
            if (ref != null) {
                ref.markInvalid(((SetPropertyBase) ref).value);
            } else {
                observable.removeListener(this);
            }
        }
    }
}
