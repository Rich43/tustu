package com.sun.jmx.mbeanserver;

import com.sun.jmx.remote.util.EnvHelp;
import java.lang.annotation.Annotation;
import java.lang.ref.SoftReference;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.lang.reflect.UndeclaredThrowableException;
import java.security.AccessController;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;
import javax.management.AttributeNotFoundException;
import javax.management.Descriptor;
import javax.management.DescriptorKey;
import javax.management.DynamicMBean;
import javax.management.ImmutableDescriptor;
import javax.management.MBeanInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.openmbean.CompositeData;
import sun.reflect.misc.MethodUtil;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/Introspector.class */
public class Introspector {
    public static final boolean ALLOW_NONPUBLIC_MBEAN = Boolean.parseBoolean((String) AccessController.doPrivileged(new GetPropertyAction("jdk.jmx.mbeans.allowNonPublic")));

    private Introspector() {
    }

    public static final boolean isDynamic(Class<?> cls) {
        return DynamicMBean.class.isAssignableFrom(cls);
    }

    public static void testCreation(Class<?> cls) throws NotCompliantMBeanException {
        int modifiers = cls.getModifiers();
        if (Modifier.isAbstract(modifiers) || Modifier.isInterface(modifiers)) {
            throw new NotCompliantMBeanException("MBean class must be concrete");
        }
        if (cls.getConstructors().length == 0) {
            throw new NotCompliantMBeanException("MBean class must have public constructor");
        }
    }

    public static void checkCompliance(Class<?> cls) throws NotCompliantMBeanException {
        if (DynamicMBean.class.isAssignableFrom(cls)) {
            return;
        }
        try {
            getStandardMBeanInterface(cls);
        } catch (NotCompliantMBeanException e2) {
            try {
                getMXBeanInterface(cls);
            } catch (NotCompliantMBeanException e3) {
                throw new NotCompliantMBeanException("MBean class " + cls.getName() + " does not implement DynamicMBean, and neither follows the Standard MBean conventions (" + e2.toString() + ") nor the MXBean conventions (" + e3.toString() + ")");
            }
        }
    }

    public static <T> DynamicMBean makeDynamicMBean(T t2) throws NotCompliantMBeanException {
        if (t2 instanceof DynamicMBean) {
            return (DynamicMBean) t2;
        }
        Class<?> cls = t2.getClass();
        Class cls2 = null;
        try {
            cls2 = (Class) Util.cast(getStandardMBeanInterface(cls));
        } catch (NotCompliantMBeanException e2) {
        }
        if (cls2 != null) {
            return new StandardMBeanSupport(t2, cls2);
        }
        try {
            cls2 = (Class) Util.cast(getMXBeanInterface(cls));
        } catch (NotCompliantMBeanException e3) {
        }
        if (cls2 != null) {
            return new MXBeanSupport(t2, cls2);
        }
        checkCompliance(cls);
        throw new NotCompliantMBeanException("Not compliant");
    }

    public static MBeanInfo testCompliance(Class<?> cls) throws NotCompliantMBeanException {
        if (isDynamic(cls)) {
            return null;
        }
        return testCompliance(cls, null);
    }

    public static void testComplianceMXBeanInterface(Class<?> cls) throws NotCompliantMBeanException {
        MXBeanIntrospector.getInstance().getAnalyzer(cls);
    }

    public static void testComplianceMBeanInterface(Class<?> cls) throws NotCompliantMBeanException {
        StandardMBeanIntrospector.getInstance().getAnalyzer(cls);
    }

    public static synchronized MBeanInfo testCompliance(Class<?> cls, Class<?> cls2) throws NotCompliantMBeanException {
        if (cls2 == null) {
            cls2 = getStandardMBeanInterface(cls);
        }
        ReflectUtil.checkPackageAccess(cls2);
        return getClassMBeanInfo(StandardMBeanIntrospector.getInstance(), cls, cls2);
    }

    private static <M> MBeanInfo getClassMBeanInfo(MBeanIntrospector<M> mBeanIntrospector, Class<?> cls, Class<?> cls2) throws NotCompliantMBeanException {
        return mBeanIntrospector.getClassMBeanInfo(cls, mBeanIntrospector.getPerInterface(cls2));
    }

