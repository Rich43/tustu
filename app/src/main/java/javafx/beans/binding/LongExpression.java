package javafx.beans.binding;

import javafx.beans.value.ObservableLongValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/beans/binding/LongExpression.class */
public abstract class LongExpression extends NumberExpressionBase implements ObservableLongValue {
    @Override // javafx.beans.value.ObservableNumberValue
    public int intValue() {
        return (int) get();
    }

    @Override // javafx.beans.value.ObservableNumberValue
    public long longValue() {
        return get();
    }

    @Override // javafx.beans.value.ObservableNumberValue
    public float floatValue() {
        return get();
    }

    @Override // javafx.beans.value.ObservableNumberValue
    public double doubleValue() {
        return get();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.beans.value.ObservableValue
    /* renamed from: getValue */
    public Number getValue2() {
        return Long.valueOf(get());
    }

    public static LongExpression longExpression(final ObservableLongValue value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return value instanceof LongExpression ? (LongExpression) value : new LongBinding() { // from class: javafx.beans.binding.LongExpression.1
            {
                super.bind(value);
            }

            @Override // javafx.beans.binding.LongBinding, javafx.beans.binding.Binding
            public void dispose() {
                super.unbind(value);
            }

            @Override // javafx.beans.binding.LongBinding
            protected long computeValue() {
                return value.get();
            }

            @Override // javafx.beans.binding.LongBinding, javafx.beans.binding.Binding
            public ObservableList<ObservableLongValue> getDependencies() {
                return FXCollections.singletonObservableList(value);
            }
        };
    }

    public static <T extends Number> LongExpression longExpression(final ObservableValue<T> value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return value instanceof LongExpression ? (LongExpression) value : new LongBinding() { // from class: javafx.beans.binding.LongExpression.2
            {
                super.bind(value);
            }

            @Override // javafx.beans.binding.LongBinding, javafx.beans.binding.Binding
            public void dispose() {
                super.unbind(value);
            }

            @Override // javafx.beans.binding.LongBinding
            protected long computeValue() {
                Number number = (Number) value.getValue2();
                if (number == null) {
                    return 0L;
                }
                return number.longValue();
            }

            @Override // javafx.beans.binding.LongBinding, javafx.beans.binding.Binding
            public ObservableList<ObservableValue<T>> getDependencies() {
                return FXCollections.singletonObservableList(value);
            }
        };
    }

    @Override // javafx.beans.binding.NumberExpression
    public LongBinding negate() {
        return (LongBinding) Bindings.negate(this);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding add(double other) {
        return Bindings.add(this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public FloatBinding add(float other) {
        return (FloatBinding) Bindings.add((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public LongBinding add(long other) {
        return (LongBinding) Bindings.add((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public LongBinding add(int other) {
        return (LongBinding) Bindings.add((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding subtract(double other) {
        return Bindings.subtract(this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public FloatBinding subtract(float other) {
        return (FloatBinding) Bindings.subtract((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public LongBinding subtract(long other) {
        return (LongBinding) Bindings.subtract((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public LongBinding subtract(int other) {
        return (LongBinding) Bindings.subtract((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding multiply(double other) {
        return Bindings.multiply(this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public FloatBinding multiply(float other) {
        return (FloatBinding) Bindings.multiply((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public LongBinding multiply(long other) {
        return (LongBinding) Bindings.multiply((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public LongBinding multiply(int other) {
        return (LongBinding) Bindings.multiply((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding divide(double other) {
        return Bindings.divide(this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public FloatBinding divide(float other) {
        return (FloatBinding) Bindings.divide((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public LongBinding divide(long other) {
        return (LongBinding) Bindings.divide((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public LongBinding divide(int other) {
        return (LongBinding) Bindings.divide((ObservableNumberValue) this, other);
    }

    public ObjectExpression<Long> asObject() {
        return new ObjectBinding<Long>() { // from class: javafx.beans.binding.LongExpression.3
            {
                bind(LongExpression.this);
            }

            @Override // javafx.beans.binding.ObjectBinding, javafx.beans.binding.Binding
            public void dispose() {
                unbind(LongExpression.this);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // javafx.beans.binding.ObjectBinding
            public Long computeValue() {
                return LongExpression.this.getValue2();
            }
        };
    }
}
