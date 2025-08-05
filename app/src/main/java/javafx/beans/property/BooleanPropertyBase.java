package javafx.beans.property;

import com.sun.javafx.binding.ExpressionHelper;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;

/* loaded from: jfxrt.jar:javafx/beans/property/BooleanPropertyBase.class */
public abstract class BooleanPropertyBase extends BooleanProperty {
    private boolean value;
    private ObservableBooleanValue observable = null;
    private InvalidationListener listener = null;
    private boolean valid = true;
    private ExpressionHelper<Boolean> helper = null;

    @Override // javafx.beans.property.BooleanProperty, javafx.beans.value.WritableValue, javafx.beans.value.WritableBooleanValue
    public /* bridge */ /* synthetic */ void setValue(Boolean bool) {
        super.setValue(bool);
    }

    @Override // javafx.beans.property.BooleanProperty, javafx.beans.property.ReadOnlyBooleanProperty, javafx.beans.binding.BooleanExpression
    public /* bridge */ /* synthetic */ ReadOnlyObjectProperty asObject() {
        return super.asObject();
    }

    @Override // javafx.beans.property.BooleanProperty, javafx.beans.property.ReadOnlyBooleanProperty, javafx.beans.binding.BooleanExpression
    public /* bridge */ /* synthetic */ ObjectExpression asObject() {
        return super.asObject();
    }

    public BooleanPropertyBase() {
    }

    public BooleanPropertyBase(boolean initialValue) {
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
    public void addListener(ChangeListener<? super Boolean> listener) {
        this.helper = ExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.beans.value.ObservableValue
    public void removeListener(ChangeListener<? super Boolean> listener) {
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

    @Override // javafx.beans.value.ObservableBooleanValue
    public boolean get() {
        this.valid = true;
        return this.observable == null ? this.value : this.observable.get();
    }

    @Override // javafx.beans.value.WritableBooleanValue
    public void set(boolean newValue) {
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
    public void bind(final ObservableValue<? extends Boolean> rawObservable) {
        if (rawObservable == null) {
            throw new NullPointerException("Cannot bind to null");
        }
        ObservableBooleanValue newObservable = rawObservable instanceof ObservableBooleanValue ? (ObservableBooleanValue) rawObservable : new BooleanBinding() { // from class: javafx.beans.property.BooleanPropertyBase.1
            {
                super.bind(rawObservable);
            }

            @Override // javafx.beans.binding.BooleanBinding
            protected boolean computeValue() {
                Boolean value = (Boolean) rawObservable.getValue2();
                if (value == null) {
                    return false;
                }
                return value.booleanValue();
            }
        };
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
            this.value = this.observable.get();
            this.observable.removeListener(this.listener);
            this.observable = null;
        }
    }

    @Override // javafx.beans.property.BooleanProperty, javafx.beans.property.ReadOnlyBooleanProperty
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("BooleanProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if (name != null && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        if (isBound()) {
            result.append("bound, ");
            if (this.valid) {
                result.append("value: ").append(get());
            } else {
                result.append("invalid");
            }
        } else {
            result.append("value: ").append(get());
        }
        result.append("]");
        return result.toString();
    }

    /* loaded from: jfxrt.jar:javafx/beans/property/BooleanPropertyBase$Listener.class */
    private static class Listener implements InvalidationListener {
        private final WeakReference<BooleanPropertyBase> wref;

        public Listener(BooleanPropertyBase ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable observable) {
            BooleanPropertyBase ref = this.wref.get();
            if (ref != null) {
                ref.markInvalid();
            } else {
                observable.removeListener(this);
            }
        }
    }
}
