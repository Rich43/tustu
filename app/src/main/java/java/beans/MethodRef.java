package java.beans;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:java/beans/MethodRef.class */
final class MethodRef {
    private String signature;
    private SoftReference<Method> methodRef;
    private WeakReference<Class<?>> typeRef;

    MethodRef() {
    }

    void set(Method method) {
        if (method == null) {
            this.signature = null;
            this.methodRef = null;
            this.typeRef = null;
        } else {
            this.signature = method.toGenericString();
            this.methodRef = new SoftReference<>(method);
            this.typeRef = new WeakReference<>(method.getDeclaringClass());
        }
    }

    boolean isSet() {
        return this.methodRef != null;
    }

    Method get() {
        if (this.methodRef == null) {
            return null;
        }
        Method methodFind = this.methodRef.get();
        if (methodFind == null) {
            methodFind = find(this.typeRef.get(), this.signature);
            if (methodFind == null) {
                this.signature = null;
                this.methodRef = null;
                this.typeRef = null;
                return null;
            }
            this.methodRef = new SoftReference<>(methodFind);
        }
        if (ReflectUtil.isPackageAccessible(methodFind.getDeclaringClass())) {
            return methodFind;
        }
        return null;
    }

    private static Method find(Class<?> cls, String str) throws SecurityException {
        if (cls != null) {
            for (Method method : cls.getMethods()) {
                if (cls.equals(method.getDeclaringClass()) && method.toGenericString().equals(str)) {
                    return method;
                }
            }
            return null;
        }
        return null;
    }
}
