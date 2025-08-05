package com.sun.corba.se.impl.dynamicany;

import com.sun.corba.se.spi.orb.ORB;
import java.io.Serializable;
import org.omg.CORBA.Any;
import org.omg.CORBA.BAD_OPERATION;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.Object;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.InputStream;
import org.omg.CORBA.portable.OutputStream;
import org.omg.DynamicAny.DynAny;
import org.omg.DynamicAny.DynAnyFactoryPackage.InconsistentTypeCode;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;
import org.omg.DynamicAny.DynSequence;

/* loaded from: rt.jar:com/sun/corba/se/impl/dynamicany/DynSequenceImpl.class */
public class DynSequenceImpl extends DynAnyCollectionImpl implements DynSequence {
    @Override // com.sun.corba.se.impl.dynamicany.DynAnyCollectionImpl, org.omg.DynamicAny.DynArrayOperations
    public /* bridge */ /* synthetic */ void set_elements_as_dyn_any(DynAny[] dynAnyArr) throws TypeMismatch, InvalidValue {
        super.set_elements_as_dyn_any(dynAnyArr);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyCollectionImpl, org.omg.DynamicAny.DynArrayOperations
    public /* bridge */ /* synthetic */ DynAny[] get_elements_as_dyn_any() {
        return super.get_elements_as_dyn_any();
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyCollectionImpl, org.omg.DynamicAny.DynArrayOperations
    public /* bridge */ /* synthetic */ void set_elements(Any[] anyArr) throws TypeMismatch, InvalidValue {
        super.set_elements(anyArr);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyCollectionImpl, org.omg.DynamicAny.DynArrayOperations
    public /* bridge */ /* synthetic */ Any[] get_elements() {
        return super.get_elements();
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

    private DynSequenceImpl() {
        this(null, (Any) null, false);
    }

    protected DynSequenceImpl(ORB orb, Any any, boolean z2) {
        super(orb, any, z2);
    }

    protected DynSequenceImpl(ORB orb, TypeCode typeCode) {
        super(orb, typeCode);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl
    protected boolean initializeComponentsFromAny() {
        this.any.type();
        TypeCode contentType = getContentType();
        try {
            InputStream inputStreamCreate_input_stream = this.any.create_input_stream();
            int i2 = inputStreamCreate_input_stream.read_long();
            this.components = new DynAny[i2];
            this.anys = new Any[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                this.anys[i3] = DynAnyUtil.extractAnyFromStream(contentType, inputStreamCreate_input_stream, this.orb);
                try {
                    this.components[i3] = DynAnyUtil.createMostDerivedDynAny(this.anys[i3], this.orb, false);
                } catch (InconsistentTypeCode e2) {
                }
            }
            return true;
        } catch (BAD_OPERATION e3) {
            return false;
        }
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl
    protected boolean initializeComponentsFromTypeCode() {
        this.components = new DynAny[0];
        this.anys = new Any[0];
        return true;
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyConstructedImpl
    protected boolean initializeAnyFromComponents() throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = this.any.create_output_stream();
        outputStreamCreate_output_stream.write_long(this.components.length);
        for (int i2 = 0; i2 < this.components.length; i2++) {
            if (this.components[i2] instanceof DynAnyImpl) {
                ((DynAnyImpl) this.components[i2]).writeAny(outputStreamCreate_output_stream);
            } else {
                this.components[i2].to_any().write_value(outputStreamCreate_output_stream);
            }
        }
        this.any.read_value(outputStreamCreate_output_stream.create_input_stream(), this.any.type());
        return true;
    }

    @Override // org.omg.DynamicAny.DynSequenceOperations
    public int get_length() {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (checkInitComponents()) {
            return this.components.length;
        }
        return 0;
    }

    @Override // org.omg.DynamicAny.DynSequenceOperations
    public void set_length(int i2) throws InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        int bound = getBound();
        if (bound > 0 && i2 > bound) {
            throw new InvalidValue();
        }
        checkInitComponents();
        int length = this.components.length;
        if (i2 > length) {
            DynAny[] dynAnyArr = new DynAny[i2];
            Any[] anyArr = new Any[i2];
            System.arraycopy(this.components, 0, dynAnyArr, 0, length);
            System.arraycopy(this.anys, 0, anyArr, 0, length);
            this.components = dynAnyArr;
            this.anys = anyArr;
            TypeCode contentType = getContentType();
            for (int i3 = length; i3 < i2; i3++) {
                createDefaultComponentAt(i3, contentType);
            }
            if (this.index == -1) {
                this.index = length;
                return;
            }
            return;
        }
        if (i2 < length) {
            DynAny[] dynAnyArr2 = new DynAny[i2];
            Any[] anyArr2 = new Any[i2];
            System.arraycopy(this.components, 0, dynAnyArr2, 0, i2);
            System.arraycopy(this.anys, 0, anyArr2, 0, i2);
            this.components = dynAnyArr2;
            this.anys = anyArr2;
            if (i2 == 0 || this.index >= i2) {
                this.index = -1;
                return;
            }
            return;
        }
        if (this.index == -1 && i2 > 0) {
            this.index = 0;
        }
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyCollectionImpl
    protected void checkValue(Object[] objArr) throws InvalidValue {
        if (objArr == null || objArr.length == 0) {
            clearData();
            this.index = -1;
            return;
        }
        this.index = 0;
        int bound = getBound();
        if (bound > 0 && objArr.length > bound) {
            throw new InvalidValue();
        }
    }
}
