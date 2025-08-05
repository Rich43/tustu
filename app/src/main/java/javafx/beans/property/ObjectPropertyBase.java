package javafx.beans.property;

import com.sun.javafx.binding.ExpressionHelper;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/* loaded from: jfxrt.jar:javafx/beans/property/ObjectPropertyBase.class */
public abstract class ObjectPropertyBase<T> extends ObjectProperty<T> {
    private T value;
    private ObservableValue<? extends T> observable = null;
    private InvalidationListener listener = null;
    private boolean valid = true;
    private ExpressionHelper<T> helper = null;

    public ObjectPropertyBase() {
    }

    public ObjectPropertyBase(T initialValue) {
        this.value = initialValue;
    }

    @Override // javafx.beans.Observable
    public void addListener(InvalidationListener listener) {
        this.helper = ExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.beans.Observable
    public void removeListener(InvalidationListener listener) {
        this.helper = ExpressionHelper.removeListener(this.helper, listener);
    }

    @Override // javafx.beans.value.ObservableValue
    public void addListener(ChangeListener<? super T> listener) {
        this.helper = ExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.beans.value.ObservableValue
    public void removeListener(ChangeListener<? super T> listener) {
        this.helper = ExpressionHelper.removeListener(this.helper, listener);
    }

    protected void fireValueChangedEvent() {
        ExpressionHelper.fireValueChangedEvent(this.helper);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void markInvalid() {
        if (this.valid) {
            this.valid = false;
            invalidated();
            fireValueChangedEvent();
        }
    }

    protected void invalidated() {
    }

    @Override // javafx.beans.value.ObservableObjectValue
    public T get() {
        this.valid = true;
        return this.observable == null ? this.value : this.observable.getValue2();
    }

    @Override // javafx.beans.value.WritableObjectValue
    public void set(T newValue) {
        if (isBound()) {
            throw new RuntimeException(((getBean() == null || getName() == null) ? "" : getBean().getClass().getSimpleName() + "." + getName() + " : ") + "A bound value cannot be set.");
        }
        if (this.value != newValue) {
            this.value = newValue;
            markInvalid();
        }
    }

    @Override // javafx.beans.property.Property
    public boolean isBound() {
        return this.observable != null;
    }

    @Override // javafx.beans.property.Property
    public void bind(ObservableValue<? extends T> newObservable) {
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
            markInvalid();
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

    @Override // javafx.beans.property.ObjectProperty, javafx.beans.property.ReadOnlyObjectProperty
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("ObjectProperty [");
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

    /* loaded from: jfxrt.jar:javafx/beans/property/ObjectPropertyBase$Listener.class */
    private static class Listener implements InvalidationListener {
        private final WeakReference<ObjectPropertyBase<?>> wref;

        public Listener(ObjectPropertyBase<?> ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable observable) {
            ObjectPropertyBase<?> ref = this.wref.get();
            if (ref != null) {
                ref.markInvalid();
            } else {
                observable.removeListener(this);
            }
        }
    }
}
