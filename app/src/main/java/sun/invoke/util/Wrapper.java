package sun.invoke.util;

import java.lang.reflect.Array;
import java.util.Arrays;

/* loaded from: rt.jar:sun/invoke/util/Wrapper.class */
public enum Wrapper {
    BOOLEAN(Boolean.class, Boolean.TYPE, 'Z', false, new boolean[0], Format.unsigned(1)),
    BYTE(Byte.class, Byte.TYPE, 'B', (byte) 0, new byte[0], Format.signed(8)),
    SHORT(Short.class, Short.TYPE, 'S', (short) 0, new short[0], Format.signed(16)),
    CHAR(Character.class, Character.TYPE, 'C', (char) 0, new char[0], Format.unsigned(16)),
    INT(Integer.class, Integer.TYPE, 'I', 0, new int[0], Format.signed(32)),
    LONG(Long.class, Long.TYPE, 'J', 0L, new long[0], Format.signed(64)),
    FLOAT(Float.class, Float.TYPE, 'F', Float.valueOf(0.0f), new float[0], Format.floating(32)),
    DOUBLE(Double.class, Double.TYPE, 'D', Double.valueOf(0.0d), new double[0], Format.floating(64)),
    OBJECT(Object.class, Object.class, 'L', null, new Object[0], Format.other(1)),
    VOID(Void.class, Void.TYPE, 'V', null, null, Format.other(0));

