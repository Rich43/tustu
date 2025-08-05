package java.lang.invoke;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;

/* loaded from: rt.jar:java/lang/invoke/MethodHandleNatives.class */
class MethodHandleNatives {
    static final boolean COUNT_GWT;
    static final /* synthetic */ boolean $assertionsDisabled;

    static native void init(MemberName memberName, Object obj);

    static native void expand(MemberName memberName);

    static native MemberName resolve(MemberName memberName, Class<?> cls) throws LinkageError, ClassNotFoundException;

    static native int getMembers(Class<?> cls, String str, String str2, int i2, Class<?> cls2, int i3, MemberName[] memberNameArr);

    static native long objectFieldOffset(MemberName memberName);

    static native long staticFieldOffset(MemberName memberName);

    static native Object staticFieldBase(MemberName memberName);

    static native Object getMemberVMInfo(MemberName memberName);

    static native int getConstant(int i2);

    static native void setCallSiteTargetNormal(CallSite callSite, MethodHandle methodHandle);

    static native void setCallSiteTargetVolatile(CallSite callSite, MethodHandle methodHandle);

    private static native void registerNatives();

    private static native int getNamedCon(int i2, Object[] objArr);

    static {
        $assertionsDisabled = !MethodHandleNatives.class.desiredAssertionStatus();
        registerNatives();
        COUNT_GWT = getConstant(4) != 0;
        MethodHandleImpl.initStatics();
        byte b2 = 1;
        while (true) {
            byte b3 = b2;
            if (b3 < 10) {
                if (!$assertionsDisabled) {
                    if (refKindHasReceiver(b3) != (((1 << b3) & 682) != 0)) {
                        throw new AssertionError((int) b3);
                    }
                }
                b2 = (byte) (b3 + 1);
            } else {
                if (!$assertionsDisabled && !verifyConstants()) {
                    throw new AssertionError();
                }
                return;
            }
        }
    }

    private MethodHandleNatives() {
    }

    /* loaded from: rt.jar:java/lang/invoke/MethodHandleNatives$Constants.class */
    static class Constants {
        static final int GC_COUNT_GWT = 4;
        static final int GC_LAMBDA_SUPPORT = 5;
        static final int MN_IS_METHOD = 65536;
        static final int MN_IS_CONSTRUCTOR = 131072;
        static final int MN_IS_FIELD = 262144;
        static final int MN_IS_TYPE = 524288;
        static final int MN_CALLER_SENSITIVE = 1048576;
        static final int MN_REFERENCE_KIND_SHIFT = 24;
        static final int MN_REFERENCE_KIND_MASK = 15;
        static final int MN_SEARCH_SUPERCLASSES = 1048576;
        static final int MN_SEARCH_INTERFACES = 2097152;
        static final int T_BOOLEAN = 4;
        static final int T_CHAR = 5;
        static final int T_FLOAT = 6;
        static final int T_DOUBLE = 7;
        static final int T_BYTE = 8;
        static final int T_SHORT = 9;
        static final int T_INT = 10;
        static final int T_LONG = 11;
        static final int T_OBJECT = 12;
        static final int T_VOID = 14;
        static final int T_ILLEGAL = 99;
        static final byte CONSTANT_Utf8 = 1;
        static final byte CONSTANT_Integer = 3;
        static final byte CONSTANT_Float = 4;
        static final byte CONSTANT_Long = 5;
        static final byte CONSTANT_Double = 6;
        static final byte CONSTANT_Class = 7;
        static final byte CONSTANT_String = 8;
        static final byte CONSTANT_Fieldref = 9;
        static final byte CONSTANT_Methodref = 10;
        static final byte CONSTANT_InterfaceMethodref = 11;
        static final byte CONSTANT_NameAndType = 12;
        static final byte CONSTANT_MethodHandle = 15;
        static final byte CONSTANT_MethodType = 16;
        static final byte CONSTANT_InvokeDynamic = 18;
        static final byte CONSTANT_LIMIT = 19;
        static final char ACC_PUBLIC = 1;
        static final char ACC_PRIVATE = 2;
        static final char ACC_PROTECTED = 4;
        static final char ACC_STATIC = '\b';
        static final char ACC_FINAL = 16;
        static final char ACC_SYNCHRONIZED = ' ';
        static final char ACC_VOLATILE = '@';
        static final char ACC_TRANSIENT = 128;
        static final char ACC_NATIVE = 256;
        static final char ACC_INTERFACE = 512;
        static final char ACC_ABSTRACT = 1024;
        static final char ACC_STRICT = 2048;
        static final char ACC_SYNTHETIC = 4096;
        static final char ACC_ANNOTATION = 8192;
        static final char ACC_ENUM = 16384;
        static final char ACC_SUPER = ' ';
        static final char ACC_BRIDGE = '@';
        static final char ACC_VARARGS = 128;
        static final byte REF_NONE = 0;
        static final byte REF_getField = 1;
        static final byte REF_getStatic = 2;
        static final byte REF_putField = 3;
        static final byte REF_putStatic = 4;
        static final byte REF_invokeVirtual = 5;
        static final byte REF_invokeStatic = 6;
        static final byte REF_invokeSpecial = 7;
        static final byte REF_newInvokeSpecial = 8;
        static final byte REF_invokeInterface = 9;
        static final byte REF_LIMIT = 10;

