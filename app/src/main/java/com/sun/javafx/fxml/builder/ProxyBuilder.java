package com.sun.javafx.fxml.builder;

import com.sun.javafx.fxml.BeanAdapter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import javafx.beans.NamedArg;
import javafx.util.Builder;
import sun.reflect.misc.ConstructorUtil;
import sun.reflect.misc.MethodUtil;
import sun.reflect.misc.ReflectUtil;

/* loaded from: jfxrt.jar:com/sun/javafx/fxml/builder/ProxyBuilder.class */
public class ProxyBuilder<T> extends AbstractMap<String, Object> implements Builder<T> {
    private Class<?> type;
    private final Map<String, Property> propertiesMap;
    private final Set<Constructor> constructors;
    private Set<String> propertyNames;
    private boolean hasDefaultConstructor;
    private Constructor defaultConstructor;
    private static final String SETTER_PREFIX = "set";
    private static final String GETTER_PREFIX = "get";
    private static final Map<Class<?>, Object> defaultsMap = new HashMap();
    private final Comparator<Constructor> constructorComparator = (o1, o2) -> {
        int len1 = o1.getParameterCount();
        int len2 = o2.getParameterCount();
        int lim = Math.min(len1, len2);
        for (int i2 = 0; i2 < lim; i2++) {
            Class c1 = o1.getParameterTypes()[i2];
            Class c2 = o2.getParameterTypes()[i2];
            if (!c1.equals(c2)) {
                if (c1.equals(Integer.TYPE) && c2.equals(Double.TYPE)) {
                    return -1;
                }
                if (c1.equals(Double.TYPE) && c2.equals(Integer.TYPE)) {
                    return 1;
                }
                return c1.getCanonicalName().compareTo(c2.getCanonicalName());
            }
        }
        return len1 - len2;
    };
    private final Map<String, Object> userValues = new HashMap();
    private final Map<String, Object> containers = new HashMap();
    private final Map<Constructor, Map<String, AnnotationValue>> constructorsMap = new HashMap();

