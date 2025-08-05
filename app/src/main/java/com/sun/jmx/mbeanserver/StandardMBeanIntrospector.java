package com.sun.jmx.mbeanserver;

import com.sun.jmx.mbeanserver.MBeanIntrospector;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.WeakHashMap;
import javax.management.Descriptor;
import javax.management.ImmutableDescriptor;
import javax.management.IntrospectionException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanException;
import javax.management.MBeanOperationInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationBroadcaster;
import javax.management.NotificationBroadcasterSupport;
import sun.reflect.misc.MethodUtil;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/StandardMBeanIntrospector.class */
class StandardMBeanIntrospector extends MBeanIntrospector<Method> {
    private static final StandardMBeanIntrospector instance = new StandardMBeanIntrospector();
    private static final WeakHashMap<Class<?>, Boolean> definitelyImmutable = new WeakHashMap<>();
    private static final MBeanIntrospector.PerInterfaceMap<Method> perInterfaceMap = new MBeanIntrospector.PerInterfaceMap<>();
    private static final MBeanIntrospector.MBeanInfoMap mbeanInfoMap = new MBeanIntrospector.MBeanInfoMap();

    StandardMBeanIntrospector() {
    }

    static StandardMBeanIntrospector getInstance() {
        return instance;
    }

    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    MBeanIntrospector.PerInterfaceMap<Method> getPerInterfaceMap() {
        return perInterfaceMap;
    }

    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    MBeanIntrospector.MBeanInfoMap getMBeanInfoMap() {
        return mbeanInfoMap;
    }

    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    MBeanAnalyzer<Method> getAnalyzer(Class<?> cls) throws NotCompliantMBeanException {
        return MBeanAnalyzer.analyzer(cls, this);
    }

    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    boolean isMXBean() {
        return false;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public Method mFrom(Method method) {
        return method;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public String getName(Method method) {
        return method.getName();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public Type getGenericReturnType(Method method) {
        return method.getGenericReturnType();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public Type[] getGenericParameterTypes(Method method) {
        return method.getGenericParameterTypes();
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public String[] getSignature(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        String[] strArr = new String[parameterTypes.length];
        for (int i2 = 0; i2 < parameterTypes.length; i2++) {
            strArr[i2] = parameterTypes[i2].getName();
        }
        return strArr;
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public void checkMethod(Method method) {
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public Object invokeM2(Method method, Object obj, Object[] objArr, Object obj2) throws MBeanException, IllegalAccessException, InvocationTargetException {
        return MethodUtil.invoke(method, obj, objArr);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public boolean validParameter(Method method, Object obj, int i2, Object obj2) {
        return isValidParameter(method, obj, i2);
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public MBeanAttributeInfo getMBeanAttributeInfo(String str, Method method, Method method2) {
        try {
            return new MBeanAttributeInfo(str, "Attribute exposed for management", method, method2);
        } catch (IntrospectionException e2) {
            throw new RuntimeException(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: package-private */
    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    public MBeanOperationInfo getMBeanOperationInfo(String str, Method method) {
        return new MBeanOperationInfo("Operation exposed for management", method);
    }

    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    Descriptor getBasicMBeanDescriptor() {
        return ImmutableDescriptor.EMPTY_DESCRIPTOR;
    }

    @Override // com.sun.jmx.mbeanserver.MBeanIntrospector
    Descriptor getMBeanDescriptor(Class<?> cls) {
        return new ImmutableDescriptor("mxbean=false", "immutableInfo=" + isDefinitelyImmutableInfo(cls));
    }

    static boolean isDefinitelyImmutableInfo(Class<?> cls) {
        boolean zBooleanValue;
        if (!NotificationBroadcaster.class.isAssignableFrom(cls)) {
            return true;
        }
        synchronized (definitelyImmutable) {
            Boolean boolValueOf = definitelyImmutable.get(cls);
            if (boolValueOf == null) {
                if (NotificationBroadcasterSupport.class.isAssignableFrom(cls)) {
                    try {
                        boolValueOf = Boolean.valueOf(cls.getMethod("getNotificationInfo", new Class[0]).getDeclaringClass() == NotificationBroadcasterSupport.class);
                    } catch (Exception e2) {
                        return false;
                    }
                } else {
                    boolValueOf = false;
                }
                definitelyImmutable.put(cls, boolValueOf);
            }
            zBooleanValue = boolValueOf.booleanValue();
        }
        return zBooleanValue;
    }
}