        Constants() {
        }
    }

    static boolean refKindIsValid(int i2) {
        return i2 > 0 && i2 < 10;
    }

    static boolean refKindIsField(byte b2) {
        if ($assertionsDisabled || refKindIsValid(b2)) {
            return b2 <= 4;
        }
        throw new AssertionError();
    }

    static boolean refKindIsGetter(byte b2) {
        if ($assertionsDisabled || refKindIsValid(b2)) {
            return b2 <= 2;
        }
        throw new AssertionError();
    }

    static boolean refKindIsSetter(byte b2) {
        return refKindIsField(b2) && !refKindIsGetter(b2);
    }

    static boolean refKindIsMethod(byte b2) {
        return (refKindIsField(b2) || b2 == 8) ? false : true;
    }

    static boolean refKindIsConstructor(byte b2) {
        return b2 == 8;
    }

    static boolean refKindHasReceiver(byte b2) {
        if ($assertionsDisabled || refKindIsValid(b2)) {
            return (b2 & 1) != 0;
        }
        throw new AssertionError();
    }

    static boolean refKindIsStatic(byte b2) {
        return (refKindHasReceiver(b2) || b2 == 8) ? false : true;
    }

    static boolean refKindDoesDispatch(byte b2) {
        if ($assertionsDisabled || refKindIsValid(b2)) {
            return b2 == 5 || b2 == 9;
        }
        throw new AssertionError();
    }

    static String refKindName(byte b2) {
        if (!$assertionsDisabled && !refKindIsValid(b2)) {
            throw new AssertionError();
        }
        switch (b2) {
            case 1:
                return "getField";
            case 2:
                return "getStatic";
            case 3:
                return "putField";
            case 4:
                return "putStatic";
            case 5:
                return "invokeVirtual";
            case 6:
                return "invokeStatic";
            case 7:
                return "invokeSpecial";
            case 8:
                return "newInvokeSpecial";
            case 9:
                return "invokeInterface";
            default:
                return "REF_???";
        }
    }

    static boolean verifyConstants() {
        Object[] objArr = {null};
        int i2 = 0;
        while (true) {
            objArr[0] = null;
            int namedCon = getNamedCon(i2, objArr);
            if (objArr[0] != null) {
                String str = (String) objArr[0];
                try {
                    int i3 = Constants.class.getDeclaredField(str).getInt(null);
                    if (i3 != namedCon) {
                        String str2 = str + ": JVM has " + namedCon + " while Java has " + i3;
                        if (str.equals("CONV_OP_LIMIT")) {
                            System.err.println("warning: " + str2);
                        } else {
                            throw new InternalError(str2);
                        }
                    }
                } catch (IllegalAccessException | NoSuchFieldException e2) {
                    String str3 = str + ": JVM has " + namedCon + " which Java does not define";
                }
                i2++;
            } else {
                return true;
            }
        }
    }

    static MemberName linkCallSite(Object obj, Object obj2, Object obj3, Object obj4, Object obj5, Object[] objArr) {
        MethodHandle methodHandle = (MethodHandle) obj2;
        Class cls = (Class) obj;
        String strIntern = obj3.toString().intern();
        MethodType methodType = (MethodType) obj4;
        if (!MethodHandleStatics.TRACE_METHOD_LINKAGE) {
            return linkCallSiteImpl(cls, methodHandle, strIntern, methodType, obj5, objArr);
        }
        return linkCallSiteTracing(cls, methodHandle, strIntern, methodType, obj5, objArr);
    }

    static MemberName linkCallSiteImpl(Class<?> cls, MethodHandle methodHandle, String str, MethodType methodType, Object obj, Object[] objArr) {
        CallSite callSiteMakeSite = CallSite.makeSite(methodHandle, str, methodType, obj, cls);
        if (callSiteMakeSite instanceof ConstantCallSite) {
            objArr[0] = callSiteMakeSite.dynamicInvoker();
            return Invokers.linkToTargetMethod(methodType);
        }
        objArr[0] = callSiteMakeSite;
        return Invokers.linkToCallSiteMethod(methodType);
    }

    static MemberName linkCallSiteTracing(Class<?> cls, MethodHandle methodHandle, String str, MethodType methodType, Object obj, Object[] objArr) {
        MemberName memberNameInternalMemberName = methodHandle.internalMemberName();
        if (memberNameInternalMemberName == null) {
            memberNameInternalMemberName = methodHandle;
        }
        System.out.println("linkCallSite " + cls.getName() + " " + ((Object) memberNameInternalMemberName) + " " + str + ((Object) methodType) + "/" + (obj instanceof Object[] ? Arrays.asList((Object[]) obj) : obj));
        try {
            MemberName memberNameLinkCallSiteImpl = linkCallSiteImpl(cls, methodHandle, str, methodType, obj, objArr);
            System.out.println("linkCallSite => " + ((Object) memberNameLinkCallSiteImpl) + " + " + objArr[0]);
            return memberNameLinkCallSiteImpl;
        } catch (Throwable th) {
            System.out.println("linkCallSite => throw " + ((Object) th));
            throw th;
        }
    }

