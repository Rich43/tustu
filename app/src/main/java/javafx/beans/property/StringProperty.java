package javafx.beans.property;

import java.text.Format;
import javafx.beans.binding.Bindings;
import javafx.beans.value.WritableStringValue;
import javafx.util.StringConverter;

/* loaded from: jfxrt.jar:javafx/beans/property/StringProperty.class */
public abstract class StringProperty extends ReadOnlyStringProperty implements Property<String>, WritableStringValue {
    @Override // javafx.beans.value.WritableValue, javafx.beans.value.WritableBooleanValue
    public void setValue(String v2) {
        set(v2);
    }

    @Override // javafx.beans.property.Property
    public void bindBidirectional(Property<String> other) {
        Bindings.bindBidirectional(this, other);
    }

    public void bindBidirectional(Property<?> other, Format format) {
        Bindings.bindBidirectional(this, other, format);
    }

    public <T> void bindBidirectional(Property<T> other, StringConverter<T> converter) {
        Bindings.bindBidirectional(this, other, converter);
    }

    @Override // javafx.beans.property.Property
    public void unbindBidirectional(Property<String> other) {
        Bindings.unbindBidirectional((Property) this, (Property) other);
    }

    public void unbindBidirectional(Object other) {
        Bindings.unbindBidirectional(this, other);
    }

    @Override // javafx.beans.property.ReadOnlyStringProperty
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("StringProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if (name != null && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append(get()).append("]");
        return result.toString();
    }
}
