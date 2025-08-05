package javafx.beans.binding;

import com.sun.javafx.binding.StringFormatter;
import java.util.Locale;
import javafx.beans.value.ObservableObjectValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/beans/binding/ObjectExpression.class */
public abstract class ObjectExpression<T> implements ObservableObjectValue<T> {
    @Override // javafx.beans.value.ObservableValue
    /* renamed from: getValue */
    public T getValue2() {
        return get();
    }

    public static <T> ObjectExpression<T> objectExpression(final ObservableObjectValue<T> value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return value instanceof ObjectExpression ? (ObjectExpression) value : new ObjectBinding<T>() { // from class: javafx.beans.binding.ObjectExpression.1
            {
                super.bind(value);
            }

            @Override // javafx.beans.binding.ObjectBinding, javafx.beans.binding.Binding
            public void dispose() {
                super.unbind(value);
            }

            @Override // javafx.beans.binding.ObjectBinding
            protected T computeValue() {
                return (T) value.get();
            }

            @Override // javafx.beans.binding.ObjectBinding, javafx.beans.binding.Binding
            public ObservableList<ObservableObjectValue<T>> getDependencies() {
                return FXCollections.singletonObservableList(value);
            }
        };
    }

    public BooleanBinding isEqualTo(ObservableObjectValue<?> other) {
        return Bindings.equal((ObservableObjectValue<?>) this, other);
    }

    public BooleanBinding isEqualTo(Object other) {
        return Bindings.equal(this, other);
    }

    public BooleanBinding isNotEqualTo(ObservableObjectValue<?> other) {
        return Bindings.notEqual((ObservableObjectValue<?>) this, other);
    }

    public BooleanBinding isNotEqualTo(Object other) {
        return Bindings.notEqual(this, other);
    }

    public BooleanBinding isNull() {
        return Bindings.isNull(this);
    }

    public BooleanBinding isNotNull() {
        return Bindings.isNotNull(this);
    }

    public StringBinding asString() {
        return (StringBinding) StringFormatter.convert(this);
    }

    public StringBinding asString(String format) {
        return (StringBinding) Bindings.format(format, this);
    }

    public StringBinding asString(Locale locale, String format) {
        return (StringBinding) Bindings.format(locale, format, this);
    }
}
