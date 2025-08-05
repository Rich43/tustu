package org.omg.DynamicAny;

import java.io.Serializable;
import org.omg.CORBA.Any;
import org.omg.CORBA.Object;
import org.omg.CORBA.TypeCode;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;

/* loaded from: rt.jar:org/omg/DynamicAny/DynAnyOperations.class */
public interface DynAnyOperations {
    TypeCode type();

    void assign(DynAny dynAny) throws TypeMismatch;

    void from_any(Any any) throws TypeMismatch, InvalidValue;

    Any to_any();

    boolean equal(DynAny dynAny);

    void destroy();

    DynAny copy();

    void insert_boolean(boolean z2) throws TypeMismatch, InvalidValue;

    void insert_octet(byte b2) throws TypeMismatch, InvalidValue;

    void insert_char(char c2) throws TypeMismatch, InvalidValue;

    void insert_short(short s2) throws TypeMismatch, InvalidValue;

    void insert_ushort(short s2) throws TypeMismatch, InvalidValue;

    void insert_long(int i2) throws TypeMismatch, InvalidValue;

    void insert_ulong(int i2) throws TypeMismatch, InvalidValue;

    void insert_float(float f2) throws TypeMismatch, InvalidValue;

    void insert_double(double d2) throws TypeMismatch, InvalidValue;

    void insert_string(String str) throws TypeMismatch, InvalidValue;

    void insert_reference(Object object) throws TypeMismatch, InvalidValue;

    void insert_typecode(TypeCode typeCode) throws TypeMismatch, InvalidValue;

    void insert_longlong(long j2) throws TypeMismatch, InvalidValue;

    void insert_ulonglong(long j2) throws TypeMismatch, InvalidValue;

    void insert_wchar(char c2) throws TypeMismatch, InvalidValue;

    void insert_wstring(String str) throws TypeMismatch, InvalidValue;

    void insert_any(Any any) throws TypeMismatch, InvalidValue;

    void insert_dyn_any(DynAny dynAny) throws TypeMismatch, InvalidValue;

    void insert_val(Serializable serializable) throws TypeMismatch, InvalidValue;

    boolean get_boolean() throws TypeMismatch, InvalidValue;

    byte get_octet() throws TypeMismatch, InvalidValue;

    char get_char() throws TypeMismatch, InvalidValue;

    short get_short() throws TypeMismatch, InvalidValue;

    short get_ushort() throws TypeMismatch, InvalidValue;

    int get_long() throws TypeMismatch, InvalidValue;

    int get_ulong() throws TypeMismatch, InvalidValue;

    float get_float() throws TypeMismatch, InvalidValue;

    double get_double() throws TypeMismatch, InvalidValue;

    String get_string() throws TypeMismatch, InvalidValue;

    Object get_reference() throws TypeMismatch, InvalidValue;

    TypeCode get_typecode() throws TypeMismatch, InvalidValue;

    long get_longlong() throws TypeMismatch, InvalidValue;

    long get_ulonglong() throws TypeMismatch, InvalidValue;

    char get_wchar() throws TypeMismatch, InvalidValue;

    String get_wstring() throws TypeMismatch, InvalidValue;

    Any get_any() throws TypeMismatch, InvalidValue;

    DynAny get_dyn_any() throws TypeMismatch, InvalidValue;

    Serializable get_val() throws TypeMismatch, InvalidValue;

    boolean seek(int i2);

    void rewind();

    boolean next();

    int component_count();

    DynAny current_component() throws TypeMismatch;
}
