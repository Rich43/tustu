package com.sun.corba.se.impl.encoding;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import org.omg.CORBA.Any;
import org.omg.CORBA.Object;
import org.omg.CORBA.Principal;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/MarshalOutputStream.class */
public interface MarshalOutputStream {
    InputStream create_input_stream();

    void write_boolean(boolean z2);

    void write_char(char c2);

    void write_wchar(char c2);

    void write_octet(byte b2);

    void write_short(short s2);

    void write_ushort(short s2);

    void write_long(int i2);

    void write_ulong(int i2);

    void write_longlong(long j2);

    void write_ulonglong(long j2);

    void write_float(float f2);

    void write_double(double d2);

    void write_string(String str);

    void write_wstring(String str);

    void write_boolean_array(boolean[] zArr, int i2, int i3);

    void write_char_array(char[] cArr, int i2, int i3);

    void write_wchar_array(char[] cArr, int i2, int i3);

    void write_octet_array(byte[] bArr, int i2, int i3);

    void write_short_array(short[] sArr, int i2, int i3);

    void write_ushort_array(short[] sArr, int i2, int i3);

    void write_long_array(int[] iArr, int i2, int i3);

    void write_ulong_array(int[] iArr, int i2, int i3);

    void write_longlong_array(long[] jArr, int i2, int i3);

    void write_ulonglong_array(long[] jArr, int i2, int i3);

    void write_float_array(float[] fArr, int i2, int i3);

    void write_double_array(double[] dArr, int i2, int i3);

    void write_Object(Object object);

    void write_TypeCode(TypeCode typeCode);

    void write_any(Any any);

    void write_Principal(Principal principal);

    void write_value(Serializable serializable);

    void start_block();

    void end_block();

    void putEndian();

    void writeTo(OutputStream outputStream) throws IOException;

    byte[] toByteArray();
}
