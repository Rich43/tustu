package javafx.beans.property;

import javafx.beans.binding.Bindings;
import javafx.beans.value.WritableSetValue;
import javafx.collections.ObservableSet;

/* loaded from: jfxrt.jar:javafx/beans/property/SetProperty.class */
public abstract class SetProperty<E> extends ReadOnlySetProperty<E> implements Property<ObservableSet<E>>, WritableSetValue<E> {
    @Override // javafx.beans.value.WritableValue, javafx.beans.value.WritableBooleanValue
    public void setValue(ObservableSet<E> v2) {
        set(v2);
    }

    @Override // javafx.beans.property.Property
    public void bindBidirectional(Property<ObservableSet<E>> other) {
        Bindings.bindBidirectional(this, other);
    }

    @Override // javafx.beans.property.Property
    public void unbindBidirectional(Property<ObservableSet<E>> other) {
        Bindings.unbindBidirectional((Property) this, (Property) other);
    }

    @Override // javafx.beans.property.ReadOnlySetProperty
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("SetProperty [");
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
