package com.sun.jmx.mbeanserver;

import com.sun.jmx.mbeanserver.MBeanAnalyzer;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.WeakHashMap;
import javax.management.Descriptor;
import javax.management.ImmutableDescriptor;
import javax.management.InvalidAttributeValueException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.NotCompliantMBeanException;
import javax.management.NotificationBroadcaster;
import javax.management.ReflectionException;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:com/sun/jmx/mbeanserver/MBeanIntrospector.class */
abstract class MBeanIntrospector<M> {
    abstract PerInterfaceMap<M> getPerInterfaceMap();

    abstract MBeanInfoMap getMBeanInfoMap();

    abstract MBeanAnalyzer<M> getAnalyzer(Class<?> cls) throws NotCompliantMBeanException;

    abstract boolean isMXBean();

    abstract M mFrom(Method method);

    abstract String getName(M m2);

    abstract Type getGenericReturnType(M m2);

    abstract Type[] getGenericParameterTypes(M m2);

    abstract String[] getSignature(M m2);

    abstract void checkMethod(M m2);

    abstract Object invokeM2(M m2, Object obj, Object[] objArr, Object obj2) throws MBeanException, IllegalAccessException, InvocationTargetException;

    abstract boolean validParameter(M m2, Object obj, int i2, Object obj2);

    abstract MBeanAttributeInfo getMBeanAttributeInfo(String str, M m2, M m3);

    abstract MBeanOperationInfo getMBeanOperationInfo(String str, M m2);

    abstract Descriptor getBasicMBeanDescriptor();

    abstract Descriptor getMBeanDescriptor(Class<?> cls);

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/MBeanIntrospector$PerInterfaceMap.class */
    static final class PerInterfaceMap<M> extends WeakHashMap<Class<?>, WeakReference<PerInterface<M>>> {
        PerInterfaceMap() {
        }
    }

    MBeanIntrospector() {
    }

    final List<Method> getMethods(Class<?> cls) {
        ReflectUtil.checkPackageAccess(cls);
        return Arrays.asList(cls.getMethods());
    }

    final PerInterface<M> getPerInterface(Class<?> cls) throws NotCompliantMBeanException {
        PerInterface<M> perInterface;
        PerInterfaceMap<M> perInterfaceMap = getPerInterfaceMap();
        synchronized (perInterfaceMap) {
            WeakReference<PerInterface<M>> weakReference = perInterfaceMap.get(cls);
            PerInterface<M> perInterface2 = weakReference == null ? null : weakReference.get();
            if (perInterface2 == null) {
                try {
                    MBeanAnalyzer<M> analyzer = getAnalyzer(cls);
                    perInterface2 = new PerInterface<>(cls, this, analyzer, makeInterfaceMBeanInfo(cls, analyzer));
                    perInterfaceMap.put(cls, new WeakReference(perInterface2));
                } catch (Exception e2) {
                    throw Introspector.throwException(cls, e2);
                }
            }
            perInterface = perInterface2;
        }
        return perInterface;
    }

    private MBeanInfo makeInterfaceMBeanInfo(Class<?> cls, MBeanAnalyzer<M> mBeanAnalyzer) {
        MBeanInfoMaker mBeanInfoMaker = new MBeanInfoMaker();
        mBeanAnalyzer.visit(mBeanInfoMaker);
        return mBeanInfoMaker.makeMBeanInfo(cls, "Information on the management interface of the MBean");
    }

    final boolean consistent(M m2, M m3) {
        return m2 == null || m3 == null || getGenericReturnType(m2).equals(getGenericParameterTypes(m3)[0]);
    }

    final Object invokeM(M m2, Object obj, Object[] objArr, Object obj2) throws MBeanException, ReflectionException {
        try {
            return invokeM2(m2, obj, objArr, obj2);
        } catch (IllegalAccessException e2) {
            throw new ReflectionException(e2, e2.toString());
        } catch (InvocationTargetException e3) {
            unwrapInvocationTargetException(e3);
            throw new RuntimeException(e3);
        }
    }

    final void invokeSetter(String str, M m2, Object obj, Object obj2, Object obj3) throws MBeanException, InvalidAttributeValueException, ReflectionException {
        try {
            invokeM2(m2, obj, new Object[]{obj2}, obj3);
        } catch (IllegalAccessException e2) {
            throw new ReflectionException(e2, e2.toString());
        } catch (RuntimeException e3) {
            maybeInvalidParameter(str, m2, obj2, obj3);
            throw e3;
        } catch (InvocationTargetException e4) {
            maybeInvalidParameter(str, m2, obj2, obj3);
            unwrapInvocationTargetException(e4);
        }
    }

    private void maybeInvalidParameter(String str, M m2, Object obj, Object obj2) throws InvalidAttributeValueException {
        if (!validParameter(m2, obj, 0, obj2)) {
            throw new InvalidAttributeValueException("Invalid value for attribute " + str + ": " + obj);
        }
    }

    static boolean isValidParameter(Method method, Object obj, int i2) throws ArrayIndexOutOfBoundsException {
        try {
            Array.set(Array.newInstance(method.getParameterTypes()[i2], 1), 0, obj);
            return true;
        } catch (IllegalArgumentException e2) {
            return false;
        }
    }

