package javafx.beans.property;

import com.sun.javafx.binding.ExpressionHelper;
import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectExpression;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;

/* loaded from: jfxrt.jar:javafx/beans/property/DoublePropertyBase.class */
public abstract class DoublePropertyBase extends DoubleProperty {
    private double value;
    private ObservableDoubleValue observable = null;
    private InvalidationListener listener = null;
    private boolean valid = true;
    private ExpressionHelper<Number> helper = null;

    @Override // javafx.beans.property.DoubleProperty, javafx.beans.value.WritableValue, javafx.beans.value.WritableBooleanValue
    public /* bridge */ /* synthetic */ void setValue(Number number) {
        super.setValue(number);
    }

    @Override // javafx.beans.property.DoubleProperty, javafx.beans.property.ReadOnlyDoubleProperty, javafx.beans.binding.DoubleExpression
    public /* bridge */ /* synthetic */ ReadOnlyObjectProperty asObject() {
        return super.asObject();
    }

    @Override // javafx.beans.property.DoubleProperty, javafx.beans.property.ReadOnlyDoubleProperty, javafx.beans.binding.DoubleExpression
    public /* bridge */ /* synthetic */ ObjectExpression asObject() {
        return super.asObject();
    }

    public DoublePropertyBase() {
    }

    public DoublePropertyBase(double initialValue) {
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

    @Override // javafx.beans.value.ObservableDoubleValue
    public double get() {
        this.valid = true;
        return this.observable == null ? this.value : this.observable.get();
    }

    public void set(double newValue) {
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
        ObservableDoubleValue newObservable;
        if (rawObservable == null) {
            throw new NullPointerException("Cannot bind to null");
        }
        if (rawObservable instanceof ObservableDoubleValue) {
            newObservable = (ObservableDoubleValue) rawObservable;
        } else if (rawObservable instanceof ObservableNumberValue) {
            final ObservableNumberValue numberValue = (ObservableNumberValue) rawObservable;
            newObservable = new DoubleBinding() { // from class: javafx.beans.property.DoublePropertyBase.1
                {
                    super.bind(rawObservable);
                }

                @Override // javafx.beans.binding.DoubleBinding
                protected double computeValue() {
                    return numberValue.doubleValue();
                }
            };
        } else {
            newObservable = new DoubleBinding() { // from class: javafx.beans.property.DoublePropertyBase.2
                {
                    super.bind(rawObservable);
                }

                @Override // javafx.beans.binding.DoubleBinding
                protected double computeValue() {
                    Number value = (Number) rawObservable.getValue2();
                    if (value == null) {
                        return 0.0d;
                    }
                    return value.doubleValue();
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

    @Override // javafx.beans.property.DoubleProperty, javafx.beans.property.ReadOnlyDoubleProperty
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("DoubleProperty [");
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

    /* loaded from: jfxrt.jar:javafx/beans/property/DoublePropertyBase$Listener.class */
    private static class Listener implements InvalidationListener {
        private final WeakReference<DoublePropertyBase> wref;

        public Listener(DoublePropertyBase ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override // javafx.beans.InvalidationListener
        public void invalidated(Observable observable) {
            DoublePropertyBase ref = this.wref.get();
            if (ref != null) {
                ref.markInvalid();
            } else {
                observable.removeListener(this);
            }
        }
    }
}
