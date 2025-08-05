package javafx.beans.property;

import java.util.Map;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.MapExpression;
import javafx.collections.ObservableMap;

/* loaded from: jfxrt.jar:javafx/beans/property/ReadOnlyMapProperty.class */
public abstract class ReadOnlyMapProperty<K, V> extends MapExpression<K, V> implements ReadOnlyProperty<ObservableMap<K, V>> {
    public void bindContentBidirectional(ObservableMap<K, V> map) {
        Bindings.bindContentBidirectional(this, map);
    }

    public void unbindContentBidirectional(Object object) {
        Bindings.unbindContentBidirectional(this, object);
    }

    public void bindContent(ObservableMap<K, V> map) {
        Bindings.bindContent(this, map);
    }

    public void unbindContent(Object object) {
        Bindings.unbindContent(this, object);
    }

    @Override // java.util.Map
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Map)) {
            return false;
        }
        Map<K, V> m2 = (Map) obj;
        if (m2.size() != size()) {
            return false;
        }
        try {
            for (Map.Entry<K, V> e2 : entrySet()) {
                K key = e2.getKey();
                V value = e2.getValue();
                if (value == null) {
                    if (m2.get(key) != null || !m2.containsKey(key)) {
                        return false;
                    }
                } else if (!value.equals(m2.get(key))) {
                    return false;
                }
            }
            return true;
        } catch (ClassCastException e3) {
            return false;
        } catch (NullPointerException e4) {
            return false;
        }
    }

    @Override // java.util.Map
    public int hashCode() {
        int h2 = 0;
        for (Map.Entry<K, V> e2 : entrySet()) {
            h2 += e2.hashCode();
        }
        return h2;
    }

    public String toString() {
        Object bean = getBean();
        String name = getName();
        StringBuilder result = new StringBuilder("ReadOnlyMapProperty [");
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
