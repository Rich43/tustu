package java.lang.invoke;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import sun.invoke.WrapperInstance;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:java/lang/invoke/MethodHandleProxies.class */
public class MethodHandleProxies {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !MethodHandleProxies.class.desiredAssertionStatus();
    }

    private MethodHandleProxies() {
    }

    @CallerSensitive
    public static <T> T asInterfaceInstance(final Class<T> cls, final MethodHandle methodHandle) throws SecurityException, IllegalArgumentException {
        MethodHandle methodHandleBindCaller;
        Object objNewProxyInstance;
        if (!cls.isInterface() || !Modifier.isPublic(cls.getModifiers())) {
            throw MethodHandleStatics.newIllegalArgumentException("not a public interface", cls.getName());
        }
        if (System.getSecurityManager() != null) {
            Class<?> callerClass = Reflection.getCallerClass();
            ClassLoader classLoader = callerClass != null ? callerClass.getClassLoader() : null;
            ReflectUtil.checkProxyPackageAccess(classLoader, cls);
            methodHandleBindCaller = classLoader != null ? bindCaller(methodHandle, callerClass) : methodHandle;
        } else {
            methodHandleBindCaller = methodHandle;
        }
        ClassLoader classLoader2 = cls.getClassLoader();
        if (classLoader2 == null) {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            classLoader2 = contextClassLoader != null ? contextClassLoader : ClassLoader.getSystemClassLoader();
        }
        final Method[] singleNameMethods = getSingleNameMethods(cls);
        if (singleNameMethods == null) {
            throw MethodHandleStatics.newIllegalArgumentException("not a single-method interface", cls.getName());
        }
        final MethodHandle[] methodHandleArr = new MethodHandle[singleNameMethods.length];
        for (int i2 = 0; i2 < singleNameMethods.length; i2++) {
            Method method = singleNameMethods[i2];
            MethodType methodType = MethodType.methodType(method.getReturnType(), method.getParameterTypes());
            MethodHandle methodHandleAsType = methodHandleBindCaller.asType(methodType);
            methodHandleArr[i2] = methodHandleAsType.asType(methodHandleAsType.type().changeReturnType(Object.class)).asSpreader(Object[].class, methodType.parameterCount());
        }
        final ConcurrentHashMap concurrentHashMap = hasDefaultMethods(cls) ? new ConcurrentHashMap() : null;
        final InvocationHandler invocationHandler = new InvocationHandler() { // from class: java.lang.invoke.MethodHandleProxies.1
            private Object getArg(String str) {
                if (str == "getWrapperInstanceTarget") {
                    return methodHandle;
                }
                if (str == "getWrapperInstanceType") {
                    return cls;
                }
                throw new AssertionError();
            }

            @Override // java.lang.reflect.InvocationHandler
            public Object invoke(Object obj, Method method2, Object[] objArr) throws Throwable {
                for (int i3 = 0; i3 < singleNameMethods.length; i3++) {
                    if (method2.equals(singleNameMethods[i3])) {
                        return (Object) methodHandleArr[i3].invokeExact(objArr);
                    }
                }
                if (method2.getDeclaringClass() != WrapperInstance.class) {
                    if (MethodHandleProxies.isObjectMethod(method2)) {
                        return MethodHandleProxies.callObjectMethod(obj, method2, objArr);
                    }
                    if (MethodHandleProxies.isDefaultMethod(method2)) {
                        return MethodHandleProxies.callDefaultMethod(concurrentHashMap, obj, cls, method2, objArr);
                    }
                    throw MethodHandleStatics.newInternalError("bad proxy method: " + ((Object) method2));
                }
                return getArg(method2.getName());
            }
        };
        if (System.getSecurityManager() != null) {
            final ClassLoader classLoader3 = classLoader2;
            objNewProxyInstance = AccessController.doPrivileged(new PrivilegedAction<Object>() { // from class: java.lang.invoke.MethodHandleProxies.2
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Object run2() {
                    return Proxy.newProxyInstance(classLoader3, new Class[]{cls, WrapperInstance.class}, invocationHandler);
                }
            });
        } else {
            objNewProxyInstance = Proxy.newProxyInstance(classLoader2, new Class[]{cls, WrapperInstance.class}, invocationHandler);
        }
        return cls.cast(objNewProxyInstance);
    }

    private static MethodHandle bindCaller(MethodHandle methodHandle, Class<?> cls) {
        MethodHandle methodHandleBindCaller = MethodHandleImpl.bindCaller(methodHandle, cls);
        if (methodHandle.isVarargsCollector()) {
            MethodType methodTypeType = methodHandleBindCaller.type();
            return methodHandleBindCaller.asVarargsCollector(methodTypeType.parameterType(methodTypeType.parameterCount() - 1));
        }
        return methodHandleBindCaller;
    }

    public static boolean isWrapperInstance(Object obj) {
        return obj instanceof WrapperInstance;
    }

    private static WrapperInstance asWrapperInstance(Object obj) {
        if (obj != null) {
            try {
                return (WrapperInstance) obj;
            } catch (ClassCastException e2) {
            }
        }
        throw MethodHandleStatics.newIllegalArgumentException("not a wrapper instance");
    }

    public static MethodHandle wrapperInstanceTarget(Object obj) {
        return asWrapperInstance(obj).getWrapperInstanceTarget();
    }

    public static Class<?> wrapperInstanceType(Object obj) {
        return asWrapperInstance(obj).getWrapperInstanceType();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isObjectMethod(Method method) {
        switch (method.getName()) {
            case "toString":
                if (method.getReturnType() != String.class || method.getParameterTypes().length != 0) {
                }
                break;
            case "hashCode":
                if (method.getReturnType() != Integer.TYPE || method.getParameterTypes().length != 0) {
                }
                break;
            case "equals":
                if (method.getReturnType() != Boolean.TYPE || method.getParameterTypes().length != 1 || method.getParameterTypes()[0] != Object.class) {
                }
                break;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object callObjectMethod(Object obj, Method method, Object[] objArr) {
        if (!$assertionsDisabled && !isObjectMethod(method)) {
            throw new AssertionError(method);
        }
        switch (method.getName()) {
            case "toString":
                return obj.getClass().getName() + "@" + Integer.toHexString(obj.hashCode());
            case "hashCode":
                return Integer.valueOf(System.identityHashCode(obj));
            case "equals":
                return Boolean.valueOf(obj == objArr[0]);
            default:
                return null;
        }
    }

    private static Method[] getSingleNameMethods(Class<?> cls) throws SecurityException {
        ArrayList arrayList = new ArrayList();
        String str = null;
        for (Method method : cls.getMethods()) {
            if (!isObjectMethod(method) && Modifier.isAbstract(method.getModifiers())) {
                String name = method.getName();
                if (str == null) {
                    str = name;
                } else if (!str.equals(name)) {
                    return null;
                }
                arrayList.add(method);
            }
        }
        if (str == null) {
            return null;
        }
        return (Method[]) arrayList.toArray(new Method[arrayList.size()]);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean isDefaultMethod(Method method) {
        return !Modifier.isAbstract(method.getModifiers());
    }

    private static boolean hasDefaultMethods(Class<?> cls) throws SecurityException {
        for (Method method : cls.getMethods()) {
            if (!isObjectMethod(method) && !Modifier.isAbstract(method.getModifiers())) {
                return true;
            }
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Object callDefaultMethod(ConcurrentHashMap<Method, MethodHandle> concurrentHashMap, Object obj, Class<?> cls, Method method, Object[] objArr) throws Throwable {
        if ($assertionsDisabled || (isDefaultMethod(method) && !isObjectMethod(method))) {
            return (Object) concurrentHashMap.computeIfAbsent(method, method2 -> {
                try {
                    return MethodHandles.Lookup.IMPL_LOOKUP.findSpecial(cls, method2.getName(), MethodType.methodType(method2.getReturnType(), method2.getParameterTypes()), obj.getClass()).asSpreader(Object[].class, method2.getParameterCount());
                } catch (IllegalAccessException | NoSuchMethodException e2) {
                    throw new InternalError(e2);
                }
            }).invoke(obj, objArr);
        }
        throw new AssertionError(method);
    }
}
