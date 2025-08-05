package org.omg.DynamicAny;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Properties;
import org.omg.CORBA.Any;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.ObjectImpl;
import org.omg.CORBA.portable.ServantObject;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;

/* loaded from: rt.jar:org/omg/DynamicAny/_DynFixedStub.class */
public class _DynFixedStub extends ObjectImpl implements DynFixed {
    public static final Class _opsClass = DynFixedOperations.class;
    private static String[] __ids = {"IDL:omg.org/DynamicAny/DynFixed:1.0", "IDL:omg.org/DynamicAny/DynAny:1.0"};

    @Override // org.omg.DynamicAny.DynFixedOperations
    public String get_value() {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_value", _opsClass);
        try {
            String str = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_value();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return str;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynFixedOperations
    public boolean set_value(String str) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("set_value", _opsClass);
        try {
            boolean z2 = ((DynFixedOperations) servantObject_servant_preinvoke.servant).set_value(str);
            _servant_postinvoke(servantObject_servant_preinvoke);
            return z2;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public TypeCode type() {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("type", _opsClass);
        try {
            TypeCode typeCodeType = ((DynFixedOperations) servantObject_servant_preinvoke.servant).type();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return typeCodeType;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void assign(DynAny dynAny) throws TypeMismatch {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("assign", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).assign(dynAny);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void from_any(Any any) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("from_any", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).from_any(any);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public Any to_any() {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("to_any", _opsClass);
        try {
            Any any = ((DynFixedOperations) servantObject_servant_preinvoke.servant).to_any();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return any;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public boolean equal(DynAny dynAny) {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("equal", _opsClass);
        try {
            boolean zEqual = ((DynFixedOperations) servantObject_servant_preinvoke.servant).equal(dynAny);
            _servant_postinvoke(servantObject_servant_preinvoke);
            return zEqual;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void destroy() {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("destroy", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).destroy();
        } finally {
            _servant_postinvoke(servantObject_servant_preinvoke);
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public DynAny copy() {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("copy", _opsClass);
        try {
            DynAny dynAnyCopy = ((DynFixedOperations) servantObject_servant_preinvoke.servant).copy();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return dynAnyCopy;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_boolean(boolean z2) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("insert_boolean", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).insert_boolean(z2);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_octet(byte b2) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("insert_octet", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).insert_octet(b2);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_char(char c2) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("insert_char", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).insert_char(c2);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_short(short s2) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("insert_short", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).insert_short(s2);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_ushort(short s2) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("insert_ushort", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).insert_ushort(s2);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_long(int i2) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("insert_long", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).insert_long(i2);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_ulong(int i2) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("insert_ulong", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).insert_ulong(i2);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_float(float f2) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("insert_float", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).insert_float(f2);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_double(double d2) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("insert_double", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).insert_double(d2);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_string(String str) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("insert_string", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).insert_string(str);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_reference(Object object) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("insert_reference", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).insert_reference(object);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_typecode(TypeCode typeCode) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("insert_typecode", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).insert_typecode(typeCode);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_longlong(long j2) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("insert_longlong", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).insert_longlong(j2);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_ulonglong(long j2) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("insert_ulonglong", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).insert_ulonglong(j2);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_wchar(char c2) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("insert_wchar", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).insert_wchar(c2);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_wstring(String str) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("insert_wstring", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).insert_wstring(str);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_any(Any any) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("insert_any", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).insert_any(any);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_dyn_any(DynAny dynAny) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("insert_dyn_any", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).insert_dyn_any(dynAny);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_val(Serializable serializable) throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("insert_val", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).insert_val(serializable);
            _servant_postinvoke(servantObject_servant_preinvoke);
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public boolean get_boolean() throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_boolean", _opsClass);
        try {
            boolean z2 = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_boolean();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return z2;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public byte get_octet() throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_octet", _opsClass);
        try {
            byte b2 = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_octet();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return b2;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public char get_char() throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_char", _opsClass);
        try {
            char c2 = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_char();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return c2;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public short get_short() throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_short", _opsClass);
        try {
            short s2 = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_short();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return s2;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public short get_ushort() throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_ushort", _opsClass);
        try {
            short s2 = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_ushort();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return s2;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public int get_long() throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_long", _opsClass);
        try {
            int i2 = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_long();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return i2;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public int get_ulong() throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_ulong", _opsClass);
        try {
            int i2 = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_ulong();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return i2;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public float get_float() throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_float", _opsClass);
        try {
            float f2 = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_float();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return f2;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public double get_double() throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_double", _opsClass);
        try {
            double d2 = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_double();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return d2;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public String get_string() throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_string", _opsClass);
        try {
            String str = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_string();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return str;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public Object get_reference() throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_reference", _opsClass);
        try {
            Object object = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_reference();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return object;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public TypeCode get_typecode() throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_typecode", _opsClass);
        try {
            TypeCode typeCode = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_typecode();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return typeCode;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public long get_longlong() throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_longlong", _opsClass);
        try {
            long j2 = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_longlong();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return j2;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public long get_ulonglong() throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_ulonglong", _opsClass);
        try {
            long j2 = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_ulonglong();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return j2;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public char get_wchar() throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_wchar", _opsClass);
        try {
            char c2 = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_wchar();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return c2;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public String get_wstring() throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_wstring", _opsClass);
        try {
            String str = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_wstring();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return str;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public Any get_any() throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_any", _opsClass);
        try {
            Any any = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_any();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return any;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public DynAny get_dyn_any() throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_dyn_any", _opsClass);
        try {
            DynAny dynAny = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_dyn_any();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return dynAny;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public Serializable get_val() throws TypeMismatch, InvalidValue {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("get_val", _opsClass);
        try {
            Serializable serializable = ((DynFixedOperations) servantObject_servant_preinvoke.servant).get_val();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return serializable;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public boolean seek(int i2) {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("seek", _opsClass);
        try {
            boolean zSeek = ((DynFixedOperations) servantObject_servant_preinvoke.servant).seek(i2);
            _servant_postinvoke(servantObject_servant_preinvoke);
            return zSeek;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void rewind() {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("rewind", _opsClass);
        try {
            ((DynFixedOperations) servantObject_servant_preinvoke.servant).rewind();
        } finally {
            _servant_postinvoke(servantObject_servant_preinvoke);
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public boolean next() {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke(Constants.NEXT, _opsClass);
        try {
            boolean next = ((DynFixedOperations) servantObject_servant_preinvoke.servant).next();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return next;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public int component_count() {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("component_count", _opsClass);
        try {
            int iComponent_count = ((DynFixedOperations) servantObject_servant_preinvoke.servant).component_count();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return iComponent_count;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public DynAny current_component() throws TypeMismatch {
        ServantObject servantObject_servant_preinvoke = _servant_preinvoke("current_component", _opsClass);
        try {
            DynAny dynAnyCurrent_component = ((DynFixedOperations) servantObject_servant_preinvoke.servant).current_component();
            _servant_postinvoke(servantObject_servant_preinvoke);
            return dynAnyCurrent_component;
        } catch (Throwable th) {
            _servant_postinvoke(servantObject_servant_preinvoke);
            throw th;
        }
    }

    @Override // org.omg.CORBA.portable.ObjectImpl
    public String[] _ids() {
        return (String[]) __ids.clone();
    }

    private void readObject(ObjectInputStream objectInputStream) throws IOException {
        String utf = objectInputStream.readUTF();
        ORB orbInit = ORB.init((String[]) null, (Properties) null);
        try {
            _set_delegate(((ObjectImpl) orbInit.string_to_object(utf))._get_delegate());
            orbInit.destroy();
        } catch (Throwable th) {
            orbInit.destroy();
            throw th;
        }
    }

    private void writeObject(ObjectOutputStream objectOutputStream) throws IOException {
        ORB orbInit = ORB.init((String[]) null, (Properties) null);
        try {
            objectOutputStream.writeUTF(orbInit.object_to_string(this));
            orbInit.destroy();
        } catch (Throwable th) {
            orbInit.destroy();
            throw th;
        }
    }
}
