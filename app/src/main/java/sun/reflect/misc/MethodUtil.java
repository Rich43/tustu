package sun.reflect.misc;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.PrivilegedExceptionAction;
import java.security.SecureClassLoader;
import java.security.cert.Certificate;
import java.util.HashMap;
import java.util.Map;
import sun.misc.IOUtils;

/* loaded from: rt.jar:sun/reflect/misc/MethodUtil.class */
public final class MethodUtil extends SecureClassLoader {
    private static final String MISC_PKG = "sun.reflect.misc.";
    private static final String TRAMPOLINE = "sun.reflect.misc.Trampoline";
    private static final Method bounce = getTrampoline();

    private MethodUtil() {
    }

    public static Method getMethod(Class<?> cls, String str, Class<?>[] clsArr) throws NoSuchMethodException {
        ReflectUtil.checkPackageAccess(cls);
        return cls.getMethod(str, clsArr);
    }

    public static Method[] getMethods(Class<?> cls) {
        ReflectUtil.checkPackageAccess(cls);
        return cls.getMethods();
    }

    public static Method[] getPublicMethods(Class<?> cls) {
        if (System.getSecurityManager() == null) {
            return cls.getMethods();
        }
        HashMap map = new HashMap();
        while (cls != null && !getInternalPublicMethods(cls, map)) {
            getInterfaceMethods(cls, map);
            cls = cls.getSuperclass();
        }
        return (Method[]) map.values().toArray(new Method[map.size()]);
    }

    private static void getInterfaceMethods(Class<?> cls, Map<Signature, Method> map) {
        for (Class<?> cls2 : cls.getInterfaces()) {
            if (!getInternalPublicMethods(cls2, map)) {
                getInterfaceMethods(cls2, map);
            }
        }
    }

    private static boolean getInternalPublicMethods(Class<?> cls, Map<Signature, Method> map) {
        try {
            if (!Modifier.isPublic(cls.getModifiers()) || !ReflectUtil.isPackageAccessible(cls)) {
                return false;
            }
            Method[] methods = cls.getMethods();
            boolean z2 = true;
            int i2 = 0;
            while (true) {
                if (i2 >= methods.length) {
                    break;
                }
                if (Modifier.isPublic(methods[i2].getDeclaringClass().getModifiers())) {
                    i2++;
                } else {
                    z2 = false;
                    break;
                }
            }
            if (z2) {
                for (Method method : methods) {
                    addMethod(map, method);
                }
            } else {
                for (int i3 = 0; i3 < methods.length; i3++) {
                    if (cls.equals(methods[i3].getDeclaringClass())) {
                        addMethod(map, methods[i3]);
                    }
                }
            }
            return z2;
        } catch (SecurityException e2) {
            return false;
        }
    }

    private static void addMethod(Map<Signature, Method> map, Method method) {
        Signature signature = new Signature(method);
        if (!map.containsKey(signature)) {
            map.put(signature, method);
        } else if (!method.getDeclaringClass().isInterface() && map.get(signature).getDeclaringClass().isInterface()) {
            map.put(signature, method);
        }
    }

    /* loaded from: rt.jar:sun/reflect/misc/MethodUtil$Signature.class */
    private static class Signature {
        private String methodName;
        private Class<?>[] argClasses;
        private volatile int hashCode = 0;

        Signature(Method method) {
            this.methodName = method.getName();
            this.argClasses = method.getParameterTypes();
        }

        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            Signature signature = (Signature) obj;
            if (!this.methodName.equals(signature.methodName) || this.argClasses.length != signature.argClasses.length) {
                return false;
            }
            for (int i2 = 0; i2 < this.argClasses.length; i2++) {
                if (this.argClasses[i2] != signature.argClasses[i2]) {
                    return false;
                }
            }
            return true;
        }

