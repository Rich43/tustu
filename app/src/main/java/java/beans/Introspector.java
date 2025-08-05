package java.beans;

import com.sun.beans.TypeResolver;
import com.sun.beans.WeakCache;
import com.sun.beans.finder.ClassFinder;
import com.sun.beans.finder.MethodFinder;
import java.awt.Component;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TooManyListenersException;
import java.util.TreeMap;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:java/beans/Introspector.class */
public class Introspector {
    public static final int USE_ALL_BEANINFO = 1;
    public static final int IGNORE_IMMEDIATE_BEANINFO = 2;
    public static final int IGNORE_ALL_BEANINFO = 3;
    private Class<?> beanClass;
    private BeanInfo explicitBeanInfo;
    private BeanInfo superBeanInfo;
    private BeanInfo[] additionalBeanInfo;
    private String defaultEventName;
    private String defaultPropertyName;
    private Map<String, MethodDescriptor> methods;
    private Map<String, PropertyDescriptor> properties;
    private Map<String, EventSetDescriptor> events;
    static final String ADD_PREFIX = "add";
    static final String REMOVE_PREFIX = "remove";
    static final String GET_PREFIX = "get";
    static final String SET_PREFIX = "set";
    static final String IS_PREFIX = "is";
    private static final WeakCache<Class<?>, Method[]> declaredMethodCache = new WeakCache<>();
    private static Class<EventListener> eventListenerType = EventListener.class;
    private static final EventSetDescriptor[] EMPTY_EVENTSETDESCRIPTORS = new EventSetDescriptor[0];
    private boolean propertyChangeSource = false;
    private int defaultEventIndex = -1;
    private int defaultPropertyIndex = -1;
    private HashMap<String, List<PropertyDescriptor>> pdStore = new HashMap<>();

    public static BeanInfo getBeanInfo(Class<?> cls) throws IntrospectionException {
        BeanInfo beanInfo;
        if (!ReflectUtil.isPackageAccessible(cls)) {
            return new Introspector(cls, null, 1).getBeanInfo();
        }
        ThreadGroupContext context = ThreadGroupContext.getContext();
        synchronized (declaredMethodCache) {
            beanInfo = context.getBeanInfo(cls);
        }
        if (beanInfo == null) {
            beanInfo = new Introspector(cls, null, 1).getBeanInfo();
            synchronized (declaredMethodCache) {
                context.putBeanInfo(cls, beanInfo);
            }
        }
        return beanInfo;
    }

    public static BeanInfo getBeanInfo(Class<?> cls, int i2) throws IntrospectionException {
        return getBeanInfo(cls, null, i2);
    }

    public static BeanInfo getBeanInfo(Class<?> cls, Class<?> cls2) throws IntrospectionException {
        return getBeanInfo(cls, cls2, 1);
    }

    public static BeanInfo getBeanInfo(Class<?> cls, Class<?> cls2, int i2) throws IntrospectionException {
        BeanInfo beanInfo;
        if (cls2 == null && i2 == 1) {
            beanInfo = getBeanInfo(cls);
        } else {
            beanInfo = new Introspector(cls, cls2, i2).getBeanInfo();
        }
        return beanInfo;
    }

    public static String decapitalize(String str) {
        if (str == null || str.length() == 0) {
            return str;
        }
        if (str.length() > 1 && Character.isUpperCase(str.charAt(1)) && Character.isUpperCase(str.charAt(0))) {
            return str;
        }
        char[] charArray = str.toCharArray();
        charArray[0] = Character.toLowerCase(charArray[0]);
        return new String(charArray);
    }

    public static String[] getBeanInfoSearchPath() {
        return ThreadGroupContext.getContext().getBeanInfoFinder().getPackages();
    }

