package com.sun.jna;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/* loaded from: JavaFTD2XX.jar:com/sun/jna/Pointer.class */
public class Pointer {
    public static final int SIZE;
    public static final Pointer NULL;
    protected long peer;
    static Class class$com$sun$jna$Structure;
    static Class class$com$sun$jna$Structure$ByReference;
    static Class class$java$lang$Boolean;
    static Class class$java$lang$Byte;
    static Class class$java$lang$Short;
    static Class class$java$lang$Character;
    static Class class$java$lang$Integer;
    static Class class$java$lang$Long;
    static Class class$java$lang$Float;
    static Class class$java$lang$Double;
    static Class class$com$sun$jna$Pointer;
    static Class class$java$lang$String;
    static Class class$com$sun$jna$WString;
    static Class class$com$sun$jna$Callback;
    static Class class$java$nio$Buffer;
    static Class class$com$sun$jna$NativeMapped;

    /* renamed from: com.sun.jna.Pointer$1, reason: invalid class name */
    /* loaded from: JavaFTD2XX.jar:com/sun/jna/Pointer$1.class */
    static class AnonymousClass1 {
    }

    static {
        int i2 = Native.POINTER_SIZE;
        SIZE = i2;
        if (i2 == 0) {
            throw new Error("Native library not initialized");
        }
        NULL = null;
    }

    public static final Pointer createConstant(long peer) {
        return new Opaque(peer, null);
    }

    public static final Pointer createConstant(int peer) {
        return new Opaque(peer & (-1), null);
    }

    Pointer() {
    }

    public Pointer(long peer) {
        this.peer = peer;
    }

    public Pointer share(long offset) {
        return share(offset, 0L);
    }

    public Pointer share(long offset, long sz) {
        return offset == 0 ? this : new Pointer(this.peer + offset);
    }

    public void clear(long size) {
        setMemory(0L, size, (byte) 0);
    }

    public boolean equals(Object o2) {
        if (o2 == this) {
            return true;
        }
        return o2 != null && (o2 instanceof Pointer) && ((Pointer) o2).peer == this.peer;
    }

    public int hashCode() {
        return (int) ((this.peer >>> 32) + (this.peer & (-1)));
    }

    public long indexOf(long offset, byte value) {
        return Native.indexOf(this.peer + offset, value);
    }

    public void read(long offset, byte[] buf, int index, int length) {
        Native.read(this.peer + offset, buf, index, length);
    }

    public void read(long offset, short[] buf, int index, int length) {
        Native.read(this.peer + offset, buf, index, length);
    }

    public void read(long offset, char[] buf, int index, int length) {
        Native.read(this.peer + offset, buf, index, length);
    }

    public void read(long offset, int[] buf, int index, int length) {
        Native.read(this.peer + offset, buf, index, length);
    }

    public void read(long offset, long[] buf, int index, int length) {
        Native.read(this.peer + offset, buf, index, length);
    }

    public void read(long offset, float[] buf, int index, int length) {
        Native.read(this.peer + offset, buf, index, length);
    }

    public void read(long offset, double[] buf, int index, int length) {
        Native.read(this.peer + offset, buf, index, length);
    }

    public void read(long offset, Pointer[] buf, int index, int length) {
        for (int i2 = 0; i2 < length; i2++) {
            Pointer p2 = getPointer(offset + (i2 * SIZE));
            Pointer oldp = buf[i2 + index];
            if (oldp == null || p2 == null || p2.peer != oldp.peer) {
                buf[i2 + index] = p2;
            }
        }
    }

    public void write(long offset, byte[] buf, int index, int length) {
        Native.write(this.peer + offset, buf, index, length);
    }

    public void write(long offset, short[] buf, int index, int length) {
        Native.write(this.peer + offset, buf, index, length);
    }

    public void write(long offset, char[] buf, int index, int length) {
        Native.write(this.peer + offset, buf, index, length);
    }

    public void write(long offset, int[] buf, int index, int length) {
        Native.write(this.peer + offset, buf, index, length);
    }

    public void write(long offset, long[] buf, int index, int length) {
        Native.write(this.peer + offset, buf, index, length);
    }

    public void write(long offset, float[] buf, int index, int length) {
        Native.write(this.peer + offset, buf, index, length);
    }

