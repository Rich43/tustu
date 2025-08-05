package sun.invoke.util;

import java.lang.invoke.MethodType;
import sun.invoke.empty.Empty;

/* loaded from: rt.jar:sun/invoke/util/VerifyType.class */
public class VerifyType {
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !VerifyType.class.desiredAssertionStatus();
    }

    private VerifyType() {
    }

    public static boolean isNullConversion(Class<?> cls, Class<?> cls2, boolean z2) {
        if (cls == cls2) {
            return true;
        }
        if (!z2) {
            if (cls2.isInterface()) {
                cls2 = Object.class;
            }
            if (cls.isInterface()) {
                cls = Object.class;
            }
            if (cls == cls2) {
                return true;
            }
        }
        if (isNullType(cls)) {
            return !cls2.isPrimitive();
        }
        if (!cls.isPrimitive()) {
            return cls2.isAssignableFrom(cls);
        }
        if (!cls2.isPrimitive()) {
            return false;
        }
        Wrapper wrapperForPrimitiveType = Wrapper.forPrimitiveType(cls);
        if (cls2 == Integer.TYPE) {
            return wrapperForPrimitiveType.isSubwordOrInt();
        }
        Wrapper wrapperForPrimitiveType2 = Wrapper.forPrimitiveType(cls2);
        if (wrapperForPrimitiveType.isSubwordOrInt() && wrapperForPrimitiveType2.isSubwordOrInt()) {
            return (wrapperForPrimitiveType2.isSigned() || !wrapperForPrimitiveType.isSigned()) && wrapperForPrimitiveType2.bitWidth() > wrapperForPrimitiveType.bitWidth();
        }
        return false;
    }

    public static boolean isNullReferenceConversion(Class<?> cls, Class<?> cls2) {
        if (!$assertionsDisabled && cls2.isPrimitive()) {
            throw new AssertionError();
        }
        if (cls2.isInterface() || isNullType(cls)) {
            return true;
        }
        return cls2.isAssignableFrom(cls);
    }

    public static boolean isNullType(Class<?> cls) {
        return cls == Void.class || cls == Empty.class;
    }

    public static boolean isNullConversion(MethodType methodType, MethodType methodType2, boolean z2) {
        if (methodType == methodType2) {
            return true;
        }
        int iParameterCount = methodType.parameterCount();
        if (iParameterCount != methodType2.parameterCount()) {
            return false;
        }
        for (int i2 = 0; i2 < iParameterCount; i2++) {
            if (!isNullConversion(methodType.parameterType(i2), methodType2.parameterType(i2), z2)) {
                return false;
            }
        }
        return isNullConversion(methodType2.returnType(), methodType.returnType(), z2);
    }

    public static int canPassUnchecked(Class<?> cls, Class<?> cls2) {
        if (cls == cls2) {
            return 1;
        }
        if (cls2.isPrimitive()) {
            if (cls2 == Void.TYPE) {
                return 1;
            }
            if (cls == Void.TYPE || !cls.isPrimitive()) {
                return 0;
            }
            Wrapper wrapperForPrimitiveType = Wrapper.forPrimitiveType(cls);
            Wrapper wrapperForPrimitiveType2 = Wrapper.forPrimitiveType(cls2);
            if (wrapperForPrimitiveType.isSubwordOrInt() && wrapperForPrimitiveType2.isSubwordOrInt()) {
                if (wrapperForPrimitiveType.bitWidth() >= wrapperForPrimitiveType2.bitWidth()) {
                    return -1;
                }
                if (!wrapperForPrimitiveType2.isSigned() && wrapperForPrimitiveType.isSigned()) {
                    return -1;
                }
                return 1;
            }
            if (cls == Float.TYPE || cls2 == Float.TYPE) {
                if (cls == Double.TYPE || cls2 == Double.TYPE) {
                    return -1;
                }
                return 0;
            }
            return 0;
        }
        if (cls.isPrimitive()) {
            return 0;
        }
        if (isNullReferenceConversion(cls, cls2)) {
            return 1;
        }
        return -1;
    }

    public static boolean isSpreadArgType(Class<?> cls) {
        return cls.isArray();
    }

    public static Class<?> spreadArgElementType(Class<?> cls, int i2) {
        return cls.getComponentType();
    }
}
