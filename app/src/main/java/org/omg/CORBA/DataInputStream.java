package org.omg.CORBA;

import java.io.Serializable;
import org.omg.CORBA.portable.ValueBase;

/* loaded from: rt.jar:org/omg/CORBA/DataInputStream.class */
public interface DataInputStream extends ValueBase {
    Any read_any();

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

    Object read_Object();

    java.lang.Object read_Abstract();

    Serializable read_Value();

    TypeCode read_TypeCode();

    void read_any_array(AnySeqHolder anySeqHolder, int i2, int i3);

    void read_boolean_array(BooleanSeqHolder booleanSeqHolder, int i2, int i3);

    void read_char_array(CharSeqHolder charSeqHolder, int i2, int i3);

    void read_wchar_array(WCharSeqHolder wCharSeqHolder, int i2, int i3);

    void read_octet_array(OctetSeqHolder octetSeqHolder, int i2, int i3);

    void read_short_array(ShortSeqHolder shortSeqHolder, int i2, int i3);

    void read_ushort_array(UShortSeqHolder uShortSeqHolder, int i2, int i3);

    void read_long_array(LongSeqHolder longSeqHolder, int i2, int i3);

    void read_ulong_array(ULongSeqHolder uLongSeqHolder, int i2, int i3);

    void read_ulonglong_array(ULongLongSeqHolder uLongLongSeqHolder, int i2, int i3);

    void read_longlong_array(LongLongSeqHolder longLongSeqHolder, int i2, int i3);

    void read_float_array(FloatSeqHolder floatSeqHolder, int i2, int i3);

    void read_double_array(DoubleSeqHolder doubleSeqHolder, int i2, int i3);
}
