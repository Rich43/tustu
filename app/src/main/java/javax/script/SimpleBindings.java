package javax.script;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:javax/script/SimpleBindings.class */
public class SimpleBindings implements Bindings {
    private Map<String, Object> map;

    public SimpleBindings(Map<String, Object> map) {
        if (map == null) {
            throw new NullPointerException();
        }
        this.map = map;
    }

    public SimpleBindings() {
        this(new HashMap());
    }

    /* JADX WARN: Can't rename method to resolve collision */
    @Override // javax.script.Bindings, java.util.Map
    public Object put(String str, Object obj) {
        checkKey(str);
        return this.map.put(str, obj);
    }

    @Override // javax.script.Bindings, java.util.Map
    public void putAll(Map<? extends String, ? extends Object> map) {
        if (map == null) {
            throw new NullPointerException("toMerge map is null");
        }
        for (Map.Entry<? extends String, ? extends Object> entry : map.entrySet()) {
            String key = entry.getKey();
            checkKey(key);
            put(key, entry.getValue());
        }
    }

    @Override // java.util.Map
    public void clear() {
        this.map.clear();
    }

    @Override // javax.script.Bindings, java.util.Map
    public boolean containsKey(Object obj) {
        checkKey(obj);
        return this.map.containsKey(obj);
    }

    @Override // java.util.Map
    public boolean containsValue(Object obj) {
        return this.map.containsValue(obj);
    }

    @Override // java.util.Map
    public Set<Map.Entry<String, Object>> entrySet() {
        return this.map.entrySet();
    }

    @Override // javax.script.Bindings, java.util.Map
    public Object get(Object obj) {
        checkKey(obj);
        return this.map.get(obj);
    }

    @Override // java.util.Map
    public boolean isEmpty() {
        return this.map.isEmpty();
    }

    @Override // java.util.Map
    public Set<String> keySet() {
        return this.map.keySet();
    }

    @Override // javax.script.Bindings, java.util.Map
    public Object remove(Object obj) {
        checkKey(obj);
        return this.map.remove(obj);
    }

    @Override // java.util.Map
    public int size() {
        return this.map.size();
    }

    @Override // java.util.Map
    public Collection<Object> values() {
        return this.map.values();
    }

    private void checkKey(Object obj) {
        if (obj == null) {
            throw new NullPointerException("key can not be null");
        }
        if (!(obj instanceof String)) {
            throw new ClassCastException("key should be a String");
        }
        if (obj.equals("")) {
            throw new IllegalArgumentException("key can not be empty");
        }
    }
}
