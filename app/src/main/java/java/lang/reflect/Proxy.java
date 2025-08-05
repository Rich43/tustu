package java.lang.reflect;

import java.io.Serializable;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.IdentityHashMap;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;
import sun.misc.ProxyGenerator;
import sun.misc.VM;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.reflect.misc.ReflectUtil;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/lang/reflect/Proxy.class */
public class Proxy implements Serializable {
    private static final long serialVersionUID = -2222568056686623797L;

    /* renamed from: h, reason: collision with root package name */
    protected InvocationHandler f12445h;
    private static final Class<?>[] constructorParams = {InvocationHandler.class};
    private static final WeakCache<ClassLoader, Class<?>[], Class<?>> proxyClassCache = new WeakCache<>(new KeyFactory(), new ProxyClassFactory());
    private static final Object key0 = new Object();

    /* JADX INFO: Access modifiers changed from: private */
    public static native Class<?> defineClass0(ClassLoader classLoader, String str, byte[] bArr, int i2, int i3);

    private Proxy() {
    }

    protected Proxy(InvocationHandler invocationHandler) {
        Objects.requireNonNull(invocationHandler);
        this.f12445h = invocationHandler;
    }

    @CallerSensitive
    public static Class<?> getProxyClass(ClassLoader classLoader, Class<?>... clsArr) throws IllegalArgumentException {
        Class[] clsArr2 = (Class[]) clsArr.clone();
        if (System.getSecurityManager() != null) {
            checkProxyAccess(Reflection.getCallerClass(), classLoader, clsArr2);
        }
        return getProxyClass0(classLoader, clsArr2);
    }

