package com.sun.corba.se.impl.dynamicany;

import com.sun.corba.se.spi.orb.ORB;
import java.io.Serializable;
import org.omg.CORBA.Any;
import org.omg.CORBA.Object;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.DynamicAny.DynAny;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;
import org.omg.DynamicAny.DynValue;
import org.omg.DynamicAny.NameDynAnyPair;
import org.omg.DynamicAny.NameValuePair;

/* loaded from: rt.jar:com/sun/corba/se/impl/dynamicany/DynValueImpl.class */
public class DynValueImpl extends DynValueCommonImpl implements DynValue {
    @Override // com.sun.corba.se.impl.dynamicany.DynValueCommonImpl, com.sun.corba.se.impl.dynamicany.DynAnyComplexImpl, org.omg.DynamicAny.DynStructOperations
    public /* bridge */ /* synthetic */ void set_members_as_dyn_any(NameDynAnyPair[] nameDynAnyPairArr) throws TypeMismatch, InvalidValue {
        super.set_members_as_dyn_any(nameDynAnyPairArr);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynValueCommonImpl, com.sun.corba.se.impl.dynamicany.DynAnyComplexImpl, org.omg.DynamicAny.DynStructOperations
    public /* bridge */ /* synthetic */ void set_members(NameValuePair[] nameValuePairArr) throws TypeMismatch, InvalidValue {
        super.set_members(nameValuePairArr);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynValueCommonImpl
    public /* bridge */ /* synthetic */ NameDynAnyPair[] get_members_as_dyn_any() throws InvalidValue {
        return super.get_members_as_dyn_any();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynValueCommonImpl
    public /* bridge */ /* synthetic */ NameValuePair[] get_members() throws InvalidValue {
        return super.get_members();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynValueCommonImpl, org.omg.DynamicAny.DynValueCommonOperations
    public /* bridge */ /* synthetic */ void set_to_value() {
        super.set_to_value();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynValueCommonImpl, org.omg.DynamicAny.DynValueCommonOperations
    public /* bridge */ /* synthetic */ void set_to_null() {
        super.set_to_null();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynValueCommonImpl, org.omg.DynamicAny.DynValueCommonOperations
    public /* bridge */ /* synthetic */ boolean is_null() {
        return super.is_null();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyComplexImpl, org.omg.DynamicAny.DynStructOperations
    public /* bridge */ /* synthetic */ TCKind current_member_kind() throws TypeMismatch, InvalidValue {
        return super.current_member_kind();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyComplexImpl, org.omg.DynamicAny.DynStructOperations
    public /* bridge */ /* synthetic */ String current_member_name() throws TypeMismatch, InvalidValue {
        return super.current_member_name();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ DynAny get_dyn_any() throws TypeMismatch, InvalidValue {
        return super.get_dyn_any();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ Any get_any() throws TypeMismatch, InvalidValue {
        return super.get_any();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ String get_wstring() throws TypeMismatch, InvalidValue {
        return super.get_wstring();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ char get_wchar() throws TypeMismatch, InvalidValue {
        return super.get_wchar();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ long get_ulonglong() throws TypeMismatch, InvalidValue {
        return super.get_ulonglong();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ long get_longlong() throws TypeMismatch, InvalidValue {
        return super.get_longlong();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ TypeCode get_typecode() throws TypeMismatch, InvalidValue {
        return super.get_typecode();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ Object get_reference() throws TypeMismatch, InvalidValue {
        return super.get_reference();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ String get_string() throws TypeMismatch, InvalidValue {
        return super.get_string();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ double get_double() throws TypeMismatch, InvalidValue {
        return super.get_double();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ float get_float() throws TypeMismatch, InvalidValue {
        return super.get_float();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ int get_ulong() throws TypeMismatch, InvalidValue {
        return super.get_ulong();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ int get_long() throws TypeMismatch, InvalidValue {
        return super.get_long();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ short get_ushort() throws TypeMismatch, InvalidValue {
        return super.get_ushort();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ short get_short() throws TypeMismatch, InvalidValue {
        return super.get_short();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ char get_char() throws TypeMismatch, InvalidValue {
        return super.get_char();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ byte get_octet() throws TypeMismatch, InvalidValue {
        return super.get_octet();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ boolean get_boolean() throws TypeMismatch, InvalidValue {
        return super.get_boolean();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ Serializable get_val() throws TypeMismatch, InvalidValue {
        return super.get_val();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_val(Serializable serializable) throws TypeMismatch, InvalidValue {
        super.insert_val(serializable);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_dyn_any(DynAny dynAny) throws TypeMismatch, InvalidValue {
        super.insert_dyn_any(dynAny);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_any(Any any) throws TypeMismatch, InvalidValue {
        super.insert_any(any);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_wstring(String str) throws TypeMismatch, InvalidValue {
        super.insert_wstring(str);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_wchar(char c2) throws TypeMismatch, InvalidValue {
        super.insert_wchar(c2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_ulonglong(long j2) throws TypeMismatch, InvalidValue {
        super.insert_ulonglong(j2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_longlong(long j2) throws TypeMismatch, InvalidValue {
        super.insert_longlong(j2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_typecode(TypeCode typeCode) throws TypeMismatch, InvalidValue {
        super.insert_typecode(typeCode);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_reference(Object object) throws TypeMismatch, InvalidValue {
        super.insert_reference(object);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_string(String str) throws TypeMismatch, InvalidValue {
        super.insert_string(str);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_double(double d2) throws TypeMismatch, InvalidValue {
        super.insert_double(d2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_float(float f2) throws TypeMismatch, InvalidValue {
        super.insert_float(f2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_ulong(int i2) throws TypeMismatch, InvalidValue {
        super.insert_ulong(i2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_long(int i2) throws TypeMismatch, InvalidValue {
        super.insert_long(i2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_ushort(short s2) throws TypeMismatch, InvalidValue {
        super.insert_ushort(s2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_short(short s2) throws TypeMismatch, InvalidValue {
        super.insert_short(s2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_char(char c2) throws TypeMismatch, InvalidValue {
        super.insert_char(c2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_octet(byte b2) throws TypeMismatch, InvalidValue {
        super.insert_octet(b2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void insert_boolean(boolean z2) throws TypeMismatch, InvalidValue {
        super.insert_boolean(z2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ DynAny copy() {
        return super.copy();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void destroy() {
        super.destroy();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ boolean equal(DynAny dynAny) {
        return super.equal(dynAny);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ Any to_any() {
        return super.to_any();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void from_any(Any any) throws TypeMismatch, InvalidValue {
        super.from_any(any);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void assign(DynAny dynAny) throws TypeMismatch {
        super.assign(dynAny);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ void rewind() {
        super.rewind();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ boolean seek(int i2) {
        return super.seek(i2);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ boolean next() {
        return super.next();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ int component_count() {
        return super.component_count();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ DynAny current_component() throws TypeMismatch {
        return super.current_component();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyImpl
    public /* bridge */ /* synthetic */ String[] _ids() {
        return super._ids();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ TypeCode type() {
        return super.type();
    }

    private DynValueImpl() {
        this(null, (Any) null, false);
    }

    protected DynValueImpl(ORB orb, Any any, boolean z2) {
        super(orb, any, z2);
    }

    protected DynValueImpl(ORB orb, TypeCode typeCode) {
        super(orb, typeCode);
    }
}