    public void write(long offset, double[] buf, int index, int length) {
        Native.write(this.peer + offset, buf, index, length);
    }

    public void write(long bOff, Pointer[] buf, int index, int length) {
        for (int i2 = 0; i2 < length; i2++) {
            setPointer(bOff + (i2 * SIZE), buf[index + i2]);
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:24:0x0086  */
    /* JADX WARN: Removed duplicated region for block: B:36:0x00bb  */
    /* JADX WARN: Removed duplicated region for block: B:44:0x00ec  */
    /* JADX WARN: Removed duplicated region for block: B:52:0x011d  */
    /* JADX WARN: Removed duplicated region for block: B:60:0x014e  */
    /* JADX WARN: Removed duplicated region for block: B:68:0x017f  */
    /* JADX WARN: Removed duplicated region for block: B:76:0x01b0  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x01e1  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    java.lang.Object getValue(long r8, java.lang.Class r10, java.lang.Object r11) throws java.lang.Throwable {
        /*
            Method dump skipped, instructions count: 1075
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.jna.Pointer.getValue(long, java.lang.Class, java.lang.Object):java.lang.Object");
    }

    static Class class$(String x0) throws Throwable {
        try {
            return Class.forName(x0);
        } catch (ClassNotFoundException x1) {
            throw new NoClassDefFoundError().initCause(x1);
        }
    }

    private void getArrayValue(long offset, Object o2, Class cls) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        int length = Array.getLength(o2);
        if (cls == Byte.TYPE) {
            read(offset, (byte[]) o2, 0, length);
            return;
        }
        if (cls == Short.TYPE) {
            read(offset, (short[]) o2, 0, length);
            return;
        }
        if (cls == Character.TYPE) {
            read(offset, (char[]) o2, 0, length);
            return;
        }
        if (cls == Integer.TYPE) {
            read(offset, (int[]) o2, 0, length);
            return;
        }
        if (cls == Long.TYPE) {
            read(offset, (long[]) o2, 0, length);
            return;
        }
        if (cls == Float.TYPE) {
            read(offset, (float[]) o2, 0, length);
            return;
        }
        if (cls == Double.TYPE) {
            read(offset, (double[]) o2, 0, length);
            return;
        }
        if (class$com$sun$jna$Pointer == null) {
            clsClass$ = class$("com.sun.jna.Pointer");
            class$com$sun$jna$Pointer = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$Pointer;
        }
        if (clsClass$.isAssignableFrom(cls)) {
            read(offset, (Pointer[]) o2, 0, length);
            return;
        }
        if (class$com$sun$jna$Structure == null) {
            clsClass$2 = class$("com.sun.jna.Structure");
            class$com$sun$jna$Structure = clsClass$2;
        } else {
            clsClass$2 = class$com$sun$jna$Structure;
        }
        if (clsClass$2.isAssignableFrom(cls)) {
            Structure[] sarray = (Structure[]) o2;
            if (class$com$sun$jna$Structure$ByReference == null) {
                clsClass$4 = class$("com.sun.jna.Structure$ByReference");
                class$com$sun$jna$Structure$ByReference = clsClass$4;
            } else {
                clsClass$4 = class$com$sun$jna$Structure$ByReference;
            }
            if (clsClass$4.isAssignableFrom(cls)) {
                Pointer[] parray = getPointerArray(offset, sarray.length);
                for (int i2 = 0; i2 < sarray.length; i2++) {
                    sarray[i2] = Structure.updateStructureByReference(cls, sarray[i2], parray[i2]);
                }
                return;
            }
            for (int i3 = 0; i3 < sarray.length; i3++) {
                if (sarray[i3] == null) {
                    sarray[i3] = Structure.newInstance(cls);
                }
                sarray[i3].useMemory(this, (int) (offset + (i3 * sarray[i3].size())));
                sarray[i3].read();
            }
            return;
        }
        if (class$com$sun$jna$NativeMapped == null) {
            clsClass$3 = class$("com.sun.jna.NativeMapped");
            class$com$sun$jna$NativeMapped = clsClass$3;
        } else {
            clsClass$3 = class$com$sun$jna$NativeMapped;
        }
        if (clsClass$3.isAssignableFrom(cls)) {
            NativeMapped[] array = (NativeMapped[]) o2;
            NativeMappedConverter tc = NativeMappedConverter.getInstance(cls);
            int size = Native.getNativeSize(o2.getClass(), o2) / array.length;
            for (int i4 = 0; i4 < array.length; i4++) {
                Object value = getValue(offset + (size * i4), tc.nativeType(), array[i4]);
                array[i4] = (NativeMapped) tc.fromNative(value, new FromNativeContext(cls));
            }
            return;
        }
        throw new IllegalArgumentException(new StringBuffer().append("Reading array of ").append((Object) cls).append(" from memory not supported").toString());
    }

    public byte getByte(long offset) {
        return Native.getByte(this.peer + offset);
    }

    public char getChar(long offset) {
        return Native.getChar(this.peer + offset);
    }

    public short getShort(long offset) {
        return Native.getShort(this.peer + offset);
    }

    public int getInt(long offset) {
        return Native.getInt(this.peer + offset);
    }

    public long getLong(long offset) {
        return Native.getLong(this.peer + offset);
    }

    public NativeLong getNativeLong(long offset) {
        return new NativeLong(NativeLong.SIZE == 8 ? getLong(offset) : getInt(offset));
    }

    public float getFloat(long offset) {
        return Native.getFloat(this.peer + offset);
    }

    public double getDouble(long offset) {
        return Native.getDouble(this.peer + offset);
    }

    public Pointer getPointer(long offset) {
        return Native.getPointer(this.peer + offset);
    }

    public ByteBuffer getByteBuffer(long offset, long length) {
        return Native.getDirectByteBuffer(this.peer + offset, length).order(ByteOrder.nativeOrder());
    }

    public String getString(long offset, boolean wide) {
        return Native.getString(this.peer + offset, wide);
    }

    public String getString(long offset) {
        String encoding = System.getProperty("jna.encoding");
        if (encoding != null) {
            long len = indexOf(offset, (byte) 0);
            if (len != -1) {
                if (len > 2147483647L) {
                    throw new OutOfMemoryError(new StringBuffer().append("String exceeds maximum length: ").append(len).toString());
                }
                byte[] data = getByteArray(offset, (int) len);
                try {
                    return new String(data, encoding);
                } catch (UnsupportedEncodingException e2) {
                }
            }
        }
        return getString(offset, false);
    }

    public byte[] getByteArray(long offset, int arraySize) {
        byte[] buf = new byte[arraySize];
        read(offset, buf, 0, arraySize);
        return buf;
    }

    public char[] getCharArray(long offset, int arraySize) {
        char[] buf = new char[arraySize];
        read(offset, buf, 0, arraySize);
        return buf;
    }

    public short[] getShortArray(long offset, int arraySize) {
        short[] buf = new short[arraySize];
        read(offset, buf, 0, arraySize);
        return buf;
    }

    public int[] getIntArray(long offset, int arraySize) {
        int[] buf = new int[arraySize];
        read(offset, buf, 0, arraySize);
        return buf;
    }

    public long[] getLongArray(long offset, int arraySize) {
        long[] buf = new long[arraySize];
        read(offset, buf, 0, arraySize);
        return buf;
    }

    public float[] getFloatArray(long offset, int arraySize) {
        float[] buf = new float[arraySize];
        read(offset, buf, 0, arraySize);
        return buf;
    }

    public double[] getDoubleArray(long offset, int arraySize) {
        double[] buf = new double[arraySize];
        read(offset, buf, 0, arraySize);
        return buf;
    }

    public Pointer[] getPointerArray(long base) {
        List array = new ArrayList();
        int offset = 0;
        Pointer pointer = getPointer(base);
        while (true) {
            Pointer p2 = pointer;
            if (p2 != null) {
                array.add(p2);
                offset += SIZE;
                pointer = getPointer(base + offset);
            } else {
                return (Pointer[]) array.toArray(new Pointer[array.size()]);
            }
        }
    }

    public Pointer[] getPointerArray(long offset, int arraySize) {
        Pointer[] buf = new Pointer[arraySize];
        read(offset, buf, 0, arraySize);
        return buf;
    }

    public String[] getStringArray(long base) {
        return getStringArray(base, -1, false);
    }

    public String[] getStringArray(long base, int length) {
        return getStringArray(base, length, false);
    }

    public String[] getStringArray(long base, boolean wide) {
        return getStringArray(base, -1, wide);
    }

    public String[] getStringArray(long base, int length, boolean wide) {
        List strings = new ArrayList();
        int offset = 0;
        if (length == -1) {
            while (true) {
                Pointer p2 = getPointer(base + offset);
                if (p2 == null) {
                    break;
                }
                String s2 = p2 == null ? null : p2.getString(0L, wide);
                strings.add(s2);
                offset += SIZE;
            }
        } else {
            Pointer p3 = getPointer(base + 0);
            int count = 0;
            while (true) {
                int i2 = count;
                count++;
                if (i2 >= length) {
                    break;
                }
                String s3 = p3 == null ? null : p3.getString(0L, wide);
                strings.add(s3);
                if (count < length) {
                    offset += SIZE;
                    p3 = getPointer(base + offset);
                }
            }
        }
        return (String[]) strings.toArray(new String[strings.size()]);
    }

    void setValue(long offset, Object value, Class type) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        Class clsClass$5;
        Class clsClass$6;
        Class clsClass$7;
        Class clsClass$8;
        Class clsClass$9;
        Class clsClass$10;
        Class clsClass$11;
        Class clsClass$12;
        Class clsClass$13;
        Class clsClass$14;
        Class clsClass$15;
        Class clsClass$16;
        if (type != Boolean.TYPE) {
            if (class$java$lang$Boolean == null) {
                clsClass$ = class$(Constants.BOOLEAN_CLASS);
                class$java$lang$Boolean = clsClass$;
            } else {
                clsClass$ = class$java$lang$Boolean;
            }
            if (type != clsClass$) {
                if (type != Byte.TYPE) {
                    if (class$java$lang$Byte == null) {
                        clsClass$2 = class$("java.lang.Byte");
                        class$java$lang$Byte = clsClass$2;
                    } else {
                        clsClass$2 = class$java$lang$Byte;
                    }
                    if (type != clsClass$2) {
                        if (type != Short.TYPE) {
                            if (class$java$lang$Short == null) {
                                clsClass$3 = class$("java.lang.Short");
                                class$java$lang$Short = clsClass$3;
                            } else {
                                clsClass$3 = class$java$lang$Short;
                            }
                            if (type != clsClass$3) {
                                if (type != Character.TYPE) {
                                    if (class$java$lang$Character == null) {
                                        clsClass$4 = class$("java.lang.Character");
                                        class$java$lang$Character = clsClass$4;
                                    } else {
                                        clsClass$4 = class$java$lang$Character;
                                    }
                                    if (type != clsClass$4) {
                                        if (type != Integer.TYPE) {
                                            if (class$java$lang$Integer == null) {
                                                clsClass$5 = class$(Constants.INTEGER_CLASS);
                                                class$java$lang$Integer = clsClass$5;
                                            } else {
                                                clsClass$5 = class$java$lang$Integer;
                                            }
                                            if (type != clsClass$5) {
                                                if (type != Long.TYPE) {
                                                    if (class$java$lang$Long == null) {
                                                        clsClass$6 = class$("java.lang.Long");
                                                        class$java$lang$Long = clsClass$6;
                                                    } else {
                                                        clsClass$6 = class$java$lang$Long;
                                                    }
                                                    if (type != clsClass$6) {
                                                        if (type != Float.TYPE) {
                                                            if (class$java$lang$Float == null) {
                                                                clsClass$7 = class$("java.lang.Float");
                                                                class$java$lang$Float = clsClass$7;
                                                            } else {
                                                                clsClass$7 = class$java$lang$Float;
                                                            }
                                                            if (type != clsClass$7) {
                                                                if (type != Double.TYPE) {
                                                                    if (class$java$lang$Double == null) {
                                                                        clsClass$8 = class$(Constants.DOUBLE_CLASS);
                                                                        class$java$lang$Double = clsClass$8;
                                                                    } else {
                                                                        clsClass$8 = class$java$lang$Double;
                                                                    }
                                                                    if (type != clsClass$8) {
                                                                        if (class$com$sun$jna$Pointer == null) {
                                                                            clsClass$9 = class$("com.sun.jna.Pointer");
                                                                            class$com$sun$jna$Pointer = clsClass$9;
                                                                        } else {
                                                                            clsClass$9 = class$com$sun$jna$Pointer;
                                                                        }
                                                                        if (type == clsClass$9) {
                                                                            setPointer(offset, (Pointer) value);
                                                                            return;
                                                                        }
                                                                        if (class$java$lang$String == null) {
                                                                            clsClass$10 = class$("java.lang.String");
                                                                            class$java$lang$String = clsClass$10;
                                                                        } else {
                                                                            clsClass$10 = class$java$lang$String;
                                                                        }
                                                                        if (type == clsClass$10) {
                                                                            setPointer(offset, (Pointer) value);
                                                                            return;
                                                                        }
                                                                        if (class$com$sun$jna$WString == null) {
                                                                            clsClass$11 = class$("com.sun.jna.WString");
                                                                            class$com$sun$jna$WString = clsClass$11;
                                                                        } else {
                                                                            clsClass$11 = class$com$sun$jna$WString;
                                                                        }
                                                                        if (type == clsClass$11) {
                                                                            setPointer(offset, (Pointer) value);
                                                                            return;
                                                                        }
                                                                        if (class$com$sun$jna$Structure == null) {
                                                                            clsClass$12 = class$("com.sun.jna.Structure");
                                                                            class$com$sun$jna$Structure = clsClass$12;
                                                                        } else {
                                                                            clsClass$12 = class$com$sun$jna$Structure;
                                                                        }
                                                                        if (clsClass$12.isAssignableFrom(type)) {
                                                                            Structure s2 = (Structure) value;
                                                                            if (class$com$sun$jna$Structure$ByReference == null) {
                                                                                clsClass$16 = class$("com.sun.jna.Structure$ByReference");
                                                                                class$com$sun$jna$Structure$ByReference = clsClass$16;
                                                                            } else {
                                                                                clsClass$16 = class$com$sun$jna$Structure$ByReference;
                                                                            }
                                                                            if (clsClass$16.isAssignableFrom(type)) {
                                                                                setPointer(offset, s2 == null ? null : s2.getPointer());
                                                                                if (s2 != null) {
                                                                                    s2.autoWrite();
                                                                                    return;
                                                                                }
                                                                                return;
                                                                            }
                                                                            s2.useMemory(this, (int) offset);
                                                                            s2.write();
                                                                            return;
                                                                        }
                                                                        if (class$com$sun$jna$Callback == null) {
                                                                            clsClass$13 = class$("com.sun.jna.Callback");
                                                                            class$com$sun$jna$Callback = clsClass$13;
                                                                        } else {
                                                                            clsClass$13 = class$com$sun$jna$Callback;
                                                                        }
                                                                        if (clsClass$13.isAssignableFrom(type)) {
                                                                            setPointer(offset, CallbackReference.getFunctionPointer((Callback) value));
                                                                            return;
                                                                        }
                                                                        if (class$java$nio$Buffer == null) {
                                                                            clsClass$14 = class$("java.nio.Buffer");
                                                                            class$java$nio$Buffer = clsClass$14;
                                                                        } else {
                                                                            clsClass$14 = class$java$nio$Buffer;
                                                                        }
                                                                        if (clsClass$14.isAssignableFrom(type)) {
                                                                            Pointer p2 = value == null ? null : Native.getDirectBufferPointer((Buffer) value);
                                                                            setPointer(offset, p2);
                                                                            return;
                                                                        }
                                                                        if (class$com$sun$jna$NativeMapped == null) {
                                                                            clsClass$15 = class$("com.sun.jna.NativeMapped");
                                                                            class$com$sun$jna$NativeMapped = clsClass$15;
                                                                        } else {
                                                                            clsClass$15 = class$com$sun$jna$NativeMapped;
                                                                        }
                                                                        if (clsClass$15.isAssignableFrom(type)) {
                                                                            NativeMappedConverter tc = NativeMappedConverter.getInstance(type);
                                                                            Class nativeType = tc.nativeType();
                                                                            setValue(offset, tc.toNative(value, new ToNativeContext()), nativeType);
                                                                            return;
                                                                        } else {
                                                                            if (type.isArray()) {
                                                                                setArrayValue(offset, value, type.getComponentType());
                                                                                return;
                                                                            }
                                                                            throw new IllegalArgumentException(new StringBuffer().append("Writing ").append((Object) type).append(" to memory is not supported").toString());
                                                                        }
                                                                    }
                                                                }
                                                                setDouble(offset, value == null ? 0.0d : ((Double) value).doubleValue());
                                                                return;
                                                            }
                                                        }
                                                        setFloat(offset, value == null ? 0.0f : ((Float) value).floatValue());
                                                        return;
                                                    }
                                                }
                                                setLong(offset, value == null ? 0L : ((Long) value).longValue());
                                                return;
                                            }
                                        }
                                        setInt(offset, value == null ? 0 : ((Integer) value).intValue());
                                        return;
                                    }
                                }
                                setChar(offset, value == null ? (char) 0 : ((Character) value).charValue());
                                return;
                            }
                        }
                        setShort(offset, value == null ? (short) 0 : ((Short) value).shortValue());
                        return;
                    }
                }
                setByte(offset, value == null ? (byte) 0 : ((Byte) value).byteValue());
                return;
            }
        }
        setInt(offset, Boolean.TRUE.equals(value) ? -1 : 0);
    }

    private void setArrayValue(long offset, Object value, Class cls) throws Throwable {
        Class clsClass$;
        Class clsClass$2;
        Class clsClass$3;
        Class clsClass$4;
        if (cls == Byte.TYPE) {
            byte[] buf = (byte[]) value;
            write(offset, buf, 0, buf.length);
            return;
        }
        if (cls == Short.TYPE) {
            short[] buf2 = (short[]) value;
            write(offset, buf2, 0, buf2.length);
            return;
        }
        if (cls == Character.TYPE) {
            char[] buf3 = (char[]) value;
            write(offset, buf3, 0, buf3.length);
            return;
        }
        if (cls == Integer.TYPE) {
            int[] buf4 = (int[]) value;
            write(offset, buf4, 0, buf4.length);
            return;
        }
        if (cls == Long.TYPE) {
            long[] buf5 = (long[]) value;
            write(offset, buf5, 0, buf5.length);
            return;
        }
        if (cls == Float.TYPE) {
            float[] buf6 = (float[]) value;
            write(offset, buf6, 0, buf6.length);
            return;
        }
        if (cls == Double.TYPE) {
            double[] buf7 = (double[]) value;
            write(offset, buf7, 0, buf7.length);
            return;
        }
        if (class$com$sun$jna$Pointer == null) {
            clsClass$ = class$("com.sun.jna.Pointer");
            class$com$sun$jna$Pointer = clsClass$;
        } else {
            clsClass$ = class$com$sun$jna$Pointer;
        }
        if (clsClass$.isAssignableFrom(cls)) {
            Pointer[] buf8 = (Pointer[]) value;
            write(offset, buf8, 0, buf8.length);
            return;
        }
        if (class$com$sun$jna$Structure == null) {
            clsClass$2 = class$("com.sun.jna.Structure");
            class$com$sun$jna$Structure = clsClass$2;
        } else {
            clsClass$2 = class$com$sun$jna$Structure;
        }
        if (clsClass$2.isAssignableFrom(cls)) {
            Structure[] sbuf = (Structure[]) value;
            if (class$com$sun$jna$Structure$ByReference == null) {
                clsClass$4 = class$("com.sun.jna.Structure$ByReference");
                class$com$sun$jna$Structure$ByReference = clsClass$4;
            } else {
                clsClass$4 = class$com$sun$jna$Structure$ByReference;
            }
            if (clsClass$4.isAssignableFrom(cls)) {
                Pointer[] buf9 = new Pointer[sbuf.length];
                for (int i2 = 0; i2 < sbuf.length; i2++) {
                    if (sbuf[i2] == null) {
                        buf9[i2] = null;
                    } else {
                        buf9[i2] = sbuf[i2].getPointer();
                        sbuf[i2].write();
                    }
                }
                write(offset, buf9, 0, buf9.length);
                return;
            }
            for (int i3 = 0; i3 < sbuf.length; i3++) {
                if (sbuf[i3] == null) {
                    sbuf[i3] = Structure.newInstance(cls);
                }
                sbuf[i3].useMemory(this, (int) (offset + (i3 * sbuf[i3].size())));
                sbuf[i3].write();
            }
            return;
        }
        if (class$com$sun$jna$NativeMapped == null) {
            clsClass$3 = class$("com.sun.jna.NativeMapped");
            class$com$sun$jna$NativeMapped = clsClass$3;
        } else {
            clsClass$3 = class$com$sun$jna$NativeMapped;
        }
        if (clsClass$3.isAssignableFrom(cls)) {
            NativeMapped[] buf10 = (NativeMapped[]) value;
            NativeMappedConverter tc = NativeMappedConverter.getInstance(cls);
            Class nativeType = tc.nativeType();
            int size = Native.getNativeSize(value.getClass(), value) / buf10.length;
            for (int i4 = 0; i4 < buf10.length; i4++) {
                Object element = tc.toNative(buf10[i4], new ToNativeContext());
                setValue(offset + (i4 * size), element, nativeType);
            }
            return;
        }
        throw new IllegalArgumentException(new StringBuffer().append("Writing array of ").append((Object) cls).append(" to memory not supported").toString());
    }

    public void setMemory(long offset, long length, byte value) {
        Native.setMemory(this.peer + offset, length, value);
    }

    public void setByte(long offset, byte value) {
        Native.setByte(this.peer + offset, value);
    }

    public void setShort(long offset, short value) {
        Native.setShort(this.peer + offset, value);
    }

    public void setChar(long offset, char value) {
        Native.setChar(this.peer + offset, value);
    }

    public void setInt(long offset, int value) {
        Native.setInt(this.peer + offset, value);
    }

    public void setLong(long offset, long value) {
        Native.setLong(this.peer + offset, value);
    }

    public void setNativeLong(long offset, NativeLong value) {
        if (NativeLong.SIZE == 8) {
            setLong(offset, value.longValue());
        } else {
            setInt(offset, value.intValue());
        }
    }

    public void setFloat(long offset, float value) {
        Native.setFloat(this.peer + offset, value);
    }

    public void setDouble(long offset, double value) {
        Native.setDouble(this.peer + offset, value);
    }

    public void setPointer(long offset, Pointer value) {
        Native.setPointer(this.peer + offset, value != null ? value.peer : 0L);
    }

    public void setString(long offset, String value, boolean wide) {
        Native.setString(this.peer + offset, value, wide);
    }

    public void setString(long offset, String value) {
        byte[] data = Native.getBytes(value);
        write(offset, data, 0, data.length);
        setByte(offset + data.length, (byte) 0);
    }

    public String toString() {
        return new StringBuffer().append("native@0x").append(Long.toHexString(this.peer)).toString();
    }

    public static long nativeValue(Pointer p2) {
        return p2.peer;
    }

    public static void nativeValue(Pointer p2, long value) {
        p2.peer = value;
    }

    /* loaded from: JavaFTD2XX.jar:com/sun/jna/Pointer$Opaque.class */
    private static class Opaque extends Pointer {
        private final String MSG;

        Opaque(long x0, AnonymousClass1 x1) {
            this(x0);
        }

        private Opaque(long peer) {
            super(peer);
            this.MSG = new StringBuffer().append("This pointer is opaque: ").append((Object) this).toString();
        }

        @Override // com.sun.jna.Pointer
        public long indexOf(long offset, byte value) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void read(long bOff, byte[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void read(long bOff, char[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void read(long bOff, short[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void read(long bOff, int[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void read(long bOff, long[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void read(long bOff, float[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void read(long bOff, double[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void write(long bOff, byte[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void write(long bOff, char[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void write(long bOff, short[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void write(long bOff, int[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void write(long bOff, long[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void write(long bOff, float[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void write(long bOff, double[] buf, int index, int length) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public byte getByte(long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public char getChar(long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public short getShort(long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public int getInt(long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public long getLong(long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public float getFloat(long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public double getDouble(long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public Pointer getPointer(long bOff) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public String getString(long bOff, boolean wide) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void setByte(long bOff, byte value) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void setChar(long bOff, char value) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void setShort(long bOff, short value) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void setInt(long bOff, int value) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void setLong(long bOff, long value) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void setFloat(long bOff, float value) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void setDouble(long bOff, double value) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void setPointer(long offset, Pointer value) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public void setString(long offset, String value, boolean wide) {
            throw new UnsupportedOperationException(this.MSG);
        }

        @Override // com.sun.jna.Pointer
        public String toString() {
            return new StringBuffer().append("opaque@0x").append(Long.toHexString(this.peer)).toString();
        }
    }
}