    private static void checkProxyAccess(Class<?> cls, ClassLoader classLoader, Class<?>... clsArr) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            ClassLoader classLoader2 = cls.getClassLoader();
            if (VM.isSystemDomainLoader(classLoader) && !VM.isSystemDomainLoader(classLoader2)) {
                securityManager.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
            }
            ReflectUtil.checkProxyPackageAccess(classLoader2, clsArr);
        }
    }

    private static Class<?> getProxyClass0(ClassLoader classLoader, Class<?>... clsArr) {
        if (clsArr.length > 65535) {
            throw new IllegalArgumentException("interface limit exceeded");
        }
        return proxyClassCache.get(classLoader, clsArr);
    }

    /* loaded from: rt.jar:java/lang/reflect/Proxy$Key1.class */
    private static final class Key1 extends WeakReference<Class<?>> {
        private final int hash;

        Key1(Class<?> cls) {
            super(cls);
            this.hash = cls.hashCode();
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            Class<?> cls;
            return this == obj || (obj != null && obj.getClass() == Key1.class && (cls = get()) != null && cls == ((Key1) obj).get());
        }
    }

    /* loaded from: rt.jar:java/lang/reflect/Proxy$Key2.class */
    private static final class Key2 extends WeakReference<Class<?>> {
        private final int hash;
        private final WeakReference<Class<?>> ref2;

        Key2(Class<?> cls, Class<?> cls2) {
            super(cls);
            this.hash = (31 * cls.hashCode()) + cls2.hashCode();
            this.ref2 = new WeakReference<>(cls2);
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            Class<?> cls;
            Class<?> cls2;
            return this == obj || (obj != null && obj.getClass() == Key2.class && (cls = get()) != null && cls == ((Key2) obj).get() && (cls2 = this.ref2.get()) != null && cls2 == ((Key2) obj).ref2.get());
        }
    }

    /* loaded from: rt.jar:java/lang/reflect/Proxy$KeyX.class */
    private static final class KeyX {
        private final int hash;
        private final WeakReference<Class<?>>[] refs;

        KeyX(Class<?>[] clsArr) {
            this.hash = Arrays.hashCode(clsArr);
            this.refs = new WeakReference[clsArr.length];
            for (int i2 = 0; i2 < clsArr.length; i2++) {
                this.refs[i2] = new WeakReference<>(clsArr[i2]);
            }
        }

        public int hashCode() {
            return this.hash;
        }

        public boolean equals(Object obj) {
            return this == obj || (obj != null && obj.getClass() == KeyX.class && equals(this.refs, ((KeyX) obj).refs));
        }

        private static boolean equals(WeakReference<Class<?>>[] weakReferenceArr, WeakReference<Class<?>>[] weakReferenceArr2) {
            if (weakReferenceArr.length != weakReferenceArr2.length) {
                return false;
            }
            for (int i2 = 0; i2 < weakReferenceArr.length; i2++) {
                Class<?> cls = weakReferenceArr[i2].get();
                if (cls == null || cls != weakReferenceArr2[i2].get()) {
                    return false;
                }
            }
            return true;
        }
    }

    /* loaded from: rt.jar:java/lang/reflect/Proxy$KeyFactory.class */
    private static final class KeyFactory implements BiFunction<ClassLoader, Class<?>[], Object> {
        private KeyFactory() {
        }

        @Override // java.util.function.BiFunction
        public Object apply(ClassLoader classLoader, Class<?>[] clsArr) {
            switch (clsArr.length) {
                case 0:
                    return Proxy.key0;
                case 1:
                    return new Key1(clsArr[0]);
                case 2:
                    return new Key2(clsArr[0], clsArr[1]);
                default:
                    return new KeyX(clsArr);
            }
        }
    }

    /* loaded from: rt.jar:java/lang/reflect/Proxy$ProxyClassFactory.class */
    private static final class ProxyClassFactory implements BiFunction<ClassLoader, Class<?>[], Class<?>> {
        private static final String proxyClassNamePrefix = "$Proxy";
        private static final AtomicLong nextUniqueNumber = new AtomicLong();

        private ProxyClassFactory() {
        }

        @Override // java.util.function.BiFunction
        public Class<?> apply(ClassLoader classLoader, Class<?>[] clsArr) throws SecurityException {
            IdentityHashMap identityHashMap = new IdentityHashMap(clsArr.length);
            for (Class<?> cls : clsArr) {
                Class<?> cls2 = null;
                try {
                    cls2 = Class.forName(cls.getName(), false, classLoader);
                } catch (ClassNotFoundException e2) {
                }
                if (cls2 != cls) {
                    throw new IllegalArgumentException(((Object) cls) + " is not visible from class loader: " + ((Object) classLoader));
                }
                if (!cls2.isInterface()) {
                    throw new IllegalArgumentException(cls2.getName() + " is not an interface");
                }
                if (identityHashMap.put(cls2, Boolean.TRUE) != 0) {
                    throw new IllegalArgumentException("repeated interface: " + cls2.getName());
                }
            }
            String str = null;
            int i2 = 17;
            for (Class<?> cls3 : clsArr) {
                if (!Modifier.isPublic(cls3.getModifiers())) {
                    i2 = 16;
                    String name = cls3.getName();
                    int iLastIndexOf = name.lastIndexOf(46);
                    String strSubstring = iLastIndexOf == -1 ? "" : name.substring(0, iLastIndexOf + 1);
                    if (str == null) {
                        str = strSubstring;
                    } else if (!strSubstring.equals(str)) {
                        throw new IllegalArgumentException("non-public interfaces from different packages");
                    }
                }
            }
            if (str == null) {
                str = "com.sun.proxy.";
            }
            String str2 = str + proxyClassNamePrefix + nextUniqueNumber.getAndIncrement();
            byte[] bArrGenerateProxyClass = ProxyGenerator.generateProxyClass(str2, clsArr, i2);
            try {
                return Proxy.defineClass0(classLoader, str2, bArrGenerateProxyClass, 0, bArrGenerateProxyClass.length);
            } catch (ClassFormatError e3) {
                throw new IllegalArgumentException(e3.toString());
            }
        }
    }

    @CallerSensitive
    public static Object newProxyInstance(ClassLoader classLoader, Class<?>[] clsArr, InvocationHandler invocationHandler) throws IllegalArgumentException {
        Objects.requireNonNull(invocationHandler);
        Class[] clsArr2 = (Class[]) clsArr.clone();
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            checkProxyAccess(Reflection.getCallerClass(), classLoader, clsArr2);
        }
        Class<?> proxyClass0 = getProxyClass0(classLoader, clsArr2);
        if (securityManager != null) {
            try {
                checkNewProxyPermission(Reflection.getCallerClass(), proxyClass0);
            } catch (IllegalAccessException | InstantiationException e2) {
                throw new InternalError(e2.toString(), e2);
            } catch (NoSuchMethodException e3) {
                throw new InternalError(e3.toString(), e3);
            } catch (InvocationTargetException e4) {
                Throwable cause = e4.getCause();
                if (cause instanceof RuntimeException) {
                    throw ((RuntimeException) cause);
                }
                throw new InternalError(cause.toString(), cause);
            }
        }
        final Constructor<?> constructor = proxyClass0.getConstructor(constructorParams);
        if (!Modifier.isPublic(proxyClass0.getModifiers())) {
            AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.lang.reflect.Proxy.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                public Void run() {
                    constructor.setAccessible(true);
                    return null;
                }
            });
        }
        return constructor.newInstance(invocationHandler);
    }

    private static void checkNewProxyPermission(Class<?> cls, Class<?> cls2) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null && ReflectUtil.isNonPublicProxyClass(cls2)) {
            ClassLoader classLoader = cls.getClassLoader();
            ClassLoader classLoader2 = cls2.getClassLoader();
            int iLastIndexOf = cls2.getName().lastIndexOf(46);
            String strSubstring = iLastIndexOf == -1 ? "" : cls2.getName().substring(0, iLastIndexOf);
            int iLastIndexOf2 = cls.getName().lastIndexOf(46);
            String strSubstring2 = iLastIndexOf2 == -1 ? "" : cls.getName().substring(0, iLastIndexOf2);
            if (classLoader2 != classLoader || !strSubstring.equals(strSubstring2)) {
                securityManager.checkPermission(new ReflectPermission("newProxyInPackage." + strSubstring));
            }
        }
    }

    public static boolean isProxyClass(Class<?> cls) {
        return Proxy.class.isAssignableFrom(cls) && proxyClassCache.containsValue(cls);
    }

    @CallerSensitive
    public static InvocationHandler getInvocationHandler(Object obj) throws IllegalArgumentException {
        if (!isProxyClass(obj.getClass())) {
            throw new IllegalArgumentException("not a proxy instance");
        }
        InvocationHandler invocationHandler = ((Proxy) obj).f12445h;
        if (System.getSecurityManager() != null) {
            Class<?> cls = invocationHandler.getClass();
            if (ReflectUtil.needsPackageAccessCheck(Reflection.getCallerClass().getClassLoader(), cls.getClassLoader())) {
                ReflectUtil.checkPackageAccess(cls);
            }
        }
        return invocationHandler;
    }
}
