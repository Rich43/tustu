package com.sun.javafx.fxml;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javafx.beans.value.ObservableValue;
import sun.reflect.misc.FieldUtil;
import sun.reflect.misc.MethodUtil;
import sun.reflect.misc.ReflectUtil;

/* loaded from: jfxrt.jar:com/sun/javafx/fxml/BeanAdapter.class */
public class BeanAdapter extends AbstractMap<String, Object> {
    private final Object bean;
    private static final HashMap<Class<?>, MethodCache> globalMethodCache = new HashMap<>();
    private final MethodCache localCache;
    public static final String GET_PREFIX = "get";
    public static final String IS_PREFIX = "is";
    public static final String SET_PREFIX = "set";
    public static final String PROPERTY_SUFFIX = "Property";
    public static final String VALUE_OF_METHOD_NAME = "valueOf";

    /* loaded from: jfxrt.jar:com/sun/javafx/fxml/BeanAdapter$MethodCache.class */
    private static class MethodCache {
        private final Map<String, List<Method>> methods;
        private final MethodCache nextClassCache;

        private MethodCache(Map<String, List<Method>> methods, MethodCache nextClassCache) {
            this.methods = methods;
            this.nextClassCache = nextClassCache;
        }

        /* JADX INFO: Access modifiers changed from: private */
        public Method getMethod(String name, Class<?>... parameterTypes) {
            List<Method> namedMethods = this.methods.get(name);
            if (namedMethods != null) {
                for (int i2 = 0; i2 < namedMethods.size(); i2++) {
                    Method namedMethod = namedMethods.get(i2);
                    if (namedMethod.getName().equals(name) && Arrays.equals(namedMethod.getParameterTypes(), parameterTypes)) {
                        return namedMethod;
                    }
                }
            }
            if (this.nextClassCache != null) {
                return this.nextClassCache.getMethod(name, parameterTypes);
            }
            return null;
        }
    }

    public BeanAdapter(Object bean) {
        this.bean = bean;
        this.localCache = getClassMethodCache(bean.getClass());
    }

