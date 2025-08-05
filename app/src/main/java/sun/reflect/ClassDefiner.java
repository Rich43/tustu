package sun.reflect;

import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.misc.Unsafe;

/* loaded from: rt.jar:sun/reflect/ClassDefiner.class */
class ClassDefiner {
    static final Unsafe unsafe = Unsafe.getUnsafe();

    ClassDefiner() {
    }

    static Class<?> defineClass(String str, byte[] bArr, int i2, int i3, final ClassLoader classLoader) {
        return unsafe.defineClass(str, bArr, i2, i3, (ClassLoader) AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() { // from class: sun.reflect.ClassDefiner.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ClassLoader run2() {
                return new DelegatingClassLoader(classLoader);
            }
        }), null);
    }
}
