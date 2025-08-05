package javafx.beans.binding;

import com.sun.javafx.binding.StringFormatter;
import javafx.beans.value.ObservableBooleanValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/beans/binding/BooleanExpression.class */
public abstract class BooleanExpression implements ObservableBooleanValue {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javafx.beans.value.ObservableValue
    /* renamed from: getValue */
    public Boolean getValue2() {
        return Boolean.valueOf(get());
    }

    public static BooleanExpression booleanExpression(final ObservableBooleanValue value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return value instanceof BooleanExpression ? (BooleanExpression) value : new BooleanBinding() { // from class: javafx.beans.binding.BooleanExpression.1
            {
                super.bind(value);
            }

            @Override // javafx.beans.binding.BooleanBinding, javafx.beans.binding.Binding
            public void dispose() {
                super.unbind(value);
            }

            @Override // javafx.beans.binding.BooleanBinding
            protected boolean computeValue() {
                return value.get();
            }

            @Override // javafx.beans.binding.BooleanBinding, javafx.beans.binding.Binding
            public ObservableList<ObservableBooleanValue> getDependencies() {
                return FXCollections.singletonObservableList(value);
            }
        };
    }

    public static BooleanExpression booleanExpression(final ObservableValue<Boolean> value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return value instanceof BooleanExpression ? (BooleanExpression) value : new BooleanBinding() { // from class: javafx.beans.binding.BooleanExpression.2
            {
                super.bind(value);
            }

            @Override // javafx.beans.binding.BooleanBinding, javafx.beans.binding.Binding
            public void dispose() {
                super.unbind(value);
            }

            @Override // javafx.beans.binding.BooleanBinding
            protected boolean computeValue() {
                Boolean val = (Boolean) value.getValue2();
                if (val == null) {
                    return false;
                }
                return val.booleanValue();
            }

            @Override // javafx.beans.binding.BooleanBinding, javafx.beans.binding.Binding
            public ObservableList<ObservableValue<Boolean>> getDependencies() {
                return FXCollections.singletonObservableList(value);
            }
        };
    }

    public BooleanBinding and(ObservableBooleanValue other) {
        return Bindings.and(this, other);
    }

    public BooleanBinding or(ObservableBooleanValue other) {
        return Bindings.or(this, other);
    }

    public BooleanBinding not() {
        return Bindings.not(this);
    }

    public BooleanBinding isEqualTo(ObservableBooleanValue other) {
        return Bindings.equal(this, other);
    }

    public BooleanBinding isNotEqualTo(ObservableBooleanValue other) {
        return Bindings.notEqual(this, other);
    }

    public StringBinding asString() {
        return (StringBinding) StringFormatter.convert(this);
    }

    public ObjectExpression<Boolean> asObject() {
        return new ObjectBinding<Boolean>() { // from class: javafx.beans.binding.BooleanExpression.3
            {
                bind(BooleanExpression.this);
            }

            @Override // javafx.beans.binding.ObjectBinding, javafx.beans.binding.Binding
            public void dispose() {
                unbind(BooleanExpression.this);
            }

            /* JADX INFO: Access modifiers changed from: protected */
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // javafx.beans.binding.ObjectBinding
            public Boolean computeValue() {
                return BooleanExpression.this.getValue2();
            }
        };
    }
}
