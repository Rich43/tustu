package com.sun.corba.se.impl.dynamicany;

import com.sun.corba.se.spi.orb.ORB;
import java.io.Serializable;
import org.omg.CORBA.Any;
import org.omg.CORBA.DATA_CONVERSION;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.Object;
import org.omg.CORBA.TypeCode;
import org.omg.DynamicAny.DynAny;
import org.omg.DynamicAny.DynAnyFactoryPackage.InconsistentTypeCode;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;

/* loaded from: rt.jar:com/sun/corba/se/impl/dynamicany/DynAnyBasicImpl.class */
public class DynAnyBasicImpl extends DynAnyImpl {
    @Override // com.sun.corba.se.impl.dynamicany.DynAnyImpl
    public /* bridge */ /* synthetic */ String[] _ids() {
        return super._ids();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public /* bridge */ /* synthetic */ TypeCode type() {
        return super.type();
    }

    private DynAnyBasicImpl() {
        this(null, (Any) null, false);
    }

    protected DynAnyBasicImpl(ORB orb, Any any, boolean z2) {
        super(orb, any, z2);
        this.index = -1;
    }

    protected DynAnyBasicImpl(ORB orb, TypeCode typeCode) {
        super(orb, typeCode);
        this.index = -1;
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public void assign(DynAny dynAny) throws TypeMismatch {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        super.assign(dynAny);
        this.index = -1;
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public void from_any(Any any) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        super.from_any(any);
        this.index = -1;
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public Any to_any() {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        return DynAnyUtil.copy(this.any, this.orb);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public boolean equal(DynAny dynAny) {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (dynAny == this) {
            return true;
        }
        if (!this.any.type().equal(dynAny.type())) {
            return false;
        }
        return this.any.equal(getAny(dynAny));
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public void destroy() {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.status == 0) {
            this.status = (byte) 2;
        }
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public DynAny copy() {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        try {
            return DynAnyUtil.createMostDerivedDynAny(this.any, this.orb, true);
        } catch (InconsistentTypeCode e2) {
            return null;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public DynAny current_component() throws TypeMismatch {
        return null;
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public int component_count() {
        return 0;
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public boolean next() {
        return false;
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public boolean seek(int i2) {
        return false;
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void rewind() {
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_boolean(boolean z2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 8) {
            throw new TypeMismatch();
        }
        this.any.insert_boolean(z2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_octet(byte b2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 10) {
            throw new TypeMismatch();
        }
        this.any.insert_octet(b2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_char(char c2) throws TypeMismatch, InvalidValue, DATA_CONVERSION {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 9) {
            throw new TypeMismatch();
        }
        this.any.insert_char(c2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_short(short s2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 2) {
            throw new TypeMismatch();
        }
        this.any.insert_short(s2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_ushort(short s2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 4) {
            throw new TypeMismatch();
        }
        this.any.insert_ushort(s2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_long(int i2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 3) {
            throw new TypeMismatch();
        }
        this.any.insert_long(i2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_ulong(int i2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 5) {
            throw new TypeMismatch();
        }
        this.any.insert_ulong(i2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_float(float f2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 6) {
            throw new TypeMismatch();
        }
        this.any.insert_float(f2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_double(double d2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 7) {
            throw new TypeMismatch();
        }
        this.any.insert_double(d2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_string(String str) throws MARSHAL, TypeMismatch, InvalidValue, DATA_CONVERSION {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 18) {
            throw new TypeMismatch();
        }
        if (str == null) {
            throw new InvalidValue();
        }
        if (this.any.type().length() > 0 && this.any.type().length() < str.length()) {
            throw new InvalidValue();
        }
        this.any.insert_string(str);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_reference(Object object) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 14) {
            throw new TypeMismatch();
        }
        this.any.insert_Object(object);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_typecode(TypeCode typeCode) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 12) {
            throw new TypeMismatch();
        }
        this.any.insert_TypeCode(typeCode);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_longlong(long j2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 23) {
            throw new TypeMismatch();
        }
        this.any.insert_longlong(j2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_ulonglong(long j2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 24) {
            throw new TypeMismatch();
        }
        this.any.insert_ulonglong(j2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_wchar(char c2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 26) {
            throw new TypeMismatch();
        }
        this.any.insert_wchar(c2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_wstring(String str) throws MARSHAL, TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 27) {
            throw new TypeMismatch();
        }
        if (str == null) {
            throw new InvalidValue();
        }
        if (this.any.type().length() > 0 && this.any.type().length() < str.length()) {
            throw new InvalidValue();
        }
        this.any.insert_wstring(str);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_any(Any any) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 11) {
            throw new TypeMismatch();
        }
        this.any.insert_any(any);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_dyn_any(DynAny dynAny) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 11) {
            throw new TypeMismatch();
        }
        this.any.insert_any(dynAny.to_any());
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_val(Serializable serializable) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        int iValue = this.any.type().kind().value();
        if (iValue != 29 && iValue != 30) {
            throw new TypeMismatch();
        }
        this.any.insert_Value(serializable);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public Serializable get_val() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        int iValue = this.any.type().kind().value();
        if (iValue != 29 && iValue != 30) {
            throw new TypeMismatch();
        }
        return this.any.extract_Value();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public boolean get_boolean() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 8) {
            throw new TypeMismatch();
        }
        return this.any.extract_boolean();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public byte get_octet() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 10) {
            throw new TypeMismatch();
        }
        return this.any.extract_octet();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public char get_char() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 9) {
            throw new TypeMismatch();
        }
        return this.any.extract_char();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public short get_short() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 2) {
            throw new TypeMismatch();
        }
        return this.any.extract_short();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public short get_ushort() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 4) {
            throw new TypeMismatch();
        }
        return this.any.extract_ushort();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public int get_long() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 3) {
            throw new TypeMismatch();
        }
        return this.any.extract_long();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public int get_ulong() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 5) {
            throw new TypeMismatch();
        }
        return this.any.extract_ulong();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public float get_float() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 6) {
            throw new TypeMismatch();
        }
        return this.any.extract_float();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public double get_double() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 7) {
            throw new TypeMismatch();
        }
        return this.any.extract_double();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public String get_string() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 18) {
            throw new TypeMismatch();
        }
        return this.any.extract_string();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public Object get_reference() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 14) {
            throw new TypeMismatch();
        }
        return this.any.extract_Object();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public TypeCode get_typecode() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 12) {
            throw new TypeMismatch();
        }
        return this.any.extract_TypeCode();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public long get_longlong() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 23) {
            throw new TypeMismatch();
        }
        return this.any.extract_longlong();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public long get_ulonglong() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 24) {
            throw new TypeMismatch();
        }
        return this.any.extract_ulonglong();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public char get_wchar() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 26) {
            throw new TypeMismatch();
        }
        return this.any.extract_wchar();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public String get_wstring() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 27) {
            throw new TypeMismatch();
        }
        return this.any.extract_wstring();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public Any get_any() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 11) {
            throw new TypeMismatch();
        }
        return this.any.extract_any();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public DynAny get_dyn_any() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.any.type().kind().value() != 11) {
            throw new TypeMismatch();
        }
        try {
            return DynAnyUtil.createMostDerivedDynAny(this.any.extract_any(), this.orb, true);
        } catch (InconsistentTypeCode e2) {
            return null;
        }
    }
}
