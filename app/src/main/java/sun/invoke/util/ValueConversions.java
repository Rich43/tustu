package sun.invoke.util;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.EnumMap;

/* loaded from: rt.jar:sun/invoke/util/ValueConversions.class */
public class ValueConversions {
    private static final Class<?> THIS_CLASS;
    private static final MethodHandles.Lookup IMPL_LOOKUP;
    private static final WrapperCache[] UNBOX_CONVERSIONS;
    private static final Integer ZERO_INT;
    private static final Integer ONE_INT;
    private static final WrapperCache[] BOX_CONVERSIONS;
    private static final WrapperCache[] CONSTANT_FUNCTIONS;
    private static final MethodHandle CAST_REFERENCE;
    private static final MethodHandle IGNORE;
    private static final MethodHandle EMPTY;
    private static final WrapperCache[] CONVERT_PRIMITIVE_FUNCTIONS;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !ValueConversions.class.desiredAssertionStatus();
        THIS_CLASS = ValueConversions.class;
        IMPL_LOOKUP = MethodHandles.lookup();
        UNBOX_CONVERSIONS = newWrapperCaches(4);
        ZERO_INT = 0;
        ONE_INT = 1;
        BOX_CONVERSIONS = newWrapperCaches(1);
        CONSTANT_FUNCTIONS = newWrapperCaches(2);
        try {
            MethodType methodTypeGenericMethodType = MethodType.genericMethodType(1);
            MethodType methodTypeChangeReturnType = methodTypeGenericMethodType.changeReturnType(Void.TYPE);
            CAST_REFERENCE = IMPL_LOOKUP.findVirtual(Class.class, "cast", methodTypeGenericMethodType);
            IGNORE = IMPL_LOOKUP.findStatic(THIS_CLASS, "ignore", methodTypeChangeReturnType);
            EMPTY = IMPL_LOOKUP.findStatic(THIS_CLASS, Constants.ELEMNAME_EMPTY_STRING, methodTypeChangeReturnType.dropParameterTypes(0, 1));
            CONVERT_PRIMITIVE_FUNCTIONS = newWrapperCaches(Wrapper.values().length);
        } catch (IllegalAccessException | NoSuchMethodException e2) {
            throw newInternalError("uncaught exception", e2);
        }
    }

    /* loaded from: rt.jar:sun/invoke/util/ValueConversions$WrapperCache.class */
    private static class WrapperCache {
        private final EnumMap<Wrapper, MethodHandle> map;

        private WrapperCache() {
            this.map = new EnumMap<>(Wrapper.class);
        }

        public MethodHandle get(Wrapper wrapper) {
            return this.map.get(wrapper);
        }

        public synchronized MethodHandle put(Wrapper wrapper, MethodHandle methodHandle) {
            MethodHandle methodHandlePutIfAbsent = this.map.putIfAbsent(wrapper, methodHandle);
            return methodHandlePutIfAbsent != null ? methodHandlePutIfAbsent : methodHandle;
        }
    }

    private static WrapperCache[] newWrapperCaches(int i2) {
        WrapperCache[] wrapperCacheArr = new WrapperCache[i2];
        for (int i3 = 0; i3 < i2; i3++) {
            wrapperCacheArr[i3] = new WrapperCache();
        }
        return wrapperCacheArr;
    }

    static int unboxInteger(Integer num) {
        return num.intValue();
    }

    static int unboxInteger(Object obj, boolean z2) {
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        return primitiveConversion(Wrapper.INT, obj, z2).intValue();
    }

    static byte unboxByte(Byte b2) {
        return b2.byteValue();
    }

    static byte unboxByte(Object obj, boolean z2) {
        if (obj instanceof Byte) {
            return ((Byte) obj).byteValue();
        }
        return primitiveConversion(Wrapper.BYTE, obj, z2).byteValue();
    }

    static short unboxShort(Short sh) {
        return sh.shortValue();
    }

    static short unboxShort(Object obj, boolean z2) {
        if (obj instanceof Short) {
            return ((Short) obj).shortValue();
        }
        return primitiveConversion(Wrapper.SHORT, obj, z2).shortValue();
    }

    static boolean unboxBoolean(Boolean bool) {
        return bool.booleanValue();
    }

    static boolean unboxBoolean(Object obj, boolean z2) {
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        return (primitiveConversion(Wrapper.BOOLEAN, obj, z2).intValue() & 1) != 0;
    }

    static char unboxCharacter(Character ch) {
        return ch.charValue();
    }

    static char unboxCharacter(Object obj, boolean z2) {
        if (obj instanceof Character) {
            return ((Character) obj).charValue();
        }
        return (char) primitiveConversion(Wrapper.CHAR, obj, z2).intValue();
    }

    static long unboxLong(Long l2) {
        return l2.longValue();
    }

    static long unboxLong(Object obj, boolean z2) {
        if (obj instanceof Long) {
            return ((Long) obj).longValue();
        }
        return primitiveConversion(Wrapper.LONG, obj, z2).longValue();
    }

    static float unboxFloat(Float f2) {
        return f2.floatValue();
    }

    static float unboxFloat(Object obj, boolean z2) {
        if (obj instanceof Float) {
            return ((Float) obj).floatValue();
        }
        return primitiveConversion(Wrapper.FLOAT, obj, z2).floatValue();
    }

    static double unboxDouble(Double d2) {
        return d2.doubleValue();
    }

    static double unboxDouble(Object obj, boolean z2) {
        if (obj instanceof Double) {
            return ((Double) obj).doubleValue();
        }
        return primitiveConversion(Wrapper.DOUBLE, obj, z2).doubleValue();
    }

    private static MethodType unboxType(Wrapper wrapper, int i2) {
        return i2 == 0 ? MethodType.methodType(wrapper.primitiveType(), wrapper.wrapperType()) : MethodType.methodType(wrapper.primitiveType(), Object.class, Boolean.TYPE);
    }

    private static MethodHandle unbox(Wrapper wrapper, int i2) throws RuntimeException {
        MethodHandle methodHandleAsType;
        WrapperCache wrapperCache = UNBOX_CONVERSIONS[i2];
        MethodHandle methodHandle = wrapperCache.get(wrapper);
        if (methodHandle != null) {
            return methodHandle;
        }
        switch (wrapper) {
            case OBJECT:
            case VOID:
                throw new IllegalArgumentException("unbox " + ((Object) wrapper));
            default:
                try {
                    methodHandleAsType = IMPL_LOOKUP.findStatic(THIS_CLASS, "unbox" + wrapper.wrapperSimpleName(), unboxType(wrapper, i2));
                } catch (ReflectiveOperationException e2) {
                    methodHandleAsType = null;
                }
                if (methodHandleAsType != null) {
                    if (i2 > 0) {
                        methodHandleAsType = MethodHandles.insertArguments(methodHandleAsType, 1, Boolean.valueOf(i2 != 2));
                    }
                    if (i2 == 1) {
                        methodHandleAsType = methodHandleAsType.asType(unboxType(wrapper, 0));
                    }
                    return wrapperCache.put(wrapper, methodHandleAsType);
                }
                throw new IllegalArgumentException("cannot find unbox adapter for " + ((Object) wrapper) + (i2 <= 1 ? " (exact)" : i2 == 3 ? " (cast)" : ""));
        }
    }

    public static MethodHandle unboxExact(Wrapper wrapper) {
        return unbox(wrapper, 0);
    }

    public static MethodHandle unboxExact(Wrapper wrapper, boolean z2) {
        return unbox(wrapper, z2 ? 0 : 1);
    }

    public static MethodHandle unboxWiden(Wrapper wrapper) {
        return unbox(wrapper, 2);
    }

    public static MethodHandle unboxCast(Wrapper wrapper) {
        return unbox(wrapper, 3);
    }

    public static Number primitiveConversion(Wrapper wrapper, Object obj, boolean z2) {
        Number numberValueOf;
        if (obj == null) {
            if (z2) {
                return ZERO_INT;
            }
            return null;
        }
        if (obj instanceof Number) {
            numberValueOf = (Number) obj;
        } else if (obj instanceof Boolean) {
            numberValueOf = ((Boolean) obj).booleanValue() ? ONE_INT : ZERO_INT;
        } else if (obj instanceof Character) {
            numberValueOf = Integer.valueOf(((Character) obj).charValue());
        } else {
            numberValueOf = (Number) obj;
        }
        Wrapper wrapperFindWrapperType = Wrapper.findWrapperType(obj.getClass());
        if (wrapperFindWrapperType == null || (!z2 && !wrapper.isConvertibleFrom(wrapperFindWrapperType))) {
            return (Number) wrapper.wrapperType().cast(obj);
        }
        return numberValueOf;
    }

    public static int widenSubword(Object obj) {
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        if (obj instanceof Boolean) {
            return fromBoolean(((Boolean) obj).booleanValue());
        }
        if (obj instanceof Character) {
            return ((Character) obj).charValue();
        }
        if (obj instanceof Short) {
            return ((Short) obj).shortValue();
        }
        if (obj instanceof Byte) {
            return ((Byte) obj).byteValue();
        }
        return ((Integer) obj).intValue();
    }

    static Integer boxInteger(int i2) {
        return Integer.valueOf(i2);
    }

    static Byte boxByte(byte b2) {
        return Byte.valueOf(b2);
    }

    static Short boxShort(short s2) {
        return Short.valueOf(s2);
    }

    static Boolean boxBoolean(boolean z2) {
        return Boolean.valueOf(z2);
    }

    static Character boxCharacter(char c2) {
        return Character.valueOf(c2);
    }

    static Long boxLong(long j2) {
        return Long.valueOf(j2);
    }

    static Float boxFloat(float f2) {
        return Float.valueOf(f2);
    }

    static Double boxDouble(double d2) {
        return Double.valueOf(d2);
    }

    private static MethodType boxType(Wrapper wrapper) {
        return MethodType.methodType(wrapper.wrapperType(), wrapper.primitiveType());
    }

    public static MethodHandle boxExact(Wrapper wrapper) {
        MethodHandle methodHandleFindStatic;
        WrapperCache wrapperCache = BOX_CONVERSIONS[0];
        MethodHandle methodHandle = wrapperCache.get(wrapper);
        if (methodHandle != null) {
            return methodHandle;
        }
        try {
            methodHandleFindStatic = IMPL_LOOKUP.findStatic(THIS_CLASS, "box" + wrapper.wrapperSimpleName(), boxType(wrapper));
        } catch (ReflectiveOperationException e2) {
            methodHandleFindStatic = null;
        }
        if (methodHandleFindStatic != null) {
            return wrapperCache.put(wrapper, methodHandleFindStatic);
        }
        throw new IllegalArgumentException("cannot find box adapter for " + ((Object) wrapper));
    }

    static void ignore(Object obj) {
    }

    static void empty() {
    }

    static Object zeroObject() {
        return null;
    }

    static int zeroInteger() {
        return 0;
    }

    static long zeroLong() {
        return 0L;
    }

    static float zeroFloat() {
        return 0.0f;
    }

    static double zeroDouble() {
        return 0.0d;
    }

    public static MethodHandle zeroConstantFunction(Wrapper wrapper) {
        WrapperCache wrapperCache = CONSTANT_FUNCTIONS[0];
        MethodHandle methodHandleFindStatic = wrapperCache.get(wrapper);
        if (methodHandleFindStatic != null) {
            return methodHandleFindStatic;
        }
        MethodType methodType = MethodType.methodType(wrapper.primitiveType());
        switch (wrapper) {
            case OBJECT:
            case INT:
            case LONG:
            case FLOAT:
            case DOUBLE:
                try {
                    methodHandleFindStatic = IMPL_LOOKUP.findStatic(THIS_CLASS, "zero" + wrapper.wrapperSimpleName(), methodType);
                    break;
                } catch (ReflectiveOperationException e2) {
                    methodHandleFindStatic = null;
                    break;
                }
            case VOID:
                methodHandleFindStatic = EMPTY;
                break;
        }
        if (methodHandleFindStatic != null) {
            return wrapperCache.put(wrapper, methodHandleFindStatic);
        }
        if (wrapper.isSubwordOrInt() && wrapper != Wrapper.INT) {
            return wrapperCache.put(wrapper, MethodHandles.explicitCastArguments(zeroConstantFunction(Wrapper.INT), methodType));
        }
        throw new IllegalArgumentException("cannot find zero constant for " + ((Object) wrapper));
    }

    public static MethodHandle ignore() {
        return IGNORE;
    }

    public static MethodHandle cast() {
        return CAST_REFERENCE;
    }

    static float doubleToFloat(double d2) {
        return (float) d2;
    }

    static long doubleToLong(double d2) {
        return (long) d2;
    }

    static int doubleToInt(double d2) {
        return (int) d2;
    }

    static short doubleToShort(double d2) {
        return (short) d2;
    }

    static char doubleToChar(double d2) {
        return (char) d2;
    }

    static byte doubleToByte(double d2) {
        return (byte) d2;
    }

    static boolean doubleToBoolean(double d2) {
        return toBoolean((byte) d2);
    }

    static double floatToDouble(float f2) {
        return f2;
    }

    static long floatToLong(float f2) {
        return (long) f2;
    }

    static int floatToInt(float f2) {
        return (int) f2;
    }

    static short floatToShort(float f2) {
        return (short) f2;
    }

    static char floatToChar(float f2) {
        return (char) f2;
    }

    static byte floatToByte(float f2) {
        return (byte) f2;
    }

    static boolean floatToBoolean(float f2) {
        return toBoolean((byte) f2);
    }

    static double longToDouble(long j2) {
        return j2;
    }

    static float longToFloat(long j2) {
        return j2;
    }

    static int longToInt(long j2) {
        return (int) j2;
    }

    static short longToShort(long j2) {
        return (short) j2;
    }

    static char longToChar(long j2) {
        return (char) j2;
    }

    static byte longToByte(long j2) {
        return (byte) j2;
    }

    static boolean longToBoolean(long j2) {
        return toBoolean((byte) j2);
    }

    static double intToDouble(int i2) {
        return i2;
    }

    static float intToFloat(int i2) {
        return i2;
    }

    static long intToLong(int i2) {
        return i2;
    }

    static short intToShort(int i2) {
        return (short) i2;
    }

    static char intToChar(int i2) {
        return (char) i2;
    }

    static byte intToByte(int i2) {
        return (byte) i2;
    }

    static boolean intToBoolean(int i2) {
        return toBoolean((byte) i2);
    }

    static double shortToDouble(short s2) {
        return s2;
    }

    static float shortToFloat(short s2) {
        return s2;
    }

    static long shortToLong(short s2) {
        return s2;
    }

    static int shortToInt(short s2) {
        return s2;
    }

    static char shortToChar(short s2) {
        return (char) s2;
    }

    static byte shortToByte(short s2) {
        return (byte) s2;
    }

    static boolean shortToBoolean(short s2) {
        return toBoolean((byte) s2);
    }

    static double charToDouble(char c2) {
        return c2;
    }

    static float charToFloat(char c2) {
        return c2;
    }

    static long charToLong(char c2) {
        return c2;
    }

    static int charToInt(char c2) {
        return c2;
    }

    static short charToShort(char c2) {
        return (short) c2;
    }

    static byte charToByte(char c2) {
        return (byte) c2;
    }

    static boolean charToBoolean(char c2) {
        return toBoolean((byte) c2);
    }

    static double byteToDouble(byte b2) {
        return b2;
    }

    static float byteToFloat(byte b2) {
        return b2;
    }

    static long byteToLong(byte b2) {
        return b2;
    }

    static int byteToInt(byte b2) {
        return b2;
    }

    static short byteToShort(byte b2) {
        return b2;
    }

    static char byteToChar(byte b2) {
        return (char) b2;
    }

    static boolean byteToBoolean(byte b2) {
        return toBoolean(b2);
    }

    static double booleanToDouble(boolean z2) {
        return fromBoolean(z2);
    }

    static float booleanToFloat(boolean z2) {
        return fromBoolean(z2);
    }

    static long booleanToLong(boolean z2) {
        return fromBoolean(z2);
    }

    static int booleanToInt(boolean z2) {
        return fromBoolean(z2);
    }

    static short booleanToShort(boolean z2) {
        return fromBoolean(z2);
    }

    static char booleanToChar(boolean z2) {
        return (char) fromBoolean(z2);
    }

    static byte booleanToByte(boolean z2) {
        return fromBoolean(z2);
    }

    static boolean toBoolean(byte b2) {
        return (b2 & 1) != 0;
    }

    static byte fromBoolean(boolean z2) {
        return z2 ? (byte) 1 : (byte) 0;
    }

    public static MethodHandle convertPrimitive(Wrapper wrapper, Wrapper wrapper2) {
        MethodHandle methodHandleFindStatic;
        WrapperCache wrapperCache = CONVERT_PRIMITIVE_FUNCTIONS[wrapper.ordinal()];
        MethodHandle methodHandle = wrapperCache.get(wrapper2);
        if (methodHandle != null) {
            return methodHandle;
        }
        Class<?> clsPrimitiveType = wrapper.primitiveType();
        Class<?> clsPrimitiveType2 = wrapper2.primitiveType();
        MethodType methodType = MethodType.methodType(clsPrimitiveType2, clsPrimitiveType);
        if (wrapper == wrapper2) {
            methodHandleFindStatic = MethodHandles.identity(clsPrimitiveType);
        } else {
            if (!$assertionsDisabled && (!clsPrimitiveType.isPrimitive() || !clsPrimitiveType2.isPrimitive())) {
                throw new AssertionError();
            }
            try {
                methodHandleFindStatic = IMPL_LOOKUP.findStatic(THIS_CLASS, clsPrimitiveType.getSimpleName() + "To" + capitalize(clsPrimitiveType2.getSimpleName()), methodType);
            } catch (ReflectiveOperationException e2) {
                methodHandleFindStatic = null;
            }
        }
        if (methodHandleFindStatic != null) {
            if ($assertionsDisabled || methodHandleFindStatic.type() == methodType) {
                return wrapperCache.put(wrapper2, methodHandleFindStatic);
            }
            throw new AssertionError(methodHandleFindStatic);
        }
        throw new IllegalArgumentException("cannot find primitive conversion function for " + clsPrimitiveType.getSimpleName() + " -> " + clsPrimitiveType2.getSimpleName());
    }

    public static MethodHandle convertPrimitive(Class<?> cls, Class<?> cls2) {
        return convertPrimitive(Wrapper.forPrimitiveType(cls), Wrapper.forPrimitiveType(cls2));
    }

    private static String capitalize(String str) {
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    private static InternalError newInternalError(String str, Throwable th) {
        return new InternalError(str, th);
    }

    private static InternalError newInternalError(Throwable th) {
        return new InternalError(th);
    }
}
