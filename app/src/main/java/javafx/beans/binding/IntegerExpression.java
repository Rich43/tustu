package javafx.beans.binding;

import javafx.beans.value.ObservableIntegerValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/beans/binding/IntegerExpression.class */
public abstract class IntegerExpression extends NumberExpressionBase implements ObservableIntegerValue {
    @Override // javafx.beans.value.ObservableNumberValue
    public int intValue() {
        return get();
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
        return Integer.valueOf(get());
    }

    public static IntegerExpression integerExpression(final ObservableIntegerValue value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return value instanceof IntegerExpression ? (IntegerExpression) value : new IntegerBinding() { // from class: javafx.beans.binding.IntegerExpression.1
            {
                super.bind(value);
            }

            @Override // javafx.beans.binding.IntegerBinding, javafx.beans.binding.Binding
            public void dispose() {
                super.unbind(value);
            }

            @Override // javafx.beans.binding.IntegerBinding
            protected int computeValue() {
                return value.get();
            }

            @Override // javafx.beans.binding.IntegerBinding, javafx.beans.binding.Binding
            public ObservableList<ObservableIntegerValue> getDependencies() {
                return FXCollections.singletonObservableList(value);
            }
        };
    }

    public static <T extends Number> IntegerExpression integerExpression(final ObservableValue<T> value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return value instanceof IntegerExpression ? (IntegerExpression) value : new IntegerBinding() { // from class: javafx.beans.binding.IntegerExpression.2
            {
                super.bind(value);
            }

            @Override // javafx.beans.binding.IntegerBinding, javafx.beans.binding.Binding
            public void dispose() {
                super.unbind(value);
            }

            @Override // javafx.beans.binding.IntegerBinding
            protected int computeValue() {
                Number number = (Number) value.getValue2();
                if (number == null) {
                    return 0;
                }
                return number.intValue();
            }

            @Override // javafx.beans.binding.IntegerBinding, javafx.beans.binding.Binding
            public ObservableList<ObservableValue<T>> getDependencies() {
                return FXCollections.singletonObservableList(value);
            }
        };
    }

    @Override // javafx.beans.binding.NumberExpression
    public IntegerBinding negate() {
        return (IntegerBinding) Bindings.negate(this);
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
    public IntegerBinding add(int other) {
        return (IntegerBinding) Bindings.add((ObservableNumberValue) this, other);
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
    public IntegerBinding subtract(int other) {
        return (IntegerBinding) Bindings.subtract((ObservableNumberValue) this, other);
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
    public IntegerBinding multiply(int other) {
        return (IntegerBinding) Bindings.multiply((ObservableNumberValue) this, other);
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
    public IntegerBinding divide(int other) {
        return (IntegerBinding) Bindings.divide((ObservableNumberValue) this, other);
    }

    public ObjectExpression<Integer> asObject() {
        return new ObjectBinding<Integer>() { // from class: javafx.beans.binding.IntegerExpression.3
            {
                bind(IntegerExpression.this);
            }

            @Override // javafx.beans.binding.ObjectBinding, javafx.beans.binding.Binding
            public void dispose() {
                unbind(IntegerExpression.this);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // javafx.beans.binding.ObjectBinding
            public Integer computeValue() {
                return IntegerExpression.this.getValue2();
            }
        };
    }
}
