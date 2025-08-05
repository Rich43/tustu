package com.oracle.webservices.internal.api.message;

import com.oracle.webservices.internal.api.message.PropertySet;
import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.javafx.fxml.BeanAdapter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/* loaded from: rt.jar:com/oracle/webservices/internal/api/message/BasePropertySet.class */
public abstract class BasePropertySet implements PropertySet {
    private Map<String, Object> mapView;

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:com/oracle/webservices/internal/api/message/BasePropertySet$Accessor.class */
    public interface Accessor {
        String getName();

        boolean hasValue(PropertySet propertySet);

        Object get(PropertySet propertySet);

        void set(PropertySet propertySet, Object obj);
    }

    protected abstract PropertyMap getPropertyMap();

    protected BasePropertySet() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:com/oracle/webservices/internal/api/message/BasePropertySet$PropertyMap.class */
    public static class PropertyMap extends HashMap<String, Accessor> {
        transient PropertyMapEntry[] cachedEntries = null;

        protected PropertyMap() {
        }

        PropertyMapEntry[] getPropertyMapEntries() {
            if (this.cachedEntries == null) {
                this.cachedEntries = createPropertyMapEntries();
            }
            return this.cachedEntries;
        }

        private PropertyMapEntry[] createPropertyMapEntries() {
            PropertyMapEntry[] modelEntries = new PropertyMapEntry[size()];
            int i2 = 0;
            for (Map.Entry<String, Accessor> e2 : entrySet()) {
                int i3 = i2;
                i2++;
                modelEntries[i3] = new PropertyMapEntry(e2.getKey(), e2.getValue());
            }
            return modelEntries;
        }
    }

    /* loaded from: rt.jar:com/oracle/webservices/internal/api/message/BasePropertySet$PropertyMapEntry.class */
    public static class PropertyMapEntry {
        String key;
        Accessor value;

        public PropertyMapEntry(String k2, Accessor v2) {
            this.key = k2;
            this.value = v2;
        }
    }

