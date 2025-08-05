package java.lang.invoke;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.Objects;

/* loaded from: rt.jar:java/lang/invoke/MethodHandleInfo.class */
public interface MethodHandleInfo {
    public static final int REF_getField = 1;
    public static final int REF_getStatic = 2;
    public static final int REF_putField = 3;
    public static final int REF_putStatic = 4;
    public static final int REF_invokeVirtual = 5;
    public static final int REF_invokeStatic = 6;
    public static final int REF_invokeSpecial = 7;
    public static final int REF_newInvokeSpecial = 8;
    public static final int REF_invokeInterface = 9;

    int getReferenceKind();

    Class<?> getDeclaringClass();

    String getName();

    MethodType getMethodType();

    <T extends Member> T reflectAs(Class<T> cls, MethodHandles.Lookup lookup);

    int getModifiers();

    default boolean isVarArgs() {
        if (MethodHandleNatives.refKindIsField((byte) getReferenceKind())) {
            return false;
        }
        return Modifier.isTransient(getModifiers());
    }

    static String referenceKindToString(int i2) {
        if (!MethodHandleNatives.refKindIsValid(i2)) {
            throw MethodHandleStatics.newIllegalArgumentException("invalid reference kind", Integer.valueOf(i2));
        }
        return MethodHandleNatives.refKindName((byte) i2);
    }

    static String toString(int i2, Class<?> cls, String str, MethodType methodType) {
        Objects.requireNonNull(str);
        Objects.requireNonNull(methodType);
        return String.format("%s %s.%s:%s", referenceKindToString(i2), cls.getName(), str, methodType);
    }
}
