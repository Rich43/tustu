package com.sun.xml.internal.ws.api;

import com.oracle.webservices.internal.api.message.BasePropertySet;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/PropertySet.class */
public abstract class PropertySet extends BasePropertySet {
    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.oracle.webservices.internal.api.message.BasePropertySet
    public abstract PropertyMap getPropertyMap();

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/PropertySet$PropertyMap.class */
    protected static class PropertyMap extends BasePropertySet.PropertyMap {
        protected PropertyMap() {
        }
    }

    protected static PropertyMap parse(Class clazz) {
        BasePropertySet.PropertyMap pm = BasePropertySet.parse(clazz);
        PropertyMap map = new PropertyMap();
        map.putAll(pm);
        return map;
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet, com.oracle.webservices.internal.api.message.PropertySet
    public Object get(Object key) {
        BasePropertySet.Accessor sp = getPropertyMap().get(key);
        if (sp != null) {
            return sp.get(this);
        }
        throw new IllegalArgumentException("Undefined property " + key);
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet, com.oracle.webservices.internal.api.message.PropertySet
    public Object put(String key, Object value) {
        BasePropertySet.Accessor sp = getPropertyMap().get(key);
        if (sp != null) {
            Object old = sp.get(this);
            sp.set(this, value);
            return old;
        }
        throw new IllegalArgumentException("Undefined property " + key);
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet, com.oracle.webservices.internal.api.message.PropertySet
    public boolean supports(Object key) {
        return getPropertyMap().containsKey(key);
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet, com.oracle.webservices.internal.api.message.PropertySet
    public Object remove(Object key) {
        BasePropertySet.Accessor sp = getPropertyMap().get(key);
        if (sp != null) {
            Object old = sp.get(this);
            sp.set(this, null);
            return old;
        }
        throw new IllegalArgumentException("Undefined property " + key);
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet
    protected void createEntrySet(Set<Map.Entry<String, Object>> core) {
        for (final Map.Entry<String, BasePropertySet.Accessor> e2 : getPropertyMap().entrySet()) {
            core.add(new Map.Entry<String, Object>() { // from class: com.sun.xml.internal.ws.api.PropertySet.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Map.Entry
                public String getKey() {
                    return (String) e2.getKey();
                }

                @Override // java.util.Map.Entry
                public Object getValue() {
                    return ((BasePropertySet.Accessor) e2.getValue()).get(PropertySet.this);
                }

                @Override // java.util.Map.Entry
                public Object setValue(Object value) {
                    BasePropertySet.Accessor acc = (BasePropertySet.Accessor) e2.getValue();
                    Object old = acc.get(PropertySet.this);
                    acc.set(PropertySet.this, value);
                    return old;
                }
            });
        }
    }
}
