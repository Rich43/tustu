package javafx.beans.binding;

import com.sun.javafx.binding.StringFormatter;
import javafx.beans.value.ObservableStringValue;
import javafx.beans.value.ObservableValue;

/* loaded from: jfxrt.jar:javafx/beans/binding/StringExpression.class */
public abstract class StringExpression implements ObservableStringValue {
    @Override // javafx.beans.value.ObservableValue
    /* renamed from: getValue */
    public String getValue2() {
        return get();
    }

    public final String getValueSafe() {
        String value = get();
        return value == null ? "" : value;
    }

    public static StringExpression stringExpression(ObservableValue<?> value) {
        if (value == null) {
            throw new NullPointerException("Value must be specified.");
        }
        return StringFormatter.convert(value);
    }

    public StringExpression concat(Object other) {
        return Bindings.concat(this, other);
    }

    public BooleanBinding isEqualTo(ObservableStringValue other) {
        return Bindings.equal((ObservableStringValue) this, other);
    }

    public BooleanBinding isEqualTo(String other) {
        return Bindings.equal((ObservableStringValue) this, other);
    }

    public BooleanBinding isNotEqualTo(ObservableStringValue other) {
        return Bindings.notEqual((ObservableStringValue) this, other);
    }

    public BooleanBinding isNotEqualTo(String other) {
        return Bindings.notEqual((ObservableStringValue) this, other);
    }

    public BooleanBinding isEqualToIgnoreCase(ObservableStringValue other) {
        return Bindings.equalIgnoreCase(this, other);
    }

    public BooleanBinding isEqualToIgnoreCase(String other) {
        return Bindings.equalIgnoreCase(this, other);
    }

    public BooleanBinding isNotEqualToIgnoreCase(ObservableStringValue other) {
        return Bindings.notEqualIgnoreCase(this, other);
    }

    public BooleanBinding isNotEqualToIgnoreCase(String other) {
        return Bindings.notEqualIgnoreCase(this, other);
    }

    public BooleanBinding greaterThan(ObservableStringValue other) {
        return Bindings.greaterThan(this, other);
    }

    public BooleanBinding greaterThan(String other) {
        return Bindings.greaterThan(this, other);
    }

    public BooleanBinding lessThan(ObservableStringValue other) {
        return Bindings.lessThan(this, other);
    }

    public BooleanBinding lessThan(String other) {
        return Bindings.lessThan(this, other);
    }

    public BooleanBinding greaterThanOrEqualTo(ObservableStringValue other) {
        return Bindings.greaterThanOrEqual(this, other);
    }

    public BooleanBinding greaterThanOrEqualTo(String other) {
        return Bindings.greaterThanOrEqual(this, other);
    }

    public BooleanBinding lessThanOrEqualTo(ObservableStringValue other) {
        return Bindings.lessThanOrEqual(this, other);
    }

    public BooleanBinding lessThanOrEqualTo(String other) {
        return Bindings.lessThanOrEqual(this, other);
    }

    public BooleanBinding isNull() {
        return Bindings.isNull(this);
    }

    public BooleanBinding isNotNull() {
        return Bindings.isNotNull(this);
    }

    public IntegerBinding length() {
        return Bindings.length(this);
    }

    public BooleanBinding isEmpty() {
        return Bindings.isEmpty(this);
    }

    public BooleanBinding isNotEmpty() {
        return Bindings.isNotEmpty(this);
    }
}
