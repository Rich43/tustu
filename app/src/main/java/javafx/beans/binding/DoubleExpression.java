package javafx.beans.binding;

import javafx.beans.value.ObservableDoubleValue;
import javafx.beans.value.ObservableNumberValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/beans/binding/DoubleExpression.class */
public abstract class DoubleExpression extends NumberExpressionBase implements ObservableDoubleValue {
    @Override // javafx.beans.value.ObservableNumberValue
    public int intValue() {
        return (int) get();
    }

    @Override // javafx.beans.value.ObservableNumberValue
    public long longValue() {
        return (long) get();
    }

    @Override // javafx.beans.value.ObservableNumberValue
    public float floatValue() {
        return (float) get();
    }

    @Override // javafx.beans.value.ObservableNumberValue
    public double doubleValue() {
        return get();
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.beans.value.ObservableValue
    /* renamed from: getValue */
    public Number getValue2() {
        return Double.valueOf(get());
    }

    public static DoubleExpression doubleExpression(final ObservableDoubleValue value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return value instanceof DoubleExpression ? (DoubleExpression) value : new DoubleBinding() { // from class: javafx.beans.binding.DoubleExpression.1
            {
                super.bind(value);
            }

            @Override // javafx.beans.binding.DoubleBinding, javafx.beans.binding.Binding
            public void dispose() {
                super.unbind(value);
            }

            @Override // javafx.beans.binding.DoubleBinding
            protected double computeValue() {
                return value.get();
            }

            @Override // javafx.beans.binding.DoubleBinding, javafx.beans.binding.Binding
            public ObservableList<ObservableDoubleValue> getDependencies() {
                return FXCollections.singletonObservableList(value);
            }
        };
    }

    public static <T extends Number> DoubleExpression doubleExpression(final ObservableValue<T> value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return value instanceof DoubleExpression ? (DoubleExpression) value : new DoubleBinding() { // from class: javafx.beans.binding.DoubleExpression.2
            {
                super.bind(value);
            }

            @Override // javafx.beans.binding.DoubleBinding, javafx.beans.binding.Binding
            public void dispose() {
                super.unbind(value);
            }

            @Override // javafx.beans.binding.DoubleBinding
            protected double computeValue() {
                Number number = (Number) value.getValue2();
                if (number == null) {
                    return 0.0d;
                }
                return number.doubleValue();
            }

            @Override // javafx.beans.binding.DoubleBinding, javafx.beans.binding.Binding
            public ObservableList<ObservableValue<T>> getDependencies() {
                return FXCollections.singletonObservableList(value);
            }
        };
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding negate() {
        return (DoubleBinding) Bindings.negate(this);
    }

    @Override // javafx.beans.binding.NumberExpressionBase, javafx.beans.binding.NumberExpression
    public DoubleBinding add(ObservableNumberValue other) {
        return (DoubleBinding) Bindings.add(this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding add(double other) {
        return Bindings.add(this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding add(float other) {
        return (DoubleBinding) Bindings.add((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding add(long other) {
        return (DoubleBinding) Bindings.add((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding add(int other) {
        return (DoubleBinding) Bindings.add((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpressionBase, javafx.beans.binding.NumberExpression
    public DoubleBinding subtract(ObservableNumberValue other) {
        return (DoubleBinding) Bindings.subtract(this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding subtract(double other) {
        return Bindings.subtract(this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding subtract(float other) {
        return (DoubleBinding) Bindings.subtract((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding subtract(long other) {
        return (DoubleBinding) Bindings.subtract((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding subtract(int other) {
        return (DoubleBinding) Bindings.subtract((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpressionBase, javafx.beans.binding.NumberExpression
    public DoubleBinding multiply(ObservableNumberValue other) {
        return (DoubleBinding) Bindings.multiply(this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding multiply(double other) {
        return Bindings.multiply(this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding multiply(float other) {
        return (DoubleBinding) Bindings.multiply((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding multiply(long other) {
        return (DoubleBinding) Bindings.multiply((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding multiply(int other) {
        return (DoubleBinding) Bindings.multiply((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpressionBase, javafx.beans.binding.NumberExpression
    public DoubleBinding divide(ObservableNumberValue other) {
        return (DoubleBinding) Bindings.divide(this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding divide(double other) {
        return Bindings.divide(this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding divide(float other) {
        return (DoubleBinding) Bindings.divide((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding divide(long other) {
        return (DoubleBinding) Bindings.divide((ObservableNumberValue) this, other);
    }

    @Override // javafx.beans.binding.NumberExpression
    public DoubleBinding divide(int other) {
        return (DoubleBinding) Bindings.divide((ObservableNumberValue) this, other);
    }

    public ObjectExpression<Double> asObject() {
        return new ObjectBinding<Double>() { // from class: javafx.beans.binding.DoubleExpression.3
            {
                bind(DoubleExpression.this);
            }

            @Override // javafx.beans.binding.ObjectBinding, javafx.beans.binding.Binding
            public void dispose() {
                unbind(DoubleExpression.this);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // javafx.beans.binding.ObjectBinding
            public Double computeValue() {
                return DoubleExpression.this.getValue2();
            }
        };
    }
}
