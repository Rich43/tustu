package org.omg.CORBA;

import java.io.Serializable;
import java.math.BigDecimal;
import org.omg.CORBA.portable.IDLEntity;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.CORBA.portable.Streamable;

/* loaded from: rt.jar:org/omg/CORBA/Any.class */
public abstract class Any implements IDLEntity {
    public abstract boolean equal(Any any);

    public abstract TypeCode type();

    public abstract void type(TypeCode typeCode);

    public abstract void read_value(InputStream inputStream, TypeCode typeCode) throws MARSHAL;

    public abstract void write_value(OutputStream outputStream);

    public abstract OutputStream create_output_stream();

    public abstract InputStream create_input_stream();

    public abstract short extract_short() throws BAD_OPERATION;

    public abstract void insert_short(short s2);

    public abstract int extract_long() throws BAD_OPERATION;

    public abstract void insert_long(int i2);

    public abstract long extract_longlong() throws BAD_OPERATION;

    public abstract void insert_longlong(long j2);

    public abstract short extract_ushort() throws BAD_OPERATION;

    public abstract void insert_ushort(short s2);

    public abstract int extract_ulong() throws BAD_OPERATION;

    public abstract void insert_ulong(int i2);

    public abstract long extract_ulonglong() throws BAD_OPERATION;

    public abstract void insert_ulonglong(long j2);

    public abstract float extract_float() throws BAD_OPERATION;

    public abstract void insert_float(float f2);

    public abstract double extract_double() throws BAD_OPERATION;

    public abstract void insert_double(double d2);

    public abstract boolean extract_boolean() throws BAD_OPERATION;

    public abstract void insert_boolean(boolean z2);

    public abstract char extract_char() throws BAD_OPERATION;

    public abstract void insert_char(char c2) throws DATA_CONVERSION;

    public abstract char extract_wchar() throws BAD_OPERATION;

    public abstract void insert_wchar(char c2);

    public abstract byte extract_octet() throws BAD_OPERATION;

    public abstract void insert_octet(byte b2);

    public abstract Any extract_any() throws BAD_OPERATION;

    public abstract void insert_any(Any any);

    public abstract Object extract_Object() throws BAD_OPERATION;

    public abstract void insert_Object(Object object);

    public abstract Serializable extract_Value() throws BAD_OPERATION;

    public abstract void insert_Value(Serializable serializable);

    public abstract void insert_Value(Serializable serializable, TypeCode typeCode) throws MARSHAL;

    public abstract void insert_Object(Object object, TypeCode typeCode) throws BAD_PARAM;

    public abstract String extract_string() throws BAD_OPERATION;

    public abstract void insert_string(String str) throws MARSHAL, DATA_CONVERSION;

    public abstract String extract_wstring() throws BAD_OPERATION;

    public abstract void insert_wstring(String str) throws MARSHAL;

    public abstract TypeCode extract_TypeCode() throws BAD_OPERATION;

    public abstract void insert_TypeCode(TypeCode typeCode);

    @Deprecated
    public Principal extract_Principal() throws BAD_OPERATION {
        throw new NO_IMPLEMENT();
    }

    @Deprecated
    public void insert_Principal(Principal principal) {
        throw new NO_IMPLEMENT();
    }

    public Streamable extract_Streamable() throws BAD_INV_ORDER {
        throw new NO_IMPLEMENT();
    }

    public void insert_Streamable(Streamable streamable) {
        throw new NO_IMPLEMENT();
    }

    public BigDecimal extract_fixed() {
        throw new NO_IMPLEMENT();
    }

    public void insert_fixed(BigDecimal bigDecimal) {
        throw new NO_IMPLEMENT();
    }

    public void insert_fixed(BigDecimal bigDecimal, TypeCode typeCode) throws BAD_INV_ORDER {
        throw new NO_IMPLEMENT();
    }
}
