package sun.misc;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;

/* loaded from: rt.jar:sun/misc/Unsafe.class */
public final class Unsafe {
    private static final Unsafe theUnsafe;
    public static final int INVALID_FIELD_OFFSET = -1;
    public static final int ARRAY_BOOLEAN_BASE_OFFSET;
    public static final int ARRAY_BYTE_BASE_OFFSET;
    public static final int ARRAY_SHORT_BASE_OFFSET;
    public static final int ARRAY_CHAR_BASE_OFFSET;
    public static final int ARRAY_INT_BASE_OFFSET;
    public static final int ARRAY_LONG_BASE_OFFSET;
    public static final int ARRAY_FLOAT_BASE_OFFSET;
    public static final int ARRAY_DOUBLE_BASE_OFFSET;
    public static final int ARRAY_OBJECT_BASE_OFFSET;
    public static final int ARRAY_BOOLEAN_INDEX_SCALE;
    public static final int ARRAY_BYTE_INDEX_SCALE;
    public static final int ARRAY_SHORT_INDEX_SCALE;
    public static final int ARRAY_CHAR_INDEX_SCALE;
    public static final int ARRAY_INT_INDEX_SCALE;
    public static final int ARRAY_LONG_INDEX_SCALE;
    public static final int ARRAY_FLOAT_INDEX_SCALE;
    public static final int ARRAY_DOUBLE_INDEX_SCALE;
    public static final int ARRAY_OBJECT_INDEX_SCALE;
    public static final int ADDRESS_SIZE;

    private static native void registerNatives();

    public native int getInt(Object obj, long j2);

    public native void putInt(Object obj, long j2, int i2);

    public native Object getObject(Object obj, long j2);

    public native void putObject(Object obj, long j2, Object obj2);

    public native boolean getBoolean(Object obj, long j2);

    public native void putBoolean(Object obj, long j2, boolean z2);

    public native byte getByte(Object obj, long j2);

    public native void putByte(Object obj, long j2, byte b2);

    public native short getShort(Object obj, long j2);

    public native void putShort(Object obj, long j2, short s2);

    public native char getChar(Object obj, long j2);

    public native void putChar(Object obj, long j2, char c2);

    public native long getLong(Object obj, long j2);

    public native void putLong(Object obj, long j2, long j3);

    public native float getFloat(Object obj, long j2);

    public native void putFloat(Object obj, long j2, float f2);

    public native double getDouble(Object obj, long j2);

    public native void putDouble(Object obj, long j2, double d2);

    public native byte getByte(long j2);

    public native void putByte(long j2, byte b2);

    public native short getShort(long j2);

    public native void putShort(long j2, short s2);

    public native char getChar(long j2);

    public native void putChar(long j2, char c2);

    public native int getInt(long j2);

    public native void putInt(long j2, int i2);

    public native long getLong(long j2);

    public native void putLong(long j2, long j3);

    public native float getFloat(long j2);

    public native void putFloat(long j2, float f2);

    public native double getDouble(long j2);

    public native void putDouble(long j2, double d2);

    public native long getAddress(long j2);

    public native void putAddress(long j2, long j3);

    public native long allocateMemory(long j2);

    public native long reallocateMemory(long j2, long j3);

    public native void setMemory(Object obj, long j2, long j3, byte b2);

    public native void copyMemory(Object obj, long j2, Object obj2, long j3, long j4);

    public native void freeMemory(long j2);

    public native long staticFieldOffset(Field field);

    public native long objectFieldOffset(Field field);

    public native Object staticFieldBase(Field field);

    public native boolean shouldBeInitialized(Class<?> cls);

    public native void ensureClassInitialized(Class<?> cls);

    public native int arrayBaseOffset(Class<?> cls);

    public native int arrayIndexScale(Class<?> cls);

    public native int addressSize();

    public native int pageSize();

    public native Class<?> defineClass(String str, byte[] bArr, int i2, int i3, ClassLoader classLoader, ProtectionDomain protectionDomain);

    public native Class<?> defineAnonymousClass(Class<?> cls, byte[] bArr, Object[] objArr);

    public native Object allocateInstance(Class<?> cls) throws InstantiationException;

    @Deprecated
    public native void monitorEnter(Object obj);

    @Deprecated
    public native void monitorExit(Object obj);

    @Deprecated
    public native boolean tryMonitorEnter(Object obj);

    public native void throwException(Throwable th);

