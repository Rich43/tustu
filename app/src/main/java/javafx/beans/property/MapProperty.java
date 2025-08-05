package javafx.beans.property;

import javafx.beans.binding.Bindings;
import javafx.beans.value.WritableMapValue;
import javafx.collections.ObservableMap;

/* loaded from: jfxrt.jar:javafx/beans/property/MapProperty.class */
public abstract class MapProperty<K, V> extends ReadOnlyMapProperty<K, V> implements Property<ObservableMap<K, V>>, WritableMapValue<K, V> {
    @Override // javafx.beans.value.WritableValue, javafx.beans.value.WritableBooleanValue
    public void setValue(ObservableMap<K, V> v2) {
        set(v2);
    }

    @Override // javafx.beans.property.Property
    public void bindBidirectional(Property<ObservableMap<K, V>> other) {
        Bindings.bindBidirectional(this, other);
    }

    @Override // javafx.beans.property.Property
    public void unbindBidirectional(Property<ObservableMap<K, V>> other) {
        Bindings.unbindBidirectional((Property) this, (Property) other);
    }

    @Override // javafx.beans.property.ReadOnlyMapProperty
    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("MapProperty [");
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
