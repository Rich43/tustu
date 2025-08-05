package java.lang.invoke;

import java.security.AccessController;
import java.security.PrivilegedAction;
import sun.misc.Unsafe;

/* loaded from: rt.jar:java/lang/invoke/MethodHandleStatics.class */
class MethodHandleStatics {
    static final Unsafe UNSAFE = Unsafe.getUnsafe();
    static final boolean DEBUG_METHOD_HANDLE_NAMES;
    static final boolean DUMP_CLASS_FILES;
    static final boolean TRACE_INTERPRETER;
    static final boolean TRACE_METHOD_LINKAGE;
    static final int COMPILE_THRESHOLD;
    static final int DONT_INLINE_THRESHOLD;
    static final int PROFILE_LEVEL;
    static final boolean PROFILE_GWT;
    static final int CUSTOMIZE_THRESHOLD;

    private MethodHandleStatics() {
    }

    static {
        final Object[] objArr = new Object[9];
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.lang.invoke.MethodHandleStatics.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                objArr[0] = Boolean.valueOf(Boolean.getBoolean("java.lang.invoke.MethodHandle.DEBUG_NAMES"));
                objArr[1] = Boolean.valueOf(Boolean.getBoolean("java.lang.invoke.MethodHandle.DUMP_CLASS_FILES"));
                objArr[2] = Boolean.valueOf(Boolean.getBoolean("java.lang.invoke.MethodHandle.TRACE_INTERPRETER"));
                objArr[3] = Boolean.valueOf(Boolean.getBoolean("java.lang.invoke.MethodHandle.TRACE_METHOD_LINKAGE"));
                objArr[4] = Integer.getInteger("java.lang.invoke.MethodHandle.COMPILE_THRESHOLD", 0);
                objArr[5] = Integer.getInteger("java.lang.invoke.MethodHandle.DONT_INLINE_THRESHOLD", 30);
                objArr[6] = Integer.getInteger("java.lang.invoke.MethodHandle.PROFILE_LEVEL", 0);
                objArr[7] = Boolean.valueOf(Boolean.parseBoolean(System.getProperty("java.lang.invoke.MethodHandle.PROFILE_GWT", "true")));
                objArr[8] = Integer.getInteger("java.lang.invoke.MethodHandle.CUSTOMIZE_THRESHOLD", 127);
                return null;
            }
        });
        DEBUG_METHOD_HANDLE_NAMES = ((Boolean) objArr[0]).booleanValue();
        DUMP_CLASS_FILES = ((Boolean) objArr[1]).booleanValue();
        TRACE_INTERPRETER = ((Boolean) objArr[2]).booleanValue();
        TRACE_METHOD_LINKAGE = ((Boolean) objArr[3]).booleanValue();
        COMPILE_THRESHOLD = ((Integer) objArr[4]).intValue();
        DONT_INLINE_THRESHOLD = ((Integer) objArr[5]).intValue();
        PROFILE_LEVEL = ((Integer) objArr[6]).intValue();
        PROFILE_GWT = ((Boolean) objArr[7]).booleanValue();
        CUSTOMIZE_THRESHOLD = ((Integer) objArr[8]).intValue();
        if (CUSTOMIZE_THRESHOLD < -1 || CUSTOMIZE_THRESHOLD > 127) {
            throw newInternalError("CUSTOMIZE_THRESHOLD should be in [-1...127] range");
        }
    }

    static boolean debugEnabled() {
        return DEBUG_METHOD_HANDLE_NAMES | DUMP_CLASS_FILES | TRACE_INTERPRETER | TRACE_METHOD_LINKAGE;
    }

    static String getNameString(MethodHandle methodHandle, MethodType methodType) {
        if (methodType == null) {
            methodType = methodHandle.type();
        }
        MemberName memberNameInternalMemberName = null;
        if (methodHandle != null) {
            memberNameInternalMemberName = methodHandle.internalMemberName();
        }
        if (memberNameInternalMemberName == null) {
            return "invoke" + ((Object) methodType);
        }
        return memberNameInternalMemberName.getName() + ((Object) methodType);
    }

    static String getNameString(MethodHandle methodHandle, MethodHandle methodHandle2) {
        return getNameString(methodHandle, methodHandle2 == null ? (MethodType) null : methodHandle2.type());
    }

    static String getNameString(MethodHandle methodHandle) {
        return getNameString(methodHandle, (MethodType) null);
    }

    static String addTypeString(Object obj, MethodHandle methodHandle) {
        String strValueOf = String.valueOf(obj);
        if (methodHandle == null) {
            return strValueOf;
        }
        int iIndexOf = strValueOf.indexOf(40);
        if (iIndexOf >= 0) {
            strValueOf = strValueOf.substring(0, iIndexOf);
        }
        return strValueOf + ((Object) methodHandle.type());
    }

    static InternalError newInternalError(String str) {
        return new InternalError(str);
    }

    static InternalError newInternalError(String str, Throwable th) {
        return new InternalError(str, th);
    }

    static InternalError newInternalError(Throwable th) {
        return new InternalError(th);
    }

    static RuntimeException newIllegalStateException(String str) {
        return new IllegalStateException(str);
    }

    static RuntimeException newIllegalStateException(String str, Object obj) {
        return new IllegalStateException(message(str, obj));
    }

    static RuntimeException newIllegalArgumentException(String str) {
        return new IllegalArgumentException(str);
    }

    static RuntimeException newIllegalArgumentException(String str, Object obj) {
        return new IllegalArgumentException(message(str, obj));
    }

    static RuntimeException newIllegalArgumentException(String str, Object obj, Object obj2) {
        return new IllegalArgumentException(message(str, obj, obj2));
    }

    static Error uncaughtException(Throwable th) {
        if (th instanceof Error) {
            throw ((Error) th);
        }
        if (th instanceof RuntimeException) {
            throw ((RuntimeException) th);
        }
        throw newInternalError("uncaught exception", th);
    }

    static Error NYI() {
        throw new AssertionError((Object) "NYI");
    }

    private static String message(String str, Object obj) {
        if (obj != null) {
            str = str + ": " + obj;
        }
        return str;
    }

    private static String message(String str, Object obj, Object obj2) {
        if (obj != null || obj2 != null) {
            str = str + ": " + obj + ", " + obj2;
        }
        return str;
    }
}