    public final native boolean compareAndSwapObject(Object obj, long j2, Object obj2, Object obj3);

    public final native boolean compareAndSwapInt(Object obj, long j2, int i2, int i3);

    public final native boolean compareAndSwapLong(Object obj, long j2, long j3, long j4);

    public native Object getObjectVolatile(Object obj, long j2);

    public native void putObjectVolatile(Object obj, long j2, Object obj2);

    public native int getIntVolatile(Object obj, long j2);

    public native void putIntVolatile(Object obj, long j2, int i2);

    public native boolean getBooleanVolatile(Object obj, long j2);

    public native void putBooleanVolatile(Object obj, long j2, boolean z2);

    public native byte getByteVolatile(Object obj, long j2);

    public native void putByteVolatile(Object obj, long j2, byte b2);

    public native short getShortVolatile(Object obj, long j2);

    public native void putShortVolatile(Object obj, long j2, short s2);

    public native char getCharVolatile(Object obj, long j2);

    public native void putCharVolatile(Object obj, long j2, char c2);

    public native long getLongVolatile(Object obj, long j2);

    public native void putLongVolatile(Object obj, long j2, long j3);

    public native float getFloatVolatile(Object obj, long j2);

    public native void putFloatVolatile(Object obj, long j2, float f2);

    public native double getDoubleVolatile(Object obj, long j2);

    public native void putDoubleVolatile(Object obj, long j2, double d2);

    public native void putOrderedObject(Object obj, long j2, Object obj2);

    public native void putOrderedInt(Object obj, long j2, int i2);

    public native void putOrderedLong(Object obj, long j2, long j3);

    public native void unpark(Object obj);

    public native void park(boolean z2, long j2);

    public native int getLoadAverage(double[] dArr, int i2);

    public native void loadFence();

    public native void storeFence();

    public native void fullFence();

    static {
        registerNatives();
        Reflection.registerMethodsToFilter(Unsafe.class, "getUnsafe");
        theUnsafe = new Unsafe();
        ARRAY_BOOLEAN_BASE_OFFSET = theUnsafe.arrayBaseOffset(boolean[].class);
        ARRAY_BYTE_BASE_OFFSET = theUnsafe.arrayBaseOffset(byte[].class);
        ARRAY_SHORT_BASE_OFFSET = theUnsafe.arrayBaseOffset(short[].class);
        ARRAY_CHAR_BASE_OFFSET = theUnsafe.arrayBaseOffset(char[].class);
        ARRAY_INT_BASE_OFFSET = theUnsafe.arrayBaseOffset(int[].class);
        ARRAY_LONG_BASE_OFFSET = theUnsafe.arrayBaseOffset(long[].class);
        ARRAY_FLOAT_BASE_OFFSET = theUnsafe.arrayBaseOffset(float[].class);
        ARRAY_DOUBLE_BASE_OFFSET = theUnsafe.arrayBaseOffset(double[].class);
        ARRAY_OBJECT_BASE_OFFSET = theUnsafe.arrayBaseOffset(Object[].class);
        ARRAY_BOOLEAN_INDEX_SCALE = theUnsafe.arrayIndexScale(boolean[].class);
        ARRAY_BYTE_INDEX_SCALE = theUnsafe.arrayIndexScale(byte[].class);
        ARRAY_SHORT_INDEX_SCALE = theUnsafe.arrayIndexScale(short[].class);
        ARRAY_CHAR_INDEX_SCALE = theUnsafe.arrayIndexScale(char[].class);
        ARRAY_INT_INDEX_SCALE = theUnsafe.arrayIndexScale(int[].class);
        ARRAY_LONG_INDEX_SCALE = theUnsafe.arrayIndexScale(long[].class);
        ARRAY_FLOAT_INDEX_SCALE = theUnsafe.arrayIndexScale(float[].class);
        ARRAY_DOUBLE_INDEX_SCALE = theUnsafe.arrayIndexScale(double[].class);
        ARRAY_OBJECT_INDEX_SCALE = theUnsafe.arrayIndexScale(Object[].class);
        ADDRESS_SIZE = theUnsafe.addressSize();
    }

    private Unsafe() {
    }

    @CallerSensitive
    public static Unsafe getUnsafe() {
        if (!VM.isSystemDomainLoader(Reflection.getCallerClass().getClassLoader())) {
            throw new SecurityException("Unsafe");
        }
        return theUnsafe;
    }