    public ProxyBuilder(Class<?> tp) {
        this.hasDefaultConstructor = false;
        this.type = tp;
        Constructor[] ctors = ConstructorUtil.getConstructors(this.type);
        for (Constructor c2 : ctors) {
            Class<?>[] paramTypes = c2.getParameterTypes();
            Annotation[][] paramAnnotations = c2.getParameterAnnotations();
            if (paramTypes.length == 0) {
                this.hasDefaultConstructor = true;
                this.defaultConstructor = c2;
            } else {
                int i2 = 0;
                boolean properlyAnnotated = true;
                Map<String, AnnotationValue> args = new LinkedHashMap<>();
                int length = paramTypes.length;
                int i3 = 0;
                while (true) {
                    if (i3 >= length) {
                        break;
                    }
                    Class<?> clazz = paramTypes[i3];
                    NamedArg argAnnotation = null;
                    Annotation[] annotationArr = paramAnnotations[i2];
                    int length2 = annotationArr.length;
                    int i4 = 0;
                    while (true) {
                        if (i4 >= length2) {
                            break;
                        }
                        Annotation annotation = annotationArr[i4];
                        if (!(annotation instanceof NamedArg)) {
                            i4++;
                        } else {
                            argAnnotation = (NamedArg) annotation;
                            break;
                        }
                    }
                    if (argAnnotation != null) {
                        AnnotationValue av2 = new AnnotationValue(argAnnotation.value(), argAnnotation.defaultValue(), clazz);
                        args.put(argAnnotation.value(), av2);
                        i2++;
                        i3++;
                    } else {
                        properlyAnnotated = false;
                        break;
                    }
                }
                if (properlyAnnotated) {
                    this.constructorsMap.put(c2, args);
                }
            }
        }
        if (!this.hasDefaultConstructor && this.constructorsMap.isEmpty()) {
            throw new RuntimeException("Cannot create instance of " + this.type.getCanonicalName() + " the constructor is not properly annotated.");
        }
        this.constructors = new TreeSet(this.constructorComparator);
        this.constructors.addAll(this.constructorsMap.keySet());
        this.propertiesMap = scanForSetters();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Object put(String key, Object value) {
        this.userValues.put(key, value);
        return null;
    }

    private Object getTemporaryContainer(String propName) {
        Object o2 = this.containers.get(propName);
        if (o2 == null) {
            o2 = getReadOnlyProperty(propName);
            if (o2 != null) {
                this.containers.put(propName, o2);
            }
        }
        return o2;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/fxml/builder/ProxyBuilder$ArrayListWrapper.class */
    private static class ArrayListWrapper<T> extends ArrayList<T> {
        private ArrayListWrapper() {
        }
    }

    private Object getReadOnlyProperty(String propName) {
        return new ArrayListWrapper();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public int size() {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.AbstractMap, java.util.Map
    public Set<Map.Entry<String, Object>> entrySet() {
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

    /* JADX WARN: Multi-variable type inference failed */
    @Override // javafx.util.Builder
    /* renamed from: build */
    public T build2() throws RuntimeException {
        T tCreateObjectFromConstructor = null;
        for (Map.Entry<String, Object> entry : this.containers.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }
        this.propertyNames = this.userValues.keySet();
        for (Constructor c2 : this.constructors) {
            Set<String> argumentNames = getArgumentNames(c2);
            if (this.propertyNames.equals(argumentNames)) {
                tCreateObjectFromConstructor = createObjectWithExactArguments(c2, argumentNames);
                if (tCreateObjectFromConstructor != null) {
                    return tCreateObjectFromConstructor;
                }
            }
        }
        Set<String> settersArgs = this.propertiesMap.keySet();
        if (settersArgs.containsAll(this.propertyNames) && this.hasDefaultConstructor) {
            tCreateObjectFromConstructor = createObjectFromDefaultConstructor();
            if (tCreateObjectFromConstructor != null) {
                return tCreateObjectFromConstructor;
            }
        }
        Set<String> propertiesToSet = new HashSet<>(this.propertyNames);
        propertiesToSet.retainAll(settersArgs);
        Set<Constructor> chosenConstructors = chooseBestConstructors(settersArgs);
        for (Constructor constructor : chosenConstructors) {
            tCreateObjectFromConstructor = createObjectFromConstructor(constructor, propertiesToSet);
            if (tCreateObjectFromConstructor != null) {
                return tCreateObjectFromConstructor;
            }
        }
        if (tCreateObjectFromConstructor == null) {
            throw new RuntimeException("Cannot create instance of " + this.type.getCanonicalName() + " with given set of properties: " + this.userValues.keySet().toString());
        }
        return tCreateObjectFromConstructor;
    }

    private Set<Constructor> chooseBestConstructors(Set<String> settersArgs) {
        Collection<?> immutablesToSet = new HashSet<>(this.propertyNames);
        immutablesToSet.removeAll(settersArgs);
        Set<String> propertiesToSet = new HashSet<>(this.propertyNames);
        propertiesToSet.retainAll(settersArgs);
        int propertiesToSetCount = Integer.MAX_VALUE;
        int mutablesToSetCount = Integer.MAX_VALUE;
        Set<Constructor> chosenConstructors = new TreeSet<>(this.constructorComparator);
        Set<String> argsNotSet = null;
        for (Constructor c2 : this.constructors) {
            Collection<?> argumentNames = getArgumentNames(c2);
            if (argumentNames.containsAll(immutablesToSet)) {
                Set<String> propertiesToSetInConstructor = new HashSet<>((Collection<? extends String>) argumentNames);
                propertiesToSetInConstructor.removeAll(this.propertyNames);
                Set<String> mutablesNotSet = new HashSet<>(propertiesToSet);
                mutablesNotSet.removeAll(argumentNames);
                int currentPropSize = propertiesToSetInConstructor.size();
                if (propertiesToSetCount == currentPropSize && mutablesToSetCount == mutablesNotSet.size()) {
                    chosenConstructors.add(c2);
                }
                if (propertiesToSetCount > currentPropSize || (propertiesToSetCount == currentPropSize && mutablesToSetCount > mutablesNotSet.size())) {
                    propertiesToSetCount = currentPropSize;
                    mutablesToSetCount = mutablesNotSet.size();
                    chosenConstructors.clear();
                    chosenConstructors.add(c2);
                }
            }
        }
        if (0 != 0 && !argsNotSet.isEmpty()) {
            throw new RuntimeException("Cannot create instance of " + this.type.getCanonicalName() + " no constructor contains all properties specified in FXML.");
        }
        return chosenConstructors;
    }

    private Set<String> getArgumentNames(Constructor c2) {
        Map<String, AnnotationValue> constructorArgsMap = this.constructorsMap.get(c2);
        Set<String> argumentNames = null;
        if (constructorArgsMap != null) {
            argumentNames = constructorArgsMap.keySet();
        }
        return argumentNames;
    }

    private Object createObjectFromDefaultConstructor() throws RuntimeException {
        try {
            Object retObj = createInstance(this.defaultConstructor, new Object[0]);
            for (String propName : this.propertyNames) {
                try {
                    Property property = this.propertiesMap.get(propName);
                    property.invoke(retObj, getUserValue(propName, property.getType()));
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
            return retObj;
        } catch (Exception ex2) {
            throw new RuntimeException(ex2);
        }
    }

    private Object createObjectFromConstructor(Constructor constructor, Set<String> propertiesToSet) {
        Object retObj = null;
        Map<String, AnnotationValue> constructorArgsMap = this.constructorsMap.get(constructor);
        Object[] argsForConstruction = new Object[constructorArgsMap.size()];
        int i2 = 0;
        Set<String> currentPropertiesToSet = new HashSet<>(propertiesToSet);
        for (AnnotationValue value : constructorArgsMap.values()) {
            Object userValue = getUserValue(value.getName(), value.getType());
            if (userValue != null) {
                try {
                    argsForConstruction[i2] = BeanAdapter.coerce(userValue, value.getType());
                } catch (Exception e2) {
                    return null;
                }
            } else if (!value.getDefaultValue().isEmpty()) {
                try {
                    argsForConstruction[i2] = BeanAdapter.coerce(value.getDefaultValue(), value.getType());
                } catch (Exception e3) {
                    return null;
                }
            } else {
                argsForConstruction[i2] = getDefaultValue(value.getType());
            }
            currentPropertiesToSet.remove(value.getName());
            i2++;
        }
        try {
            retObj = createInstance(constructor, argsForConstruction);
        } catch (Exception e4) {
        }
        if (retObj != null) {
            for (String propName : currentPropertiesToSet) {
                try {
                    Property property = this.propertiesMap.get(propName);
                    property.invoke(retObj, getUserValue(propName, property.getType()));
                } catch (Exception e5) {
                    return null;
                }
            }
        }
        return retObj;
    }

    private Object getUserValue(String key, Class<?> type) {
        Object val = this.userValues.get(key);
        if (val == null) {
            return null;
        }
        if (type.isAssignableFrom(val.getClass())) {
            return val;
        }
        if (type.isArray()) {
            try {
                return convertListToArray(val, type);
            } catch (RuntimeException e2) {
            }
        }
        if (ArrayListWrapper.class.equals(val.getClass())) {
            List l2 = (List) val;
            return l2.get(0);
        }
        return val;
    }

    private Object createObjectWithExactArguments(Constructor c2, Set<String> argumentNames) {
        Object retObj = null;
        Object[] argsForConstruction = new Object[argumentNames.size()];
        Map<String, AnnotationValue> constructorArgsMap = this.constructorsMap.get(c2);
        int i2 = 0;
        for (String arg : argumentNames) {
            Class<?> tp = constructorArgsMap.get(arg).getType();
            Object value = getUserValue(arg, tp);
            try {
                int i3 = i2;
                i2++;
                argsForConstruction[i3] = BeanAdapter.coerce(value, tp);
            } catch (Exception e2) {
                return null;
            }
        }
        try {
            retObj = createInstance(c2, argsForConstruction);
        } catch (Exception e3) {
        }
        return retObj;
    }

    private Object createInstance(Constructor c2, Object[] args) throws Exception {
        ReflectUtil.checkPackageAccess(this.type);
        Object retObj = c2.newInstance(args);
        return retObj;
    }

    private Map<String, Property> scanForSetters() throws SecurityException {
        Map<String, Property> strsMap = new HashMap<>();
        Map<String, LinkedList<Method>> methods = getClassMethodCache(this.type);
        for (String methodName : methods.keySet()) {
            if (methodName.startsWith("set")) {
                String propName = methodName.substring("set".length());
                String propName2 = Character.toLowerCase(propName.charAt(0)) + propName.substring(1);
                List<Method> methodsList = methods.get(methodName);
                for (Method m2 : methodsList) {
                    Class<?> retType = m2.getReturnType();
                    Class<?>[] argType = m2.getParameterTypes();
                    if (retType.equals(Void.TYPE) && argType.length == 1) {
                        strsMap.put(propName2, new Setter(m2, argType[0]));
                    }
                }
            }
            if (methodName.startsWith("get")) {
                String propName3 = methodName.substring("set".length());
                String propName4 = Character.toLowerCase(propName3.charAt(0)) + propName3.substring(1);
                List<Method> methodsList2 = methods.get(methodName);
                for (Method m3 : methodsList2) {
                    Class<?> retType2 = m3.getReturnType();
                    Class<?>[] argType2 = m3.getParameterTypes();
                    if (Collection.class.isAssignableFrom(retType2) && argType2.length == 0) {
                        strsMap.put(propName4, new Getter(m3, retType2));
                    }
                }
            }
        }
        return strsMap;
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/fxml/builder/ProxyBuilder$Property.class */
    private static abstract class Property {
        protected final Method method;
        protected final Class<?> type;

        public abstract void invoke(Object obj, Object obj2) throws Exception;

        public Property(Method m2, Class<?> t2) {
            this.method = m2;
            this.type = t2;
        }

        public Class<?> getType() {
            return this.type;
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/fxml/builder/ProxyBuilder$Setter.class */
    private static class Setter extends Property {
        public Setter(Method m2, Class<?> t2) {
            super(m2, t2);
        }

        @Override // com.sun.javafx.fxml.builder.ProxyBuilder.Property
        public void invoke(Object obj, Object argStr) throws Exception {
            Object[] arg = {BeanAdapter.coerce(argStr, this.type)};
            MethodUtil.invoke(this.method, obj, arg);
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/fxml/builder/ProxyBuilder$Getter.class */
    private static class Getter extends Property {
        public Getter(Method m2, Class<?> t2) {
            super(m2, t2);
        }

        @Override // com.sun.javafx.fxml.builder.ProxyBuilder.Property
        public void invoke(Object obj, Object argStr) throws Exception {
            Collection to = (Collection) MethodUtil.invoke(this.method, obj, new Object[0]);
            if (argStr instanceof Collection) {
                Collection from = (Collection) argStr;
                to.addAll(from);
            } else {
                to.add(argStr);
            }
        }
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/fxml/builder/ProxyBuilder$AnnotationValue.class */
    private static class AnnotationValue {
        private final String name;
        private final String defaultValue;
        private final Class<?> type;

        public AnnotationValue(String name, String defaultValue, Class<?> type) {
            this.name = name;
            this.defaultValue = defaultValue;
            this.type = type;
        }

        public String getName() {
            return this.name;
        }

        public String getDefaultValue() {
            return this.defaultValue;
        }

        public Class<?> getType() {
            return this.type;
        }
    }

    private static HashMap<String, LinkedList<Method>> getClassMethodCache(Class<?> type) throws SecurityException {
        HashMap<String, LinkedList<Method>> classMethodCache = new HashMap<>();
        ReflectUtil.checkPackageAccess(type);
        Method[] declaredMethods = type.getMethods();
        for (Method method : declaredMethods) {
            int modifiers = method.getModifiers();
            if (Modifier.isPublic(modifiers) && !Modifier.isStatic(modifiers)) {
                String name = method.getName();
                LinkedList<Method> namedMethods = classMethodCache.get(name);
                if (namedMethods == null) {
                    namedMethods = new LinkedList<>();
                    classMethodCache.put(name, namedMethods);
                }
                namedMethods.add(method);
            }
        }
        return classMethodCache;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static Object[] convertListToArray(Object userValue, Class<?> localType) {
        Class<?> arrayType = localType.getComponentType();
        return ((List) BeanAdapter.coerce(userValue, List.class)).toArray((Object[]) Array.newInstance(arrayType, 0));
    }

    private static Object getDefaultValue(Class clazz) {
        return defaultsMap.get(clazz);
    }

    static {
        defaultsMap.put(Byte.TYPE, (byte) 0);
        defaultsMap.put(Short.TYPE, (short) 0);
        defaultsMap.put(Integer.TYPE, 0);
        defaultsMap.put(Long.TYPE, 0L);
        defaultsMap.put(Integer.TYPE, 0);
        defaultsMap.put(Float.TYPE, Float.valueOf(0.0f));
        defaultsMap.put(Double.TYPE, Double.valueOf(0.0d));
        defaultsMap.put(Character.TYPE, (char) 0);
        defaultsMap.put(Boolean.TYPE, false);
        defaultsMap.put(Object.class, null);
    }
}