    private static MethodCache getClassMethodCache(final Class<?> type) {
        if (type == Object.class) {
            return null;
        }
        synchronized (globalMethodCache) {
            MethodCache classMethodCache = globalMethodCache.get(type);
            if (classMethodCache != null) {
                return classMethodCache;
            }
            Map<String, List<Method>> classMethods = new HashMap<>();
            ReflectUtil.checkPackageAccess(type);
            if (Modifier.isPublic(type.getModifiers())) {
                Method[] declaredMethods = (Method[]) AccessController.doPrivileged(new PrivilegedAction<Method[]>() { // from class: com.sun.javafx.fxml.BeanAdapter.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Method[] run2() {
                        return type.getDeclaredMethods();
                    }
                });
                for (Method method : declaredMethods) {
                    int modifiers = method.getModifiers();
                    if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
                        String name = method.getName();
                        List<Method> namedMethods = classMethods.get(name);
                        if (namedMethods == null) {
                            namedMethods = new ArrayList<>();
                            classMethods.put(name, namedMethods);
                        }
                        namedMethods.add(method);
                    }
                }
            }
            MethodCache cache = new MethodCache(classMethods, getClassMethodCache(type.getSuperclass()));
            globalMethodCache.put(type, cache);
            return cache;
        }
    }

    public Object getBean() {
        return this.bean;
    }

    private Method getGetterMethod(String key) {
        Method getterMethod = this.localCache.getMethod(getMethodName("get", key), new Class[0]);
        if (getterMethod == null) {
            getterMethod = this.localCache.getMethod(getMethodName(IS_PREFIX, key), new Class[0]);
        }
        return getterMethod;
    }

    private Method getSetterMethod(String key) {
        Class<?> type = getType(key);
        if (type != null) {
            return this.localCache.getMethod(getMethodName("set", key), type);
        }
        throw new UnsupportedOperationException("Cannot determine type for property.");
    }

    private static String getMethodName(String prefix, String key) {
        return prefix + Character.toUpperCase(key.charAt(0)) + key.substring(1);
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object get(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return get(key.toString());
    }

    private Object get(String key) {
        Object value;
        Method getterMethod = key.endsWith(PROPERTY_SUFFIX) ? this.localCache.getMethod(key, new Class[0]) : getGetterMethod(key);
        if (getterMethod != null) {
            try {
                value = MethodUtil.invoke(getterMethod, this.bean, (Object[]) null);
            } catch (IllegalAccessException exception) {
                throw new RuntimeException(exception);
            } catch (InvocationTargetException exception2) {
                throw new RuntimeException(exception2);
            }
        } else {
            value = null;
        }
        return value;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object put(String key, Object value) {
        if (key == null) {
            throw new NullPointerException();
        }
        Method setterMethod = getSetterMethod(key);
        if (setterMethod == null) {
            throw new PropertyNotFoundException("Property \"" + key + "\" does not exist or is read-only.");
        }
        try {
            MethodUtil.invoke(setterMethod, this.bean, new Object[]{coerce(value, getType(key))});
            return null;
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        } catch (InvocationTargetException exception2) {
            throw new RuntimeException(exception2);
        }
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean containsKey(Object key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return getType(key.toString()) != null;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<String, Object>> entrySet() {
        throw new UnsupportedOperationException();
    }

    public boolean isReadOnly(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return getSetterMethod(key) == null;
    }

    public <T> ObservableValue<T> getPropertyModel(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
        return (ObservableValue) get(key + PROPERTY_SUFFIX);
    }

    public Class<?> getType(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Method getterMethod = getGetterMethod(key);
        if (getterMethod == null) {
            return null;
        }
        return getterMethod.getReturnType();
    }

    public Type getGenericType(String key) {
        if (key == null) {
            throw new NullPointerException();
        }
        Method getterMethod = getGetterMethod(key);
        if (getterMethod == null) {
            return null;
        }
        return getterMethod.getGenericReturnType();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public boolean equals(Object object) {
        boolean equals = false;
        if (object instanceof BeanAdapter) {
            BeanAdapter beanAdapter = (BeanAdapter) object;
            equals = this.bean == beanAdapter.bean;
        }
        return equals;
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int hashCode() {
        if (this.bean == null) {
            return -1;
        }
        return this.bean.hashCode();
    }

    public static <T> T coerce(Object obj, Class<? extends T> cls) throws NumberFormatException, SecurityException {
        Object objValueOf;
        if (cls == null) {
            throw new NullPointerException();
        }
        if (obj == null) {
            objValueOf = null;
        } else if (cls.isAssignableFrom(obj.getClass())) {
            objValueOf = obj;
        } else if (cls == Boolean.class || cls == Boolean.TYPE) {
            objValueOf = Boolean.valueOf(obj.toString());
        } else if (cls == Character.class || cls == Character.TYPE) {
            objValueOf = Character.valueOf(obj.toString().charAt(0));
        } else if (cls == Byte.class || cls == Byte.TYPE) {
            if (obj instanceof Number) {
                objValueOf = Byte.valueOf(((Number) obj).byteValue());
            } else {
                objValueOf = Byte.valueOf(obj.toString());
            }
        } else if (cls == Short.class || cls == Short.TYPE) {
            if (obj instanceof Number) {
                objValueOf = Short.valueOf(((Number) obj).shortValue());
            } else {
                objValueOf = Short.valueOf(obj.toString());
            }
        } else if (cls == Integer.class || cls == Integer.TYPE) {
            if (obj instanceof Number) {
                objValueOf = Integer.valueOf(((Number) obj).intValue());
            } else {
                objValueOf = Integer.valueOf(obj.toString());
            }
        } else if (cls == Long.class || cls == Long.TYPE) {
            if (obj instanceof Number) {
                objValueOf = Long.valueOf(((Number) obj).longValue());
            } else {
                objValueOf = Long.valueOf(obj.toString());
            }
        } else if (cls == BigInteger.class) {
            if (obj instanceof Number) {
                objValueOf = BigInteger.valueOf(((Number) obj).longValue());
            } else {
                objValueOf = new BigInteger(obj.toString());
            }
        } else if (cls == Float.class || cls == Float.TYPE) {
            if (obj instanceof Number) {
                objValueOf = Float.valueOf(((Number) obj).floatValue());
            } else {
                objValueOf = Float.valueOf(obj.toString());
            }
        } else if (cls == Double.class || cls == Double.TYPE) {
            if (obj instanceof Number) {
                objValueOf = Double.valueOf(((Number) obj).doubleValue());
            } else {
                objValueOf = Double.valueOf(obj.toString());
            }
        } else if (cls == Number.class) {
            String string = obj.toString();
            if (string.contains(".")) {
                objValueOf = Double.valueOf(string);
            } else {
                objValueOf = Long.valueOf(string);
            }
        } else if (cls == BigDecimal.class) {
            if (obj instanceof Number) {
                objValueOf = BigDecimal.valueOf(((Number) obj).doubleValue());
            } else {
                objValueOf = new BigDecimal(obj.toString());
            }
        } else if (cls == Class.class) {
            try {
                String string2 = obj.toString();
                ReflectUtil.checkPackageAccess(string2);
                objValueOf = Class.forName(string2, false, Thread.currentThread().getContextClassLoader());
            } catch (ClassNotFoundException e2) {
                throw new IllegalArgumentException(e2);
            }
        } else {
            Class<?> superclass = obj.getClass();
            Method declaredMethod = null;
            while (declaredMethod == null && superclass != null) {
                try {
                    ReflectUtil.checkPackageAccess(cls);
                    declaredMethod = cls.getDeclaredMethod(VALUE_OF_METHOD_NAME, superclass);
                } catch (NoSuchMethodException e3) {
                }
                if (declaredMethod == null) {
                    superclass = superclass.getSuperclass();
                }
            }
            if (declaredMethod == null) {
                throw new IllegalArgumentException("Unable to coerce " + obj + " to " + ((Object) cls) + ".");
            }
            if (cls.isEnum() && (obj instanceof String) && Character.isLowerCase(((String) obj).charAt(0))) {
                obj = toAllCaps((String) obj);
            }
            try {
                objValueOf = MethodUtil.invoke(declaredMethod, null, new Object[]{obj});
            } catch (IllegalAccessException e4) {
                throw new RuntimeException(e4);
            } catch (SecurityException e5) {
                throw new RuntimeException(e5);
            } catch (InvocationTargetException e6) {
                throw new RuntimeException(e6);
            }
        }
        return (T) objValueOf;
    }

    public static <T> T get(Object obj, Class<?> cls, String str) {
        Object objInvoke = null;
        Method staticGetterMethod = getStaticGetterMethod(cls, str, obj.getClass());
        if (staticGetterMethod != null) {
            try {
                objInvoke = MethodUtil.invoke(staticGetterMethod, null, new Object[]{obj});
            } catch (IllegalAccessException e2) {
                throw new RuntimeException(e2);
            } catch (InvocationTargetException e3) {
                throw new RuntimeException(e3);
            }
        }
        return (T) objInvoke;
    }

    public static void put(Object target, Class<?> sourceType, String key, Object value) throws NumberFormatException, SecurityException {
        Class<?> propertyType;
        Class<?> targetType = target.getClass();
        Method setterMethod = null;
        if (value != null) {
            setterMethod = getStaticSetterMethod(sourceType, key, value.getClass(), targetType);
        }
        if (setterMethod == null && (propertyType = getType(sourceType, key, targetType)) != null) {
            setterMethod = getStaticSetterMethod(sourceType, key, propertyType, targetType);
            value = coerce(value, propertyType);
        }
        if (setterMethod == null) {
            throw new PropertyNotFoundException("Static property \"" + key + "\" does not exist or is read-only.");
        }
        try {
            MethodUtil.invoke(setterMethod, null, new Object[]{target, value});
        } catch (IllegalAccessException exception) {
            throw new RuntimeException(exception);
        } catch (InvocationTargetException exception2) {
            throw new RuntimeException(exception2);
        }
    }

    public static boolean isDefined(Class<?> sourceType, String key, Class<?> targetType) {
        return getStaticGetterMethod(sourceType, key, targetType) != null;
    }

    public static Class<?> getType(Class<?> sourceType, String key, Class<?> targetType) {
        Method getterMethod = getStaticGetterMethod(sourceType, key, targetType);
        if (getterMethod == null) {
            return null;
        }
        return getterMethod.getReturnType();
    }

    public static Type getGenericType(Class<?> sourceType, String key, Class<?> targetType) {
        Method getterMethod = getStaticGetterMethod(sourceType, key, targetType);
        if (getterMethod == null) {
            return null;
        }
        return getterMethod.getGenericReturnType();
    }

    public static Class<?> getListItemType(Type listType) {
        Type itemType = getGenericListItemType(listType);
        if (itemType instanceof ParameterizedType) {
            itemType = ((ParameterizedType) itemType).getRawType();
        }
        return (Class) itemType;
    }

    public static Class<?> getMapValueType(Type mapType) {
        Type valueType = getGenericMapValueType(mapType);
        if (valueType instanceof ParameterizedType) {
            valueType = ((ParameterizedType) valueType).getRawType();
        }
        return (Class) valueType;
    }

    public static Type getGenericListItemType(Type listType) {
        Type itemType = null;
        Type genericSuperclass = listType;
        while (true) {
            Type parentType = genericSuperclass;
            if (parentType == null) {
                break;
            }
            if (parentType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) parentType;
                Class<?> rawType = (Class) parameterizedType.getRawType();
                if (List.class.isAssignableFrom(rawType)) {
                    itemType = parameterizedType.getActualTypeArguments()[0];
                }
            } else {
                Class<?> classType = (Class) parentType;
                Type[] genericInterfaces = classType.getGenericInterfaces();
                int i2 = 0;
                while (true) {
                    if (i2 >= genericInterfaces.length) {
                        break;
                    }
                    Type genericInterface = genericInterfaces[i2];
                    if (genericInterface instanceof ParameterizedType) {
                        ParameterizedType parameterizedType2 = (ParameterizedType) genericInterface;
                        Class<?> interfaceType = (Class) parameterizedType2.getRawType();
                        if (List.class.isAssignableFrom(interfaceType)) {
                            itemType = parameterizedType2.getActualTypeArguments()[0];
                            break;
                        }
                    }
                    i2++;
                }
                if (itemType != null) {
                    break;
                }
                genericSuperclass = classType.getGenericSuperclass();
            }
        }
        if (itemType != null && (itemType instanceof TypeVariable)) {
            itemType = Object.class;
        }
        return itemType;
    }

    public static Type getGenericMapValueType(Type mapType) {
        Type valueType = null;
        Type genericSuperclass = mapType;
        while (true) {
            Type parentType = genericSuperclass;
            if (parentType == null) {
                break;
            }
            if (parentType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) parentType;
                Class<?> rawType = (Class) parameterizedType.getRawType();
                if (Map.class.isAssignableFrom(rawType)) {
                    valueType = parameterizedType.getActualTypeArguments()[1];
                }
            } else {
                Class<?> classType = (Class) parentType;
                Type[] genericInterfaces = classType.getGenericInterfaces();
                int i2 = 0;
                while (true) {
                    if (i2 >= genericInterfaces.length) {
                        break;
                    }
                    Type genericInterface = genericInterfaces[i2];
                    if (genericInterface instanceof ParameterizedType) {
                        ParameterizedType parameterizedType2 = (ParameterizedType) genericInterface;
                        Class<?> interfaceType = (Class) parameterizedType2.getRawType();
                        if (Map.class.isAssignableFrom(interfaceType)) {
                            valueType = parameterizedType2.getActualTypeArguments()[1];
                            break;
                        }
                    }
                    i2++;
                }
                if (valueType != null) {
                    break;
                }
                genericSuperclass = classType.getGenericSuperclass();
            }
        }
        if (valueType != null && (valueType instanceof TypeVariable)) {
            valueType = Object.class;
        }
        return valueType;
    }

    public static Object getConstantValue(Class<?> type, String name) throws IllegalArgumentException {
        if (type == null) {
            throw new IllegalArgumentException();
        }
        if (name == null) {
            throw new IllegalArgumentException();
        }
        try {
            Field field = FieldUtil.getField(type, name);
            int fieldModifiers = field.getModifiers();
            if ((fieldModifiers & 8) == 0 || (fieldModifiers & 16) == 0) {
                throw new IllegalArgumentException("Field is not a constant.");
            }
            try {
                Object value = field.get(null);
                return value;
            } catch (IllegalAccessException exception) {
                throw new IllegalArgumentException(exception);
            }
        } catch (NoSuchFieldException exception2) {
            throw new IllegalArgumentException(exception2);
        }
    }

    private static Method getStaticGetterMethod(Class<?> sourceType, String key, Class<?> targetType) {
        if (sourceType == null) {
            throw new NullPointerException();
        }
        if (key == null) {
            throw new NullPointerException();
        }
        Method method = null;
        if (targetType != null) {
            String key2 = Character.toUpperCase(key.charAt(0)) + key.substring(1);
            String getMethodName = "get" + key2;
            String isMethodName = IS_PREFIX + key2;
            try {
                method = MethodUtil.getMethod(sourceType, getMethodName, new Class[]{targetType});
            } catch (NoSuchMethodException e2) {
            }
            if (method == null) {
                try {
                    method = MethodUtil.getMethod(sourceType, isMethodName, new Class[]{targetType});
                } catch (NoSuchMethodException e3) {
                }
            }
            if (method == null) {
                Class<?>[] interfaces = targetType.getInterfaces();
                for (int i2 = 0; i2 < interfaces.length; i2++) {
                    try {
                        method = MethodUtil.getMethod(sourceType, getMethodName, new Class[]{interfaces[i2]});
                    } catch (NoSuchMethodException e4) {
                    }
                    if (method == null) {
                        try {
                            method = MethodUtil.getMethod(sourceType, isMethodName, new Class[]{interfaces[i2]});
                        } catch (NoSuchMethodException e5) {
                        }
                    }
                    if (method != null) {
                        break;
                    }
                }
            }
            if (method == null) {
                method = getStaticGetterMethod(sourceType, key2, targetType.getSuperclass());
            }
        }
        return method;
    }

    private static Method getStaticSetterMethod(Class<?> sourceType, String key, Class<?> valueType, Class<?> targetType) {
        if (sourceType == null) {
            throw new NullPointerException();
        }
        if (key == null) {
            throw new NullPointerException();
        }
        if (valueType == null) {
            throw new NullPointerException();
        }
        Method method = null;
        if (targetType != null) {
            String key2 = Character.toUpperCase(key.charAt(0)) + key.substring(1);
            String setMethodName = "set" + key2;
            try {
                method = MethodUtil.getMethod(sourceType, setMethodName, new Class[]{targetType, valueType});
            } catch (NoSuchMethodException e2) {
            }
            if (method == null) {
                Class<?>[] interfaces = targetType.getInterfaces();
                for (Class<?> cls : interfaces) {
                    try {
                        method = MethodUtil.getMethod(sourceType, setMethodName, new Class[]{cls, valueType});
                    } catch (NoSuchMethodException e3) {
                    }
                    if (method != null) {
                        break;
                    }
                }
            }
            if (method == null) {
                method = getStaticSetterMethod(sourceType, key2, valueType, targetType.getSuperclass());
            }
        }
        return method;
    }

    private static String toAllCaps(String value) {
        if (value == null) {
            throw new NullPointerException();
        }
        StringBuilder allCapsBuilder = new StringBuilder();
        int n2 = value.length();
        for (int i2 = 0; i2 < n2; i2++) {
            char c2 = value.charAt(i2);
            if (Character.isUpperCase(c2)) {
                allCapsBuilder.append('_');
            }
            allCapsBuilder.append(Character.toUpperCase(c2));
        }
        return allCapsBuilder.toString();
    }
}
