package com.sun.corba.se.impl.dynamicany;

import com.sun.corba.se.impl.corba.TypeCodeImpl;
import com.sun.corba.se.spi.orb.ORB;
import java.io.Serializable;
import org.omg.CORBA.Any;
import org.omg.CORBA.MARSHAL;
import org.omg.CORBA.Object;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.portable.OutputStream;
import org.omg.DynamicAny.DynAny;
import org.omg.DynamicAny.DynAnyFactoryPackage.InconsistentTypeCode;
import org.omg.DynamicAny.DynAnyPackage.InvalidValue;
import org.omg.DynamicAny.DynAnyPackage.TypeMismatch;

/* loaded from: rt.jar:com/sun/corba/se/impl/dynamicany/DynAnyConstructedImpl.class */
abstract class DynAnyConstructedImpl extends DynAnyImpl {
    protected static final byte REPRESENTATION_NONE = 0;
    protected static final byte REPRESENTATION_TYPECODE = 1;
    protected static final byte REPRESENTATION_ANY = 2;
    protected static final byte REPRESENTATION_COMPONENTS = 4;
    protected static final byte RECURSIVE_UNDEF = -1;
    protected static final byte RECURSIVE_NO = 0;
    protected static final byte RECURSIVE_YES = 1;
    protected static final DynAny[] emptyComponents = new DynAny[0];
    DynAny[] components;
    byte representations;
    byte isRecursive;

    protected abstract boolean initializeComponentsFromAny();

    protected abstract boolean initializeComponentsFromTypeCode();

    private DynAnyConstructedImpl() {
        this(null, (Any) null, false);
    }

    protected DynAnyConstructedImpl(ORB orb, Any any, boolean z2) {
        super(orb, any, z2);
        this.components = emptyComponents;
        this.representations = (byte) 0;
        this.isRecursive = (byte) -1;
        if (this.any != null) {
            this.representations = (byte) 2;
        }
        this.index = 0;
    }

    protected DynAnyConstructedImpl(ORB orb, TypeCode typeCode) {
        super(orb, typeCode);
        this.components = emptyComponents;
        this.representations = (byte) 0;
        this.isRecursive = (byte) -1;
        if (typeCode != null) {
            this.representations = (byte) 1;
        }
        this.index = -1;
    }

