package com.oracle.webservices.internal.api.message;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import java.util.AbstractMap;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:com/oracle/webservices/internal/api/message/BaseDistributedPropertySet.class */
public abstract class BaseDistributedPropertySet extends BasePropertySet implements DistributedPropertySet {
    private final Map<Class<? extends PropertySet>, PropertySet> satellites = new IdentityHashMap();
    private final Map<String, Object> viewthis = super.createView();

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.oracle.webservices.internal.api.message.DistributedPropertySet
    public void addSatellite(@NotNull PropertySet satellite) {
        addSatellite(satellite.getClass(), satellite);
    }

    @Override // com.oracle.webservices.internal.api.message.DistributedPropertySet
    public void addSatellite(@NotNull Class<? extends PropertySet> keyClass, @NotNull PropertySet satellite) {
        this.satellites.put(keyClass, satellite);
    }

    @Override // com.oracle.webservices.internal.api.message.DistributedPropertySet
    public void removeSatellite(PropertySet satellite) {
        this.satellites.remove(satellite.getClass());
    }

    public void copySatelliteInto(@NotNull DistributedPropertySet r2) {
        for (Map.Entry<Class<? extends PropertySet>, PropertySet> entry : this.satellites.entrySet()) {
            r2.addSatellite(entry.getKey(), entry.getValue());
        }
    }

    @Override // com.oracle.webservices.internal.api.message.DistributedPropertySet
    public void copySatelliteInto(MessageContext r2) {
        copySatelliteInto((DistributedPropertySet) r2);
    }

    @Override // com.oracle.webservices.internal.api.message.DistributedPropertySet
    @Nullable
    public <T extends PropertySet> T getSatellite(Class<T> cls) {
        T t2;
        T t3 = (T) this.satellites.get(cls);
        if (t3 != null) {
            return t3;
        }
        for (PropertySet propertySet : this.satellites.values()) {
            if (cls.isInstance(propertySet)) {
                return cls.cast(propertySet);
            }
            if (DistributedPropertySet.class.isInstance(propertySet) && (t2 = (T) ((DistributedPropertySet) DistributedPropertySet.class.cast(propertySet)).getSatellite(cls)) != null) {
                return t2;
            }
        }
        return null;
    }

    @Override // com.oracle.webservices.internal.api.message.DistributedPropertySet
    public Map<Class<? extends PropertySet>, PropertySet> getSatellites() {
        return this.satellites;
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet, com.oracle.webservices.internal.api.message.PropertySet
    public Object get(Object key) {
        for (PropertySet child : this.satellites.values()) {
            if (child.supports(key)) {
                return child.get(key);
            }
        }
        return super.get(key);
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet, com.oracle.webservices.internal.api.message.PropertySet
    public Object put(String key, Object value) {
        for (PropertySet child : this.satellites.values()) {
            if (child.supports(key)) {
                return child.put(key, value);
            }
        }
        return super.put(key, value);
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet, com.oracle.webservices.internal.api.message.PropertySet
    public boolean containsKey(Object key) {
        if (this.viewthis.containsKey(key)) {
            return true;
        }
        for (PropertySet child : this.satellites.values()) {
            if (child.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet, com.oracle.webservices.internal.api.message.PropertySet
    public boolean supports(Object key) {
        for (PropertySet child : this.satellites.values()) {
            if (child.supports(key)) {
                return true;
            }
        }
        return super.supports(key);
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet, com.oracle.webservices.internal.api.message.PropertySet
    public Object remove(Object key) {
        for (PropertySet child : this.satellites.values()) {
            if (child.supports(key)) {
                return child.remove(key);
            }
        }
        return super.remove(key);
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet
    protected void createEntrySet(Set<Map.Entry<String, Object>> core) {
        super.createEntrySet(core);
        for (PropertySet child : this.satellites.values()) {
            ((BasePropertySet) child).createEntrySet(core);
        }
    }

    protected Map<String, Object> asMapLocal() {
        return this.viewthis;
    }

    protected boolean supportsLocal(Object key) {
        return super.supports(key);
    }

    /* loaded from: rt.jar:com/oracle/webservices/internal/api/message/BaseDistributedPropertySet$DistributedMapView.class */
    class DistributedMapView extends AbstractMap<String, Object> {
        DistributedMapView() {
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Object get(Object key) {
            for (PropertySet child : BaseDistributedPropertySet.this.satellites.values()) {
                if (child.supports(key)) {
                    return child.get(key);
                }
            }
            return BaseDistributedPropertySet.this.viewthis.get(key);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            int size = BaseDistributedPropertySet.this.viewthis.size();
            for (PropertySet child : BaseDistributedPropertySet.this.satellites.values()) {
                size += child.asMap().size();
            }
            return size;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object key) {
            if (!BaseDistributedPropertySet.this.viewthis.containsKey(key)) {
                for (PropertySet child : BaseDistributedPropertySet.this.satellites.values()) {
                    if (child.containsKey(key)) {
                        return true;
                    }
                }
                return false;
            }
            return true;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<String, Object>> entrySet() {
            Set<Map.Entry<String, Object>> entries = new HashSet<>();
            for (PropertySet child : BaseDistributedPropertySet.this.satellites.values()) {
                for (Map.Entry<String, Object> entry : child.asMap().entrySet()) {
                    entries.add(new AbstractMap.SimpleImmutableEntry<>(entry.getKey(), entry.getValue()));
                }
            }
            for (Map.Entry<String, Object> entry2 : BaseDistributedPropertySet.this.viewthis.entrySet()) {
                entries.add(new AbstractMap.SimpleImmutableEntry<>(entry2.getKey(), entry2.getValue()));
            }
            return entries;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Object put(String key, Object value) {
            for (PropertySet child : BaseDistributedPropertySet.this.satellites.values()) {
                if (child.supports(key)) {
                    return child.put(key, value);
                }
            }
            return BaseDistributedPropertySet.this.viewthis.put(key, value);
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            BaseDistributedPropertySet.this.satellites.clear();
            BaseDistributedPropertySet.this.viewthis.clear();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Object remove(Object key) {
            for (PropertySet child : BaseDistributedPropertySet.this.satellites.values()) {
                if (child.supports(key)) {
                    return child.remove(key);
                }
            }
            return BaseDistributedPropertySet.this.viewthis.remove(key);
        }
    }

    @Override // com.oracle.webservices.internal.api.message.BasePropertySet
    protected Map<String, Object> createView() {
        return new DistributedMapView();
    }
}