    public static void setBeanInfoSearchPath(String[] strArr) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPropertiesAccess();
        }
        ThreadGroupContext.getContext().getBeanInfoFinder().setPackages(strArr);
    }

    public static void flushCaches() {
        synchronized (declaredMethodCache) {
            ThreadGroupContext.getContext().clearBeanInfoCache();
            declaredMethodCache.clear();
        }
    }

    public static void flushFromCaches(Class<?> cls) {
        if (cls == null) {
            throw new NullPointerException();
        }
        synchronized (declaredMethodCache) {
            ThreadGroupContext.getContext().removeBeanInfo(cls);
            declaredMethodCache.put(cls, null);
        }
    }

    private Introspector(Class<?> cls, Class<?> cls2, int i2) throws IntrospectionException {
        this.beanClass = cls;
        if (cls2 != null) {
            boolean z2 = false;
            Class<? super Object> superclass = cls.getSuperclass();
            while (true) {
                Class<? super Object> cls3 = superclass;
                if (cls3 == null) {
                    break;
                }
                if (cls3 == cls2) {
                    z2 = true;
                }
                superclass = cls3.getSuperclass();
            }
            if (!z2) {
                throw new IntrospectionException(cls2.getName() + " not superclass of " + cls.getName());
            }
        }
        if (i2 == 1) {
            this.explicitBeanInfo = findExplicitBeanInfo(cls);
        }
        Class<? super Object> superclass2 = cls.getSuperclass();
        if (superclass2 != cls2) {
            int i3 = i2;
            this.superBeanInfo = getBeanInfo(superclass2, cls2, i3 == 2 ? 1 : i3);
        }
        if (this.explicitBeanInfo != null) {
            this.additionalBeanInfo = this.explicitBeanInfo.getAdditionalBeanInfo();
        }
        if (this.additionalBeanInfo == null) {
            this.additionalBeanInfo = new BeanInfo[0];
        }
    }

    private BeanInfo getBeanInfo() throws IntrospectionException {
        BeanDescriptor targetBeanDescriptor = getTargetBeanDescriptor();
        MethodDescriptor[] targetMethodInfo = getTargetMethodInfo();
        return new GenericBeanInfo(targetBeanDescriptor, getTargetEventInfo(), getTargetDefaultEventIndex(), getTargetPropertyInfo(), getTargetDefaultPropertyIndex(), targetMethodInfo, this.explicitBeanInfo);
    }

    private static BeanInfo findExplicitBeanInfo(Class<?> cls) {
        return ThreadGroupContext.getContext().getBeanInfoFinder().find(cls);
    }

    private PropertyDescriptor[] getTargetPropertyInfo() {
        PropertyDescriptor[] propertyDescriptors = null;
        if (this.explicitBeanInfo != null) {
            propertyDescriptors = getPropertyDescriptors(this.explicitBeanInfo);
        }
        if (propertyDescriptors == null && this.superBeanInfo != null) {
            addPropertyDescriptors(getPropertyDescriptors(this.superBeanInfo));
        }
        for (int i2 = 0; i2 < this.additionalBeanInfo.length; i2++) {
            addPropertyDescriptors(this.additionalBeanInfo[i2].getPropertyDescriptors());
        }
        if (propertyDescriptors != null) {
            addPropertyDescriptors(propertyDescriptors);
        } else {
            for (Method method : getPublicDeclaredMethods(this.beanClass)) {
                if (method != null && !Modifier.isStatic(method.getModifiers())) {
                    String name = method.getName();
                    Class<?>[] parameterTypes = method.getParameterTypes();
                    Class<?> returnType = method.getReturnType();
                    int length = parameterTypes.length;
                    PropertyDescriptor propertyDescriptor = null;
                    if (name.length() > 3 || name.startsWith("is")) {
                        if (length == 0) {
                            try {
                                if (name.startsWith("get")) {
                                    propertyDescriptor = new PropertyDescriptor(this.beanClass, name.substring(3), method, (Method) null);
                                } else if (returnType == Boolean.TYPE && name.startsWith("is")) {
                                    propertyDescriptor = new PropertyDescriptor(this.beanClass, name.substring(2), method, (Method) null);
                                }
                            } catch (IntrospectionException e2) {
                                propertyDescriptor = null;
                            }
                        } else if (length == 1) {
                            if (Integer.TYPE.equals(parameterTypes[0]) && name.startsWith("get")) {
                                propertyDescriptor = new IndexedPropertyDescriptor(this.beanClass, name.substring(3), (Method) null, (Method) null, method, (Method) null);
                            } else if (Void.TYPE.equals(returnType) && name.startsWith("set")) {
                                propertyDescriptor = new PropertyDescriptor(this.beanClass, name.substring(3), (Method) null, method);
                                if (throwsException(method, PropertyVetoException.class)) {
                                    propertyDescriptor.setConstrained(true);
                                }
                            }
                        } else if (length == 2 && Void.TYPE.equals(returnType) && Integer.TYPE.equals(parameterTypes[0]) && name.startsWith("set")) {
                            propertyDescriptor = new IndexedPropertyDescriptor(this.beanClass, name.substring(3), (Method) null, (Method) null, (Method) null, method);
                            if (throwsException(method, PropertyVetoException.class)) {
                                propertyDescriptor.setConstrained(true);
                            }
                        }
                        if (propertyDescriptor != null) {
                            if (this.propertyChangeSource) {
                                propertyDescriptor.setBound(true);
                            }
                            addPropertyDescriptor(propertyDescriptor);
                        }
                    }
                }
            }
        }
        processPropertyDescriptors();
        PropertyDescriptor[] propertyDescriptorArr = (PropertyDescriptor[]) this.properties.values().toArray(new PropertyDescriptor[this.properties.size()]);
        if (this.defaultPropertyName != null) {
            for (int i3 = 0; i3 < propertyDescriptorArr.length; i3++) {
                if (this.defaultPropertyName.equals(propertyDescriptorArr[i3].getName())) {
                    this.defaultPropertyIndex = i3;
                }
            }
        }
        return propertyDescriptorArr;
    }

    private void addPropertyDescriptor(PropertyDescriptor propertyDescriptor) {
        String name = propertyDescriptor.getName();
        List<PropertyDescriptor> arrayList = this.pdStore.get(name);
        if (arrayList == null) {
            arrayList = new ArrayList();
            this.pdStore.put(name, arrayList);
        }
        if (this.beanClass != propertyDescriptor.getClass0()) {
            Method readMethod = propertyDescriptor.getReadMethod();
            Method writeMethod = propertyDescriptor.getWriteMethod();
            boolean z2 = true;
            if (readMethod != null) {
                z2 = 1 != 0 && (readMethod.getGenericReturnType() instanceof Class);
            }
            if (writeMethod != null) {
                z2 = z2 && (writeMethod.getGenericParameterTypes()[0] instanceof Class);
            }
            if (propertyDescriptor instanceof IndexedPropertyDescriptor) {
                IndexedPropertyDescriptor indexedPropertyDescriptor = (IndexedPropertyDescriptor) propertyDescriptor;
                Method indexedReadMethod = indexedPropertyDescriptor.getIndexedReadMethod();
                Method indexedWriteMethod = indexedPropertyDescriptor.getIndexedWriteMethod();
                if (indexedReadMethod != null) {
                    z2 = z2 && (indexedReadMethod.getGenericReturnType() instanceof Class);
                }
                if (indexedWriteMethod != null) {
                    z2 = z2 && (indexedWriteMethod.getGenericParameterTypes()[1] instanceof Class);
                }
                if (!z2) {
                    propertyDescriptor = new IndexedPropertyDescriptor(indexedPropertyDescriptor);
                    propertyDescriptor.updateGenericsFor(this.beanClass);
                }
            } else if (!z2) {
                propertyDescriptor = new PropertyDescriptor(propertyDescriptor);
                propertyDescriptor.updateGenericsFor(this.beanClass);
            }
        }
        arrayList.add(propertyDescriptor);
    }

    private void addPropertyDescriptors(PropertyDescriptor[] propertyDescriptorArr) {
        if (propertyDescriptorArr != null) {
            for (PropertyDescriptor propertyDescriptor : propertyDescriptorArr) {
                addPropertyDescriptor(propertyDescriptor);
            }
        }
    }

    private PropertyDescriptor[] getPropertyDescriptors(BeanInfo beanInfo) {
        PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
        int defaultPropertyIndex = beanInfo.getDefaultPropertyIndex();
        if (0 <= defaultPropertyIndex && defaultPropertyIndex < propertyDescriptors.length) {
            this.defaultPropertyName = propertyDescriptors[defaultPropertyIndex].getName();
        }
        return propertyDescriptors;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v103, types: [java.beans.PropertyDescriptor] */
    /* JADX WARN: Type inference failed for: r0v105, types: [java.beans.PropertyDescriptor] */
    /* JADX WARN: Type inference failed for: r0v131, types: [java.beans.PropertyDescriptor] */
    /* JADX WARN: Type inference failed for: r0v139, types: [java.beans.PropertyDescriptor] */
    /* JADX WARN: Type inference failed for: r0v145, types: [java.beans.PropertyDescriptor] */
    /* JADX WARN: Type inference failed for: r0v162, types: [java.beans.PropertyDescriptor] */
    /* JADX WARN: Type inference failed for: r0v177, types: [java.beans.PropertyDescriptor] */
    /* JADX WARN: Type inference failed for: r0v75, types: [java.beans.PropertyDescriptor] */
    /* JADX WARN: Type inference failed for: r0v82, types: [java.beans.PropertyDescriptor] */
    private void processPropertyDescriptors() {
        IndexedPropertyDescriptor indexedPropertyDescriptorMergePropertyDescriptor;
        IndexedPropertyDescriptor indexedPropertyDescriptorMergePropertyDescriptor2;
        if (this.properties == null) {
            this.properties = new TreeMap();
        }
        for (List<PropertyDescriptor> list : this.pdStore.values()) {
            IndexedPropertyDescriptor indexedPropertyDescriptorMergePropertyWithIndexedProperty = null;
            IndexedPropertyDescriptor indexedPropertyDescriptorMergePropertyWithIndexedProperty2 = null;
            IndexedPropertyDescriptor indexedPropertyDescriptor = null;
            IndexedPropertyDescriptor indexedPropertyDescriptor2 = null;
            for (int i2 = 0; i2 < list.size(); i2++) {
                PropertyDescriptor propertyDescriptor = list.get(i2);
                if (propertyDescriptor instanceof IndexedPropertyDescriptor) {
                    IndexedPropertyDescriptor indexedPropertyDescriptor3 = (IndexedPropertyDescriptor) propertyDescriptor;
                    if (indexedPropertyDescriptor3.getIndexedReadMethod() != null) {
                        if (indexedPropertyDescriptor != null) {
                            indexedPropertyDescriptor = new IndexedPropertyDescriptor(indexedPropertyDescriptor, indexedPropertyDescriptor3);
                        } else {
                            indexedPropertyDescriptor = indexedPropertyDescriptor3;
                        }
                    }
                } else if (propertyDescriptor.getReadMethod() != null) {
                    String name = propertyDescriptor.getReadMethod().getName();
                    if (indexedPropertyDescriptorMergePropertyWithIndexedProperty != null) {
                        String name2 = indexedPropertyDescriptorMergePropertyWithIndexedProperty.getReadMethod().getName();
                        if (name2.equals(name) || !name2.startsWith("is")) {
                            indexedPropertyDescriptorMergePropertyWithIndexedProperty = new PropertyDescriptor(indexedPropertyDescriptorMergePropertyWithIndexedProperty, propertyDescriptor);
                        }
                    } else {
                        indexedPropertyDescriptorMergePropertyWithIndexedProperty = propertyDescriptor;
                    }
                }
            }
            for (int i3 = 0; i3 < list.size(); i3++) {
                PropertyDescriptor propertyDescriptor2 = list.get(i3);
                if (propertyDescriptor2 instanceof IndexedPropertyDescriptor) {
                    IndexedPropertyDescriptor indexedPropertyDescriptor4 = (IndexedPropertyDescriptor) propertyDescriptor2;
                    if (indexedPropertyDescriptor4.getIndexedWriteMethod() != null) {
                        if (indexedPropertyDescriptor != null) {
                            if (isAssignable(indexedPropertyDescriptor.getIndexedPropertyType(), indexedPropertyDescriptor4.getIndexedPropertyType())) {
                                if (indexedPropertyDescriptor2 != null) {
                                    indexedPropertyDescriptor2 = new IndexedPropertyDescriptor(indexedPropertyDescriptor2, indexedPropertyDescriptor4);
                                } else {
                                    indexedPropertyDescriptor2 = indexedPropertyDescriptor4;
                                }
                            }
                        } else if (indexedPropertyDescriptor2 != null) {
                            indexedPropertyDescriptor2 = new IndexedPropertyDescriptor(indexedPropertyDescriptor2, indexedPropertyDescriptor4);
                        } else {
                            indexedPropertyDescriptor2 = indexedPropertyDescriptor4;
                        }
                    }
                } else if (propertyDescriptor2.getWriteMethod() != null) {
                    if (indexedPropertyDescriptorMergePropertyWithIndexedProperty != null) {
                        if (isAssignable(indexedPropertyDescriptorMergePropertyWithIndexedProperty.getPropertyType(), propertyDescriptor2.getPropertyType())) {
                            if (indexedPropertyDescriptorMergePropertyWithIndexedProperty2 != null) {
                                indexedPropertyDescriptorMergePropertyWithIndexedProperty2 = new PropertyDescriptor(indexedPropertyDescriptorMergePropertyWithIndexedProperty2, propertyDescriptor2);
                            } else {
                                indexedPropertyDescriptorMergePropertyWithIndexedProperty2 = propertyDescriptor2;
                            }
                        }
                    } else if (indexedPropertyDescriptorMergePropertyWithIndexedProperty2 != null) {
                        indexedPropertyDescriptorMergePropertyWithIndexedProperty2 = new PropertyDescriptor(indexedPropertyDescriptorMergePropertyWithIndexedProperty2, propertyDescriptor2);
                    } else {
                        indexedPropertyDescriptorMergePropertyWithIndexedProperty2 = propertyDescriptor2;
                    }
                }
            }
            IndexedPropertyDescriptor indexedPropertyDescriptorMergePropertyDescriptor3 = null;
            if (indexedPropertyDescriptor != null && indexedPropertyDescriptor2 != null) {
                if (indexedPropertyDescriptorMergePropertyWithIndexedProperty == indexedPropertyDescriptorMergePropertyWithIndexedProperty2 || indexedPropertyDescriptorMergePropertyWithIndexedProperty == null) {
                    indexedPropertyDescriptorMergePropertyDescriptor = indexedPropertyDescriptorMergePropertyWithIndexedProperty2;
                } else if (indexedPropertyDescriptorMergePropertyWithIndexedProperty2 == null) {
                    indexedPropertyDescriptorMergePropertyDescriptor = indexedPropertyDescriptorMergePropertyWithIndexedProperty;
                } else if (indexedPropertyDescriptorMergePropertyWithIndexedProperty2 instanceof IndexedPropertyDescriptor) {
                    indexedPropertyDescriptorMergePropertyDescriptor = mergePropertyWithIndexedProperty(indexedPropertyDescriptorMergePropertyWithIndexedProperty, indexedPropertyDescriptorMergePropertyWithIndexedProperty2);
                } else if (indexedPropertyDescriptorMergePropertyWithIndexedProperty instanceof IndexedPropertyDescriptor) {
                    indexedPropertyDescriptorMergePropertyDescriptor = mergePropertyWithIndexedProperty(indexedPropertyDescriptorMergePropertyWithIndexedProperty2, indexedPropertyDescriptorMergePropertyWithIndexedProperty);
                } else {
                    indexedPropertyDescriptorMergePropertyDescriptor = mergePropertyDescriptor((PropertyDescriptor) indexedPropertyDescriptorMergePropertyWithIndexedProperty, (PropertyDescriptor) indexedPropertyDescriptorMergePropertyWithIndexedProperty2);
                }
                if (indexedPropertyDescriptor == indexedPropertyDescriptor2) {
                    indexedPropertyDescriptorMergePropertyDescriptor2 = indexedPropertyDescriptor;
                } else {
                    indexedPropertyDescriptorMergePropertyDescriptor2 = mergePropertyDescriptor(indexedPropertyDescriptor, indexedPropertyDescriptor2);
                }
                if (indexedPropertyDescriptorMergePropertyDescriptor == null) {
                    indexedPropertyDescriptorMergePropertyDescriptor3 = indexedPropertyDescriptorMergePropertyDescriptor2;
                } else {
                    Class<?> propertyType = indexedPropertyDescriptorMergePropertyDescriptor.getPropertyType();
                    Class<?> indexedPropertyType = indexedPropertyDescriptorMergePropertyDescriptor2.getIndexedPropertyType();
                    if (propertyType.isArray() && propertyType.getComponentType() == indexedPropertyType) {
                        indexedPropertyDescriptorMergePropertyDescriptor3 = indexedPropertyDescriptorMergePropertyDescriptor.getClass0().isAssignableFrom(indexedPropertyDescriptorMergePropertyDescriptor2.getClass0()) ? new IndexedPropertyDescriptor(indexedPropertyDescriptorMergePropertyDescriptor, indexedPropertyDescriptorMergePropertyDescriptor2) : new IndexedPropertyDescriptor(indexedPropertyDescriptorMergePropertyDescriptor2, indexedPropertyDescriptorMergePropertyDescriptor);
                    } else if (indexedPropertyDescriptorMergePropertyDescriptor.getClass0().isAssignableFrom(indexedPropertyDescriptorMergePropertyDescriptor2.getClass0())) {
                        indexedPropertyDescriptorMergePropertyDescriptor3 = indexedPropertyDescriptorMergePropertyDescriptor.getClass0().isAssignableFrom(indexedPropertyDescriptorMergePropertyDescriptor2.getClass0()) ? new PropertyDescriptor(indexedPropertyDescriptorMergePropertyDescriptor, indexedPropertyDescriptorMergePropertyDescriptor2) : new PropertyDescriptor(indexedPropertyDescriptorMergePropertyDescriptor2, indexedPropertyDescriptorMergePropertyDescriptor);
                    } else {
                        indexedPropertyDescriptorMergePropertyDescriptor3 = indexedPropertyDescriptorMergePropertyDescriptor2;
                    }
                }
            } else if (indexedPropertyDescriptorMergePropertyWithIndexedProperty != null && indexedPropertyDescriptorMergePropertyWithIndexedProperty2 != null) {
                if (indexedPropertyDescriptor != null) {
                    indexedPropertyDescriptorMergePropertyWithIndexedProperty = mergePropertyWithIndexedProperty(indexedPropertyDescriptorMergePropertyWithIndexedProperty, indexedPropertyDescriptor);
                }
                if (indexedPropertyDescriptor2 != null) {
                    indexedPropertyDescriptorMergePropertyWithIndexedProperty2 = mergePropertyWithIndexedProperty(indexedPropertyDescriptorMergePropertyWithIndexedProperty2, indexedPropertyDescriptor2);
                }
                if (indexedPropertyDescriptorMergePropertyWithIndexedProperty == indexedPropertyDescriptorMergePropertyWithIndexedProperty2) {
                    indexedPropertyDescriptorMergePropertyDescriptor3 = indexedPropertyDescriptorMergePropertyWithIndexedProperty;
                } else if (indexedPropertyDescriptorMergePropertyWithIndexedProperty2 instanceof IndexedPropertyDescriptor) {
                    indexedPropertyDescriptorMergePropertyDescriptor3 = mergePropertyWithIndexedProperty(indexedPropertyDescriptorMergePropertyWithIndexedProperty, indexedPropertyDescriptorMergePropertyWithIndexedProperty2);
                } else if (indexedPropertyDescriptorMergePropertyWithIndexedProperty instanceof IndexedPropertyDescriptor) {
                    indexedPropertyDescriptorMergePropertyDescriptor3 = mergePropertyWithIndexedProperty(indexedPropertyDescriptorMergePropertyWithIndexedProperty2, indexedPropertyDescriptorMergePropertyWithIndexedProperty);
                } else {
                    indexedPropertyDescriptorMergePropertyDescriptor3 = mergePropertyDescriptor((PropertyDescriptor) indexedPropertyDescriptorMergePropertyWithIndexedProperty, (PropertyDescriptor) indexedPropertyDescriptorMergePropertyWithIndexedProperty2);
                }
            } else if (indexedPropertyDescriptor2 != null) {
                indexedPropertyDescriptorMergePropertyDescriptor3 = indexedPropertyDescriptor2;
                if (indexedPropertyDescriptorMergePropertyWithIndexedProperty2 != null) {
                    indexedPropertyDescriptorMergePropertyDescriptor3 = mergePropertyDescriptor(indexedPropertyDescriptor2, (PropertyDescriptor) indexedPropertyDescriptorMergePropertyWithIndexedProperty2);
                }
                if (indexedPropertyDescriptorMergePropertyWithIndexedProperty != null) {
                    indexedPropertyDescriptorMergePropertyDescriptor3 = mergePropertyDescriptor(indexedPropertyDescriptor2, (PropertyDescriptor) indexedPropertyDescriptorMergePropertyWithIndexedProperty);
                }
            } else if (indexedPropertyDescriptor != null) {
                indexedPropertyDescriptorMergePropertyDescriptor3 = indexedPropertyDescriptor;
                if (indexedPropertyDescriptorMergePropertyWithIndexedProperty != null) {
                    indexedPropertyDescriptorMergePropertyDescriptor3 = mergePropertyDescriptor(indexedPropertyDescriptor, (PropertyDescriptor) indexedPropertyDescriptorMergePropertyWithIndexedProperty);
                }
                if (indexedPropertyDescriptorMergePropertyWithIndexedProperty2 != null) {
                    indexedPropertyDescriptorMergePropertyDescriptor3 = mergePropertyDescriptor(indexedPropertyDescriptor, (PropertyDescriptor) indexedPropertyDescriptorMergePropertyWithIndexedProperty2);
                }
            } else if (indexedPropertyDescriptorMergePropertyWithIndexedProperty2 != null) {
                indexedPropertyDescriptorMergePropertyDescriptor3 = indexedPropertyDescriptorMergePropertyWithIndexedProperty2;
            } else if (indexedPropertyDescriptorMergePropertyWithIndexedProperty != null) {
                indexedPropertyDescriptorMergePropertyDescriptor3 = indexedPropertyDescriptorMergePropertyWithIndexedProperty;
            }
            if (indexedPropertyDescriptorMergePropertyDescriptor3 instanceof IndexedPropertyDescriptor) {
                IndexedPropertyDescriptor indexedPropertyDescriptor5 = indexedPropertyDescriptorMergePropertyDescriptor3;
                if (indexedPropertyDescriptor5.getIndexedReadMethod() == null && indexedPropertyDescriptor5.getIndexedWriteMethod() == null) {
                    indexedPropertyDescriptorMergePropertyDescriptor3 = new PropertyDescriptor(indexedPropertyDescriptor5);
                }
            }
            if (indexedPropertyDescriptorMergePropertyDescriptor3 == null && list.size() > 0) {
                indexedPropertyDescriptorMergePropertyDescriptor3 = list.get(0);
            }
            if (indexedPropertyDescriptorMergePropertyDescriptor3 != null) {
                this.properties.put(indexedPropertyDescriptorMergePropertyDescriptor3.getName(), indexedPropertyDescriptorMergePropertyDescriptor3);
            }
        }
    }

    private static boolean isAssignable(Class<?> cls, Class<?> cls2) {
        return (cls == null || cls2 == null) ? cls == cls2 : cls.isAssignableFrom(cls2);
    }

    private PropertyDescriptor mergePropertyWithIndexedProperty(PropertyDescriptor propertyDescriptor, IndexedPropertyDescriptor indexedPropertyDescriptor) {
        Class<?> propertyType = propertyDescriptor.getPropertyType();
        if (propertyType.isArray() && propertyType.getComponentType() == indexedPropertyDescriptor.getIndexedPropertyType()) {
            return propertyDescriptor.getClass0().isAssignableFrom(indexedPropertyDescriptor.getClass0()) ? new IndexedPropertyDescriptor(propertyDescriptor, indexedPropertyDescriptor) : new IndexedPropertyDescriptor(indexedPropertyDescriptor, propertyDescriptor);
        }
        return propertyDescriptor;
    }

    private PropertyDescriptor mergePropertyDescriptor(IndexedPropertyDescriptor indexedPropertyDescriptor, PropertyDescriptor propertyDescriptor) {
        PropertyDescriptor propertyDescriptor2;
        Method methodFindMethod;
        Class<?> propertyType = propertyDescriptor.getPropertyType();
        Class<?> indexedPropertyType = indexedPropertyDescriptor.getIndexedPropertyType();
        if (propertyType.isArray() && propertyType.getComponentType() == indexedPropertyType) {
            propertyDescriptor2 = propertyDescriptor.getClass0().isAssignableFrom(indexedPropertyDescriptor.getClass0()) ? new IndexedPropertyDescriptor(propertyDescriptor, indexedPropertyDescriptor) : new IndexedPropertyDescriptor(indexedPropertyDescriptor, propertyDescriptor);
        } else if (indexedPropertyDescriptor.getReadMethod() == null && indexedPropertyDescriptor.getWriteMethod() == null) {
            propertyDescriptor2 = propertyDescriptor.getClass0().isAssignableFrom(indexedPropertyDescriptor.getClass0()) ? new PropertyDescriptor(propertyDescriptor, indexedPropertyDescriptor) : new PropertyDescriptor(indexedPropertyDescriptor, propertyDescriptor);
        } else if (propertyDescriptor.getClass0().isAssignableFrom(indexedPropertyDescriptor.getClass0())) {
            propertyDescriptor2 = indexedPropertyDescriptor;
        } else {
            propertyDescriptor2 = propertyDescriptor;
            Method writeMethod = propertyDescriptor2.getWriteMethod();
            Method readMethod = propertyDescriptor2.getReadMethod();
            if (readMethod == null && writeMethod != null) {
                readMethod = findMethod(propertyDescriptor2.getClass0(), "get" + NameGenerator.capitalize(propertyDescriptor2.getName()), 0);
                if (readMethod != null) {
                    try {
                        propertyDescriptor2.setReadMethod(readMethod);
                    } catch (IntrospectionException e2) {
                    }
                }
            }
            if (writeMethod == null && readMethod != null && (methodFindMethod = findMethod(propertyDescriptor2.getClass0(), "set" + NameGenerator.capitalize(propertyDescriptor2.getName()), 1, new Class[]{FeatureDescriptor.getReturnType(propertyDescriptor2.getClass0(), readMethod)})) != null) {
                try {
                    propertyDescriptor2.setWriteMethod(methodFindMethod);
                } catch (IntrospectionException e3) {
                }
            }
        }
        return propertyDescriptor2;
    }

    private PropertyDescriptor mergePropertyDescriptor(PropertyDescriptor propertyDescriptor, PropertyDescriptor propertyDescriptor2) {
        if (propertyDescriptor.getClass0().isAssignableFrom(propertyDescriptor2.getClass0())) {
            return new PropertyDescriptor(propertyDescriptor, propertyDescriptor2);
        }
        return new PropertyDescriptor(propertyDescriptor2, propertyDescriptor);
    }

    private IndexedPropertyDescriptor mergePropertyDescriptor(IndexedPropertyDescriptor indexedPropertyDescriptor, IndexedPropertyDescriptor indexedPropertyDescriptor2) {
        if (indexedPropertyDescriptor.getClass0().isAssignableFrom(indexedPropertyDescriptor2.getClass0())) {
            return new IndexedPropertyDescriptor(indexedPropertyDescriptor, indexedPropertyDescriptor2);
        }
        return new IndexedPropertyDescriptor(indexedPropertyDescriptor2, indexedPropertyDescriptor);
    }

    private EventSetDescriptor[] getTargetEventInfo() throws IntrospectionException {
        EventSetDescriptor[] eventSetDescriptorArr;
        if (this.events == null) {
            this.events = new HashMap();
        }
        EventSetDescriptor[] eventSetDescriptors = null;
        if (this.explicitBeanInfo != null) {
            eventSetDescriptors = this.explicitBeanInfo.getEventSetDescriptors();
            int defaultEventIndex = this.explicitBeanInfo.getDefaultEventIndex();
            if (defaultEventIndex >= 0 && defaultEventIndex < eventSetDescriptors.length) {
                this.defaultEventName = eventSetDescriptors[defaultEventIndex].getName();
            }
        }
        if (eventSetDescriptors == null && this.superBeanInfo != null) {
            EventSetDescriptor[] eventSetDescriptors2 = this.superBeanInfo.getEventSetDescriptors();
            for (EventSetDescriptor eventSetDescriptor : eventSetDescriptors2) {
                addEvent(eventSetDescriptor);
            }
            int defaultEventIndex2 = this.superBeanInfo.getDefaultEventIndex();
            if (defaultEventIndex2 >= 0 && defaultEventIndex2 < eventSetDescriptors2.length) {
                this.defaultEventName = eventSetDescriptors2[defaultEventIndex2].getName();
            }
        }
        for (int i2 = 0; i2 < this.additionalBeanInfo.length; i2++) {
            EventSetDescriptor[] eventSetDescriptors3 = this.additionalBeanInfo[i2].getEventSetDescriptors();
            if (eventSetDescriptors3 != null) {
                for (EventSetDescriptor eventSetDescriptor2 : eventSetDescriptors3) {
                    addEvent(eventSetDescriptor2);
                }
            }
        }
        if (eventSetDescriptors != null) {
            for (EventSetDescriptor eventSetDescriptor3 : eventSetDescriptors) {
                addEvent(eventSetDescriptor3);
            }
        } else {
            HashMap map = null;
            HashMap map2 = null;
            HashMap map3 = null;
            for (Method method : getPublicDeclaredMethods(this.beanClass)) {
                if (method != null && !Modifier.isStatic(method.getModifiers())) {
                    String name = method.getName();
                    if (name.startsWith(ADD_PREFIX) || name.startsWith(REMOVE_PREFIX) || name.startsWith("get")) {
                        if (name.startsWith(ADD_PREFIX)) {
                            if (method.getReturnType() == Void.TYPE) {
                                Type[] genericParameterTypes = method.getGenericParameterTypes();
                                if (genericParameterTypes.length == 1) {
                                    Class<?> clsErase = TypeResolver.erase(TypeResolver.resolveInClass(this.beanClass, genericParameterTypes[0]));
                                    if (isSubclass(clsErase, eventListenerType)) {
                                        String strSubstring = name.substring(3);
                                        if (strSubstring.length() > 0 && clsErase.getName().endsWith(strSubstring)) {
                                            if (map == null) {
                                                map = new HashMap();
                                            }
                                            map.put(strSubstring, method);
                                        }
                                    }
                                }
                            }
                        } else if (name.startsWith(REMOVE_PREFIX)) {
                            if (method.getReturnType() == Void.TYPE) {
                                Type[] genericParameterTypes2 = method.getGenericParameterTypes();
                                if (genericParameterTypes2.length == 1) {
                                    Class<?> clsErase2 = TypeResolver.erase(TypeResolver.resolveInClass(this.beanClass, genericParameterTypes2[0]));
                                    if (isSubclass(clsErase2, eventListenerType)) {
                                        String strSubstring2 = name.substring(6);
                                        if (strSubstring2.length() > 0 && clsErase2.getName().endsWith(strSubstring2)) {
                                            if (map2 == null) {
                                                map2 = new HashMap();
                                            }
                                            map2.put(strSubstring2, method);
                                        }
                                    }
                                }
                            }
                        } else if (name.startsWith("get") && method.getParameterTypes().length == 0) {
                            Class<?> returnType = FeatureDescriptor.getReturnType(this.beanClass, method);
                            if (returnType.isArray()) {
                                Class<?> componentType = returnType.getComponentType();
                                if (isSubclass(componentType, eventListenerType)) {
                                    String strSubstring3 = name.substring(3, name.length() - 1);
                                    if (strSubstring3.length() > 0 && componentType.getName().endsWith(strSubstring3)) {
                                        if (map3 == null) {
                                            map3 = new HashMap();
                                        }
                                        map3.put(strSubstring3, method);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            if (map != null && map2 != null) {
                for (String str : map.keySet()) {
                    if (map2.get(str) != null && str.endsWith("Listener")) {
                        String strDecapitalize = decapitalize(str.substring(0, str.length() - 8));
                        Method method2 = (Method) map.get(str);
                        Method method3 = (Method) map2.get(str);
                        Method method4 = null;
                        if (map3 != null) {
                            method4 = (Method) map3.get(str);
                        }
                        Class<?> cls = FeatureDescriptor.getParameterTypes(this.beanClass, method2)[0];
                        Method[] publicDeclaredMethods = getPublicDeclaredMethods(cls);
                        ArrayList arrayList = new ArrayList(publicDeclaredMethods.length);
                        for (int i3 = 0; i3 < publicDeclaredMethods.length; i3++) {
                            if (publicDeclaredMethods[i3] != null && isEventHandler(publicDeclaredMethods[i3])) {
                                arrayList.add(publicDeclaredMethods[i3]);
                            }
                        }
                        EventSetDescriptor eventSetDescriptor4 = new EventSetDescriptor(strDecapitalize, cls, (Method[]) arrayList.toArray(new Method[arrayList.size()]), method2, method3, method4);
                        if (throwsException(method2, TooManyListenersException.class)) {
                            eventSetDescriptor4.setUnicast(true);
                        }
                        addEvent(eventSetDescriptor4);
                    }
                }
            }
        }
        if (this.events.size() == 0) {
            eventSetDescriptorArr = EMPTY_EVENTSETDESCRIPTORS;
        } else {
            eventSetDescriptorArr = (EventSetDescriptor[]) this.events.values().toArray(new EventSetDescriptor[this.events.size()]);
            if (this.defaultEventName != null) {
                for (int i4 = 0; i4 < eventSetDescriptorArr.length; i4++) {
                    if (this.defaultEventName.equals(eventSetDescriptorArr[i4].getName())) {
                        this.defaultEventIndex = i4;
                    }
                }
            }
        }
        return eventSetDescriptorArr;
    }

    private void addEvent(EventSetDescriptor eventSetDescriptor) {
        String name = eventSetDescriptor.getName();
        if (eventSetDescriptor.getName().equals("propertyChange")) {
            this.propertyChangeSource = true;
        }
        EventSetDescriptor eventSetDescriptor2 = this.events.get(name);
        if (eventSetDescriptor2 == null) {
            this.events.put(name, eventSetDescriptor);
        } else {
            this.events.put(name, new EventSetDescriptor(eventSetDescriptor2, eventSetDescriptor));
        }
    }

    private MethodDescriptor[] getTargetMethodInfo() {
        if (this.methods == null) {
            this.methods = new HashMap(100);
        }
        MethodDescriptor[] methodDescriptors = null;
        if (this.explicitBeanInfo != null) {
            methodDescriptors = this.explicitBeanInfo.getMethodDescriptors();
        }
        if (methodDescriptors == null && this.superBeanInfo != null) {
            for (MethodDescriptor methodDescriptor : this.superBeanInfo.getMethodDescriptors()) {
                addMethod(methodDescriptor);
            }
        }
        for (int i2 = 0; i2 < this.additionalBeanInfo.length; i2++) {
            MethodDescriptor[] methodDescriptors2 = this.additionalBeanInfo[i2].getMethodDescriptors();
            if (methodDescriptors2 != null) {
                for (MethodDescriptor methodDescriptor2 : methodDescriptors2) {
                    addMethod(methodDescriptor2);
                }
            }
        }
        if (methodDescriptors != null) {
            for (MethodDescriptor methodDescriptor3 : methodDescriptors) {
                addMethod(methodDescriptor3);
            }
        } else {
            for (Method method : getPublicDeclaredMethods(this.beanClass)) {
                if (method != null) {
                    addMethod(new MethodDescriptor(method));
                }
            }
        }
        return (MethodDescriptor[]) this.methods.values().toArray(new MethodDescriptor[this.methods.size()]);
    }

    private void addMethod(MethodDescriptor methodDescriptor) {
        String name = methodDescriptor.getName();
        MethodDescriptor methodDescriptor2 = this.methods.get(name);
        if (methodDescriptor2 == null) {
            this.methods.put(name, methodDescriptor);
            return;
        }
        String[] paramNames = methodDescriptor.getParamNames();
        String[] paramNames2 = methodDescriptor2.getParamNames();
        boolean z2 = false;
        if (paramNames.length == paramNames2.length) {
            z2 = true;
            int i2 = 0;
            while (true) {
                if (i2 >= paramNames.length) {
                    break;
                }
                if (paramNames[i2] == paramNames2[i2]) {
                    i2++;
                } else {
                    z2 = false;
                    break;
                }
            }
        }
        if (z2) {
            this.methods.put(name, new MethodDescriptor(methodDescriptor2, methodDescriptor));
            return;
        }
        String strMakeQualifiedMethodName = makeQualifiedMethodName(name, paramNames);
        MethodDescriptor methodDescriptor3 = this.methods.get(strMakeQualifiedMethodName);
        if (methodDescriptor3 == null) {
            this.methods.put(strMakeQualifiedMethodName, methodDescriptor);
        } else {
            this.methods.put(strMakeQualifiedMethodName, new MethodDescriptor(methodDescriptor3, methodDescriptor));
        }
    }

    private static String makeQualifiedMethodName(String str, String[] strArr) {
        StringBuffer stringBuffer = new StringBuffer(str);
        stringBuffer.append('=');
        for (String str2 : strArr) {
            stringBuffer.append(':');
            stringBuffer.append(str2);
        }
        return stringBuffer.toString();
    }

    private int getTargetDefaultEventIndex() {
        return this.defaultEventIndex;
    }

    private int getTargetDefaultPropertyIndex() {
        return this.defaultPropertyIndex;
    }

    private BeanDescriptor getTargetBeanDescriptor() {
        BeanDescriptor beanDescriptor;
        if (this.explicitBeanInfo != null && (beanDescriptor = this.explicitBeanInfo.getBeanDescriptor()) != null) {
            return beanDescriptor;
        }
        return new BeanDescriptor(this.beanClass, findCustomizerClass(this.beanClass));
    }

    private static Class<?> findCustomizerClass(Class<?> cls) {
        try {
            Class<?> clsFindClass = ClassFinder.findClass(cls.getName() + "Customizer", cls.getClassLoader());
            if (!Component.class.isAssignableFrom(clsFindClass)) {
                return null;
            }
            if (Customizer.class.isAssignableFrom(clsFindClass)) {
                return clsFindClass;
            }
            return null;
        } catch (Exception e2) {
            return null;
        }
    }

    private boolean isEventHandler(Method method) {
        Type[] genericParameterTypes = method.getGenericParameterTypes();
        if (genericParameterTypes.length != 1) {
            return false;
        }
        return isSubclass(TypeResolver.erase(TypeResolver.resolveInClass(this.beanClass, genericParameterTypes[0])), EventObject.class);
    }

    private static Method[] getPublicDeclaredMethods(Class<?> cls) {
        Method[] methodArr;
        if (!ReflectUtil.isPackageAccessible(cls)) {
            return new Method[0];
        }
        synchronized (declaredMethodCache) {
            Method[] methods = declaredMethodCache.get(cls);
            if (methods == null) {
                methods = cls.getMethods();
                for (int i2 = 0; i2 < methods.length; i2++) {
                    Method method = methods[i2];
                    if (!method.getDeclaringClass().equals(cls)) {
                        methods[i2] = null;
                    } else {
                        try {
                            Method methodFindAccessibleMethod = MethodFinder.findAccessibleMethod(method);
                            Class<?> declaringClass = methodFindAccessibleMethod.getDeclaringClass();
                            methods[i2] = (declaringClass.equals(cls) || declaringClass.isInterface()) ? methodFindAccessibleMethod : null;
                        } catch (NoSuchMethodException e2) {
                        }
                    }
                }
                declaredMethodCache.put(cls, methods);
            }
            methodArr = methods;
        }
        return methodArr;
    }

    private static Method internalFindMethod(Class<?> cls, String str, int i2, Class[] clsArr) {
        Method method;
        Class<?> superclass = cls;
        loop0: while (true) {
            Class<?> cls2 = superclass;
            if (cls2 != null) {
                Method[] publicDeclaredMethods = getPublicDeclaredMethods(cls2);
                for (int i3 = 0; i3 < publicDeclaredMethods.length; i3++) {
                    method = publicDeclaredMethods[i3];
                    if (method != null && method.getName().equals(str)) {
                        Type[] genericParameterTypes = method.getGenericParameterTypes();
                        if (genericParameterTypes.length == i2) {
                            if (clsArr == null) {
                                break loop0;
                            }
                            boolean z2 = false;
                            if (i2 <= 0) {
                                break loop0;
                            }
                            for (int i4 = 0; i4 < i2; i4++) {
                                if (TypeResolver.erase(TypeResolver.resolveInClass(cls, genericParameterTypes[i4])) != clsArr[i4]) {
                                    z2 = true;
                                }
                            }
                            if (!z2) {
                                break loop0;
                            }
                        } else {
                            continue;
                        }
                    }
                }
                superclass = cls2.getSuperclass();
            } else {
                Method methodInternalFindMethod = null;
                for (Class<?> cls3 : cls.getInterfaces()) {
                    methodInternalFindMethod = internalFindMethod(cls3, str, i2, null);
                    if (methodInternalFindMethod != null) {
                        break;
                    }
                }
                return methodInternalFindMethod;
            }
        }
        return method;
    }

    static Method findMethod(Class<?> cls, String str, int i2) {
        return findMethod(cls, str, i2, null);
    }

    static Method findMethod(Class<?> cls, String str, int i2, Class[] clsArr) {
        if (str == null) {
            return null;
        }
        return internalFindMethod(cls, str, i2, clsArr);
    }

    static boolean isSubclass(Class<?> cls, Class<?> cls2) {
        if (cls == cls2) {
            return true;
        }
        if (cls == null || cls2 == null) {
            return false;
        }
        Class<?> superclass = cls;
        while (true) {
            Class<?> cls3 = superclass;
            if (cls3 != null) {
                if (cls3 == cls2) {
                    return true;
                }
                if (cls2.isInterface()) {
                    for (Class<?> cls4 : cls3.getInterfaces()) {
                        if (isSubclass(cls4, cls2)) {
                            return true;
                        }
                    }
                }
                superclass = cls3.getSuperclass();
            } else {
                return false;
            }
        }
    }

    private boolean throwsException(Method method, Class<?> cls) {
        for (Class<?> cls2 : method.getExceptionTypes()) {
            if (cls2 == cls) {
                return true;
            }
        }
        return false;
    }

    static Object instantiate(Class<?> cls, String str) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        return ClassFinder.findClass(str, cls.getClassLoader()).newInstance();
    }
}
