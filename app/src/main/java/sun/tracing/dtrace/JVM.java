package sun.tracing.dtrace;

import java.lang.reflect.Method;
import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:sun/tracing/dtrace/JVM.class */
class JVM {
    private static native long activate0(String str, DTraceProvider[] dTraceProviderArr);

    private static native void dispose0(long j2);

    private static native boolean isEnabled0(Method method);

    private static native boolean isSupported0();

    private static native Class<?> defineClass0(ClassLoader classLoader, String str, byte[] bArr, int i2, int i3);

    JVM() {
    }

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.tracing.dtrace.JVM.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                System.loadLibrary("jsdt");
                return null;
            }
        });
    }

    static long activate(String str, DTraceProvider[] dTraceProviderArr) {
        return activate0(str, dTraceProviderArr);
    }

    static void dispose(long j2) {
        dispose0(j2);
    }

    static boolean isEnabled(Method method) {
        return isEnabled0(method);
    }

    static boolean isSupported() {
        return isSupported0();
    }

    static Class<?> defineClass(ClassLoader classLoader, String str, byte[] bArr, int i2, int i3) {
        return defineClass0(classLoader, str, bArr, i2, i3);
    }
}