    public static Class<?> getMBeanInterface(Class<?> cls) {
        if (isDynamic(cls)) {
            return null;
        }
        try {
            return getStandardMBeanInterface(cls);
        } catch (NotCompliantMBeanException e2) {
            return null;
        }
    }

    public static <T> Class<? super T> getStandardMBeanInterface(Class<T> cls) throws NotCompliantMBeanException {
        Class<? super T> clsFindMBeanInterface = null;
        for (Class<T> superclass = cls; superclass != null; superclass = superclass.getSuperclass()) {
            clsFindMBeanInterface = findMBeanInterface(superclass, superclass.getName());
            if (clsFindMBeanInterface != null) {
                break;
            }
        }
        if (clsFindMBeanInterface != null) {
            return clsFindMBeanInterface;
        }
        throw new NotCompliantMBeanException("Class " + cls.getName() + " is not a JMX compliant Standard MBean");
    }

    public static <T> Class<? super T> getMXBeanInterface(Class<T> cls) throws NotCompliantMBeanException {
        try {
            return MXBeanSupport.findMXBeanInterface(cls);
        } catch (Exception e2) {
            throw throwException(cls, e2);
        }
    }

    private static <T> Class<? super T> findMBeanInterface(Class<T> cls, String str) {
        Class<T> superclass = cls;
        while (true) {
            Class<T> cls2 = superclass;
            if (cls2 != null) {
                for (Class<?> cls3 : cls2.getInterfaces()) {
                    Class<? super T> clsImplementsMBean = implementsMBean((Class) Util.cast(cls3), str);
                    if (clsImplementsMBean != null) {
                        return clsImplementsMBean;
                    }
                }
                superclass = cls2.getSuperclass();
            } else {
                return null;
            }
        }
    }

    public static Descriptor descriptorForElement(AnnotatedElement annotatedElement) {
        if (annotatedElement == null) {
            return ImmutableDescriptor.EMPTY_DESCRIPTOR;
        }
        return descriptorForAnnotations(annotatedElement.getAnnotations());
    }

    public static Descriptor descriptorForAnnotations(Annotation[] annotationArr) {
        if (annotationArr.length == 0) {
            return ImmutableDescriptor.EMPTY_DESCRIPTOR;
        }
        HashMap map = new HashMap();
        for (Annotation annotation : annotationArr) {
            Class<? extends Annotation> clsAnnotationType = annotation.annotationType();
            boolean z2 = false;
            for (Method method : clsAnnotationType.getMethods()) {
                DescriptorKey descriptorKey = (DescriptorKey) method.getAnnotation(DescriptorKey.class);
                if (descriptorKey != null) {
                    String strValue = descriptorKey.value();
                    if (!z2) {
                        try {
                            ReflectUtil.checkPackageAccess(clsAnnotationType);
                            z2 = true;
                        } catch (RuntimeException e2) {
                            throw e2;
                        } catch (Exception e3) {
                            throw new UndeclaredThrowableException(e3);
                        }
                    }
                    Object objAnnotationToField = annotationToField(MethodUtil.invoke(method, annotation, null));
                    Object objPut = map.put(strValue, objAnnotationToField);
                    if (objPut != null && !equals(objPut, objAnnotationToField)) {
                        throw new IllegalArgumentException("Inconsistent values for descriptor field " + strValue + " from annotations: " + objAnnotationToField + " :: " + objPut);
                    }
                }
            }
        }
        if (map.isEmpty()) {
            return ImmutableDescriptor.EMPTY_DESCRIPTOR;
        }
        return new ImmutableDescriptor(map);
    }

    static NotCompliantMBeanException throwException(Class<?> cls, Throwable th) throws NotCompliantMBeanException, SecurityException {
        if (th instanceof SecurityException) {
            throw ((SecurityException) th);
        }
        if (th instanceof NotCompliantMBeanException) {
            throw ((NotCompliantMBeanException) th);
        }
        NotCompliantMBeanException notCompliantMBeanException = new NotCompliantMBeanException((cls == null ? "null class" : cls.getName()) + ": " + (th == null ? "Not compliant" : th.getMessage()));
        notCompliantMBeanException.initCause(th);
        throw notCompliantMBeanException;
    }