    protected static PropertyMap parse(final Class clazz) {
        return (PropertyMap) AccessController.doPrivileged(new PrivilegedAction<PropertyMap>() { // from class: com.oracle.webservices.internal.api.message.BasePropertySet.1
            static final /* synthetic */ boolean $assertionsDisabled;

            static {
                $assertionsDisabled = !BasePropertySet.class.desiredAssertionStatus();
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public PropertyMap run2() throws SecurityException {
                Method setter;
                PropertyMap props = new PropertyMap();
                Class superclass = clazz;
                while (true) {
                    Class c2 = superclass;
                    if (c2 != null) {
                        for (Field f2 : c2.getDeclaredFields()) {
                            PropertySet.Property cp = (PropertySet.Property) f2.getAnnotation(PropertySet.Property.class);
                            if (cp != null) {
                                for (String value : cp.value()) {
                                    props.put(value, new FieldAccessor(f2, value));
                                }
                            }
                        }
                        for (Method m2 : c2.getDeclaredMethods()) {
                            PropertySet.Property cp2 = (PropertySet.Property) m2.getAnnotation(PropertySet.Property.class);
                            if (cp2 != null) {
                                String name = m2.getName();
                                if (!$assertionsDisabled && !name.startsWith("get") && !name.startsWith(BeanAdapter.IS_PREFIX)) {
                                    throw new AssertionError();
                                }
                                String setName = name.startsWith(BeanAdapter.IS_PREFIX) ? "set" + name.substring(2) : 's' + name.substring(1);
                                try {
                                    setter = clazz.getMethod(setName, m2.getReturnType());
                                } catch (NoSuchMethodException e2) {
                                    setter = null;
                                }
                                for (String value2 : cp2.value()) {
                                    props.put(value2, new MethodAccessor(m2, setter, value2));
                                }
                            }
                        }
                        superclass = c2.getSuperclass();
                    } else {
                        return props;
                    }
                }
            }
        });
    }

    /* loaded from: rt.jar:com/oracle/webservices/internal/api/message/BasePropertySet$FieldAccessor.class */
    static final class FieldAccessor implements Accessor {

        /* renamed from: f, reason: collision with root package name */
        private final Field f11800f;
        private final String name;

        protected FieldAccessor(Field f2, String name) {
            this.f11800f = f2;
            f2.setAccessible(true);
            this.name = name;
        }

        @Override // com.oracle.webservices.internal.api.message.BasePropertySet.Accessor
        public String getName() {
            return this.name;
        }

        @Override // com.oracle.webservices.internal.api.message.BasePropertySet.Accessor
        public boolean hasValue(PropertySet props) {
            return get(props) != null;
        }

        @Override // com.oracle.webservices.internal.api.message.BasePropertySet.Accessor
        public Object get(PropertySet props) {
            try {
                return this.f11800f.get(props);
            } catch (IllegalAccessException e2) {
                throw new AssertionError();
            }
        }

        @Override // com.oracle.webservices.internal.api.message.BasePropertySet.Accessor
        public void set(PropertySet props, Object value) throws IllegalArgumentException {
            try {
                this.f11800f.set(props, value);
            } catch (IllegalAccessException e2) {
                throw new AssertionError();
            }
        }
    }

    /* loaded from: rt.jar:com/oracle/webservices/internal/api/message/BasePropertySet$MethodAccessor.class */
    static final class MethodAccessor implements Accessor {

        @NotNull
        private final Method getter;

        @Nullable
        private final Method setter;
        private final String name;

        protected MethodAccessor(Method getter, Method setter, String value) {
            this.getter = getter;
            this.setter = setter;
            this.name = value;
            getter.setAccessible(true);
            if (setter != null) {
                setter.setAccessible(true);
            }
        }

        @Override // com.oracle.webservices.internal.api.message.BasePropertySet.Accessor
        public String getName() {
            return this.name;
        }

        @Override // com.oracle.webservices.internal.api.message.BasePropertySet.Accessor
        public boolean hasValue(PropertySet props) {
            return get(props) != null;
        }

        @Override // com.oracle.webservices.internal.api.message.BasePropertySet.Accessor
        public Object get(PropertySet props) {
            try {
                return this.getter.invoke(props, new Object[0]);
            } catch (IllegalAccessException e2) {
                throw new AssertionError();
            } catch (InvocationTargetException e3) {
                handle(e3);
                return 0;
            }
        }

        @Override // com.oracle.webservices.internal.api.message.BasePropertySet.Accessor
        public void set(PropertySet props, Object value) throws IllegalArgumentException {
            if (this.setter == null) {
                throw new ReadOnlyPropertyException(getName());
            }
            try {
                this.setter.invoke(props, value);
            } catch (IllegalAccessException e2) {
                throw new AssertionError();
            } catch (InvocationTargetException e3) {
                handle(e3);
            }
        }

        private Exception handle(InvocationTargetException e2) {
            Throwable t2 = e2.getTargetException();
            if (t2 instanceof Error) {
                throw ((Error) t2);
            }
            if (t2 instanceof RuntimeException) {
                throw ((RuntimeException) t2);
            }
            throw new Error(e2);
        }
    }

    /* loaded from: rt.jar:com/oracle/webservices/internal/api/message/BasePropertySet$MapView.class */
    final class MapView extends HashMap<String, Object> {
        boolean extensible;

        MapView(boolean extensible) {
            super(BasePropertySet.this.getPropertyMap().getPropertyMapEntries().length);
            this.extensible = extensible;
            initialize();
        }

        public void initialize() {
            PropertyMapEntry[] entries = BasePropertySet.this.getPropertyMap().getPropertyMapEntries();
            for (PropertyMapEntry entry : entries) {
                super.put((MapView) entry.key, (String) entry.value);
            }
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public Object get(Object key) {
            Object o2 = super.get(key);
            if (o2 instanceof Accessor) {
                return ((Accessor) o2).get(BasePropertySet.this);
            }
            return o2;
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<String, Object>> entrySet() {
            Set<Map.Entry<String, Object>> entries = new HashSet<>();
            for (String key : keySet()) {
                entries.add(new AbstractMap.SimpleImmutableEntry<>(key, get(key)));
            }
            return entries;
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public Object put(String key, Object value) {
            Object o2 = super.get(key);
            if (o2 != null && (o2 instanceof Accessor)) {
                Object oldValue = ((Accessor) o2).get(BasePropertySet.this);
                ((Accessor) o2).set(BasePropertySet.this, value);
                return oldValue;
            }
            if (this.extensible) {
                return super.put((MapView) key, (String) value);
            }
            throw new IllegalStateException("Unknown property [" + key + "] for PropertySet [" + BasePropertySet.this.getClass().getName() + "]");
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public void clear() {
            for (String key : keySet()) {
                remove(key);
            }
        }

        @Override // java.util.HashMap, java.util.AbstractMap, java.util.Map
        public Object remove(Object key) {
            Object o2 = super.get(key);
            if (o2 instanceof Accessor) {
                ((Accessor) o2).set(BasePropertySet.this, null);
            }
            return super.remove(key);
        }
    }

    @Override // com.oracle.webservices.internal.api.message.PropertySet
    public boolean containsKey(Object key) {
        Accessor sp = getPropertyMap().get(key);
        return (sp == null || sp.get(this) == null) ? false : true;
    }

    @Override // com.oracle.webservices.internal.api.message.PropertySet
    public Object get(Object key) {
        Accessor sp = getPropertyMap().get(key);
        if (sp != null) {
            return sp.get(this);
        }
        throw new IllegalArgumentException("Undefined property " + key);
    }

    @Override // com.oracle.webservices.internal.api.message.PropertySet
    public Object put(String key, Object value) {
        Accessor sp = getPropertyMap().get(key);
        if (sp != null) {
            Object old = sp.get(this);
            sp.set(this, value);
            return old;
        }
        throw new IllegalArgumentException("Undefined property " + key);
    }

    @Override // com.oracle.webservices.internal.api.message.PropertySet
    public boolean supports(Object key) {
        return getPropertyMap().containsKey(key);
    }

    @Override // com.oracle.webservices.internal.api.message.PropertySet
    public Object remove(Object key) {
        Accessor sp = getPropertyMap().get(key);
        if (sp != null) {
            Object old = sp.get(this);
            sp.set(this, null);
            return old;
        }
        throw new IllegalArgumentException("Undefined property " + key);
    }

    @Override // com.oracle.webservices.internal.api.message.PropertySet
    @Deprecated
    public final Map<String, Object> createMapView() {
        final Set<Map.Entry<String, Object>> core = new HashSet<>();
        createEntrySet(core);
        return new AbstractMap<String, Object>() { // from class: com.oracle.webservices.internal.api.message.BasePropertySet.2
            @Override // java.util.AbstractMap, java.util.Map
            public Set<Map.Entry<String, Object>> entrySet() {
                return core;
            }
        };
    }

    @Override // com.oracle.webservices.internal.api.message.PropertySet
    public Map<String, Object> asMap() {
        if (this.mapView == null) {
            this.mapView = createView();
        }
        return this.mapView;
    }

    protected Map<String, Object> createView() {
        return new MapView(mapAllowsAdditionalProperties());
    }

    protected boolean mapAllowsAdditionalProperties() {
        return false;
    }

    protected void createEntrySet(Set<Map.Entry<String, Object>> core) {
        for (final Map.Entry<String, Accessor> e2 : getPropertyMap().entrySet()) {
            core.add(new Map.Entry<String, Object>() { // from class: com.oracle.webservices.internal.api.message.BasePropertySet.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Map.Entry
                public String getKey() {
                    return (String) e2.getKey();
                }

                @Override // java.util.Map.Entry
                public Object getValue() {
                    return ((Accessor) e2.getValue()).get(BasePropertySet.this);
                }

                @Override // java.util.Map.Entry
                public Object setValue(Object value) {
                    Accessor acc = (Accessor) e2.getValue();
                    Object old = acc.get(BasePropertySet.this);
                    acc.set(BasePropertySet.this, value);
                    return old;
                }
            });
        }
    }
}
