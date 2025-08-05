package javafx.fxml;

import com.sun.javafx.fxml.BeanAdapter;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.util.Builder;
import sun.reflect.misc.MethodUtil;

/* compiled from: JavaFXBuilderFactory.java */
/* loaded from: jfxrt.jar:javafx/fxml/JavaFXBuilder.class */
final class JavaFXBuilder {
    private static final Object[] NO_ARGS;
    private static final Class<?>[] NO_SIG;
    private final Class<?> builderClass;
    private final Method createMethod;
    private final Method buildMethod;
    private final Map<String, Method> methods;
    private final Map<String, Method> getters;
    private final Map<String, Method> setters;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !JavaFXBuilder.class.desiredAssertionStatus();
        NO_ARGS = new Object[0];
        NO_SIG = new Class[0];
    }

    /* compiled from: JavaFXBuilderFactory.java */
    /* loaded from: jfxrt.jar:javafx/fxml/JavaFXBuilder$ObjectBuilder.class */
    final class ObjectBuilder extends AbstractMap<String, Object> implements Builder<Object> {
        private final Map<String, Object> containers;
        private Object builder;
        private Map<Object, Object> properties;

        private ObjectBuilder() {
            this.containers = new HashMap();
            this.builder = null;
            try {
                this.builder = MethodUtil.invoke(JavaFXBuilder.this.createMethod, null, JavaFXBuilder.NO_ARGS);
            } catch (Exception e2) {
                throw new RuntimeException("Creation of the builder " + JavaFXBuilder.this.builderClass.getName() + " failed.", e2);
            }
        }

        @Override // javafx.util.Builder
        /* renamed from: build */
        public Object build2() {
            for (Map.Entry<String, Object> entry : this.containers.entrySet()) {
                try {
                    put(entry.getKey(), entry.getValue());
                } finally {
                    this.builder = null;
                }
            }
            try {
                try {
                    Object res = MethodUtil.invoke(JavaFXBuilder.this.buildMethod, this.builder, JavaFXBuilder.NO_ARGS);
                    if (this.properties != null && (res instanceof Node)) {
                        ((Node) res).getProperties().putAll(this.properties);
                    }
                    return res;
                } catch (IllegalAccessException exception) {
                    throw new RuntimeException(exception);
                }
            } catch (InvocationTargetException exception2) {
                throw new RuntimeException(exception2);
            }
        }

        @Override // java.util.AbstractMap, java.util.Map
        public int size() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean isEmpty() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsKey(Object key) {
            return getTemporaryContainer(key.toString()) != null;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public boolean containsValue(Object value) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Object get(Object key) {
            return getTemporaryContainer(key.toString());
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Object put(String key, Object value) {
            List<?> list;
            if (!Node.class.isAssignableFrom(JavaFXBuilder.this.getTargetClass()) || !"properties".equals(key)) {
                try {
                    Method m2 = (Method) JavaFXBuilder.this.methods.get(key);
                    if (m2 == null) {
                        m2 = JavaFXBuilder.this.findMethod(key);
                        JavaFXBuilder.this.methods.put(key, m2);
                    }
                    try {
                        Class<?> type = m2.getParameterTypes()[0];
                        if (type.isArray()) {
                            if (value instanceof List) {
                                list = (List) value;
                            } else {
                                list = Arrays.asList(value.toString().split(","));
                            }
                            Class<?> componentType = type.getComponentType();
                            Object array = Array.newInstance(componentType, list.size());
                            for (int i2 = 0; i2 < list.size(); i2++) {
                                Array.set(array, i2, BeanAdapter.coerce(list.get(i2), componentType));
                            }
                            value = array;
                        }
                        MethodUtil.invoke(m2, this.builder, new Object[]{BeanAdapter.coerce(value, type)});
                        return null;
                    } catch (Exception e2) {
                        Logger.getLogger(JavaFXBuilder.class.getName()).log(Level.WARNING, "Method " + m2.getName() + " failed", (Throwable) e2);
                        return null;
                    }
                } catch (Exception e3) {
                    Logger.getLogger(JavaFXBuilder.class.getName()).log(Level.WARNING, "Failed to set " + ((Object) JavaFXBuilder.this.getTargetClass()) + "." + key + " using " + ((Object) JavaFXBuilder.this.builderClass), (Throwable) e3);
                    return null;
                }
            }
            this.properties = (Map) value;
            return null;
        }

        Object getReadOnlyProperty(String propName) {
            Class<?> type;
            if (JavaFXBuilder.this.setters.get(propName) != null) {
                return null;
            }
            Method getter = (Method) JavaFXBuilder.this.getters.get(propName);
            if (getter == null) {
                Method setter = null;
                Class<?> target = JavaFXBuilder.this.getTargetClass();
                String suffix = Character.toUpperCase(propName.charAt(0)) + propName.substring(1);
                try {
                    getter = MethodUtil.getMethod(target, "get" + suffix, JavaFXBuilder.NO_SIG);
                    setter = MethodUtil.getMethod(target, "set" + suffix, new Class[]{getter.getReturnType()});
                } catch (Exception e2) {
                }
                if (getter != null) {
                    JavaFXBuilder.this.getters.put(propName, getter);
                    JavaFXBuilder.this.setters.put(propName, setter);
                }
                if (setter != null) {
                    return null;
                }
            }
            if (getter == null) {
                Method m2 = JavaFXBuilder.this.findMethod(propName);
                if (m2 == null) {
                    return null;
                }
                type = m2.getParameterTypes()[0];
                if (type.isArray()) {
                    type = List.class;
                }
            } else {
                type = getter.getReturnType();
            }
            if (ObservableMap.class.isAssignableFrom(type)) {
                return FXCollections.observableMap(new HashMap());
            }
            if (Map.class.isAssignableFrom(type)) {
                return new HashMap();
            }
            if (ObservableList.class.isAssignableFrom(type)) {
                return FXCollections.observableArrayList();
            }
            if (List.class.isAssignableFrom(type)) {
                return new ArrayList();
            }
            if (Set.class.isAssignableFrom(type)) {
                return new HashSet();
            }
            return null;
        }

        public Object getTemporaryContainer(String propName) {
            Object o2 = this.containers.get(propName);
            if (o2 == null) {
                o2 = getReadOnlyProperty(propName);
                if (o2 != null) {
                    this.containers.put(propName, o2);
                }
            }
            return o2;
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Object remove(Object key) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void putAll(Map<? extends String, ? extends Object> m2) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<String> keySet() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Collection<Object> values() {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.AbstractMap, java.util.Map
        public Set<Map.Entry<String, Object>> entrySet() {
            throw new UnsupportedOperationException();
        }
    }

    JavaFXBuilder() {
        this.methods = new HashMap();
        this.getters = new HashMap();
        this.setters = new HashMap();
        this.builderClass = null;
        this.createMethod = null;
        this.buildMethod = null;
    }

    JavaFXBuilder(Class<?> builderClass) throws IllegalAccessException, NoSuchMethodException, InstantiationException {
        this.methods = new HashMap();
        this.getters = new HashMap();
        this.setters = new HashMap();
        this.builderClass = builderClass;
        this.createMethod = MethodUtil.getMethod(builderClass, "create", NO_SIG);
        this.buildMethod = MethodUtil.getMethod(builderClass, "build", NO_SIG);
        if (!$assertionsDisabled && !Modifier.isStatic(this.createMethod.getModifiers())) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && Modifier.isStatic(this.buildMethod.getModifiers())) {
            throw new AssertionError();
        }
    }

    Builder<Object> createBuilder() {
        return new ObjectBuilder();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Method findMethod(String name) {
        if (name.length() > 1 && Character.isUpperCase(name.charAt(1))) {
            name = Character.toUpperCase(name.charAt(0)) + name.substring(1);
        }
        for (Method m2 : MethodUtil.getMethods(this.builderClass)) {
            if (m2.getName().equals(name)) {
                return m2;
            }
        }
        throw new IllegalArgumentException("Method " + name + " could not be found at class " + this.builderClass.getName());
    }

    public Class<?> getTargetClass() {
        return this.buildMethod.getReturnType();
    }
}
