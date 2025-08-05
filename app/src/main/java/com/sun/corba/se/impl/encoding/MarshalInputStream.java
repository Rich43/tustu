package com.sun.corba.se.impl.encoding;

import java.io.Serializable;
import org.omg.CORBA.Any;
import org.omg.CORBA.Object;
import org.omg.CORBA.Principal;
import org.omg.CORBA.TypeCode;

/* loaded from: rt.jar:com/sun/corba/se/impl/encoding/MarshalInputStream.class */
public interface MarshalInputStream {
    boolean read_boolean();

    char read_char();

    char read_wchar();

    byte read_octet();

    short read_short();

    short read_ushort();

    int read_long();

    int read_ulong();

    long read_longlong();

    long read_ulonglong();

    float read_float();

    double read_double();

    String read_string();

    String read_wstring();

    void read_boolean_array(boolean[] zArr, int i2, int i3);

    void read_char_array(char[] cArr, int i2, int i3);

    void read_wchar_array(char[] cArr, int i2, int i3);

    void read_octet_array(byte[] bArr, int i2, int i3);

    void read_short_array(short[] sArr, int i2, int i3);

    void read_ushort_array(short[] sArr, int i2, int i3);

    void read_long_array(int[] iArr, int i2, int i3);

    void read_ulong_array(int[] iArr, int i2, int i3);

    void read_longlong_array(long[] jArr, int i2, int i3);

    void read_ulonglong_array(long[] jArr, int i2, int i3);

    void read_float_array(float[] fArr, int i2, int i3);

    void read_double_array(double[] dArr, int i2, int i3);

    Object read_Object();

    TypeCode read_TypeCode();

    Any read_any();

    Principal read_Principal();

    Object read_Object(Class cls);

    Serializable read_value() throws Exception;

    void consumeEndian();

    int getPosition();

    void mark(int i2);

    void reset();

    void performORBVersionSpecificInit();

    void resetCodeSetConverters();
}
