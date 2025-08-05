package javafx.beans.property;

import javafx.beans.binding.Bindings;
import javafx.beans.value.WritableObjectValue;

/* loaded from: jfxrt.jar:javafx/beans/property/ObjectProperty.class */
public abstract class ObjectProperty<T> extends ReadOnlyObjectProperty<T> implements Property<T>, WritableObjectValue<T> {
    @Override // javafx.beans.value.WritableValue, javafx.beans.value.WritableBooleanValue
    public void setValue(T v2) {
        set(v2);
    }

    @Override // javafx.beans.property.Property
    public void bindBidirectional(Property<T> other) {
        Bindings.bindBidirectional(this, other);
    }

    @Override // javafx.beans.property.Property
    public void unbindBidirectional(Property<T> other) {
        Bindings.unbindBidirectional((Property) this, (Property) other);
    }

    @Override // javafx.beans.property.ReadOnlyObjectProperty
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("ObjectProperty [");
        if (bean != null) {
            result.append("bean: ").append(bean).append(", ");
        }
        if (name != null && !name.equals("")) {
            result.append("name: ").append(name).append(", ");
        }
        result.append("value: ").append((Object) get()).append("]");
        return result.toString();
    }
}
