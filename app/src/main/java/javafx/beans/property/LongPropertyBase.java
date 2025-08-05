package javafx.beans.property;

import com.sun.javafx.binding.ExpressionHelper;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.LongBinding;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableLongValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;

/* loaded from: jfxrt.jar:javafx/beans/property/LongPropertyBase.class */
public abstract class LongPropertyBase extends LongProperty {
    private long value;
    private ObservableLongValue observable = null;
    private InvalidationListener listener = null;
    private boolean valid = true;
    private ExpressionHelper<Number> helper = null;

    @Override // javafx.beans.property.LongProperty, javafx.beans.value.WritableValue, javafx.beans.value.WritableBooleanValue
    public /* bridge */ /* synthetic */ void setValue(Number number) {
        super.setValue(number);
    }

    @Override // javafx.beans.property.LongProperty, javafx.beans.property.ReadOnlyLongProperty, javafx.beans.binding.LongExpression
    public /* bridge */ /* synthetic */ ReadOnlyObjectProperty asObject() {
        return super.asObject();
    }

    @Override // javafx.beans.property.LongProperty, javafx.beans.property.ReadOnlyLongProperty, javafx.beans.binding.LongExpression
    public /* bridge */ /* synthetic */ ObjectExpression asObject() {
        return super.asObject();
    }

    public LongPropertyBase() {
    }

    public LongPropertyBase(long initialValue) {
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
    public void addListener(ChangeListener<? super Number> listener) {
        this.helper = ExpressionHelper.addListener(this.helper, this, listener);
    }

    @Override // javafx.beans.value.ObservableValue
    public void removeListener(ChangeListener<? super Number> listener) {
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

    @Override // javafx.beans.value.ObservableLongValue
    public long get() {
        this.valid = true;
        return this.observable == null ? this.value : this.observable.get();
    }

    @Override // javafx.beans.value.WritableLongValue
    public void set(long newValue) {
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
    public void bind(final ObservableValue<? extends Number> rawObservable) {
        ObservableLongValue newObservable;
        if (rawObservable == null) {
            throw new NullPointerException("Cannot bind to null");
        }
        if (rawObservable instanceof ObservableLongValue) {
            newObservable = (ObservableLongValue) rawObservable;
        } else if (rawObservable instanceof ObservableNumberValue) {
            final ObservableNumberValue numberValue = (ObservableNumberValue) rawObservable;
            newObservable = new LongBinding() { // from class: javafx.beans.property.LongPropertyBase.1
                {
                    super.bind(rawObservable);
                }

                @Override // javafx.beans.binding.LongBinding
                protected long computeValue() {
                    return numberValue.longValue();
                }
            };
        } else {
            newObservable = new LongBinding() { // from class: javafx.beans.property.LongPropertyBase.2
                {
                    super.bind(rawObservable);
                }

                @Override // javafx.beans.binding.LongBinding
                protected long computeValue() {
                    Number value = (Number) rawObservable.getValue2();
                    if (value == null) {
                        return 0L;
                    }
                    return value.longValue();
                }
            };
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
            this.value = this.observable.get();
            this.observable.removeListener(this.listener);
            this.observable = null;
        }
    }

    @Override // javafx.beans.property.LongProperty, javafx.beans.property.ReadOnlyLongProperty
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("LongProperty [");
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

    /* loaded from: jfxrt.jar:javafx/beans/property/LongPropertyBase$Listener.class */
    private static class Listener implements InvalidationListener {
        private final WeakReference<LongPropertyBase> wref;

        public Listener(LongPropertyBase ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable observable) {
            LongPropertyBase ref = this.wref.get();
            if (ref != null) {
                ref.markInvalid();
            } else {
                observable.removeListener(this);
            }
        }
    }
}
