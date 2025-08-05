package org.omg.CORBA;

import java.io.Serializable;
import org.omg.CORBA.DynAnyPackage.Invalid;
import org.omg.CORBA.DynAnyPackage.InvalidValue;
import org.omg.CORBA.DynAnyPackage.TypeMismatch;

@Deprecated
/* loaded from: rt.jar:org/omg/CORBA/DynAny.class */
public interface DynAny extends Object {
    TypeCode type();

    void assign(DynAny dynAny) throws Invalid;

    void from_any(Any any) throws Invalid;

    Any to_any() throws Invalid;

    void destroy();

    DynAny copy();

    void insert_boolean(boolean z2) throws InvalidValue;

    void insert_octet(byte b2) throws InvalidValue;

    void insert_char(char c2) throws InvalidValue;

    void insert_short(short s2) throws InvalidValue;

    void insert_ushort(short s2) throws InvalidValue;

    void insert_long(int i2) throws InvalidValue;

    void insert_ulong(int i2) throws InvalidValue;

    void insert_float(float f2) throws InvalidValue;

    void insert_double(double d2) throws InvalidValue;

    void insert_string(String str) throws InvalidValue;

    void insert_reference(Object object) throws InvalidValue;

    void insert_typecode(TypeCode typeCode) throws InvalidValue;

    void insert_longlong(long j2) throws InvalidValue;

    void insert_ulonglong(long j2) throws InvalidValue;

    void insert_wchar(char c2) throws InvalidValue;

    void insert_wstring(String str) throws InvalidValue;

    void insert_any(Any any) throws InvalidValue;

    void insert_val(Serializable serializable) throws InvalidValue;

    Serializable get_val() throws TypeMismatch;

    boolean get_boolean() throws TypeMismatch;

    byte get_octet() throws TypeMismatch;

    char get_char() throws TypeMismatch;

    short get_short() throws TypeMismatch;

    short get_ushort() throws TypeMismatch;

    int get_long() throws TypeMismatch;

    int get_ulong() throws TypeMismatch;

    float get_float() throws TypeMismatch;

    double get_double() throws TypeMismatch;

    String get_string() throws TypeMismatch;

    Object get_reference() throws TypeMismatch;

    TypeCode get_typecode() throws TypeMismatch;

    long get_longlong() throws TypeMismatch;

    long get_ulonglong() throws TypeMismatch;

    char get_wchar() throws TypeMismatch;

    String get_wstring() throws TypeMismatch;

    Any get_any() throws TypeMismatch;

    DynAny current_component();

    boolean next();

    boolean seek(int i2);

    void rewind();
}