    @Deprecated
    public int getInt(Object obj, int i2) {
        return getInt(obj, i2);
    }

    @Deprecated
    public void putInt(Object obj, int i2, int i3) {
        putInt(obj, i2, i3);
    }

    @Deprecated
    public Object getObject(Object obj, int i2) {
        return getObject(obj, i2);
    }

    @Deprecated
    public void putObject(Object obj, int i2, Object obj2) {
        putObject(obj, i2, obj2);
    }

    @Deprecated
    public boolean getBoolean(Object obj, int i2) {
        return getBoolean(obj, i2);
    }

    @Deprecated
    public void putBoolean(Object obj, int i2, boolean z2) {
        putBoolean(obj, i2, z2);
    }

    @Deprecated
    public byte getByte(Object obj, int i2) {
        return getByte(obj, i2);
    }

    @Deprecated
    public void putByte(Object obj, int i2, byte b2) {
        putByte(obj, i2, b2);
    }

    @Deprecated
    public short getShort(Object obj, int i2) {
        return getShort(obj, i2);
    }

    @Deprecated
    public void putShort(Object obj, int i2, short s2) {
        putShort(obj, i2, s2);
    }

    @Deprecated
    public char getChar(Object obj, int i2) {
        return getChar(obj, i2);
    }

    @Deprecated
    public void putChar(Object obj, int i2, char c2) {
        putChar(obj, i2, c2);
    }

    @Deprecated
    public long getLong(Object obj, int i2) {
        return getLong(obj, i2);
    }

    @Deprecated
    public void putLong(Object obj, int i2, long j2) {
        putLong(obj, i2, j2);
    }

    @Deprecated
    public float getFloat(Object obj, int i2) {
        return getFloat(obj, i2);
    }

    @Deprecated
    public void putFloat(Object obj, int i2, float f2) {
        putFloat(obj, i2, f2);
    }

    @Deprecated
    public double getDouble(Object obj, int i2) {
        return getDouble(obj, i2);
    }

    @Deprecated
    public void putDouble(Object obj, int i2, double d2) {
        putDouble(obj, i2, d2);
    }

    public void setMemory(long j2, long j3, byte b2) {
        setMemory(null, j2, j3, b2);
    }

    public void copyMemory(long j2, long j3, long j4) {
        copyMemory(null, j2, null, j3, j4);
    }

    @Deprecated
    public int fieldOffset(Field field) {
        if (Modifier.isStatic(field.getModifiers())) {
            return (int) staticFieldOffset(field);
        }
        return (int) objectFieldOffset(field);
    }

    @Deprecated
    public Object staticFieldBase(Class<?> cls) throws SecurityException {
        Field[] declaredFields = cls.getDeclaredFields();
        for (int i2 = 0; i2 < declaredFields.length; i2++) {
            if (Modifier.isStatic(declaredFields[i2].getModifiers())) {
                return staticFieldBase(declaredFields[i2]);
            }
        }
        return null;
    }

    public final int getAndAddInt(Object obj, long j2, int i2) {
        int intVolatile;
        do {
            intVolatile = getIntVolatile(obj, j2);
        } while (!compareAndSwapInt(obj, j2, intVolatile, intVolatile + i2));
        return intVolatile;
    }

    public final long getAndAddLong(Object obj, long j2, long j3) {
        long longVolatile;
        do {
            longVolatile = getLongVolatile(obj, j2);
        } while (!compareAndSwapLong(obj, j2, longVolatile, longVolatile + j3));
        return longVolatile;
    }

    public final int getAndSetInt(Object obj, long j2, int i2) {
        int intVolatile;
        do {
            intVolatile = getIntVolatile(obj, j2);
        } while (!compareAndSwapInt(obj, j2, intVolatile, i2));
        return intVolatile;
    }

    public final long getAndSetLong(Object obj, long j2, long j3) {
        long longVolatile;
        do {
            longVolatile = getLongVolatile(obj, j2);
        } while (!compareAndSwapLong(obj, j2, longVolatile, j3));
        return longVolatile;
    }

    public final Object getAndSetObject(Object obj, long j2, Object obj2) {
        Object objectVolatile;
        do {
            objectVolatile = getObjectVolatile(obj, j2);
        } while (!compareAndSwapObject(obj, j2, objectVolatile, obj2));
        return objectVolatile;
    }

    private static void throwIllegalAccessError() {
        throw new IllegalAccessError();
    }
}