    private static void unwrapInvocationTargetException(InvocationTargetException invocationTargetException) throws MBeanException {
        Throwable cause = invocationTargetException.getCause();
        if (cause instanceof RuntimeException) {
            throw ((RuntimeException) cause);
        }
        if (cause instanceof Error) {
            throw ((Error) cause);
        }
        throw new MBeanException((Exception) cause, cause == null ? null : cause.toString());
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/MBeanIntrospector$MBeanInfoMaker.class */
    private class MBeanInfoMaker implements MBeanAnalyzer.MBeanVisitor<M> {
        private final List<MBeanAttributeInfo> attrs;
        private final List<MBeanOperationInfo> ops;

        private MBeanInfoMaker() {
            this.attrs = Util.newList();
            this.ops = Util.newList();
        }

        @Override // com.sun.jmx.mbeanserver.MBeanAnalyzer.MBeanVisitor
        public void visitAttribute(String str, M m2, M m3) {
            this.attrs.add(MBeanIntrospector.this.getMBeanAttributeInfo(str, m2, m3));
        }

        @Override // com.sun.jmx.mbeanserver.MBeanAnalyzer.MBeanVisitor
        public void visitOperation(String str, M m2) {
            this.ops.add(MBeanIntrospector.this.getMBeanOperationInfo(str, m2));
        }

        MBeanInfo makeMBeanInfo(Class<?> cls, String str) {
            return new MBeanInfo(cls.getName(), str, (MBeanAttributeInfo[]) this.attrs.toArray(new MBeanAttributeInfo[0]), null, (MBeanOperationInfo[]) this.ops.toArray(new MBeanOperationInfo[0]), null, DescriptorCache.getInstance().union(new ImmutableDescriptor("interfaceClassName=" + cls.getName()), MBeanIntrospector.this.getBasicMBeanDescriptor(), Introspector.descriptorForElement(cls)));
        }
    }

    /* loaded from: rt.jar:com/sun/jmx/mbeanserver/MBeanIntrospector$MBeanInfoMap.class */
    static class MBeanInfoMap extends WeakHashMap<Class<?>, WeakHashMap<Class<?>, MBeanInfo>> {
        MBeanInfoMap() {
        }
    }

    final MBeanInfo getMBeanInfo(Object obj, PerInterface<M> perInterface) {
        MBeanInfo classMBeanInfo = getClassMBeanInfo(obj.getClass(), perInterface);
        MBeanNotificationInfo[] mBeanNotificationInfoArrFindNotifications = findNotifications(obj);
        if (mBeanNotificationInfoArrFindNotifications == null || mBeanNotificationInfoArrFindNotifications.length == 0) {
            return classMBeanInfo;
        }
        return new MBeanInfo(classMBeanInfo.getClassName(), classMBeanInfo.getDescription(), classMBeanInfo.getAttributes(), classMBeanInfo.getConstructors(), classMBeanInfo.getOperations(), mBeanNotificationInfoArrFindNotifications, classMBeanInfo.getDescriptor());
    }

    final MBeanInfo getClassMBeanInfo(Class<?> cls, PerInterface<M> perInterface) {
        MBeanInfo mBeanInfo;
        MBeanInfoMap mBeanInfoMap = getMBeanInfoMap();
        synchronized (mBeanInfoMap) {
            WeakHashMap<Class<?>, MBeanInfo> weakHashMap = mBeanInfoMap.get(cls);
            if (weakHashMap == null) {
                weakHashMap = new WeakHashMap<>();
                mBeanInfoMap.put(cls, weakHashMap);
            }
            Class<?> mBeanInterface = perInterface.getMBeanInterface();
            MBeanInfo mBeanInfo2 = weakHashMap.get(mBeanInterface);
            if (mBeanInfo2 == null) {
                MBeanInfo mBeanInfo3 = perInterface.getMBeanInfo();
                mBeanInfo2 = new MBeanInfo(cls.getName(), mBeanInfo3.getDescription(), mBeanInfo3.getAttributes(), findConstructors(cls), mBeanInfo3.getOperations(), (MBeanNotificationInfo[]) null, ImmutableDescriptor.union(mBeanInfo3.getDescriptor(), getMBeanDescriptor(cls)));
                weakHashMap.put(mBeanInterface, mBeanInfo2);
            }
            mBeanInfo = mBeanInfo2;
        }
        return mBeanInfo;
    }

    static MBeanNotificationInfo[] findNotifications(Object obj) {
        MBeanNotificationInfo[] notificationInfo;
        if (!(obj instanceof NotificationBroadcaster) || (notificationInfo = ((NotificationBroadcaster) obj).getNotificationInfo()) == null) {
            return null;
        }
        MBeanNotificationInfo[] mBeanNotificationInfoArr = new MBeanNotificationInfo[notificationInfo.length];
        for (int i2 = 0; i2 < notificationInfo.length; i2++) {
            MBeanNotificationInfo mBeanNotificationInfo = notificationInfo[i2];
            if (mBeanNotificationInfo.getClass() != MBeanNotificationInfo.class) {
                mBeanNotificationInfo = (MBeanNotificationInfo) mBeanNotificationInfo.clone();
            }
            mBeanNotificationInfoArr[i2] = mBeanNotificationInfo;
        }
        return mBeanNotificationInfoArr;
    }

    private static MBeanConstructorInfo[] findConstructors(Class<?> cls) throws SecurityException {
        Constructor<?>[] constructors = cls.getConstructors();
        MBeanConstructorInfo[] mBeanConstructorInfoArr = new MBeanConstructorInfo[constructors.length];
        for (int i2 = 0; i2 < constructors.length; i2++) {
            mBeanConstructorInfoArr[i2] = new MBeanConstructorInfo("Public constructor of the MBean", constructors[i2]);
        }
        return mBeanConstructorInfoArr;
    }
}