        public int hashCode() {
            if (this.hashCode == 0) {
                int iHashCode = (37 * 17) + this.methodName.hashCode();
                if (this.argClasses != null) {
                    for (int i2 = 0; i2 < this.argClasses.length; i2++) {
                        iHashCode = (37 * iHashCode) + (this.argClasses[i2] == null ? 0 : this.argClasses[i2].hashCode());
                    }
                }
                this.hashCode = iHashCode;
            }
            return this.hashCode;
        }
    }

    public static Object invoke(Method method, Object obj, Object[] objArr) throws IllegalAccessException, InvocationTargetException {
        try {
            return bounce.invoke(null, method, obj, objArr);
        } catch (IllegalAccessException e2) {
            throw new Error("Unexpected invocation error", e2);
        } catch (InvocationTargetException e3) {
            Throwable cause = e3.getCause();
            if (cause instanceof InvocationTargetException) {
                throw ((InvocationTargetException) cause);
            }
            if (cause instanceof IllegalAccessException) {
                throw ((IllegalAccessException) cause);
            }
            if (cause instanceof RuntimeException) {
                throw ((RuntimeException) cause);
            }
            if (cause instanceof Error) {
                throw ((Error) cause);
            }
            throw new Error("Unexpected invocation error", cause);
        }
    }

    private static Method getTrampoline() {
        try {
            return (Method) AccessController.doPrivileged(new PrivilegedExceptionAction<Method>() { // from class: sun.reflect.misc.MethodUtil.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public Method run() throws Exception {
                    Method declaredMethod = MethodUtil.getTrampolineClass().getDeclaredMethod("invoke", Method.class, Object.class, Object[].class);
                    declaredMethod.setAccessible(true);
                    return declaredMethod;
                }
            });
        } catch (Exception e2) {
            throw new InternalError("bouncer cannot be found", e2);
        }
    }

    @Override // java.lang.ClassLoader
    protected synchronized Class<?> loadClass(String str, boolean z2) throws ClassNotFoundException {
        ReflectUtil.checkPackageAccess(str);
        Class<?> clsFindLoadedClass = findLoadedClass(str);
        if (clsFindLoadedClass == null) {
            try {
                clsFindLoadedClass = findClass(str);
            } catch (ClassNotFoundException e2) {
            }
            if (clsFindLoadedClass == null) {
                clsFindLoadedClass = getParent().loadClass(str);
            }
        }
        if (z2) {
            resolveClass(clsFindLoadedClass);
        }
        return clsFindLoadedClass;
    }

    @Override // java.lang.ClassLoader
    protected Class<?> findClass(String str) throws ClassNotFoundException {
        if (!str.startsWith(MISC_PKG)) {
            throw new ClassNotFoundException(str);
        }
        URL resource = getResource(str.replace('.', '/').concat(".class"));
        if (resource != null) {
            try {
                return defineClass(str, resource);
            } catch (IOException e2) {
                throw new ClassNotFoundException(str, e2);
            }
        }
        throw new ClassNotFoundException(str);
    }

    private Class<?> defineClass(String str, URL url) throws IOException {
        byte[] bytes = getBytes(url);
        CodeSource codeSource = new CodeSource((URL) null, (Certificate[]) null);
        if (!str.equals(TRAMPOLINE)) {
            throw new IOException("MethodUtil: bad name " + str);
        }
        return defineClass(str, bytes, 0, bytes.length, codeSource);
    }

    private static byte[] getBytes(URL url) throws IOException {
        URLConnection uRLConnectionOpenConnection = url.openConnection();
        if ((uRLConnectionOpenConnection instanceof HttpURLConnection) && ((HttpURLConnection) uRLConnectionOpenConnection).getResponseCode() >= 400) {
            throw new IOException("open HTTP connection failed.");
        }
        int contentLength = uRLConnectionOpenConnection.getContentLength();
        BufferedInputStream bufferedInputStream = new BufferedInputStream(uRLConnectionOpenConnection.getInputStream());
        Throwable th = null;
        try {
            try {
                byte[] allBytes = IOUtils.readAllBytes(bufferedInputStream);
                if (contentLength != -1 && allBytes.length != contentLength) {
                    throw new EOFException("Expected:" + contentLength + ", read:" + allBytes.length);
                }
                if (bufferedInputStream != null) {
                    if (0 != 0) {
                        try {
                            bufferedInputStream.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    } else {
                        bufferedInputStream.close();
                    }
                }
                return allBytes;
            } finally {
            }
        } catch (Throwable th3) {
            if (bufferedInputStream != null) {
                if (th != null) {
                    try {
                        bufferedInputStream.close();
                    } catch (Throwable th4) {
                        th.addSuppressed(th4);
                    }
                } else {
                    bufferedInputStream.close();
                }
            }
            throw th3;
        }
    }

    @Override // java.security.SecureClassLoader
    protected PermissionCollection getPermissions(CodeSource codeSource) {
        PermissionCollection permissions = super.getPermissions(codeSource);
        permissions.add(new AllPermission());
        return permissions;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Class<?> getTrampolineClass() {
        try {
            return Class.forName(TRAMPOLINE, true, new MethodUtil());
        } catch (ClassNotFoundException e2) {
            return null;
        }
    }
}