    private final Class<?> wrapperType;
    private final Class<?> primitiveType;
    private final char basicTypeChar;
    private final Object zero;
    private final Object emptyArray;
    private final int format;
    private final String wrapperSimpleName;
    private final String primitiveSimpleName;
    private static final Wrapper[] FROM_PRIM;
    private static final Wrapper[] FROM_WRAP;
    private static final Wrapper[] FROM_CHAR;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Wrapper.class.desiredAssertionStatus();
        if (!$assertionsDisabled && !checkConvertibleFrom()) {
            throw new AssertionError();
        }
        FROM_PRIM = new Wrapper[16];
        FROM_WRAP = new Wrapper[16];
        FROM_CHAR = new Wrapper[16];
        for (Wrapper wrapper : values()) {
            int iHashPrim = hashPrim(wrapper.primitiveType);
            int iHashWrap = hashWrap(wrapper.wrapperType);
            int iHashChar = hashChar(wrapper.basicTypeChar);
            if (!$assertionsDisabled && FROM_PRIM[iHashPrim] != null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && FROM_WRAP[iHashWrap] != null) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && FROM_CHAR[iHashChar] != null) {
                throw new AssertionError();
            }
            FROM_PRIM[iHashPrim] = wrapper;
            FROM_WRAP[iHashWrap] = wrapper;
            FROM_CHAR[iHashChar] = wrapper;
        }
    }

    Wrapper(Class cls, Class cls2, char c2, Object obj, Object obj2, int i2) {
        this.wrapperType = cls;
        this.primitiveType = cls2;
        this.basicTypeChar = c2;
        this.zero = obj;
        this.emptyArray = obj2;
        this.format = i2;
        this.wrapperSimpleName = cls.getSimpleName();
        this.primitiveSimpleName = cls2.getSimpleName();
    }

    public String detailString() {
        return this.wrapperSimpleName + ((Object) Arrays.asList(this.wrapperType, this.primitiveType, Character.valueOf(this.basicTypeChar), this.zero, "0x" + Integer.toHexString(this.format)));
    }

    /* loaded from: rt.jar:sun/invoke/util/Wrapper$Format.class */
    private static abstract class Format {
        static final int SLOT_SHIFT = 0;
        static final int SIZE_SHIFT = 2;
        static final int KIND_SHIFT = 12;
        static final int SIGNED = -4096;
        static final int UNSIGNED = 0;
        static final int FLOATING = 4096;
        static final int SLOT_MASK = 3;
        static final int SIZE_MASK = 1023;
        static final int INT = -3967;
        static final int SHORT = -4031;
        static final int BOOLEAN = 5;
        static final int CHAR = 65;
        static final int FLOAT = 4225;
        static final int VOID = 0;
        static final int NUM_MASK = -4;
        static final /* synthetic */ boolean $assertionsDisabled;

        private Format() {
        }

        static {
            $assertionsDisabled = !Wrapper.class.desiredAssertionStatus();
        }

        static int format(int i2, int i3, int i4) {
            if (!$assertionsDisabled && ((i2 >> 12) << 12) != i2) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && (i3 & (i3 - 1)) != 0) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && (i2 != SIGNED ? i2 != 0 ? !(i2 == 4096 && (i3 == 32 || i3 == 64)) : i3 <= 0 : i3 <= 0)) {
                throw new AssertionError();
            }
            if ($assertionsDisabled || (i4 != 2 ? !(i4 != 1 || i3 > 32) : i3 == 64)) {
                return i2 | (i3 << 2) | (i4 << 0);
            }
            throw new AssertionError();
        }

        static int signed(int i2) {
            return format(SIGNED, i2, i2 > 32 ? 2 : 1);
        }

        static int unsigned(int i2) {
            return format(0, i2, i2 > 32 ? 2 : 1);
        }

        static int floating(int i2) {
            return format(4096, i2, i2 > 32 ? 2 : 1);
        }

        static int other(int i2) {
            return i2 << 0;
        }
    }

    public int bitWidth() {
        return (this.format >> 2) & 1023;
    }

    public int stackSlots() {
        return (this.format >> 0) & 3;
    }

    public boolean isSingleWord() {
        return (this.format & 1) != 0;
    }

    public boolean isDoubleWord() {
        return (this.format & 2) != 0;
    }

    public boolean isNumeric() {
        return (this.format & (-4)) != 0;
    }

    public boolean isIntegral() {
        return isNumeric() && this.format < 4225;
    }

    public boolean isSubwordOrInt() {
        return isIntegral() && isSingleWord();
    }

    public boolean isSigned() {
        return this.format < 0;
    }

    public boolean isUnsigned() {
        return this.format >= 5 && this.format < 4225;
    }

    public boolean isFloating() {
        return this.format >= 4225;
    }

    public boolean isOther() {
        return (this.format & (-4)) == 0;
    }

    public boolean isConvertibleFrom(Wrapper wrapper) {
        if (this == wrapper) {
            return true;
        }
        if (compareTo(wrapper) < 0) {
            return false;
        }
        if (!(((this.format & wrapper.format) & (-4096)) != 0)) {
            return isOther() || wrapper.format == 65;
        }
        if (!$assertionsDisabled && !isFloating() && !isSigned()) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || wrapper.isFloating() || wrapper.isSigned()) {
            return true;
        }
        throw new AssertionError();
    }

    private static boolean checkConvertibleFrom() {
        for (Wrapper wrapper : values()) {
            if (!$assertionsDisabled && !wrapper.isConvertibleFrom(wrapper)) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && !VOID.isConvertibleFrom(wrapper)) {
                throw new AssertionError();
            }
            if (wrapper != VOID) {
                if (!$assertionsDisabled && !OBJECT.isConvertibleFrom(wrapper)) {
                    throw new AssertionError();
                }
                if (!$assertionsDisabled && wrapper.isConvertibleFrom(VOID)) {
                    throw new AssertionError();
                }
            }
            if (wrapper != CHAR) {
                if (!$assertionsDisabled && CHAR.isConvertibleFrom(wrapper)) {
                    throw new AssertionError();
                }
                if (!wrapper.isConvertibleFrom(INT) && !$assertionsDisabled && wrapper.isConvertibleFrom(CHAR)) {
                    throw new AssertionError();
                }
            }
            if (wrapper != BOOLEAN) {
                if (!$assertionsDisabled && BOOLEAN.isConvertibleFrom(wrapper)) {
                    throw new AssertionError();
                }
                if (wrapper != VOID && wrapper != OBJECT && !$assertionsDisabled && wrapper.isConvertibleFrom(BOOLEAN)) {
                    throw new AssertionError();
                }
            }
            if (wrapper.isSigned()) {
                for (Wrapper wrapper2 : values()) {
                    if (wrapper != wrapper2) {
                        if (wrapper2.isFloating()) {
                            if (!$assertionsDisabled && wrapper.isConvertibleFrom(wrapper2)) {
                                throw new AssertionError();
                            }
                        } else if (!wrapper2.isSigned()) {
                            continue;
                        } else if (wrapper.compareTo(wrapper2) < 0) {
                            if (!$assertionsDisabled && wrapper.isConvertibleFrom(wrapper2)) {
                                throw new AssertionError();
                            }
                        } else if (!$assertionsDisabled && !wrapper.isConvertibleFrom(wrapper2)) {
                            throw new AssertionError();
                        }
                    }
                }
            }
            if (wrapper.isFloating()) {
                for (Wrapper wrapper3 : values()) {
                    if (wrapper != wrapper3) {
                        if (wrapper3.isSigned()) {
                            if (!$assertionsDisabled && !wrapper.isConvertibleFrom(wrapper3)) {
                                throw new AssertionError();
                            }
                        } else if (!wrapper3.isFloating()) {
                            continue;
                        } else if (wrapper.compareTo(wrapper3) < 0) {
                            if (!$assertionsDisabled && wrapper.isConvertibleFrom(wrapper3)) {
                                throw new AssertionError();
                            }
                        } else if (!$assertionsDisabled && !wrapper.isConvertibleFrom(wrapper3)) {
                            throw new AssertionError();
                        }
                    }
                }
            }
        }
        return true;
    }

    public Object zero() {
        return this.zero;
    }

    public <T> T zero(Class<T> cls) {
        return (T) convert(this.zero, cls);
    }

    public static Wrapper forPrimitiveType(Class<?> cls) {
        Wrapper wrapperFindPrimitiveType = findPrimitiveType(cls);
        if (wrapperFindPrimitiveType != null) {
            return wrapperFindPrimitiveType;
        }
        if (cls.isPrimitive()) {
            throw new InternalError();
        }
        throw newIllegalArgumentException("not primitive: " + ((Object) cls));
    }

    static Wrapper findPrimitiveType(Class<?> cls) {
        Wrapper wrapper = FROM_PRIM[hashPrim(cls)];
        if (wrapper != null && wrapper.primitiveType == cls) {
            return wrapper;
        }
        return null;
    }

    public static Wrapper forWrapperType(Class<?> cls) {
        Wrapper wrapperFindWrapperType = findWrapperType(cls);
        if (wrapperFindWrapperType != null) {
            return wrapperFindWrapperType;
        }
        for (Wrapper wrapper : values()) {
            if (wrapper.wrapperType == cls) {
                throw new InternalError();
            }
        }
        throw newIllegalArgumentException("not wrapper: " + ((Object) cls));
    }

    static Wrapper findWrapperType(Class<?> cls) {
        Wrapper wrapper = FROM_WRAP[hashWrap(cls)];
        if (wrapper != null && wrapper.wrapperType == cls) {
            return wrapper;
        }
        return null;
    }

    public static Wrapper forBasicType(char c2) {
        Wrapper wrapper = FROM_CHAR[hashChar(c2)];
        if (wrapper != null && wrapper.basicTypeChar == c2) {
            return wrapper;
        }
        for (Wrapper wrapper2 : values()) {
            if (wrapper.basicTypeChar == c2) {
                throw new InternalError();
            }
        }
        throw newIllegalArgumentException("not basic type char: " + c2);
    }

    public static Wrapper forBasicType(Class<?> cls) {
        if (cls.isPrimitive()) {
            return forPrimitiveType(cls);
        }
        return OBJECT;
    }

    private static int hashPrim(Class<?> cls) {
        String name = cls.getName();
        if (name.length() < 3) {
            return 0;
        }
        return (name.charAt(0) + name.charAt(2)) % 16;
    }

    private static int hashWrap(Class<?> cls) {
        String name = cls.getName();
        if (!$assertionsDisabled && 10 != "java.lang.".length()) {
            throw new AssertionError();
        }
        if (name.length() < 13) {
            return 0;
        }
        return ((3 * name.charAt(11)) + name.charAt(12)) % 16;
    }

    private static int hashChar(char c2) {
        return (c2 + (c2 >> 1)) % 16;
    }

    public Class<?> primitiveType() {
        return this.primitiveType;
    }

    public Class<?> wrapperType() {
        return this.wrapperType;
    }

    public <T> Class<T> wrapperType(Class<T> cls) {
        if (cls == this.wrapperType) {
            return cls;
        }
        if (cls == this.primitiveType || this.wrapperType == Object.class || cls.isInterface()) {
            return forceType(this.wrapperType, cls);
        }
        throw newClassCastException(cls, this.primitiveType);
    }

    private static ClassCastException newClassCastException(Class<?> cls, Class<?> cls2) {
        return new ClassCastException(((Object) cls) + " is not compatible with " + ((Object) cls2));
    }

    public static <T> Class<T> asWrapperType(Class<T> cls) {
        if (cls.isPrimitive()) {
            return forPrimitiveType(cls).wrapperType(cls);
        }
        return cls;
    }

    public static <T> Class<T> asPrimitiveType(Class<T> cls) {
        Wrapper wrapperFindWrapperType = findWrapperType(cls);
        if (wrapperFindWrapperType != null) {
            return forceType(wrapperFindWrapperType.primitiveType(), cls);
        }
        return cls;
    }

    public static boolean isWrapperType(Class<?> cls) {
        return findWrapperType(cls) != null;
    }

    public static boolean isPrimitiveType(Class<?> cls) {
        return cls.isPrimitive();
    }

    public static char basicTypeChar(Class<?> cls) {
        if (!cls.isPrimitive()) {
            return 'L';
        }
        return forPrimitiveType(cls).basicTypeChar();
    }

    public char basicTypeChar() {
        return this.basicTypeChar;
    }

    public String wrapperSimpleName() {
        return this.wrapperSimpleName;
    }

    public String primitiveSimpleName() {
        return this.primitiveSimpleName;
    }

    public <T> T cast(Object obj, Class<T> cls) {
        return (T) convert(obj, cls, true);
    }

    public <T> T convert(Object obj, Class<T> cls) {
        return (T) convert(obj, cls, false);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private <T> T convert(Object obj, Class<T> cls, boolean z2) {
        if (this == OBJECT) {
            if (!$assertionsDisabled && cls.isPrimitive()) {
                throw new AssertionError();
            }
            if (!cls.isInterface()) {
                cls.cast(obj);
            }
            return obj;
        }
        Class<T> clsWrapperType = wrapperType(cls);
        if (clsWrapperType.isInstance(obj)) {
            return clsWrapperType.cast(obj);
        }
        if (!z2) {
            Class<?> cls2 = obj.getClass();
            Wrapper wrapperFindWrapperType = findWrapperType(cls2);
            if (wrapperFindWrapperType == null || !isConvertibleFrom(wrapperFindWrapperType)) {
                throw newClassCastException(clsWrapperType, cls2);
            }
        } else if (obj == 0) {
            return (T) this.zero;
        }
        T t2 = (T) wrap(obj);
        if (!$assertionsDisabled) {
            if ((t2 == null ? Void.class : t2.getClass()) != clsWrapperType) {
                throw new AssertionError();
            }
        }
        return t2;
    }

    /* JADX WARN: Multi-variable type inference failed */
    static <T> Class<T> forceType(Class<?> cls, Class<T> cls2) {
        if (!(cls == cls2 || (cls.isPrimitive() && forPrimitiveType(cls) == findWrapperType(cls2)) || ((cls2.isPrimitive() && forPrimitiveType(cls2) == findWrapperType(cls)) || (cls == Object.class && !cls2.isPrimitive())))) {
            System.out.println(((Object) cls) + " <= " + ((Object) cls2));
        }
        if (!$assertionsDisabled && cls != cls2 && ((!cls.isPrimitive() || forPrimitiveType(cls) != findWrapperType(cls2)) && ((!cls2.isPrimitive() || forPrimitiveType(cls2) != findWrapperType(cls)) && (cls != Object.class || cls2.isPrimitive())))) {
            throw new AssertionError();
        }
        return cls;
    }

    public Object wrap(Object obj) {
        switch (this.basicTypeChar) {
            case 'L':
                return obj;
            case 'V':
                return null;
            default:
                Number numberNumberValue = numberValue(obj);
                switch (this.basicTypeChar) {
                    case 'B':
                        return Byte.valueOf((byte) numberNumberValue.intValue());
                    case 'C':
                        return Character.valueOf((char) numberNumberValue.intValue());
                    case 'D':
                        return Double.valueOf(numberNumberValue.doubleValue());
                    case 'E':
                    case 'G':
                    case 'H':
                    case 'K':
                    case 'L':
                    case 'M':
                    case 'N':
                    case 'O':
                    case 'P':
                    case 'Q':
                    case 'R':
                    case 'T':
                    case 'U':
                    case 'V':
                    case 'W':
                    case 'X':
                    case 'Y':
                    default:
                        throw new InternalError("bad wrapper");
                    case 'F':
                        return Float.valueOf(numberNumberValue.floatValue());
                    case 'I':
                        return Integer.valueOf(numberNumberValue.intValue());
                    case 'J':
                        return Long.valueOf(numberNumberValue.longValue());
                    case 'S':
                        return Short.valueOf((short) numberNumberValue.intValue());
                    case 'Z':
                        return Boolean.valueOf(boolValue(numberNumberValue.byteValue()));
                }
        }
    }

    public Object wrap(int i2) {
        if (this.basicTypeChar == 'L') {
            return Integer.valueOf(i2);
        }
        switch (this.basicTypeChar) {
            case 'B':
                return Byte.valueOf((byte) i2);
            case 'C':
                return Character.valueOf((char) i2);
            case 'D':
                return Double.valueOf(i2);
            case 'E':
            case 'G':
            case 'H':
            case 'K':
            case 'M':
            case 'N':
            case 'O':
            case 'P':
            case 'Q':
            case 'R':
            case 'T':
            case 'U':
            case 'W':
            case 'X':
            case 'Y':
            default:
                throw new InternalError("bad wrapper");
            case 'F':
                return Float.valueOf(i2);
            case 'I':
                return Integer.valueOf(i2);
            case 'J':
                return Long.valueOf(i2);
            case 'L':
                throw newIllegalArgumentException("cannot wrap to object type");
            case 'S':
                return Short.valueOf((short) i2);
            case 'V':
                return null;
            case 'Z':
                return Boolean.valueOf(boolValue((byte) i2));
        }
    }

    private static Number numberValue(Object obj) {
        if (obj instanceof Number) {
            return (Number) obj;
        }
        if (obj instanceof Character) {
            return Integer.valueOf(((Character) obj).charValue());
        }
        if (obj instanceof Boolean) {
            return Integer.valueOf(((Boolean) obj).booleanValue() ? 1 : 0);
        }
        return (Number) obj;
    }

    private static boolean boolValue(byte b2) {
        return ((byte) (b2 & 1)) != 0;
    }

    private static RuntimeException newIllegalArgumentException(String str, Object obj) {
        return newIllegalArgumentException(str + obj);
    }

    private static RuntimeException newIllegalArgumentException(String str) {
        return new IllegalArgumentException(str);
    }

    public Object makeArray(int i2) {
        return Array.newInstance(this.primitiveType, i2);
    }

    public Class<?> arrayType() {
        return this.emptyArray.getClass();
    }

    public void copyArrayUnboxing(Object[] objArr, int i2, Object obj, int i3, int i4) {
        if (obj.getClass() != arrayType()) {
            arrayType().cast(obj);
        }
        for (int i5 = 0; i5 < i4; i5++) {
            Array.set(obj, i5 + i3, convert(objArr[i5 + i2], this.primitiveType));
        }
    }

    public void copyArrayBoxing(Object obj, int i2, Object[] objArr, int i3, int i4) throws ArrayIndexOutOfBoundsException, IllegalArgumentException {
        if (obj.getClass() != arrayType()) {
            arrayType().cast(obj);
        }
        for (int i5 = 0; i5 < i4; i5++) {
            Object obj2 = Array.get(obj, i5 + i2);
            if (!$assertionsDisabled && obj2.getClass() != this.wrapperType) {
                throw new AssertionError();
            }
            objArr[i5 + i3] = obj2;
        }
    }
}