    protected boolean isRecursive() {
        if (this.isRecursive == -1) {
            TypeCode typeCodeType = this.any.type();
            if ((typeCodeType instanceof TypeCodeImpl) && ((TypeCodeImpl) typeCodeType).is_recursive()) {
                this.isRecursive = (byte) 1;
            } else {
                this.isRecursive = (byte) 0;
            }
        }
        return this.isRecursive == 1;
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public DynAny current_component() throws TypeMismatch {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index != -1 && checkInitComponents()) {
            return this.components[this.index];
        }
        return null;
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public int component_count() {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (checkInitComponents()) {
            return this.components.length;
        }
        return 0;
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public boolean next() {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (!checkInitComponents()) {
            return false;
        }
        this.index++;
        if (this.index >= 0 && this.index < this.components.length) {
            return true;
        }
        this.index = -1;
        return false;
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public boolean seek(int i2) {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (i2 < 0) {
            this.index = -1;
            return false;
        }
        if (checkInitComponents() && i2 < this.components.length) {
            this.index = i2;
            return true;
        }
        return false;
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void rewind() {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        seek(0);
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyImpl
    protected void clearData() {
        super.clearData();
        this.components = emptyComponents;
        this.index = -1;
        this.representations = (byte) 0;
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyImpl
    protected void writeAny(OutputStream outputStream) {
        checkInitAny();
        super.writeAny(outputStream);
    }

    protected boolean checkInitComponents() {
        if ((this.representations & 4) == 0) {
            if ((this.representations & 2) != 0) {
                if (initializeComponentsFromAny()) {
                    this.representations = (byte) (this.representations | 4);
                    return true;
                }
                return false;
            }
            if ((this.representations & 1) != 0) {
                if (initializeComponentsFromTypeCode()) {
                    this.representations = (byte) (this.representations | 4);
                    return true;
                }
                return false;
            }
            return true;
        }
        return true;
    }

    protected void checkInitAny() {
        if ((this.representations & 2) == 0) {
            if ((this.representations & 4) != 0) {
                if (initializeAnyFromComponents()) {
                    this.representations = (byte) (this.representations | 2);
                }
            } else if ((this.representations & 1) != 0) {
                if (this.representations == 1 && isRecursive()) {
                    return;
                }
                if (initializeComponentsFromTypeCode()) {
                    this.representations = (byte) (this.representations | 4);
                }
                if (initializeAnyFromComponents()) {
                    this.representations = (byte) (this.representations | 2);
                }
            }
        }
    }

    protected boolean initializeAnyFromComponents() throws MARSHAL {
        OutputStream outputStreamCreate_output_stream = this.any.create_output_stream();
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

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public void assign(DynAny dynAny) throws TypeMismatch {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        clearData();
        super.assign(dynAny);
        this.representations = (byte) 2;
        this.index = 0;
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public void from_any(Any any) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        clearData();
        super.from_any(any);
        this.representations = (byte) 2;
        this.index = 0;
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public Any to_any() {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        checkInitAny();
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
        if (!this.any.type().equal(dynAny.type()) || !checkInitComponents()) {
            return false;
        }
        DynAny dynAnyCurrent_component = null;
        try {
            dynAnyCurrent_component = dynAny.current_component();
            for (int i2 = 0; i2 < this.components.length; i2++) {
                if (!dynAny.seek(i2)) {
                    DynAnyUtil.set_current_component(dynAny, dynAnyCurrent_component);
                    return false;
                }
                if (!this.components[i2].equal(dynAny.current_component())) {
                    DynAnyUtil.set_current_component(dynAny, dynAnyCurrent_component);
                    return false;
                }
            }
            DynAnyUtil.set_current_component(dynAny, dynAnyCurrent_component);
            return true;
        } catch (TypeMismatch e2) {
            DynAnyUtil.set_current_component(dynAny, dynAnyCurrent_component);
            return true;
        } catch (Throwable th) {
            DynAnyUtil.set_current_component(dynAny, dynAnyCurrent_component);
            throw th;
        }
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public void destroy() {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.status == 0) {
            this.status = (byte) 2;
            for (int i2 = 0; i2 < this.components.length; i2++) {
                if (this.components[i2] instanceof DynAnyImpl) {
                    ((DynAnyImpl) this.components[i2]).setStatus((byte) 0);
                }
                this.components[i2].destroy();
            }
        }
    }

    @Override // com.sun.corba.se.impl.dynamicany.DynAnyImpl, org.omg.DynamicAny.DynAnyOperations
    public DynAny copy() {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        checkInitAny();
        try {
            return DynAnyUtil.createMostDerivedDynAny(this.any, this.orb, true);
        } catch (InconsistentTypeCode e2) {
            return null;
        }
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_boolean(boolean z2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        dynAnyCurrent_component.insert_boolean(z2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_octet(byte b2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        dynAnyCurrent_component.insert_octet(b2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_char(char c2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        dynAnyCurrent_component.insert_char(c2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_short(short s2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        dynAnyCurrent_component.insert_short(s2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_ushort(short s2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        dynAnyCurrent_component.insert_ushort(s2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_long(int i2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        dynAnyCurrent_component.insert_long(i2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_ulong(int i2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        dynAnyCurrent_component.insert_ulong(i2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_float(float f2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        dynAnyCurrent_component.insert_float(f2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_double(double d2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        dynAnyCurrent_component.insert_double(d2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_string(String str) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        dynAnyCurrent_component.insert_string(str);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_reference(Object object) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        dynAnyCurrent_component.insert_reference(object);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_typecode(TypeCode typeCode) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        dynAnyCurrent_component.insert_typecode(typeCode);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_longlong(long j2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        dynAnyCurrent_component.insert_longlong(j2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_ulonglong(long j2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        dynAnyCurrent_component.insert_ulonglong(j2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_wchar(char c2) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        dynAnyCurrent_component.insert_wchar(c2);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_wstring(String str) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        dynAnyCurrent_component.insert_wstring(str);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_any(Any any) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        dynAnyCurrent_component.insert_any(any);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_dyn_any(DynAny dynAny) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        dynAnyCurrent_component.insert_dyn_any(dynAny);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public void insert_val(Serializable serializable) throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        dynAnyCurrent_component.insert_val(serializable);
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public Serializable get_val() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        return dynAnyCurrent_component.get_val();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public boolean get_boolean() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        return dynAnyCurrent_component.get_boolean();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public byte get_octet() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        return dynAnyCurrent_component.get_octet();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public char get_char() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        return dynAnyCurrent_component.get_char();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public short get_short() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        return dynAnyCurrent_component.get_short();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public short get_ushort() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        return dynAnyCurrent_component.get_ushort();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public int get_long() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        return dynAnyCurrent_component.get_long();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public int get_ulong() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        return dynAnyCurrent_component.get_ulong();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public float get_float() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        return dynAnyCurrent_component.get_float();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public double get_double() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        return dynAnyCurrent_component.get_double();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public String get_string() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        return dynAnyCurrent_component.get_string();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public Object get_reference() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        return dynAnyCurrent_component.get_reference();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public TypeCode get_typecode() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        return dynAnyCurrent_component.get_typecode();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public long get_longlong() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        return dynAnyCurrent_component.get_longlong();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public long get_ulonglong() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        return dynAnyCurrent_component.get_ulonglong();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public char get_wchar() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        return dynAnyCurrent_component.get_wchar();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public String get_wstring() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        return dynAnyCurrent_component.get_wstring();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public Any get_any() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        return dynAnyCurrent_component.get_any();
    }

    @Override // org.omg.DynamicAny.DynAnyOperations
    public DynAny get_dyn_any() throws TypeMismatch, InvalidValue {
        if (this.status == 2) {
            throw this.wrapper.dynAnyDestroyed();
        }
        if (this.index == -1) {
            throw new InvalidValue();
        }
        DynAny dynAnyCurrent_component = current_component();
        if (DynAnyUtil.isConstructedDynAny(dynAnyCurrent_component)) {
            throw new TypeMismatch();
        }
        return dynAnyCurrent_component.get_dyn_any();
    }
}