    private static Object annotationToField(Object obj) {
        if (obj == null) {
            return null;
        }
        if ((obj instanceof Number) || (obj instanceof String) || (obj instanceof Character) || (obj instanceof Boolean) || (obj instanceof String[])) {
            return obj;
        }
        Class<?> cls = obj.getClass();
        if (cls.isArray()) {
            if (cls.getComponentType().isPrimitive()) {
                return obj;
            }
            Object[] objArr = (Object[]) obj;
            String[] strArr = new String[objArr.length];
            for (int i2 = 0; i2 < objArr.length; i2++) {
                strArr[i2] = (String) annotationToField(objArr[i2]);
            }
            return strArr;
        }
        if (obj instanceof Class) {
            return ((Class) obj).getName();
        }
        if (obj instanceof Enum) {
            return ((Enum) obj).name();
        }
        if (Proxy.isProxyClass(cls)) {
            cls = cls.getInterfaces()[0];
        }
        throw new IllegalArgumentException("Illegal type for annotation element using @DescriptorKey: " + cls.getName());
    }

    private static boolean equals(Object obj, Object obj2) {
        return Arrays.deepEquals(new Object[]{obj}, new Object[]{obj2});
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static <T> Class<? super T> implementsMBean(Class<T> cls, String str) {
        String str2 = str + "MBean";
        if (cls.getName().equals(str2)) {
            return cls;
        }
        Class<?>[] interfaces = cls.getInterfaces();
        for (int i2 = 0; i2 < interfaces.length; i2++) {
            if (interfaces[i2].getName().equals(str2) && (Modifier.isPublic(interfaces[i2].getModifiers()) || ALLOW_NONPUBLIC_MBEAN)) {
                return (Class) Util.cast(interfaces[i2]);
            }
        }
        return null;
    }

    public static Object elementFromComplex(Object obj, String str) throws AttributeNotFoundException {
        try {
            if (obj.getClass().isArray() && str.equals("length")) {
                return Integer.valueOf(Array.getLength(obj));
            }
            if (obj instanceof CompositeData) {
                return ((CompositeData) obj).get(str);
            }
            Class<?> cls = obj.getClass();
            Method readMethod = null;
            if (BeansHelper.isAvailable()) {
                Object[] propertyDescriptors = BeansHelper.getPropertyDescriptors(BeansHelper.getBeanInfo(cls));
                int length = propertyDescriptors.length;
                int i2 = 0;
                while (true) {
                    if (i2 >= length) {
                        break;
                    }
                    Object obj2 = propertyDescriptors[i2];
                    if (!BeansHelper.getPropertyName(obj2).equals(str)) {
                        i2++;
                    } else {
                        readMethod = BeansHelper.getReadMethod(obj2);
                        break;
                    }
                }
            } else {
                readMethod = SimpleIntrospector.getReadMethod(cls, str);
            }
            if (readMethod != null) {
                ReflectUtil.checkPackageAccess(readMethod.getDeclaringClass());
                return MethodUtil.invoke(readMethod, obj, new Class[0]);
            }
            throw new AttributeNotFoundException("Could not find the getter method for the property " + str + " using the Java Beans introspector");
        } catch (InvocationTargetException e2) {
            throw new IllegalArgumentException(e2);
        } catch (AttributeNotFoundException e3) {
            throw e3;
        } catch (Exception e4) {
            throw ((AttributeNotFoundException) EnvHelp.initCause(new AttributeNotFoundException(e4.getMessage()), e4));
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/Introspector$SimpleIntrospector.class */
    private static class SimpleIntrospector {
        private static final String GET_METHOD_PREFIX = "get";
        private static final String IS_METHOD_PREFIX = "is";
        private static final Map<Class<?>, SoftReference<List<Method>>> cache = Collections.synchronizedMap(new WeakHashMap());

        private SimpleIntrospector() {
        }

        private static List<Method> getCachedMethods(Class<?> cls) {
            List<Method> list;
            SoftReference<List<Method>> softReference = cache.get(cls);
            if (softReference != null && (list = softReference.get()) != null) {
                return list;
            }
            return null;
        }

        static boolean isReadMethod(Method method) {
            if (Modifier.isStatic(method.getModifiers())) {
                return false;
            }
            String name = method.getName();
            if (method.getParameterTypes().length != 0 || name.length() <= 2) {
                return false;
            }
            return name.startsWith("is") ? method.getReturnType() == Boolean.TYPE : name.length() > 3 && name.startsWith("get") && method.getReturnType() != Void.TYPE;
        }

        static List<Method> getReadMethods(Class<?> cls) {
            List<Method> cachedMethods = getCachedMethods(cls);
            if (cachedMethods != null) {
                return cachedMethods;
            }
            List<Method> listEliminateCovariantMethods = MBeanAnalyzer.eliminateCovariantMethods(StandardMBeanIntrospector.getInstance().getMethods(cls));
            LinkedList linkedList = new LinkedList();
            for (Method method : listEliminateCovariantMethods) {
                if (isReadMethod(method)) {
                    if (method.getName().startsWith("is")) {
                        linkedList.add(0, method);
                    } else {
                        linkedList.add(method);
                    }
                }
            }
            cache.put(cls, new SoftReference<>(linkedList));
            return linkedList;
        }

        static Method getReadMethod(Class<?> cls, String str) {
            String str2 = str.substring(0, 1).toUpperCase(Locale.ENGLISH) + str.substring(1);
            String str3 = "get" + str2;
            String str4 = "is" + str2;
            for (Method method : getReadMethods(cls)) {
                String name = method.getName();
                if (name.equals(str4) || name.equals(str3)) {
                    return method;
                }
            }
            return null;
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/Introspector$BeansHelper.class */
    private static class BeansHelper {
        private static final Class<?> introspectorClass = getClass("java.beans.Introspector");
        private static final Class<?> beanInfoClass;
        private static final Class<?> getPropertyDescriptorClass;
        private static final Method getBeanInfo;
        private static final Method getPropertyDescriptors;
        private static final Method getPropertyName;
        private static final Method getReadMethod;

        static {
            beanInfoClass = introspectorClass == null ? null : getClass("java.beans.BeanInfo");
            getPropertyDescriptorClass = beanInfoClass == null ? null : getClass("java.beans.PropertyDescriptor");
            getBeanInfo = getMethod(introspectorClass, "getBeanInfo", Class.class);
            getPropertyDescriptors = getMethod(beanInfoClass, "getPropertyDescriptors", new Class[0]);
            getPropertyName = getMethod(getPropertyDescriptorClass, "getName", new Class[0]);
            getReadMethod = getMethod(getPropertyDescriptorClass, "getReadMethod", new Class[0]);
        }

        private static Class<?> getClass(String str) {
            try {
                return Class.forName(str, true, null);
            } catch (ClassNotFoundException e2) {
                return null;
            }
        }

        private static Method getMethod(Class<?> cls, String str, Class<?>... clsArr) {
            if (cls != null) {
                try {
                    return cls.getMethod(str, clsArr);
                } catch (NoSuchMethodException e2) {
                    throw new AssertionError(e2);
                }
            }
            return null;
        }

        private BeansHelper() {
        }

        static boolean isAvailable() {
            return introspectorClass != null;
        }

        static Object getBeanInfo(Class<?> cls) throws Exception {
            try {
                return getBeanInfo.invoke(null, cls);
            } catch (IllegalAccessException e2) {
                throw new AssertionError(e2);
            } catch (InvocationTargetException e3) {
                Throwable cause = e3.getCause();
                if (cause instanceof Exception) {
                    throw ((Exception) cause);
                }
                throw new AssertionError(e3);
            }
        }

        static Object[] getPropertyDescriptors(Object obj) {
            try {
                return (Object[]) getPropertyDescriptors.invoke(obj, new Object[0]);
            } catch (IllegalAccessException e2) {
                throw new AssertionError(e2);
            } catch (InvocationTargetException e3) {
                Throwable cause = e3.getCause();
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                }
                throw new AssertionError(e3);
            }
        }

        static String getPropertyName(Object obj) {
            try {
                return (String) getPropertyName.invoke(obj, new Object[0]);
            } catch (IllegalAccessException e2) {
                throw new AssertionError(e2);
            } catch (InvocationTargetException e3) {
                Throwable cause = e3.getCause();
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                }
                throw new AssertionError(e3);
            }
        }

        static Method getReadMethod(Object obj) {
            try {
                return (Method) getReadMethod.invoke(obj, new Object[0]);
            } catch (IllegalAccessException e2) {
                throw new AssertionError(e2);
            } catch (InvocationTargetException e3) {
                Throwable cause = e3.getCause();
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                }
                throw new AssertionError(e3);
            }
        }
    }
}