    static MethodType findMethodHandleType(Class<?> cls, Class<?>[] clsArr) {
        return MethodType.makeImpl(cls, clsArr, true);
    }

    static MemberName linkMethod(Class<?> cls, int i2, Class<?> cls2, String str, Object obj, Object[] objArr) {
        if (!MethodHandleStatics.TRACE_METHOD_LINKAGE) {
            return linkMethodImpl(cls, i2, cls2, str, obj, objArr);
        }
        return linkMethodTracing(cls, i2, cls2, str, obj, objArr);
    }

    static MemberName linkMethodImpl(Class<?> cls, int i2, Class<?> cls2, String str, Object obj, Object[] objArr) {
        if (cls2 == MethodHandle.class && i2 == 5) {
            try {
                return Invokers.methodHandleInvokeLinkerMethod(str, fixMethodType(cls, obj), objArr);
            } catch (Throwable th) {
                if (th instanceof LinkageError) {
                    throw ((LinkageError) th);
                }
                throw new LinkageError(th.getMessage(), th);
            }
        }
        throw new LinkageError("no such method " + cls2.getName() + "." + str + obj);
    }

    private static MethodType fixMethodType(Class<?> cls, Object obj) {
        if (obj instanceof MethodType) {
            return (MethodType) obj;
        }
        return MethodType.fromMethodDescriptorString((String) obj, cls.getClassLoader());
    }

    static MemberName linkMethodTracing(Class<?> cls, int i2, Class<?> cls2, String str, Object obj, Object[] objArr) {
        System.out.println("linkMethod " + cls2.getName() + "." + str + obj + "/" + Integer.toHexString(i2));
        try {
            MemberName memberNameLinkMethodImpl = linkMethodImpl(cls, i2, cls2, str, obj, objArr);
            System.out.println("linkMethod => " + ((Object) memberNameLinkMethodImpl) + " + " + objArr[0]);
            return memberNameLinkMethodImpl;
        } catch (Throwable th) {
            System.out.println("linkMethod => throw " + ((Object) th));
            throw th;
        }
    }

    static MethodHandle linkMethodHandleConstant(Class<?> cls, int i2, Class<?> cls2, String str, Object obj) {
        try {
            MethodHandles.Lookup lookupIn = MethodHandles.Lookup.IMPL_LOOKUP.in(cls);
            if ($assertionsDisabled || refKindIsValid(i2)) {
                return lookupIn.linkMethodHandleConstant((byte) i2, cls2, str, obj);
            }
            throw new AssertionError();
        } catch (IllegalAccessException e2) {
            Throwable cause = e2.getCause();
            if (cause instanceof AbstractMethodError) {
                throw ((AbstractMethodError) cause);
            }
            throw initCauseFrom(new IllegalAccessError(e2.getMessage()), e2);
        } catch (NoSuchFieldException e3) {
            throw initCauseFrom(new NoSuchFieldError(e3.getMessage()), e3);
        } catch (NoSuchMethodException e4) {
            throw initCauseFrom(new NoSuchMethodError(e4.getMessage()), e4);
        } catch (ReflectiveOperationException e5) {
            throw initCauseFrom(new IncompatibleClassChangeError(), e5);
        }
    }

    private static Error initCauseFrom(Error error, Exception exc) {
        Throwable cause = exc.getCause();
        if (error.getClass().isInstance(cause)) {
            return (Error) cause;
        }
        error.initCause(cause == null ? exc : cause);
        return error;
    }

    static boolean isCallerSensitive(MemberName memberName) {
        if (memberName.isInvocable()) {
            return memberName.isCallerSensitive() || canBeCalledVirtual(memberName);
        }
        return false;
    }

    static boolean canBeCalledVirtual(MemberName memberName) {
        if (!$assertionsDisabled && !memberName.isInvocable()) {
            throw new AssertionError();
        }
        memberName.getDeclaringClass();
        switch (memberName.getName()) {
            case "checkMemberAccess":
                return canBeCalledVirtual(memberName, SecurityManager.class);
            case "getContextClassLoader":
                return canBeCalledVirtual(memberName, Thread.class);
            default:
                return false;
        }
    }

    static boolean canBeCalledVirtual(MemberName memberName, Class<?> cls) {
        Class<?> declaringClass = memberName.getDeclaringClass();
        if (declaringClass == cls) {
            return true;
        }
        if (memberName.isStatic() || memberName.isPrivate()) {
            return false;
        }
        return cls.isAssignableFrom(declaringClass) || declaringClass.isInterface();
    }
}
