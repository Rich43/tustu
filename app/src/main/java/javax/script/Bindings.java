package javax.script;

import java.util.Map;

/* loaded from: rt.jar:javax/script/Bindings.class */
public interface Bindings extends Map<String, Object> {
    @Override // java.util.Map
    Object put(String str, Object obj);

    @Override // java.util.Map
    void putAll(Map<? extends String, ? extends Object> map);

    @Override // java.util.Map
    boolean containsKey(Object obj);

    @Override // java.util.Map
    Object get(Object obj);

    @Override // java.util.Map
    Object remove(Object obj);
}
