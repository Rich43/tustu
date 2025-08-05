package javafx.beans.binding;

import com.sun.javafx.binding.StringFormatter;
import java.util.AbstractMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javafx.beans.InvalidationListener;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.value.ObservableMapValue;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

/* loaded from: jfxrt.jar:javafx/beans/binding/MapExpression.class */
public abstract class MapExpression<K, V> implements ObservableMapValue<K, V> {
    private static final ObservableMap EMPTY_MAP = new EmptyObservableMap();

    public abstract ReadOnlyIntegerProperty sizeProperty();

    public abstract ReadOnlyBooleanProperty emptyProperty();

    /* loaded from: jfxrt.jar:javafx/beans/binding/MapExpression$EmptyObservableMap.class */
    private static class EmptyObservableMap<K, V> extends AbstractMap<K, V> implements ObservableMap<K, V> {
        private EmptyObservableMap() {
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<K, V>> entrySet() {
            return Collections.emptySet();
        }

        @Override // javafx.collections.ObservableMap
        public void addListener(MapChangeListener<? super K, ? super V> mapChangeListener) {
        }

        @Override // javafx.collections.ObservableMap
        public void removeListener(MapChangeListener<? super K, ? super V> mapChangeListener) {
        }

        @Override // javafx.beans.Observable
        public void addListener(InvalidationListener listener) {
        }

        @Override // javafx.beans.Observable
        public void removeListener(InvalidationListener listener) {
        }
    }

    @Override // javafx.beans.value.ObservableValue
    /* renamed from: getValue */
    public ObservableMap<K, V> getValue2() {
        return get();
    }

    public static <K, V> MapExpression<K, V> mapExpression(final ObservableMapValue<K, V> value) {
        if (value == null) {
            throw new NullPointerException("Map must be specified.");
        }
        return value instanceof MapExpression ? (MapExpression) value : new MapBinding<K, V>() { // from class: javafx.beans.binding.MapExpression.1
            {
                super.bind(value);
            }

            @Override // javafx.beans.binding.MapBinding, javafx.beans.binding.Binding
            public void dispose() {
                super.unbind(value);
            }

            @Override // javafx.beans.binding.MapBinding
            protected ObservableMap<K, V> computeValue() {
                return value.get();
            }

            @Override // javafx.beans.binding.MapBinding, javafx.beans.binding.Binding
            public ObservableList<?> getDependencies() {
                return FXCollections.singletonObservableList(value);
            }
        };
    }

    public int getSize() {
        return size();
    }

    public ObjectBinding<V> valueAt(K key) {
        return Bindings.valueAt(this, key);
    }

    public ObjectBinding<V> valueAt(ObservableValue<K> key) {
        return Bindings.valueAt((ObservableMap) this, (ObservableValue) key);
    }

    public BooleanBinding isEqualTo(ObservableMap<?, ?> other) {
        return Bindings.equal(this, other);
    }

    public BooleanBinding isNotEqualTo(ObservableMap<?, ?> other) {
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

    @Override // java.util.Map
    public int size() {
        ObservableMap<K, V> map = get();
        return map == null ? EMPTY_MAP.size() : map.size();
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        ObservableMap<K, V> map = get();
        return map == null ? EMPTY_MAP.isEmpty() : map.isEmpty();
    }

    @Override // java.util.Map
    public boolean containsKey(Object obj) {
        ObservableMap<K, V> map = get();
        return map == null ? EMPTY_MAP.containsKey(obj) : map.containsKey(obj);
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        ObservableMap<K, V> map = get();
        return map == null ? EMPTY_MAP.containsValue(obj) : map.containsValue(obj);
    }

    @Override // java.util.Map
    public V put(K key, V value) {
        ObservableMap<K, V> map = get();
        return map == null ? EMPTY_MAP.put(key, value) : map.put(key, value);
    }

    @Override // java.util.Map
    public V remove(Object obj) {
        ObservableMap<K, V> map = get();
        return map == null ? EMPTY_MAP.remove(obj) : map.remove(obj);
    }

    @Override // java.util.Map
    public void putAll(Map<? extends K, ? extends V> elements) {
        ObservableMap<K, V> map = get();
        if (map == null) {
            EMPTY_MAP.putAll(elements);
        } else {
            map.putAll(elements);
        }
    }

    @Override // java.util.Map
    public void clear() {
        ObservableMap<K, V> map = get();
        if (map == null) {
            EMPTY_MAP.clear();
        } else {
            map.clear();
        }
    }

    @Override // java.util.Map
    public Set<K> keySet() {
        ObservableMap<K, V> map = get();
        return map == null ? EMPTY_MAP.keySet() : map.keySet();
    }

    @Override // java.util.Map
    public Collection<V> values() {
        ObservableMap<K, V> map = get();
        return map == null ? EMPTY_MAP.values() : map.values();
    }

    @Override // java.util.Map
    public Set<Map.Entry<K, V>> entrySet() {
        ObservableMap<K, V> map = get();
        return map == null ? EMPTY_MAP.entrySet() : map.entrySet();
    }

    @Override // java.util.Map
    public V get(Object key) {
        ObservableMap<K, V> map = get();
        return map == null ? EMPTY_MAP.get(key) : map.get(key);
    }
}
