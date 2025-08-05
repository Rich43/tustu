package javafx.beans.property;

import javafx.beans.binding.Bindings;
import javafx.beans.value.WritableListValue;
import javafx.collections.ObservableList;

/* loaded from: jfxrt.jar:javafx/beans/property/ListProperty.class */
public abstract class ListProperty<E> extends ReadOnlyListProperty<E> implements Property<ObservableList<E>>, WritableListValue<E> {
    @Override // javafx.beans.value.WritableValue, javafx.beans.value.WritableBooleanValue
    public void setValue(ObservableList<E> v2) {
        set(v2);
    }

    @Override // javafx.beans.property.Property
    public void bindBidirectional(Property<ObservableList<E>> other) {
        Bindings.bindBidirectional(this, other);
    }

    @Override // javafx.beans.property.Property
    public void unbindBidirectional(Property<ObservableList<E>> other) {
        Bindings.unbindBidirectional((Property) this, (Property) other);
    }

    @Override // javafx.beans.property.ReadOnlyListProperty
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("ListProperty [");
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
