package org.omg.CORBA.portable;

import java.io.IOException;
import java.math.BigDecimal;
import org.omg.CORBA.Any;
import org.omg.CORBA.Context;
import org.omg.CORBA.ContextList;
import org.omg.CORBA.NO_IMPLEMENT;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.Principal;
import org.omg.CORBA.TypeCode;

/* loaded from: rt.jar:org/omg/CORBA/portable/OutputStream.class */
public abstract class OutputStream extends java.io.OutputStream {
    public abstract InputStream create_input_stream();

    public abstract void write_boolean(boolean z2);

    public abstract void write_char(char c2);

    public abstract void write_wchar(char c2);

    public abstract void write_octet(byte b2);

    public abstract void write_short(short s2);

    public abstract void write_ushort(short s2);

    public abstract void write_long(int i2);

    public abstract void write_ulong(int i2);

    public abstract void write_longlong(long j2);

    public abstract void write_ulonglong(long j2);

    public abstract void write_float(float f2);

    public abstract void write_double(double d2);

    public abstract void write_string(String str);

    public abstract void write_wstring(String str);

    public abstract void write_boolean_array(boolean[] zArr, int i2, int i3);

    public abstract void write_char_array(char[] cArr, int i2, int i3);

    public abstract void write_wchar_array(char[] cArr, int i2, int i3);

    public abstract void write_octet_array(byte[] bArr, int i2, int i3);

    public abstract void write_short_array(short[] sArr, int i2, int i3);

    public abstract void write_ushort_array(short[] sArr, int i2, int i3);

    public abstract void write_long_array(int[] iArr, int i2, int i3);

    public abstract void write_ulong_array(int[] iArr, int i2, int i3);

    public abstract void write_longlong_array(long[] jArr, int i2, int i3);

    public abstract void write_ulonglong_array(long[] jArr, int i2, int i3);

    public abstract void write_float_array(float[] fArr, int i2, int i3);

    public abstract void write_double_array(double[] dArr, int i2, int i3);

    public abstract void write_Object(Object object);

    public abstract void write_TypeCode(TypeCode typeCode);

    public abstract void write_any(Any any);

    @Deprecated
    public void write_Principal(Principal principal) {
        throw new NO_IMPLEMENT();
    }

    @Override // java.io.OutputStream
    public void write(int i2) throws IOException {
        throw new NO_IMPLEMENT();
    }

    public void write_fixed(BigDecimal bigDecimal) {
        throw new NO_IMPLEMENT();
    }

    public void write_Context(Context context, ContextList contextList) {
        throw new NO_IMPLEMENT();
    }

    public ORB orb() {
        throw new NO_IMPLEMENT();
    }
}
