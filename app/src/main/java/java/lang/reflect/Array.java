package java.lang.reflect;

/* loaded from: rt.jar:java/lang/reflect/Array.class */
public final class Array {
    public static native int getLength(Object obj) throws IllegalArgumentException;

    public static native Object get(Object obj, int i2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    public static native boolean getBoolean(Object obj, int i2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    public static native byte getByte(Object obj, int i2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    public static native char getChar(Object obj, int i2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    public static native short getShort(Object obj, int i2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    public static native int getInt(Object obj, int i2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    public static native long getLong(Object obj, int i2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    public static native float getFloat(Object obj, int i2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    public static native double getDouble(Object obj, int i2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    public static native void set(Object obj, int i2, Object obj2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    public static native void setBoolean(Object obj, int i2, boolean z2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    public static native void setByte(Object obj, int i2, byte b2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    public static native void setChar(Object obj, int i2, char c2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    public static native void setShort(Object obj, int i2, short s2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    public static native void setInt(Object obj, int i2, int i3) throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    public static native void setLong(Object obj, int i2, long j2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    public static native void setFloat(Object obj, int i2, float f2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    public static native void setDouble(Object obj, int i2, double d2) throws ArrayIndexOutOfBoundsException, IllegalArgumentException;

    private static native Object newArray(Class<?> cls, int i2) throws NegativeArraySizeException;

    private static native Object multiNewArray(Class<?> cls, int[] iArr) throws IllegalArgumentException, NegativeArraySizeException;

    private Array() {
    }

    public static Object newInstance(Class<?> cls, int i2) throws NegativeArraySizeException {
        return newArray(cls, i2);
    }

    public static Object newInstance(Class<?> cls, int... iArr) throws IllegalArgumentException, NegativeArraySizeException {
        return multiNewArray(cls, iArr);
    }
}
